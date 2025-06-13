package vu.oop.passwordmanager.db;

import java.sql.*;
import java.util.ArrayList;

import vu.oop.passwordmanager.util.HelperDomainObject;

/**
 * ApiDB class provides methods to interact with an SQLite database for managing user credentials.
 * It allows creating tables, inserting, updating, deleting, and retrieving data from the database.
 * This class implements AutoCloseable to ensure the database connection is closed properly.
 * <p>
 * It is designed to be used with a single user, where the username and password are provided
 * during instantiation.
 * </p>
 * @author Dovydas Kelečius
 * Contact: kelecius.dovydas@gmail.com
 * @version 1.5
 * @since 2025-04-23
 * This class is part of the vu.oop.passwordmanager.db package.
 * * Note: This class requires the SQLite JDBC driver to be included in the project dependencies.
 */
public class ApiDB implements AutoCloseable {
    // SQLite database file path
    private static final String DB_URL = "jdbc:sqlite:credentials.db";
    private String USER = ""; 
    private String PASS = ""; 
    private Connection conn;

    public ApiDB() {
        // Default constructor, connection will be established by ApiDB(String, String)
    }

    /**
     * Constructor to initialize the database connection with user credentials.
     * In this context, USER and PASS are expected to be the Base64 encoded
     * SHA-256 hash of the master password, which will be used to identify
     * the user's specific password table and for the 'users' table entry.
     *
     * @param USER The Base64 encoded SHA-256 hash of the master password (used as username for table naming).
     * @param PASS The Base64 encoded SHA-256 hash of the master password (used as password for 'users' table).
     * @throws SQLException If an SQL error occurs during connection.
     */
    public ApiDB(String USER, String PASS) throws SQLException {
        // Store user and pass as instance variables
        this.USER = USER;
        this.PASS = PASS;
        System.out.println("[DEBUG] User (Base64 Hash): " + this.USER);

        try {
            // Establish a connection to the SQLite database
            this.conn = DriverManager.getConnection(DB_URL);
            if (this.conn != null) {
                System.out.println("[DEBUG] Database connected successfully.");
            }
        }
        catch (SQLException e) {
            System.err.println("[DEBUG] Database connection error:");
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Helper method to sanitize a Base64 string for use as an SQL table name.
     * Replaces problematic characters ('+', '/', '=') with underscores.
     *
     * @param input The Base64 encoded string (e.g., master password hash).
     * @return A sanitized string suitable for an SQL table name.
     */
    private String getSanitizedTableName(String input) {
        // Replace characters that are problematic in unquoted SQL identifiers
        // Base64 uses '+', '/', and '=' (padding). Replace them all with underscores.
        String sanitized = input.replace('+', '_')
                                .replace('/', '_')
                                .replace('=', '_');
        System.out.println("[DEBUG] Sanitized '" + input + "' to '" + sanitized + "' for table name.");
        return sanitized;
    }

    /**
     * Creates the necessary tables for the user in the database.
     * This method now uses the instance variables this.USER and this.PASS
     * which are expected to be the Base64 encoded SHA-256 hash of the master password.
     * It creates a fixed "users" table and a dynamic "sanitized_hash_pass" table.
     *
     * @throws SQLException If an SQL error occurs during table creation.
     */
    public void createTABLES() throws SQLException {
        // Sanitize the USER string for use in dynamic table names
        String sanitizedUserHashTableName = getSanitizedTableName(this.USER);

        // Fixed table name for central users table
        final String createUsersTABLE =
            "CREATE TABLE IF NOT EXISTS \"users\" (" +
            "user_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "user_name TEXT NOT NULL UNIQUE," +
            "user_password TEXT NOT NULL UNIQUE);";

        // Dynamic table for user's passwords, named after the sanitized master password hash
        final String createUser_PasswordsTABLE = String.format(
            "CREATE TABLE IF NOT EXISTS \"%s_pass\" (" +
            "password_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
            "entry_name TEXT," +
            "domain_name TEXT NOT NULL," +
            "domain_username TEXT NOT NULL," +
            "domain_password TEXT NOT NULL);", sanitizedUserHashTableName);

        System.out.println("[DEBUG] Creating tables for user and user_pass.");

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createUsersTABLE); // Execute creation of the fixed "users" table
            System.out.println("[DEBUG] Central 'users' table created or already exists.");


            String insertUserSQL = "INSERT INTO \"users\"(user_name, user_password) VALUES (?, ?)";
            try (PreparedStatement pstmt = conn.prepareStatement(insertUserSQL)) {
                pstmt.setString(1, this.USER);
                pstmt.setString(2, this.PASS);
                pstmt.executeUpdate();
                System.out.println("[DEBUG] User '" + this.USER + "' inserted into 'users' table.");
            }

            stmt.executeUpdate(createUser_PasswordsTABLE);

        }
        catch (SQLException e) {
            if (e.getErrorCode() == 19) { // SQLite UNIQUE constraint violation
                System.err.println("[DEBUG] User '" + this.USER + "' already exists, tables likely exist. Error: " + e.getErrorCode());
                if (e.getMessage().contains("UNIQUE constraint failed: users.user_name")) {
                    System.err.println("[DEBUG] User already exists in 'users' table. Skipping insert.");
                }
                else {
                    System.err.println("[DEBUG] Error creating tables or inserting user: " + e.getMessage());
                }
                throw e;
            }
            else {
                System.err.println("[DEBUG] Unmanaged error during table creation. Error: " + e.getErrorCode());
                e.printStackTrace();
                throw e; // Re-throw for other critical errors
            }
        }
    }

    /**
     * Retrieves all rows from the specified table.
     * The TABLE parameter is now expected to be the Base64 encoded SHA-256 hash
     * which will be sanitized to form the actual user-specific password table name.
     *
     * @param TABLE The Base64 encoded SHA-256 hash (used to derive the table name).
     * @return An ArrayList of HelperDomainObject containing the retrieved data.
     * @throws SQLException If an SQL error occurs.
     */
    public ArrayList<HelperDomainObject> getTABLE(String TABLE) throws SQLException {
        ArrayList<HelperDomainObject> domainObjects = new ArrayList<>();
        String sanitizedTableName = getSanitizedTableName(TABLE);

        // If requesting the "users" table, do not sanitize and use a different mapping
        boolean isUsersTable = "users".equals(TABLE);

        String selectSQL = isUsersTable
            ? "SELECT * FROM \"users\""
            : String.format("SELECT * FROM \"%s\"", sanitizedTableName);

        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(selectSQL);

            while (rs.next()) {
                if (isUsersTable) {
                    // users table: user_id, user_name, user_password
                    Integer userId = rs.getInt("user_id");
                    String userName = rs.getString("user_name");
                    String userPassword = rs.getString("user_password");
                    HelperDomainObject domainObject = new HelperDomainObject(userId, null, userName, userPassword, null);
                    //System.out.printf("[DEBUG] User Object: %s%n", domainObject.toString());
                    domainObjects.add(domainObject);
                } else {
                    // password table: password_id, entry_name, domain_name, domain_username, domain_password
                    Integer indexPassword = rs.getInt("password_id");
                    String entryName = rs.getString("entry_name");
                    String domainName = rs.getString("domain_name");
                    String domainUsername = rs.getString("domain_username");
                    String domainPassword = rs.getString("domain_password");

                    HelperDomainObject domainObject = new HelperDomainObject(indexPassword, entryName, domainName, domainUsername, domainPassword);
                    System.out.printf("[DEBUG] Domain Object: %s%n", domainObject.toString());
                    domainObjects.add(domainObject);
                }
            }
        } catch (SQLException e) {
            System.err.println("[DEBUG] An SQL exception occurred during or after using ApiDB (getTABLE for " + (isUsersTable ? "users" : sanitizedTableName + "_pass") + "):");
            e.printStackTrace();
            throw e;
        }

        System.out.println("[DEBUG] Finished retrieving data from table: " + (isUsersTable ? "users" : sanitizedTableName + "_pass"));
        return domainObjects;
    }

    /**
     * Populates the specified table with values.
     * The TABLE parameter is now expected to be the Base64 encoded SHA-256 hash
     * which will be sanitized to form the actual user-specific password table name.
     *
     * @param TABLE The Base64 encoded SHA-256 hash (used to derive the table name).
     * @param columns The names of the columns to insert values into.
     * @param values The values to insert into the columns.
     * @throws SQLException If an SQL error occurs.
     */
    public void populateTABLE(String TABLE, String[] columns, String[] values) throws SQLException {
        if (columns.length != values.length) {
            throw new IllegalArgumentException("Columns and values array length must match.");
        }

        // Sanitize the TABLE string for use in table names
        String sanitizedTableName = getSanitizedTableName(TABLE);

        StringBuilder columnsPart = new StringBuilder();
        StringBuilder valuesPlaceholders = new StringBuilder();

        for (int i = 0; i < columns.length; i++) {
            columnsPart.append(columns[i]);
            valuesPlaceholders.append("?");
            if (i < columns.length - 1) {
                columnsPart.append(", ");
                valuesPlaceholders.append(", ");
            }
        }

        // Quote the table name for insertion
        final String sql = String.format(
            "INSERT INTO \"%s\" (%s) VALUES(%s);",
            sanitizedTableName, columnsPart.toString(), valuesPlaceholders.toString()
        );

        System.out.println("[DEBUG] " + sql);

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            for (int i = 0; i < values.length; i++) {
                pstmt.setString(i + 1, values[i]);
            }
            pstmt.executeUpdate();
            System.out.println("[DEBUG] Data inserted into \"" + sanitizedTableName + "_pass\".");
        } catch (SQLException e) {
            System.err.println("[DEBUG] An SQL exception occurred during or after using ApiDB (populateTABLE for " + sanitizedTableName + "_pass):");
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Removes a row from the specified table.
     *
     * @param TABLE The Base64 encoded SHA-256 hash (used to derive the table name).
     * @param idColumn The name of the ID column to identify the row.
     * @param index The value of the ID column for the row to remove.
     * @throws SQLException If an SQL error occurs.
     */
    public void removeTABLEValue(String TABLE, String idColumn, Integer index) throws SQLException {
        
        String sanitizedTableName = getSanitizedTableName(TABLE);

        
        final String deleteTABLE = String.format("DELETE FROM \"%s\" WHERE %s = ?;", sanitizedTableName, idColumn);

        try (PreparedStatement pstmt = conn.prepareStatement(deleteTABLE)) { // Use PreparedStatement
            pstmt.setInt(1, index);
            System.out.println("[DEBUG] Deleting from TABLE \"" + sanitizedTableName + "\" where " + idColumn + " = " + index);
            pstmt.executeUpdate();
            System.out.println("[DEBUG] Row removed successfully.");
        }
        catch (SQLException e) {
            System.err.println("[DEBUG] An SQL exception occurred during or after using ApiDB (removeTABLEValue for " + sanitizedTableName + "_pass):");
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Updates a row in the specified table.
     *
     * @param TABLE The Base64 encoded SHA-256 hash (used to derive the table name).
     * @param idColumn The name of the ID column to identify the row.
     * @param index The value of the ID column for the row to update.
     * @param columns The names of the columns to update.
     * @param values The new values for the columns.
     * @throws SQLException If an SQL error occurs.
     */
    public void updateTABLEValue(String TABLE, String idColumn, Integer index, String[] columns, String[] values) throws SQLException {
        if (columns.length != values.length) {
            throw new IllegalArgumentException("Columns and values array length must match.");
        }
        // Sanitize the TABLE string for use in table names
        String sanitizedTableName = getSanitizedTableName(TABLE);

        StringBuilder setClause = new StringBuilder();
        for (int i = 0; i < columns.length; i++) {
            setClause.append(String.format("%s = ?", columns[i]));
            if (i < columns.length - 1) {
                setClause.append(", ");
            }
        }

        if (setClause.length() == 0) {
            System.out.println("[DEBUG] No fields to update for password_id: " + index + " in " + sanitizedTableName);
            return;
        }

        final String updateTABLE = String.format(
            "UPDATE \"%s\" SET %s WHERE %s = ?;", sanitizedTableName, setClause.toString(), idColumn);

        try (PreparedStatement pstmt = conn.prepareStatement(updateTABLE)) {
            int paramIndex = 1;
            for (String val : values) {
                pstmt.setString(paramIndex++, val); // Set values for update columns
            }
            pstmt.setInt(paramIndex++, index); // Set value for the WHERE clause
            System.out.println("[DEBUG] Updating TABLE \"" + sanitizedTableName + " for " + idColumn + " = " + index);
            pstmt.executeUpdate();
            System.out.println("[DEBUG] Row updated successfully.");
        }
        catch (SQLException e) {
            System.err.println("[DEBUG] An SQL exception occurred during or after using ApiDB (updateTABLEValue for " + sanitizedTableName + ":");
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Returns the current database connection.
     *
     * @return The Connection object representing the database connection.
     */
    @SuppressWarnings("exports")
    public Connection getConnection() {
        return this.conn;
    }

    /**
     * Closes the database connection.
     *
     * @throws SQLException If an SQL error occurs during closing.
     */
    @Override
    public void close() throws SQLException {
        if (this.conn != null && !this.conn.isClosed()) {
            try {
                this.conn.close();
                System.out.println("[DEBUG] Database connection closed.");
            }
            catch (SQLException e) {
                System.err.println("[DEBUG] Error closing database connection:");
                e.printStackTrace();
                throw e;
            }
        }
    }
}

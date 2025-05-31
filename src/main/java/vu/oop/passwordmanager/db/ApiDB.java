package vu.oop.passwordmanager.db;

import java.sql.*;
import java.util.ArrayList;

import vu.oop.passwordmanager.util.HelperDomainObject;

/**
 * ApiDB class provides methods to interact with an SQLite database for managing user credentials.
 * It allows creating tables, inserting, updating, deleting, and retrieving data from the database.
 * This class implements AutoCloseable to ensure the database connection is closed properly.
 * * <p>
 * * It is designed to be used with a single user, where the username and password are provided
 * during instantiation.
 * * <p>
 * * Example usage:
 * * <pre>
 * * ApiDB db = new ApiDB("username", "password");
 * * * db.createTABLES("username", "password");
 * * * db.populateTABLE("username_pass", new String[]{"domain_name", "domain_username", "domain_password"},
 * * *                   new String[]{"example.com", "user", "pass"});
 * * * ArrayList&lt;HelperDomainObject&gt; domains = db.getTABLE("username_pass");
 * * * db.updateTABLEValue("username_pass", "password_id", 1,
 * * *                     new String[]{"domain_password"}, new String[]{"newpass"});
 * * * db.removeTABLEValue("username_pass", "password_id", 1);
 * * * db.close();
 * * * </pre>
 * @author Dovydas Kelečius
 * Contact: kelecius.dovydas@gmail.com
 * @version 1.2
 * @since 2025-04-23
 * This class is part of the vu.oop.passwordmanager.db package.
 * * * Note: This class requires the SQLite JDBC driver to be included in the project dependencies.
 */
public class ApiDB implements AutoCloseable {
    // SQLite database file path
    private static final String DB_URL = "jdbc:sqlite:credentials.db";
    private String USER = "";
    private String PASS = "";
    private Connection conn;

    public ApiDB() {}

    /**
     * Constructor to initialize the database connection with user credentials.
     *
     * @param USER The username for the database connection.
     * @param PASS The password for the database connection.
     * @throws SQLException If an SQL error occurs during connection.
     */
    public ApiDB(String USER, String PASS) throws SQLException {
        // Store user and pass as instance variables
        this.USER = USER;
        this.PASS = PASS;

        System.out.println("[DEBUG] User: " + this.USER);

        try {
            this.conn = DriverManager.getConnection(DB_URL, this.USER, this.PASS);
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
     * Creates the necessary tables for the user in the database.
     *
     * @param USER The username for which to create the tables.
     * @param PASS The password for the user.
     * @throws SQLException If an SQL error occurs during table creation.
     */
    public void createTABLES(String USER, String PASS) throws SQLException {
        final String createUsersTABLE = 
        "CREATE TABLE IF NOT EXISTS users" +
        "(user_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
        "user_name TEXT NOT NULL UNIQUE," + 
        "user_password TEXT NOT NULL UNIQUE);";
        final String createUser_PasswordsTABLE = String.format(
        "CREATE TABLE IF NOT EXISTS %s_pass " +
        "(password_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
        "domain_name TEXT NOT NULL," +
        "domain_username TEXT NOT NULL," +
        "domain_password TEXT NOT NULL);", USER);

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createUsersTABLE);

            stmt.executeUpdate(String.format("INSERT INTO users(user_name, user_password) " +
                                             "VALUES  (\"%s\", \"%s\")", USER, PASS));

            stmt.executeUpdate(createUser_PasswordsTABLE);
        }
        catch (SQLException e) {
            if (e.getErrorCode()==19) {
                System.err.println("[DEBUG] User already exists, try again. Error: " + e.getErrorCode());
                throw e;
            }
            else {
                System.err.println("[DEBUG] Unmanaged error. Error: " + e.getErrorCode());
                e.printStackTrace();
                SQLException nextEx = e.getNextException();
                while (nextEx != null) {
                    System.err.println("[DEBUG] Chained Exception:");
                    nextEx.printStackTrace();
                    nextEx = nextEx.getNextException();
                    throw e;
                }
            }
        }
    }
    
    /**
     * Retrieves all rows from the specified table.
     *
     * @param TABLE The name of the table to retrieve data from.
     * @return An ArrayList of HelperDomainObject containing the retrieved data.
     * @throws SQLException If an SQL error occurs.
     */
    public ArrayList<HelperDomainObject> getTABLE(String TABLE) throws SQLException {
            ArrayList<HelperDomainObject> domainObjects = new ArrayList<>();                 
        try(Statement stmt = conn.createStatement();) {
            ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM %s", TABLE));

            // Iterate through the data in the result set and insert into arraylist.
            while (rs.next()) {
                Integer indexPassword = rs.getInt("password_id");
                String domainName = rs.getString("domain_name");
                String domainUsername = rs.getString("domain_username");
                String domainPassword = rs.getString("domain_password");

                // Create a new HelperDomainObject and add it to the list
                HelperDomainObject domainObject = new HelperDomainObject(indexPassword, domainName, domainUsername, domainPassword);
                System.out.printf("[DEBUG] Domain Object: %s%n", domainObject.toString());
                domainObjects.add(domainObject);
            
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        System.out.println("[DEBUG] Finished retrieving data from table: " + TABLE);
        return domainObjects;
    }

    /**
     * Populates the specified table with values.
     *
     * @param TABLE The name of the table to populate.
     * @param columns The names of the columns to insert values into.
     * @param values The values to insert into the columns.
     * @throws SQLException If an SQL error occurs.
     */
    public void populateTABLE(String TABLE, String[] columns, String[] values) throws SQLException {
        if (columns.length != values.length) {
            throw new IllegalArgumentException("Columns and values array length must match.");
        }

        StringBuilder columnsPart = new StringBuilder();
        StringBuilder valuesPart = new StringBuilder();

        for (int i = 0; i < columns.length; i++) {
            columnsPart.append(columns[i]);
            valuesPart.append("\"").append(values[i]).append("\"");
            if (i < columns.length - 1) {
                columnsPart.append(", ");
                valuesPart.append(", ");
            }
        }

        final String sql = String.format(
            "INSERT INTO %s (%s) VALUES(%s);",
            TABLE, columnsPart.toString(), valuesPart.toString()
        );

        System.err.println("[DEBUG] " + sql);

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(sql);
        } catch (SQLException e) {
            throw e;
        }
    }

    /**
     * Removes a row from the specified table.
     *
     * @param TABLE The name of the table to remove from.
     * @param idColumn The name of the ID column to identify the row.
     * @param index The value of the ID column for the row to remove.
     * @throws SQLException If an SQL error occurs.
     */
    public void removeTABLEValue(String TABLE, String idColumn, Integer index) throws SQLException {
        final String deleteTABLE = String.format("DELETE FROM %s WHERE %s = %d;", TABLE, idColumn, index);

        try (Statement stmt = conn.createStatement()) {
            System.err.println("[DEBUG] Deleting from TABLE");
            stmt.executeUpdate(deleteTABLE);
        }
        catch (SQLException e) {
            throw e;
        }
    }

    /**
     * Updates a row in the specified table.
     *
     * @param TABLE The name of the table to update.
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
        StringBuilder setClause = new StringBuilder();
        // Build the SET clause for the SQL UPDATE statement
        for (int i = 0; i < columns.length; i++) {
            setClause.append(String.format("%s = \"%s\"", columns[i], values[i]));
            if (i < columns.length - 1) {
                setClause.append(", ");
            }
        }
        final String updateTABLE = String.format(
            "UPDATE %s SET %s WHERE %s = %d;", TABLE, setClause.toString(), idColumn, index);

        try (Statement stmt = conn.createStatement()) {
            System.err.println("[DEBUG] Updating TABLE");
            stmt.executeUpdate(updateTABLE);
        }
        catch (SQLException e) {
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
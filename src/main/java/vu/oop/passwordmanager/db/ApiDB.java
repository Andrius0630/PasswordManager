package vu.oop.passwordmanager.db;

import java.sql.*;
import java.util.ArrayList;
import vu.oop.passwordmanager.db.HelperDomainObject;

public class ApiDB implements AutoCloseable {
    // SQLite database file path
    private static final String DB_URL = "jdbc:sqlite:credentials.db";
    private String USER = "";
    private String PASS = "";
    private Connection conn;

    public ApiDB() {}

    public ApiDB(String USER, String PASS) throws SQLException {
        // Store user and pass as instance variables
        this.USER = USER;
        this.PASS = PASS;

        HelperDomainObject domainObject = new HelperDomainObject(null, "", "", "");

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
    
    public ArrayList<HelperDomainObject> getTABLE(String TABLE) throws SQLException {
            ArrayList<HelperDomainObject> domainObjects = new ArrayList<>();                 
        try(Statement stmt = conn.createStatement();) {
            ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM %s", TABLE));
            ResultSetMetaData rsmd = rs.getMetaData();

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

    public void populateTABLE(String TABLE, String domain_name, String domain_username, String domain_password) throws SQLException {
        final String populateTABLE = String.format(
        "INSERT INTO %s_pass (domain_name,domain_username,domain_password) " +
        "VALUES(\"%s\", \"%s\", \"%s\");", USER, domain_name, domain_username, domain_password);

        System.err.println(populateTABLE);

        try (Statement stmt = conn.createStatement()) {
            System.err.println("[DEBUG] Populating TABLE");
            stmt.executeUpdate(populateTABLE);
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public void removeTABLEValue(String TABLE, Integer index) throws SQLException {
        final String deleteTABLE = String.format("DELETE FROM %s WHERE password_id = %d;", TABLE, index);

        try (Statement stmt = conn.createStatement()) {
            System.err.println("[DEBUG] Deleting from TABLE");
            stmt.executeUpdate(deleteTABLE);
        }
        catch (SQLException e) {
            throw e;
        }
    }

    public void updateTABLEValue(String TABLE, Integer index, String domain_name, String domain_username, String domain_password) throws SQLException {
        final String updateTABLE = String.format(
        "UPDATE %s SET domain_name = \"%s\", domain_username = \"%s\", domain_password = \"%s\" " +
        "WHERE password_id = %d;", TABLE, domain_name, domain_username, domain_password, index);

        try (Statement stmt = conn.createStatement()) {
            System.err.println("[DEBUG] Updating TABLE");
            stmt.executeUpdate(updateTABLE);
        }
        catch (SQLException e) {
            throw e;
        }
    }

    // Method to get the connection if needed elsewhere
    @SuppressWarnings("exports")
    public Connection getConnection() {
        return this.conn;
    }

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
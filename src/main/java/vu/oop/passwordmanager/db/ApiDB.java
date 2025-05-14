package vu.oop.passwordmanager.db;

import java.sql.*;

public class ApiDB implements AutoCloseable {
    // SQLite database file path
    private static final String DB_URL = "jdbc:sqlite:credentials.db";
    private String USER = "";
    private String PASS = "";
    private Connection conn;

    public ApiDB() {}

    public ApiDB(String USER, String PASS) {
        // Store user and pass as instance variables
        this.USER = USER;
        this.PASS = PASS;

        System.out.println("User: " + this.USER);

        try {
            this.conn = DriverManager.getConnection(DB_URL, this.USER, this.PASS);
            if (this.conn != null) {
                System.out.println("Database connected successfully.");
            }
        } catch (SQLException e) {
            System.err.println("Database connection error:");
            e.printStackTrace();
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
        "(user_id INTEGER," +
        "password_id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
        "domain_name TEXT NOT NULL," +
        "domain_username TEXT NOT NULL," +
        "domain_password TEXT NOT NULL," +
        "FOREIGN KEY (user_id) REFERENCES users(user_id));", USER);

        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(createUsersTABLE);

            stmt.executeUpdate(String.format("INSERT INTO users(user_name, user_password) " +
                                             "VALUES  (\"%s\", \"%s\")", USER, PASS));

            stmt.executeUpdate(createUser_PasswordsTABLE);
          }
          catch (SQLException e) {
            e.printStackTrace();
            SQLException nextEx = e.getNextException();
            while (nextEx != null) {
                System.err.println("Chained Exception:");
                nextEx.printStackTrace();
                nextEx = nextEx.getNextException();
            }
        }
    }
    
    public void getTABLE(String TABLE) {
        try(Statement stmt = conn.createStatement();) {
            ResultSet rs = stmt.executeQuery(String.format("SELECT * FROM %s", TABLE));
            while (rs.next()) {
               System.out.print("\nuserid: " + rs.getString("user_id"));
               System.out.print(", username: " + rs.getString("user_name"));
               System.out.print(", userpass: " + rs.getString("user_password"));
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
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
                System.out.println("\nDatabase connection closed.");
            } catch (SQLException e) {
                System.err.println("Error closing database connection:");
                e.printStackTrace();
                throw e; // Re-throw the exception so calling code knows closing failed
            }
        }
    }
    
}
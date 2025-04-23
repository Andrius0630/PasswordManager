package vu.oop.passwordmanager.db;

import java.sql.*;

public class OpenDB {

// SQLite database file path
static final String DB_URL = "jdbc:sqlite:credentials.db";
static final String USER = "";
static final String PASS = "";
static final String QUERY = "SELECT * FROM users";
static final String QUERY_TWO = "SELECT domain_name, domain_username, domain_password FROM users_passwords WHERE user_id = &1";

public static void main(String[] args) {
// Open a connection
   try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(QUERY);) {
         while (rs.next()) {
            System.out.print("\nuserid: " + rs.getString("user_id"));
            System.out.print(", username: " + rs.getString("user_name"));
            System.out.print(", userpass: " + rs.getString("user_password"));
      }
   } catch (SQLException e) {
      e.printStackTrace();
   }
}
}
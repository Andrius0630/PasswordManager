package vu.oop.passwordmanager.db;

import java.sql.*;

public class OpenDB {

   static final String DB_URL = "jdbc:mysql://localhost:3306/mktest";
   static final String USER = "root";
   static final String PASS = "password";
   static final String QUERY = "SELECT * FROM creds";

   public static void main(String[] args) {
      // Open a connection
      try(Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery(QUERY);) {
         // Extract data from result set
         while (rs.next()) {
            // Retrieve by column name
            System.out.print("userid: " + rs.getString("userid"));
            System.out.print(", username: " + rs.getString("username"));
            System.out.println(", userpass: " + rs.getFloat("userpass"));
         }
      } catch (SQLException e) {
         e.printStackTrace();
      } 
   }
}

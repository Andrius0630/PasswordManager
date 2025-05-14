package vu.oop.passwordmanager.db;

import java.sql.SQLException;

public class OpenDB {

   public static void main(String[] args) {
      String USER = "user1";
      String PASS = "user1";

      try (ApiDB db = new ApiDB(USER, PASS)) {
         if (db.getConnection() != null) {
             System.out.println("ApiDB instance created and connected.");
             
             db.createTABLES(USER, PASS);
             db.getTABLE("users");

         }
         else {
              System.err.println("ApiDB connection failed upon creation.");
         }
     }
     catch (SQLException e) {
         System.err.println("An SQL exception occurred during or after using ApiDB:");
         e.printStackTrace();
     }
     catch (Exception e) {
          System.err.println("An unexpected exception occurred:");
          e.printStackTrace();
     }
   }
}
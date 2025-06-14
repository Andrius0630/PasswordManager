package vu.oop.passwordmanager.util;

import vu.oop.passwordmanager.db.ApiDB;

import java.sql.SQLException;
import java.util.ArrayList;

public class HelperDB {

    private static String usernameLogged;
    private static String passwordLogged;
    private static int userID;

    public static void saveValidUserCredentialsToMemory(String encodedUsername, String encodedPassword, int newUserID) {
        usernameLogged = encodedUsername;
        passwordLogged = encodedPassword;
        userID = newUserID;
    }

    public static int isUserExist(String encodedUsername, String encodedPassword) {
        if (encodedUsername.isBlank() || encodedPassword.isBlank())
            throw new IllegalArgumentException();
        try {
            try (ApiDB db = new ApiDB(encodedUsername, encodedPassword)) {
                if (db.getConnection() != null) {
                    ArrayList<HelperDomainObject> users = db.getTABLE("users");
                    for (HelperDomainObject user : users) {
                        if (user.getDomainName().equals(encodedUsername) && user.getDomainUsername().equals(encodedPassword))
                            return user.getIndex();
                    }
                } else {
                    System.err.println("[DEBUG] ApiDB connection failed upon creation.");
                }
            } catch (SQLException e) {
                return 0;
            } catch (Exception e) {
                System.err.println("[DEBUG] An unexpected exception occurred:");
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static ArrayList<HelperDomainObject> retrieveUserData() {
        if (usernameLogged == null || passwordLogged == null)
            throw new IllegalStateException();
        try {
            try (ApiDB db = new ApiDB(usernameLogged, passwordLogged)) {
                if (db.getConnection() != null) {

                    return db.getTABLE(usernameLogged + "_pass");

                } else {
                    System.err.println("[DEBUG] ApiDB connection failed upon creation.");
                }
            } catch (SQLException e) {
                System.err.println("[DEBUG] An SQL exception occurred during or after using ApiDB:");
                if (e.getErrorCode()==19) {
                    // CONTROLLER CODE TO INFORM USER OF ERROR [ERROR: NOT UNIQUE] // FRONTEND TODO
                    // ... //
                    // ... //
                } else e.printStackTrace();
            } catch (Exception e) {
                System.err.println("[DEBUG] An unexpected exception occurred:");
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static void saveUpdatedEntry(String name, String username, String password, String website, int index) {
        if (usernameLogged == null || passwordLogged == null)
            throw new IllegalStateException();
        try {
            try (ApiDB db = new ApiDB(usernameLogged, passwordLogged)) {
                if (db.getConnection() != null) {

                    db.updateTABLEValue(
                            usernameLogged + "_pass",                                  // Table to update
                            "password_id",                              // Column where the ID is stored
                            index,                                             // ID of the row to update
                            new String[] {"entry_name", "domain_name", "domain_username", "domain_password"},     // Columns to update
                            new String[] {name, website, username, password}       // New values for the columns
                    );

                } else {
                    System.err.println("[DEBUG] ApiDB connection failed upon creation.");
                }
            } catch (SQLException e) {
                System.err.println("[DEBUG] An SQL exception occurred during or after using ApiDB:");
                if (e.getErrorCode()==19) {
                    // CONTROLLER CODE TO INFORM USER OF ERROR [ERROR: NOT UNIQUE] // FRONTEND TODO
                    // ... //
                    // ... //
                } else e.printStackTrace();
            } catch (Exception e) {
                System.err.println("[DEBUG] An unexpected exception occurred:");
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addNewEntry(String name, String username, String password, String website) {
        if (usernameLogged == null || passwordLogged == null)
            throw new IllegalStateException();
        try {
            try (ApiDB db = new ApiDB(usernameLogged, passwordLogged)) {
                if (db.getConnection() != null) {

                    db.populateTABLE(usernameLogged + "_pass",
                            new String[] {"entry_name", "domain_name", "domain_username", "domain_password"},
                            new String[] {name, website, password, username}
                    );

                } else {
                    System.err.println("[DEBUG] ApiDB connection failed upon creation.");
                }
            } catch (SQLException e) {
                System.err.println("[DEBUG] An SQL exception occurred during or after using ApiDB:");
                if (e.getErrorCode()==19) {
                    // CONTROLLER CODE TO INFORM USER OF ERROR [ERROR: NOT UNIQUE] // FRONTEND TODO
                    // ... //
                    // ... //
                } else e.printStackTrace();
            } catch (Exception e) {
                System.err.println("[DEBUG] An unexpected exception occurred:");
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteEntry(int index) {
        if (usernameLogged == null || passwordLogged == null)
            throw new IllegalStateException();
        try {
            try (ApiDB db = new ApiDB(usernameLogged, passwordLogged)) {
                if (db.getConnection() != null) {

                    db.removeTABLEValue(usernameLogged + "_pass", "password_id", index);

                } else {
                    System.err.println("[DEBUG] ApiDB connection failed upon creation.");
                }
            } catch (SQLException e) {
                System.err.println("[DEBUG] An SQL exception occurred during or after using ApiDB:");
                if (e.getErrorCode()==19) {
                    // CONTROLLER CODE TO INFORM USER OF ERROR [ERROR: NOT UNIQUE] // FRONTEND TODO
                    // ... //
                    // ... //
                } else e.printStackTrace();
            } catch (Exception e) {
                System.err.println("[DEBUG] An unexpected exception occurred:");
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void deleteUser() {
        if (usernameLogged == null || passwordLogged == null)
            throw new IllegalStateException();
        try {
            try (ApiDB db = new ApiDB(usernameLogged, passwordLogged)) {
                if (db.getConnection() != null) {

                    db.removeTABLEValue("users", "user_id", userID);

                } else {
                    System.err.println("[DEBUG] ApiDB connection failed upon creation.");
                }
            } catch (SQLException e) {
                System.err.println("[DEBUG] An SQL exception occurred during or after using ApiDB:");
                if (e.getErrorCode()==19) {
                    // CONTROLLER CODE TO INFORM USER OF ERROR [ERROR: NOT UNIQUE] // FRONTEND TODO
                    // ... //
                    // ... //
                } else e.printStackTrace();
            } catch (Exception e) {
                System.err.println("[DEBUG] An unexpected exception occurred:");
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

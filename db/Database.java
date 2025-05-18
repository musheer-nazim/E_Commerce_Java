
package db;

import java.sql.Connection;
import java.sql.DriverManager;

public class Database {
    private static final String DB_URL = "jdbc:sqlite:retail_platform.db";

    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(DB_URL);
        } catch (Exception e) {
            throw new RuntimeException("Failed to connect to database: " + e.getMessage());
        }
    }
}


package service;

import db.Database;
import models1.user.Admin;
import models1.user.Customer;
import models1.user.Supplier;
import models1.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class AuthService {
    public User login(String username, String password) {
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String role = rs.getString("role");
                if (role.equalsIgnoreCase("admin")) return new Admin(username, password);
                else if (role.equalsIgnoreCase("supplier")) return new Supplier(username, password);
                else return new Customer(username, password);
            }
        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
        }
        return null;
    }
}

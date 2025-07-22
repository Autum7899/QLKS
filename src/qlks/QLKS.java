/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package qlks;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;

/**
 *
 * @author Admin
 */
public class QLKS {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        addAdminUser("admin2", "password");
    }

    public static void addAdminUser(String username, String password) {
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        String sql = "INSERT INTO users (Username, PasswordHash, FullName, Email, PhoneNumber, Role) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, hashedPassword);
            ps.setString(3, "Admin");
            ps.setString(4, "admin@hotel.com");
            ps.setString(5, "123456789");
            ps.setString(6, "Admin");

            ps.executeUpdate();
            System.out.println("Admin user created successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

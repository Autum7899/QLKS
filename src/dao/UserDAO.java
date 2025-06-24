/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

/**
 *
 * @author Admin
 */

import qlks.DatabaseConnection; // Đảm bảo đúng đường dẫn
import dto.User;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Lớp DAO (Data Access Object) cho thực thể User.
 * Cung cấp các phương thức CRUD (Create, Read, Update, Delete) để thao tác với bảng 'users' trong CSDL.
 */
public class UserDAO {

    /**
     * Kiểm tra thông tin đăng nhập của nhân viên. 
     * @param username Tên đăng nhập 
     * @param password Mật khẩu 
     * @return Đối tượng User nếu thông tin hợp lệ, ngược lại trả về null. 
     * @throws SQLException nếu có lỗi truy vấn CSDL.
     */
    public User checkLogin(String username, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE Username = ? AND PasswordHash = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, username);
            ps.setString(2, password); // Trong thực tế, mật khẩu nên được băm
            
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        }
        return null; // Trả về null nếu không tìm thấy user hoặc thông tin sai
    }

    /**
     * Lấy thông tin một người dùng bằng ID.
     * @param userId ID của người dùng cần tìm.
     * @return đối tượng User hoặc null nếu không tìm thấy.
     * @throws SQLException
     */
    public User getUserById(int userId) throws SQLException {
        String sql = "SELECT * FROM users WHERE UserId = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapResultSetToUser(rs);
                }
            }
        }
        return null;
    }

    /**
     * Lấy danh sách tất cả người dùng trong hệ thống. 
     * Chức năng này dành cho Admin.
     * @return List<User>
     * @throws SQLException
     */
    public List<User> getAllUsers() throws SQLException {
        List<User> userList = new ArrayList<>();
        String sql = "SELECT * FROM users";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                userList.add(mapResultSetToUser(rs));
            }
        }
        return userList;
    }

    /**
     * Thêm một người dùng mới vào CSDL. 
     * Chức năng này dành cho Admin.
     * @param user Đối tượng User chứa thông tin cần thêm (Họ tên, Email, Vai trò, Mật khẩu...). 
     * @return true nếu thêm thành công, false nếu thất bại. 
     * @throws SQLException
     */
    public boolean addUser(User user) throws SQLException {
        String sql = "INSERT INTO users(Username, PasswordHash, FullName, Email, PhoneNumber, Role) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPasswordHash());
            ps.setString(3, user.getFullName());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getPhoneNumber());
            ps.setString(6, user.getRole());
            
            return ps.executeUpdate() > 0;
        }
    }
    
    /**
     * Cập nhật thông tin một người dùng đã có. 
     * Chức năng này dành cho Admin.
     * @param user Đối tượng User chứa thông tin cần cập nhật. ID của user dùng để xác định bản ghi cần sửa.
     * @return true nếu cập nhật thành công, false nếu thất bại. 
     * @throws SQLException
     */
    public boolean updateUser(User user) throws SQLException {
        String sql = "UPDATE users SET Username = ?, PasswordHash = ?, FullName = ?, Email = ?, PhoneNumber = ?, Role = ? WHERE UserId = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPasswordHash()); // Cân nhắc việc cho phép đổi mật khẩu ở form riêng
            ps.setString(3, user.getFullName());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getPhoneNumber());
            ps.setString(6, user.getRole());
            ps.setInt(7, user.getUserId());

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Xóa một người dùng khỏi hệ thống. 
     * Chức năng này dành cho Admin.
     * @param userId ID của người dùng cần xóa.
     * @return true nếu xóa thành công, false nếu thất bại. 
     * @throws SQLException
     */
    public boolean deleteUser(int userId) throws SQLException {
        String sql = "DELETE FROM users WHERE UserId = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);

            return ps.executeUpdate() > 0;
        }
    }
    
    /**
     * Phương thức hỗ trợ (helper method) để chuyển đổi dữ liệu từ ResultSet sang đối tượng User.
     * Giúp tránh lặp lại code ở nhiều nơi.
     * @param rs Đối tượng ResultSet đang trỏ đến một hàng dữ liệu.
     * @return Đối tượng User đã được khởi tạo với dữ liệu từ ResultSet.
     * @throws SQLException
     */
    private User mapResultSetToUser(ResultSet rs) throws SQLException {
        User user = new User();
        user.setUserId(rs.getInt("UserId"));
        user.setUsername(rs.getString("Username"));
        user.setPasswordHash(rs.getString("PasswordHash"));
        user.setFullName(rs.getString("FullName"));
        user.setEmail(rs.getString("Email"));
        user.setPhoneNumber(rs.getString("PhoneNumber"));
        user.setRole(rs.getString("Role"));
        return user;
    }
}
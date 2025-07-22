package qlks;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Lớp tiện ích để quản lý kết nối đến cơ sở dữ liệu MySQL.
 * Lớp này áp dụng nguyên tắc Singleton (chỉ có một kết nối) và tách biệt logic.
 */
public class DatabaseConnection {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/qlks";
    private static final String USER = "root";
    private static final String PASSWORD = "moly7899"; 
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Lỗi: Không tìm thấy MySQL JDBC Driver!", e);
        }
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }
}
package qlks;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Lớp tiện ích để quản lý kết nối đến cơ sở dữ liệu MySQL.
 * Lớp này áp dụng nguyên tắc Singleton (chỉ có một kết nối) và tách biệt logic.
 */
public class DatabaseConnection {

    // Chi tiết kết nối CSDL.
    private static final String DB_URL = "jdbc:mysql://localhost:3306/qlks";
    private static final String USER = "root";
    private static final String PASSWORD = "moly7899"; 

    /**
     * Thiết lập và trả về một kết nối đến cơ sở dữ liệu.
     * Phương thức này sẽ ném ra một ngoại lệ (Exception) nếu kết nối thất bại,
     * để cho lớp gọi nó (ví dụ: lớp giao diện) quyết định cách xử lý lỗi.
     *
     * @return Một đối tượng Connection đã sẵn sàng.
     * @throws SQLException nếu không thể kết nối với CSDL.
     */
    public static Connection getConnection() throws SQLException {
        try {
            // Dù không bắt buộc với các driver JDBC mới, việc đăng ký tường minh
            // giúp mã nguồn rõ ràng và tránh lỗi trên một số môi trường.
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            // Ném ra một lỗi nghiêm trọng hơn nếu không tìm thấy driver
            throw new SQLException("Lỗi: Không tìm thấy MySQL JDBC Driver!", e);
        }
        
        // Trả về kết nối, ném ra SQLException nếu có lỗi
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }
}
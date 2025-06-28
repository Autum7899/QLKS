package dao;

import dto.Customer;
import dto.Booking; // Cần thiết cho lịch sử lưu trú
import qlks.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho thực thể Khách hàng (Customer).
 * Quản lý các thao tác CRUD và các nghiệp vụ liên quan đến khách hàng.
 */
public class CustomerDAO {

    /**
     * Thêm một khách hàng mới. 
     * @param customer Đối tượng Customer chứa thông tin cần thêm.
     * @return ID của khách hàng vừa được tạo, hoặc -1 nếu thất bại.
     * @throws SQLException
     */
    public int addCustomer(Customer customer) throws SQLException {
        String sql = "INSERT INTO customers(FullName, IDCardNumber, PhoneNumber, Address, Email) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setString(1, customer.getFullName());
            ps.setString(2, customer.getIdCardNumber());
            ps.setString(3, customer.getPhoneNumber());
            ps.setString(4, customer.getAddress());
            ps.setString(5, customer.getEmail());
            
            int affectedRows = ps.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet generatedKeys = ps.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return generatedKeys.getInt(1);
                    }
                }
            }
        }
        return -1;
    }

    /**
     * Cập nhật thông tin khách hàng. 
     * @param customer Đối tượng Customer chứa thông tin mới.
     * @return true nếu cập nhật thành công.
     * @throws SQLException
     */
    public boolean updateCustomer(Customer customer) throws SQLException {
        String sql = "UPDATE customers SET FullName = ?, IDCardNumber = ?, PhoneNumber = ?, Address = ?, Email = ? WHERE CustomerId = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, customer.getFullName());
            ps.setString(2, customer.getIdCardNumber());
            ps.setString(3, customer.getPhoneNumber());
            ps.setString(4, customer.getAddress());
            ps.setString(5, customer.getEmail());
            ps.setInt(6, customer.getCustomerId());
            
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Xóa một khách hàng. 
     * @param customerId ID của khách hàng cần xóa.
     * @return true nếu xóa thành công.
     * @throws SQLException
     */
    public boolean deleteCustomer(int customerId) throws SQLException {
        // Lưu ý: Cần xử lý các booking liên quan trước khi xóa khách hàng để tránh lỗi khóa ngoại.
        String sql = "DELETE FROM customers WHERE CustomerId = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, customerId);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Lấy danh sách tất cả khách hàng.
     * @return List<Customer>
     * @throws SQLException
     */
    public List<Customer> getAllCustomers() throws SQLException {
        List<Customer> customers = new ArrayList<>();
        String sql = "SELECT * FROM customers ORDER BY FullName";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                customers.add(mapResultSetToCustomer(rs));
            }
        }
        return customers;
    }

    /**
     * Xem lịch sử lưu trú của khách hàng. 
     * @param customerId ID của khách hàng.
     * @return Danh sách các booking của khách hàng đó.
     * @throws SQLException
     */

    private Customer mapResultSetToCustomer(ResultSet rs) throws SQLException {
        Customer customer = new Customer();
        customer.setCustomerId(rs.getInt("CustomerId"));
        customer.setFullName(rs.getString("FullName"));
        customer.setIdCardNumber(rs.getString("IDCardNumber"));
        customer.setPhoneNumber(rs.getString("PhoneNumber"));
        customer.setAddress(rs.getString("Address"));
        customer.setEmail(rs.getString("Email"));
        return customer;
    }
}
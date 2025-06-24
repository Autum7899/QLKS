package dao;

import dto.Service;
import qlks.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;

/**
 * DAO cho thực thể Dịch vụ (Service).
 * Quản lý danh sách dịch vụ và ghi nhận việc sử dụng dịch vụ.
 */
public class ServiceDAO {

    /**
     * Thêm một dịch vụ mới. 
     * @param service Đối tượng Service cần thêm.
     * @return true nếu thành công.
     * @throws SQLException
     */
    public boolean addService(Service service) throws SQLException {
        String sql = "INSERT INTO services(ServiceName, Price, Description) VALUES (?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, service.getServiceName());
            ps.setBigDecimal(2, service.getPrice());
            ps.setString(3, service.getDescription());
            
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Cập nhật thông tin dịch vụ. 
     * @param service Đối tượng Service chứa thông tin mới.
     * @return true nếu thành công.
     * @throws SQLException
     */
    public boolean updateService(Service service) throws SQLException {
        String sql = "UPDATE services SET ServiceName = ?, Price = ?, Description = ? WHERE ServiceId = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, service.getServiceName());
            ps.setBigDecimal(2, service.getPrice());
            ps.setString(3, service.getDescription());
            ps.setInt(4, service.getServiceId());

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Xóa một dịch vụ. 
     * @param serviceId ID của dịch vụ cần xóa.
     * @return true nếu thành công.
     * @throws SQLException
     */
    public boolean deleteService(int serviceId) throws SQLException {
        String sql = "DELETE FROM services WHERE ServiceId = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, serviceId);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Lấy danh sách tất cả dịch vụ. 
     * @return List<Service>
     * @throws SQLException
     */
    public List<Service> getAllServices() throws SQLException {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT * FROM services ORDER BY ServiceName";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                services.add(mapResultSetToService(rs));
            }
        }
        return services;
    }
    
    /**
     * Ghi nhận một dịch vụ đã được khách hàng sử dụng vào hóa đơn phòng. 
     * @param bookingId ID của booking (phiếu đặt phòng).
     * @param serviceId ID của dịch vụ.
     * @param quantity Số lượng.
     * @return true nếu ghi nhận thành công.
     * @throws SQLException
     */
    public boolean addServiceToBooking(int bookingId, int serviceId, int quantity) throws SQLException {
        String sql = "INSERT INTO bookedservices(BookingId, ServiceId, Quantity, PriceAtBooking) " +
                     "SELECT ?, ?, ?, Price FROM services WHERE ServiceId = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, bookingId);
            ps.setInt(2, serviceId);
            ps.setInt(3, quantity);
            ps.setInt(4, serviceId);
            
            return ps.executeUpdate() > 0;
        }
    }

    private Service mapResultSetToService(ResultSet rs) throws SQLException {
        Service service = new Service();
        service.setServiceId(rs.getInt("ServiceId"));
        service.setServiceName(rs.getString("ServiceName"));
        service.setPrice(rs.getBigDecimal("Price"));
        service.setDescription(rs.getString("Description"));
        return service;
    }
}
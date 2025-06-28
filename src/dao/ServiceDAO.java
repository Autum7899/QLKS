package dao;

import static dao.InvoiceDAO.showEstimateInvoice;
import dto.Service;
import qlks.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;

/**
 * DAO cho thực thể Dịch vụ (Service).
 * Quản lý danh sách dịch vụ và ghi nhận việc sử dụng dịch vụ.
 */
public class ServiceDAO {
   public static void deleteSelectedBookedService(JTable table, int bookingId,JLabel jLabel) {
    int row = table.getSelectedRow();
    if (row < 0) {
        JOptionPane.showMessageDialog(null, "Vui lòng chọn một dòng dịch vụ để xóa.");
        return;
    }

    String serviceName = table.getValueAt(row, 0).toString();
    if (serviceName.toLowerCase().startsWith("phòng")) {
        JOptionPane.showMessageDialog(null, "Không thể xóa tiền phòng.");
        return;
    }

    // Hỏi xác nhận
    int confirm = JOptionPane.showConfirmDialog(null, "Xác nhận xóa dịch vụ: " + serviceName + "?", "Xác nhận", JOptionPane.YES_NO_OPTION);
    if (confirm != JOptionPane.YES_OPTION) return;

    try (Connection conn = DatabaseConnection.getConnection()) {
        String sql = """
            DELETE FROM bookedservices 
            WHERE BookingId = ? AND ServiceId = (
                SELECT ServiceId FROM services WHERE ServiceName = ?
            )
        """;

        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, bookingId);
        stmt.setString(2, serviceName);
        int rows = stmt.executeUpdate();

        if (rows > 0) {
            JOptionPane.showMessageDialog(null, "Đã xóa dịch vụ: " + serviceName);
            // Load lại bảng
            showEstimateInvoice(bookingId, table, jLabel); // chỉ cần truyền bảng nếu không cần tổng
        } else {
            JOptionPane.showMessageDialog(null, "Không tìm thấy dịch vụ để xóa.");
        }

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Lỗi khi xóa dịch vụ: " + e.getMessage());
    }
}

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
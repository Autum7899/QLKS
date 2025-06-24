package dao;

import dto.Booking;
import qlks.DatabaseConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * DAO cho thực thể Đặt phòng (Booking).
 * Quản lý các nghiệp vụ tạo, sửa, hủy, check-in, check-out.
 */
public class BookingDAO {

    /**
     * Tạo một phiếu đặt phòng mới. 
     * @param booking Đối tượng Booking chứa thông tin.
     * @return true nếu thành công.
     * @throws SQLException
     */
    public boolean createBooking(Booking booking) throws SQLException {
        String sql = "INSERT INTO bookings(RoomId, CustomerId, CheckInDate, CheckOutDate, Status, Notes) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, booking.getRoomId());
            ps.setInt(2, booking.getCustomerId());
            ps.setDate(3, new java.sql.Date(booking.getCheckInDate().getTime()));
            ps.setDate(4, new java.sql.Date(booking.getCheckOutDate().getTime()));
            ps.setString(5, "Đã đặt"); // Trạng thái ban đầu
            ps.setString(6, booking.getNotes());
            
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Cập nhật thông tin một phiếu đặt phòng. 
     * @param booking Đối tượng Booking chứa thông tin mới.
     * @return true nếu thành công.
     * @throws SQLException
     */
    public boolean updateBooking(Booking booking) throws SQLException {
        String sql = "UPDATE bookings SET RoomId = ?, CustomerId = ?, CheckInDate = ?, CheckOutDate = ?, Notes = ? WHERE BookingId = ?";
        // Các trường khác như Status, TotalAmount sẽ được cập nhật bởi các nghiệp vụ riêng.
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setInt(1, booking.getRoomId());
            ps.setInt(2, booking.getCustomerId());
            ps.setDate(3, new java.sql.Date(booking.getCheckInDate().getTime()));
            ps.setDate(4, new java.sql.Date(booking.getCheckOutDate().getTime()));
            ps.setString(5, booking.getNotes());
            ps.setInt(6, booking.getBookingId());

            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Hủy một phiếu đặt phòng. 
     * @param bookingId ID của booking cần hủy.
     * @return true nếu thành công.
     * @throws SQLException
     */
    public boolean cancelBooking(int bookingId) throws SQLException {
        return updateBookingStatus(bookingId, "Đã hủy");
    }

    /**
     * Cập nhật trạng thái nhận phòng (Check-in). 
     * @param bookingId ID của booking.
     * @return true nếu thành công.
     * @throws SQLException
     */
    public boolean checkIn(int bookingId) throws SQLException {
        String sql = "UPDATE bookings SET Status = 'Đang có khách', ActualCheckInDate = NOW() WHERE BookingId = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Cập nhật trạng thái trả phòng (Check-out). 
     * @param bookingId ID của booking.
     * @return true nếu thành công.
     * @throws SQLException
     */
    public boolean checkOut(int bookingId) throws SQLException {
        String sql = "UPDATE bookings SET Status = 'Đã trả phòng', ActualCheckOutDate = NOW() WHERE BookingId = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, bookingId);
            return ps.executeUpdate() > 0;
        }
    }
    
    /**
     * Phương thức chung để cập nhật trạng thái của một booking.
     * @param bookingId ID booking.
     * @param newStatus Trạng thái mới.
     * @return true nếu thành công.
     * @throws SQLException
     */
    public boolean updateBookingStatus(int bookingId, String newStatus) throws SQLException {
        String sql = "UPDATE bookings SET Status = ? WHERE BookingId = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newStatus);
            ps.setInt(2, bookingId);
            return ps.executeUpdate() > 0;
        }
    }

    /**
     * Lấy danh sách booking theo trạng thái.
     * @param status Trạng thái cần lọc.
     * @return List<Booking>
     * @throws SQLException
     */
    public List<Booking> getBookingsByStatus(String status) throws SQLException {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT * FROM bookings WHERE Status = ? ORDER BY CheckInDate";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while(rs.next()){
                    bookings.add(mapResultSetToBooking(rs));
                }
            }
        }
        return bookings;
    }
    
    private Booking mapResultSetToBooking(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setBookingId(rs.getInt("BookingId"));
        booking.setRoomId(rs.getInt("RoomId"));
        booking.setCustomerId(rs.getInt("CustomerId"));
        booking.setCheckInDate(rs.getDate("CheckInDate"));
        booking.setCheckOutDate(rs.getDate("CheckOutDate"));
        booking.setActualCheckInDate(rs.getTimestamp("ActualCheckInDate"));
        booking.setActualCheckOutDate(rs.getTimestamp("ActualCheckOutDate"));
        booking.setBookingDate(rs.getTimestamp("BookingDate"));
        booking.setStatus(rs.getString("Status"));
        booking.setTotalAmount(rs.getBigDecimal("TotalAmount"));
        booking.setPaymentStatus(rs.getString("PaymentStatus"));
        booking.setNotes(rs.getString("Notes"));
        return booking;
    }
}
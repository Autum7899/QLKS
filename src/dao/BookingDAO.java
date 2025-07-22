package dao;

import qlks.DatabaseConnection;

import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.sql.*;
import javax.swing.JOptionPane;

public class BookingDAO {

    public static void loadBookingsToTable(JTable table) {
        String[] columnNames = {"Mã Booking", "Khách hàng", "Phòng", "Ngày nhận", "Ngày trả", "Trạng thái"};
        DefaultTableModel model = new DefaultTableModel(columnNames, 0);

          String sql = """
        SELECT b.BookingId, c.FullName, r.RoomNumber, 
               b.CheckInDate, b.CheckOutDate, b.Status
        FROM bookings b
        JOIN customers c ON b.CustomerId = c.CustomerId
        JOIN rooms r ON b.RoomId = r.RoomId
        ORDER BY 
          CASE 
            WHEN b.Status IN ('CheckedOut', 'Cancelled') THEN 1
            ELSE 0
          END,
          b.CheckInDate
    """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int bookingId = rs.getInt("BookingId");
                String customerName = rs.getString("FullName");
                String roomNumber = rs.getString("RoomNumber");
                Date checkIn = rs.getDate("CheckInDate");
                Date checkOut = rs.getDate("CheckOutDate");
                String status = rs.getString("Status");

                Object[] row = {bookingId, customerName, roomNumber, checkIn, checkOut, status};
                model.addRow(row);
            }

            table.setModel(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void select(JTable table){
        table.getSelectionModel().addListSelectionListener(e -> {
        if (!e.getValueIsAdjusting()) {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                Object value = table.getValueAt(selectedRow, 0);
                int bookingId = Integer.parseInt(value.toString());
                System.out.println("BookingId được chọn: " + bookingId);
            }
        }
    }); 
    }
    public static void checkIn(JTable table) {
    int row = table.getSelectedRow();
    if (row < 0) {
        JOptionPane.showMessageDialog(null, "Vui lòng chọn một đặt phòng.");
        return;
    }
    String status = table.getValueAt(row, 5).toString(); // Cột 5 là Status
    if (status.equalsIgnoreCase("Cancelled") || 
        status.equalsIgnoreCase("CheckedIn") || 
        status.equalsIgnoreCase("CheckedOut")) {
        JOptionPane.showMessageDialog(null, "Không thể nhận phòng với trạng thái: " + status);
        return;
    }
    int bookingId = Integer.parseInt(table.getValueAt(row, 0).toString());
    String sql = "UPDATE bookings SET ActualCheckInDate = NOW(), Status = 'CheckedIn' WHERE BookingId = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement(sql)) {
        stmt.setInt(1, bookingId);
        stmt.executeUpdate();
        JOptionPane.showMessageDialog(null, "Nhận phòng thành công!");
        loadBookingsToTable(table);
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Lỗi khi nhận phòng: " + e.getMessage());
    }
    }

    // Trả phòng
    public static void checkOut(JTable table) {
    int row = table.getSelectedRow();
    if (row < 0) {JOptionPane.showMessageDialog(null, "Vui lòng chọn một đặt phòng.");return;}
    String status = table.getValueAt(row, 5).toString();
    if (status.equalsIgnoreCase("Cancelled")) {JOptionPane.showMessageDialog(null, "Đặt phòng đã bị hủy, không thể trả phòng.");return;}
    if (status.equalsIgnoreCase("CheckedOut")) {JOptionPane.showMessageDialog(null, "Đã trả phòng trước đó.");return;}
    if (!status.equalsIgnoreCase("CheckedIn")) {JOptionPane.showMessageDialog(null, "Chỉ có thể trả phòng sau khi đã nhận phòng.");return;}
    int bookingId = Integer.parseInt(table.getValueAt(row, 0).toString());
    try (Connection conn = DatabaseConnection.getConnection()) {
        conn.setAutoCommit(false); // Start transaction
        // 1. Get RoomId of this booking
        int roomId = -1;
        String getRoomIdSQL = "SELECT RoomId FROM bookings WHERE BookingId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(getRoomIdSQL)) {
            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                roomId = rs.getInt("RoomId");
            } else {
                JOptionPane.showMessageDialog(null, "Không tìm thấy phòng cho booking này.");
                conn.rollback();
                return;
            }
        }
        // 2. Update booking status to 'CheckedOut' and set ActualCheckOutDate
        String updateBookingSQL = "UPDATE bookings SET ActualCheckOutDate = NOW(), Status = 'CheckedOut' WHERE BookingId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(updateBookingSQL)) {
            stmt.setInt(1, bookingId);
            stmt.executeUpdate();
        }
        // 3. Update room status to 'Available'
        String updateRoomSQL = "UPDATE rooms SET Status = 'Available' WHERE RoomId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(updateRoomSQL)) {
            stmt.setInt(1, roomId);
            stmt.executeUpdate();
        }
        conn.commit(); // Complete transaction
        JOptionPane.showMessageDialog(null, "Trả phòng thành công!");
        loadBookingsToTable(table);

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Lỗi khi trả phòng: " + e.getMessage());
    }
}
    // Hủy đặt
    public static void cancelBooking(JTable table) {
    int row = table.getSelectedRow();
    if (row < 0) {
        JOptionPane.showMessageDialog(null, "Vui lòng chọn một đặt phòng.");
        return;
    }
    String status = table.getValueAt(row, 5).toString(); // Status column
    if (status.equalsIgnoreCase("Cancelled") ||
        status.equalsIgnoreCase("CheckedOut") ||
        status.equalsIgnoreCase("CheckedIn")) {
        JOptionPane.showMessageDialog(null, "Chỉ có thể hủy đặt phòng chưa sử dụng.");
        return;
    }
    int bookingId = Integer.parseInt(table.getValueAt(row, 0).toString());

    int confirm = JOptionPane.showConfirmDialog(
        null,
        "Bạn có chắc chắn muốn hủy đặt phòng này?",
        "Xác nhận hủy",
        JOptionPane.YES_NO_OPTION
    );
    if (confirm != JOptionPane.YES_OPTION) return;
    try (Connection conn = DatabaseConnection.getConnection()) {
        conn.setAutoCommit(false); // bắt đầu giao dịch
        // 1. Lấy RoomId của booking này
        int roomId = -1;
        String getRoomIdSQL = "SELECT RoomId FROM bookings WHERE BookingId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(getRoomIdSQL)) {
            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                roomId = rs.getInt("RoomId");
            } else {
                JOptionPane.showMessageDialog(null, "Không tìm thấy phòng cho booking này.");
                conn.rollback();
                return;
            }
        }
        // 2. Hủy đặt phòng
        String cancelSQL = "UPDATE bookings SET Status = 'Cancelled' WHERE BookingId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(cancelSQL)) {
            stmt.setInt(1, bookingId);
            stmt.executeUpdate();
        }
        // 3. Cập nhật trạng thái phòng → Available
        String updateRoomSQL = "UPDATE rooms SET Status = 'Available' WHERE RoomId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(updateRoomSQL)) {
            stmt.setInt(1, roomId);
            stmt.executeUpdate();
        }
        conn.commit(); 
        JOptionPane.showMessageDialog(null, "Hủy đặt phòng thành công!");
        loadBookingsToTable(table);
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Lỗi khi hủy đặt phòng: " + e.getMessage());
    }
}

    public static void delete(JTable table) {
    int row = table.getSelectedRow();
    if (row < 0) {
        JOptionPane.showMessageDialog(null, "Vui lòng chọn một dòng.");
        return;
    }

    String status = table.getValueAt(row, 5).toString().toLowerCase();
    if (!status.equals("cancelled") && !status.equals("checkedout")) {
        JOptionPane.showMessageDialog(null, "Chỉ xóa khi trạng thái là 'Cancelled' hoặc 'CheckedOut'");
        return;
    }

    int confirm = JOptionPane.showConfirmDialog(null, "Xác nhận xóa đặt phòng?", "Xác nhận", JOptionPane.YES_NO_OPTION);
    if (confirm != JOptionPane.YES_OPTION) return;

    int bookingId = Integer.parseInt(table.getValueAt(row, 0).toString());

    try (Connection conn = DatabaseConnection.getConnection()) {
        conn.setAutoCommit(false); // đảm bảo atomicity

        // Xóa booked services trước
        String sqlDeleteServices = "DELETE FROM bookedservices WHERE BookingId = ?";
        try (PreparedStatement stmt1 = conn.prepareStatement(sqlDeleteServices)) {
            stmt1.setInt(1, bookingId);
            stmt1.executeUpdate();
        }

        // Xóa invoice (nếu có)
        String sqlDeleteInvoice = "DELETE FROM invoices WHERE BookingId = ?";
        try (PreparedStatement stmt2 = conn.prepareStatement(sqlDeleteInvoice)) {
            stmt2.setInt(1, bookingId);
            stmt2.executeUpdate();
        }

        // Xóa booking cuối cùng
        String sqlDeleteBooking = "DELETE FROM bookings WHERE BookingId = ?";
        try (PreparedStatement stmt3 = conn.prepareStatement(sqlDeleteBooking)) {
            stmt3.setInt(1, bookingId);
            stmt3.executeUpdate();
        }

        conn.commit();
        JOptionPane.showMessageDialog(null, "Xóa thành công.");
        loadBookingsToTable(table);
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Lỗi khi xóa: " + e.getMessage());
    }
}

public static void checkOutAtPay(int bookingId) {

    try (Connection conn = DatabaseConnection.getConnection()) {
        conn.setAutoCommit(false); // Start transaction

        // 1. Get RoomId of this booking
        int roomId = -1;
        String getRoomIdSQL = "SELECT RoomId FROM bookings WHERE BookingId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(getRoomIdSQL)) {
            stmt.setInt(1, bookingId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                roomId = rs.getInt("RoomId");
            } else {
                JOptionPane.showMessageDialog(null, "Không tìm thấy phòng cho booking này.");
                conn.rollback();
                return;
            }
        }

        // 2. Update booking status to 'CheckedOut' and set ActualCheckOutDate
        String updateBookingSQL = "UPDATE bookings SET ActualCheckOutDate = NOW(), Status = 'CheckedOut' WHERE BookingId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(updateBookingSQL)) {
            stmt.setInt(1, bookingId);
            stmt.executeUpdate();
        }

        // 3. Update room status to 'Available'
        String updateRoomSQL = "UPDATE rooms SET Status = 'Available' WHERE RoomId = ?";
        try (PreparedStatement stmt = conn.prepareStatement(updateRoomSQL)) {
            stmt.setInt(1, roomId);
            stmt.executeUpdate();
        }

        conn.commit(); // Complete transaction
        JOptionPane.showMessageDialog(null, "Trả phòng thành công!");

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Lỗi khi trả phòng: " + e.getMessage());
    }
}
}



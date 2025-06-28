package dao;



import dto.Invoice;
import java.io.FileWriter;
import java.io.PrintWriter;
import qlks.DatabaseConnection;
import java.sql.*;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * DAO cho thực thể Hóa đơn (Invoice).
 * Quản lý các nghiệp vụ tính toán, tạo và thanh toán hóa đơn.
 */
public class InvoiceDAO {

    /**
     * Tạo hóa đơn chi tiết từ một booking. 
     * Đây là một giao dịch (transaction) phức tạp, bao gồm nhiều bước.
     * @param bookingId ID của booking cần tạo hóa đơn.
     * @param userId ID của nhân viên tạo hóa đơn.
     * @return ID của hóa đơn vừa tạo, hoặc -1 nếu thất bại.
     * @throws SQLException
     */
    public static void showEstimateInvoice(int bookingId, JTable tblEstimateDetails, JLabel lblEstimateTotal) {
    DefaultTableModel model = new DefaultTableModel();
    model.setColumnIdentifiers(new Object[]{"Dịch vụ", "Số lượng", "Đơn giá", "Thành tiền"});
    double total = 0;

    try (Connection conn = DatabaseConnection.getConnection()) {

        // ===== TIỀN PHÒNG =====
       String sqlRoom = """
    SELECT r.RoomNumber, r.PricePerNight,
           b.CheckInDate, b.CheckOutDate,
           b.ActualCheckInDate, b.ActualCheckOutDate
    FROM bookings b
    JOIN rooms r ON b.RoomId = r.RoomId
    WHERE b.BookingId = ?
""";

        PreparedStatement stmtRoom = conn.prepareStatement(sqlRoom);
        stmtRoom.setInt(1, bookingId);
        ResultSet rsRoom = stmtRoom.executeQuery();

        if (rsRoom.next()) {
            String roomName = "Phòng " + rsRoom.getString("RoomNumber");
            double unitPrice = rsRoom.getDouble("PricePerNight");
            Date checkIn = rsRoom.getDate("ActualCheckInDate");
Date checkOut = rsRoom.getDate("ActualCheckOutDate");

if (checkIn == null) checkIn = rsRoom.getDate("CheckInDate");
if (checkOut == null) checkOut = rsRoom.getDate("CheckOutDate");
;

            long nights = ChronoUnit.DAYS.between(
                checkIn.toLocalDate(), checkOut.toLocalDate()
            );

            double roomTotal = nights * unitPrice;
            total += roomTotal;

            model.addRow(new Object[]{
                roomName,
                nights + " đêm",
                String.format("%,.0f", unitPrice),
                String.format("%,.0f", roomTotal)
            });
        }

        // ===== DỊCH VỤ =====
        String sqlService = """
            SELECT s.ServiceName, bs.Quantity, bs.PriceAtBooking
            FROM bookedservices bs
            JOIN services s ON bs.ServiceId = s.ServiceId
            WHERE bs.BookingId = ?
        """;

        PreparedStatement stmtService = conn.prepareStatement(sqlService);
        stmtService.setInt(1, bookingId);
        ResultSet rsService = stmtService.executeQuery();

        while (rsService.next()) {
            String serviceName = rsService.getString("ServiceName");
            int quantity = rsService.getInt("Quantity");
            double unitPrice = rsService.getDouble("PriceAtBooking");
            double lineTotal = quantity * unitPrice;
            total += lineTotal;

            model.addRow(new Object[]{
                serviceName,
                quantity,
                String.format("%,.0f", unitPrice),
                String.format("%,.0f", lineTotal)
            });
        }

        // Gán dữ liệu cho bảng và label tổng
        tblEstimateDetails.setModel(model);
        lblEstimateTotal.setText("Total: " + String.format("%,.0f", total) + " VNĐ");

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Lỗi khi tính hóa đơn tạm: " + e.getMessage());
    }
}
public static boolean thanhToan(int bookingId, String username, String paymentMethod) {
    String sqlGetUserId = "SELECT UserId FROM users WHERE Username = ?";
    String sqlCheck = "SELECT b.BookingId, b.CustomerId, b.CheckInDate, b.CheckOutDate, " +
                      "r.PricePerNight, IFNULL(b.ActualCheckInDate, b.CheckInDate) AS StartDate, " +
                      "IFNULL(b.ActualCheckOutDate, b.CheckOutDate) AS EndDate " +
                      "FROM bookings b JOIN rooms r ON b.RoomId = r.RoomId " +
                      "WHERE b.BookingId = ?";
    String sqlService = "SELECT SUM(Quantity * PriceAtBooking) FROM bookedservices WHERE BookingId = ?";
    String sqlInsertInvoice = "INSERT INTO invoices (BookingId, CustomerId, TotalRoomCharge, TotalServiceCharge, GrandTotal, PaymentMethod, PaymentDate, IssuedByUserId) " +
                              "VALUES (?, ?, ?, ?, ?, ?, NOW(), ?)";
    String sqlUpdateStatus = "UPDATE bookings SET PaymentStatus = 'Paid' WHERE BookingId = ?";

    try (Connection conn = DatabaseConnection.getConnection()) {
        conn.setAutoCommit(false);

        int userId = -1;
        try (PreparedStatement psUser = conn.prepareStatement(sqlGetUserId)) {
            psUser.setString(1, username);
            try (ResultSet rsUser = psUser.executeQuery()) {
                if (rsUser.next()) {    
                    userId = rsUser.getInt("UserId");
                } else {
                    throw new Exception("Không tìm thấy người dùng với username: " + username);
                }
            }
        }

        try (PreparedStatement ps = conn.prepareStatement(sqlCheck)) {
            ps.setInt(1, bookingId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int customerId = rs.getInt("CustomerId");
                    BigDecimal pricePerNight = rs.getBigDecimal("PricePerNight");
                    LocalDate startDate = rs.getTimestamp("StartDate").toLocalDateTime().toLocalDate();
                    LocalDate endDate = rs.getTimestamp("EndDate").toLocalDateTime().toLocalDate();
                    long days = ChronoUnit.DAYS.between(startDate, endDate);
                    if (days == 0) days = 1;

                    BigDecimal roomCharge = pricePerNight.multiply(BigDecimal.valueOf(days));

                    BigDecimal serviceCharge = BigDecimal.ZERO;
                    try (PreparedStatement psService = conn.prepareStatement(sqlService)) {
                        psService.setInt(1, bookingId);
                        try (ResultSet rsService = psService.executeQuery()) {
                            if (rsService.next()) {
                                serviceCharge = rsService.getBigDecimal(1);
                                if (serviceCharge == null) serviceCharge = BigDecimal.ZERO;
                            }
                        }
                    }

                    BigDecimal grandTotal = roomCharge.add(serviceCharge);

                    try (PreparedStatement psInvoice = conn.prepareStatement(sqlInsertInvoice)) {
                        psInvoice.setInt(1, bookingId);
                        psInvoice.setInt(2, customerId);
                        psInvoice.setBigDecimal(3, roomCharge);
                        psInvoice.setBigDecimal(4, serviceCharge);
                        psInvoice.setBigDecimal(5, grandTotal);
                        psInvoice.setString(6, paymentMethod);
                        psInvoice.setInt(7, userId);
                        psInvoice.executeUpdate();
                    }

                    try (PreparedStatement psUpdate = conn.prepareStatement(sqlUpdateStatus)) {
                        psUpdate.setInt(1, bookingId);
                        psUpdate.executeUpdate();
                    }

                    conn.commit();
                    return true;
                }
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    return false;
}

public static void printInvoiceToTextFile(int bookingId, String username) {
    String filePath = "C:\\Users\\Admin\\Desktop\\HoaDon\\Invoice_" + bookingId + ".txt";

    try (Connection conn = DatabaseConnection.getConnection();
         PrintWriter writer = new PrintWriter(new FileWriter(filePath))) {

        writer.println("===== HÓA ĐƠN THANH TOÁN =====");
        writer.println("Mã đặt phòng: " + bookingId);
        writer.println("------------------------------------------");

        double total = 0;

        // ===== TIỀN PHÒNG =====
        String sqlRoom = """
            SELECT r.RoomNumber, r.PricePerNight,
                   b.CheckInDate, b.CheckOutDate,
                   b.ActualCheckInDate, b.ActualCheckOutDate
            FROM bookings b
            JOIN rooms r ON b.RoomId = r.RoomId
            WHERE b.BookingId = ?
        """;

        try (PreparedStatement stmtRoom = conn.prepareStatement(sqlRoom)) {
            stmtRoom.setInt(1, bookingId);
            ResultSet rsRoom = stmtRoom.executeQuery();

            if (rsRoom.next()) {
                String roomNumber = rsRoom.getString("RoomNumber");
                double unitPrice = rsRoom.getDouble("PricePerNight");

                Date checkIn = rsRoom.getDate("ActualCheckInDate");
                Date checkOut = rsRoom.getDate("ActualCheckOutDate");

                if (checkIn == null) checkIn = rsRoom.getDate("CheckInDate");
                if (checkOut == null) checkOut = rsRoom.getDate("CheckOutDate");

                long nights = ChronoUnit.DAYS.between(
                    checkIn.toLocalDate(), checkOut.toLocalDate()
                );

                double roomTotal = nights * unitPrice;
                total += roomTotal;

                writer.println("Phòng: " + roomNumber);
                writer.println("Số đêm: " + nights);
                writer.println("Đơn giá: " + String.format("%,.0f", unitPrice) + " VNĐ");
                writer.println("Thành tiền: " + String.format("%,.0f", roomTotal) + " VNĐ");
                writer.println();
            }
        }

        // ===== DỊCH VỤ =====
        writer.println("---- DỊCH VỤ SỬ DỤNG ----");

        String sqlService = """
            SELECT s.ServiceName, bs.Quantity, bs.PriceAtBooking
            FROM bookedservices bs
            JOIN services s ON bs.ServiceId = s.ServiceId
            WHERE bs.BookingId = ?
        """;

        try (PreparedStatement stmtService = conn.prepareStatement(sqlService)) {
            stmtService.setInt(1, bookingId);
            ResultSet rsService = stmtService.executeQuery();

            while (rsService.next()) {
                String serviceName = rsService.getString("ServiceName");
                int quantity = rsService.getInt("Quantity");
                double price = rsService.getDouble("PriceAtBooking");
                double lineTotal = quantity * price;
                total += lineTotal;

                writer.printf("• %s: %d x %,.0f = %,.0f VNĐ\n", serviceName, quantity, price, lineTotal);
            }
        }

        writer.println("------------------------------------------");
        writer.println("TỔNG CỘNG: " + String.format("%,.0f", total) + " VNĐ");
        writer.println("Ngày in: " + java.time.LocalDateTime.now());
        writer.println("In bởi nhân viên: " +username);
        writer.println("==========================================");

        writer.close();
        JOptionPane.showMessageDialog(null, "Đã in hóa đơn ra file: " + filePath);

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Lỗi khi in hóa đơn: " + e.getMessage());
    }
}

}
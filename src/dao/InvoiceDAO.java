package dao;



import dto.Invoice;
import qlks.DatabaseConnection;
import java.sql.*;
import java.math.BigDecimal;
import java.time.temporal.ChronoUnit;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

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
    public void showEstimateInvoice(int bookingId, JTable tblEstimateDetails, JLabel lblEstimateTotal) {
    DefaultTableModel model = new DefaultTableModel();
    model.setColumnIdentifiers(new Object[]{"Tên", "Số lượng", "Đơn giá", "Thành tiền"});
    double total = 0;

    try (Connection conn = DatabaseConnection.getConnection()) {

        // ===== TIỀN PHÒNG =====
        String sqlRoom = """
            SELECT r.RoomNumber, r.PricePerNight, b.CheckInDate, b.CheckOutDate
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
            Date checkIn = rsRoom.getDate("CheckInDate");
            Date checkOut = rsRoom.getDate("CheckOutDate");

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
            FROM booked_services bs
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
        lblEstimateTotal.setText("Tạm tính: " + String.format("%,.0f", total) + " VNĐ");

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Lỗi khi tính hóa đơn tạm: " + e.getMessage());
    }
}

    public int createInvoiceFromBooking(int bookingId, int userId) throws SQLException {
        String roomChargeSql = "SELECT DATEDIFF(b.CheckOutDate, b.CheckInDate) * r.PricePerNight AS RoomCharge " +
                               "FROM bookings b JOIN rooms r ON b.RoomId = r.RoomId WHERE b.BookingId = ?";
        String serviceChargeSql = "SELECT SUM(bs.Quantity * bs.PriceAtBooking) AS ServiceCharge " +
                                  "FROM bookedservices bs WHERE bs.BookingId = ?";
        String insertInvoiceSql = "INSERT INTO invoices(BookingId, CustomerId, IssueDate, TotalRoomCharge, TotalServiceCharge, GrandTotal, IssuedByUserId) " +
                                  "SELECT ?, CustomerId, NOW(), ?, ?, ?, ? FROM bookings WHERE BookingId = ?";

        Connection conn = null;
        int invoiceId = -1;
        
        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false); // Bắt đầu transaction

            BigDecimal totalRoomCharge = BigDecimal.ZERO;
            BigDecimal totalServiceCharge = BigDecimal.ZERO;

            // 1. Tính tiền phòng 
            try (PreparedStatement psRoom = conn.prepareStatement(roomChargeSql)) {
                psRoom.setInt(1, bookingId);
                try (ResultSet rs = psRoom.executeQuery()) {
                    if (rs.next()) {
                        totalRoomCharge = rs.getBigDecimal("RoomCharge");
                    }
                }
            }

            // 2. Tính tiền dịch vụ 
            try (PreparedStatement psService = conn.prepareStatement(serviceChargeSql)) {
                psService.setInt(1, bookingId);
                try (ResultSet rs = psService.executeQuery()) {
                    if (rs.next()) {
                        BigDecimal charge = rs.getBigDecimal("ServiceCharge");
                        if (charge != null) {
                            totalServiceCharge = charge;
                        }
                    }
                }
            }

            // 3. Tính tổng cộng (chưa bao gồm thuế, giảm giá) 
            BigDecimal grandTotal = totalRoomCharge.add(totalServiceCharge);

            // 4. Tạo bản ghi hóa đơn chính
            try (PreparedStatement psInvoice = conn.prepareStatement(insertInvoiceSql, Statement.RETURN_GENERATED_KEYS)) {
                psInvoice.setInt(1, bookingId);
                psInvoice.setBigDecimal(2, totalRoomCharge);
                psInvoice.setBigDecimal(3, totalServiceCharge);
                psInvoice.setBigDecimal(4, grandTotal);
                psInvoice.setInt(5, userId);
                psInvoice.setInt(6, bookingId);
                
                int affectedRows = psInvoice.executeUpdate();
                if (affectedRows > 0) {
                    try (ResultSet generatedKeys = psInvoice.getGeneratedKeys()) {
                        if (generatedKeys.next()) {
                            invoiceId = generatedKeys.getInt(1);
                        }
                    }
                } else {
                    throw new SQLException("Tạo hóa đơn thất bại, không có hàng nào được thêm.");
                }
            }
            
            // (Tùy chọn) Thêm các bản ghi vào InvoiceDetails tại đây nếu cần

            conn.commit(); // Hoàn thành transaction
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback(); // Hoàn tác nếu có lỗi
            }
            throw e; // Ném lỗi ra ngoài
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true); // Trả lại trạng thái mặc định
                conn.close();
            }
        }
        
        return invoiceId;
    }

    /**
     * Cập nhật trạng thái thanh toán cho hóa đơn. 
     * @param invoiceId ID của hóa đơn.
     * @param paymentMethod Phương thức thanh toán (Tiền mặt, Thẻ...).
     * @return true nếu thành công.
     * @throws SQLException
     */
    public boolean processPayment(int invoiceId, String paymentMethod) throws SQLException {
        String sql = "UPDATE invoices SET PaymentMethod = ?, PaymentDate = NOW() WHERE InvoiceId = ?";
        String updateBookingSql = "UPDATE bookings SET PaymentStatus = 'Đã thanh toán' WHERE BookingId = (SELECT BookingId FROM invoices WHERE InvoiceId = ?)";

        try (Connection conn = DatabaseConnection.getConnection()) {
             conn.setAutoCommit(false); // Bắt đầu transaction
             
             boolean invoiceUpdated = false;
             boolean bookingUpdated = false;

             // Cập nhật hóa đơn
             try(PreparedStatement psInvoice = conn.prepareStatement(sql)) {
                 psInvoice.setString(1, paymentMethod);
                 psInvoice.setInt(2, invoiceId);
                 invoiceUpdated = psInvoice.executeUpdate() > 0;
             }
             
             // Cập nhật trạng thái booking
             try(PreparedStatement psBooking = conn.prepareStatement(updateBookingSql)) {
                 psBooking.setInt(1, invoiceId);
                 bookingUpdated = psBooking.executeUpdate() > 0;
             }

             if(invoiceUpdated && bookingUpdated) {
                 conn.commit();
                 return true;
             } else {
                 conn.rollback();
                 return false;
             }

        } catch (SQLException e) {
            // Lỗi sẽ tự rollback nếu có
            throw e;
        }
    }
}
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package qlks;

//import static btl_thlt_java.MuonTra.setupTableAppearance;
import com.mysql.cj.result.Row;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTextArea;
import javax.swing.JTextPane;


/**
 *
 * @author Admin
 */
public class HomePage extends RoundedFrame {
    private int mouseX, mouseY;

    /**
     * Creates new form HomePage
     */
    
    public HomePage() {
        super("Phần mềm quản lý khách sạn", 30);
        initComponents();
        if ("Lễ tân".equals(UserInfo.loggedInRole)) {
        staff.setEnabled(false);
        room.setEnabled(false);
        service.setEnabled(false);
        statistic.setVisible(false);
        } else if ("Quản lý".equals(UserInfo.loggedInRole))
        {
        staff.setEnabled(false);  
        }
        loadAllInvoices();
        updateRoomButtonColors(this);
        loadDashboardToTextArea(textActivities);
        displayUsername.setText(UserInfo.loggedInUsername) ;
    }
        public void setSelectedTab(int tabIndex) {
        if (tabIndex >= 0 && tabIndex < jTabbedPane1.getTabCount()) {
            jTabbedPane1.setSelectedIndex(tabIndex);
        }
    }
    public static void loadDashboardToTextArea(JTextPane txtPane) {
    try (Connection conn = DatabaseConnection.getConnection()) {
        StringBuilder sb = new StringBuilder("<html><body style='font-family:Segoe UI;font-size:12px;'>");

        // --- 1. Hoạt động trong ngày ---
sb.append("<b><span style='color:green;'>🟢 Hoạt động trong ngày:</span></b><br><br>");

// 1. Đã check-in hôm nay
sb.append("<u>✔ Đã check-in hôm nay:</u><br>");
String sqlTodayCheckIn = """
    SELECT c.FullName, r.RoomNumber, b.Status
    FROM bookings b
    JOIN customers c ON b.CustomerId = c.CustomerId
    JOIN rooms r ON b.RoomId = r.RoomId
    WHERE DATE(b.ActualCheckInDate) = CURDATE()
""";
try (PreparedStatement stmt = conn.prepareStatement(sqlTodayCheckIn);
     ResultSet rs = stmt.executeQuery()) {
    int count = 0;
    while (rs.next()) {
        count++;
        sb.append(String.format("- %s | Phòng %s | %s<br>",
                rs.getString("FullName"),
                rs.getString("RoomNumber"),
                rs.getString("Status")));
    }
    if (count == 0) sb.append("<i>Không có.</i><br>");
}

// 2. Đã check-out hôm nay
sb.append("<br><u>✔ Đã check-out hôm nay:</u><br>");
String sqlTodayCheckOut = """
    SELECT c.FullName, r.RoomNumber, b.Status
    FROM bookings b
    JOIN customers c ON b.CustomerId = c.CustomerId
    JOIN rooms r ON b.RoomId = r.RoomId
    WHERE DATE(b.ActualCheckOutDate) = CURDATE()
""";
try (PreparedStatement stmt = conn.prepareStatement(sqlTodayCheckOut);
     ResultSet rs = stmt.executeQuery()) {
    int count = 0;
    while (rs.next()) {
        count++;
        sb.append(String.format("- %s | Phòng %s | %s<br>",
                rs.getString("FullName"),
                rs.getString("RoomNumber"),
                rs.getString("Status")));
    }
    if (count == 0) sb.append("<i>Không có.</i><br>");
}

        // --- 2. Dự kiến Check-in ---
        sb.append("<br><b><span style='color:#007bff;'>📥 Dự kiến Check-in:</span></b><br>");
        String sqlExpectedCheckIn = """
            SELECT c.FullName, r.RoomNumber, b.Status
            FROM bookings b
            JOIN customers c ON b.CustomerId = c.CustomerId
            JOIN rooms r ON b.RoomId = r.RoomId
            WHERE DATE(b.CheckInDate) = CURDATE() AND b.ActualCheckInDate IS NULL
        """;
        try (PreparedStatement stmt = conn.prepareStatement(sqlExpectedCheckIn);
             ResultSet rs = stmt.executeQuery()) {
            int count = 0;
            while (rs.next()) {
                count++;
                sb.append(String.format("- %s | Phòng %s | Trạng thái: %s<br>",
                        rs.getString("FullName"),
                        rs.getString("RoomNumber"),
                        rs.getString("Status")));
            }
            if (count == 0) sb.append("<i>Không có.</i><br>");
        }

        // --- 3. Dự kiến Check-out ---
        sb.append("<br><b><span style='color:#dc3545;'>📤 Dự kiến Check-out:</span></b><br>");
        String sqlExpectedCheckOut = """
            SELECT c.FullName, r.RoomNumber, b.Status
            FROM bookings b
            JOIN customers c ON b.CustomerId = c.CustomerId
            JOIN rooms r ON b.RoomId = r.RoomId
            WHERE DATE(b.CheckOutDate) = CURDATE() AND b.ActualCheckOutDate IS NULL
        """;
        try (PreparedStatement stmt = conn.prepareStatement(sqlExpectedCheckOut);
             ResultSet rs = stmt.executeQuery()) {
            int count = 0;
            while (rs.next()) {
                count++;
                sb.append(String.format("- %s | Phòng %s | Trạng thái: %s<br>",
                        rs.getString("FullName"),
                        rs.getString("RoomNumber"),
                        rs.getString("Status")));
            }
            if (count == 0) sb.append("<i>Không có.</i><br>");
        }

        sb.append("</body></html>");
        txtPane.setContentType("text/html"); // để hiểu HTML
        txtPane.setText(sb.toString());
        txtPane.setCaretPosition(0); // scroll lên đầu

    } catch (SQLException e) {
        e.printStackTrace();
        txtPane.setText("❌ Lỗi khi tải dữ liệu: " + e.getMessage());
    }
}


    public static void updateRoomButtonColors(HomePage homepage) {
    try (Connection conn = DatabaseConnection.getConnection()) {
        String sql = "SELECT RoomNumber, Status FROM rooms";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        Map<String, String> roomStatusMap = new HashMap<>();
        while (rs.next()) {
            String roomNumber = rs.getString("RoomNumber");
            String status = rs.getString("Status");
            roomStatusMap.put(roomNumber, status);
        }

        Class<?> cls = HomePage.class;

        for (Map.Entry<String, String> entry : roomStatusMap.entrySet()) {
            String roomNumber = entry.getKey();
            String status = entry.getValue();
            String fieldName = "p" + roomNumber;

            try {
                Field field = cls.getDeclaredField(fieldName);
                field.setAccessible(true);
                Object obj = field.get(homepage); // ✅ đúng đối tượng chứa p101

                if (obj instanceof JButton btn) {
    switch (status.toLowerCase()) {
        case "available" -> btn.setBackground(new java.awt.Color(34, 197, 94));
        case "booked"    -> btn.setBackground(new java.awt.Color(239, 68, 68));
        default           -> btn.setBackground(new java.awt.Color(107, 114, 128));
    }

    btn.setOpaque(true);
    btn.setContentAreaFilled(true);
    btn.setBorderPainted(false);

    System.out.println("✔ " + fieldName + " -> " + status);
}
            } catch (NoSuchFieldException e) {
                System.out.println("⚠ Không tìm thấy nút: " + fieldName);
            }
        }
    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Lỗi khi cập nhật trạng thái phòng: " + e.getMessage());
    }
}
public void handleRoomClick(String roomNumber, HomePage homepage) {
    String status = "";
    String type = "";
    double price = 0;
    String description = "";

    try (Connection conn = DatabaseConnection.getConnection()) {
        String sql = "SELECT * FROM rooms WHERE RoomNumber = ?";
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, roomNumber);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            status = rs.getString("Status");
            type = rs.getString("RoomType");
            price = rs.getDouble("PricePerNight");
            description = rs.getString("RoomDescription");
        } else {
            JOptionPane.showMessageDialog(null, "Không tìm thấy thông tin phòng.");
            return;
        }
    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Lỗi truy vấn: " + e.getMessage());
        return;
    }

    // 📝 Tạo thông tin hiển thị
    StringBuilder info = new StringBuilder();
    info.append("📌 Thông tin phòng: \n")
        .append("- Số phòng: ").append(roomNumber).append("\n")
        .append("- Loại phòng: ").append(type).append("\n")
        .append("- Giá mỗi đêm: ").append(String.format("%,.0f VND", price)).append("\n")
        .append("- Trạng thái: ").append(status).append("\n")
        .append("- Mô tả: ").append(description == null ? "Không có" : description).append("\n\n");

    switch (status.toLowerCase()) {
        case "booked" -> {
            int confirm = JOptionPane.showConfirmDialog(null,
                    info + "Phòng đang được đặt.\nBạn có muốn trả phòng không?",
                    "Thông tin phòng " + roomNumber, JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                QLHoaDon hd = new QLHoaDon();
            hd.setVisible(true);
            this.dispose();// ✅ Gọi trang thanh toán
            }
        }

        case "available" -> {
            int confirm = JOptionPane.showConfirmDialog(null,
                    info + "Phòng đang trống.\nBạn có muốn đặt phòng không?",
                    "Thông tin phòng " + roomNumber, JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                
AddForm form = new AddForm();
                form.setVisible(true); // ✅ Gọi trang đặt phòng
            }
        }

        default -> {
            JOptionPane.showMessageDialog(null, info.toString(), "Thông tin phòng", JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
private void setupRoomMenu(JButton button, String roomType, HomePage homepage) {
    button.addActionListener(e -> {
        JPopupMenu popup = new JPopupMenu();

        try (Connection conn = DatabaseConnection.getConnection()) {
            String sql = "SELECT RoomNumber, Status, PricePerNight FROM rooms WHERE RoomType = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, roomType);
            ResultSet rs = ps.executeQuery();

            boolean found = false;
            while (rs.next()) {
                String roomNumber = rs.getString("RoomNumber");
                String status = rs.getString("Status");
                double price = rs.getDouble("PricePerNight");

                String label = String.format("Phòng %s - %,d VND [%s]", roomNumber, (int) price, status);

                JMenuItem item = new JMenuItem(label);
                item.addActionListener(ev -> handleRoomClick(roomNumber, homepage));
                popup.add(item);
                found = true;
            }

            if (!found) {
                JMenuItem item = new JMenuItem("Không có phòng nào.");
                item.setEnabled(false);
                popup.add(item);
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        popup.show(button, 0, button.getHeight());
    });
}
public void loadAllInvoices() {
    DefaultTableModel model = (DefaultTableModel) tHoaDon.getModel();
    model.setRowCount(0); // Xoá dữ liệu cũ

    String sql = "SELECT * FROM invoices ORDER BY PaymentDate DESC";
    int invoiceCount = 0;
    double totalRevenue = 0;

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            int invoiceId = rs.getInt("InvoiceId");
            int bookingId = rs.getInt("BookingId");
            int customerId = rs.getInt("CustomerId");
            Date issueDate = rs.getDate("IssueDate");
            double roomCharge = rs.getDouble("TotalRoomCharge");
            double serviceCharge = rs.getDouble("TotalServiceCharge");
            double total = rs.getDouble("GrandTotal");
            String method = rs.getString("PaymentMethod");
            Date paymentDate = rs.getDate("PaymentDate");
            int userId = rs.getInt("IssuedByUserId");

            model.addRow(new Object[]{
                invoiceId, bookingId, customerId,
                issueDate, roomCharge, serviceCharge,
                total, method, paymentDate, userId
            });

            invoiceCount++;
            totalRevenue += total;
        }

        lblTotalInvoices.setText("Số hóa đơn: " + invoiceCount);
        lblGrandTotal.setText("Tổng doanh thu: " + String.format("%,.0f VND", totalRevenue));

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Lỗi khi tải hóa đơn: " + e.getMessage());
    }
}
public void exportStatisticsToTextFile() {
    DefaultTableModel model = (DefaultTableModel) tHoaDon.getModel();
    if (model.getRowCount() == 0) {
        JOptionPane.showMessageDialog(null, "Không có dữ liệu để xuất.");
        return;
    }

    String desktopPath = System.getProperty("user.home") + "\\Desktop\\ThongKe";
    File folder = new File(desktopPath);
    if (!folder.exists()) {
        folder.mkdirs();
    }

    String fileName = "ThongKeHoaDon_" + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")) + ".txt";

    File file = new File(folder, fileName);

    try (PrintWriter writer = new PrintWriter(file)) {
        writer.println("===== THỐNG KÊ HÓA ĐƠN =====");
        writer.println("Ngày tạo: " + LocalDate.now());
        writer.println("Tổng số hóa đơn: " + model.getRowCount());
        writer.println();

        double totalRevenue = 0;

        for (int i = 0; i < model.getRowCount(); i++) {
            int maHD = Integer.parseInt(model.getValueAt(i, 0).toString());
            int bookingId = Integer.parseInt(model.getValueAt(i, 1).toString());
            int customerId = Integer.parseInt(model.getValueAt(i, 2).toString());
            String issueDate = model.getValueAt(i, 3).toString();
            double roomCharge = Double.parseDouble(model.getValueAt(i, 4).toString());
            double serviceCharge = Double.parseDouble(model.getValueAt(i, 5).toString());
            double grandTotal = Double.parseDouble(model.getValueAt(i, 6).toString());
            String method = model.getValueAt(i, 7).toString();
            String paymentDate = model.getValueAt(i, 8).toString();
            String userId = model.getValueAt(i, 9).toString();

            // ➡️ DÒNG NGANG
            writer.printf(
                "#%d | KH: %d | Booking: %d | Ngày: %s | Phòng: %,.0f | DV: %,.0f | Tổng: %,.0f | TT: %s | NV: %s%n",
                maHD, customerId, bookingId, issueDate, roomCharge, serviceCharge, grandTotal, paymentDate, userId
            );

            totalRevenue += grandTotal;
        }

        writer.println("\nTỔNG DOANH THU: " + String.format("%,.0f VND", totalRevenue));

        JOptionPane.showMessageDialog(null, "Xuất thống kê thành công:\n" + file.getAbsolutePath());
    } catch (IOException e) {
        JOptionPane.showMessageDialog(null, "Lỗi ghi file thống kê: " + e.getMessage());
    }
}
public void filterInvoices() {
    Date fromDate = jdcFrom.getDate();
    Date toDate = jdcTo.getDate();
    String selectedPayment = cmbPayment.getSelectedItem().toString();

    DefaultTableModel model = (DefaultTableModel) tHoaDon.getModel();
    model.setRowCount(0); // clear old data

    StringBuilder sql = new StringBuilder("SELECT * FROM invoices WHERE 1=1");
    List<Object> params = new ArrayList<>();
    // Nếu có ngày bắt đầu
    if (fromDate != null) {
        sql.append(" AND PaymentDate >= ?");
        params.add(new java.sql.Date(fromDate.getTime()));
    }
    // Nếu có ngày kết thúc
    if (toDate != null) {
        sql.append(" AND PaymentDate <= ?");
        params.add(new java.sql.Date(toDate.getTime()));
    }
    // Nếu không phải là "Tất cả"
    if (!selectedPayment.equals("Tất cả")) {
        sql.append(" AND PaymentMethod = ?");
        params.add(selectedPayment);
    }

    sql.append(" ORDER BY PaymentDate DESC");

    int invoiceCount = 0;
    double totalRevenue = 0;

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql.toString())) {

        // Gán tham số động
        for (int i = 0; i < params.size(); i++) {
            ps.setObject(i + 1, params.get(i));
        }

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getInt("InvoiceId"),
                rs.getInt("BookingId"),
                rs.getInt("CustomerId"),
                rs.getDate("IssueDate"),
                rs.getDouble("TotalRoomCharge"),
                rs.getDouble("TotalServiceCharge"),
                rs.getDouble("GrandTotal"),
                rs.getString("PaymentMethod"),
                rs.getDate("PaymentDate"),
                rs.getInt("IssuedByUserId")
            });

            invoiceCount++;
            totalRevenue += rs.getDouble("GrandTotal");
        }

        lblTotalInvoices.setText("Số hóa đơn: " + invoiceCount);
        lblGrandTotal.setText("Tổng doanh thu: " + String.format("%,.0f VND", totalRevenue));

        if (invoiceCount == 0) {
            JOptionPane.showMessageDialog(null, "Không tìm thấy hóa đơn phù hợp.");
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Lỗi khi lọc: " + e.getMessage());
    }
}






    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem1 = new javax.swing.JMenuItem();
        jLabel8 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        signout = new javax.swing.JLabel();
        displayUsername = new javax.swing.JLabel();
        Logo = new javax.swing.JLabel();
        info = new qlks.HoverLabel("Your Text Here")
        ;
        homepage = new qlks.HoverLabel("Your Text Here")
        ;
        manage = new qlks.HoverLabel("Your Text Here")
        ;
        statistic = new qlks.HoverLabel("Your Text Here")
        ;
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        close = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel8 = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        pnlRoomLayout = new javax.swing.JPanel();
        p502 = new javax.swing.JButton();
        Standard = new javax.swing.JButton();
        p501 = new javax.swing.JButton();
        p401 = new javax.swing.JButton();
        p402 = new javax.swing.JButton();
        p403 = new javax.swing.JButton();
        p303 = new javax.swing.JButton();
        p203 = new javax.swing.JButton();
        p202 = new javax.swing.JButton();
        p201 = new javax.swing.JButton();
        p301 = new javax.swing.JButton();
        p302 = new javax.swing.JButton();
        p102 = new javax.swing.JButton();
        p101 = new javax.swing.JButton();
        p103 = new javax.swing.JButton();
        jLabel6 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jTextField4 = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        p503 = new javax.swing.JButton();
        Suite = new javax.swing.JButton();
        Deluxe = new javax.swing.JButton();
        jTextField7 = new javax.swing.JTextField();
        refresh = new javax.swing.JLabel();
        jPanel3 = new javax.swing.JPanel();
        jButton6 = new RoundedButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        textActivities = new javax.swing.JTextPane();
        jPanel4 = new javax.swing.JPanel();
        btnAddCustomer = new RoundedButton();
        btnAddBooking = new RoundedButton();
        jPanel9 = new javax.swing.JPanel();
        billing = new qlks.RoundedButton();
        booking = new qlks.RoundedButton();
        jPanel6 = new javax.swing.JPanel();
        service = new qlks.RoundedButton();
        staff = new qlks.RoundedButton();
        room = new qlks.RoundedButton();
        customer = new qlks.RoundedButton();
        jPanel10 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        tHoaDon = new ThemedTable();
        jdcFrom = new com.toedter.calendar.JDateChooser();
        jdcTo = new com.toedter.calendar.JDateChooser();
        lblGrandTotal = new javax.swing.JLabel();
        btnExport = new RoundedButton();
        lTotal1 = new javax.swing.JLabel();
        lblTotalInvoices = new javax.swing.JLabel();
        lTotal3 = new javax.swing.JLabel();
        dichvu = new RoundedButton();
        cmbPayment = new javax.swing.JComboBox<>();
        lTotal2 = new javax.swing.JLabel();
        btnLoc = new RoundedButton();
        doanhthu = new RoundedButton();
        jPanel11 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextPane1 = new javax.swing.JTextPane();

        jMenuItem1.setText("jMenuItem1");

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel8.setText("\t Suite");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Hotel management system ");
        setBackground(new java.awt.Color(252, 244, 234));
        setIconImage(new ImageIcon(getClass().getResource("/Icon/Title.png")).getImage());
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel1.setBackground(new java.awt.Color(9, 38, 41));
        jPanel1.setForeground(new java.awt.Color(51, 51, 51));
        jPanel1.setPreferredSize(new java.awt.Dimension(180, 700));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        signout.setBackground(new java.awt.Color(252, 244, 234));
        signout.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        signout.setForeground(new java.awt.Color(255, 255, 255));
        signout.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        signout.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/arrow.png"))); // NOI18N
        signout.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        signout.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                signoutMouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                signoutMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                signoutMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                signoutMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                signoutMouseReleased(evt);
            }
        });
        jPanel1.add(signout, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 640, 60, 60));

        displayUsername.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        displayUsername.setForeground(new java.awt.Color(255, 255, 255));
        displayUsername.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        displayUsername.setText("      Username");
        displayUsername.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jPanel1.add(displayUsername, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 640, 130, 60));

        Logo.setBackground(new java.awt.Color(252, 244, 234));
        Logo.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Logo.setForeground(new java.awt.Color(255, 255, 255));
        Logo.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Logo.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/hotel.png"))); // NOI18N
        Logo.setText("HOTEL MANAGEMENT");
        Logo.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        Logo.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        Logo.setInheritsPopupMenu(false);
        Logo.setRequestFocusEnabled(false);
        Logo.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jPanel1.add(Logo, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 190, 120));

        info.setBackground(new java.awt.Color(9, 38, 41));
        info.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        info.setForeground(new java.awt.Color(255, 255, 255));
        info.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        info.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/info.png"))); // NOI18N
        info.setText("Thông tin");
        info.setToolTipText("");
        info.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        info.setFocusable(false);
        info.setOpaque(true);
        info.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                infoMouseClicked(evt);
            }
        });
        jPanel1.add(info, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 300, 190, 60));

        homepage.setBackground(new java.awt.Color(9, 38, 41));
        homepage.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        homepage.setForeground(new java.awt.Color(255, 255, 255));
        homepage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        homepage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/home.png"))); // NOI18N
        homepage.setText("Trang chủ");
        homepage.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        homepage.setOpaque(true);
        homepage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                homepageMouseClicked(evt);
            }
        });
        jPanel1.add(homepage, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 120, 190, 60));

        manage.setBackground(new java.awt.Color(9, 38, 41));
        manage.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        manage.setForeground(new java.awt.Color(255, 255, 255));
        manage.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        manage.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/setting.png"))); // NOI18N
        manage.setText("  Quản lý");
        manage.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        manage.setOpaque(true);
        manage.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                manageMouseClicked(evt);
            }
        });
        jPanel1.add(manage, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 180, 190, 60));

        statistic.setBackground(new java.awt.Color(9, 38, 41));
        statistic.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        statistic.setForeground(new java.awt.Color(255, 255, 255));
        statistic.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        statistic.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/growth.png"))); // NOI18N
        statistic.setText("Thống kê");
        statistic.setToolTipText("");
        statistic.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        statistic.setFocusable(false);
        statistic.setOpaque(true);
        statistic.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                statisticMouseClicked(evt);
            }
        });
        jPanel1.add(statistic, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 240, 190, 60));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 190, 700));

        jPanel2.setBackground(new java.awt.Color(252, 244, 234));
        jPanel2.setForeground(new java.awt.Color(0, 51, 102));
        jPanel2.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                jPanel2MouseDragged(evt);
            }
        });
        jPanel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jPanel2MousePressed(evt);
            }
        });
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/floral.png"))); // NOI18N
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 0, 250, 60));

        close.setBackground(new java.awt.Color(252, 244, 234));
        close.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        close.setForeground(new java.awt.Color(0, 77, 79));
        close.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        close.setText("X");
        close.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        close.setOpaque(true);
        close.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                closeMouseClicked(evt);
            }
        });
        jPanel2.add(close, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 0, 60, 60));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/floral.png"))); // NOI18N
        jPanel2.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 0, 250, 60));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/floral.png"))); // NOI18N
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 0, 250, 60));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/floral.png"))); // NOI18N
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 0, 250, 60));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 60));

        jTabbedPane1.setBackground(new java.awt.Color(255, 255, 255));

        jPanel8.setBackground(new java.awt.Color(252, 244, 234));
        jPanel8.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jPanel8.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(130, 20, -1, -1));

        pnlRoomLayout.setBackground(new java.awt.Color(255, 255, 255));
        pnlRoomLayout.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 77, 79), 2, true), "Sơ  đồ phòng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(0, 77, 79))); // NOI18N
        pnlRoomLayout.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        p502.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        p502.setText("502");
        p502.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        p502.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        p502.setPreferredSize(new java.awt.Dimension(200, 100));
        p502.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        p502.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                p502ActionPerformed(evt);
            }
        });
        pnlRoomLayout.add(p502, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 20, 100, -1));

        Standard.setBackground(new java.awt.Color(9, 38, 41));
        Standard.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Standard.setForeground(new java.awt.Color(255, 255, 255));
        Standard.setText("Standard");
        Standard.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Standard.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        Standard.setPreferredSize(new java.awt.Dimension(200, 100));
        Standard.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        Standard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                StandardActionPerformed(evt);
            }
        });
        pnlRoomLayout.add(Standard, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 350, 100, 210));

        p501.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        p501.setText("501");
        p501.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        p501.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        p501.setPreferredSize(new java.awt.Dimension(200, 100));
        p501.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        p501.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                p501ActionPerformed(evt);
            }
        });
        pnlRoomLayout.add(p501, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 100, -1));

        p401.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        p401.setText("401");
        p401.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        p401.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        p401.setPreferredSize(new java.awt.Dimension(200, 100));
        p401.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        p401.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                p401ActionPerformed(evt);
            }
        });
        pnlRoomLayout.add(p401, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 130, 100, -1));

        p402.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        p402.setText("402");
        p402.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        p402.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        p402.setPreferredSize(new java.awt.Dimension(200, 100));
        p402.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        p402.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                p402ActionPerformed(evt);
            }
        });
        pnlRoomLayout.add(p402, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 130, 100, -1));

        p403.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        p403.setText("403");
        p403.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        p403.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        p403.setPreferredSize(new java.awt.Dimension(200, 100));
        p403.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        p403.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                p403ActionPerformed(evt);
            }
        });
        pnlRoomLayout.add(p403, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 130, 100, -1));

        p303.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        p303.setText("303");
        p303.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        p303.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        p303.setPreferredSize(new java.awt.Dimension(200, 100));
        p303.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        p303.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                p303ActionPerformed(evt);
            }
        });
        pnlRoomLayout.add(p303, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 240, 100, -1));

        p203.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        p203.setText("203");
        p203.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        p203.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        p203.setPreferredSize(new java.awt.Dimension(200, 100));
        p203.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        p203.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                p203ActionPerformed(evt);
            }
        });
        pnlRoomLayout.add(p203, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 350, 100, -1));

        p202.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        p202.setText("202");
        p202.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        p202.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        p202.setPreferredSize(new java.awt.Dimension(200, 100));
        p202.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        p202.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                p202ActionPerformed(evt);
            }
        });
        pnlRoomLayout.add(p202, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 350, 100, -1));

        p201.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        p201.setText("201");
        p201.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        p201.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        p201.setPreferredSize(new java.awt.Dimension(200, 100));
        p201.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        p201.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                p201ActionPerformed(evt);
            }
        });
        pnlRoomLayout.add(p201, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 350, 100, -1));

        p301.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        p301.setText("301");
        p301.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        p301.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        p301.setPreferredSize(new java.awt.Dimension(200, 100));
        p301.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        p301.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                p301ActionPerformed(evt);
            }
        });
        pnlRoomLayout.add(p301, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 240, 100, -1));

        p302.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        p302.setText("302");
        p302.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        p302.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        p302.setPreferredSize(new java.awt.Dimension(200, 100));
        p302.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        p302.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                p302ActionPerformed(evt);
            }
        });
        pnlRoomLayout.add(p302, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 240, 100, -1));

        p102.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        p102.setText("102");
        p102.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        p102.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        p102.setPreferredSize(new java.awt.Dimension(200, 100));
        p102.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        p102.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                p102ActionPerformed(evt);
            }
        });
        pnlRoomLayout.add(p102, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 460, 100, -1));

        p101.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        p101.setText("101");
        p101.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        p101.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        p101.setPreferredSize(new java.awt.Dimension(200, 100));
        p101.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        p101.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                p101ActionPerformed(evt);
            }
        });
        pnlRoomLayout.add(p101, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 460, 100, -1));

        p103.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        p103.setText("103");
        p103.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        p103.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        p103.setPreferredSize(new java.awt.Dimension(200, 100));
        p103.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        p103.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                p103ActionPerformed(evt);
            }
        });
        pnlRoomLayout.add(p103, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 460, 100, -1));

        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Phòng trống");
        pnlRoomLayout.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 590, 80, 20));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        pnlRoomLayout.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 440, -1, -1));

        jTextField4.setEditable(false);
        jTextField4.setBackground(new java.awt.Color(34, 197, 94));
        jTextField4.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        pnlRoomLayout.add(jTextField4, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 590, 100, -1));

        jLabel11.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel11.setText("Phòng đã được đặt");
        pnlRoomLayout.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 570, 120, 20));

        p503.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        p503.setText("503");
        p503.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        p503.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        p503.setPreferredSize(new java.awt.Dimension(200, 100));
        p503.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        p503.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                p503ActionPerformed(evt);
            }
        });
        pnlRoomLayout.add(p503, new org.netbeans.lib.awtextra.AbsoluteConstraints(230, 20, 100, -1));

        Suite.setBackground(new java.awt.Color(9, 38, 41));
        Suite.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Suite.setForeground(new java.awt.Color(255, 255, 255));
        Suite.setText("Suite");
        Suite.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Suite.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        Suite.setPreferredSize(new java.awt.Dimension(200, 100));
        Suite.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        Suite.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SuiteActionPerformed(evt);
            }
        });
        pnlRoomLayout.add(Suite, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 20, 100, 100));

        Deluxe.setBackground(new java.awt.Color(9, 38, 41));
        Deluxe.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        Deluxe.setForeground(new java.awt.Color(255, 255, 255));
        Deluxe.setText("Deluxe");
        Deluxe.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Deluxe.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        Deluxe.setPreferredSize(new java.awt.Dimension(200, 100));
        Deluxe.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        Deluxe.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                DeluxeActionPerformed(evt);
            }
        });
        pnlRoomLayout.add(Deluxe, new org.netbeans.lib.awtextra.AbsoluteConstraints(340, 130, 100, 210));

        jTextField7.setEditable(false);
        jTextField7.setBackground(new java.awt.Color(239, 68, 68));
        jTextField7.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jTextField7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jTextField7ActionPerformed(evt);
            }
        });
        pnlRoomLayout.add(jTextField7, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 570, 100, -1));

        refresh.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/refresh.png"))); // NOI18N
        refresh.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        refresh.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                refreshMouseClicked(evt);
            }
        });
        pnlRoomLayout.add(refresh, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 580, -1, -1));

        jPanel8.add(pnlRoomLayout, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 10, 450, 620));

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 77, 79), 2, true), "Hoạt động trong ngày", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(0, 77, 79))); // NOI18N
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jButton6.setForeground(new java.awt.Color(255, 255, 255));
        jButton6.setText("Hủy đặt");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });
        jPanel3.add(jButton6, new org.netbeans.lib.awtextra.AbsoluteConstraints(33, 469, 475, 80));

        jScrollPane2.setViewportView(textActivities);

        jPanel3.add(jScrollPane2, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 40, 430, 360));

        jPanel8.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 10, 490, 430));

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 77, 79), 2, true), "lối tắt", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(0, 77, 79))); // NOI18N
        jPanel4.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        btnAddCustomer.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnAddCustomer.setForeground(new java.awt.Color(255, 255, 255));
        btnAddCustomer.setText("Thêm khách hàng");
        btnAddCustomer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAddCustomer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddCustomerActionPerformed(evt);
            }
        });
        jPanel4.add(btnAddCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 30, 220, 130));

        btnAddBooking.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnAddBooking.setForeground(new java.awt.Color(255, 255, 255));
        btnAddBooking.setText("Đặt phòng");
        btnAddBooking.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAddBooking.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddBookingActionPerformed(evt);
            }
        });
        jPanel4.add(btnAddBooking, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 30, 220, 130));

        jPanel8.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 450, 490, 180));

        jTabbedPane1.addTab("tab1", jPanel8);

        jPanel9.setBackground(new java.awt.Color(252, 244, 234));
        jPanel9.setForeground(new java.awt.Color(255, 255, 255));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        billing.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        billing.setForeground(new java.awt.Color(255, 255, 255));
        billing.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/billing.png"))); // NOI18N
        billing.setText("Quản lý dịch vụ");
        billing.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        billing.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        billing.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        billing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                billingActionPerformed(evt);
            }
        });
        jPanel9.add(billing, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 330, 430, 290));

        booking.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        booking.setForeground(new java.awt.Color(255, 255, 255));
        booking.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/booking.png"))); // NOI18N
        booking.setText("Quản lý đặt phòng");
        booking.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        booking.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        booking.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        booking.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bookingActionPerformed(evt);
            }
        });
        jPanel9.add(booking, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 20, 430, 290));

        jPanel6.setBackground(new java.awt.Color(253, 251, 246));
        jPanel6.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 77, 79), 2, true), "Quản lý danh mục", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(0, 77, 79))); // NOI18N
        jPanel6.setOpaque(false);
        jPanel6.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        service.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        service.setForeground(new java.awt.Color(255, 255, 255));
        service.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/service.png"))); // NOI18N
        service.setText(" Dịch vụ");
        service.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        service.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        service.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        service.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                serviceActionPerformed(evt);
            }
        });
        jPanel6.add(service, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 310, 250, 280));

        staff.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        staff.setForeground(new java.awt.Color(255, 255, 255));
        staff.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/profile.png"))); // NOI18N
        staff.setText("Người dùng");
        staff.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        staff.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        staff.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        staff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                staffActionPerformed(evt);
            }
        });
        jPanel6.add(staff, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 30, 240, 270));

        room.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        room.setForeground(new java.awt.Color(255, 255, 255));
        room.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/room.png"))); // NOI18N
        room.setText("Phòng");
        room.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        room.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        room.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        room.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                roomActionPerformed(evt);
            }
        });
        jPanel6.add(room, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 310, 240, 280));

        customer.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        customer.setForeground(new java.awt.Color(255, 255, 255));
        customer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/customer.png"))); // NOI18N
        customer.setText("Khách hàng");
        customer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        customer.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        customer.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        customer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerActionPerformed(evt);
            }
        });
        jPanel6.add(customer, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 30, 250, 270));

        jPanel9.add(jPanel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 20, 520, 600));

        jTabbedPane1.addTab("tab2", jPanel9);

        jPanel10.setBackground(new java.awt.Color(252, 244, 234));
        jPanel10.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel5.setBackground(new java.awt.Color(253, 251, 246));
        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 77, 79), 2, true), "Thống kê hóa đơn", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(0, 77, 79))); // NOI18N
        jPanel5.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        tHoaDon.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null, null, null, null}
            },
            new String [] {
                "Mã hoá đơn ", "Mã đặt phòng ", "Mã khách hàng ", "Ngày lập", "Tiền phòng ", "Tiền dịch vụ ", "Tổng cộng ", "Phương thức TT", "Ngày TT", "Mã nhân viên"
            }
        ));
        tHoaDon.setEnabled(false);
        jScrollPane1.setViewportView(tHoaDon);

        jPanel5.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 120, 970, 320));
        jPanel5.add(jdcFrom, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 50, 240, 40));
        jPanel5.add(jdcTo, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 50, 230, 40));

        lblGrandTotal.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblGrandTotal.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblGrandTotal.setText("Tổng doanh thu");
        jPanel5.add(lblGrandTotal, new org.netbeans.lib.awtextra.AbsoluteConstraints(210, 440, 300, 40));

        btnExport.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnExport.setForeground(new java.awt.Color(255, 255, 255));
        btnExport.setText("Xuất");
        btnExport.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnExport.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnExportActionPerformed(evt);
            }
        });
        jPanel5.add(btnExport, new org.netbeans.lib.awtextra.AbsoluteConstraints(730, 50, 80, 40));

        lTotal1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lTotal1.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lTotal1.setText("Phương thức TT");
        jPanel5.add(lTotal1, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 20, -1, 30));

        lblTotalInvoices.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lblTotalInvoices.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        lblTotalInvoices.setText("Số hóa đơn");
        jPanel5.add(lblTotalInvoices, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 440, 200, 40));

        lTotal3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lTotal3.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lTotal3.setText("Từ ngày");
        jPanel5.add(lTotal3, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, -1, 30));

        dichvu.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        dichvu.setForeground(new java.awt.Color(255, 255, 255));
        dichvu.setText("Báo cáo dịch vụ");
        dichvu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        dichvu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dichvuActionPerformed(evt);
            }
        });
        jPanel5.add(dichvu, new org.netbeans.lib.awtextra.AbsoluteConstraints(310, 510, 250, 80));

        cmbPayment.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Tất cả", "Tiền mặt", "Chuyển khoản", "Thẻ tín dụng" }));
        jPanel5.add(cmbPayment, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 50, 130, 40));

        lTotal2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        lTotal2.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lTotal2.setText("Đến ngày");
        jPanel5.add(lTotal2, new org.netbeans.lib.awtextra.AbsoluteConstraints(260, 20, -1, 30));

        btnLoc.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnLoc.setForeground(new java.awt.Color(255, 255, 255));
        btnLoc.setText("Lọc");
        btnLoc.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLoc.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLocActionPerformed(evt);
            }
        });
        jPanel5.add(btnLoc, new org.netbeans.lib.awtextra.AbsoluteConstraints(640, 50, 80, 40));

        doanhthu.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        doanhthu.setForeground(new java.awt.Color(255, 255, 255));
        doanhthu.setText("Báo cáo doanh thu");
        doanhthu.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        doanhthu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                doanhthuActionPerformed(evt);
            }
        });
        jPanel5.add(doanhthu, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 510, 250, 80));

        jPanel10.add(jPanel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(10, 20, 990, 620));

        jTabbedPane1.addTab("tab3", jPanel10);

        jPanel11.setBackground(new java.awt.Color(252, 244, 234));
        jPanel11.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jTextPane1.setEditable(false);
        jTextPane1.setBackground(new java.awt.Color(253, 251, 246));
        jTextPane1.setContentType("text/html"); // NOI18N
        jTextPane1.setFont(new java.awt.Font("Segoe UI", 0, 24)); // NOI18N
        jTextPane1.setText("<html>        <body style='font-family: Times New Roman; color: black; font-size: 24pt;'>              <div style='text-align: center;'>                    <h2 style='margin-bottom: 5px;'>TRƯỜNG ĐẠI HỌC KINH TẾ - KỸ THUẬT CÔNG NGHIỆP</h2>                    <h3 style='margin-top: 0;'>KHOA CÔNG NGHỆ THÔNG TIN</h3>                    <hr style='width: 80%; border: 1px solid black;'><br/>                     <h1 style='margin: 10px 0; color: darkblue;'>ĐỒ ÁN 1</h1>                    <h2 style='text-decoration: underline; color: crimson;'>ĐỀ TÀI: Hệ thống quản lý khách sạn</h2><br/>                     <p style='text-align: left; margin-left: 150px;'>                          <b>Giảng viên hướng dẫn:</b> Th.S. Trần Thị Lan Anh<br/><br/>                          <b>Nhóm sinh viên thực hiện:</b><br/>                          &emsp;- Lương Minh Sơn<br/>                          &emsp;- Trương Doãn An<br/>                          &emsp;- Nguyễn Hữu Thành Danh<br/><br/>                          <b>Nhóm:</b> 3<br/>                          <b>Khoa:</b> Công nghệ thông tin                    </p>              </div>        </body>  </html> ");
        jTextPane1.setToolTipText("");
        jScrollPane3.setViewportView(jTextPane1);

        jPanel11.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(40, 30, 950, 590));

        jTabbedPane1.addTab("tab4", jPanel11);

        getContentPane().add(jTabbedPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(180, 20, 1020, 680));

        setSize(new java.awt.Dimension(1200, 700));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void closeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_closeMouseClicked
        int confirm = JOptionPane.showConfirmDialog(this, 
                "Bạn có chắc chắn muốn thoát ứng dụng?", 
                "Xác nhận Thoát", 
                JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0); 
        }
    }//GEN-LAST:event_closeMouseClicked

    private void jPanel2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MousePressed
            mouseX = evt.getX();
            mouseY = evt.getY();
    }//GEN-LAST:event_jPanel2MousePressed

    private void jPanel2MouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel2MouseDragged
            int x = evt.getXOnScreen();
    int y = evt.getYOnScreen();
    this.setLocation(x - mouseX, y - mouseY);
    }//GEN-LAST:event_jPanel2MouseDragged

    private void billingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_billingActionPerformed
        // TODO add your handling code here:
        QLHoaDon hd = new QLHoaDon();
            hd.setVisible(true);
            this.dispose();
    }//GEN-LAST:event_billingActionPerformed

    private void bookingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bookingActionPerformed
        // TODO add your handling code here:
        QLDatPhong dp = new QLDatPhong();
            dp.setVisible(true);
            this.dispose();
    }//GEN-LAST:event_bookingActionPerformed

    private void serviceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_serviceActionPerformed
        // TODO add your handling code here:
        new QLDichVu().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_serviceActionPerformed

    private void staffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_staffActionPerformed
        // TODO add your handling code here:
        new QLNguoiDung().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_staffActionPerformed

    private void roomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_roomActionPerformed
        // TODO add your handling code here:
        new QLPhong().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_roomActionPerformed

    private void customerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerActionPerformed
        // TODO add your handling code here:
        new QLKhachHang().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_customerActionPerformed

    private void signoutMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_signoutMouseClicked
        // TODO add your handling code here:
        new Login() .setVisible(true);
        this.dispose();
    }//GEN-LAST:event_signoutMouseClicked

    private void signoutMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_signoutMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_signoutMouseEntered

    private void signoutMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_signoutMouseExited
        // TODO add your handling code here:
    }//GEN-LAST:event_signoutMouseExited

    private void signoutMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_signoutMousePressed
        // TODO add your handling code here:
    }//GEN-LAST:event_signoutMousePressed

    private void signoutMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_signoutMouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_signoutMouseReleased

    private void homepageMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_homepageMouseClicked
        // TODO add your handling code here:
         jTabbedPane1.setSelectedIndex(0);
    }//GEN-LAST:event_homepageMouseClicked

    private void manageMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_manageMouseClicked
        // TODO add your handling code here:
         jTabbedPane1.setSelectedIndex(1);
    }//GEN-LAST:event_manageMouseClicked

    private void infoMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_infoMouseClicked
        // TODO add your handling code here:
         jTabbedPane1.setSelectedIndex(3);
    }//GEN-LAST:event_infoMouseClicked

    private void p502ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p502ActionPerformed
        // TODO add your handling code here:
        handleRoomClick("502", this);
    }//GEN-LAST:event_p502ActionPerformed

    private void StandardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_StandardActionPerformed
        // TODO add your handling code here:
        setupRoomMenu(Standard, "Standard", this);
    }//GEN-LAST:event_StandardActionPerformed

    private void p403ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p403ActionPerformed
        // TODO add your handling code here:
        handleRoomClick("403", this);
    }//GEN-LAST:event_p403ActionPerformed

    private void p402ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p402ActionPerformed
        // TODO add your handling code here:
        handleRoomClick("402", this);
    }//GEN-LAST:event_p402ActionPerformed

    private void p401ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p401ActionPerformed
        // TODO add your handling code here:
        handleRoomClick("401", this);
    }//GEN-LAST:event_p401ActionPerformed

    private void p303ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p303ActionPerformed
        // TODO add your handling code here:
        handleRoomClick("303", this);
    }//GEN-LAST:event_p303ActionPerformed

    private void p302ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p302ActionPerformed
        // TODO add your handling code here:
        handleRoomClick("302", this);
    }//GEN-LAST:event_p302ActionPerformed

    private void p301ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p301ActionPerformed
        // TODO add your handling code here:
        handleRoomClick("301", this);
    }//GEN-LAST:event_p301ActionPerformed

    private void p203ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p203ActionPerformed
        // TODO add your handling code here:
        handleRoomClick("203", this);
    }//GEN-LAST:event_p203ActionPerformed

    private void p202ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p202ActionPerformed
        // TODO add your handling code here:
        handleRoomClick("202", this);
    }//GEN-LAST:event_p202ActionPerformed

    private void p201ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p201ActionPerformed
        // TODO add your handling code here:
        handleRoomClick("201", this);
    }//GEN-LAST:event_p201ActionPerformed

    private void p103ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p103ActionPerformed
        // TODO add your handling code here:
        handleRoomClick("103", this);
    }//GEN-LAST:event_p103ActionPerformed

    private void p102ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p102ActionPerformed
        // TODO add your handling code here:
        handleRoomClick("102", this);
    }//GEN-LAST:event_p102ActionPerformed

    private void p101ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p101ActionPerformed
        // TODO add your handling code here:
        handleRoomClick("101", this);
    }//GEN-LAST:event_p101ActionPerformed

    private void p501ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p501ActionPerformed
        // TODO add your handling code here:
        handleRoomClick("501", this);
    }//GEN-LAST:event_p501ActionPerformed

    private void p503ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_p503ActionPerformed
        // TODO add your handling code here:
        handleRoomClick("503", this);
    }//GEN-LAST:event_p503ActionPerformed

    private void SuiteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SuiteActionPerformed
        // TODO add your handling code here:
        setupRoomMenu(Suite, "Suite", this);
    }//GEN-LAST:event_SuiteActionPerformed

    private void DeluxeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_DeluxeActionPerformed
        // TODO add your handling code here:
        setupRoomMenu(Deluxe, "Deluxe", this);
    }//GEN-LAST:event_DeluxeActionPerformed

    private void jTextField7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jTextField7ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jTextField7ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton6ActionPerformed

    private void btnAddCustomerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddCustomerActionPerformed
        // TODO add your handling code here:
        AddKhachHang form = new AddKhachHang();
        form.setVisible(true);

    }//GEN-LAST:event_btnAddCustomerActionPerformed

    private void btnAddBookingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddBookingActionPerformed
        // TODO add your handling code here:
                AddForm form = new AddForm();
        form.setVisible(true);

    }//GEN-LAST:event_btnAddBookingActionPerformed

    private void refreshMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_refreshMouseClicked
        // TODO add your handling code here:
        updateRoomButtonColors(this);

    }//GEN-LAST:event_refreshMouseClicked

    private void btnExportActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnExportActionPerformed
    exportStatisticsToTextFile();
    loadAllInvoices();
    }//GEN-LAST:event_btnExportActionPerformed

    private void dichvuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dichvuActionPerformed
        // TODO add your handling code here:
        Report report = new Report("dichvu");
            report.setVisible(true);
    }//GEN-LAST:event_dichvuActionPerformed

    private void statisticMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_statisticMouseClicked
        // TODO add your handling code here:
        jTabbedPane1.setSelectedIndex(2);
    }//GEN-LAST:event_statisticMouseClicked

    private void btnLocActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLocActionPerformed
        // TODO add your handling code here:
        filterInvoices();
    }//GEN-LAST:event_btnLocActionPerformed

    private void doanhthuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_doanhthuActionPerformed
        // TODO add your handling code here:
        Report report = new Report("doanhthu");
            report.setVisible(true);
    }//GEN-LAST:event_doanhthuActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(HomePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(HomePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(HomePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(HomePage.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new HomePage().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Deluxe;
    private javax.swing.JLabel Logo;
    private javax.swing.JButton Standard;
    private javax.swing.JButton Suite;
    private javax.swing.JButton billing;
    private javax.swing.JButton booking;
    private javax.swing.JButton btnAddBooking;
    private javax.swing.JButton btnAddCustomer;
    private javax.swing.JButton btnExport;
    private javax.swing.JButton btnLoc;
    private javax.swing.JLabel close;
    private javax.swing.JComboBox<String> cmbPayment;
    private javax.swing.JButton customer;
    private javax.swing.JButton dichvu;
    private javax.swing.JLabel displayUsername;
    private javax.swing.JButton doanhthu;
    private javax.swing.JLabel homepage;
    private javax.swing.JLabel info;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTextField jTextField4;
    private javax.swing.JTextField jTextField7;
    private javax.swing.JTextPane jTextPane1;
    private com.toedter.calendar.JDateChooser jdcFrom;
    private com.toedter.calendar.JDateChooser jdcTo;
    private javax.swing.JLabel lTotal1;
    private javax.swing.JLabel lTotal2;
    private javax.swing.JLabel lTotal3;
    private javax.swing.JLabel lblGrandTotal;
    private javax.swing.JLabel lblTotalInvoices;
    private javax.swing.JLabel manage;
    private javax.swing.JButton p101;
    private javax.swing.JButton p102;
    private javax.swing.JButton p103;
    private javax.swing.JButton p201;
    private javax.swing.JButton p202;
    private javax.swing.JButton p203;
    private javax.swing.JButton p301;
    private javax.swing.JButton p302;
    private javax.swing.JButton p303;
    private javax.swing.JButton p401;
    private javax.swing.JButton p402;
    private javax.swing.JButton p403;
    private javax.swing.JButton p501;
    private javax.swing.JButton p502;
    private javax.swing.JButton p503;
    private javax.swing.JPanel pnlRoomLayout;
    private javax.swing.JLabel refresh;
    private javax.swing.JButton room;
    private javax.swing.JButton service;
    private javax.swing.JLabel signout;
    private javax.swing.JButton staff;
    private javax.swing.JLabel statistic;
    private javax.swing.JTable tHoaDon;
    private javax.swing.JTextPane textActivities;
    // End of variables declaration//GEN-END:variables
}

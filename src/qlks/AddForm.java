/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package qlks;

//import static btl_thlt_java.MuonTra.setupTableAppearance;
import com.mysql.cj.result.Row;
import dao.BookingDAO;
import dto.Booking;
import dto.Customer;
import dto.Room;
import java.awt.Color;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;
import qlks.ThemedTable;

/**
 *
 * @author Admin
 */
public class AddForm extends RoundedFrame {
    private int mouseX, mouseY;
    private Map<String, Room> roomMap = new HashMap<>();
    private Map<String, Customer> customerMap = new HashMap<>();



    /**
     * Creates new form HomePage
     */
    
    public AddForm() {
        super("Phần mềm quản lý khách sạn", 30);
        initComponents();
        loadCustomers();
        loadRooms();
    }
    public boolean validateBookingDates() {
    Date checkIn = jdcCheckIn.getDate();
    Date checkOut = jdcCheckOut.getDate();

    if (checkIn == null || checkOut == null) {
        JOptionPane.showMessageDialog(null, "Vui lòng chọn đầy đủ ngày nhận và trả phòng.");
        return false;
    }

    Date today = new Date();

    if (checkIn.before(today)) {
        JOptionPane.showMessageDialog(null, "Ngày nhận phòng không được ở quá khứ.");
        return false;
    }

    if (checkOut.before(checkIn)) {
        JOptionPane.showMessageDialog(null, "Ngày trả phòng phải sau ngày nhận phòng.");
        return false;
    }

    return true; // Hợp lệ
}

   private void loadRooms() {
    cmbRoom.removeAllItems();
    roomMap.clear();

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement("SELECT * FROM rooms");
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            Room room = new Room();
            room.setRoomId(rs.getInt("RoomId"));
            room.setRoomNumber(rs.getString("RoomNumber"));
            room.setRoomType(rs.getString("RoomType"));

            String display = room.getRoomNumber() + " - " + room.getRoomType();
            roomMap.put(display, room);
            cmbRoom.addItem(display);  // vẫn add chuỗi
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}

private void loadCustomers() {
    cmbCustomer.removeAllItems();
    customerMap.clear();

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement stmt = conn.prepareStatement("SELECT * FROM customers");
         ResultSet rs = stmt.executeQuery()) {

        while (rs.next()) {
            Customer c = new Customer();
            c.setCustomerId(rs.getInt("CustomerId"));
            c.setFullName(rs.getString("FullName"));

            String display = c.getCustomerId() + " - " + c.getFullName();
            customerMap.put(display, c);
            cmbCustomer.addItem(display);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
private void add(){
    try {
        // Lấy phòng và khách được chọn từ combo box
        String roomStr = (String) cmbRoom.getSelectedItem();
        Room selectedRoom = roomMap.get(roomStr);

        String customerStr = (String) cmbCustomer.getSelectedItem();
        Customer selectedCustomer = customerMap.get(customerStr);

        if (selectedRoom == null || selectedCustomer == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn đầy đủ thông tin.");
            return;
        }

        int roomId = selectedRoom.getRoomId();
        int customerId = selectedCustomer.getCustomerId();

        // Lấy ngày
        java.sql.Date checkIn = new java.sql.Date(jdcCheckIn.getDate().getTime());
        java.sql.Date checkOut = new java.sql.Date(jdcCheckOut.getDate().getTime());

        try (Connection conn = DatabaseConnection.getConnection()) {
            // 1. Kiểm tra trạng thái phòng
            String checkSql = "SELECT Status FROM rooms WHERE RoomId = ?";
            PreparedStatement checkStmt = conn.prepareStatement(checkSql);
            checkStmt.setInt(1, roomId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                String roomStatus = rs.getString("Status");
                if (!roomStatus.equalsIgnoreCase("Available")) {
                    JOptionPane.showMessageDialog(this, "Phòng đã được đặt hoặc không khả dụng.");
                    return;
                }
            }

            // 2. Thêm đặt phòng
            String insertSql = "INSERT INTO bookings (RoomId, CustomerId, CheckInDate, CheckOutDate, Status, PaymentStatus) " +
                               "VALUES (?, ?, ?, ?, 'Confirmed', 'Pending')";
            PreparedStatement insertStmt = conn.prepareStatement(insertSql);
            insertStmt.setInt(1, roomId);
            insertStmt.setInt(2, customerId);
            insertStmt.setDate(3, checkIn);
            insertStmt.setDate(4, checkOut);
            insertStmt.executeUpdate();

            // 3. Cập nhật trạng thái phòng
            String updateRoomSql = "UPDATE rooms SET Status = 'Booked' WHERE RoomId = ?";
            PreparedStatement updateRoomStmt = conn.prepareStatement(updateRoomSql);
            updateRoomStmt.setInt(1, roomId);
            updateRoomStmt.executeUpdate();

            JOptionPane.showMessageDialog(this, "Đặt phòng thành công!");
            this.dispose();
        }

    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(this, "Lỗi khi thêm đặt phòng: " + ex.getMessage());
    }
}

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem1 = new javax.swing.JMenuItem();
        jOptionPane1 = new javax.swing.JOptionPane();
        jPanel2 = new javax.swing.JPanel();
        close = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jButton6 = new RoundedButton();
        jLabel2 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jdcCheckOut = new com.toedter.calendar.JDateChooser();
        jdcCheckIn = new com.toedter.calendar.JDateChooser();
        btnThem = new RoundedButton();
        cmbRoom = new javax.swing.JComboBox<>();
        cmbCustomer = new javax.swing.JComboBox<>();

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Hotel management system ");
        setBackground(new java.awt.Color(252, 244, 234));
        setIconImage(new ImageIcon(getClass().getResource("/Icon/Title.png")).getImage());
        setUndecorated(true);
        setResizable(false);
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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
        jPanel2.add(close, new org.netbeans.lib.awtextra.AbsoluteConstraints(440, 0, 60, 60));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/floral.png"))); // NOI18N
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 250, 60));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/floral.png"))); // NOI18N
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 0, 250, 60));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 500, 60));

        jPanel1.setBackground(new java.awt.Color(252, 244, 234));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(253, 251, 246));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 77, 79), 2, true), "", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(0, 77, 79))); // NOI18N
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

        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel2.setText("Ngày trả phòng");
        jPanel3.add(jLabel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 260, -1, 30));

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setText("Tên khách hàng");
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 20, 150, 30));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setText("Số phòng");
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 100, -1, 30));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel7.setText("Ngày nhận phòng");
        jPanel3.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(160, 180, -1, 30));
        jPanel3.add(jdcCheckOut, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 290, 240, 40));
        jPanel3.add(jdcCheckIn, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 210, 240, 40));

        btnThem.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnThem.setForeground(new java.awt.Color(255, 255, 255));
        btnThem.setText("Tạo ");
        btnThem.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });
        jPanel3.add(btnThem, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 370, -1, -1));

        cmbRoom.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel3.add(cmbRoom, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 130, 240, 40));

        cmbCustomer.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        jPanel3.add(cmbCustomer, new org.netbeans.lib.awtextra.AbsoluteConstraints(110, 50, 240, 40));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 0, 450, 420));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 500, 440));

        setSize(new java.awt.Dimension(500, 500));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void closeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_closeMouseClicked
this.dispose();
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

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_jButton6ActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        // TODO add your handling code here:
        if (!validateBookingDates()) {
    return; // Dừng thao tác đặt phòng
}
        add();
        
    }//GEN-LAST:event_btnThemActionPerformed

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
            java.util.logging.Logger.getLogger(AddForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AddForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AddForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AddForm.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new AddForm().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnThem;
    private javax.swing.JLabel close;
    private javax.swing.JComboBox<String> cmbCustomer;
    private javax.swing.JComboBox<String> cmbRoom;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JOptionPane jOptionPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private com.toedter.calendar.JDateChooser jdcCheckIn;
    private com.toedter.calendar.JDateChooser jdcCheckOut;
    // End of variables declaration//GEN-END:variables
}

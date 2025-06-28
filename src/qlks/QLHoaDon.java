/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package qlks;

//import static btl_thlt_java.MuonTra.setupTableAppearance;
import com.mysql.cj.result.Row;
import dao.BookingDAO;
import dao.InvoiceDAO;
import static dao.ServiceDAO.deleteSelectedBookedService;
import dto.Booking;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import qlks.ThemedTable;

/**
 *
 * @author Admin
 */
public class QLHoaDon extends RoundedFrame {
    private int mouseX, mouseY;
    private final Map<String, Integer> bookingMap = new HashMap<>();
    private final Map<String, Integer> serviceMap = new HashMap<>();

    /**
     * Creates new form HomePage
     */
    
    public QLHoaDon() {
        super("Phần mềm quản lý khách sạn", 30);
        initComponents();
        loadBookingToComboBox(cmbBooking);
        loadServicesToComboBox(cmbService);

    cmbBooking.addActionListener(e -> {
        Integer bookingId = getSelectedBookingIdFromComboBox(cmbBooking);
        if (bookingId != null) {
            UserInfo.selected = bookingId;
            InvoiceDAO.showEstimateInvoice(bookingId, tInvoice, lTotal);
        }
    });

    // Auto hiển thị hóa đơn đầu tiên nếu có
    if (cmbBooking.getItemCount() > 0) {
        cmbBooking.setSelectedIndex(0);
    }
//        InvoiceDAO.showEstimateInvoice(,tInvoice,lTotal);
//        if ("User".equals(UserInfo.loggedInRole)) {
//    manageUsers.setVisible(false);
    }
    public void themDichVuChoBooking() {
    // 1. Lấy booking ID
    String selectedBooking = (String) cmbBooking.getSelectedItem();
    Integer bookingId = bookingMap.get(selectedBooking);

    if (bookingId == null) {
        JOptionPane.showMessageDialog(null, "Vui lòng chọn một đặt phòng hợp lệ.");
        return;
    }

    // 2. Lấy service ID
    String selectedService = (String) cmbService.getSelectedItem();
    Integer serviceId = serviceMap.get(selectedService);

    if (serviceId == null) {
        JOptionPane.showMessageDialog(null, "Vui lòng chọn một dịch vụ hợp lệ.");
        return;
    }

    // 3. Số lượng
    int quantity;
    try {
        quantity = Integer.parseInt(spnSoLuong.getValue().toString());
        if (quantity <= 0) throw new NumberFormatException();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Số lượng phải là số nguyên dương.");
        return;
    }

    // 4. Lấy giá tại thời điểm thêm
    try (Connection conn = DatabaseConnection.getConnection()) {
        String priceSql = "SELECT Price FROM services WHERE ServiceId = ?";
        PreparedStatement psPrice = conn.prepareStatement(priceSql);
        psPrice.setInt(1, serviceId);
        ResultSet rs = psPrice.executeQuery();

        if (rs.next()) {
            double priceAtBooking = rs.getDouble("Price");

            // 5. Thêm vào booked_services
            String insertSql = """
                INSERT INTO bookedservices (BookingId, ServiceId, Quantity, PriceAtBooking)
                VALUES (?, ?, ?, ?)
            """;
            PreparedStatement psInsert = conn.prepareStatement(insertSql);
            psInsert.setInt(1, bookingId);
            psInsert.setInt(2, serviceId);
            psInsert.setInt(3, quantity);
            psInsert.setDouble(4, priceAtBooking);
            psInsert.executeUpdate();

            JOptionPane.showMessageDialog(null, "Đã thêm dịch vụ thành công!");

            // Cập nhật hóa đơn tạm tính sau khi thêm
            InvoiceDAO.showEstimateInvoice(bookingId, tInvoice, lTotal);

        } else {
            JOptionPane.showMessageDialog(null, "Không tìm thấy giá dịch vụ.");
        }

    } catch (Exception ex) {
        ex.printStackTrace();
        JOptionPane.showMessageDialog(null, "Lỗi khi thêm dịch vụ: " + ex.getMessage());
    }
}
    public void loadServicesToComboBox(JComboBox<String> comboBox) {
    comboBox.removeAllItems();
    serviceMap.clear();

    try (Connection conn = DatabaseConnection.getConnection()) {
        String sql = "SELECT ServiceId, ServiceName FROM services";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            int id = rs.getInt("ServiceId");
            String name = rs.getString("ServiceName");
            comboBox.addItem(name);
            serviceMap.put(name, id);
        }

    } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Lỗi khi load dịch vụ: " + e.getMessage());
    }
}

    public void loadBookingToComboBox(JComboBox<String> comboBox) {
        comboBox.removeAllItems();
    bookingMap.clear();

    try (Connection conn = DatabaseConnection.getConnection()) {
        String sql = """
            SELECT b.BookingId, c.FullName, r.RoomNumber, b.Status
            FROM bookings b
            JOIN customers c ON b.CustomerId = c.CustomerId
            JOIN rooms r ON b.RoomId = r.RoomId
            WHERE b.BookingId NOT IN (SELECT BookingId FROM invoices) AND b.Status <> 'Cancelled' AND b.Status <> 'Confirmed'
        """;

        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        while (rs.next()) {
            int bookingId = rs.getInt("BookingId");
            String customer = rs.getString("FullName");
            String room = rs.getString("RoomNumber");
            String status = rs.getString("Status");

            String display = "BKG-" + bookingId + " | Phòng " + room + " | " + customer + " | " + status;
            comboBox.addItem(display);
            bookingMap.put(display, bookingId);
        }

    } catch (Exception e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Lỗi khi tải danh sách Booking: " + e.getMessage());
    }
}
public Integer getSelectedBookingIdFromComboBox(JComboBox<String> comboBox) {
    String selected = (String) comboBox.getSelectedItem();
    if (selected == null || !bookingMap.containsKey(selected)) {
        JOptionPane.showMessageDialog(null, "Vui lòng chọn một đặt phòng hợp lệ.");
        return null;
    }

    return bookingMap.get(selected);
}

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem1 = new javax.swing.JMenuItem();
        jOptionPane1 = new javax.swing.JOptionPane();
        jPanel2 = new javax.swing.JPanel();
        close = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        Return = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        tInvoice = new ThemedTable();
        lTotal = new javax.swing.JLabel();
        btnDelete = new javax.swing.JLabel();
        btnAdd = new RoundedButton();
        btnAdd1 = new RoundedButton();
        cmbService = new javax.swing.JComboBox<>();
        spnSoLuong = new javax.swing.JSpinner();
        cmbBooking = new javax.swing.JComboBox<>();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();

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
        jPanel2.add(close, new org.netbeans.lib.awtextra.AbsoluteConstraints(1140, 0, 60, 60));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/floral.png"))); // NOI18N
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 0, 250, 60));

        Return.setBackground(new java.awt.Color(252, 244, 234));
        Return.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        Return.setForeground(new java.awt.Color(0, 77, 79));
        Return.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        Return.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/back.png"))); // NOI18N
        Return.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Return.setOpaque(true);
        Return.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ReturnMouseClicked(evt);
            }
        });
        jPanel2.add(Return, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 60, 60));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/floral.png"))); // NOI18N
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 0, 250, 60));

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/floral.png"))); // NOI18N
        jPanel2.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(1000, 0, 250, 60));

        jLabel6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/floral.png"))); // NOI18N
        jPanel2.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 0, 250, 60));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/floral.png"))); // NOI18N
        jPanel2.add(jLabel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 250, 60));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1200, 60));

        jPanel1.setBackground(new java.awt.Color(252, 244, 234));
        jPanel1.setDoubleBuffered(false);
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 77, 79), 2, true), "Quản lý Hóa đơn", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(0, 77, 79))); // NOI18N
        jPanel3.setOpaque(false);
        jPanel3.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel4.setBackground(new java.awt.Color(255, 255, 255));
        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 77, 79), 2, true), "Hóa đơn tạm tính", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 18), new java.awt.Color(0, 77, 79))); // NOI18N
        jPanel4.setOpaque(false);
        jPanel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jPanel4MouseClicked(evt);
            }
        });

        tInvoice.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Dịch vụ", "	Số lượng", "Đơn giá	", "Thành tiền "
            }
        ));
        jScrollPane2.setViewportView(tInvoice);

        lTotal.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        lTotal.setForeground(new java.awt.Color(0, 77, 79));
        lTotal.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        lTotal.setText("Total");

        btnDelete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/trash-can.png"))); // NOI18N
        btnDelete.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnDelete.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnDeleteMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 570, Short.MAX_VALUE)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(btnDelete)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lTotal, javax.swing.GroupLayout.PREFERRED_SIZE, 175, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 500, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lTotal)
                    .addComponent(btnDelete))
                .addContainerGap())
        );

        jPanel3.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(540, 22, -1, 570));

        btnAdd.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnAdd.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd.setText("Thêm dịch vụ");
        btnAdd.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAdd.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddActionPerformed(evt);
            }
        });
        jPanel3.add(btnAdd, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 340, 400, 80));

        btnAdd1.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnAdd1.setForeground(new java.awt.Color(255, 255, 255));
        btnAdd1.setText("Thanh toán");
        btnAdd1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnAdd1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdd1ActionPerformed(evt);
            }
        });
        jPanel3.add(btnAdd1, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 470, 400, 80));

        cmbService.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cmbService.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbService.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbServiceActionPerformed(evt);
            }
        });
        jPanel3.add(cmbService, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 180, 400, 40));

        spnSoLuong.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        jPanel3.add(spnSoLuong, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 260, 90, 40));

        cmbBooking.setFont(new java.awt.Font("Segoe UI", 0, 14)); // NOI18N
        cmbBooking.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));
        cmbBooking.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmbBookingActionPerformed(evt);
            }
        });
        jPanel3.add(cmbBooking, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 100, 400, 40));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel7.setText("Số lượng");
        jPanel3.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 230, 150, 30));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel8.setText("Chọn phòng");
        jPanel3.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 70, 150, 30));

        jLabel9.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel9.setText("Chọn dịch vụ");
        jPanel3.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(70, 150, 150, 30));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 0, 1150, 610));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 1200, 640));

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

    private void ReturnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ReturnMouseClicked
        // TODO add your handling code here:
        new HomePage().setVisible(true);
        this.dispose();
    }//GEN-LAST:event_ReturnMouseClicked

    private void btnAddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddActionPerformed
        // TODO add your handling code here:
themDichVuChoBooking();
    }//GEN-LAST:event_btnAddActionPerformed

    private void btnAdd1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdd1ActionPerformed
        // TODO add your handling code here:
        PaymentForm pm = new PaymentForm();
        pm.setVisible(true);
    }//GEN-LAST:event_btnAdd1ActionPerformed

    private void cmbServiceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbServiceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbServiceActionPerformed

    private void cmbBookingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmbBookingActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_cmbBookingActionPerformed

    private void btnDeleteMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnDeleteMouseClicked
        // TODO add your handling code here:
        deleteSelectedBookedService(tInvoice, UserInfo.selected,lTotal);
    }//GEN-LAST:event_btnDeleteMouseClicked

    private void jPanel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jPanel4MouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel4MouseClicked

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
            java.util.logging.Logger.getLogger(QLHoaDon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QLHoaDon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QLHoaDon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QLHoaDon.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new QLHoaDon().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Return;
    private javax.swing.JButton btnAdd;
    private javax.swing.JButton btnAdd1;
    private javax.swing.JLabel btnDelete;
    private javax.swing.JLabel close;
    private javax.swing.JComboBox<String> cmbBooking;
    private javax.swing.JComboBox<String> cmbService;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JOptionPane jOptionPane1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lTotal;
    private javax.swing.JSpinner spnSoLuong;
    private javax.swing.JTable tInvoice;
    // End of variables declaration//GEN-END:variables
}

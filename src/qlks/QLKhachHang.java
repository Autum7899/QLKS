/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package qlks;

//import static btl_thlt_java.MuonTra.setupTableAppearance;
import com.mysql.cj.result.Row;
import dao.BookingDAO;
import dao.loadData;
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
public class QLKhachHang extends RoundedFrame {
    private int mouseX, mouseY;
    int selectedCustomerId;



    /**
     * Creates new form HomePage
     */
    
    public QLKhachHang() {
        super("Phần mềm quản lý khách sạn", 30);
        initComponents();
        loadData.loadDataToTable(tKhachHang, "customers");
    }
    public void addCustomer() {
    String name = txtName.getText();
    String phone = txtPhone.getText();
    String email = txtMail.getText();
    String address = txtAdress.getText();
    String idCard = txtID.getText();

    // Kiểm tra trùng số CMND/CCCD
    String checkSql = "SELECT * FROM customers WHERE IDCardNumber = ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

        checkStmt.setString(1, idCard);
        ResultSet rs = checkStmt.executeQuery();
        if (rs.next()) {
            JOptionPane.showMessageDialog(this, "CMND/CCCD đã tồn tại.");
            return;
        }

        String insertSql = "INSERT INTO customers (FullName, IDCardNumber, PhoneNumber, Address, Email) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
            insertStmt.setString(1, name);
            insertStmt.setString(2, idCard);
            insertStmt.setString(3, phone);
            insertStmt.setString(4, address);
            insertStmt.setString(5, email);

            insertStmt.executeUpdate();
            JOptionPane.showMessageDialog(this, "Thêm khách hàng thành công.");
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Lỗi khi thêm khách hàng: " + e.getMessage());
    }
}
public void updateCustomer(int selectedCustomerId) {
    String name = txtName.getText();
    String phone = txtPhone.getText();
    String email = txtMail.getText();
    String address = txtAdress.getText();
    String idCard = txtID.getText();

    // Kiểm tra nếu CMND/CCCD mới bị trùng với khách khác
    String checkSql = "SELECT * FROM customers WHERE IDCardNumber = ? AND CustomerId != ?";
    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

        checkStmt.setString(1, idCard);
        checkStmt.setInt(2, selectedCustomerId);
        ResultSet rs = checkStmt.executeQuery();
        if (rs.next()) {
            JOptionPane.showMessageDialog(this, "CMND/CCCD đã tồn tại cho khách hàng khác.");
            return;
        }

        String updateSql = "UPDATE customers SET FullName = ?, IDCardNumber = ?, PhoneNumber = ?, Address = ?, Email = ? WHERE CustomerId = ?";
        try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
            ps.setString(1, name);
            ps.setString(2, idCard);
            ps.setString(3, phone);
            ps.setString(4, address);
            ps.setString(5, email);
            ps.setInt(6, selectedCustomerId);

            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Cập nhật khách hàng thành công.");
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(this, "Lỗi khi cập nhật: " + e.getMessage());
    }
}
public void deleteCustomer(int selectedCustomerId) {
    String checkSql = "SELECT * FROM bookings WHERE CustomerId = ?";

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement psCheck = conn.prepareStatement(checkSql)) {

        psCheck.setInt(1, selectedCustomerId);
        ResultSet rs = psCheck.executeQuery();

        if (rs.next()) {
            JOptionPane.showMessageDialog(null, "Không thể xoá khách hàng đang có đặt phòng.");
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(null, "Bạn có chắc muốn xoá khách hàng này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        String deleteSql = "DELETE FROM customers WHERE CustomerId = ?";
        try (PreparedStatement psDelete = conn.prepareStatement(deleteSql)) {
            psDelete.setInt(1, selectedCustomerId);
            psDelete.executeUpdate();
            JOptionPane.showMessageDialog(null, "Đã xoá khách hàng.");
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Lỗi khi xoá khách hàng: " + e.getMessage());
    }
}
public void searchCustomerByIDCard(String idCardNumber) {
    DefaultTableModel model = (DefaultTableModel) tKhachHang.getModel();
    model.setRowCount(0); // Xoá dữ liệu cũ

    String sql;
    boolean hasKeyword = !idCardNumber.trim().isEmpty();

    if (hasKeyword) {
        sql = "SELECT * FROM customers WHERE IDCardNumber LIKE ?";
    } else {
        sql = "SELECT * FROM customers"; // Không lọc nếu CCCD trống
    }

    try (Connection conn = DatabaseConnection.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {

        if (hasKeyword) {
            ps.setString(1, "%" + idCardNumber + "%");
        }

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            int id = rs.getInt("CustomerId");
            String name = rs.getString("FullName");
            String idCard = rs.getString("IDCardNumber");
            String phone = rs.getString("PhoneNumber");
            String email = rs.getString("Email");
            String address = rs.getString("Address");

            model.addRow(new Object[]{id, name, idCard, phone, email, address});
        }

        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "Không tìm thấy khách hàng nào.");
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null, "Lỗi khi tìm kiếm: " + e.getMessage());
    }
}



    public void resetCustomerFields(){
                   if (tKhachHang != null) {
        tKhachHang.clearSelection(); // Clears any selection in the table
    }
    selectedCustomerId=0;
    txtName.setText("");
    txtPhone.setText("");
    txtMail.setText("");
    txtAdress.setText("");
    txtID.setText("");
    }
public void loadKhachHangToFields() {
    int row = tKhachHang.getSelectedRow();

    if (row < 0) {
        JOptionPane.showMessageDialog(null, "Vui lòng chọn một khách hàng.");
        return;
    }

    selectedCustomerId = Integer.parseInt(tKhachHang.getValueAt(row, 0).toString());

    txtName.setText(tKhachHang.getValueAt(row, 1).toString());       // FullName
    txtID.setText(tKhachHang.getValueAt(row, 2).toString());         // IDCardNumber
    txtPhone.setText(tKhachHang.getValueAt(row, 3).toString());      // PhoneNumber
    txtAdress.setText(tKhachHang.getValueAt(row, 4).toString());     // Address
    txtMail.setText(tKhachHang.getValueAt(row, 5).toString());       // Email
}


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem1 = new javax.swing.JMenuItem();
        jOptionPane1 = new javax.swing.JOptionPane();
        btnThem3 = new RoundedButton();
        jPanel2 = new javax.swing.JPanel();
        Return = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        close = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jPanel1 = new javax.swing.JPanel();
        jPanel3 = new javax.swing.JPanel();
        jButton6 = new RoundedButton();
        jLabel5 = new javax.swing.JLabel();
        btnLamMoi = new RoundedButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tKhachHang = new ThemedTable();
        txtName = new javax.swing.JTextField();
        txtID = new javax.swing.JTextField();
        txtPhone = new javax.swing.JTextField();
        txtAdress = new javax.swing.JTextField();
        txtMail = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        btnThem = new RoundedButton();
        btnSua = new RoundedButton();
        btnXoa = new RoundedButton();
        search = new javax.swing.JLabel();

        jMenuItem1.setText("jMenuItem1");

        btnThem3.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnThem3.setForeground(new java.awt.Color(255, 255, 255));
        btnThem3.setText("Tạo ");
        btnThem3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnThem3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThem3ActionPerformed(evt);
            }
        });

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

        jLabel8.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/floral.png"))); // NOI18N
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 0, 250, 60));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/floral.png"))); // NOI18N
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 250, 60));

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/floral.png"))); // NOI18N
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 0, 250, 60));

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
        jPanel2.add(close, new org.netbeans.lib.awtextra.AbsoluteConstraints(940, 0, 60, 60));

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/floral.png"))); // NOI18N
        jPanel2.add(jLabel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 0, 250, 60));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 1000, 60));

        jPanel1.setBackground(new java.awt.Color(252, 244, 234));
        jPanel1.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jPanel3.setBackground(new java.awt.Color(255, 255, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createTitledBorder(new javax.swing.border.LineBorder(new java.awt.Color(0, 77, 79), 2, true), "Quản lý khách hàng", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 14), new java.awt.Color(0, 77, 79))); // NOI18N
        jPanel3.setOpaque(false);
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

        jLabel5.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel5.setText("Số CCCD");
        jPanel3.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 120, 150, 40));

        btnLamMoi.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnLamMoi.setForeground(new java.awt.Color(255, 255, 255));
        btnLamMoi.setText("Làm mới");
        btnLamMoi.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnLamMoi.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLamMoiActionPerformed(evt);
            }
        });
        jPanel3.add(btnLamMoi, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 360, 110, 40));

        tKhachHang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Mã KH", "Tên KH", "CCCD", "SĐT", "Địa chỉ", "Email"
            }
        ));
        tKhachHang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tKhachHangMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tKhachHang);

        jPanel3.add(jScrollPane1, new org.netbeans.lib.awtextra.AbsoluteConstraints(500, 20, 440, 390));
        jPanel3.add(txtName, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 60, 250, 40));
        jPanel3.add(txtID, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 120, 250, 40));
        jPanel3.add(txtPhone, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 180, 250, 40));
        jPanel3.add(txtAdress, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 240, 250, 40));
        jPanel3.add(txtMail, new org.netbeans.lib.awtextra.AbsoluteConstraints(200, 300, 250, 40));

        jLabel6.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel6.setText("Email");
        jPanel3.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 300, 150, 40));

        jLabel7.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel7.setText("Tên khách hàng");
        jPanel3.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 60, 150, 40));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel10.setText("Số điện thoại");
        jPanel3.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 180, 150, 40));

        jLabel11.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel11.setText("Địa chỉ");
        jPanel3.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 240, 150, 40));

        btnThem.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnThem.setForeground(new java.awt.Color(255, 255, 255));
        btnThem.setText("Thêm");
        btnThem.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnThem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThemActionPerformed(evt);
            }
        });
        jPanel3.add(btnThem, new org.netbeans.lib.awtextra.AbsoluteConstraints(30, 360, 100, 40));

        btnSua.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnSua.setForeground(new java.awt.Color(255, 255, 255));
        btnSua.setText("Sửa");
        btnSua.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnSua.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSuaActionPerformed(evt);
            }
        });
        jPanel3.add(btnSua, new org.netbeans.lib.awtextra.AbsoluteConstraints(140, 360, 100, 40));

        btnXoa.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        btnXoa.setForeground(new java.awt.Color(255, 255, 255));
        btnXoa.setText(" Xóa");
        btnXoa.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        btnXoa.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnXoaActionPerformed(evt);
            }
        });
        jPanel3.add(btnXoa, new org.netbeans.lib.awtextra.AbsoluteConstraints(250, 360, 100, 40));

        search.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        search.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/magnifying-glass.png"))); // NOI18N
        search.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        search.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                searchMouseClicked(evt);
            }
        });
        jPanel3.add(search, new org.netbeans.lib.awtextra.AbsoluteConstraints(450, 120, 40, 40));

        jPanel1.add(jPanel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(25, 0, 950, 420));

        getContentPane().add(jPanel1, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 60, 1000, 440));

        setSize(new java.awt.Dimension(1000, 500));
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

    private void btnLamMoiActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLamMoiActionPerformed
        // TODO add your handling code here:
resetCustomerFields();
    }//GEN-LAST:event_btnLamMoiActionPerformed

    private void btnThemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThemActionPerformed
        // TODO add your handling code here:
        addCustomer();
        loadData.loadDataToTable(tKhachHang, "customers");
    }//GEN-LAST:event_btnThemActionPerformed

    private void btnSuaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSuaActionPerformed
        // TODO add your handling code here:
        updateCustomer(selectedCustomerId);
        loadData.loadDataToTable(tKhachHang, "customers");
    }//GEN-LAST:event_btnSuaActionPerformed

    private void btnThem3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThem3ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnThem3ActionPerformed

    private void btnXoaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnXoaActionPerformed
        // TODO add your handling code here:
        deleteCustomer(selectedCustomerId);
        loadData.loadDataToTable(tKhachHang, "customers");
    }//GEN-LAST:event_btnXoaActionPerformed

    private void ReturnMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ReturnMouseClicked
        // TODO add your handling code here:
        HomePage home = new HomePage();
         home.setSelectedTab(1);
         home.setVisible(true);
         this.dispose();
    }//GEN-LAST:event_ReturnMouseClicked

    private void tKhachHangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tKhachHangMouseClicked
        // TODO add your handling code here:
        loadKhachHangToFields();
    }//GEN-LAST:event_tKhachHangMouseClicked

    private void searchMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_searchMouseClicked
        // TODO add your handling code here:
         String idCard = txtID.getText().trim();
        searchCustomerByIDCard(idCard);
    }//GEN-LAST:event_searchMouseClicked

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
            java.util.logging.Logger.getLogger(QLKhachHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(QLKhachHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(QLKhachHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(QLKhachHang.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
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
                new QLKhachHang().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel Return;
    private javax.swing.JButton btnLamMoi;
    private javax.swing.JButton btnSua;
    private javax.swing.JButton btnThem;
    private javax.swing.JButton btnThem3;
    private javax.swing.JButton btnXoa;
    private javax.swing.JLabel close;
    private javax.swing.JButton jButton6;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
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
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel search;
    private javax.swing.JTable tKhachHang;
    private javax.swing.JTextField txtAdress;
    private javax.swing.JTextField txtID;
    private javax.swing.JTextField txtMail;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtPhone;
    // End of variables declaration//GEN-END:variables
}

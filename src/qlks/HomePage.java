/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package qlks;

//import static btl_thlt_java.MuonTra.setupTableAppearance;
import com.mysql.cj.result.Row;
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
import java.util.List;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableModel;

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
//        setupTableAppearance(tblThongKe);
//        if ("User".equals(UserInfo.loggedInRole)) {
//    manageUsers.setVisible(false);
//}
        displayUsername.setText(UserInfo.loggedInUsername) ;
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem1 = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        signout = new javax.swing.JLabel();
        displayUsername = new javax.swing.JLabel();
        Logo = new javax.swing.JLabel();
        statistic = new qlks.HoverLabel("Your Text Here")
        ;
        homepage = new qlks.HoverLabel("Your Text Here")
        ;
        manage = new qlks.HoverLabel("Your Text Here")
        ;
        jPanel2 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        close = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel8 = new javax.swing.JPanel();
        jPanel9 = new javax.swing.JPanel();
        billing = new qlks.RoundedButton();
        booking = new qlks.RoundedButton();
        service = new qlks.RoundedButton();
        staff = new qlks.RoundedButton();
        room = new qlks.RoundedButton();
        customer = new qlks.RoundedButton();
        jPanel10 = new javax.swing.JPanel();
        jPanel11 = new javax.swing.JPanel();

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Hotel management system ");
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
        jTabbedPane1.addTab("tab1", jPanel8);

        jPanel9.setBackground(new java.awt.Color(252, 244, 234));
        jPanel9.setForeground(new java.awt.Color(255, 255, 255));
        jPanel9.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        billing.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        billing.setForeground(new java.awt.Color(255, 255, 255));
        billing.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/billing.png"))); // NOI18N
        billing.setText("Hóa đơn & Thanh toán");
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

        service.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        service.setForeground(new java.awt.Color(255, 255, 255));
        service.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/service.png"))); // NOI18N
        service.setText(" Quản lý Dịch vụ");
        service.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        service.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        service.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        service.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                serviceActionPerformed(evt);
            }
        });
        jPanel9.add(service, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 330, 250, 290));

        staff.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        staff.setForeground(new java.awt.Color(255, 255, 255));
        staff.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/profile.png"))); // NOI18N
        staff.setText("Quản lý Người dùng");
        staff.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        staff.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        staff.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        staff.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                staffActionPerformed(evt);
            }
        });
        jPanel9.add(staff, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 330, 250, 290));

        room.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        room.setForeground(new java.awt.Color(255, 255, 255));
        room.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/room.png"))); // NOI18N
        room.setText("Quản lý Phòng");
        room.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        room.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        room.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        room.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                roomActionPerformed(evt);
            }
        });
        jPanel9.add(room, new org.netbeans.lib.awtextra.AbsoluteConstraints(480, 20, 250, 290));

        customer.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        customer.setForeground(new java.awt.Color(255, 255, 255));
        customer.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/customer.png"))); // NOI18N
        customer.setText("Quản lý Khách hàng");
        customer.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        customer.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        customer.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        customer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                customerActionPerformed(evt);
            }
        });
        jPanel9.add(customer, new org.netbeans.lib.awtextra.AbsoluteConstraints(750, 20, 250, 290));

        jTabbedPane1.addTab("tab2", jPanel9);

        jPanel10.setBackground(new java.awt.Color(252, 244, 234));

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1020, Short.MAX_VALUE)
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 645, Short.MAX_VALUE)
        );

        jTabbedPane1.addTab("tab3", jPanel10);

        jPanel11.setBackground(new java.awt.Color(252, 244, 234));

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1020, Short.MAX_VALUE)
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 645, Short.MAX_VALUE)
        );

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
    }//GEN-LAST:event_billingActionPerformed

    private void bookingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bookingActionPerformed
        // TODO add your handling code here:
        QLDatPhong dp = new QLDatPhong();
            dp.setVisible(true);
            this.dispose();
    }//GEN-LAST:event_bookingActionPerformed

    private void serviceActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_serviceActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_serviceActionPerformed

    private void staffActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_staffActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_staffActionPerformed

    private void roomActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_roomActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_roomActionPerformed

    private void customerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_customerActionPerformed
        // TODO add your handling code here:
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

    private void statisticMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_statisticMouseClicked
        // TODO add your handling code here:
         jTabbedPane1.setSelectedIndex(2);
    }//GEN-LAST:event_statisticMouseClicked

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
    private javax.swing.JLabel Logo;
    private javax.swing.JButton billing;
    private javax.swing.JButton booking;
    private javax.swing.JLabel close;
    private javax.swing.JButton customer;
    private javax.swing.JLabel displayUsername;
    private javax.swing.JLabel homepage;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JLabel manage;
    private javax.swing.JButton room;
    private javax.swing.JButton service;
    private javax.swing.JLabel signout;
    private javax.swing.JButton staff;
    private javax.swing.JLabel statistic;
    // End of variables declaration//GEN-END:variables
}


package qlks;

import java.awt.Color;
import java.awt.Cursor;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.ImageIcon;
import java.awt.Image;



public class Login extends RoundedFrame {

//    private static final String DB_URL = "jdbc:mysql://localhost:3307/qlthuvien";
//    private static final String USER = "root";
//    private static final String PASSWORD = "";
    
    
    public Login() {
        super("Phần mềm quản lý khách sạn", 30);
        initComponents();
        this.setLocationRelativeTo(null);
       
    }

 
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        Right = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        Left = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        usernameField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        passwordField = new javax.swing.JPasswordField();
        Login_btn = new qlks.RoundedButton();
        close = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Login");
        setIconImage(new ImageIcon(getClass().getResource("/Icon/Title.png")).getImage());
        setUndecorated(true);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setPreferredSize(new java.awt.Dimension(800, 500));
        jPanel1.setLayout(null);

        Right.setBackground(new java.awt.Color(9, 38, 41));
        Right.setPreferredSize(new java.awt.Dimension(400, 500));
        Right.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());
        Right.add(jLabel5, new org.netbeans.lib.awtextra.AbsoluteConstraints(59, 442, -1, -1));

        jLabel6.setFont(new java.awt.Font("Melloida ExtBd", 1, 30)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setText("HOTEL MANAGEMENT");
        Right.add(jLabel6, new org.netbeans.lib.awtextra.AbsoluteConstraints(20, 80, -1, -1));

        jLabel7.setFont(new java.awt.Font("Segoe UI Light", 0, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(204, 204, 204));
        Right.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(59, 436, -1, -1));

        jLabel9.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Icon/HotelLogin.png"))); // NOI18N
        Right.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(74, 103, -1, 288));

        jLabel10.setFont(new java.awt.Font("Melloida ExtBd", 1, 36)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setText("SYSTEM");
        Right.add(jLabel10, new org.netbeans.lib.awtextra.AbsoluteConstraints(120, 380, -1, -1));

        jPanel1.add(Right);
        Right.setBounds(0, 0, 400, 500);

        Left.setBackground(new java.awt.Color(252, 244, 234));
        Left.setMinimumSize(new java.awt.Dimension(400, 500));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 77, 79));
        jLabel1.setText("ĐĂNG NHẬP");

        jLabel2.setBackground(new java.awt.Color(102, 102, 102));
        jLabel2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel2.setText("Tài khoản");

        usernameField.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        usernameField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                usernameFieldActionPerformed(evt);
            }
        });

        jLabel3.setBackground(new java.awt.Color(102, 102, 102));
        jLabel3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel3.setText("Mật khẩu");

        passwordField.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        passwordField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                passwordFieldActionPerformed(evt);
            }
        });

        Login_btn.setBackground(new java.awt.Color(51, 102, 255));
        Login_btn.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Login_btn.setForeground(new java.awt.Color(255, 255, 255));
        Login_btn.setText("Đăng nhập");
        Login_btn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        Login_btn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                Login_btnMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                Login_btnMouseExited(evt);
            }
        });
        Login_btn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                Login_btnActionPerformed(evt);
            }
        });

        close.setBackground(new java.awt.Color(252, 244, 234));
        close.setFont(new java.awt.Font("Segoe UI", 1, 24)); // NOI18N
        close.setForeground(new java.awt.Color(0, 77, 79));
        close.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        close.setText("X");
        close.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        close.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                closeMouseClicked(evt);
            }
        });

        javax.swing.GroupLayout LeftLayout = new javax.swing.GroupLayout(Left);
        Left.setLayout(LeftLayout);
        LeftLayout.setHorizontalGroup(
            LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LeftLayout.createSequentialGroup()
                .addGap(29, 29, 29)
                .addGroup(LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(passwordField, javax.swing.GroupLayout.DEFAULT_SIZE, 343, Short.MAX_VALUE)
                    .addComponent(usernameField)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(Login_btn))
                .addContainerGap(28, Short.MAX_VALUE))
            .addGroup(LeftLayout.createSequentialGroup()
                .addGap(91, 91, 91)
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(close, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        LeftLayout.setVerticalGroup(
            LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LeftLayout.createSequentialGroup()
                .addGroup(LeftLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(LeftLayout.createSequentialGroup()
                        .addGap(56, 56, 56)
                        .addComponent(jLabel1))
                    .addComponent(close, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(35, 35, 35)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(usernameField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(passwordField, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(30, 30, 30)
                .addComponent(Login_btn, javax.swing.GroupLayout.PREFERRED_SIZE, 36, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(133, Short.MAX_VALUE))
        );

        jPanel1.add(Left);
        Left.setBounds(400, 0, 400, 500);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void usernameFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_usernameFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_usernameFieldActionPerformed

    private void Login_btnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_Login_btnActionPerformed
            // The usernameField and passwordField are Swing components from which we get the data.
    String username = usernameField.getText();
    String password = new String(passwordField.getPassword());

    // Validate for empty fields
    if (username.isEmpty() || password.isEmpty()) {
        JOptionPane.showMessageDialog(this, "Please enter both username and password.", "Login Error", JOptionPane.ERROR_MESSAGE);
        return;
    }

    // Call the login check method
    if (checkLogin(username, password)) {
        // Login successful
        UserInfo.loggedInUsername = username; // Store username for other forms if needed

        // Placeholder for what happens after a successful login
         HomePage homePage = new HomePage();
            homePage.setVisible(true);

        this.dispose(); // Close the login window

    } else {
        // Login failed
        JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Error", JOptionPane.ERROR_MESSAGE);
    }
    }//GEN-LAST:event_Login_btnActionPerformed

    private void passwordFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_passwordFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_passwordFieldActionPerformed

    private void Login_btnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Login_btnMouseEntered
       setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        // TODO add your handling code here:
    }//GEN-LAST:event_Login_btnMouseEntered

    private void Login_btnMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_Login_btnMouseExited
        setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));// TODO add your handling code here:
    }//GEN-LAST:event_Login_btnMouseExited

    private void closeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_closeMouseClicked
        int confirm = JOptionPane.showConfirmDialog(this,
            "Bạn có chắc chắn muốn thoát ứng dụng?",
            "Xác nhận Thoát",
            JOptionPane.YES_NO_OPTION);

        if (confirm == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }//GEN-LAST:event_closeMouseClicked

    /**
     * @param args the command line arguments
     */
private boolean checkLogin(String username, String password) {
    // The SQL query is built to match the schema in qlks.sql.
    String sql = "SELECT PasswordHash, Role FROM users WHERE Username = ?"; //

    try (Connection con = DatabaseConnection.getConnection();
         PreparedStatement pst = con.prepareStatement(sql)) {

        if (con == null) {
            return false; // Connection failed
        }
        
        pst.setString(1, username);

        try (ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                // Username exists, now check the password.
                String storedPassword = rs.getString("PasswordHash"); //

                // Direct password comparison based on the plain-text data in the .sql file.
                if (password.equals(storedPassword)) {
                    // Password matches, save the user's role.
                    UserInfo.loggedInRole = rs.getString("Role"); //
                    return true; // Login successful
                } else {
                    return false; // Password incorrect
                }
            } else {
                return false; // Username does not exist
            }
        }

    } catch (SQLException e) {
        JOptionPane.showMessageDialog(null,
                "Database error during login check: " + e.getMessage(),
                "Database Error",
                JOptionPane.ERROR_MESSAGE);
        e.printStackTrace();
        return false;
    }
}
    public static void main(String[] args) {
        java.awt.EventQueue.invokeLater(() -> {
            new Login().setVisible(true);
        });
}
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel Left;
    private javax.swing.JButton Login_btn;
    private javax.swing.JPanel Right;
    private javax.swing.JLabel close;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPasswordField passwordField;
    private javax.swing.JTextField usernameField;
    // End of variables declaration//GEN-END:variables
}



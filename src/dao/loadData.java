/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.*;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import qlks.DatabaseConnection;

/**
 *
 * @author Admin
 */
public class loadData {
    public static void loadDataToTable(JTable table, String tableName) {
    try (Connection conn = DatabaseConnection.getConnection()) {
        String sql = "SELECT * FROM " + tableName;
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        // Lấy metadata để tạo cột tự động
        ResultSetMetaData meta = rs.getMetaData();
        int columnCount = meta.getColumnCount();
        DefaultTableModel model = new DefaultTableModel();

        // Tên cột
        for (int i = 1; i <= columnCount; i++) {
            model.addColumn(meta.getColumnName(i));
        }

        // Dữ liệu hàng
        while (rs.next()) {
            Object[] row = new Object[columnCount];
            for (int i = 0; i < columnCount; i++) {
                row[i] = rs.getObject(i + 1);
            }
            model.addRow(row);
        }

        table.setModel(model);

    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Lỗi khi load dữ liệu từ " + tableName + ": " + e.getMessage());
    }
}
 public static int getSelectedIdFromTable(JTable table) {
    int row = table.getSelectedRow();
    if (row < 0) {
        JOptionPane.showMessageDialog(null, "Vui lòng chọn một dòng.");
        return -1;
    }

    Object value = table.getValueAt(row, 0); // giả định cột đầu tiên là ID
    if (value instanceof Integer) return (Integer) value;
    try {
        return Integer.parseInt(value.toString());
    } catch (NumberFormatException e) {
        e.printStackTrace();
        return -1;
    }
}       
}

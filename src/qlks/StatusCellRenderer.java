/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package qlks;

/**
 *
 * @author Admin
 */
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

// Lớp này kế thừa từ JLabel và triển khai TableCellRenderer
// Kế thừa JLabel giúp chúng ta dễ dàng tùy chỉnh text, màu nền, màu chữ...
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

// Lớp này kế thừa từ JLabel và triển khai TableCellRenderer
// Kế thừa JLabel giúp chúng ta dễ dàng tùy chỉnh text, màu nền, màu chữ...
public class StatusCellRenderer extends JLabel implements TableCellRenderer {

    // Các màu sắc tương ứng với từng trạng thái
    private static final Color CONFIRMED_COLOR = Color.decode("#0d6efd"); // Xanh dương
    private static final Color CHECKED_IN_COLOR = Color.decode("#198754"); // Xanh lá
    private static final Color CHECKED_OUT_COLOR = Color.decode("#6c757d"); // Xám
    private static final Color CANCELLED_COLOR = Color.decode("#dc3545"); // Đỏ

    public StatusCellRenderer() {
        setOpaque(true); // Rất quan trọng! Phải có để màu nền được hiển thị
        setFont(new Font("Segoe UI", Font.BOLD, 12));
        setForeground(Color.WHITE); // Màu chữ luôn là màu trắng
        setHorizontalAlignment(CENTER); // Căn giữa chữ trong ô
        setBorder(new EmptyBorder(5, 10, 5, 10)); // Thêm đệm để trông giống "viên thuốc"
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value,
                                                   boolean isSelected, boolean hasFocus,
                                                   int row, int column) { 
        // Lấy giá trị của ô (chính là chuỗi trạng thái)
        String status = (value == null) ? "" : value.toString();

        // Dựa vào giá trị trạng thái để quyết định màu nền
        switch (status) {
            case "Confirmed":
                setBackground(CONFIRMED_COLOR);
                break;
            case "CheckedIn":
                setBackground(CHECKED_IN_COLOR);
                break;
            case "CheckedOut":
                setBackground(CHECKED_OUT_COLOR);
                break;
            case "Cancelled":
                setBackground(CANCELLED_COLOR);
                break;
            default:
                // Nếu trạng thái không xác định, dùng màu mặc định
                setBackground(table.getBackground());
                setForeground(table.getForeground());
                break;
        }

        // Nếu hàng đang được chọn, ưu tiên hiển thị màu chọn của bảng
        if (isSelected) {
            setBackground(table.getSelectionBackground());
            setForeground(table.getSelectionForeground());
        }

        // Đặt text cho JLabel
        setText(status);

        // Trả về chính component này để JTable vẽ nó lên ô
        return this;
    }
}
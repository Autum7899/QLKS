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
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import java.awt.*;

/**
 * Một lớp JTable tùy chỉnh, kế thừa được, đã tích hợp sẵn toàn bộ
 * phong cách trang trí hiện đại của ứng dụng (header, màu hàng xen kẽ, font chữ...).
 */
public class ThemedTable extends JTable {

    // Định nghĩa các màu sắc theo chủ đề
    private static final Color THEME_DARK = Color.decode("#ffffff");
    private static final Color ACCENT_GOLD = Color.decode("#000000");
    private static final Color PANEL_BACKGROUND = new Color(245, 245, 245);
    private static final Color GRID_COLOR = new Color(220, 220, 220);
    private static final Color SELECTION_BACKGROUND = new Color(173, 216, 230, 150);
    private static final Color EVEN_ROW_COLOR = PANEL_BACKGROUND;
    private static final Color ODD_ROW_COLOR = Color.WHITE;

    // Constructors mirroring JTable constructors
    public ThemedTable() {
        super();
        applyStyling();
    }

    public ThemedTable(TableModel model) {
        super(model);
        applyStyling();
    }

    /**
     * Phương thức chính để áp dụng tất cả các style.
     * Được gọi ngay khi table được khởi tạo.
     */
    private void applyStyling() {
        // --- Tùy chỉnh các thuộc tính chung của bảng ---
        setBackground(PANEL_BACKGROUND);
        setForeground(ACCENT_GOLD);
        setRowHeight(35);
        setGridColor(GRID_COLOR);
        setShowVerticalLines(false); // Chỉ hiển thị đường kẻ ngang
        setFont(new Font("Segoe UI", Font.PLAIN, 14));
        setSelectionBackground(SELECTION_BACKGROUND);
        setSelectionForeground(ACCENT_GOLD);
        setFillsViewportHeight(true); // Đảm bảo bảng tô màu toàn bộ viewport

        // --- Tùy chỉnh Header của bảng ---
        JTableHeader header = getTableHeader();
        header.setBackground(THEME_DARK);
        header.setForeground(ACCENT_GOLD);
        header.setFont(new Font("Segoe UI", Font.BOLD, 15));
        header.setReorderingAllowed(false); // Không cho phép kéo thả cột
        header.setDefaultRenderer(new HeaderRenderer());
    }

    /**
     * Ghi đè phương thức này để vẽ màu nền xen kẽ (zebra-striping).
     */
    @Override
public Component prepareRenderer(javax.swing.table.TableCellRenderer renderer, int row, int column) {
    // Lấy component sẽ được vẽ (đã được cấu hình bởi renderer mặc định hoặc cell renderer)
    Component component = super.prepareRenderer(renderer, row, column);

    // Chỉ áp dụng màu nền xen kẽ nếu hàng đó KHÔNG được chọn
    if (!isRowSelected(row)) {
        
        // QUAN TRỌNG: Thay đổi chỉ số cột "Trạng thái" của bạn ở đây
        int statusColumnIndex = 5; 

        // Nếu cột hiện tại KHÔNG PHẢI là cột trạng thái, chúng ta sẽ tô màu xen kẽ
        if (column != statusColumnIndex) {
            component.setBackground(row % 2 == 0 ? EVEN_ROW_COLOR : ODD_ROW_COLOR);
            // Đảm bảo màu chữ là màu mặc định
            component.setForeground(ACCENT_GOLD);
        } else {
            // Nếu ĐÚNG là cột trạng thái, chúng ta không làm gì cả.
            // Màu sắc và các thuộc tính khác đã được StatusCellRenderer quyết định rồi,
            // chúng ta chỉ việc giữ nguyên nó.
        }
    }
    // Nếu hàng được chọn, JTable sẽ tự động xử lý màu nền và màu chữ lựa chọn.
    // Lời gọi super.prepareRenderer() ở trên đã làm việc này.

    return component;
}

    /**
     * Ghi đè phương thức này để tự động trang trí JScrollPane cha.
     * addNotify() được gọi khi component được thêm vào một container.
     */
    @Override
    public void addNotify() {
        super.addNotify();
        // Tìm JScrollPane cha và trang trí nó
        Container parent = SwingUtilities.getUnwrappedParent(this);
        if (parent instanceof JViewport) {
            Container grandparent = parent.getParent();
            if (grandparent instanceof JScrollPane) {
                JScrollPane scrollPane = (JScrollPane) grandparent;
                scrollPane.setBorder(BorderFactory.createEmptyBorder());
                scrollPane.getViewport().setBackground(PANEL_BACKGROUND);
            }
        }
    }

    /**
     * Lớp nội bộ để vẽ lại header của bảng cho đẹp hơn.
     */
    private static class HeaderRenderer extends DefaultTableCellRenderer {
        public HeaderRenderer() {
            setHorizontalAlignment(JLabel.LEFT);
            setOpaque(true);
            setBorder(new EmptyBorder(10, 10, 10, 10)); // Thêm đệm
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value,
                                                       boolean isSelected, boolean hasFocus,
                                                       int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            JTableHeader header = table.getTableHeader();
            if (header != null) {
                setBackground(header.getBackground());
                setForeground(header.getForeground());
                setFont(header.getFont());
            }
            return this;
        }
    }
}

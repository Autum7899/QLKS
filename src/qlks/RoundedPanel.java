package qlks;


import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import javax.swing.JPanel;

/**
 * Phiên bản JPanel chỉ tập trung vào việc tạo ra một nền bo góc đẹp mắt,
 * không có tiêu đề.
 */
public class RoundedPanel extends JPanel {

    private final Color panelBackground;
    private final Color panelBorderColor;
    private final int cornerRadius;

    /**
     * Constructor không tham số để tạo một panel bo góc.
     */
    public RoundedPanel() {
        super();

        // --- Thiết lập màu sắc ---
        this.panelBackground = new Color(255, 255, 255, 170); // Nền trắng mờ
        this.panelBorderColor = new Color(255, 255, 255, 220); // Viền trắng đục
        this.cornerRadius = 25;

        // Làm cho panel trong suốt để có thể vẽ nền tùy chỉnh
        setOpaque(false);

        // Thêm đệm bên trong để các thành phần con không bị sát viền
        setBorder(new EmptyBorder(15, 15, 15, 15));
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Vẽ nền bo góc
        g2.setColor(panelBackground);
        g2.fill(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius));

        // Vẽ viền
        g2.setColor(panelBorderColor);
        g2.draw(new RoundRectangle2D.Float(0, 0, getWidth() - 1, getHeight() - 1, cornerRadius, cornerRadius));

        g2.dispose();
        
        // Gọi paintComponent của lớp cha để vẽ các thành phần con
        super.paintComponent(g);
    }
}
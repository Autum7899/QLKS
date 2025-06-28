package qlks;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.RoundRectangle2D;

/**
 * A custom JFrame with rounded corners.
 * This frame is undecorated, so it has no default title bar.
 * It can be dragged by clicking and dragging anywhere on the frame.
 */
public class RoundedFrame extends JFrame {

    private int cornerRadius;
    private Point initialClick; // To store the initial mouse click point for dragging

    public RoundedFrame(String title, int radius) {
        super(title);
        this.cornerRadius = radius;

        // --- Core settings for a custom-shaped window ---
        setUndecorated(true);
        setBackground(new Color(0, 0, 0, 0)); // Transparent background

        // --- Draggable functionality ---
        addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();
                getComponentAt(initialClick);
            }
        });

        addMouseMotionListener(new MouseAdapter() {
            @Override
            public void mouseDragged(MouseEvent e) {
                int thisX = getLocation().x;
                int thisY = getLocation().y;
                int xMoved = e.getX() - initialClick.x;
                int yMoved = e.getY() - initialClick.y;
                int X = thisX + xMoved;
                int Y = thisY + yMoved;
                setLocation(X, Y);
            }
        });

        // --- IMPORTANT: Update shape when component is resized ---
        // This ensures the rounded corners are applied correctly after the frame size is set.
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                setShape(new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius));
            }
        });
    }

    @Override
    public void setContentPane(Container contentPane) {
        if (contentPane instanceof JComponent) {
            ((JComponent) contentPane).setOpaque(false);
        }
        super.setContentPane(contentPane);
    }
    
    @Override
    public void paint(Graphics g) {
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Draw the visible background of the window
        g2d.setColor(getContentPane().getBackground()); 
        g2d.fillRoundRect(0, 0, getWidth(), getHeight(), cornerRadius, cornerRadius);

        // Paint all the components inside the frame
        super.paint(g);
        
        g2d.dispose();
    }
}

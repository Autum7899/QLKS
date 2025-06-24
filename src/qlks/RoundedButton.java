package qlks;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;

/**
 * A custom JButton that draws itself with rounded corners and a hover effect.
 */
public class RoundedButton extends JButton {

    private boolean over;
    private Color color;
    private Color colorOver;
    private Color colorClick;
    private int radius = 15; // Default radius is 0 for sharp corners

    public RoundedButton() {
        // Initialize colors based on the user's palette
        // Enchanted Emerald: #004D4F
        color = new Color(0, 77, 79); 
        // Fence Green: #092629 (for hover and click)
        colorOver = new Color(9, 38, 41);
        colorClick = new Color(9, 38, 41);
        
        // Make the button transparent so we can draw our own shape
        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusPainted(false);
        
        // Add mouse listener for hover and click effects
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent me) {
                over = true;
                repaint(); // Repaint the button on hover
            }

            @Override
            public void mouseExited(MouseEvent me) {
                over = false;
                repaint(); // Repaint when mouse leaves
            }

            @Override
            public void mousePressed(MouseEvent me) {
                // The color change is handled in paintComponent
                repaint();
            }

            @Override
            public void mouseReleased(MouseEvent me) {
                // The color change is handled in paintComponent
                repaint();
            }
        });
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
        repaint();
    }
    
    // Setters for colors to allow customization from the GUI builder
    public void setColor(Color color) {
        this.color = color;
        repaint();
    }
    
    public void setColorOver(Color colorOver) {
        this.colorOver = colorOver;
        repaint();
    }

    public void setColorClick(Color colorClick) {
        this.colorClick = colorClick;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // Determine the background color based on the button's state
        if (getModel().isPressed()) {
            g2.setColor(colorClick);
        } else if (over) {
            g2.setColor(colorOver);
        } else {
            g2.setColor(color);
        }
        
        // Paint the button background
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
        
        g2.dispose();
        
        // Let the parent class (JButton) paint the text and icon on top of our background
        super.paintComponent(g);
    }
}

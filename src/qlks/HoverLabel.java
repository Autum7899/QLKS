package qlks;

import javax.swing.JLabel;
import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * A custom JLabel that changes its background color on mouse hover.
 */
public class HoverLabel extends JLabel {

    private Color defaultColor;
    private Color hoverColor;

    public HoverLabel(String text) {
        super(text);

        // Define the colors based on the user's palette
        // Enchanted Emerald: #004D4F (for hover)
        this.hoverColor = new Color(0, 77, 79);
        // Fence Green: #092629 (for default state)
        this.defaultColor = new Color(9, 38, 41);

        // Set initial state
        setBackground(defaultColor);
        setOpaque(true); // Crucial for the background color to be visible

        // Add mouse listener to handle hover events
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Change background color when mouse enters
                setBackground(hoverColor);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Change background color back when mouse exits
                setBackground(defaultColor);
            }
        });
    }

    // --- Optional: Add setters to allow color customization from the GUI Builder ---

    public void setDefaultColor(Color defaultColor) {
        this.defaultColor = defaultColor;
        setBackground(defaultColor); // Update immediately
    }

    public void setHoverColor(Color hoverColor) {
        this.hoverColor = hoverColor;
    }
}

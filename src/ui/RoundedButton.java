package ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/** A button with rounded corners and a hover effect. */
public class RoundedButton extends JButton {

    private final Color base;
    private final Color textColor;
    private boolean hover;

    public RoundedButton(String text, Color base, Color textColor) {
        super(text);
        this.base = base;
        this.textColor = textColor;
        setContentAreaFilled(false);
        setFocusPainted(false);
        setBorderPainted(false);
        setFont(Theme.bold(13));
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setBorder(BorderFactory.createEmptyBorder(9, 16, 9, 16));
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                hover = true;
                repaint();
            }

            @Override
            public void mouseExited(MouseEvent e) {
                hover = false;
                repaint();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        Theme.smooth(g2);

        Color fill = base;
        Color text = textColor;
        if (!isEnabled()) {
            fill = new Color(0xF3F4F6);
            text = new Color(0x9CA3AF);
        } else if (getModel().isPressed()) {
            fill = base.darker();
        } else if (hover) {
            fill = base.brighter();
        }

        g2.setColor(fill);
        g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

        g2.setFont(getFont());
        FontMetrics fm = g2.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(getText())) / 2;
        int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
        g2.setColor(text);
        g2.drawString(getText(), x, y);
        g2.dispose();
    }
}

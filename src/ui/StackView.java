package ui;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/** Draws a stack as a column of cards. The first item is the TOP of the stack. */
public class StackView extends JPanel {

    private static final int ROW = 44;
    private static final int HEADER = 52;

    private final String title;
    private final Color accent;
    private List<String> items = new ArrayList<>();

    public StackView(String title, Color accent) {
        this.title = title;
        this.accent = accent;
        setOpaque(false);
    }

    public void setItems(List<String> items) {
        this.items = items;
        revalidate();
        repaint();
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(230, HEADER + Math.max(1, items.size()) * ROW + 12);
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        Theme.smooth(g2);
        int w = getWidth();
        int h = Math.max(getHeight(), getPreferredSize().height);

        // card background
        g2.setColor(Theme.CARD);
        g2.fillRoundRect(0, 0, w - 1, h - 1, 18, 18);
        g2.setColor(Theme.LINE);
        g2.drawRoundRect(0, 0, w - 1, h - 1, 18, 18);

        // header: title + count badge
        g2.setFont(Theme.bold(13));
        g2.setColor(accent);
        g2.drawString(title, 16, 31);

        String count = String.valueOf(items.size());
        int badgeW = 26;
        g2.fillRoundRect(w - badgeW - 14, 14, badgeW, 24, 12, 12);
        g2.setColor(Color.WHITE);
        FontMetrics fm = g2.getFontMetrics();
        g2.drawString(count, w - badgeW - 14 + (badgeW - fm.stringWidth(count)) / 2, 31);

        // items
        if (items.isEmpty()) {
            g2.setFont(Theme.plain(13));
            g2.setColor(Theme.MUTED);
            g2.drawString("(empty)", 18, HEADER + 24);
        }

        for (int i = 0; i < items.size(); i++) {
            int y = HEADER + i * ROW;
            boolean top = (i == 0);

            g2.setColor(top ? tint(accent) : new Color(0xF9FAFB));
            g2.fillRoundRect(12, y, w - 24, ROW - 8, 12, 12);
            if (top) {
                g2.setColor(accent);
                g2.drawRoundRect(12, y, w - 24, ROW - 8, 12, 12);
            }

            g2.setFont(top ? Theme.bold(13) : Theme.plain(13));
            g2.setColor(Theme.TEXT);
            g2.drawString(fit(items.get(i), g2.getFontMetrics(), w - 100), 24, y + 23);

            if (top) {
                g2.setFont(Theme.bold(10));
                g2.setColor(accent);
                g2.drawString("TOP", w - 46, y + 22);
            }
        }
        g2.dispose();
    }

    // light version of the accent color for the TOP card
    private static Color tint(Color c) {
        return new Color(c.getRed() + (255 - c.getRed()) * 85 / 100,
                c.getGreen() + (255 - c.getGreen()) * 85 / 100,
                c.getBlue() + (255 - c.getBlue()) * 85 / 100);
    }

    // cut long text and add "..."
    private static String fit(String s, FontMetrics fm, int maxWidth) {
        if (fm.stringWidth(s) <= maxWidth) return s;
        while (s.length() > 1 && fm.stringWidth(s + "...") > maxWidth) {
            s = s.substring(0, s.length() - 1);
        }
        return s + "...";
    }
}

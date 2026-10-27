package ui;

import javax.swing.*;
import java.awt.*;

/** Shows a fake "web page" for the current URL: colored banner, title and placeholder content. */
public class PagePanel extends JPanel {

    private String url = "about:blank";

    public PagePanel() {
        setOpaque(false);
    }

    public void setUrl(String url) {
        this.url = url;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        Theme.smooth(g2);
        int w = getWidth();
        int h = getHeight();

        // card
        g2.setColor(Theme.CARD);
        g2.fillRoundRect(0, 0, w - 1, h - 1, 18, 18);
        g2.setColor(Theme.LINE);
        g2.drawRoundRect(0, 0, w - 1, h - 1, 18, 18);

        boolean blank = url.equals("about:blank");

        // banner (clipped to the card's rounded top)
        Color c1 = blank ? new Color(0x9CA3AF) : Color.getHSBColor((Math.abs(url.hashCode()) % 360) / 360f, 0.60f, 0.88f);
        Color c2 = blank ? new Color(0x6B7280) : Color.getHSBColor((Math.abs(url.hashCode()) % 360) / 360f, 0.75f, 0.60f);
        Shape oldClip = g2.getClip();
        g2.clip(new java.awt.geom.RoundRectangle2D.Float(0, 0, w - 1, h - 1, 18, 18));
        g2.setPaint(new GradientPaint(0, 0, c1, w, 170, c2));
        g2.fillRect(0, 0, w, 170);
        g2.setClip(oldClip);

        // title in banner
        g2.setColor(Color.WHITE);
        g2.setFont(Theme.bold(34));
        g2.drawString(siteName(), 32, 92);
        g2.setFont(Theme.plain(14));
        g2.setColor(new Color(255, 255, 255, 215));
        g2.drawString(blank ? "Type a URL above or pick a quick link" : "Welcome to " + url, 34, 124);

        // address under banner
        g2.setFont(Theme.plain(13));
        g2.setColor(Theme.MUTED);
        g2.drawString(blank ? "about:blank" : "https://" + url, 32, 204);

        // placeholder content lines
        g2.setColor(new Color(0xF3F4F6));
        int y = 226;
        int[] widths = {w - 64, w - 120, w - 90, w - 200};
        for (int i = 0; i < widths.length && y + 14 < h - 16; i++) {
            g2.fillRoundRect(32, y, Math.max(40, widths[i]), 14, 8, 8);
            y += 26;
        }
        g2.dispose();
    }

    // "www.google.com/maps" -> "Google"
    private String siteName() {
        if (url.equals("about:blank")) return "New Tab";
        String s = url.replaceFirst("^https?://", "").replaceFirst("^www\\.", "");
        int end = s.length();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (ch == '.' || ch == '/') {
                end = i;
                break;
            }
        }
        s = s.substring(0, end);
        return s.isEmpty() ? url : Character.toUpperCase(s.charAt(0)) + s.substring(1);
    }
}

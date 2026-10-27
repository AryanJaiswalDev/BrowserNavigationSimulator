package ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

/** Colors and fonts used by the whole UI, in one place. */
public class Theme {

    public static final Color BG = new Color(0xF3F4F6);
    public static final Color CARD = Color.WHITE;
    public static final Color TEXT = new Color(0x111827);
    public static final Color MUTED = new Color(0x6B7280);
    public static final Color LINE = new Color(0xE5E7EB);

    public static final Color BLUE = new Color(0x2563EB);    // back stack / main accent
    public static final Color GREEN = new Color(0x16A34A);   // forward stack
    public static final Color GRAY_BTN = new Color(0xE5E7EB);

    public static Font plain(int size) {
        return new Font("SansSerif", Font.PLAIN, size);
    }

    public static Font bold(int size) {
        return new Font("SansSerif", Font.BOLD, size);
    }

    public static void smooth(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
    }
}

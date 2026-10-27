package ui;

import model.BrowserHistory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * Main window: toolbar (Back / Forward / address bar / Go), quick links,
 * a fake page preview, and live views of both stacks.
 */
public class BrowserFrame extends JFrame {

    private static final String[] QUICK_LINKS =
            {"google.com", "youtube.com", "github.com", "wikipedia.org", "tcet.edu.in"};

    private final BrowserHistory history = new BrowserHistory();

    private final RoundedButton backBtn = new RoundedButton("←  Back", Theme.BLUE, Color.WHITE);
    private final RoundedButton forwardBtn = new RoundedButton("Forward  →", Theme.GREEN, Color.WHITE);
    private final RoundedButton goBtn = new RoundedButton("Go", Theme.TEXT, Color.WHITE);
    private final AddressField addressBar = new AddressField();

    private final PagePanel pagePanel = new PagePanel();
    private final StackView backView = new StackView("BACK STACK", Theme.BLUE);
    private final StackView forwardView = new StackView("FORWARD STACK", Theme.GREEN);
    private final JLabel status = new JLabel();

    public BrowserFrame() {
        super("Browser Navigation Simulator");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(960, 600);
        setMinimumSize(new Dimension(820, 520));
        setLocationRelativeTo(null);

        JPanel root = new JPanel(new BorderLayout(14, 14));
        root.setBackground(Theme.BG);
        root.setBorder(BorderFactory.createEmptyBorder(14, 14, 10, 14));
        setContentPane(root);

        root.add(buildTop(), BorderLayout.NORTH);
        root.add(pagePanel, BorderLayout.CENTER);
        root.add(buildStacks(), BorderLayout.EAST);
        root.add(buildStatusBar(), BorderLayout.SOUTH);

        wireActions();
        refresh("Stack demo: every visit pushes the current page onto the Back stack.");
    }

    // ---------- layout ----------

    private JPanel buildTop() {
        JPanel top = new JPanel(new BorderLayout(0, 10));
        top.setOpaque(false);

        JPanel bar = new JPanel(new BorderLayout(10, 0));
        bar.setOpaque(false);
        JPanel nav = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        nav.setOpaque(false);
        nav.add(backBtn);
        nav.add(forwardBtn);
        bar.add(nav, BorderLayout.WEST);
        bar.add(addressBar, BorderLayout.CENTER);
        bar.add(goBtn, BorderLayout.EAST);

        JPanel chips = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        chips.setOpaque(false);
        JLabel label = new JLabel("Quick links:");
        label.setFont(Theme.plain(12));
        label.setForeground(Theme.MUTED);
        chips.add(label);
        for (String link : QUICK_LINKS) {
            RoundedButton chip = new RoundedButton(link, Theme.GRAY_BTN, Theme.TEXT);
            chip.setFont(Theme.plain(12));
            chip.setBorder(BorderFactory.createEmptyBorder(5, 12, 5, 12));
            chip.addActionListener(e -> visit(link));
            chips.add(chip);
        }

        top.add(bar, BorderLayout.NORTH);
        top.add(chips, BorderLayout.SOUTH);
        return top;
    }

    private JPanel buildStacks() {
        JPanel panel = new JPanel(new GridLayout(2, 1, 0, 14));
        panel.setOpaque(false);
        panel.setPreferredSize(new Dimension(240, 0));
        panel.add(scroll(backView));
        panel.add(scroll(forwardView));
        return panel;
    }

    private JScrollPane scroll(JComponent c) {
        JScrollPane sp = new JScrollPane(c);
        sp.setBorder(null);
        sp.setOpaque(false);
        sp.getViewport().setOpaque(false);
        sp.getVerticalScrollBar().setUnitIncrement(16);
        return sp;
    }

    private JComponent buildStatusBar() {
        status.setFont(Theme.plain(12));
        status.setForeground(Theme.MUTED);
        status.setBorder(BorderFactory.createEmptyBorder(2, 4, 0, 0));
        return status;
    }

    // ---------- actions ----------

    private void wireActions() {
        goBtn.addActionListener(e -> visit(addressBar.getText().trim()));
        addressBar.addActionListener(e -> visit(addressBar.getText().trim()));
        backBtn.addActionListener(e -> goBack());
        forwardBtn.addActionListener(e -> goForward());

        // keyboard: Alt + Left / Alt + Right
        JRootPane rp = getRootPane();
        rp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, InputEvent_ALT()), "back");
        rp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW)
                .put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, InputEvent_ALT()), "forward");
        rp.getActionMap().put("back", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                goBack();
            }
        });
        rp.getActionMap().put("forward", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                goForward();
            }
        });
    }

    private static int InputEvent_ALT() {
        return java.awt.event.InputEvent.ALT_DOWN_MASK;
    }

    private void visit(String url) {
        if (url.isEmpty()) return;
        String previous = history.getCurrent();
        history.visit(url);
        addressBar.setText("");
        refresh("push(\"" + previous + "\") on Back stack, Forward stack cleared.");
    }

    private void goBack() {
        if (!history.canGoBack()) return;
        String leaving = history.getCurrent();
        history.back();
        refresh("pop() from Back stack, push(\"" + leaving + "\") on Forward stack.");
    }

    private void goForward() {
        if (!history.canGoForward()) return;
        String leaving = history.getCurrent();
        history.forward();
        refresh("pop() from Forward stack, push(\"" + leaving + "\") on Back stack.");
    }

    // redraw everything from the history object
    private void refresh(String message) {
        pagePanel.setUrl(history.getCurrent());
        backBtn.setEnabled(history.canGoBack());
        forwardBtn.setEnabled(history.canGoForward());
        backView.setItems(history.getBackStack().toList());
        forwardView.setItems(history.getForwardStack().toList());
        status.setText(message + "     [Alt+Left = Back, Alt+Right = Forward]");
    }

    // ---------- rounded address field with placeholder ----------

    private static class AddressField extends JTextField {
        AddressField() {
            setOpaque(false);
            setFont(Theme.plain(14));
            setForeground(Theme.TEXT);
            setCaretColor(Theme.TEXT);
            setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            Theme.smooth(g2);
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 22, 22);
            g2.setColor(isFocusOwner() ? Theme.BLUE : Theme.LINE);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 22, 22);
            g2.dispose();

            super.paintComponent(g);

            if (getText().isEmpty() && !isFocusOwner()) {
                Graphics2D h = (Graphics2D) g.create();
                Theme.smooth(h);
                h.setFont(getFont());
                h.setColor(Theme.MUTED);
                h.drawString("Type a website, e.g. google.com", 16,
                        (getHeight() - h.getFontMetrics().getHeight()) / 2 + h.getFontMetrics().getAscent());
                h.dispose();
            }
        }
    }
}

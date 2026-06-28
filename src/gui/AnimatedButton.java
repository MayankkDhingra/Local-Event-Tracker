package gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.RoundRectangle2D;

public class AnimatedButton extends JButton {

    private Color baseColor;
    private Color hoverColor;
    private float glowAlpha = 0f;
    private Timer glowTimer;
    private boolean hovering = false;

    public AnimatedButton(String text, Color base, Color hover) {
        super(text);
        this.baseColor = base;
        this.hoverColor = hover;

        setFont(Theme.FONT_HEADING);
        setForeground(Theme.TEXT_PRIMARY);
        setFocusPainted(false);
        setBorderPainted(false);
        setContentAreaFilled(false);
        setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        setPreferredSize(new Dimension(160, 40));

        // Glow animation timer
        glowTimer = new Timer(16, e -> {
            if (hovering && glowAlpha < 1f) {
                glowAlpha = Math.min(1f, glowAlpha + 0.08f);
            } else if (!hovering && glowAlpha > 0f) {
                glowAlpha = Math.max(0f, glowAlpha - 0.06f);
            } else {
                glowTimer.stop();
            }
            repaint();
        });

        addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                hovering = true;
                glowTimer.start();
            }
            public void mouseExited(MouseEvent e) {
                hovering = false;
                glowTimer.start();
            }
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int w = getWidth(), h = getHeight();

        // Glow effect behind button
        if (glowAlpha > 0) {
            g2.setColor(new Color(
                hoverColor.getRed(), hoverColor.getGreen(), hoverColor.getBlue(),
                (int)(60 * glowAlpha)
            ));
            g2.fillRoundRect(-4, -4, w + 8, h + 8, 20, 20);
        }

        // Button background
        Color bg = interpolateColor(baseColor, hoverColor, glowAlpha);
        g2.setColor(bg);
        g2.fillRoundRect(0, 0, w, h, 12, 12);

        // Border
        g2.setColor(new Color(
            hoverColor.getRed(), hoverColor.getGreen(), hoverColor.getBlue(),
            (int)(180 + 75 * glowAlpha)
        ));
        g2.setStroke(new BasicStroke(1.2f));
        g2.drawRoundRect(0, 0, w - 1, h - 1, 12, 12);

        // Text
        g2.setFont(getFont());
        g2.setColor(getForeground());
        FontMetrics fm = g2.getFontMetrics();
        int tx = (w - fm.stringWidth(getText())) / 2;
        int ty = (h + fm.getAscent() - fm.getDescent()) / 2;
        g2.drawString(getText(), tx, ty);

        g2.dispose();
    }

    private Color interpolateColor(Color a, Color b, float t) {
        int r = (int)(a.getRed()   + (b.getRed()   - a.getRed())   * t * 0.3f);
        int g = (int)(a.getGreen() + (b.getGreen() - a.getGreen()) * t * 0.3f);
        int bl= (int)(a.getBlue()  + (b.getBlue()  - a.getBlue())  * t * 0.3f);
        return new Color(
            Math.max(0, Math.min(255, r)),
            Math.max(0, Math.min(255, g)),
            Math.max(0, Math.min(255, bl))
        );
    }
}

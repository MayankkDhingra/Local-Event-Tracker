package gui;

import database.EventDAO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class MainFrame extends JFrame {

    private EventDAO dao;
    private JPanel contentPanel;
    private CardLayout cardLayout;
    private JPanel[] navButtons;
    private String currentPage = "view";

    private ViewEventsPanel viewPanel;
    private AddEventPanel addPanel;
    private SearchPanel searchPanel;

    public MainFrame() {
        dao = new EventDAO();
        dao.seedIndianEvents();

        setTitle("🇮🇳 India Event & Festival Tracker");
        setSize(1100, 700);
        setMinimumSize(new Dimension(900, 580));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getContentPane().setBackground(Theme.BG_DEEP);
        setLayout(new BorderLayout(0, 0));

        initUI();
        showPage("view");
    }

    private void initUI() {
        add(buildSidebar(), BorderLayout.WEST);

        cardLayout   = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Theme.BG_DEEP);

        viewPanel   = new ViewEventsPanel(dao);
        addPanel    = new AddEventPanel(dao, this);
        searchPanel = new SearchPanel(dao);

        contentPanel.add(viewPanel,   "view");
        contentPanel.add(addPanel,    "add");
        contentPanel.add(searchPanel, "search");

        add(contentPanel, BorderLayout.CENTER);
    }

    private JPanel buildSidebar() {
        JPanel sidebar = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(Theme.BG_CARD);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(Theme.BG_BORDER);
                g2.setStroke(new BasicStroke(1));
                g2.drawLine(getWidth()-1, 0, getWidth()-1, getHeight());
                g2.dispose();
            }
        };
        sidebar.setPreferredSize(new Dimension(230, 700));
        sidebar.setLayout(new BorderLayout());
        sidebar.setOpaque(false);

        // ── Logo ──
        JPanel logo = new JPanel() {
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(Theme.BG_CARD);
                g2.fillRect(0, 0, getWidth(), getHeight());
                g2.setColor(Theme.BG_BORDER);
                g2.drawLine(20, getHeight()-1, getWidth()-20, getHeight()-1);
                g2.dispose();
            }
        };
        logo.setPreferredSize(new Dimension(230, 110));
        logo.setLayout(new BoxLayout(logo, BoxLayout.Y_AXIS));
        logo.setOpaque(false);
        logo.setBorder(BorderFactory.createEmptyBorder(22, 22, 18, 22));

        JLabel flagLbl = new JLabel("🇮🇳  EventTracker");
        flagLbl.setFont(new Font("Segoe UI", Font.BOLD, 18));
        flagLbl.setForeground(Theme.TEXT_PRIMARY);
        flagLbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel sub = new JLabel("India's Festival Hub");
        sub.setFont(Theme.FONT_SMALL);
        sub.setForeground(Theme.ACCENT);
        sub.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Animated accent line
        JPanel accentLine = new JPanel() {
            int tick = 0;
            Timer t = new Timer(30, e -> { tick = (tick + 2) % 230; repaint(); });
            { t.start(); setOpaque(false); setPreferredSize(new Dimension(186, 3)); setMaximumSize(new Dimension(186,3)); }
            @Override protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(Theme.BG_BORDER);
                g2.fillRoundRect(0,0,getWidth(),3,3,3);
                int gx = (tick - 60 + 230) % 230;
                GradientPaint gp = new GradientPaint(gx, 0, new Color(0,0,0,0), gx+60, 0, Theme.ACCENT, false);
                GradientPaint gp2= new GradientPaint(gx+60,0, Theme.ACCENT, gx+90,0, new Color(0,0,0,0),false);
                g2.setPaint(gp);  g2.fillRoundRect(0,0,getWidth(),3,3,3);
                g2.setPaint(gp2); g2.fillRoundRect(0,0,getWidth(),3,3,3);
                g2.dispose();
            }
        };
        accentLine.setAlignmentX(Component.LEFT_ALIGNMENT);

        logo.add(flagLbl);
        logo.add(Box.createVerticalStrut(4));
        logo.add(sub);
        logo.add(Box.createVerticalStrut(10));
        logo.add(accentLine);

        // ── Nav ──
        String[][] items = {
            {"📋", "All Events",  "view"},
            {"➕", "Add Event",   "add"},
            {"🔍", "Search",      "search"}
        };

        JPanel nav = new JPanel();
        nav.setLayout(new BoxLayout(nav, BoxLayout.Y_AXIS));
        nav.setOpaque(false);
        nav.setBorder(BorderFactory.createEmptyBorder(24, 12, 20, 12));

        navButtons = new JPanel[items.length];
        for (int i = 0; i < items.length; i++) {
            final String pg = items[i][2];
            final String ic = items[i][0];
            final String lb = items[i][1];
            final int idx   = i;

            JPanel btn = new JPanel(new FlowLayout(FlowLayout.LEFT, 14, 0)) {
                boolean hov = false;
                { setOpaque(false); setMaximumSize(new Dimension(206, 50)); setPreferredSize(new Dimension(206, 50));
                  setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
                  addMouseListener(new MouseAdapter() {
                      public void mouseEntered(MouseEvent e) { hov=true; repaint(); }
                      public void mouseExited(MouseEvent e)  { hov=false; repaint(); }
                      public void mouseClicked(MouseEvent e) { showPage(pg); }
                  });
                }
                @Override protected void paintComponent(Graphics g) {
                    Graphics2D g2=(Graphics2D)g.create();
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,RenderingHints.VALUE_ANTIALIAS_ON);
                    boolean active = pg.equals(currentPage);
                    if (active) {
                        g2.setColor(new Color(255,162,0,22));
                        g2.fillRoundRect(0,0,getWidth(),getHeight(),12,12);
                        g2.setColor(Theme.ACCENT);
                        g2.fillRoundRect(0,10,4,28,4,4);
                    } else if (hov) {
                        g2.setColor(Theme.BG_HOVER);
                        g2.fillRoundRect(0,0,getWidth(),getHeight(),12,12);
                    }
                    g2.dispose(); super.paintComponent(g);
                }
            };

            JLabel icLbl = new JLabel(ic);
            icLbl.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 17));

            JLabel txLbl = new JLabel(lb);
            txLbl.setFont(Theme.FONT_HEADING);
            txLbl.setForeground(Theme.TEXT_PRIMARY);

            btn.add(icLbl); btn.add(txLbl);
            navButtons[idx] = btn;
            nav.add(btn);
            nav.add(Box.createVerticalStrut(4));
        }

        // ── Footer ──
        JPanel foot = new JPanel();
        foot.setLayout(new BoxLayout(foot, BoxLayout.Y_AXIS));
        foot.setOpaque(false);
        foot.setBorder(BorderFactory.createEmptyBorder(12, 22, 22, 22));

        JLabel v = new JLabel("Java Swing + MySQL");
        v.setFont(Theme.FONT_SMALL); v.setForeground(Theme.TEXT_MUTED);
        v.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel oop = new JLabel("Encap · Abstract · Inherit · Poly");
        oop.setFont(Theme.FONT_SMALL); oop.setForeground(new Color(255,162,0,100));
        oop.setAlignmentX(Component.LEFT_ALIGNMENT);

        foot.add(v); foot.add(Box.createVerticalStrut(3)); foot.add(oop);

        sidebar.add(logo, BorderLayout.NORTH);
        sidebar.add(nav,  BorderLayout.CENTER);
        sidebar.add(foot, BorderLayout.SOUTH);
        return sidebar;
    }

    public void showPage(String page) {
        currentPage = page;
        cardLayout.show(contentPanel, page);
        if (navButtons != null) for (JPanel b : navButtons) b.repaint();
        if (page.equals("view")) viewPanel.loadEvents();
    }

    public void refreshViewTab() { showPage("view"); }
}

package gui;

import database.EventDAO;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class AddEventPanel extends JPanel {

    private EventDAO dao;
    private MainFrame mainFrame;

    private JTextField nameField, locationField, dateField, categoryField, organizerField;
    private JTextArea descArea;
    private JComboBox<String> stateBox, typeBox;

    public AddEventPanel(EventDAO dao, MainFrame mainFrame) {
        this.dao = dao;
        this.mainFrame = mainFrame;
        setLayout(new BorderLayout());
        setBackground(Theme.BG_DEEP);
        initUI();
    }

    private void initUI() {
        // Header
        JPanel header = new JPanel(new BorderLayout()) {
            @Override protected void paintComponent(Graphics g) {
                g.setColor(Theme.BG_CARD);
                g.fillRect(0,0,getWidth(),getHeight());
                Graphics2D g2=(Graphics2D)g;
                g2.setColor(Theme.BG_BORDER);
                g2.setStroke(new BasicStroke(1));
                g2.drawLine(0,getHeight()-1,getWidth(),getHeight()-1);
            }
        };
        header.setOpaque(false);
        header.setPreferredSize(new Dimension(0, 72));
        header.setBorder(BorderFactory.createEmptyBorder(16, 28, 16, 28));

        JLabel title = new JLabel("Add New Event");
        title.setFont(new Font("Segoe UI", Font.BOLD, 20));
        title.setForeground(Theme.TEXT_PRIMARY);
        header.add(title, BorderLayout.WEST);

        // Scrollable form
        JPanel form = new JPanel(new GridBagLayout());
        form.setBackground(Theme.BG_DEEP);
        form.setBorder(BorderFactory.createEmptyBorder(30, 60, 30, 60));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 8, 8, 8);
        gbc.anchor = GridBagConstraints.WEST;
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Row 0: Name + Location
        gbc.gridx=0; gbc.gridy=0; gbc.gridwidth=1; gbc.weightx=0;
        form.add(makeLabel("Event Name *"), gbc);
        nameField = makeField("e.g. Sunburn Festival 2026");
        gbc.gridx=1; gbc.weightx=1;
        form.add(nameField, gbc);

        gbc.gridx=2; gbc.weightx=0;
        form.add(makeLabel("Location *"), gbc);
        locationField = makeField("e.g. Vagator Beach");
        gbc.gridx=3; gbc.weightx=1;
        form.add(locationField, gbc);

        // Row 1: Date + State
        gbc.gridx=0; gbc.gridy=1; gbc.weightx=0;
        form.add(makeLabel("Date *"), gbc);
        dateField = makeField("YYYY-MM-DD");
        gbc.gridx=1; gbc.weightx=1;
        form.add(dateField, gbc);

        gbc.gridx=2; gbc.weightx=0;
        form.add(makeLabel("State *"), gbc);
        String[] states = {"Andhra Pradesh","Arunachal Pradesh","Assam","Bihar","Chhattisgarh",
            "Goa","Gujarat","Haryana","Himachal Pradesh","Jharkhand","Karnataka","Kerala",
            "Madhya Pradesh","Maharashtra","Manipur","Meghalaya","Mizoram","Nagaland","Odisha",
            "Punjab","Rajasthan","Sikkim","Tamil Nadu","Telangana","Tripura","Uttar Pradesh",
            "Uttarakhand","West Bengal","Delhi","Jammu & Kashmir","Ladakh","Puducherry"};
        stateBox = makeCombo(states);
        gbc.gridx=3; gbc.weightx=1;
        form.add(stateBox, gbc);

        // Row 2: Type + Category
        gbc.gridx=0; gbc.gridy=2; gbc.weightx=0;
        form.add(makeLabel("Event Type *"), gbc);
        typeBox = makeCombo(new String[]{"Festival","Concert","Sports","Cultural","Exhibition","Workshop","Other"});
        gbc.gridx=1; gbc.weightx=1;
        form.add(typeBox, gbc);

        gbc.gridx=2; gbc.weightx=0;
        form.add(makeLabel("Category"), gbc);
        categoryField = makeField("e.g. Religious, Electronic...");
        gbc.gridx=3; gbc.weightx=1;
        form.add(categoryField, gbc);

        // Row 3: Organizer (full width)
        gbc.gridx=0; gbc.gridy=3; gbc.weightx=0;
        form.add(makeLabel("Organizer"), gbc);
        organizerField = makeField("e.g. BookMyShow Live");
        gbc.gridx=1; gbc.weightx=1; gbc.gridwidth=3;
        form.add(organizerField, gbc);
        gbc.gridwidth=1;

        // Row 4: Description
        gbc.gridx=0; gbc.gridy=4; gbc.weightx=0; gbc.anchor=GridBagConstraints.NORTHWEST;
        form.add(makeLabel("Description"), gbc);
        gbc.anchor=GridBagConstraints.WEST;

        descArea = new JTextArea(4, 20);
        descArea.setFont(Theme.FONT_BODY);
        descArea.setBackground(Theme.BG_SURFACE);
        descArea.setForeground(Theme.TEXT_PRIMARY);
        descArea.setCaretColor(Theme.ACCENT);
        descArea.setLineWrap(true);
        descArea.setWrapStyleWord(true);
        descArea.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Theme.BG_BORDER, 1, true),
            BorderFactory.createEmptyBorder(8,10,8,10)
        ));
        JScrollPane descScroll = new JScrollPane(descArea);
        descScroll.setBorder(BorderFactory.createEmptyBorder());
        descScroll.getViewport().setBackground(Theme.BG_SURFACE);
        gbc.gridx=1; gbc.weightx=1; gbc.gridwidth=3;
        form.add(descScroll, gbc);
        gbc.gridwidth=1;

        // Row 5: Buttons
        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 16, 0));
        btnRow.setOpaque(false);

        AnimatedButton submitBtn = new AnimatedButton("✦  Add Event", new Color(60,40,0), Theme.ACCENT);
        submitBtn.setPreferredSize(new Dimension(160, 44));
        submitBtn.setForeground(Theme.BG_DEEP);
        submitBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        submitBtn.addActionListener(e -> submitEvent());

        AnimatedButton clearBtn = new AnimatedButton("↺  Clear Form", Theme.BG_SURFACE, Theme.BG_BORDER);
        clearBtn.setPreferredSize(new Dimension(140, 44));
        clearBtn.addActionListener(e -> clearForm());

        btnRow.add(submitBtn);
        btnRow.add(clearBtn);

        gbc.gridx=0; gbc.gridy=5; gbc.gridwidth=4; gbc.anchor=GridBagConstraints.CENTER;
        gbc.insets = new Insets(20, 8, 8, 8);
        form.add(btnRow, gbc);

        JScrollPane scrollPane = new JScrollPane(form);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        scrollPane.getViewport().setBackground(Theme.BG_DEEP);

        add(header, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
    }

    private JLabel makeLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(new Font("Segoe UI", Font.BOLD, 12));
        l.setForeground(Theme.TEXT_SECONDARY);
        return l;
    }

    private JTextField makeField(String placeholder) {
        JTextField f = new JTextField(20) {
            @Override protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (getText().isEmpty() && !isFocusOwner()) {
                    Graphics2D g2=(Graphics2D)g.create();
                    g2.setColor(Theme.TEXT_MUTED);
                    g2.setFont(getFont().deriveFont(Font.ITALIC));
                    Insets ins = getInsets();
                    g2.drawString(placeholder, ins.left+2, getHeight()/2 + g2.getFontMetrics().getAscent()/2 - 1);
                    g2.dispose();
                }
            }
        };
        f.setFont(Theme.FONT_BODY);
        f.setBackground(Theme.BG_SURFACE);
        f.setForeground(Theme.TEXT_PRIMARY);
        f.setCaretColor(Theme.ACCENT);
        f.setPreferredSize(new Dimension(200, 38));
        f.setBorder(BorderFactory.createCompoundBorder(
            new LineBorder(Theme.BG_BORDER, 1, true),
            BorderFactory.createEmptyBorder(6, 10, 6, 10)
        ));

        // Focus glow border
        f.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                f.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(Theme.ACCENT_DIM, 1, true),
                    BorderFactory.createEmptyBorder(6,10,6,10)));
                f.repaint();
            }
            public void focusLost(FocusEvent e) {
                f.setBorder(BorderFactory.createCompoundBorder(
                    new LineBorder(Theme.BG_BORDER, 1, true),
                    BorderFactory.createEmptyBorder(6,10,6,10)));
                f.repaint();
            }
        });
        return f;
    }

    private JComboBox<String> makeCombo(String[] items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setFont(Theme.FONT_BODY);
        cb.setBackground(Theme.BG_SURFACE);
        cb.setForeground(Theme.TEXT_PRIMARY);
        cb.setPreferredSize(new Dimension(200, 38));
        cb.setBorder(new LineBorder(Theme.BG_BORDER, 1, true));
        return cb;
    }

    private void submitEvent() {
        String name     = nameField.getText().trim();
        String location = locationField.getText().trim();
        String date     = dateField.getText().trim();

        if (name.isEmpty() || location.isEmpty() || date.isEmpty()) {
            JOptionPane.showMessageDialog(this, "⚠ Please fill all required (*) fields!", "Validation", JOptionPane.WARNING_MESSAGE);
            return;
        }

        boolean ok = dao.addEvent(name, location, date,
            stateBox.getSelectedItem().toString(),
            descArea.getText().trim(),
            typeBox.getSelectedItem().toString(),
            categoryField.getText().trim(),
            organizerField.getText().trim());

        if (ok) {
            JOptionPane.showMessageDialog(this, "✅ Event \"" + name + "\" added!", "Success", JOptionPane.INFORMATION_MESSAGE);
            clearForm();
            mainFrame.refreshViewTab();
        } else {
            JOptionPane.showMessageDialog(this, "❌ Failed. Check DB connection.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearForm() {
        nameField.setText(""); locationField.setText("");
        dateField.setText(""); categoryField.setText("");
        organizerField.setText(""); descArea.setText("");
        stateBox.setSelectedIndex(0); typeBox.setSelectedIndex(0);
    }
}

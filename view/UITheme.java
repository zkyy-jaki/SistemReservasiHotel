package view;

import java.awt.*;
import java.io.File;
import java.net.URL;
import java.text.NumberFormat;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

final class UITheme {

    static final Color BACKGROUND = new Color(246, 244, 239);
    static final Color SURFACE = Color.WHITE;
    static final Color SIDEBAR = new Color(36, 37, 40);
    static final Color SIDEBAR_ACTIVE = new Color(121, 94, 47);
    static final Color SIDEBAR_HOVER = new Color(72, 62, 47);
    static final Color PRIMARY = new Color(163, 127, 62);
    static final Color PRIMARY_HOVER = new Color(136, 103, 48);
    static final Color TEXT = new Color(34, 40, 49);
    static final Color MUTED = new Color(105, 116, 128);
    static final Color BORDER = new Color(222, 216, 204);
    static final Color ROW_ALT = new Color(250, 248, 244);
    static final Color SUCCESS = new Color(46, 125, 90);
    static final Color DANGER = new Color(181, 73, 73);
    static final Color WARNING = new Color(176, 128, 50);
    static final Color NEUTRAL = new Color(112, 122, 132);

    private static final String FONT_FAMILY = "Segoe UI";

    private UITheme() {
    }

    static Font font(int style, int size) {
        return new Font(FONT_FAMILY, style, size);
    }

    static JLabel title(String text) {
        JLabel label = new JLabel(text);
        label.setFont(font(Font.BOLD, 24));
        label.setForeground(TEXT);
        return label;
    }

    static JLabel subtitle(String text) {
        JLabel label = new JLabel(text);
        label.setFont(font(Font.PLAIN, 12));
        label.setForeground(MUTED);
        return label;
    }

    static JLabel fieldLabel(String text) {
        JLabel label = new JLabel(text);
        label.setFont(font(Font.BOLD, 12));
        label.setForeground(new Color(67, 76, 86));
        return label;
    }

    static JLabel logoLabel(int width, int height) {
        JLabel label = new JLabel();
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setPreferredSize(new Dimension(width, height));
        label.setMaximumSize(new Dimension(width, height));

        ImageIcon icon = loadLogoIcon(width, height);

        if (icon == null) {
            label.setText("HH");
            label.setFont(font(Font.BOLD, Math.max(16, width / 3)));
            label.setForeground(Color.WHITE);
            label.setOpaque(true);
            label.setBackground(PRIMARY);
        } else {
            label.setIcon(icon);
        }

        return label;
    }

    private static ImageIcon loadLogoIcon(int width, int height) {
        URL resource = UITheme.class.getResource("/assets/logo.png");
        ImageIcon source = null;

        if (resource != null) {
            source = new ImageIcon(resource);
        } else {
            File[] candidates = {
                new File("src/assets/logo.png"),
                new File("assets/logo.png"),
                new File("build/classes/assets/logo.png")
            };

            for (File candidate : candidates) {
                if (candidate.exists()) {
                    source = new ImageIcon(candidate.getPath());
                    break;
                }
            }
        }

        if (source == null || source.getIconWidth() <= 0) {
            return null;
        }

        Image scaled = source.getImage()
                .getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaled);
    }

    static JTextField textField() {
        JTextField field = new JTextField();
        styleTextField(field);
        return field;
    }

    static JPasswordField passwordField() {
        JPasswordField field = new JPasswordField();
        styleTextField(field);
        return field;
    }

    static void styleTextField(JTextField field) {
        field.setFont(font(Font.PLAIN, 13));
        field.setForeground(TEXT);
        field.setBackground(Color.WHITE);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(BORDER),
                new EmptyBorder(8, 10, 8, 10)));
        field.setMinimumSize(new Dimension(160, 38));
        field.setPreferredSize(new Dimension(180, 38));
    }

    static JComboBox<String> comboBox() {
        JComboBox<String> comboBox = new JComboBox<>();
        comboBox.setFont(font(Font.PLAIN, 13));
        comboBox.setForeground(TEXT);
        comboBox.setBackground(Color.WHITE);
        comboBox.setBorder(BorderFactory.createLineBorder(BORDER));
        comboBox.setPreferredSize(new Dimension(180, 38));
        return comboBox;
    }

    static JButton primaryButton(String text) {
        return button(text, PRIMARY, PRIMARY_HOVER, 118);
    }

    static JButton successButton(String text) {
        return button(text, SUCCESS, new Color(35, 105, 74), 118);
    }

    static JButton dangerButton(String text) {
        return button(text, DANGER, new Color(150, 56, 56), 118);
    }

    static JButton neutralButton(String text) {
        return button(text, NEUTRAL, new Color(90, 98, 108), 118);
    }

    static JButton backButton(String text) {
        JButton button = button(text, SURFACE, new Color(248, 244, 235), 104);
        button.setForeground(PRIMARY);
        button.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(201, 178, 133)),
                new EmptyBorder(8, 14, 8, 14)));
        return button;
    }

    static JButton button(String text, Color color, Color hover, int width) {
        JButton button = new JButton(text);
        button.setFont(font(Font.BOLD, 12));
        button.setForeground(Color.WHITE);
        button.setBackground(color);
        button.setBorder(new EmptyBorder(9, 14, 9, 14));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(width, 36));
        button.setMargin(new Insets(0, 0, 0, 0));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(hover);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(color);
            }
        });
        return button;
    }

    static JButton menuButton(String text) {
        return menuButton(text, SIDEBAR);
    }

    static JButton menuButton(String text, Color baseColor) {
        JButton button = new JButton(text);
        button.setMaximumSize(new Dimension(Integer.MAX_VALUE, 42));
        button.setBackground(baseColor);
        button.setForeground(new Color(234, 228, 216));
        button.setFont(font(Font.PLAIN, 13));
        button.setBorder(new EmptyBorder(10, 18, 10, 18));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent e) {
                button.setBackground(SIDEBAR_HOVER);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent e) {
                button.setBackground(baseColor);
            }
        });
        return button;
    }

    static JPanel card() {
        RoundedPanel panel = new RoundedPanel(8);
        panel.setBackground(SURFACE);
        panel.setLayout(new java.awt.BorderLayout());
        panel.setBorder(new EmptyBorder(18, 18, 18, 18));
        return panel;
    }

    static void styleTable(JTable table) {
        table.setFont(font(Font.PLAIN, 12));
        table.setForeground(TEXT);
        table.setRowHeight(32);
        table.setShowVerticalLines(false);
        table.setGridColor(new Color(232, 226, 214));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setSelectionBackground(new Color(240, 229, 205));
        table.setSelectionForeground(TEXT);
        table.getTableHeader().setFont(font(Font.BOLD, 12));
        table.getTableHeader().setBackground(new Color(239, 234, 224));
        table.getTableHeader().setForeground(new Color(52, 61, 71));
        table.getTableHeader().setBorder(BorderFactory.createLineBorder(BORDER));
        table.setDefaultRenderer(Object.class, new ZebraRenderer());
    }

    static JScrollPane tableScroll(JTable table) {
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(BORDER));
        scrollPane.getViewport().setBackground(Color.WHITE);
        return scrollPane;
    }

    static String money(double value) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        formatter.setMaximumFractionDigits(0);
        return formatter.format(value);
    }

    static DefaultTableCellRenderer moneyRenderer() {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table,
                                                           Object value,
                                                           boolean isSelected,
                                                           boolean hasFocus,
                                                           int row,
                                                           int column) {
                Object displayValue = value;

                if (value instanceof Number) {
                    displayValue = money(((Number) value).doubleValue());
                }

                Component component = super.getTableCellRendererComponent(
                        table, displayValue, isSelected, hasFocus, row, column);
                setHorizontalAlignment(SwingConstants.RIGHT);
                setBorder(new EmptyBorder(0, 10, 0, 10));

                if (!isSelected) {
                    component.setBackground(row % 2 == 0 ? Color.WHITE : ROW_ALT);
                    component.setForeground(TEXT);
                } else {
                    component.setForeground(TEXT);
                }

                return component;
            }
        };
    }

    static DefaultTableCellRenderer statusRenderer() {
        return new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table,
                                                           Object value,
                                                           boolean isSelected,
                                                           boolean hasFocus,
                                                           int row,
                                                           int column) {
                Component component = super.getTableCellRendererComponent(
                        table, value, isSelected, hasFocus, row, column);
                String status = value == null ? "" : value.toString();
                setHorizontalAlignment(SwingConstants.CENTER);
                setFont(font(Font.BOLD, 11));
                setBorder(new EmptyBorder(0, 10, 0, 10));

                if (!isSelected) {
                    if ("Available".equalsIgnoreCase(status)) {
                        component.setBackground(new Color(226, 244, 235));
                        component.setForeground(SUCCESS);
                    } else if ("Booked".equalsIgnoreCase(status)) {
                        component.setBackground(new Color(250, 231, 231));
                        component.setForeground(DANGER);
                    } else {
                        component.setBackground(new Color(244, 236, 222));
                        component.setForeground(WARNING);
                    }
                } else {
                    component.setForeground(TEXT);
                }

                return component;
            }
        };
    }

    static class RoundedPanel extends JPanel {
        private final int radius;

        RoundedPanel(int radius) {
            this.radius = radius;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(getBackground());
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), radius, radius);
            g2.dispose();
            super.paintComponent(g);
        }

        @Override
        protected void paintBorder(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(BORDER);
            g2.setStroke(new BasicStroke(1));
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, radius, radius);
            g2.dispose();
        }
    }

    private static class ZebraRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table,
                                                       Object value,
                                                       boolean isSelected,
                                                       boolean hasFocus,
                                                       int row,
                                                       int column) {
            Component component = super.getTableCellRendererComponent(
                    table, value, isSelected, hasFocus, row, column);
            setBorder(new EmptyBorder(0, 10, 0, 10));

            if (!isSelected) {
                component.setBackground(row % 2 == 0 ? Color.WHITE : ROW_ALT);
                component.setForeground(TEXT);
            } else {
                component.setForeground(TEXT);
            }

            return component;
        }
    }
}

package view;

import controller.ReservationController;
import controller.RoomController;
import java.awt.*;
import java.awt.event.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.swing.*;
import javax.swing.border.*;

public class DashboardView extends JFrame {

    private JLabel totalRoomsLabel;
    private JLabel bookedRoomsLabel;
    private JLabel availableRoomsLabel;
    private JLabel totalReservationsLabel;
    private JButton dashboardBtn;
    private JButton reservationBtn;
    private JButton roomBtn;
    private JButton logoutBtn;
    private final RoomController roomController;
    private final ReservationController reservationController;

    public DashboardView() {
        roomController = new RoomController();
        reservationController = new ReservationController();
        initComponents();
        refreshStatistics();
        setupUI();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1180, 700);
        setMinimumSize(new Dimension(1000, 640));
        setLocationRelativeTo(null);
        setTitle("Horizon Hotel - Dashboard");
        setResizable(true);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UITheme.BACKGROUND);

        mainPanel.add(createSidebar(), BorderLayout.WEST);
        mainPanel.add(createContent(), BorderLayout.CENTER);
        add(mainPanel);
    }

    private JPanel createSidebar() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(UITheme.SIDEBAR);
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setPreferredSize(new Dimension(242, 700));
        sidebar.setBorder(new EmptyBorder(24, 16, 18, 16));

        JLabel logoLabel = UITheme.logoLabel(62, 62);

        JLabel brandLabel = new JLabel("Horizon Hotel");
        brandLabel.setFont(UITheme.font(Font.BOLD, 16));
        brandLabel.setForeground(Color.WHITE);
        brandLabel.setAlignmentX(LEFT_ALIGNMENT);

        JLabel sectionLabel = new JLabel("Admin panel");
        sectionLabel.setFont(UITheme.font(Font.PLAIN, 12));
        sectionLabel.setForeground(new Color(207, 197, 178));
        sectionLabel.setAlignmentX(LEFT_ALIGNMENT);

        JPanel brand = new JPanel();
        brand.setOpaque(false);
        brand.setLayout(new BoxLayout(brand, BoxLayout.Y_AXIS));
        brand.add(logoLabel);
        brand.add(Box.createVerticalStrut(16));
        brand.add(brandLabel);
        brand.add(Box.createVerticalStrut(4));
        brand.add(sectionLabel);

        JLabel menuLabel = new JLabel("MENU");
        menuLabel.setFont(UITheme.font(Font.BOLD, 11));
        menuLabel.setForeground(new Color(172, 160, 139));
        menuLabel.setBorder(new EmptyBorder(22, 2, 8, 0));
        menuLabel.setAlignmentX(LEFT_ALIGNMENT);

        dashboardBtn = UITheme.menuButton("Dashboard", UITheme.SIDEBAR_ACTIVE);
        reservationBtn = UITheme.menuButton("Reservasi");
        roomBtn = UITheme.menuButton("Kamar");
        logoutBtn = UITheme.menuButton("Keluar", new Color(105, 57, 57));

        sidebar.add(brand);
        sidebar.add(menuLabel);
        sidebar.add(dashboardBtn);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(reservationBtn);
        sidebar.add(Box.createVerticalStrut(8));
        sidebar.add(roomBtn);
        sidebar.add(Box.createVerticalGlue());
        sidebar.add(logoutBtn);

        return sidebar;
    }

    private JPanel createContent() {
        JPanel content = new JPanel(new BorderLayout(0, 22));
        content.setBackground(UITheme.BACKGROUND);
        content.setBorder(new EmptyBorder(30, 34, 30, 34));

        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel headerText = new JPanel();
        headerText.setOpaque(false);
        headerText.setLayout(new BoxLayout(headerText, BoxLayout.Y_AXIS));

        JLabel title = UITheme.title("Ringkasan Operasional");
        JLabel subtitle = UITheme.subtitle("Data kamar dan reservasi tersinkron dengan database hotel.");
        headerText.add(title);
        headerText.add(Box.createVerticalStrut(6));
        headerText.add(subtitle);

        String today = new SimpleDateFormat("EEEE, dd MMMM yyyy",
                new Locale("id", "ID")).format(new Date());
        JLabel dateLabel = UITheme.subtitle("Hari ini: " + today);
        dateLabel.setHorizontalAlignment(SwingConstants.RIGHT);

        header.add(headerText, BorderLayout.WEST);
        header.add(dateLabel, BorderLayout.EAST);

        JPanel statsPanel = new JPanel(new GridLayout(2, 2, 16, 16));
        statsPanel.setOpaque(false);

        JPanel totalRoomsCard = createStatCard("Total kamar", "0", UITheme.PRIMARY);
        JPanel bookedRoomsCard = createStatCard("Kamar booked", "0", UITheme.DANGER);
        JPanel availableRoomsCard = createStatCard("Kamar tersedia", "0", UITheme.SUCCESS);
        JPanel totalReservationsCard = createStatCard("Total reservasi", "0", UITheme.WARNING);

        totalRoomsLabel = getStatValueLabel(totalRoomsCard);
        bookedRoomsLabel = getStatValueLabel(bookedRoomsCard);
        availableRoomsLabel = getStatValueLabel(availableRoomsCard);
        totalReservationsLabel = getStatValueLabel(totalReservationsCard);

        statsPanel.add(totalRoomsCard);
        statsPanel.add(bookedRoomsCard);
        statsPanel.add(availableRoomsCard);
        statsPanel.add(totalReservationsCard);

        JPanel body = new JPanel(new BorderLayout());
        body.setOpaque(false);
        body.add(statsPanel, BorderLayout.NORTH);
        body.add(createQuickNote(), BorderLayout.CENTER);

        content.add(header, BorderLayout.NORTH);
        content.add(body, BorderLayout.CENTER);
        return content;
    }

    private JPanel createQuickNote() {
        JPanel panel = UITheme.card();
        panel.setBorder(new EmptyBorder(18, 20, 18, 20));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Alur kerja");
        title.setFont(UITheme.font(Font.BOLD, 14));
        title.setForeground(UITheme.TEXT);

        JLabel detail = UITheme.subtitle("Kelola kamar terlebih dahulu, lalu buat reservasi hanya dari kamar yang tersedia.");

        text.add(title);
        text.add(Box.createVerticalStrut(6));
        text.add(detail);
        panel.add(text, BorderLayout.CENTER);
        panel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 100));
        return panel;
    }

    private JPanel createStatCard(String title, String value, Color accentColor) {
        JPanel card = UITheme.card();
        card.setBorder(new EmptyBorder(18, 20, 18, 20));

        JPanel marker = new JPanel();
        marker.setBackground(accentColor);
        marker.setPreferredSize(new Dimension(5, 70));

        JPanel content = new JPanel();
        content.setOpaque(false);
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));
        content.setBorder(new EmptyBorder(0, 14, 0, 0));

        JLabel titleLabel = UITheme.subtitle(title);
        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(UITheme.font(Font.BOLD, 32));
        valueLabel.setForeground(UITheme.TEXT);

        content.add(titleLabel);
        content.add(Box.createVerticalStrut(8));
        content.add(valueLabel);

        card.add(marker, BorderLayout.WEST);
        card.add(content, BorderLayout.CENTER);
        card.putClientProperty("valueLabel", valueLabel);
        return card;
    }

    private JLabel getStatValueLabel(JPanel card) {
        return (JLabel) card.getClientProperty("valueLabel");
    }

    private void setupUI() {
        setVisible(true);
    }

    public void setTotalRooms(int value) {
        totalRoomsLabel.setText(String.valueOf(value));
    }

    public void setBookedRooms(int value) {
        bookedRoomsLabel.setText(String.valueOf(value));
    }

    public void setAvailableRooms(int value) {
        availableRoomsLabel.setText(String.valueOf(value));
    }

    public void setTotalReservations(int value) {
        totalReservationsLabel.setText(String.valueOf(value));
    }

    public final void refreshStatistics() {
        RefreshStatisticsThread statsThread = new RefreshStatisticsThread(
                roomController,
                reservationController,
                (stats) -> {
                    setTotalRooms(stats[0]);
                    setBookedRooms(stats[1]);
                    setAvailableRooms(stats[2]);
                    setTotalReservations(stats[3]);
                },
                (errorMsg) -> {
                    System.err.println(errorMsg);
                    showMessage("Gagal memuat statistik: " + errorMsg);
                }
        );
        statsThread.start();
    }

    public void setDashboardListener(ActionListener listener) {
        dashboardBtn.addActionListener(listener);
    }

    public void setReservationListener(ActionListener listener) {
        reservationBtn.addActionListener(listener);
    }

    public void setRoomListener(ActionListener listener) {
        roomBtn.addActionListener(listener);
    }

    public void setLogoutListener(ActionListener listener) {
        logoutBtn.addActionListener(listener);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(DashboardView::new);
    }

    
    private static class RefreshStatisticsThread extends Thread {

        private final RoomController roomController;
        private final ReservationController reservationController;
        private final java.util.function.Consumer<int[]> onSuccess;
        private final java.util.function.Consumer<String> onError;

        public RefreshStatisticsThread(RoomController roomController,
                                       ReservationController reservationController,
                                       java.util.function.Consumer<int[]> onSuccess,
                                       java.util.function.Consumer<String> onError) {
            super("RefreshStatisticsThread");
            this.roomController = roomController;
            this.reservationController = reservationController;
            this.onSuccess = onSuccess;
            this.onError = onError;
        }

        @Override
        public void run() {
            try {
                int[] stats = new int[]{
                    roomController.countAllRooms(),
                    roomController.countRoomsByStatus("Booked"),
                    roomController.countRoomsByStatus("Available"),
                    reservationController.countAllReservations()
                };
                SwingUtilities.invokeLater(() -> onSuccess.accept(stats));
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> onError.accept(e.getMessage()));
            }
        }
    }
}

package view;

import controller.LoginController;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public class LoginView extends JFrame {

    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public LoginView() {
        initComponents();
        setupUI();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(940, 560);
        setMinimumSize(new Dimension(860, 520));
        setLocationRelativeTo(null);
        setTitle("Horizon Hotel - Login");
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(UITheme.BACKGROUND);
        mainPanel.setBorder(new EmptyBorder(18, 18, 18, 18));

        JPanel brandPanel = createBrandPanel();
        JPanel formPanel = createFormPanel();

        mainPanel.add(brandPanel, BorderLayout.WEST);
        mainPanel.add(formPanel, BorderLayout.CENTER);
        add(mainPanel);
    }

    private JPanel createBrandPanel() {
        UITheme.RoundedPanel panel = new UITheme.RoundedPanel(8);
        panel.setPreferredSize(new Dimension(360, 0));
        panel.setBackground(UITheme.SIDEBAR);
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(34, 32, 34, 32));

        JPanel top = new JPanel();
        top.setOpaque(false);
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));

        JLabel logoLabel = UITheme.logoLabel(124, 124);
        logoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel productLabel = new JLabel("Horizon Hotel");
        productLabel.setFont(UITheme.font(Font.BOLD, 25));
        productLabel.setForeground(Color.WHITE);
        productLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel moduleLabel = new JLabel("Front office dan admin panel");
        moduleLabel.setFont(UITheme.font(Font.PLAIN, 13));
        moduleLabel.setForeground(new Color(214, 204, 184));
        moduleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        top.add(logoLabel);
        top.add(Box.createVerticalStrut(28));
        top.add(productLabel);
        top.add(Box.createVerticalStrut(8));
        top.add(moduleLabel);

        JPanel bottom = new JPanel();
        bottom.setOpaque(false);
        bottom.setLayout(new BoxLayout(bottom, BoxLayout.Y_AXIS));

        JLabel noteTitle = new JLabel("Akses internal hotel");
        noteTitle.setFont(UITheme.font(Font.BOLD, 13));
        noteTitle.setForeground(Color.WHITE);
        noteTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel note = new JLabel("<html>Gunakan akun admin atau staff untuk mengelola kamar dan reservasi tamu.</html>");
        note.setFont(UITheme.font(Font.PLAIN, 12));
        note.setForeground(new Color(214, 204, 184));
        note.setAlignmentX(Component.LEFT_ALIGNMENT);

        bottom.add(noteTitle);
        bottom.add(Box.createVerticalStrut(8));
        bottom.add(note);

        panel.add(top, BorderLayout.NORTH);
        panel.add(bottom, BorderLayout.SOUTH);
        return panel;
    }

    private JPanel createFormPanel() {
        JPanel wrapper = new JPanel(new GridBagLayout());
        wrapper.setOpaque(false);

        JPanel form = new JPanel();
        form.setOpaque(false);
        form.setLayout(new BoxLayout(form, BoxLayout.Y_AXIS));
        form.setBorder(new EmptyBorder(22, 58, 22, 42));

        JLabel titleLabel = UITheme.title("Masuk");
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel infoLabel = UITheme.subtitle("Kelola data kamar dan reservasi dari satu tempat.");
        infoLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel usernameLabel = UITheme.fieldLabel("Username");
        usernameLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        usernameField = UITheme.textField();
        usernameField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        usernameField.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel passwordLabel = UITheme.fieldLabel("Password");
        passwordLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        passwordField = UITheme.passwordField();
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        passwordField.setAlignmentX(Component.LEFT_ALIGNMENT);

        loginButton = UITheme.primaryButton("Masuk");
        loginButton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        loginButton.setAlignmentX(Component.LEFT_ALIGNMENT);
        loginButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        JLabel hintLabel = UITheme.subtitle("Role yang bisa masuk: ADMIN dan STAFF.");
        hintLabel.setAlignmentX(Component.LEFT_ALIGNMENT);

        form.add(titleLabel);
        form.add(Box.createVerticalStrut(6));
        form.add(infoLabel);
        form.add(Box.createVerticalStrut(32));
        form.add(usernameLabel);
        form.add(Box.createVerticalStrut(8));
        form.add(usernameField);
        form.add(Box.createVerticalStrut(16));
        form.add(passwordLabel);
        form.add(Box.createVerticalStrut(8));
        form.add(passwordField);
        form.add(Box.createVerticalStrut(24));
        form.add(loginButton);
        form.add(Box.createVerticalStrut(16));
        form.add(hintLabel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;
        gbc.insets = new Insets(0, 0, 0, 0);
        wrapper.add(form, gbc);

        getRootPane().setDefaultButton(loginButton);
        return wrapper;
    }

    private void setupUI() {
        setVisible(true);
    }

    public String getUsername() {
        return usernameField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }

    public void clearPassword() {
        passwordField.setText("");
    }

    public void setLoginButtonListener(ActionListener listener) {
        loginButton.addActionListener(listener);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public void showSuccess(String message) {
        JOptionPane.showMessageDialog(this, message, "Success", JOptionPane.INFORMATION_MESSAGE);
    }

    private static void configureLogin(LoginView loginView,
                                       LoginController loginController) {

        loginView.setLoginButtonListener(e -> {
            loginView.loginButton.setEnabled(false);
            loginView.loginButton.setText("Memeriksa...");

            LoginThread loginThread = new LoginThread(
                    loginController,
                    loginView.getUsername(),
                    loginView.getPassword(),
                    (success) -> {
                        if (success) {
                            loginView.dispose();
                            openDashboard(loginController);
                            return;
                        }

                        loginView.showError("Username atau password salah.");
                        loginView.clearPassword();
                        loginView.loginButton.setEnabled(true);
                        loginView.loginButton.setText("Masuk");
                    },
                    (errorMsg) -> {
                        loginView.showError("Terjadi kesalahan: " + errorMsg);
                        loginView.loginButton.setEnabled(true);
                        loginView.loginButton.setText("Masuk");
                    }
            );
            loginThread.start();
        });
    }

    private static void openDashboard(LoginController loginController) {

        DashboardView dashboardView = new DashboardView();

        dashboardView.setDashboardListener(e -> dashboardView.refreshStatistics());

        dashboardView.setReservationListener(e -> {
            ReservationView reservationView = new ReservationView();
            reservationView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    dashboardView.refreshStatistics();
                }
            });
        });

        dashboardView.setRoomListener(e -> {
            RoomView roomView = new RoomView();
            roomView.addWindowListener(new WindowAdapter() {
                @Override
                public void windowClosed(WindowEvent e) {
                    dashboardView.refreshStatistics();
                }
            });
        });

        dashboardView.setLogoutListener(e -> {
            dashboardView.dispose();

            LoginView loginView = new LoginView();
            configureLogin(loginView, loginController);
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            LoginController loginController = new LoginController();
            LoginView loginView = new LoginView();

            configureLogin(loginView, loginController);
        });
    }

  
    private static class LoginThread extends Thread {

        private final LoginController loginController;
        private final String username;
        private final String password;
        private final java.util.function.Consumer<Boolean> onResult;
        private final java.util.function.Consumer<String> onError;

        public LoginThread(LoginController loginController,
                           String username,
                           String password,
                           java.util.function.Consumer<Boolean> onResult,
                           java.util.function.Consumer<String> onError) {
            super("LoginThread");
            this.loginController = loginController;
            this.username = username;
            this.password = password;
            this.onResult = onResult;
            this.onError = onError;
        }

        @Override
        public void run() {
            try {
                boolean success = loginController.login(username, password);
                SwingUtilities.invokeLater(() -> onResult.accept(success));
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> onError.accept(e.getMessage()));
            }
        }
    }
}

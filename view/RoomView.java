package view;

import controller.RoomController;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import model.Room;

public class RoomView extends JFrame {

    private JTextField roomNumberField;
    private JComboBox<String> roomTypeComboBox;
    private JTextField priceField;
    private JComboBox<String> availabilityComboBox;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;
    private JButton backButton;
    private JTable roomTable;
    private DefaultTableModel tableModel;
    private JLabel totalRoomsLabel;
    private JLabel availableRoomsLabel;
    private final RoomController roomController;

    public RoomView() {
        roomController = new RoomController();
        initComponents();
        configureController();
        loadRoomsFromDatabase();
        setupUI();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1120, 720);
        setMinimumSize(new Dimension(960, 640));
        setLocationRelativeTo(null);
        setTitle("Horizon Hotel - Kamar");
        setResizable(true);

        JPanel mainPanel = new JPanel(new BorderLayout(0, 18));
        mainPanel.setBackground(UITheme.BACKGROUND);
        mainPanel.setBorder(new EmptyBorder(24, 26, 24, 26));

        mainPanel.add(createHeader(), BorderLayout.NORTH);
        mainPanel.add(createBody(), BorderLayout.CENTER);
        add(mainPanel);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);

        JPanel titleGroup = new JPanel();
        titleGroup.setOpaque(false);
        titleGroup.setLayout(new BoxLayout(titleGroup, BoxLayout.Y_AXIS));

        JLabel title = UITheme.title("Kelola Kamar");
        JLabel subtitle = UITheme.subtitle("Tambah kamar, ubah tarif, dan pantau status ketersediaan.");

        titleGroup.add(title);
        titleGroup.add(Box.createVerticalStrut(6));
        titleGroup.add(subtitle);

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        rightPanel.setOpaque(false);
        backButton = UITheme.backButton("Kembali");
        rightPanel.add(backButton);

        JPanel stats = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        stats.setOpaque(false);
        totalRoomsLabel = createSmallStat(stats, "Total", "0");
        availableRoomsLabel = createSmallStat(stats, "Tersedia", "0");
        rightPanel.add(stats);

        header.add(titleGroup, BorderLayout.WEST);
        header.add(rightPanel, BorderLayout.EAST);
        return header;
    }

    private JLabel createSmallStat(JPanel parent, String label, String value) {
        JPanel stat = UITheme.card();
        stat.setPreferredSize(new Dimension(130, 58));
        stat.setBorder(new EmptyBorder(10, 12, 10, 12));

        JPanel text = new JPanel();
        text.setOpaque(false);
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));

        JLabel labelText = UITheme.subtitle(label);
        JLabel valueText = new JLabel(value);
        valueText.setFont(UITheme.font(Font.BOLD, 18));
        valueText.setForeground(UITheme.TEXT);

        text.add(labelText);
        text.add(valueText);
        stat.add(text, BorderLayout.CENTER);
        parent.add(stat);
        return valueText;
    }

    private JPanel createBody() {
        JPanel body = new JPanel();
        body.setOpaque(false);
        body.setLayout(new BoxLayout(body, BoxLayout.Y_AXIS));

        body.add(createFormPanel());
        body.add(Box.createVerticalStrut(12));
        body.add(createButtonPanel());
        body.add(Box.createVerticalStrut(12));
        body.add(createTablePanel());
        return body;
    }

    private JPanel createFormPanel() {
        JPanel form = UITheme.card();
        form.setLayout(new GridBagLayout());
        form.setMaximumSize(new Dimension(Integer.MAX_VALUE, 142));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(7, 8, 7, 8);
        gbc.weightx = 0.25;

        roomNumberField = UITheme.textField();
        roomTypeComboBox = UITheme.comboBox();
        roomTypeComboBox.setModel(new DefaultComboBoxModel<>(new String[]{
            "Standard Room",
            "Deluxe Room",
            "Suite Room"
        }));
        priceField = UITheme.textField();
        availabilityComboBox = UITheme.comboBox();
        availabilityComboBox.setModel(new DefaultComboBoxModel<>(new String[]{
            "Available",
            "Booked",
            "Occupied"
        }));

        addField(form, gbc, 0, 0, "No. kamar", roomNumberField);
        addField(form, gbc, 2, 0, "Tipe kamar", roomTypeComboBox);
        addField(form, gbc, 0, 1, "Harga per malam", priceField);
        addField(form, gbc, 2, 1, "Status", availabilityComboBox);

        return form;
    }

    private void addField(JPanel panel,
                          GridBagConstraints gbc,
                          int labelX,
                          int row,
                          String label,
                          java.awt.Component field) {
        gbc.gridx = labelX;
        gbc.gridy = row;
        gbc.weightx = 0.08;
        panel.add(UITheme.fieldLabel(label), gbc);

        gbc.gridx = labelX + 1;
        gbc.weightx = 0.42;
        panel.add(field, gbc);
    }

    private JPanel createButtonPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 0));
        panel.setOpaque(false);

        addButton = UITheme.successButton("Tambah");
        updateButton = UITheme.primaryButton("Simpan");
        deleteButton = UITheme.dangerButton("Hapus");
        clearButton = UITheme.neutralButton("Reset");

        panel.add(addButton);
        panel.add(updateButton);
        panel.add(deleteButton);
        panel.add(clearButton);
        return panel;
    }

    private JScrollPane createTablePanel() {
        String[] columnNames = {"No. Kamar", "Tipe", "Harga/malam", "Status"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        roomTable = new JTable(tableModel);
        UITheme.styleTable(roomTable);
        roomTable.getColumnModel().getColumn(0).setPreferredWidth(110);
        roomTable.getColumnModel().getColumn(1).setPreferredWidth(160);
        roomTable.getColumnModel().getColumn(2).setPreferredWidth(140);
        roomTable.getColumnModel().getColumn(3).setPreferredWidth(110);
        roomTable.getColumnModel().getColumn(2).setCellRenderer(UITheme.moneyRenderer());
        roomTable.getColumnModel().getColumn(3).setCellRenderer(UITheme.statusRenderer());

        return UITheme.tableScroll(roomTable);
    }

    private void setupUI() {
        setVisible(true);
    }

    private void configureController() {
        setAddButtonListener(e -> addRoom());
        setUpdateButtonListener(e -> updateRoom());
        setDeleteButtonListener(e -> deleteRoom());
        setClearButtonListener(e -> clearForm());
        setBackButtonListener(e -> dispose());

        roomTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillFormFromSelectedRow();
            }
        });
    }

    private void loadRoomsFromDatabase() {
        roomTable.setEnabled(false);
        LoadRoomsThread loadThread = new LoadRoomsThread(
                roomController,
                null,
                (rooms) -> {
                    clearTable();
                    for (Room room : rooms) {
                        addRowToTable(new Object[]{
                            room.getRoomNumber(),
                            room.getRoomType(),
                            room.getPrice(),
                            room.getStatus()
                        });
                    }
                },
                (errorMsg) -> showError("Gagal memuat data kamar: " + errorMsg),
                () -> roomTable.setEnabled(true)
        );
        loadThread.start();
    }

    private Room buildRoomFromForm() {
        String roomNumber = getRoomNumber().trim();
        String priceText = getPrice().trim();

        if (roomNumber.isEmpty()) {
            throw new IllegalArgumentException("Nomor kamar wajib diisi.");
        }

        if (priceText.isEmpty()) {
            throw new IllegalArgumentException("Harga kamar wajib diisi.");
        }

        double price;

        try {
            price = Double.parseDouble(priceText);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Harga kamar harus berupa angka.");
        }

        Room room = new Room();
        room.setRoomNumber(roomNumber);
        room.setRoomType(getRoomType());
        room.setPrice(price);
        room.setStatus(getAvailability());

        return room;
    }

    private void addRoom() {
        try {
            Room room = buildRoomFromForm();
            addButton.setEnabled(false);

            AddRoomThread addThread = new AddRoomThread(
                    roomController,
                    room,
                    (success) -> {
                        if (success) {
                            showMessage("Kamar berhasil ditambahkan.");
                            loadRoomsFromDatabase();
                            clearForm();
                        } else {
                            showError("Kamar gagal ditambahkan. Periksa nomor kamar atau koneksi database.");
                        }
                    },
                    (errorMsg) -> showError("Terjadi kesalahan: " + errorMsg),
                    () -> addButton.setEnabled(true)
            );
            addThread.start();
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    private void updateRoom() {
        int selectedRow = getSelectedRow();

        if (selectedRow < 0) {
            showError("Pilih kamar yang ingin diupdate.");
            return;
        }

        try {
            String originalRoomNumber = String.valueOf(
                    tableModel.getValueAt(selectedRow, 0));
            Room room = buildRoomFromForm();
            updateButton.setEnabled(false);

            UpdateRoomThread updateThread = new UpdateRoomThread(
                    roomController,
                    originalRoomNumber,
                    room,
                    (success) -> {
                        if (success) {
                            showMessage("Kamar berhasil diupdate.");
                            loadRoomsFromDatabase();
                            clearForm();
                        } else {
                            showError("Kamar gagal diupdate. Kamar yang sudah punya reservasi tidak bisa sembarang diubah.");
                        }
                    },
                    (errorMsg) -> showError("Terjadi kesalahan: " + errorMsg),
                    () -> updateButton.setEnabled(true)
            );
            updateThread.start();
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    private void deleteRoom() {
        int selectedRow = getSelectedRow();

        if (selectedRow < 0) {
            showError("Pilih kamar yang ingin dihapus.");
            return;
        }

        String roomNumber = String.valueOf(tableModel.getValueAt(selectedRow, 0));

        if (showConfirmDialog("Hapus kamar " + roomNumber + "?")
                != JOptionPane.YES_OPTION) {
            return;
        }

        deleteButton.setEnabled(false);
        DeleteRoomThread deleteThread = new DeleteRoomThread(
                roomController,
                roomNumber,
                (success) -> {
                    if (success) {
                        showMessage("Kamar berhasil dihapus.");
                        loadRoomsFromDatabase();
                        clearForm();
                    } else {
                        showError("Kamar gagal dihapus. Kamar yang sudah punya reservasi masih terikat data reservasi.");
                    }
                },
                (errorMsg) -> showError("Terjadi kesalahan: " + errorMsg),
                () -> deleteButton.setEnabled(true)
        );
        deleteThread.start();
    }

    private void fillFormFromSelectedRow() {
        int selectedRow = getSelectedRow();

        if (selectedRow < 0) {
            return;
        }

        setRoomNumber(String.valueOf(tableModel.getValueAt(selectedRow, 0)));
        setRoomType(String.valueOf(tableModel.getValueAt(selectedRow, 1)));
        setPrice(String.valueOf(tableModel.getValueAt(selectedRow, 2)));
        setAvailability(String.valueOf(tableModel.getValueAt(selectedRow, 3)));
    }

    public String getRoomNumber() {
        return roomNumberField.getText();
    }

    public String getRoomType() {
        return (String) roomTypeComboBox.getSelectedItem();
    }

    public String getPrice() {
        return priceField.getText();
    }

    public String getAvailability() {
        return (String) availabilityComboBox.getSelectedItem();
    }

    public void setRoomNumber(String number) {
        roomNumberField.setText(number);
    }

    public void setRoomType(String type) {
        roomTypeComboBox.setSelectedItem(type);
    }

    public void setPrice(String price) {
        priceField.setText(price);
    }

    public void setAvailability(String status) {
        availabilityComboBox.setSelectedItem(status);
    }

    public void clearForm() {
        roomNumberField.setText("");
        roomTypeComboBox.setSelectedIndex(0);
        priceField.setText("");
        availabilityComboBox.setSelectedIndex(0);
    }

    public void addRowToTable(Object[] rowData) {
        tableModel.addRow(rowData);
        updateStatistics();
    }

    public int getSelectedRow() {
        return roomTable.getSelectedRow();
    }

    public void clearTable() {
        tableModel.setRowCount(0);
        updateStatistics();
    }

    private void updateStatistics() {
        int total = tableModel.getRowCount();
        int available = 0;

        for (int i = 0; i < total; i++) {
            String status = (String) tableModel.getValueAt(i, 3);
            if ("Available".equals(status)) {
                available++;
            }
        }

        totalRoomsLabel.setText(String.valueOf(total));
        availableRoomsLabel.setText(String.valueOf(available));
    }

    public void setAddButtonListener(ActionListener listener) {
        addButton.addActionListener(listener);
    }

    public void setUpdateButtonListener(ActionListener listener) {
        updateButton.addActionListener(listener);
    }

    public void setDeleteButtonListener(ActionListener listener) {
        deleteButton.addActionListener(listener);
    }

    public void setClearButtonListener(ActionListener listener) {
        clearButton.addActionListener(listener);
    }

    public void setBackButtonListener(ActionListener listener) {
        backButton.addActionListener(listener);
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public int showConfirmDialog(String message) {
        return JOptionPane.showConfirmDialog(this, message, "Confirm", JOptionPane.YES_NO_OPTION);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(RoomView::new);
    }

    private static class LoadRoomsThread extends Thread {

        private final RoomController roomController;
        private final Runnable onBeforeStart;
        private final java.util.function.Consumer<List<Room>> onSuccess;
        private final java.util.function.Consumer<String> onError;
        private final Runnable onFinally;

        public LoadRoomsThread(RoomController roomController,
                               Runnable onBeforeStart,
                               java.util.function.Consumer<List<Room>> onSuccess,
                               java.util.function.Consumer<String> onError,
                               Runnable onFinally) {
            super("LoadRoomsThread");
            this.roomController = roomController;
            this.onBeforeStart = onBeforeStart;
            this.onSuccess = onSuccess;
            this.onError = onError;
            this.onFinally = onFinally;
        }

        @Override
        public void run() {
            if (onBeforeStart != null) {
                SwingUtilities.invokeLater(onBeforeStart);
            }

            try {
                List<Room> rooms = roomController.getAllRooms();
                SwingUtilities.invokeLater(() -> onSuccess.accept(rooms));
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> onError.accept(e.getMessage()));
            } finally {
                if (onFinally != null) {
                    SwingUtilities.invokeLater(onFinally);
                }
            }
        }
    }

    private static class AddRoomThread extends Thread {

        private final RoomController roomController;
        private final Room room;
        private final java.util.function.Consumer<Boolean> onResult;
        private final java.util.function.Consumer<String> onError;
        private final Runnable onFinally;

        public AddRoomThread(RoomController roomController,
                             Room room,
                             java.util.function.Consumer<Boolean> onResult,
                             java.util.function.Consumer<String> onError,
                             Runnable onFinally) {
            super("AddRoomThread");
            this.roomController = roomController;
            this.room = room;
            this.onResult = onResult;
            this.onError = onError;
            this.onFinally = onFinally;
        }

        @Override
        public void run() {
            try {
                boolean success = roomController.addRoom(room);
                SwingUtilities.invokeLater(() -> onResult.accept(success));
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> onError.accept(e.getMessage()));
            } finally {
                if (onFinally != null) {
                    SwingUtilities.invokeLater(onFinally);
                }
            }
        }
    }

    
    private static class UpdateRoomThread extends Thread {

        private final RoomController roomController;
        private final String originalRoomNumber;
        private final Room room;
        private final java.util.function.Consumer<Boolean> onResult;
        private final java.util.function.Consumer<String> onError;
        private final Runnable onFinally;

        public UpdateRoomThread(RoomController roomController,
                                String originalRoomNumber,
                                Room room,
                                java.util.function.Consumer<Boolean> onResult,
                                java.util.function.Consumer<String> onError,
                                Runnable onFinally) {
            super("UpdateRoomThread");
            this.roomController = roomController;
            this.originalRoomNumber = originalRoomNumber;
            this.room = room;
            this.onResult = onResult;
            this.onError = onError;
            this.onFinally = onFinally;
        }

        @Override
        public void run() {
            try {
                boolean success = roomController.updateRoom(originalRoomNumber, room);
                SwingUtilities.invokeLater(() -> onResult.accept(success));
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> onError.accept(e.getMessage()));
            } finally {
                if (onFinally != null) {
                    SwingUtilities.invokeLater(onFinally);
                }
            }
        }
    }

    private static class DeleteRoomThread extends Thread {
        private final RoomController roomController;
        private final String roomNumber;
        private final java.util.function.Consumer<Boolean> onResult;
        private final java.util.function.Consumer<String> onError;
        private final Runnable onFinally;
        public DeleteRoomThread(RoomController roomController,
                                String roomNumber,
                                java.util.function.Consumer<Boolean> onResult,
                                java.util.function.Consumer<String> onError,
                                Runnable onFinally) {
            super("DeleteRoomThread");
            this.roomController = roomController;
            this.roomNumber = roomNumber;
            this.onResult = onResult;
            this.onError = onError;
            this.onFinally = onFinally;
        }

        @Override
        public void run() {
            try {
                boolean success = roomController.deleteRoom(roomNumber);
                SwingUtilities.invokeLater(() -> onResult.accept(success));
            } catch (Exception e) {
                SwingUtilities.invokeLater(() -> onError.accept(e.getMessage()));
            } finally {
                if (onFinally != null) {
                    SwingUtilities.invokeLater(onFinally);
                }
            }
        }
    }
}

package view;

import controller.ReservationController;
import controller.RoomController;
import java.awt.*;
import java.awt.event.*;
import java.time.*;
import java.time.format.*;
import java.util.List;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.*;
import javax.swing.table.*;
import model.Customer;
import model.Reservation;
import model.Room;

public class ReservationView extends JFrame {

    private JTextField customerNameField;
    private JTextField phoneNumberField;
    private JComboBox<String> roomComboBox;
    private JTextField checkInField;
    private JTextField checkOutField;
    private JTextField totalPriceField;
    private JButton addButton;
    private JButton updateButton;
    private JButton deleteButton;
    private JButton clearButton;
    private JButton backButton;
    private JTable reservationTable;
    private JTextField searchField;
    private JButton searchButton;
    private DefaultTableModel tableModel;
    private final ReservationController reservationController;
    private final RoomController roomController;

    public ReservationView() {
        reservationController = new ReservationController();
        roomController = new RoomController();
        initComponents();
        configureController();
        loadRoomOptions();
        loadReservationsFromDatabase();
        setupUI();
    }

    private void initComponents() {
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1240, 760);
        setMinimumSize(new Dimension(1040, 680));
        setLocationRelativeTo(null);
        setTitle("Horizon Hotel - Reservasi");
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

        JLabel title = UITheme.title("Kelola Reservasi");
        JLabel subtitle = UITheme.subtitle("Reservasi hanya bisa dibuat untuk kamar yang masih tersedia.");

        titleGroup.add(title);
        titleGroup.add(Box.createVerticalStrut(6));
        titleGroup.add(subtitle);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        searchPanel.setOpaque(false);

        backButton = UITheme.backButton("Kembali");
        JLabel searchLabel = UITheme.fieldLabel("Cari");
        searchField = UITheme.textField();
        searchField.setPreferredSize(new Dimension(210, 38));
        searchButton = UITheme.primaryButton("Cari");
        searchButton.setPreferredSize(new Dimension(78, 38));

        searchPanel.add(backButton);
        searchPanel.add(searchLabel);
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        header.add(titleGroup, BorderLayout.WEST);
        header.add(searchPanel, BorderLayout.EAST);
        return header;
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
        form.setMaximumSize(new Dimension(Integer.MAX_VALUE, 170));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(7, 8, 7, 8);

        customerNameField = UITheme.textField();
        phoneNumberField = UITheme.textField();
        roomComboBox = UITheme.comboBox();
        roomComboBox.setModel(new DefaultComboBoxModel<>(new String[]{""}));
        checkInField = UITheme.textField();
        checkInField.setText("YYYY-MM-DD");
        checkOutField = UITheme.textField();
        checkOutField.setText("YYYY-MM-DD");
        totalPriceField = UITheme.textField();
        totalPriceField.setEditable(false);
        totalPriceField.setBackground(new java.awt.Color(248, 250, 252));

        addField(form, gbc, 0, 0, "Nama tamu", customerNameField);
        addField(form, gbc, 2, 0, "No. HP", phoneNumberField);
        addField(form, gbc, 0, 1, "Kamar tersedia", roomComboBox);
        addField(form, gbc, 2, 1, "Check-in", checkInField);
        addField(form, gbc, 0, 2, "Check-out", checkOutField);
        addField(form, gbc, 2, 2, "Total", totalPriceField);

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
        String[] columnNames = {
            "ID",
            "Customer ID",
            "Nama tamu",
            "No. HP",
            "Kamar",
            "Check-in",
            "Check-out",
            "Total"
        };
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        reservationTable = new JTable(tableModel);
        UITheme.styleTable(reservationTable);
        reservationTable.removeColumn(reservationTable.getColumnModel().getColumn(1));
        reservationTable.getColumnModel().getColumn(0).setPreferredWidth(58);
        reservationTable.getColumnModel().getColumn(1).setPreferredWidth(180);
        reservationTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        reservationTable.getColumnModel().getColumn(3).setPreferredWidth(110);
        reservationTable.getColumnModel().getColumn(4).setPreferredWidth(110);
        reservationTable.getColumnModel().getColumn(5).setPreferredWidth(110);
        reservationTable.getColumnModel().getColumn(6).setPreferredWidth(130);
        reservationTable.getColumnModel().getColumn(6).setCellRenderer(UITheme.moneyRenderer());

        return UITheme.tableScroll(reservationTable);
    }

    private void setupUI() {
        setVisible(true);
    }

    private void configureController() {
        setAddButtonListener(e -> addReservation());
        setUpdateButtonListener(e -> updateReservation());
        setDeleteButtonListener(e -> deleteReservation());
        setClearButtonListener(e -> clearForm());
        setBackButtonListener(e -> dispose());
        setSearchButtonListener(e -> performSearch());

        searchField.addActionListener(e -> performSearch());

        roomComboBox.addActionListener(e -> refreshTotalPrice(false));

        DocumentListener priceListener = new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                refreshTotalPrice(false);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                refreshTotalPrice(false);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                refreshTotalPrice(false);
            }
        };

        checkInField.getDocument().addDocumentListener(priceListener);
        checkOutField.getDocument().addDocumentListener(priceListener);

        reservationTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                fillFormFromSelectedRow();
            }
        });
    }

    private void performSearch() {
        searchButton.setEnabled(false);
        String query = getSearchQuery();
        new javax.swing.SwingWorker<List<Reservation>, Void>() {
            @Override
            protected List<Reservation> doInBackground() throws Exception {
                return reservationController.searchReservations(query);
            }
            @Override
            protected void done() {
                try {
                    loadReservations(get());
                } catch (Exception ex) {
                    showError("Pencarian gagal: " + ex.getMessage());
                } finally {
                    searchButton.setEnabled(true);
                }
            }
        }.execute();
    }

    private void loadRoomOptions() {
        roomComboBox.removeAllItems();
        roomComboBox.addItem("Loading...");
        roomComboBox.setEnabled(false);

        new javax.swing.SwingWorker<List<Room>, Void>() {
            @Override
            protected List<Room> doInBackground() throws Exception {
                return roomController.getAvailableRooms();
            }
            @Override
            protected void done() {
                try {
                    roomComboBox.removeAllItems();
                    roomComboBox.addItem("");
                    for (Room room : get()) {
                        roomComboBox.addItem(room.getRoomNumber()
                                + " - " + room.getRoomType());
                    }
                } catch (Exception ex) {
                    showError("Gagal memuat kamar: " + ex.getMessage());
                } finally {
                    roomComboBox.setEnabled(true);
                }
            }
        }.execute();
    }

    private void loadReservationsFromDatabase() {
        reservationTable.setEnabled(false);
        new javax.swing.SwingWorker<List<Reservation>, Void>() {
            @Override
            protected List<Reservation> doInBackground() throws Exception {
                return reservationController.getAllReservations();
            }
            @Override
            protected void done() {
                try {
                    loadReservations(get());
                } catch (Exception ex) {
                    showError("Gagal memuat reservasi: " + ex.getMessage());
                } finally {
                    reservationTable.setEnabled(true);
                }
            }
        }.execute();
    }

    private void loadReservations(List<Reservation> reservations) {
        clearTable();

        for (Reservation reservation : reservations) {
            addRowToTable(new Object[]{
                reservation.getReservationId(),
                reservation.getCustomer().getId(),
                reservation.getCustomer().getName(),
                reservation.getCustomer().getPhoneNumber(),
                reservation.getRoom().getRoomNumber(),
                reservation.getCheckInDate(),
                reservation.getCheckOutDate(),
                reservation.getTotalPrice()
            });
        }
    }

    private Reservation buildPartialReservationFromForm() {
        String customerName = getCustomerName().trim();
        String roomNumber = getSelectedRoomNumber();

        if (customerName.isEmpty()) {
            throw new IllegalArgumentException("Nama tamu wajib diisi.");
        }

        if (roomNumber.isEmpty()) {
            throw new IllegalArgumentException("Pilih kamar terlebih dahulu.");
        }

        LocalDate checkIn = parseDate(getCheckInDate(), "Check-in");
        LocalDate checkOut = parseDate(getCheckOutDate(), "Check-out");

        if (checkOut.isBefore(checkIn)) {
            throw new IllegalArgumentException("Tanggal check-out tidak boleh sebelum check-in.");
        }

        Customer customer = new Customer();
        customer.setName(customerName);
        customer.setPhoneNumber(getPhoneNumber());

        Room room = new Room();
        room.setRoomNumber(roomNumber);

        Reservation reservation = new Reservation();
        reservation.setCustomer(customer);
        reservation.setRoom(room);
        reservation.setCheckInDate(checkIn);
        reservation.setCheckOutDate(checkOut);

        return reservation;
    }

    private LocalDate parseDate(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()
                || "YYYY-MM-DD".equals(value.trim())) {
            throw new IllegalArgumentException(fieldName
                    + " wajib diisi dengan format YYYY-MM-DD.");
        }

        try {
            return LocalDate.parse(value.trim());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(fieldName
                    + " harus memakai format YYYY-MM-DD.");
        }
    }

    private void refreshTotalPrice(boolean showErrors) {
        try {
            String roomNumber = getSelectedRoomNumber();

            if (roomNumber.isEmpty()
                    || "YYYY-MM-DD".equals(getCheckInDate().trim())
                    || "YYYY-MM-DD".equals(getCheckOutDate().trim())) {
                setTotalPrice("");
                return;
            }

            LocalDate checkIn = parseDate(getCheckInDate(), "Check-in");
            LocalDate checkOut = parseDate(getCheckOutDate(), "Check-out");

            if (checkOut.isBefore(checkIn)) {
                setTotalPrice("");
                return;
            }

            setTotalPrice("Menghitung...");
            new javax.swing.SwingWorker<Double, Void>() {
                @Override
                protected Double doInBackground() throws Exception {
                    return reservationController.calculateTotalPrice(
                            roomNumber, checkIn, checkOut);
                }
                @Override
                protected void done() {
                    try {
                        setTotalPrice(UITheme.money(get()));
                    } catch (Exception ex) {
                        setTotalPrice("");
                        if (showErrors) showError(ex.getMessage());
                    }
                }
            }.execute();

        } catch (IllegalArgumentException e) {
            setTotalPrice("");
            if (showErrors) {
                showError(e.getMessage());
            }
        }
    }

    private void addReservation() {
        try {
            Reservation partialRes = buildPartialReservationFromForm();
            addButton.setEnabled(false);

            new javax.swing.SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    double totalPrice = reservationController.calculateTotalPrice(
                            partialRes.getRoom().getRoomNumber(), 
                            partialRes.getCheckInDate(), 
                            partialRes.getCheckOutDate());
                    if (totalPrice <= 0) throw new IllegalArgumentException("Total harga gagal dihitung.");
                    
                    Room room = roomController.getRoomByNumber(partialRes.getRoom().getRoomNumber());
                    if (room != null) partialRes.setRoom(room);
                    partialRes.setTotalPrice(totalPrice);

                    return reservationController.addReservation(partialRes);
                }
                @Override
                protected void done() {
                    try {
                        if (get()) {
                            showMessage("Reservasi berhasil ditambahkan.");
                            loadRoomOptions();
                            loadReservationsFromDatabase();
                            clearForm();
                        } else {
                            showError("Reservasi gagal ditambahkan. Kamar mungkin sudah tidak tersedia.");
                        }
                    } catch (Exception e) {
                        showError("Terjadi kesalahan: " + e.getMessage());
                    } finally {
                        addButton.setEnabled(true);
                    }
                }
            }.execute();
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    private void updateReservation() {
        int selectedRow = getSelectedRow();
        if (selectedRow < 0) {
            showError("Pilih reservasi yang ingin diupdate.");
            return;
        }

        try {
            Reservation partialRes = buildPartialReservationFromForm();
            partialRes.setReservationId((int) tableModel.getValueAt(selectedRow, 0));
            partialRes.getCustomer().setId((int) tableModel.getValueAt(selectedRow, 1));
            updateButton.setEnabled(false);

            new javax.swing.SwingWorker<Boolean, Void>() {
                @Override
                protected Boolean doInBackground() throws Exception {
                    double totalPrice = reservationController.calculateTotalPrice(
                            partialRes.getRoom().getRoomNumber(), 
                            partialRes.getCheckInDate(), 
                            partialRes.getCheckOutDate());
                    if (totalPrice <= 0) throw new IllegalArgumentException("Total harga gagal dihitung.");
                    
                    Room room = roomController.getRoomByNumber(partialRes.getRoom().getRoomNumber());
                    if (room != null) partialRes.setRoom(room);
                    partialRes.setTotalPrice(totalPrice);

                    return reservationController.updateReservation(partialRes);
                }
                @Override
                protected void done() {
                    try {
                        if (get()) {
                            showMessage("Reservasi berhasil diupdate.");
                            loadRoomOptions();
                            loadReservationsFromDatabase();
                            clearForm();
                        } else {
                            showError("Reservasi gagal diupdate. Periksa kamar dan tanggal.");
                        }
                    } catch (Exception e) {
                        showError("Terjadi kesalahan: " + e.getMessage());
                    } finally {
                        updateButton.setEnabled(true);
                    }
                }
            }.execute();
        } catch (IllegalArgumentException e) {
            showError(e.getMessage());
        }
    }

    private void deleteReservation() {
        int selectedRow = getSelectedRow();
        if (selectedRow < 0) {
            showError("Pilih reservasi yang ingin dihapus.");
            return;
        }

        int reservationId = (int) tableModel.getValueAt(selectedRow, 0);
        int confirm = JOptionPane.showConfirmDialog(
                this, "Hapus reservasi #" + reservationId + "?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        deleteButton.setEnabled(false);
        new javax.swing.SwingWorker<Boolean, Void>() {
            @Override
            protected Boolean doInBackground() throws Exception {
                return reservationController.deleteReservation(reservationId);
            }
            @Override
            protected void done() {
                try {
                    if (get()) {
                        showMessage("Reservasi berhasil dihapus.");
                        loadRoomOptions();
                        loadReservationsFromDatabase();
                        clearForm();
                    } else {
                        showError("Reservasi gagal dihapus.");
                    }
                } catch (Exception e) {
                    showError("Terjadi kesalahan: " + e.getMessage());
                } finally {
                    deleteButton.setEnabled(true);
                }
            }
        }.execute();
    }

    private void fillFormFromSelectedRow() {
        int selectedRow = getSelectedRow();

        if (selectedRow < 0) {
            return;
        }

        customerNameField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 2)));
        phoneNumberField.setText(emptyIfNull(tableModel.getValueAt(selectedRow, 3)));
        setSelectedRoomByNumber(String.valueOf(tableModel.getValueAt(selectedRow, 4)));
        checkInField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 5)));
        checkOutField.setText(String.valueOf(tableModel.getValueAt(selectedRow, 6)));
        totalPriceField.setText(UITheme.money(
                ((Number) tableModel.getValueAt(selectedRow, 7)).doubleValue()));
    }

    public String getCustomerName() {
        return customerNameField.getText();
    }

    public String getPhoneNumber() {
        return phoneNumberField.getText();
    }

    public String getSelectedRoom() {
        return (String) roomComboBox.getSelectedItem();
    }

    public String getSelectedRoomNumber() {
        String selectedRoom = getSelectedRoom();

        if (selectedRoom == null) {
            return "";
        }

        String value = selectedRoom.trim();
        int separatorIndex = value.indexOf(" - ");

        if (separatorIndex > 0) {
            return value.substring(0, separatorIndex).trim();
        }

        return value;
    }

    public String getCheckInDate() {
        return checkInField.getText();
    }

    public String getCheckOutDate() {
        return checkOutField.getText();
    }

    public String getTotalPrice() {
        return totalPriceField.getText();
    }

    public void setTotalPrice(String price) {
        totalPriceField.setText(price);
    }

    public void setSelectedRoomByNumber(String roomNumber) {
        ComboBoxModel<String> model = roomComboBox.getModel();

        for (int i = 0; i < model.getSize(); i++) {
            String item = model.getElementAt(i);

            if (item != null
                    && (item.equals(roomNumber)
                    || item.startsWith(roomNumber + " - "))) {
                roomComboBox.setSelectedItem(item);
                return;
            }
        }

        if (roomNumber == null || roomNumber.trim().isEmpty()) {
            return;
        }

        Room room = roomController.getRoomByNumber(roomNumber);

        if (room != null) {
            String item = room.getRoomNumber()
                    + " - " + room.getRoomType()
                    + " (reservasi ini)";
            roomComboBox.addItem(item);
            roomComboBox.setSelectedItem(item);
        }
    }

    private String emptyIfNull(Object value) {
        if (value == null) {
            return "";
        }

        return String.valueOf(value);
    }

    public void clearForm() {
        customerNameField.setText("");
        phoneNumberField.setText("");
        loadRoomOptions();
        roomComboBox.setSelectedIndex(0);
        checkInField.setText("YYYY-MM-DD");
        checkOutField.setText("YYYY-MM-DD");
        totalPriceField.setText("");
    }

    public void addRowToTable(Object[] rowData) {
        tableModel.addRow(rowData);
    }

    public int getSelectedRow() {
        return reservationTable.getSelectedRow();
    }

    public void clearTable() {
        tableModel.setRowCount(0);
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

    public void setSearchButtonListener(ActionListener listener) {
        searchButton.addActionListener(listener);
    }

    public String getSearchQuery() {
        return searchField.getText();
    }

    public void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Information", JOptionPane.INFORMATION_MESSAGE);
    }

    public void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Error", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ReservationView::new);
    }
}

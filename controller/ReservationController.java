package controller;

import model.Customer;
import model.DatabaseConnection;
import model.DynamicPricing;
import model.Reservation;
import model.Room;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReservationController {

    private Connection connection;
    private final DynamicPricing dynamicPricing = new DynamicPricing();

    public ReservationController() {
        connection = DatabaseConnection.getConnection();
    }

    private Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DatabaseConnection.getConnection();
        }

        if (connection == null) {
            throw new SQLException("Koneksi database tidak tersedia.");
        }

        return connection;
    }

    public boolean addReservation(Reservation reservation) {

        String query = "INSERT INTO reservations "
                + "(customer_id, room_number, check_in, check_out, total_price) "
                + "VALUES (?, ?, ?, ?, ?)";

        try {
            Connection conn = getConnection();
            conn.setAutoCommit(false);

            String roomNumber = reservation.getRoom().getRoomNumber();

            if (!isRoomAvailable(conn, roomNumber)) {
                throw new SQLException("Kamar sudah booked atau tidak tersedia.");
            }

            int customerId = saveCustomer(conn, reservation.getCustomer());

            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, customerId);
                ps.setString(2, roomNumber);
                ps.setDate(3, Date.valueOf(reservation.getCheckInDate()));
                ps.setDate(4, Date.valueOf(reservation.getCheckOutDate()));
                ps.setDouble(5, reservation.getTotalPrice());
                ps.executeUpdate();
            }

            updateRoomStatus(conn, roomNumber, "BOOKED");
            conn.commit();

            return true;

        } catch (SQLException e) {
            rollback();
            e.printStackTrace();
            return false;
        } finally {
            restoreAutoCommit();
        }
    }

    public List<Reservation> getAllReservations() {
        String query = "SELECT r.id, r.customer_id, c.name AS customer_name, "
                + "c.phone, c.email, r.room_number, r.check_in, "
                + "r.check_out, r.total_price "
                + "FROM reservations r "
                + "LEFT JOIN customers c ON c.id = r.customer_id "
                + "ORDER BY r.id DESC";

        return getReservations(query, null);
    }

    public List<Reservation> searchReservations(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return getAllReservations();
        }

        String query = "SELECT r.id, r.customer_id, c.name AS customer_name, "
                + "c.phone, c.email, r.room_number, r.check_in, "
                + "r.check_out, r.total_price "
                + "FROM reservations r "
                + "LEFT JOIN customers c ON c.id = r.customer_id "
                + "WHERE c.name LIKE ? OR c.phone LIKE ? OR r.room_number LIKE ? "
                + "ORDER BY r.id DESC";

        return getReservations(query, "%" + keyword.trim() + "%");
    }

    private List<Reservation> getReservations(String query, String keyword) {

        List<Reservation> reservationList = new ArrayList<>();

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {

            if (keyword != null) {
                ps.setString(1, keyword);
                ps.setString(2, keyword);
                ps.setString(3, keyword);
            }

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Customer customer = new Customer();
                    customer.setId(rs.getInt("customer_id"));
                    customer.setName(rs.getString("customer_name"));
                    customer.setPhoneNumber(rs.getString("phone"));
                    customer.setEmail(rs.getString("email"));

                    Room room = getRoomByNumber(rs.getString("room_number"));

                    if (room == null) {
                        room = new Room();
                        room.setRoomNumber(rs.getString("room_number"));
                    }

                    Reservation reservation = new Reservation();
                    reservation.setReservationId(rs.getInt("id"));
                    reservation.setCustomer(customer);
                    reservation.setRoom(room);
                    reservation.setCheckInDate(rs.getDate("check_in").toLocalDate());
                    reservation.setCheckOutDate(rs.getDate("check_out").toLocalDate());
                    reservation.setTotalPrice(rs.getDouble("total_price"));

                    reservationList.add(reservation);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return reservationList;
    }

    public boolean updateReservation(Reservation reservation) {

        String query = "UPDATE reservations SET "
                + "customer_id=?, "
                + "room_number=?, "
                + "check_in=?, "
                + "check_out=?, "
                + "total_price=? "
                + "WHERE id=?";

        try {
            Connection conn = getConnection();
            conn.setAutoCommit(false);

            String oldRoomNumber = findReservationRoomNumber(conn,
                    reservation.getReservationId());
            String newRoomNumber = reservation.getRoom().getRoomNumber();

            if (oldRoomNumber == null) {
                throw new SQLException("Reservasi tidak ditemukan.");
            }

            if (!oldRoomNumber.equals(newRoomNumber)
                    && !isRoomAvailable(conn, newRoomNumber)) {
                throw new SQLException("Kamar sudah booked atau tidak tersedia.");
            }

            int customerId = saveCustomer(conn, reservation.getCustomer());

            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, customerId);
                ps.setString(2, newRoomNumber);
                ps.setDate(3, Date.valueOf(reservation.getCheckInDate()));
                ps.setDate(4, Date.valueOf(reservation.getCheckOutDate()));
                ps.setDouble(5, reservation.getTotalPrice());
                ps.setInt(6, reservation.getReservationId());
                ps.executeUpdate();
            }

            if (!oldRoomNumber.equals(newRoomNumber)
                    && !hasReservationsForRoom(conn, oldRoomNumber)) {
                updateRoomStatus(conn, oldRoomNumber, "AVAILABLE");
            }

            updateRoomStatus(conn, newRoomNumber, "BOOKED");
            conn.commit();

            return true;

        } catch (SQLException e) {
            rollback();
            e.printStackTrace();
            return false;
        } finally {
            restoreAutoCommit();
        }
    }

    public boolean deleteReservation(int reservationId) {

        String query = "DELETE FROM reservations WHERE id=?";

        try {
            Connection conn = getConnection();
            conn.setAutoCommit(false);

            String roomNumber = findReservationRoomNumber(conn, reservationId);

            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setInt(1, reservationId);
                ps.executeUpdate();
            }

            if (roomNumber != null && !hasReservationsForRoom(conn, roomNumber)) {
                updateRoomStatus(conn, roomNumber, "AVAILABLE");
            }

            conn.commit();

            return true;

        } catch (SQLException e) {
            rollback();
            e.printStackTrace();
            return false;
        } finally {
            restoreAutoCommit();
        }
    }

    public double calculateTotalPrice(String roomNumber,
                                      LocalDate checkIn,
                                      LocalDate checkOut) {

        Room room;

        try {
            room = getRoomByNumber(roomNumber);
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }

        if (room == null) {
            return 0;
        }

        return dynamicPricing.calculateDynamicPrice(room, checkIn, checkOut);
    }

    public int countAllReservations() {

        String query = "SELECT COUNT(*) FROM reservations";

        try (Statement st = getConnection().createStatement();
             ResultSet rs = st.executeQuery(query)) {

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    private int saveCustomer(Connection conn, Customer customer)
            throws SQLException {

        String name = normalizeText(customer.getName());
        String phone = normalizeText(customer.getPhoneNumber());
        String email = normalizeText(customer.getEmail());

        if (name == null) {
            throw new SQLException("Nama customer wajib diisi.");
        }

        if (customer.getId() > 0) {
            String query = "UPDATE customers "
                    + "SET name = ?, phone = ?, email = ? "
                    + "WHERE id = ?";

            try (PreparedStatement ps = conn.prepareStatement(query)) {
                ps.setString(1, name);
                ps.setString(2, phone);
                ps.setString(3, email);
                ps.setInt(4, customer.getId());
                ps.executeUpdate();
            }

            return customer.getId();
        }

        int existingCustomerId = findCustomerId(conn, name, phone);

        if (existingCustomerId > 0) {
            return existingCustomerId;
        }

        String query = "INSERT INTO customers (name, phone, email) "
                + "VALUES (?, ?, ?)";

        try (PreparedStatement ps = conn.prepareStatement(query,
                Statement.RETURN_GENERATED_KEYS)) {

            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, email);
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }

        throw new SQLException("ID customer gagal dibuat.");
    }

    private int findCustomerId(Connection conn, String name, String phone)
            throws SQLException {

        String query;

        if (phone == null) {
            query = "SELECT id FROM customers "
                    + "WHERE name = ? AND (phone IS NULL OR phone = '') "
                    + "LIMIT 1";
        } else {
            query = "SELECT id FROM customers "
                    + "WHERE name = ? AND phone = ? "
                    + "LIMIT 1";
        }

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, name);

            if (phone != null) {
                ps.setString(2, phone);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("id");
                }
            }
        }

        return 0;
    }

    private Room getRoomByNumber(String roomNumber) throws SQLException {

        String query = "SELECT room_number, type, base_price, status "
                + "FROM rooms WHERE room_number = ?";

        try (PreparedStatement ps = getConnection().prepareStatement(query)) {

            ps.setString(1, roomNumber);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Room room = new Room();
                    room.setRoomNumber(rs.getString("room_number"));
                    room.setRoomType(RoomController.toDisplayRoomType(
                            rs.getString("type")));
                    room.setPrice(rs.getDouble("base_price"));
                    room.setStatus(RoomController.toDisplayStatus(
                            rs.getString("status")));
                    return room;
                }
            }
        }

        return null;
    }

    private boolean isRoomAvailable(Connection conn, String roomNumber)
            throws SQLException {

        String query = "SELECT status FROM rooms WHERE room_number = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {
            ps.setString(1, roomNumber);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next()
                        && "AVAILABLE".equalsIgnoreCase(rs.getString("status"));
            }
        }
    }

    private String findReservationRoomNumber(Connection conn,
                                             int reservationId)
            throws SQLException {

        String query = "SELECT room_number FROM reservations WHERE id = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setInt(1, reservationId);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("room_number");
                }
            }
        }

        return null;
    }

    private void updateRoomStatus(Connection conn,
                                  String roomNumber,
                                  String status)
            throws SQLException {

        String query = "UPDATE rooms SET status = ? WHERE room_number = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, status);
            ps.setString(2, roomNumber);
            ps.executeUpdate();
        }
    }

    private boolean hasReservationsForRoom(Connection conn,
                                           String roomNumber)
            throws SQLException {

        String query = "SELECT COUNT(*) FROM reservations WHERE room_number = ?";

        try (PreparedStatement ps = conn.prepareStatement(query)) {

            ps.setString(1, roomNumber);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() && rs.getInt(1) > 0;
            }
        }
    }

    private String normalizeText(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }

        return value.trim();
    }

    private void rollback() {
        try {
            if (connection != null) {
                connection.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void restoreAutoCommit() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.setAutoCommit(true);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}

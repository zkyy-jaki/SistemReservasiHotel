/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.DatabaseConnection;
import model.Room;

public class RoomController {

    private Connection connection;

    // Constructor
    public RoomController() {
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

    public static String toDatabaseRoomType(String roomType) {
        if (roomType == null) {
            return "STANDARD";
        }

        String normalized = roomType.trim().toUpperCase()
                .replace(" ROOM", "")
                .replace(" ", "_");

        if (normalized.contains("DELUXE")) {
            return "DELUXE";
        }

        if (normalized.contains("SUITE")) {
            return "SUITE";
        }

        return "STANDARD";
    }

    public static String toDisplayRoomType(String roomType) {
        if (roomType == null) {
            return "Standard Room";
        }

        switch (roomType.trim().toUpperCase()) {
            case "DELUXE":
                return "Deluxe Room";
            case "SUITE":
                return "Suite Room";
            default:
                return "Standard Room";
        }
    }

    public static String toDatabaseStatus(String status) {
        if (status == null) {
            return "AVAILABLE";
        }

        String normalized = status.trim().toUpperCase();

        if (normalized.contains("BOOK")) {
            return "BOOKED";
        }

        if (normalized.contains("OCCUP") || normalized.contains("MAINTENANCE")) {
            return "OCCUPIED";
        }

        return "AVAILABLE";
    }

    public static String toDisplayStatus(String status) {
        if (status == null) {
            return "Available";
        }

        switch (status.trim().toUpperCase()) {
            case "BOOKED":
                return "Booked";
            case "OCCUPIED":
                return "Occupied";
            default:
                return "Available";
        }
    }

    private Room mapRoom(ResultSet rs) throws SQLException {
        Room room = new Room();
        room.setRoomNumber(rs.getString("room_number"));
        room.setRoomType(toDisplayRoomType(rs.getString("type")));
        room.setPrice(rs.getDouble("base_price"));
        room.setStatus(toDisplayStatus(rs.getString("status")));
        return room;
    }

    public boolean addRoom(Room room) {

        String query = "INSERT INTO rooms "
                + "(room_number, type, base_price, status) "
                + "VALUES (?, ?, ?, ?)";

        try (PreparedStatement ps =
                     getConnection().prepareStatement(query)) {

            ps.setString(1, room.getRoomNumber());
            ps.setString(2, toDatabaseRoomType(room.getRoomType()));
            ps.setDouble(3, room.getPrice());
            ps.setString(4, toDatabaseStatus(room.getStatus()));

            ps.executeUpdate();

            return true;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }

    public List<Room> getAllRooms() {

        List<Room> roomList = new ArrayList<>();

        String query = "SELECT room_number, type, base_price, status "
                + "FROM rooms ORDER BY room_number";

        try (Statement st = getConnection().createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                roomList.add(mapRoom(rs));
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return roomList;
    }

    public Room getRoomByNumber(String roomNumber) {

        String query = "SELECT room_number, type, base_price, status "
                + "FROM rooms WHERE room_number = ?";

        try (PreparedStatement ps =
                     getConnection().prepareStatement(query)) {

            ps.setString(1, roomNumber);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapRoom(rs);
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return null;
    }

    public List<Room> getAvailableRooms() {

        List<Room> roomList = new ArrayList<>();

        String query = "SELECT room_number, type, base_price, status "
                + "FROM rooms WHERE status = 'AVAILABLE' "
                + "ORDER BY room_number";

        try (Statement st = getConnection().createStatement();
             ResultSet rs = st.executeQuery(query)) {

            while (rs.next()) {
                roomList.add(mapRoom(rs));
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return roomList;
    }

    public boolean updateRoom(Room room) {
        return updateRoom(room.getRoomNumber(), room);
    }

    public boolean updateRoom(String originalRoomNumber, Room room) {

        String query = "UPDATE rooms SET "
                + "room_number=?, "
                + "type=?, "
                + "base_price=?, "
                + "status=? "
                + "WHERE room_number=?";

        try (PreparedStatement ps =
                     getConnection().prepareStatement(query)) {

            ps.setString(1, room.getRoomNumber());
            ps.setString(2, toDatabaseRoomType(room.getRoomType()));
            ps.setDouble(3, room.getPrice());
            ps.setString(4, toDatabaseStatus(room.getStatus()));
            ps.setString(5, originalRoomNumber);

            ps.executeUpdate();

            return true;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }

    public boolean deleteRoom(int roomId) {
        return false;
    }

    public boolean deleteRoom(String roomNumber) {

        String query =
                "DELETE FROM rooms WHERE room_number=?";

        try (PreparedStatement ps =
                     getConnection().prepareStatement(query)) {

            ps.setString(1, roomNumber);

            ps.executeUpdate();

            return true;

        } catch (SQLException e) {

            e.printStackTrace();
            return false;
        }
    }

    public int countAllRooms() {
        return countRooms(null);
    }

    public int countRoomsByStatus(String status) {
        return countRooms(toDatabaseStatus(status));
    }

    private int countRooms(String status) {

        String query = status == null
                ? "SELECT COUNT(*) FROM rooms"
                : "SELECT COUNT(*) FROM rooms WHERE status = ?";

        try (PreparedStatement ps =
                     getConnection().prepareStatement(query)) {

            if (status != null) {
                ps.setString(1, status);
            }

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }

        } catch (SQLException e) {

            e.printStackTrace();
        }

        return 0;
    }
}

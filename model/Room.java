/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author LENOVO
 */
public class Room {

    private int id;
    private String roomNumber;
    private String roomType;
    private double price;
    private boolean available;
    private String status;

    // Constructor kosong
    public Room() {
    }

    // Constructor parameter
    public Room(int id, String roomNumber, String roomType, double price, boolean available) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.price = price;
        this.available = available;
        this.status = available ? "Available" : "Booked";
    }

    // Getter & Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
        this.status = available ? "Available" : "Booked";
    }

    public String getStatus() {
        if (status == null || status.trim().isEmpty()) {
            return available ? "Available" : "Booked";
        }

        return status;
    }

    public void setStatus(String status) {
        this.status = status;
        this.available = "AVAILABLE".equalsIgnoreCase(status)
                || "Available".equalsIgnoreCase(status);
    }

    // Method polymorphism nanti bisa dioverride
    public double calculatePrice(int days) {
        return price * days;
    }

    // Menampilkan info kamar
    public String getRoomInfo() {
        return "Room Number : " + roomNumber +
               "\nRoom Type : " + roomType +
               "\nPrice : " + price +
               "\nStatus : " + getStatus();
    }
}

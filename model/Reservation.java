/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author LENOVO
 */
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Reservation {

    private int reservationId;
    private Customer customer;
    private Room room;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private double totalPrice;

    // Constructor kosong
    public Reservation() {
    }

    // Constructor lengkap
    public Reservation(int reservationId,
                       Customer customer,
                       Room room,
                       LocalDate checkInDate,
                       LocalDate checkOutDate) {

        this.reservationId = reservationId;
        this.customer = customer;
        this.room = room;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;

        calculateTotalPrice();
    }

    // Menghitung total harga
    public void calculateTotalPrice() {

        long days = ChronoUnit.DAYS.between(checkInDate, checkOutDate);

        if (days <= 0) {
            days = 1;
        }

        this.totalPrice = room.calculatePrice((int) days);
    }

    // Getter Setter
    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getReservationInfo() {

        return "Reservation ID : " + reservationId +
               "\nCustomer : " + customer.getnama() +
               "\nRoom : " + room.getRoomNumber() +
               "\nCheck In : " + checkInDate +
               "\nCheck Out : " + checkOutDate +
               "\nTotal Price : " + totalPrice;
    }
}
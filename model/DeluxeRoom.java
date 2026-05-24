/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author LENOVO
 */
public class DeluxeRoom extends Room {

    private boolean breakfastIncluded;
    private boolean jacuzzi;

    // Constructor kosong
    public DeluxeRoom() {
    }

    // Constructor lengkap
    public DeluxeRoom(int id, String roomNumber, double price,
                       boolean available,
                       boolean breakfastIncluded,
                       boolean jacuzzi) {

        super(id, roomNumber, "Deluxe Room", price, available);

        this.breakfastIncluded = breakfastIncluded;
        this.jacuzzi = jacuzzi;
    }

    // Getter Setter
    public boolean isBreakfastIncluded() {
        return breakfastIncluded;
    }

    public void setBreakfastIncluded(boolean breakfastIncluded) {
        this.breakfastIncluded = breakfastIncluded;
    }

    public boolean isJacuzzi() {
        return jacuzzi;
    }

    public void setJacuzzi(boolean jacuzzi) {
        this.jacuzzi = jacuzzi;
    }

    /**
     * Polymorphism (Override)
     * Deluxe room kena tambahan 15%
     */
    @Override
    public double calculatePrice(int days) {

        double total = getPrice() * days;

        // tambahan deluxe service
        total += total * 0.15;

        return total;
    }

    @Override
    public String getRoomInfo() {

        return super.getRoomInfo() +
               "\nBreakfast : " + breakfastIncluded +
               "\nJacuzzi : " + jacuzzi;
    }
}
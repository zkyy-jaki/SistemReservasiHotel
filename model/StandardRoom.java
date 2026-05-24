/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author LENOVO
 */
public class StandardRoom extends Room {

    private boolean freeWifi;

    // Constructor kosong
    public StandardRoom() {
    }

    // Constructor lengkap
    public StandardRoom(int id,
                        String roomNumber,
                        double price,
                        boolean available,
                        boolean freeWifi) {

        super(id, roomNumber, "Standard Room", price, available);

        this.freeWifi = freeWifi;
    }

    // Getter Setter
    public boolean isFreeWifi() {
        return freeWifi;
    }

    public void setFreeWifi(boolean freeWifi) {
        this.freeWifi = freeWifi;
    }

    /**
     * Override polymorphism
     * Standard room tidak ada tambahan biaya
     */
    @Override
    public double calculatePrice(int days) {

        return getPrice() * days;
    }

    @Override
    public String getRoomInfo() {

        return super.getRoomInfo() +
               "\nFree Wifi : " + freeWifi;
    }
}

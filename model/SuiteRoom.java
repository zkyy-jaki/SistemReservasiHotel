/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author LENOVO
 */

    public class SuiteRoom extends Room {

    private boolean privatePool;
    private boolean vipService;

    // Constructor kosong
    public SuiteRoom() {
    }

    // Constructor lengkap
    public SuiteRoom(int id, String roomNumber,
                     double price,
                     boolean available,
                     boolean privatePool,
                     boolean vipService) {

        super(id, roomNumber, "Suite Room", price, available);

        this.privatePool = privatePool;
        this.vipService = vipService;
    }

    // Getter Setter
    public boolean isPrivatePool() {
        return privatePool;
    }

    public void setPrivatePool(boolean privatePool) {
        this.privatePool = privatePool;
    }

    public boolean isVipService() {
        return vipService;
    }

    public void setVipService(boolean vipService) {
        this.vipService = vipService;
    }

    /**
     * Override polymorphism
     * Suite room kena tambahan 30%
     */
    @Override
    public double calculatePrice(int days) {

        double total = getPrice() * days;

        // tambahan layanan suite
        total += total * 0.30;

        return total;
    }

    @Override
    public String getRoomInfo() {

        return super.getRoomInfo() +
               "\nPrivate Pool : " + privatePool +
               "\nVIP Service : " + vipService;
    }
}


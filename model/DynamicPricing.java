/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author LENOVO
 */
import java.time.DayOfWeek;
import java.time.LocalDate;

public class DynamicPricing {

    // Hitung harga dinamis
    public double calculateDynamicPrice(Room room,
                                        LocalDate checkIn,
                                        LocalDate checkOut) {

        long days = checkOut.toEpochDay() - checkIn.toEpochDay();

        if (days <= 0) {
            days = 1;
        }

        double basePrice = room.calculatePrice((int) days);

        // Weekend pricing
        if (checkIn.getDayOfWeek() == DayOfWeek.SATURDAY ||
            checkIn.getDayOfWeek() == DayOfWeek.SUNDAY) {

            basePrice += basePrice * 0.20;
        }

        // Diskon long stay
        if (days >= 7) {
            basePrice -= basePrice * 0.10;
        }

        return basePrice;
    }
}
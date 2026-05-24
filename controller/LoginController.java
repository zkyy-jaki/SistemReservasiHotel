/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import model.DatabaseConnection;

public class LoginController {

    public boolean login(String username,
                         String password) {

        if (username == null || password == null) {
            return false;
        }

        String query = "SELECT id FROM users "
                + "WHERE username = ? AND password = ? "
                + "AND role IN ('ADMIN', 'STAFF') "
                + "LIMIT 1";

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement ps = connection.prepareStatement(query)) {

            ps.setString(1, username.trim());
            ps.setString(2, password);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }

        } catch (SQLException | NullPointerException e) {
            e.printStackTrace();
            return false;
        }
    }
}

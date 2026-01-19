package auth;

import java.sql.*;
import java.util.Scanner;
import db.DBConnection;

public class AuthService {

    Scanner sc = new Scanner(System.in);

    public int userId;
    public String role;

    public boolean login() {

        System.out.print("Username: ");
        String username = sc.next();
        System.out.print("Password: ");
        String password = sc.next();

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM users WHERE username=? AND password=?"
            );
            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                userId = rs.getInt("id");
                role = rs.getString("role"); // 🔥 IMPORTANT
                System.out.println("Login Successful!");
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Invalid Login!");
        return false;
    }

    public void register() {

        System.out.print("Username: ");
        String username = sc.next();
        System.out.print("Password: ");
        String password = sc.next();

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO users(username,password,role) VALUES(?,?,?)"
            );
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, "student"); // ✅ FIXED ROLE

            ps.executeUpdate();
            System.out.println("Registration Successful! (Student role assigned)");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

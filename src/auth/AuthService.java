package auth;

import java.sql.*;
import java.util.Scanner;
import db.DBConnection;
import java.security.MessageDigest;

public class AuthService {

    Scanner sc = new Scanner(System.in);

    public int userId;
    public String role;

    // ================= LOGIN =================
    public boolean login() {

        System.out.print("Username: ");
        String username = sc.next();

        System.out.print("Password: ");
        String password = sc.next();

        try {
            Connection con = DBConnection.getConnection();

            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM users WHERE username=? AND password=? AND active=TRUE"
            );

            ps.setString(1, username);
            ps.setString(2, hashPassword(password));

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                userId = rs.getInt("id");
                role = rs.getString("role");
                boolean firstLogin = rs.getBoolean("first_login");

                System.out.println("Login Successful!");

                if (firstLogin) {
                    forcePasswordChange(userId);
                }
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("Invalid Login!");
        return false;
    }

    // ================= FORCE PASSWORD CHANGE =================
    private void forcePasswordChange(int userId) {

        System.out.println("\n⚠ FIRST LOGIN DETECTED");
        System.out.println("You must change your password.");

        System.out.print("Enter New Password: ");
        String newPassword = sc.next();

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE users SET password=?, first_login=FALSE WHERE id=?"
            );

            ps.setString(1, hashPassword(newPassword));
            ps.setInt(2, userId);
            ps.executeUpdate();

            System.out.println("✅ Password changed successfully.");
            System.out.println("Please login again.");
            System.exit(0);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= CHANGE PASSWORD =================
    public void changePassword(int userId) {

        System.out.print("Enter New Password: ");
        String newPass = sc.next();

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE users SET password=? WHERE id=?"
            );

            ps.setString(1, hashPassword(newPass));
            ps.setInt(2, userId);
            ps.executeUpdate();

            System.out.println("✅ Password changed successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= PASSWORD HASHING =================
    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(password.getBytes());

            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    // ================= ADMIN RESET PASSWORD =================
    public void resetStudentPassword() {

        System.out.print("Enter Student Username: ");
        String username = sc.next();

        String tempPassword = "Reset@" + (int)(Math.random() * 1000);

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "UPDATE users SET password=?, first_login=TRUE WHERE username=? AND role='student'"
            );

            ps.setString(1, hashPassword(tempPassword));
            ps.setString(2, username);

            int updated = ps.executeUpdate();

            if (updated > 0) {
                System.out.println("Temporary Password: " + tempPassword);
                System.out.println("Student must change password on next login.");
            } else {
                System.out.println("Student not found.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

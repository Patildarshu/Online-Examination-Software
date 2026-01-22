package student;

import db.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class StudentService {

    // Show only ACTIVE subjects (like college exam schedule)
    public void showAvailableSubjects() {

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "SELECT name FROM subjects WHERE active = TRUE"
            );

            ResultSet rs = ps.executeQuery();

            System.out.println("\n--- AVAILABLE SUBJECT EXAMS ---");

            boolean found = false;
            while (rs.next()) {
                System.out.println("• " + rs.getString("name"));
                found = true;
            }

            if (!found) {
                System.out.println("No exams available right now.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

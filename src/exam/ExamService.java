package exam;

import java.sql.*;
import java.util.Scanner;
import db.DBConnection;

public class ExamService {


    public int startExam(String subject) {

        int score = 0;

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "SELECT * FROM questions WHERE subject=?"
            );

            ps.setString(1, subject);
            ResultSet rs = ps.executeQuery();

            Scanner sc = new Scanner(System.in);

            while (rs.next()) {
                System.out.println(rs.getString("question"));
                System.out.println("1. " + rs.getString("option1"));
                System.out.println("2. " + rs.getString("option2"));
                System.out.println("3. " + rs.getString("option3"));
                System.out.println("4. " + rs.getString("option4"));

                System.out.print("Answer: ");
                int ans = sc.nextInt();

                if (ans == rs.getInt("correct_option")) {
                    score++;
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return score;
    }


}

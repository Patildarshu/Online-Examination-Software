package exam;

import java.sql.*;
import java.util.Scanner;
import db.DBConnection;

public class ExamService {

    public int startExam() {
        Scanner sc = new Scanner(System.in);
        int score = 0;
        long startTime = System.currentTimeMillis();
        long duration = 60 * 5000; // 1 minute

        try {
            Connection con = DBConnection.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM questions");

            while (rs.next()) {
                if (System.currentTimeMillis() - startTime > duration) {
                    System.out.println("\n⏰ Time Over!");
                    break;
                }

                System.out.println("\n" + rs.getString("question"));
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

package student;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;
import db.DBConnection;

public class StudentService {

    public void startExam() {
        Scanner sc = new Scanner(System.in);
        int score = 0;

        try {
            Connection con = DBConnection.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM questions");

            while (rs.next()) {
                System.out.println("\n" + rs.getString("question"));
                System.out.println("1. " + rs.getString("option1"));
                System.out.println("2. " + rs.getString("option2"));
                System.out.println("3. " + rs.getString("option3"));
                System.out.println("4. " + rs.getString("option4"));

                System.out.print("Your Answer: ");
                int ans = sc.nextInt();

                if (ans == rs.getInt("correct_option")) {
                    score++;
                }
            }

            System.out.println("\nExam Finished!");
            System.out.println("Your Score: " + score);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

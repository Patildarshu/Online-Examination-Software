package admin;

import java.sql.*;
import java.util.Scanner;

import ai.AIQuestionService;
import ai.QuestionParserService;
import auth.AuthService;
import db.DBConnection;

public class AdminService {

    Scanner sc = new Scanner(System.in);

    public void adminMenu() {

        while (true) {
            System.out.println("\n--- ADMIN PANEL ---");
            System.out.println("1. Add Question (Manual)");
            System.out.println("2. View Results");
            System.out.println("3. Reset Student Exam");
            System.out.println("4. Reset Student Password");
            System.out.println("5. Generate Questions using AI");
            System.out.println("6. Exit");

            System.out.print("Enter choice: ");
            int ch = sc.nextInt();

            switch (ch) {

                case 1:
                    addQuestion();
                    break;

                case 2:
                    viewResults();
                    break;

                case 3:
                    resetExam();
                    break;

                case 4:
                    AuthService auth = new AuthService();
                    auth.resetStudentPassword();
                    break;

                case 5:
                    try {
                        sc.nextLine();
                        System.out.print("Enter Topic: ");
                        String topic = sc.nextLine();

                        System.out.print("Number of Questions: ");
                        int count = sc.nextInt();

                        AIQuestionService ai = new AIQuestionService();
                        QuestionParserService parser = new QuestionParserService();

                        String response = ai.generateMCQs(topic, count);
                        parser.saveQuestions(response);

                        System.out.println("🎉 AI Question Generation Completed!");

                    } catch (Exception e) {
                        System.out.println("❌ Failed to generate questions using AI.");
                        e.printStackTrace();
                    }
                    break;

                case 6:
                    return;

                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    // ================= ADD QUESTION =================
    public void addQuestion() {

        sc.nextLine();
        System.out.print("Question: ");
        String q = sc.nextLine();

        System.out.print("Option 1: ");
        String o1 = sc.nextLine();
        System.out.print("Option 2: ");
        String o2 = sc.nextLine();
        System.out.print("Option 3: ");
        String o3 = sc.nextLine();
        System.out.print("Option 4: ");
        String o4 = sc.nextLine();

        System.out.print("Correct Option (1-4): ");
        int correct = sc.nextInt();

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO questions VALUES(NULL,?,?,?,?,?,?)"
            );

            ps.setString(1, q);
            ps.setString(2, o1);
            ps.setString(3, o2);
            ps.setString(4, o3);
            ps.setString(5, o4);
            ps.setInt(6, correct);

            ps.executeUpdate();
            System.out.println("Question Added!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= VIEW RESULTS =================
    public void viewResults() {

        try {
            Connection con = DBConnection.getConnection();
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(
                    "SELECT u.username, r.score FROM results r JOIN users u ON r.user_id=u.id"
            );

            System.out.println("\n--- RESULTS ---");
            while (rs.next()) {
                System.out.println(rs.getString(1) + " → Score: " + rs.getInt(2));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= RESET EXAM =================
    public void resetExam() {

        System.out.print("Enter Student Username: ");
        String user = sc.next();

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "DELETE FROM results WHERE user_id=(SELECT id FROM users WHERE username=?)"
            );

            ps.setString(1, user);
            ps.executeUpdate();

            System.out.println("Exam reset successfully!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

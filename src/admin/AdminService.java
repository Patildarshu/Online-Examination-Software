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
            System.out.println("1. Add Question");
            System.out.println("2. View Results");
            System.out.println("3. Reset Student Exam");
            System.out.println("4. Reset Student Password");
            System.out.println("5. Generate Questions using AI");
            System.out.println("6. Add Student");
            System.out.println("7. Exit");

            System.out.print("Enter choice: ");
            int ch = sc.nextInt();

            switch (ch) {
                case 1 -> addQuestion();
                case 2 -> viewResults();
                case 3 -> resetExam();
                case 4 -> new AuthService().resetStudentPassword();
                case 5 -> generateAIQuestions();
                case 6 -> addStudent();
                case 7 -> { return; }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    // ================= ADD QUESTION =================
    public void addQuestion() {

        System.out.print("Enter Subject ID: ");
        int subjectId = sc.nextInt();
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
                    "INSERT INTO questions(subject_id,question,option1,option2,option3,option4,correct_option) " +
                            "VALUES(?,?,?,?,?,?,?)"
            );

            ps.setInt(1, subjectId);
            ps.setString(2, q);
            ps.setString(3, o1);
            ps.setString(4, o2);
            ps.setString(5, o3);
            ps.setString(6, o4);
            ps.setInt(7, correct);

            ps.executeUpdate();
            System.out.println("✅ Question Added!");

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
                    "SELECT u.username, r.subject, r.score " +
                            "FROM results r JOIN users u ON r.user_id=u.id"
            );

            System.out.println("\n--- RESULTS ---");
            while (rs.next()) {
                System.out.println(
                        rs.getString("username") + " | " +
                                rs.getString("subject") + " | Score: " +
                                rs.getInt("score")
                );
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

    // ================= ADD STUDENT =================
    public void addStudent() {

        System.out.print("Student Username: ");
        String username = sc.next();

        String tempPassword = "Temp@" + (int)(Math.random() * 1000);

        AuthService auth = new AuthService();
        String hashed = auth.hashPassword(tempPassword);

        try {
            Connection con = DBConnection.getConnection();
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO users(username,password,role,first_login,active) VALUES(?,?,?,?,?)"
            );

            ps.setString(1, username);
            ps.setString(2, hashed);
            ps.setString(3, "student");
            ps.setBoolean(4, true);
            ps.setBoolean(5, true);

            ps.executeUpdate();

            System.out.println("✅ Student Added");
            System.out.println("Temporary Password: " + tempPassword);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ================= AI QUESTIONS =================
    private void generateAIQuestions() {

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
    }
}

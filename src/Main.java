import auth.AuthService;
import admin.AdminService;
import exam.ExamService;
import result.ResultService;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        AuthService auth = new AuthService();

        System.out.println("1. Login");
        int choice = sc.nextInt();

        if (choice == 1 && auth.login()) {

            // ADMIN FLOW
            if (auth.role.equals("admin")) {
                AdminService admin = new AdminService();
                admin.adminMenu();
            }

            // STUDENT FLOW
            else if (auth.role.equals("student")) {

                ResultService resultService = new ResultService();

                if (resultService.hasAttempted(auth.userId)) {
                    System.out.println("❌ You have already attempted the exam.");
                } else {
                    ExamService exam = new ExamService();
                    int score = exam.startExam();

                    resultService.saveResult(auth.userId, score);
                    System.out.println("Your Score: " + score);
                }
            }
        }
    }
}

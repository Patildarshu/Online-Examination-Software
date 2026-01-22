import auth.AuthService;
import admin.AdminService;
import exam.ExamService;
import result.ResultService;
import student.StudentService;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        AuthService auth = new AuthService();

        System.out.println("1. Login");
        System.out.print("Enter choice: ");
        int choice = sc.nextInt();

        if (choice == 1 && auth.login()) {

            while (true) {

                System.out.println("\n--- USER MENU ---");
                System.out.println("1. Change Password");

                if (auth.role.equals("admin")) {
                    System.out.println("2. Admin Panel");
                } else {
                    System.out.println("2. Give Exam");
                }

                System.out.println("3. Logout");
                System.out.print("Enter choice: ");
                int menuChoice = sc.nextInt();

                // CHANGE PASSWORD
                if (menuChoice == 1) {
                    auth.changePassword(auth.userId);
                }

                // ADMIN / STUDENT ACTION
                else if (menuChoice == 2) {

                    if (auth.role.equals("admin")) {
                        new AdminService().adminMenu();
                    }
                    else {
                        StudentService studentService = new StudentService();
                        studentService.showAvailableSubjects();

                        System.out.print("Enter Subject Name: ");
                        String subject = sc.next();

                        ResultService resultService = new ResultService();

                        if (resultService.hasAttempted(auth.userId, subject)) {
                            System.out.println("❌ You already attempted " + subject);
                        } else {
                            ExamService exam = new ExamService();
                            int score = exam.startExam(subject);

                            resultService.saveResult(auth.userId, subject, score);
                            System.out.println("Your Score: " + score);
                        }
                    }
                }

                // LOGOUT
                else if (menuChoice == 3) {
                    System.out.println("Logged out successfully.");
                    break;
                }
                else {
                    System.out.println("Invalid choice");
                }
            }
        }
    }
}

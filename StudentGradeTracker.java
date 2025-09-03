import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

class Student {
    private String name;
    private int grade;

    public Student(String name, int grade) {
        this.name = name;
        this.grade = grade;
    }

    public String getName() { return name; }
    public int getGrade() { return grade; }

    // Convert numeric grade to Letter Grade
    public String getLetterGrade() {
        int g = grade;
        if (g >= 90) return "A+";
        else if (g >= 80) return "A";
        else if (g >= 70) return "B+";
        else if (g >= 60) return "B";
        else if (g >= 50) return "C";
        else if (g >= 40) return "D";
        else return "F";
    }

    @Override
    public String toString() {
        return name + "," + grade;
    }

    public static Student fromString(String line) {
        String[] parts = line.split(",");
        return new Student(parts[0], Integer.parseInt(parts[1]));
    }
}

public class StudentGradeTracker {
    private static ArrayList<Student> students = new ArrayList<>();
    private static final String FILE_NAME = "students.txt";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        // Load saved data
        loadFromFile();

        int choice;
        do {
            System.out.println("\n===== Student Grade Tracker =====");
            System.out.println("1. Add Student");
            System.out.println("2. View Summary Report");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1:
                    addStudent(scanner);
                    saveToFile(); // save immediately after change
                    break;
                case 2:
                    displaySummary();
                    break;
                case 3:
                    System.out.println("Exiting... Goodbye!");
                    break;
                default:
                    System.out.println("Invalid choice. Try again.");
            }
        } while (choice != 3);

        scanner.close();
    }

    private static void addStudent(Scanner scanner) {
        System.out.print("Enter student name: ");
        String name = scanner.nextLine();
        System.out.print("Enter student grade (0–100): ");
        int grade = scanner.nextInt();

        students.add(new Student(name, grade));
        System.out.println("Student added successfully!");
    }

    private static void displaySummary() {
        if (students.isEmpty()) {
            System.out.println("No student data available.");
            return;
        }

        int total = 0;
        int highest = Integer.MIN_VALUE;
        int lowest = Integer.MAX_VALUE;
        String highestStudent = "";
        String lowestStudent = "";

        System.out.println("\n===== Student Grades =====");
        for (Student s : students) {
            System.out.println(s.getName() + " : " + s.getGrade() + " (" + s.getLetterGrade() + ")");
            total += s.getGrade();

            if (s.getGrade() > highest) {
                highest = s.getGrade();
                highestStudent = s.getName();
            }

            if (s.getGrade() < lowest) {
                lowest = s.getGrade();
                lowestStudent = s.getName();
            }
        }

        double average = (double) total / students.size();

        System.out.println("\n===== Summary Report =====");
        System.out.println("Class Average: " + average);
        System.out.println("Highest Score: " + highest + " (by " + highestStudent + ")");
        System.out.println("Lowest Score: " + lowest + " (by " + lowestStudent + ")");
    }

    private static void saveToFile() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_NAME))) {
            for (Student s : students) {
                writer.println(s.toString());
            }
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    private static void loadFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                students.add(Student.fromString(line));
            }
        } catch (IOException e) {
            System.out.println("No previous data found. Starting fresh.");
        }
    }
}

package com.example;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Scanner;

/**
 * Main application class providing an interactive terminal UI menu loop
 * for the Student Management System.
 */
public class Main {
    private static final StudentService studentService = new StudentService();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("==========================================");
        System.out.println("  Welcome to Student Management System   ");
        System.out.println("==========================================");

        while (running) {
            printMenu();
            System.out.print("Enter your choice: ");

            String choiceInput = readLineOrNull(scanner);
            if (choiceInput == null) {
                // End of Input (EOF) reached
                System.out.println("\nEnd of input detected. Exiting system. Goodbye!");
                break;
            }

            String choice = choiceInput.trim();

            switch (choice) {
                case "1":
                    handleAddStudent(scanner);
                    break;
                case "2":
                    handleViewAllStudents();
                    break;
                case "3":
                    handleSearchStudent(scanner);
                    break;
                case "4":
                    handleDeleteStudent(scanner);
                    break;
                case "5":
                    System.out.println("\nThank you for using Student Management System. Goodbye!");
                    running = false;
                    break;
                case "6":
                    // Extended option for full CRUD support
                    handleUpdateStudent(scanner);
                    break;
                default:
                    System.out.println("Invalid choice. Please select an option from the menu (1-5).\n");
                    break;
            }
        }

        scanner.close();
    }

    /**
     * Displays the interactive main menu.
     */
    private static void printMenu() {
        System.out.println("\n==========================================");
        System.out.println("                MAIN MENU                 ");
        System.out.println("==========================================");
        System.out.println("1. Add Student");
        System.out.println("2. View All");
        System.out.println("3. Search by ID");
        System.out.println("4. Delete Student");
        System.out.println("5. Exit");
        System.out.println("==========================================");
    }

    /**
     * Handles adding a new student with validation.
     */
    private static void handleAddStudent(Scanner scanner) {
        System.out.println("\n--- Add New Student ---");

        Integer id = readPositiveInteger(scanner, "Enter Student ID: ");
        if (id == null) return;

        if (studentService.existsById(id)) {
            System.out.printf("Error: Student with ID %d already exists.\n", id);
            return;
        }

        String name = readNonEmptyString(scanner, "Enter Student Name: ");
        if (name == null) return;

        String course = readNonEmptyString(scanner, "Enter Course Name: ");
        if (course == null) return;

        Double marks = readDoubleInRange(scanner, "Enter Marks (0.0 - 100.0): ", 0.0, 100.0);
        if (marks == null) return;

        Student student = new Student(id, name, course, marks);
        boolean added = studentService.addStudent(student);

        if (added) {
            System.out.println("Student added successfully!");
            System.out.println(student);
        } else {
            System.out.println("Failed to add student. Please try again.");
        }
    }

    /**
     * Handles viewing all student records in a formatted table.
     */
    private static void handleViewAllStudents() {
        System.out.println("\n--- View All Students ---");
        List<Student> students = studentService.getAllStudents();

        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }

        System.out.println("+--------+----------------------+----------------------+-------+-------+");
        System.out.printf("| %-6s | %-20s | %-20s | %-5s | %-5s |\n", "ID", "Name", "Course", "Marks", "Grade");
        System.out.println("+--------+----------------------+----------------------+-------+-------+");
        for (Student s : students) {
            System.out.printf("| %-6d | %-20s | %-20s | %5.2f | %-5s |\n",
                    s.getId(),
                    truncate(s.getName(), 20),
                    truncate(s.getCourse(), 20),
                    s.getMarks(),
                    s.getGrade());
        }
        System.out.println("+--------+----------------------+----------------------+-------+-------+");
        System.out.printf("Total Students: %d\n", students.size());
    }

    /**
     * Handles searching for a student by their ID.
     */
    private static void handleSearchStudent(Scanner scanner) {
        System.out.println("\n--- Search Student by ID ---");
        Integer id = readPositiveInteger(scanner, "Enter Student ID to search: ");
        if (id == null) return;

        Optional<Student> studentOpt = studentService.findStudentById(id);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            System.out.println("Student found:");
            System.out.println("------------------------------------------");
            System.out.printf("ID     : %d\n", student.getId());
            System.out.printf("Name   : %s\n", student.getName());
            System.out.printf("Course : %s\n", student.getCourse());
            System.out.printf("Marks  : %.2f\n", student.getMarks());
            System.out.printf("Grade  : %s\n", student.getGrade());
            System.out.println("------------------------------------------");
        } else {
            System.out.printf("Student with ID %d not found.\n", id);
        }
    }

    /**
     * Handles deleting a student by their ID.
     */
    private static void handleDeleteStudent(Scanner scanner) {
        System.out.println("\n--- Delete Student ---");
        Integer id = readPositiveInteger(scanner, "Enter Student ID to delete: ");
        if (id == null) return;

        boolean deleted = studentService.deleteStudent(id);
        if (deleted) {
            System.out.printf("Student with ID %d deleted successfully.\n", id);
        } else {
            System.out.printf("Student with ID %d not found.\n", id);
        }
    }

    /**
     * Handles updating an existing student record (full CRUD capability).
     */
    private static void handleUpdateStudent(Scanner scanner) {
        System.out.println("\n--- Update Student ---");
        Integer id = readPositiveInteger(scanner, "Enter Student ID to update: ");
        if (id == null) return;

        Optional<Student> studentOpt = studentService.findStudentById(id);
        if (!studentOpt.isPresent()) {
            System.out.printf("Student with ID %d not found.\n", id);
            return;
        }

        Student existing = studentOpt.get();
        System.out.printf("Current details: %s\n", existing);

        String newName = readNonEmptyString(scanner, "Enter New Name: ");
        if (newName == null) return;

        String newCourse = readNonEmptyString(scanner, "Enter New Course: ");
        if (newCourse == null) return;

        Double newMarks = readDoubleInRange(scanner, "Enter New Marks (0.0 - 100.0): ", 0.0, 100.0);
        if (newMarks == null) return;

        boolean updated = studentService.updateStudent(id, newName, newCourse, newMarks);
        if (updated) {
            System.out.printf("Student with ID %d updated successfully.\n", id);
            System.out.println(studentService.getStudentById(id));
        } else {
            System.out.printf("Failed to update student with ID %d.\n", id);
        }
    }

    // ==================== Robust Input Helpers ====================

    /**
     * Safely reads a line from scanner, returning null if EOF is encountered.
     */
    private static String readLineOrNull(Scanner scanner) {
        try {
            if (scanner.hasNextLine()) {
                return scanner.nextLine();
            }
        } catch (NoSuchElementException | IllegalStateException e) {
            return null;
        }
        return null;
    }

    /**
     * Reads a non-empty string with prompt, robust against empty lines.
     */
    private static String readNonEmptyString(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = readLineOrNull(scanner);
            if (line == null) {
                return null;
            }
            line = line.trim();
            if (!line.isEmpty()) {
                return line;
            }
            System.out.println("Input cannot be empty. Please try again.");
        }
    }

    /**
     * Reads a positive integer with prompt, robust against non-numeric and non-positive input.
     */
    private static Integer readPositiveInteger(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String line = readLineOrNull(scanner);
            if (line == null) {
                return null;
            }
            try {
                int val = Integer.parseInt(line.trim());
                if (val <= 0) {
                    System.out.println("Error: Student ID must be a positive integer (> 0). Please try again.");
                    continue;
                }
                return val;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid integer number.");
            }
        }
    }

    /**
     * Reads a double value within a specified range, robust against malformed input.
     */
    private static Double readDoubleInRange(Scanner scanner, String prompt, double min, double max) {
        while (true) {
            System.out.print(prompt);
            String line = readLineOrNull(scanner);
            if (line == null) {
                return null;
            }
            try {
                double value = Double.parseDouble(line.trim());
                if (value < min || value > max) {
                    System.out.printf("Value must be between %.1f and %.1f. Please try again.\n", min, max);
                    continue;
                }
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid numeric value.");
            }
        }
    }

    /**
     * Truncates strings exceeding column width for neat display.
     */
    private static String truncate(String text, int maxLength) {
        if (text == null) return "";
        if (text.length() <= maxLength) return text;
        return text.substring(0, maxLength - 3) + "...";
    }
}

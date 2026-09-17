package com.example;

import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Main application driver providing an interactive CLI menu loop
 * for the Comprehensive Academic Student Management & Analytics System.
 */
public class Main {

    private static final String DATA_DIR = ".";
    private static final StudentService studentService = new StudentService(DATA_DIR);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        System.out.println("===============================================================");
        System.out.println("       STUDENT MANAGEMENT & ACADEMIC ANALYTICS SYSTEM (CLI)    ");
        System.out.println("===============================================================");

        int sCount = studentService.getStudentCount();
        int cCount = studentService.getCourseCount();
        int eCount = studentService.getEnrollmentCount();
        System.out.printf("Persistent Storage: Loaded %d student(s), %d course(s), %d enrollment(s).\n", sCount, cCount, eCount);

        while (running) {
            printMainMenu();
            System.out.print("Enter your choice (1-11): ");

            String choiceInput = ValidationUtils.readLineOrNull(scanner);
            if (choiceInput == null) {
                studentService.syncAllData();
                System.out.println("\nEnd of input detected. Academic data synced. Goodbye!");
                break;
            }

            String choice = choiceInput.trim();

            switch (choice) {
                case "1":
                    handleRegisterUndergraduate(scanner);
                    break;
                case "2":
                    handleRegisterGraduate(scanner);
                    break;
                case "3":
                    handleViewAllStudents();
                    break;
                case "4":
                    handleViewDeansList();
                    break;
                case "5":
                    handleViewProbationList();
                    break;
                case "6":
                    handleAddCourse(scanner);
                    break;
                case "7":
                    handleViewCourseCatalog();
                    break;
                case "8":
                    handleEnrollStudent(scanner);
                    break;
                case "9":
                    handleGenerateTranscript(scanner);
                    break;
                case "10":
                    handleDeleteStudent(scanner);
                    break;
                case "11":
                    studentService.syncAllData();
                    System.out.println("\nAll academic records safely saved to storage. Thank you for using the system!");
                    running = false;
                    break;
                default:
                    System.out.println("Invalid selection. Please choose an option from 1 to 11.\n");
                    break;
            }
        }

        scanner.close();
    }

    private static void printMainMenu() {
        System.out.println("\n===============================================================");
        System.out.println("                          MAIN MENU                            ");
        System.out.println("===============================================================");
        System.out.println("  1. Register Undergraduate Student (B.Tech / B.S.)");
        System.out.println("  2. Register Graduate Student (M.Tech / M.S. / Ph.D.)");
        System.out.println("  3. View All Registered Students");
        System.out.println("  4. View Dean's Honor List (Merit Ranking: GPA >= 3.70)");
        System.out.println("  5. View Academic Probation List (GPA < 2.00)");
        System.out.println("  6. Add New Course Offering");
        System.out.println("  7. View Course Catalog");
        System.out.println("  8. Enroll Student in Course & Record Grade");
        System.out.println("  9. Generate Official Academic Transcript");
        System.out.println(" 10. Delete Student Record");
        System.out.println(" 11. Exit System");
        System.out.println("===============================================================");
    }

    // ==================== 1. Register Undergraduate ====================
    private static void handleRegisterUndergraduate(Scanner scanner) {
        System.out.println("\n--- [1] Register Undergraduate Student ---");
        Integer id = ValidationUtils.readPositiveInt(scanner, "Enter Student ID: ");
        if (id == null) return;

        if (studentService.findStudentById(id).isPresent()) {
            System.out.printf("Error: Student with ID %d already exists.\n", id);
            return;
        }

        String name = ValidationUtils.readNonEmptyString(scanner, "Enter Student Full Name: ");
        if (name == null) return;

        String email = ValidationUtils.readEmail(scanner, "Enter Email Address: ");
        if (email == null) return;

        String phone = ValidationUtils.readPhone(scanner, "Enter Phone Number: ");
        if (phone == null) return;

        String dept = ValidationUtils.readNonEmptyString(scanner, "Enter Department (e.g. CSE, ECE, Mechanical): ");
        if (dept == null) return;

        Double gpa = ValidationUtils.readDoubleInRange(scanner, "Enter Cumulative GPA (0.00 - 4.00): ", 0.0, 4.0);
        if (gpa == null) return;

        Integer sem = ValidationUtils.readPositiveInt(scanner, "Enter Current Semester (1 - 8): ");
        if (sem == null) return;

        String minor = ValidationUtils.readNonEmptyString(scanner, "Enter Minor Subject (or 'None'): ");
        if (minor == null) return;

        String capstone = ValidationUtils.readNonEmptyString(scanner, "Enter Capstone Project Title (or 'Pending'): ");
        if (capstone == null) return;

        UndergraduateStudent ug = new UndergraduateStudent(id, name, email, phone, dept, gpa, sem, minor, capstone);
        boolean added = studentService.registerStudent(ug);

        if (added) {
            System.out.println("Undergraduate student successfully registered and persisted!");
            System.out.println(ug);
        } else {
            System.out.println("Failed to register student.");
        }
    }

    // ==================== 2. Register Graduate ====================
    private static void handleRegisterGraduate(Scanner scanner) {
        System.out.println("\n--- [2] Register Graduate Research Student ---");
        Integer id = ValidationUtils.readPositiveInt(scanner, "Enter Student ID: ");
        if (id == null) return;

        if (studentService.findStudentById(id).isPresent()) {
            System.out.printf("Error: Student with ID %d already exists.\n", id);
            return;
        }

        String name = ValidationUtils.readNonEmptyString(scanner, "Enter Student Full Name: ");
        if (name == null) return;

        String email = ValidationUtils.readEmail(scanner, "Enter Email Address: ");
        if (email == null) return;

        String phone = ValidationUtils.readPhone(scanner, "Enter Phone Number: ");
        if (phone == null) return;

        String dept = ValidationUtils.readNonEmptyString(scanner, "Enter Department: ");
        if (dept == null) return;

        Double gpa = ValidationUtils.readDoubleInRange(scanner, "Enter Cumulative GPA (0.00 - 4.00): ", 0.0, 4.0);
        if (gpa == null) return;

        String advisor = ValidationUtils.readNonEmptyString(scanner, "Enter Thesis Advisor Name: ");
        if (advisor == null) return;

        String thesis = ValidationUtils.readNonEmptyString(scanner, "Enter Research / Thesis Title: ");
        if (thesis == null) return;

        String assistantship = ValidationUtils.readNonEmptyString(scanner, "Enter Assistantship (Research Assistant/Teaching Assistant/None): ");
        if (assistantship == null) return;

        GraduateStudent grad = new GraduateStudent(id, name, email, phone, dept, gpa, advisor, thesis, assistantship);
        boolean added = studentService.registerStudent(grad);

        if (added) {
            System.out.println("Graduate student successfully registered and persisted!");
            System.out.println(grad);
        } else {
            System.out.println("Failed to register student.");
        }
    }

    // ==================== 3. View All Students ====================
    private static void handleViewAllStudents() {
        System.out.println("\n--- [3] View All Registered Students ---");
        List<Student> students = studentService.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No student records found.");
            return;
        }

        System.out.println("+------+----------------------+----------------------+-------+-------+-------------------------+----------------------+");
        System.out.printf("| %-4s | %-20s | %-20s | %-5s | %-5s | %-23s | %-20s |\n",
                "ID", "Name", "Department", "GPA", "Grade", "Program Level", "Academic Standing");
        System.out.println("+------+----------------------+----------------------+-------+-------+-------------------------+----------------------+");
        for (Student s : students) {
            System.out.printf("| %-4d | %-20s | %-20s | %5.2f | %-5s | %-23s | %-20s |\n",
                    s.getId(),
                    ValidationUtils.truncate(s.getName(), 20),
                    ValidationUtils.truncate(s.getDepartment(), 20),
                    s.getGpa(),
                    s.getEquivalentGrade(),
                    ValidationUtils.truncate(s.getRoleDescription(), 23),
                    ValidationUtils.truncate(s.getAcademicStanding().name(), 20));
        }
        System.out.println("+------+----------------------+----------------------+-------+-------+-------------------------+----------------------+");
        System.out.printf("Total Enrolled Students: %d\n", students.size());
    }

    // ==================== 4. View Dean's Honor List ====================
    private static void handleViewDeansList() {
        System.out.println("\n--- [4] Dean's Honor List (Merit Ranking: GPA >= 3.70) ---");
        System.out.println("Top academic performers sorted dynamically by GPA in descending order:");
        List<Student> list = studentService.getDeansHonorList();
        if (list.isEmpty()) {
            System.out.println("No students currently qualify for Dean's List (Minimum GPA required: 3.70).");
            return;
        }

        System.out.println("+------+----------------------+----------------------+-------+-------+---------------------------------------+");
        System.out.printf("| %-4s | %-20s | %-20s | %-5s | %-5s | %-37s |\n",
                "ID", "Honors Scholar Name", "Department", "GPA", "Grade", "Milestone / Academic Achievement");
        System.out.println("+------+----------------------+----------------------+-------+-------+---------------------------------------+");
        for (Student s : list) {
            System.out.printf("| %-4d | %-20s | %-20s | %5.2f | %-5s | %-37s |\n",
                    s.getId(),
                    ValidationUtils.truncate(s.getName(), 20),
                    ValidationUtils.truncate(s.getDepartment(), 20),
                    s.getGpa(),
                    s.getEquivalentGrade(),
                    ValidationUtils.truncate(s.getGraduationMilestone(), 37));
        }
        System.out.println("+------+----------------------+----------------------+-------+-------+---------------------------------------+");
        System.out.printf("Total Dean's Honor Scholars: %d\n", list.size());
    }

    // ==================== 5. View Probation List ====================
    private static void handleViewProbationList() {
        System.out.println("\n--- [5] Academic Probation & Intervention List ---");
        List<Student> list = studentService.getProbationList();
        if (list.isEmpty()) {
            System.out.println("Excellent news! Zero students currently on academic probation.");
            return;
        }

        System.out.println("+------+----------------------+----------------------+-------+---------------------------------------+");
        System.out.printf("| %-4s | %-20s | %-20s | %-5s | %-37s |\n",
                "ID", "Student Name", "Department", "GPA", "Intervention Status");
        System.out.println("+------+----------------------+----------------------+-------+---------------------------------------+");
        for (Student s : list) {
            System.out.printf("| %-4d | %-20s | %-20s | %5.2f | %-37s |\n",
                    s.getId(),
                    ValidationUtils.truncate(s.getName(), 20),
                    ValidationUtils.truncate(s.getDepartment(), 20),
                    s.getGpa(),
                    ValidationUtils.truncate(s.getAcademicStanding().getDescription(), 37));
        }
        System.out.println("+------+----------------------+----------------------+-------+---------------------------------------+");
        System.out.printf("Students Requiring Academic Advising: %d\n", list.size());
    }

    // ==================== 6. Add Course ====================
    private static void handleAddCourse(Scanner scanner) {
        System.out.println("\n--- [6] Add New Course Offering ---");
        String code = ValidationUtils.readNonEmptyString(scanner, "Enter Course Code (e.g. CSE2005): ");
        if (code == null) return;
        code = code.trim().toUpperCase();

        if (studentService.findCourseByCode(code).isPresent()) {
            System.out.printf("Error: Course code %s already exists in catalog.\n", code);
            return;
        }

        String title = ValidationUtils.readNonEmptyString(scanner, "Enter Course Title: ");
        if (title == null) return;

        Integer credits = ValidationUtils.readPositiveInt(scanner, "Enter Credit Units (1 - 5): ");
        if (credits == null) return;

        String dept = ValidationUtils.readNonEmptyString(scanner, "Enter Department: ");
        if (dept == null) return;

        String instructor = ValidationUtils.readNonEmptyString(scanner, "Enter Lead Instructor Name: ");
        if (instructor == null) return;

        Course course = new Course(code, title, credits, dept, instructor);
        boolean added = studentService.addCourse(course);

        if (added) {
            System.out.println("Course added successfully to institutional catalog!");
            System.out.println(course);
        } else {
            System.out.println("Failed to add course.");
        }
    }

    // ==================== 7. View Course Catalog ====================
    private static void handleViewCourseCatalog() {
        System.out.println("\n--- [7] Institutional Course Catalog ---");
        List<Course> courses = studentService.getAllCourses();
        if (courses.isEmpty()) {
            System.out.println("No course offerings registered in catalog.");
            return;
        }

        System.out.println("+------------+--------------------------------+---------+----------------------+----------------------+");
        System.out.printf("| %-10s | %-30s | %-7s | %-20s | %-20s |\n",
                "CourseCode", "Course Title", "Credits", "Department", "Lead Instructor");
        System.out.println("+------------+--------------------------------+---------+----------------------+----------------------+");
        for (Course c : courses) {
            System.out.printf("| %-10s | %-30s | %-7d | %-20s | %-20s |\n",
                    c.getCourseCode(),
                    ValidationUtils.truncate(c.getCourseTitle(), 30),
                    c.getCredits(),
                    ValidationUtils.truncate(c.getDepartment(), 20),
                    ValidationUtils.truncate(c.getInstructorName(), 20));
        }
        System.out.println("+------------+--------------------------------+---------+----------------------+----------------------+");
        System.out.printf("Total Courses Available: %d\n", courses.size());
    }

    // ==================== 8. Enroll Student ====================
    private static void handleEnrollStudent(Scanner scanner) {
        System.out.println("\n--- [8] Enroll Student in Course & Record Grade ---");
        Integer enrollId = ValidationUtils.readPositiveInt(scanner, "Enter New Enrollment ID: ");
        if (enrollId == null) return;

        Integer studentId = ValidationUtils.readPositiveInt(scanner, "Enter Student ID: ");
        if (studentId == null) return;
        Optional<Student> stOpt = studentService.findStudentById(studentId);
        if (!stOpt.isPresent()) {
            System.out.printf("Error: Student #%d does not exist. Please register student first.\n", studentId);
            return;
        }

        String courseCode = ValidationUtils.readNonEmptyString(scanner, "Enter Course Code (e.g. CSE2005): ");
        if (courseCode == null) return;
        courseCode = courseCode.trim().toUpperCase();

        Optional<Course> crsOpt = studentService.findCourseByCode(courseCode);
        if (!crsOpt.isPresent()) {
            System.out.printf("Error: Course code %s not found in catalog.\n", courseCode);
            return;
        }

        Double marks = ValidationUtils.readDoubleInRange(scanner, "Enter Final Course Marks (0.0 - 100.0): ", 0.0, 100.0);
        if (marks == null) return;

        String term = ValidationUtils.readNonEmptyString(scanner, "Enter Academic Term (e.g. Fall 2026): ");
        if (term == null) return;

        Enrollment enrollment = new Enrollment(enrollId, studentId, courseCode, marks, term);
        boolean success = studentService.enrollStudent(enrollment);

        if (success) {
            System.out.println("Student successfully enrolled and grade computed!");
            System.out.println(enrollment);
        } else {
            System.out.println("Failed to enroll: Either enrollment ID already exists or student is already enrolled in this course.");
        }
    }

    // ==================== 9. Generate Transcript ====================
    private static void handleGenerateTranscript(Scanner scanner) {
        System.out.println("\n--- [9] Generate Official Academic Transcript ---");
        Integer studentId = ValidationUtils.readPositiveInt(scanner, "Enter Student ID: ");
        if (studentId == null) return;

        String transcript = studentService.generateAcademicTranscript(studentId);
        System.out.println(transcript);
    }

    // ==================== 10. Delete Student ====================
    private static void handleDeleteStudent(Scanner scanner) {
        System.out.println("\n--- [10] Delete Student Record ---");
        Integer studentId = ValidationUtils.readPositiveInt(scanner, "Enter Student ID to delete: ");
        if (studentId == null) return;

        boolean deleted = studentService.deleteStudent(studentId);
        if (deleted) {
            System.out.printf("Student #%d successfully removed from institutional registry.\n", studentId);
        } else {
            System.out.printf("Error: Student #%d not found.\n", studentId);
        }
    }
}

package com.example;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Service orchestrator managing academic CRUD logic, Dean's List merit ranking,
 * course enrollments, grade computation, and CSV disk synchronization.
 */
public class StudentService {

    private final Map<Integer, Student> studentMap;
    private final Map<String, Course> courseMap;
    private final Map<Integer, Enrollment> enrollmentMap;
    private final StorageManager storageManager;

    private static final String STUDENTS_FILE = "students.csv";
    private static final String COURSES_FILE = "courses.csv";
    private static final String ENROLLMENTS_FILE = "enrollments.csv";

    public StudentService(String dataDirectory) {
        this.storageManager = new StorageManager(dataDirectory);
        this.studentMap = new LinkedHashMap<>();
        this.courseMap = new LinkedHashMap<>();
        this.enrollmentMap = new LinkedHashMap<>();
        loadAllData();
    }

    public void loadAllData() {
        studentMap.clear();
        courseMap.clear();
        enrollmentMap.clear();

        studentMap.putAll(storageManager.loadStudents(STUDENTS_FILE));
        courseMap.putAll(storageManager.loadCourses(COURSES_FILE));
        enrollmentMap.putAll(storageManager.loadEnrollments(ENROLLMENTS_FILE));
    }

    public void syncAllData() {
        storageManager.saveStudents(STUDENTS_FILE, studentMap.values());
        storageManager.saveCourses(COURSES_FILE, courseMap.values());
        storageManager.saveEnrollments(ENROLLMENTS_FILE, enrollmentMap.values());
    }

    // ==================== Student Management ====================

    public boolean registerStudent(Student student) {
        if (student == null || studentMap.containsKey(student.getId())) {
            return false;
        }
        studentMap.put(student.getId(), student);
        storageManager.saveStudents(STUDENTS_FILE, studentMap.values());
        return true;
    }

    public List<Student> getAllStudents() {
        return new ArrayList<>(studentMap.values());
    }

    public Optional<Student> findStudentById(int id) {
        return Optional.ofNullable(studentMap.get(id));
    }

    public boolean deleteStudent(int id) {
        Student removed = studentMap.remove(id);
        if (removed != null) {
            storageManager.saveStudents(STUDENTS_FILE, studentMap.values());
            return true;
        }
        return false;
    }

    // ==================== Academic Analytics & Merit Ranking ====================

    /**
     * Generates the Dean's Honor List containing top academic performers (GPA >= 3.70)
     * sorted strictly in descending order of GPA using Java stream comparators.
     */
    public List<Student> getDeansHonorList() {
        return studentMap.values().stream()
                .filter(s -> s.getAcademicStanding() == AcademicStanding.DEANS_LIST)
                .sorted(Comparator.comparingDouble(Student::getGpa).reversed()
                        .thenComparing(Student::getName))
                .collect(Collectors.toList());
    }

    /**
     * Retrieves students on Academic Warning or Probation (GPA < 2.00)
     * requiring institutional intervention.
     */
    public List<Student> getProbationList() {
        return studentMap.values().stream()
                .filter(s -> s.getGpa() < 2.00)
                .sorted(Comparator.comparingDouble(Student::getGpa))
                .collect(Collectors.toList());
    }

    // ==================== Course Management ====================

    public boolean addCourse(Course course) {
        if (course == null || courseMap.containsKey(course.getCourseCode())) {
            return false;
        }
        courseMap.put(course.getCourseCode(), course);
        storageManager.saveCourses(COURSES_FILE, courseMap.values());
        return true;
    }

    public List<Course> getAllCourses() {
        return new ArrayList<>(courseMap.values());
    }

    public Optional<Course> findCourseByCode(String code) {
        if (code == null) return Optional.empty();
        return Optional.ofNullable(courseMap.get(code.trim().toUpperCase()));
    }

    // ==================== Enrollment & Transcript Operations ====================

    public boolean enrollStudent(Enrollment enrollment) {
        if (enrollment == null || enrollmentMap.containsKey(enrollment.getEnrollmentId())) {
            return false;
        }
        if (!studentMap.containsKey(enrollment.getStudentId())) {
            return false;
        }
        if (!courseMap.containsKey(enrollment.getCourseCode())) {
            return false;
        }

        // Prevent duplicate enrollment in the same course
        boolean alreadyEnrolled = enrollmentMap.values().stream()
                .anyMatch(e -> e.getStudentId() == enrollment.getStudentId()
                        && e.getCourseCode().equalsIgnoreCase(enrollment.getCourseCode()));
        if (alreadyEnrolled) {
            return false;
        }

        enrollmentMap.put(enrollment.getEnrollmentId(), enrollment);
        storageManager.saveEnrollments(ENROLLMENTS_FILE, enrollmentMap.values());
        return true;
    }

    public List<Enrollment> getAllEnrollments() {
        return new ArrayList<>(enrollmentMap.values());
    }

    public List<Enrollment> getEnrollmentsForStudent(int studentId) {
        return enrollmentMap.values().stream()
                .filter(e -> e.getStudentId() == studentId)
                .collect(Collectors.toList());
    }

    /**
     * Generates a formal, formatted institutional academic grade transcript.
     */
    public String generateAcademicTranscript(int studentId) {
        Student student = studentMap.get(studentId);
        if (student == null) {
            return "Error: Student not found in records.";
        }

        List<Enrollment> enrollments = getEnrollmentsForStudent(studentId);

        StringBuilder sb = new StringBuilder();
        sb.append("\n===============================================================\n");
        sb.append("                  OFFICIAL ACADEMIC TRANSCRIPT                 \n");
        sb.append("===============================================================\n");
        sb.append(String.format("Student ID    : #%d\n", student.getId()));
        sb.append(String.format("Student Name  : %s\n", student.getName()));
        sb.append(String.format("Program Type  : %s\n", student.getRoleDescription()));
        sb.append(String.format("Department    : %s\n", student.getDepartment()));
        sb.append(String.format("Cumulative GPA: %.2f / 4.00 (Equivalent: %s)\n", student.getGpa(), student.getEquivalentGrade()));
        sb.append(String.format("Academic Standing: %s\n", student.getAcademicStanding().getDescription()));
        sb.append(String.format("Milestone     : %s\n", student.getGraduationMilestone()));
        sb.append("---------------------------------------------------------------\n");
        sb.append(String.format("| %-10s | %-24s | %-5s | %-5s |\n", "Course", "Course Title", "Marks", "Grade"));
        sb.append("---------------------------------------------------------------\n");

        if (enrollments.isEmpty()) {
            sb.append("| No registered course enrollments found on file.             |\n");
        } else {
            for (Enrollment e : enrollments) {
                Course c = courseMap.get(e.getCourseCode());
                String title = (c != null) ? c.getCourseTitle() : "General Elective";
                sb.append(String.format("| %-10s | %-24s | %5.2f | %-5s |\n",
                        e.getCourseCode(),
                        ValidationUtils.truncate(title, 24),
                        e.getMarks(),
                        e.getGrade()));
            }
        }
        sb.append("===============================================================\n");
        return sb.toString();
    }

    public int getStudentCount() {
        return studentMap.size();
    }

    public int getCourseCount() {
        return courseMap.size();
    }

    public int getEnrollmentCount() {
        return enrollmentMap.size();
    }
}

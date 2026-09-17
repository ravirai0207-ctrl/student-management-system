package com.example;

import java.util.Objects;

/**
 * Association entity representing a student's enrollment and grade record
 * within a specific academic course offering.
 */
public class Enrollment {
    private int enrollmentId;
    private int studentId;
    private String courseCode;
    private double marks; // 0.0 to 100.0
    private String grade;
    private String semesterTerm;

    public Enrollment() {
    }

    public Enrollment(int enrollmentId, int studentId, String courseCode, double marks, String semesterTerm) {
        this.enrollmentId = enrollmentId;
        this.studentId = studentId;
        this.courseCode = courseCode != null ? courseCode.trim().toUpperCase() : "";
        this.semesterTerm = semesterTerm;
        setMarks(marks);
    }

    public int getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(int enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public int getStudentId() {
        return studentId;
    }

    public void setStudentId(int studentId) {
        this.studentId = studentId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode != null ? courseCode.trim().toUpperCase() : "";
    }

    public double getMarks() {
        return marks;
    }

    public void setMarks(double marks) {
        this.marks = Math.max(0.0, Math.min(100.0, marks));
        this.grade = calculateGrade(this.marks);
    }

    public String getGrade() {
        return grade;
    }

    public String getSemesterTerm() {
        return semesterTerm;
    }

    public void setSemesterTerm(String semesterTerm) {
        this.semesterTerm = semesterTerm;
    }

    /**
     * Standard institutional marks-to-grade evaluation rubric.
     */
    public static String calculateGrade(double score) {
        if (score >= 90.0) return "A";
        if (score >= 80.0) return "B";
        if (score >= 70.0) return "C";
        if (score >= 60.0) return "D";
        return "F";
    }

    public static double gradeToPoints(String gr) {
        if (gr == null) return 0.0;
        switch (gr.toUpperCase()) {
            case "A": return 4.0;
            case "B": return 3.0;
            case "C": return 2.0;
            case "D": return 1.0;
            default: return 0.0;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Enrollment that = (Enrollment) o;
        return enrollmentId == that.enrollmentId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(enrollmentId);
    }

    @Override
    public String toString() {
        return String.format("Enrollment [ID=%d, StudentID=%d, Course='%s', Marks=%.2f, Grade='%s', Term='%s']",
                enrollmentId, studentId, courseCode, marks, grade, semesterTerm);
    }
}

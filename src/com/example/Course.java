package com.example;

import java.util.Objects;

/**
 * Model class representing an Academic Course offering within the institution.
 */
public class Course {
    private String courseCode;
    private String courseTitle;
    private int credits;
    private String department;
    private String instructorName;

    public Course() {
    }

    public Course(String courseCode, String courseTitle, int credits, String department, String instructorName) {
        this.courseCode = courseCode != null ? courseCode.trim().toUpperCase() : "";
        this.courseTitle = courseTitle;
        this.credits = credits;
        this.department = department;
        this.instructorName = instructorName;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode != null ? courseCode.trim().toUpperCase() : "";
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Course course = (Course) o;
        return Objects.equals(courseCode, course.courseCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(courseCode);
    }

    @Override
    public String toString() {
        return String.format("Course [Code='%s', Title='%s', Credits=%d, Dept='%s', Instructor='%s']",
                courseCode, courseTitle, credits, department, instructorName);
    }
}

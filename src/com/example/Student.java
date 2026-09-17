package com.example;

import java.util.Objects;

/**
 * Model class representing a Student in the Student Management System.
 */
public class Student {
    private int id;
    private String name;
    private String course;
    private double marks;

    /**
     * Default constructor.
     */
    public Student() {
    }

    /**
     * Parameterized constructor.
     *
     * @param id     unique student identifier
     * @param name   student's full name
     * @param course enrolled course name
     * @param marks  marks scored (0.0 to 100.0)
     */
    public Student(int id, String name, String course, double marks) {
        this.id = id;
        this.name = name;
        this.course = course;
        this.marks = marks;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }

    public double getMarks() {
        return marks;
    }

    public void setMarks(double marks) {
        this.marks = marks;
    }

    /**
     * Calculates letter grade based on marks.
     *
     * @return letter grade (A, B, C, D, or F)
     */
    public String getGrade() {
        if (marks >= 90.0) {
            return "A";
        } else if (marks >= 80.0) {
            return "B";
        } else if (marks >= 70.0) {
            return "C";
        } else if (marks >= 60.0) {
            return "D";
        } else {
            return "F";
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return id == student.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Student [ID=%d, Name='%s', Course='%s', Marks=%.2f, Grade='%s']",
                id, name, course, marks, getGrade());
    }
}

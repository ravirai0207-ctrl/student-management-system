package com.example;

/**
 * Core entity model representing a Student enrolled in the institution.
 * Inherits fundamental identity fields from Person and encapsulates academic metrics.
 */
public abstract class Student extends Person {
    private String department;
    private double gpa; // 0.00 to 4.00
    private AcademicStanding academicStanding;

    public Student() {
        super();
        this.academicStanding = AcademicStanding.GOOD_STANDING;
    }

    public Student(int id, String name, String email, String phone, String department, double gpa) {
        super(id, name, email, phone);
        this.department = department;
        setGpa(gpa);
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = Math.max(0.0, Math.min(4.0, gpa));
        this.academicStanding = AcademicStanding.evaluate(this.gpa);
    }

    public AcademicStanding getAcademicStanding() {
        return academicStanding;
    }

    public void setAcademicStanding(AcademicStanding academicStanding) {
        this.academicStanding = academicStanding != null ? academicStanding : AcademicStanding.evaluate(this.gpa);
    }

    /**
     * Calculates letter grade based on standard 4.0 GPA conversion.
     */
    public String getEquivalentGrade() {
        if (gpa >= 3.70) return "A";
        if (gpa >= 3.30) return "A-";
        if (gpa >= 3.00) return "B+";
        if (gpa >= 2.70) return "B";
        if (gpa >= 2.30) return "B-";
        if (gpa >= 2.00) return "C+";
        if (gpa >= 1.70) return "C";
        if (gpa >= 1.00) return "D";
        return "F";
    }

    /**
     * Polymorphic method implemented by Undergraduate and Graduate student models
     * to describe specific graduation milestone requirements.
     */
    public abstract String getGraduationMilestone();

    @Override
    public String toString() {
        return String.format("Student [ID=%d, Name='%s', Dept='%s', GPA=%.2f (%s), Standing=%s]",
                getId(), getName(), department, gpa, getEquivalentGrade(), academicStanding.name());
    }
}

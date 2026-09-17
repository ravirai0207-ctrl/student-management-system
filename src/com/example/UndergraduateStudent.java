package com.example;

/**
 * Subclass representing an Undergraduate Student pursuing a baccalaureate degree.
 * Demonstrates Object-Oriented inheritance, specialization, and polymorphism.
 */
public class UndergraduateStudent extends Student {
    private int semester;
    private String minorSubject;
    private String capstoneProjectTitle;

    public UndergraduateStudent() {
        super();
    }

    public UndergraduateStudent(int id, String name, String email, String phone, String department,
                                double gpa, int semester, String minorSubject, String capstoneProjectTitle) {
        super(id, name, email, phone, department, gpa);
        this.semester = semester;
        this.minorSubject = minorSubject != null ? minorSubject : "None";
        this.capstoneProjectTitle = capstoneProjectTitle != null ? capstoneProjectTitle : "Pending Proposal";
    }

    public int getSemester() {
        return semester;
    }

    public void setSemester(int semester) {
        this.semester = semester;
    }

    public String getMinorSubject() {
        return minorSubject;
    }

    public void setMinorSubject(String minorSubject) {
        this.minorSubject = minorSubject;
    }

    public String getCapstoneProjectTitle() {
        return capstoneProjectTitle;
    }

    public void setCapstoneProjectTitle(String capstoneProjectTitle) {
        this.capstoneProjectTitle = capstoneProjectTitle;
    }

    @Override
    public String getRoleDescription() {
        return "Undergraduate (Semester " + semester + ")";
    }

    @Override
    public String getGraduationMilestone() {
        return "Capstone: " + capstoneProjectTitle;
    }

    @Override
    public String toString() {
        return String.format("UndergraduateStudent [ID=%d, Name='%s', Dept='%s', GPA=%.2f, Sem=%d, Minor='%s', Capstone='%s', Standing=%s]",
                getId(), getName(), getDepartment(), getGpa(), semester, minorSubject, capstoneProjectTitle, getAcademicStanding().name());
    }
}

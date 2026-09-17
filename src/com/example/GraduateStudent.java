package com.example;

/**
 * Subclass representing a Graduate Student engaged in specialized research or post-graduate study.
 * Demonstrates Object-Oriented inheritance, specialization, and polymorphism.
 */
public class GraduateStudent extends Student {
    private String researchAdvisor;
    private String thesisTitle;
    private String assistantshipType; // e.g. "Research Assistant", "Teaching Assistant", "Self-Funded"

    public GraduateStudent() {
        super();
    }

    public GraduateStudent(int id, String name, String email, String phone, String department,
                           double gpa, String researchAdvisor, String thesisTitle, String assistantshipType) {
        super(id, name, email, phone, department, gpa);
        this.researchAdvisor = researchAdvisor != null ? researchAdvisor : "TBD";
        this.thesisTitle = thesisTitle != null ? thesisTitle : "Proposal Defense Pending";
        this.assistantshipType = assistantshipType != null ? assistantshipType : "None";
    }

    public String getResearchAdvisor() {
        return researchAdvisor;
    }

    public void setResearchAdvisor(String researchAdvisor) {
        this.researchAdvisor = researchAdvisor;
    }

    public String getThesisTitle() {
        return thesisTitle;
    }

    public void setThesisTitle(String thesisTitle) {
        this.thesisTitle = thesisTitle;
    }

    public String getAssistantshipType() {
        return assistantshipType;
    }

    public void setAssistantshipType(String assistantshipType) {
        this.assistantshipType = assistantshipType;
    }

    @Override
    public String getRoleDescription() {
        return "Graduate / Research Fellow (" + assistantshipType + ")";
    }

    @Override
    public String getGraduationMilestone() {
        return "Thesis: " + thesisTitle + " (Advisor: Prof. " + researchAdvisor + ")";
    }

    @Override
    public String toString() {
        return String.format("GraduateStudent [ID=%d, Name='%s', Dept='%s', GPA=%.2f, Advisor='%s', Thesis='%s', Assistantship='%s', Standing=%s]",
                getId(), getName(), getDepartment(), getGpa(), researchAdvisor, thesisTitle, assistantshipType, getAcademicStanding().name());
    }
}

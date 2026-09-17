package com.example;

/**
 * Enumeration representing a student's institutional academic standing
 * determined dynamically based on cumulative GPA.
 */
public enum AcademicStanding {
    DEANS_LIST("Dean's Honor List / High Distinction", 3.70),
    GOOD_STANDING("Good Standing / Normal Progress", 2.00),
    ACADEMIC_WARNING("Academic Warning / Needs Improvement", 1.50),
    PROBATION("Academic Probation / Intervention Required", 0.00);

    private final String description;
    private final double minGpaThreshold;

    AcademicStanding(String description, double minGpaThreshold) {
        this.description = description;
        this.minGpaThreshold = minGpaThreshold;
    }

    public String getDescription() {
        return description;
    }

    public double getMinGpaThreshold() {
        return minGpaThreshold;
    }

    /**
     * Determines the appropriate academic standing for a given cumulative GPA (0.0 to 4.0).
     */
    public static AcademicStanding evaluate(double gpa) {
        if (gpa >= DEANS_LIST.minGpaThreshold) {
            return DEANS_LIST;
        } else if (gpa >= GOOD_STANDING.minGpaThreshold) {
            return GOOD_STANDING;
        } else if (gpa >= ACADEMIC_WARNING.minGpaThreshold) {
            return ACADEMIC_WARNING;
        } else {
            return PROBATION;
        }
    }

    public static AcademicStanding fromString(String text) {
        if (text == null) return GOOD_STANDING;
        try {
            return AcademicStanding.valueOf(text.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            return GOOD_STANDING;
        }
    }
}

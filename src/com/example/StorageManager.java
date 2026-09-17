package com.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Dedicated persistence engine responsible for serializing and recovering
 * Students (Undergraduate & Graduate), Courses, and Enrollments to CSV disk files.
 */
public class StorageManager {

    private final String dataDirectory;

    public StorageManager(String dataDirectory) {
        this.dataDirectory = dataDirectory;
        ensureDirectoryExists();
    }

    private void ensureDirectoryExists() {
        File dir = new File(dataDirectory);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    private File getFile(String filename) {
        return new File(dataDirectory, filename);
    }

    // ==================== Students CSV ====================

    public Map<Integer, Student> loadStudents(String filename) {
        Map<Integer, Student> map = new LinkedHashMap<>();
        File file = getFile(filename);
        if (!file.exists()) return map;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Header
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] p = parseCsvLine(line);
                if (p.length >= 7) {
                    try {
                        String type = p[0].trim().toUpperCase();
                        int id = Integer.parseInt(p[1].trim());
                        String name = p[2].trim();
                        String email = p[3].trim();
                        String phone = p[4].trim();
                        String dept = p[5].trim();
                        double gpa = Double.parseDouble(p[6].trim());

                        if ("GRAD".equals(type) && p.length >= 10) {
                            String advisor = p[7].trim();
                            String thesis = p[8].trim();
                            String assistantship = p[9].trim();
                            map.put(id, new GraduateStudent(id, name, email, phone, dept, gpa, advisor, thesis, assistantship));
                        } else {
                            int sem = (p.length >= 8) ? Integer.parseInt(p[7].trim()) : 1;
                            String minor = (p.length >= 9) ? p[8].trim() : "None";
                            String capstone = (p.length >= 10) ? p[9].trim() : "TBD";
                            map.put(id, new UndergraduateStudent(id, name, email, phone, dept, gpa, sem, minor, capstone));
                        }
                    } catch (Exception ignored) {
                        // Skip corrupt lines gracefully
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Warning: Unable to read students file: " + e.getMessage());
        }
        return map;
    }

    public void saveStudents(String filename, Iterable<Student> students) {
        File file = getFile(filename);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("Type,ID,Name,Email,Phone,Department,GPA,Field1,Field2,Field3");
            writer.newLine();
            for (Student s : students) {
                if (s instanceof GraduateStudent) {
                    GraduateStudent g = (GraduateStudent) s;
                    writer.write(String.format("GRAD,%d,%s,%s,%s,%s,%.2f,%s,%s,%s",
                            g.getId(),
                            escape(g.getName()),
                            escape(g.getEmail()),
                            escape(g.getPhone()),
                            escape(g.getDepartment()),
                            g.getGpa(),
                            escape(g.getResearchAdvisor()),
                            escape(g.getThesisTitle()),
                            escape(g.getAssistantshipType())));
                } else if (s instanceof UndergraduateStudent) {
                    UndergraduateStudent u = (UndergraduateStudent) s;
                    writer.write(String.format("UG,%d,%s,%s,%s,%s,%.2f,%d,%s,%s",
                            u.getId(),
                            escape(u.getName()),
                            escape(u.getEmail()),
                            escape(u.getPhone()),
                            escape(u.getDepartment()),
                            u.getGpa(),
                            u.getSemester(),
                            escape(u.getMinorSubject()),
                            escape(u.getCapstoneProjectTitle())));
                }
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Warning: Unable to save students: " + e.getMessage());
        }
    }

    // ==================== Courses CSV ====================

    public Map<String, Course> loadCourses(String filename) {
        Map<String, Course> map = new LinkedHashMap<>();
        File file = getFile(filename);
        if (!file.exists()) return map;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] p = parseCsvLine(line);
                if (p.length >= 5) {
                    try {
                        String code = p[0].trim().toUpperCase();
                        String title = p[1].trim();
                        int credits = Integer.parseInt(p[2].trim());
                        String dept = p[3].trim();
                        String instructor = p[4].trim();
                        map.put(code, new Course(code, title, credits, dept, instructor));
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Warning: Unable to read courses file: " + e.getMessage());
        }
        return map;
    }

    public void saveCourses(String filename, Iterable<Course> courses) {
        File file = getFile(filename);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("CourseCode,Title,Credits,Department,Instructor");
            writer.newLine();
            for (Course c : courses) {
                writer.write(String.format("%s,%s,%d,%s,%s",
                        escape(c.getCourseCode()),
                        escape(c.getCourseTitle()),
                        c.getCredits(),
                        escape(c.getDepartment()),
                        escape(c.getInstructorName())));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Warning: Unable to save courses: " + e.getMessage());
        }
    }

    // ==================== Enrollments CSV ====================

    public Map<Integer, Enrollment> loadEnrollments(String filename) {
        Map<Integer, Enrollment> map = new LinkedHashMap<>();
        File file = getFile(filename);
        if (!file.exists()) return map;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) continue;
                String[] p = parseCsvLine(line);
                if (p.length >= 5) {
                    try {
                        int id = Integer.parseInt(p[0].trim());
                        int studentId = Integer.parseInt(p[1].trim());
                        String code = p[2].trim();
                        double marks = Double.parseDouble(p[3].trim());
                        String term = p[4].trim();
                        map.put(id, new Enrollment(id, studentId, code, marks, term));
                    } catch (NumberFormatException ignored) {
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Warning: Unable to read enrollments file: " + e.getMessage());
        }
        return map;
    }

    public void saveEnrollments(String filename, Iterable<Enrollment> enrollments) {
        File file = getFile(filename);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("EnrollmentID,StudentID,CourseCode,Marks,SemesterTerm");
            writer.newLine();
            for (Enrollment e : enrollments) {
                writer.write(String.format("%d,%d,%s,%.2f,%s",
                        e.getEnrollmentId(),
                        e.getStudentId(),
                        escape(e.getCourseCode()),
                        e.getMarks(),
                        escape(e.getSemesterTerm())));
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Warning: Unable to save enrollments: " + e.getMessage());
        }
    }

    // ==================== Helpers ====================

    private String escape(String val) {
        if (val == null) return "";
        if (val.contains(",") || val.contains("\"") || val.contains("\n")) {
            return "\"" + val.replace("\"", "\"\"") + "\"";
        }
        return val;
    }

    private String[] parseCsvLine(String line) {
        List<String> tokens = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        boolean inQuotes = false;
        for (int i = 0; i < line.length(); i++) {
            char c = line.charAt(i);
            if (c == '\"') {
                if (inQuotes && i + 1 < line.length() && line.charAt(i + 1) == '\"') {
                    sb.append('\"');
                    i++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (c == ',' && !inQuotes) {
                tokens.add(sb.toString());
                sb.setLength(0);
            } else {
                sb.append(c);
            }
        }
        tokens.add(sb.toString());
        return tokens.toArray(new String[0]);
    }
}

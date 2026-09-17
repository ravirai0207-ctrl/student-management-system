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
import java.util.Optional;

/**
 * Service class handling in-memory CRUD business logic and CSV file persistence for Students.
 */
public class StudentService {
    public static final String DEFAULT_STORAGE_FILE = "students.csv";

    private final Map<Integer, Student> studentMap;
    private final String storageFilePath;

    /**
     * Default constructor using standard storage file "students.csv".
     */
    public StudentService() {
        this(DEFAULT_STORAGE_FILE);
    }

    /**
     * Parameterized constructor for custom storage path (useful for testing).
     *
     * @param storageFilePath file path for persistent CSV storage
     */
    public StudentService(String storageFilePath) {
        this.studentMap = new LinkedHashMap<>();
        this.storageFilePath = storageFilePath;
        loadFromStorage();
    }

    /**
     * Adds a new student record to the system and persists changes to storage.
     *
     * @param student Student instance to add
     * @return true if added successfully, false if student is null or ID already exists
     */
    public boolean addStudent(Student student) {
        if (student == null) {
            return false;
        }
        if (studentMap.containsKey(student.getId())) {
            return false;
        }
        studentMap.put(student.getId(), student);
        saveToStorage();
        return true;
    }

    /**
     * Retrieves all students currently registered.
     *
     * @return List of all students in insertion order
     */
    public List<Student> getAllStudents() {
        return new ArrayList<>(studentMap.values());
    }

    /**
     * Finds a student by their unique ID.
     *
     * @param id student ID
     * @return Optional containing Student if found, empty Optional otherwise
     */
    public Optional<Student> findStudentById(int id) {
        return Optional.ofNullable(studentMap.get(id));
    }

    /**
     * Direct retrieval of a student by ID.
     *
     * @param id student ID
     * @return Student if found, null otherwise
     */
    public Student getStudentById(int id) {
        return studentMap.get(id);
    }

    /**
     * Updates an existing student record and persists changes to storage.
     *
     * @param id        student ID to update
     * @param newName   new name (ignored if null/blank)
     * @param newCourse new course (ignored if null/blank)
     * @param newMarks  new marks (0.0 to 100.0)
     * @return true if updated successfully, false if student not found
     */
    public boolean updateStudent(int id, String newName, String newCourse, double newMarks) {
        Student student = studentMap.get(id);
        if (student == null) {
            return false;
        }
        if (newName != null && !newName.trim().isEmpty()) {
            student.setName(newName.trim());
        }
        if (newCourse != null && !newCourse.trim().isEmpty()) {
            student.setCourse(newCourse.trim());
        }
        if (newMarks >= 0.0 && newMarks <= 100.0) {
            student.setMarks(newMarks);
        }
        saveToStorage();
        return true;
    }

    /**
     * Deletes a student by their ID and persists changes to storage.
     *
     * @param id student ID
     * @return true if found and removed, false if not found
     */
    public boolean deleteStudent(int id) {
        boolean removed = studentMap.remove(id) != null;
        if (removed) {
            saveToStorage();
        }
        return removed;
    }

    /**
     * Checks if a student ID exists in the store.
     *
     * @param id student ID
     * @return true if exists, false otherwise
     */
    public boolean existsById(int id) {
        return studentMap.containsKey(id);
    }

    /**
     * Gets the total count of registered students.
     *
     * @return number of students
     */
    public int getStudentCount() {
        return studentMap.size();
    }

    /**
     * Clears all student records and updates storage.
     */
    public void clearAll() {
        studentMap.clear();
        saveToStorage();
    }

    /**
     * Loads student records from the CSV storage file if it exists.
     * Expected CSV format: ID,Name,Course,Marks
     */
    public void loadFromStorage() {
        if (storageFilePath == null) {
            return;
        }
        File file = new File(storageFilePath);
        if (!file.exists() || !file.isFile()) {
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine(); // Header row: ID,Name,Course,Marks
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue;
                }
                String[] parts = parseCsvLine(line);
                if (parts.length >= 4) {
                    try {
                        int id = Integer.parseInt(parts[0].trim());
                        String name = parts[1].trim();
                        String course = parts[2].trim();
                        double marks = Double.parseDouble(parts[3].trim());
                        studentMap.put(id, new Student(id, name, course, marks));
                    } catch (NumberFormatException ignored) {
                        // Skip corrupt/malformed records gracefully
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Warning: Unable to read storage file: " + e.getMessage());
        }
    }

    /**
     * Persists all current student records into the CSV storage file.
     *
     * @return true if successfully saved, false otherwise
     */
    public boolean saveToStorage() {
        if (storageFilePath == null) {
            return false;
        }
        File file = new File(storageFilePath);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("ID,Name,Course,Marks");
            writer.newLine();
            for (Student s : studentMap.values()) {
                writer.write(String.format("%d,%s,%s,%.2f",
                        s.getId(),
                        escapeCsv(s.getName()),
                        escapeCsv(s.getCourse()),
                        s.getMarks()));
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.err.println("Warning: Unable to save to storage file: " + e.getMessage());
            return false;
        }
    }

    /**
     * Escapes CSV fields containing commas, double quotes, or newlines.
     */
    private String escapeCsv(String val) {
        if (val == null) {
            return "";
        }
        if (val.contains(",") || val.contains("\"") || val.contains("\n")) {
            return "\"" + val.replace("\"", "\"\"") + "\"";
        }
        return val;
    }

    /**
     * Parses a CSV row supporting double-quoted fields with escaped quotes.
     */
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

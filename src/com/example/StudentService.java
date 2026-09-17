package com.example;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Service class handling in-memory CRUD business logic for Students.
 */
public class StudentService {
    private final Map<Integer, Student> studentMap;

    public StudentService() {
        this.studentMap = new LinkedHashMap<>();
    }

    /**
     * Adds a new student record to the system.
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
     * Updates an existing student record.
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
        return true;
    }

    /**
     * Deletes a student by their ID.
     *
     * @param id student ID
     * @return true if found and removed, false if not found
     */
    public boolean deleteStudent(int id) {
        return studentMap.remove(id) != null;
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
     * Clears all student records.
     */
    public void clearAll() {
        studentMap.clear();
    }
}

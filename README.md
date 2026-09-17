# Student Management System (CLI)

A robust, console-based CRUD (Create, Read, Update, Delete) application developed in standard Java. The system allows educational institutions or administrators to manage student records through an interactive command-line interface with full input validation and error prevention.

> 📄 **Academic Project Report**: For the formal project documentation, SRS requirements, Mermaid class diagrams, complexity analysis, and verification matrix, see [PROJECT_REPORT.md](PROJECT_REPORT.md).

---

## Features

- **Add Student**: Register a new student with unique ID, name, course, and marks. Includes automated grade calculation.
- **View All Students**: Display all registered students in a formatted tabular layout.
- **Search by ID**: Quickly retrieve student details by ID.
- **Delete Student**: Safely remove student records by ID.
- **Update Student**: Modify existing student attributes (name, course, marks).
- **Persistent Storage (CSV)**: Automatically saves records to `students.csv` and loads them upon program startup.
- **Bulletproof Input Handling**: Validates user inputs (non-empty strings, numeric types, range checks) to prevent runtime crashes or infinite loops during interactive and piped execution.
- **Zero External Dependencies**: Built strictly with standard Java libraries (`java.util`, `java.io`).

---

## Prerequisites

- **Java Development Kit (JDK)**: JDK 11, JDK 17, or newer (tested with JDK 21).
- **Terminal / Command Prompt**: Any standard terminal emulator (macOS Terminal, Linux Bash/Zsh, Windows Command Prompt/PowerShell).

Verify your Java installation:
```bash
java -version
javac -version
```

---

## Project Structure

```
student-management-system/
├── src/
│   └── com/
│       └── example/
│           ├── Main.java           # Terminal UI menu loop & input validation
│           ├── Student.java        # Student entity model
│           └── StudentService.java # Business logic & CSV persistence layer
├── students.csv                    # Persistent data storage (CSV format)
├── PROJECT_REPORT.md               # Formal academic & technical project report
├── README.md                       # Documentation & instructions
└── .gitignore                      # Standard Java ignore rules
```

---

## Setup and Execution

Navigate to the project directory:
```bash
cd student-management-system
```

### 1. Compilation
Compile all Java source files into the `bin` output directory:
```bash
javac -d bin src/com/example/*.java
```

### 2. Execution
Run the compiled application:
```bash
java -cp bin com.example.Main
```

---

## Menu Overview & Usage Walkthrough

Upon launching the application, you are greeted with the interactive menu:

```
==========================================
  Welcome to Student Management System   
==========================================
Persistent Storage: Loaded 2 student record(s) from students.csv

==========================================
                MAIN MENU                 
==========================================
1. Add Student
2. View All
3. Search by ID
4. Delete Student
5. Update Student
6. Exit
==========================================
Enter your choice: 
```

### Sample Workflow

#### 1. Adding a Student (Option 1)
```
Enter your choice: 1

--- Add New Student ---
Enter Student ID: 101
Enter Student Name: Alice Smith
Enter Course Name: Computer Science
Enter Marks (0.0 - 100.0): 94.5
Student added successfully!
Student [ID=101, Name='Alice Smith', Course='Computer Science', Marks=94.50, Grade='A']
```

#### 2. Viewing All Students (Option 2)
```
Enter your choice: 2

--- View All Students ---
+--------+----------------------+----------------------+-------+-------+
| ID     | Name                 | Course               | Marks | Grade |
+--------+----------------------+----------------------+-------+-------+
| 101    | Alice Smith          | Computer Science     | 94.50 | A     |
+--------+----------------------+----------------------+-------+-------+
Total Students: 1
```

#### 3. Searching for a Student (Option 3)
```
Enter your choice: 3

--- Search Student by ID ---
Enter Student ID to search: 101
Student found:
------------------------------------------
ID     : 101
Name   : Alice Smith
Course : Computer Science
Marks  : 94.50
Grade  : A
------------------------------------------
```

#### 4. Deleting a Student (Option 4)
```
Enter your choice: 4

--- Delete Student ---
Enter Student ID to delete: 101
Student with ID 101 deleted successfully.
```

#### 5. Updating a Student (Option 5)
```
Enter your choice: 5

--- Update Student ---
Enter Student ID to update: 101
Current details: Student [ID=101, Name='Alice Smith', Course='Computer Science', Marks=94.50, Grade='A']
Enter New Name: Alice Smith
Enter New Course: AI & Data Science
Enter New Marks (0.0 - 100.0): 98.0
Student with ID 101 updated successfully.
```

#### 6. Exiting the System (Option 6)
```
Enter your choice: 6

Thank you for using Student Management System. Goodbye!
```

---

## Automated Evaluation Compatibility

This project is tailored to work seamlessly with automated evaluation pipelines and test harnesses:
- Handles standard input redirection and pipes (e.g., `java -cp bin com.example.Main < test_input.txt`).
- Safely handles end-of-file (EOF) conditions without throwing unhandled exceptions.
- Employs token and line parsing to avoid `InputMismatchException` crashes when non-numeric inputs are provided.

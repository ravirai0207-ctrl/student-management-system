# Student Management & Academic Analytics System (CLI)

A robust, enterprise-grade console application developed in standard Java for academic institutions. The system manages multi-tiered student populations (Undergraduate and Graduate research scholars), maintains institutional course catalogs, handles student course enrollments, automates Dean's List honors ranking and academic probation monitoring, and produces official transcripts with multi-entity CSV persistence.

> **Academic Documentation**:
> - Formal Statement & Scope: [statement.md](statement.md)
> - Comprehensive Project Report: [PROJECT_REPORT.md](PROJECT_REPORT.md)
> - PDF Project Report: [PROJECT_REPORT.pdf](PROJECT_REPORT.pdf)

---

## Features

1. **Polymorphic Student Administration**:
   - **Undergraduate Students**: Captures semester standing, minor subjects, and senior capstone design project titles.
   - **Graduate Research Scholars**: Captures thesis advisors, research dissertation topics, and graduate assistantship classifications (RA, TA).
2. **Academic Analytics & Merit Ranking**:
   - **Dean's Honor List Engine**: Dynamically identifies top scholars ($GPA \ge 3.70$) and sorts them in descending order of GPA using Java stream comparators.
   - **Academic Probation Alerts**: Automatically detects at-risk students ($GPA < 2.00$) needing institutional advising.
3. **Course Catalog Management**:
   - Register course offerings with unique alphanumeric course codes, credit allocations, departments, and lead instructors.
4. **Course Enrollment & Grade Evaluation**:
   - Enroll students in courses, record evaluated marks ($0.0 - 100.0$), compute letter grades ($A, B, C, D, F$), and prevent duplicate enrollments.
5. **Official Transcript Generation**:
   - Generates itemized, formatted academic grade transcripts displaying student details, program milestone, cumulative GPA, and enrolled course grades.
6. **Multi-Entity Disk Persistence (CSV)**:
   - Synchronizes additions, updates, and deletions across `students.csv`, `courses.csv`, and `enrollments.csv` with RFC 4180 quote escaping.
7. **Bulletproof Input Handling**:
   - Universal line-based `Scanner` token parsing preventing buffer desynchronization, numeric errors, and stream EOF termination.
8. **Zero External Dependencies**:
   - 100% Core Java SE (`java.util`, `java.io`, `java.util.regex`).

---

## Technologies & Tools Used

- **Programming Language**: Java Standard Edition 17+ (Tested on OpenJDK 21)
- **Architecture**: Three-Tier Layered / MVC-Lite Architecture
- **OOP Principles**: Abstraction, Inheritance, Polymorphism, Encapsulation, Enums
- **Persistence Engine**: Multi-Entity CSV Storage with RFC 4180 Escaping
- **Build & Execution**: Standard JDK command-line tools (`javac`, `java`)
- **Version Control**: Git & GitHub CLI (`gh`)

---

## Project Directory Structure

```
student-management-system/
├── src/
│   └── com/
│       └── example/
│           ├── Person.java                 # Abstract base model (Inheritance)
│           ├── Student.java                # Specialized base student entity
│           ├── UndergraduateStudent.java   # Subclass for B.Tech/B.S. students
│           ├── GraduateStudent.java        # Subclass for M.Tech/Ph.D. scholars
│           ├── AcademicStanding.java       # Enumeration for academic standing
│           ├── Course.java                 # Academic course offering entity
│           ├── Enrollment.java             # Student-course association & grades
│           ├── StorageManager.java         # Multi-file CSV persistence engine
│           ├── StudentService.java         # Business logic & analytics service
│           ├── ValidationUtils.java        # Defensive stream parser & regex validators
│           └── Main.java                   # Interactive 11-option console UI
├── students.csv                            # Persistent student records (UG & Grad)
├── courses.csv                             # Persistent institutional course catalog
├── enrollments.csv                         # Persistent course enrollment & grade records
├── statement.md                            # Official VITyarthi Project Statement
├── PROJECT_REPORT.md                       # Comprehensive academic project report
├── README.md                               # Quick-start & operational documentation
└── .gitignore                              # Standard Java ignore rules
```

---

## Setup & Execution Guide

### 1. Prerequisites
Ensure a Java Development Kit (JDK 17 or higher) is installed:
```bash
java -version
javac -version
```

### 2. Navigate to Directory
```bash
cd student-management-system
```

### 3. Compilation
Compile all Java source files into the `bin/` output directory:
```bash
javac -d bin src/com/example/*.java
```

### 4. Running the Application
Launch the compiled CLI system:
```bash
java -cp bin com.example.Main
```

---

## Menu Overview & Interactive Walkthrough

Upon launching the application, you are presented with the main menu:

```
===============================================================
       STUDENT MANAGEMENT & ACADEMIC ANALYTICS SYSTEM (CLI)    
===============================================================
Persistent Storage: Loaded 4 student(s), 3 course(s), 5 enrollment(s).

===============================================================
                          MAIN MENU                            
===============================================================
  1. Register Undergraduate Student (B.Tech / B.S.)
  2. Register Graduate Student (M.Tech / M.S. / Ph.D.)
  3. View All Registered Students
  4. View Dean's Honor List (Merit Ranking: GPA >= 3.70)
  5. View Academic Probation List (GPA < 2.00)
  6. Add New Course Offering
  7. View Course Catalog
  8. Enroll Student in Course & Record Grade
  9. Generate Official Academic Transcript
 10. Delete Student Record
 11. Exit System
===============================================================
Enter your choice (1-11): 
```

### Sample Operations

#### 1. Dean's Honor List (Merit Ranking: Option 4)
```
--- [4] Dean's Honor List (Merit Ranking: GPA >= 3.70) ---
Top academic performers sorted dynamically by GPA in descending order:
+------+----------------------+----------------------+-------+-------+---------------------------------------+
| ID   | Honors Scholar Name  | Department           | GPA   | Grade | Milestone / Academic Achievement      |
+------+----------------------+----------------------+-------+-------+---------------------------------------+
| 101  | Alice Smith          | Computer Science     |  3.92 | A     | Capstone: Autonomous Drone Navigation |
| 201  | Dr. Clara Oswald     | Computer Science     |  3.88 | A     | Thesis: Quantum Cryptographic Prot... |
+------+----------------------+----------------------+-------+-------+---------------------------------------+
Total Dean's Honor Scholars: 2
```

#### 2. Generating an Official Academic Transcript (Option 9)
```
--- [9] Generate Official Academic Transcript ---
Enter Student ID: 101

===============================================================
                  OFFICIAL ACADEMIC TRANSCRIPT                 
===============================================================
Student ID    : #101
Student Name  : Alice Smith
Program Type  : Undergraduate (Semester 6)
Department    : Computer Science
Cumulative GPA: 3.92 / 4.00 (Equivalent: A)
Academic Standing: Dean's Honor List / High Distinction
Milestone     : Capstone: Autonomous Drone Navigation
---------------------------------------------------------------
| Course     | Course Title             | Marks | Grade |
---------------------------------------------------------------
| CSE2005    | Object-Oriented Progr... | 96.50 | A     |
| CSE3001    | Database Management S... | 92.00 | A     |
===============================================================
```

---

## Automated Testing & Evaluation Compatibility

This project is engineered to work reliably with automated evaluation test harnesses and piped standard input:
```bash
# Execute automated batch test script
java -cp bin com.example.Main < test_input.txt
```
- **EOF-Proof**: Employs `ValidationUtils.readLineOrNull()` to terminate gracefully upon stream closure without throwing `NoSuchElementException`.
- **Buffer Safety**: Exclusively uses line parsing (`Scanner.nextLine()`) to prevent newline skipping bugs common in `Scanner.nextInt()`.
- **Validation Retries**: Handles malformed types (`NumberFormatException`) cleanly with inline re-prompting.

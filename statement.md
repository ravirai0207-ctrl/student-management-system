# Project Statement: Comprehensive Academic Student Management & Analytics System

---

## 1. Problem Statement

Academic institutions and university departments often face substantial administrative overhead when tracking multi-tiered student populations (undergraduate and postgraduate scholars), course enrollments, and academic performance metrics. Manual record keeping or generic spreadsheets present several severe limitations:
- **Lack of Program Specialization**: Traditional systems fail to distinguish the differing academic obligations of undergraduate students (semesters, capstone design projects) versus graduate research scholars (thesis advisors, research topics, graduate assistantships).
- **Delayed Academic Advising**: Without dynamic performance analytics, institutions struggle to identify at-risk students who slip into academic warning or probation ($GPA < 2.00$) in time for intervention.
- **Fragmented Grade & Transcript Generation**: Generating consolidated, multi-course academic transcripts and calculating Dean's Honor rankings is cumbersome and prone to calculation errors.
- **Data Volatility**: Desktop utilities lacking structured multi-entity disk persistence lose newly enrolled students and grades upon program termination.
- **Terminal Reliability**: Console applications routinely crash when encountering invalid input tokens (`NumberFormatException`) or stream closure (EOF) during automated evaluation batch testing.

The **Comprehensive Academic Student Management & Analytics System** addresses these institutional needs by providing a robust, crash-proof, Object-Oriented Java console application with polymorphic student hierarchies, dynamic merit ranking, course enrollment management, automated transcript generation, and multi-file CSV persistence.

---

## 2. Scope of the Project

The scope of this software solution covers:
- **Polymorphic Student Administration**: Registration and lifecycle tracking of both Undergraduate (`B.Tech / B.S.`) and Graduate (`M.Tech / Ph.D.`) students inheriting from a unified `Person` and `Student` base model.
- **Course Catalog Management**: Registration and directory maintenance of academic course offerings, credit allocations, and lead faculty instructors.
- **Enrollment & Academic Evaluation**: Association of students with course offerings, recording evaluated marks, automated letter grade computation, and duplicate enrollment prevention.
- **Academic Performance Analytics**:
  - **Dean's Honor List Engine**: Dynamically filters scholars with $GPA \ge 3.70$ and sorts them in descending order of GPA using multi-level stream comparators.
  - **Academic Probation Early Warning**: Flags students with cumulative $GPA < 2.00$ requiring mandatory counseling.
- **Official Transcript Generation**: Generates itemized, formatted grade transcripts displaying student demographics, course grades, cumulative GPA, and graduation milestones.
- **Multi-Entity Disk Persistence**: Serializes and recovers records across three dedicated CSV stores (`students.csv`, `courses.csv`, `enrollments.csv`) using RFC 4180 quote escaping.

### Out of Scope:
- Biometric attendance tracking.
- Network-distributed microservices architecture (single-node local storage).

---

## 3. Target Users

1. **Academic Registrars & Department Heads**: Administrators managing admissions, student rosters, and degree milestones.
2. **Academic Advisors**: Faculty members reviewing Dean's Honor rosters and counseling students placed on academic warning or probation.
3. **Course Coordinators & Instructors**: Faculty registering course offerings, evaluating final student marks, and issuing transcripts.
4. **Academic Evaluators**: Automated test runners and evaluators assessing Object-Oriented Software Engineering principles, stream safety, and code design.

---

## 4. High-Level Features

- **Object-Oriented Inheritance & Polymorphism**: Clear hierarchy with `Person` (abstract base), `Student` (specialized base), `UndergraduateStudent`, and `GraduateStudent`.
- **Dean's Honor List Ranking Engine**: Multi-criteria comparator sorting honors scholars by GPA and name.
- **Academic Probation Alerts**: Automated detection of at-risk students ($GPA < 2.00$).
- **Course Enrollment & Grade Computation**: Enforces credit limits and prevents duplicate enrollments.
- **Itemized Transcript Formatter**: Produces professional ASCII academic transcripts.
- **Multi-File CSV Persistence**: Auto-saves on mutations to `students.csv`, `courses.csv`, and `enrollments.csv`.
- **Bulletproof Input Handling**: EOF-proof token parsing with zero unhandled exceptions.

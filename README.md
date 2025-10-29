# INFO5100 Group Assignment 1: Implementing Access-Controlled Use Cases in a Digital University System

## 1. Project Title
EduCore: Role-Based Digital University Platform

## 2. Team Information
* **Zihao Chen**
    * NUID: [ ]
    * Assigned Role/Use Case: Administrator
    * Responsibilities: [My Profile, Manage Persons & Accounts, and Analytics Dashboard]
* **Zenish Borad**
    * NUID: [002593475]
    * Assigned Role/Use Case: Faculty
    * Responsibilities: [Course Management, Profile Management, Enrollment Insight, Student Management, and Performance Reporting]
* **Deep Prajapati**
    * NUID: [002539860]
    * Assigned Role/Use Case: Student
    * Responsibilities: [Course Registration, Transcript, Graduation Audit, Financials, Profile, View/Submit Assignments]


## 3. Project Overview
This project integrates a reference implementation of a Digital University System with an Access Control Layer . The goal is to enable proper user authentication and role-based authorization for different user types within the university environment. Key features implemented include functionalities specific to the Administrator, Faculty, and Student roles, allowing them to perform tasks relevant to their responsibilities within the digital university model.

## 4. Installation & Setup Instructions
### Prerequisites
* Java Development Kit (JDK) - Version 11 or higher recommended.
* Apache NetBeans IDE - Version 12.0 or higher recommended.
* Git (for cloning the repository).

### Setup
1.  **Clone the Repository:**
    ```bash
    git clone [Your Repository URL Here]
    ```
2.  **Open in NetBeans:** Launch NetBeans IDE and select `File > Open Project...`. Navigate to the cloned repository folder and open the project.
3.  **Clean and Build:** Right-click the project name in the "Projects" tab and select "Clean and Build". This compiles the code and resolves dependencies.
4.  **Run:** Locate the `src/ui/MainJFrame.java` file. Right-click it and select "Run File". This will launch the application's login window.

## 5. Authentication & Access Control
### Authentication
* ]Users log in via the main window using a username and password 330].
* ]The `auth.AuthManager` class handles credential verification against a list of pre-populated or admin-created `UserAccount` objects.
* ]A "Logout" button is available within each role's dashboard to terminate the session and return to the login screen.

### Default Credentials
* **Admin:** username: `admin`, password: `admin`
* **Faculty:** username: `f1`, password: `pass` (through `f10`)
* **Student:** username: `s1`, password: `pass` (through `s10`)

### Authorization (Role-Based Access)
* ]Upon successful login, `MainJFrame.java` checks the user's role stored in their `UserAccount` object.
* ]Based on the role ("Admin", "Faculty", "Student"), the application displays the corresponding main dashboard panel (`AdminWorkAreaJPanel`, `FacultyJPanel`, `StudentWorkAreaJPanel`).
* ]Each role-specific panel only contains buttons and navigation leading to features authorized for that role, as defined in the assignment responsibilities. Users cannot access panels or features intended for other roles through the UI.

## 6. Features Implemented
*(Note: Assign responsible team member for each feature/use case)*

### Administrator Role (Implemented by: [Team Member Name])
* ]**User Account Management:** Create, Update (Password, Role, Linked ID), and Delete user accounts (`ManageAccountsPanel`) 210]. Prevents duplicate usernames.
* ]**Person Registration:** Register new Student, Faculty, and Admin persons (`ManagePersonsPanel`) 212, 215]. ]Auto-generates unique ID 217]. ]Prevents duplicate emails 216].
* ]**Records Management:** View, Update (Name, Phone, Dept), and Delete Student, Faculty, and Admin person records (`ManagePersonsPanel`) 223]. ]Search persons by University ID, Name, or Department 225].
* ]**Profile Management:** View and update own Admin profile details (`ProfilePanel`) 227].
* ]**Analytics Dashboard:** View summary reports (`AnalyticsPanel`) including: Total active users by role  232]], Total courses offered per semester  233]], Total enrolled students per course  234]], and Total tuition revenue summary 235].

### Faculty Role (Implemented by: [Team Member Name])
* **Course Management:**
    * ]View/Add/Update/Delete details (ID, Title, Semester/Schedule, Capacity) for *own* courses (`CourseDetailsJPanel`) 242].
    * ]Manage syllabus name for own courses (`SyllabusManagementJPanel`) 242].
    * ]Open/Close enrollment for own courses (`CourseEnrollmentJPanel` in `ui.CourseManagement`) 245].
* ]**Profile Management:** View and update own profile details (`ProfileManagementJPanel`) 246].
* **Student Management:**
    * ]View list of enrolled students per course (`CourseEnrollmentJPanel` in `ui.StudentManagement`) 247].
    * ]View selected student's transcript summary (opens `TranscriptJPanel`) 248].
    * ]Manage assignments (Add/Update/Delete with title, max score, weight) for own courses (`StudentGradingJPanel`) 249].
    * ]Grade student submissions for assignments (`StudentGradingJPanel`) 249].
    * ]Final letter grade is automatically computed and updated based on weighted assignment scores (`Enrollment.calculateAndUpdateGrade`) 249].
* ]**Performance Reporting:** Generate and export reports per course/semester showing average class GPA, grade distribution, and enrollment count (`PerformanceReportingJPanel`)  251-256]. ]Includes student ranking based on final grade points for the course 250].
* ]**Enrollment Insight:** View total enrollment count and tuition revenue for own courses, filterable by semester (`EnrollmentInsightJPanel`) 258].

### Student Role (Implemented by: [Team Member Name])
* ]**Course Registration:** View available courses per semester, Search courses (by ID, Name, Faculty), Enroll in courses (up to 8 credits/semester), Drop courses (`CourseRegistrationJPanel`)  263-265]. ]Student is billed upon enrollment  298]]; simplified refund/credit applied upon drop 302].
* ]**Transcript Review:** View course history, credits, grades per semester or all semesters (`TranscriptJPanel`) 269]. ]Calculates and displays Term GPA, Overall GPA  293-295]], and Academic Standing (Good, Warning, Probation)  275-280]. ]Access is blocked if tuition balance is outstanding 300].
* ]**Graduation Audit:** Check progress towards MSIS degree requirements (32 credits total, including core course INFO5100) based on completed courses with passing grades (`GraduationAuditJPanel`)  266-268]. Displays "Ready" or "Not Ready" status with reasons.
* ]**Financial Management:** View current tuition balance and transaction history (`StudentFinancialsJPanel`) 297]. ]Make payments via "Pay Tuition" button 298]. ]Handles zero/negative balance 301]. ]Shows billing/payment/refund history 303].
* ]**Profile Management:** View and update own profile details (`StudentProfileJPanel`) 296].
* ]**Coursework Management:** View assignments for enrolled courses, check status (Not Submitted, Submitted, Graded), view score if graded, and "Submit" assignments (creates submission record) (`StudentAssignmentPanel`) 262]. (Note: Does not implement file upload/download or detailed progress).

## 7. Usage Instructions
1.  **Launch:** Run `ui.MainJFrame.java`.
2.  **Login:** Enter credentials for one of the roles (see Default Credentials above).
3.  **Admin:** Use buttons to navigate between managing persons, managing accounts, viewing analytics, or editing own profile. Use tables and forms to view, create, update, or delete records. Use search fields in Manage Persons.
4.  **Faculty:** Use buttons to navigate between managing courses (details, syllabus, enrollment status), managing students (view list, view transcript, manage/grade assignments), viewing performance reports, viewing enrollment insights, or editing own profile. Use dropdowns to select courses/semesters where applicable.
5.  **Student:** Use buttons to navigate between course registration, viewing transcript, checking graduation audit, managing financials, editing own profile, or viewing/submitting assignments. Use dropdowns to select semesters/courses. Use tables and buttons to enroll/drop or submit assignments.
6.  **Logout:** Use the "Logout" button on any dashboard to return to the login screen.

## 8. Testing Guide
* **Authentication:** Try logging in with correct and incorrect credentials for each role. Verify logout works.
* **Authorization:** After logging in, try to access features meant for other roles (should not be possible via UI).
* **Admin Tasks:**
    * Create a new Student, Faculty, and Admin person. Verify unique ID and email checks.
    * Create corresponding user accounts. Verify duplicate username check.
    * Search for persons using all 3 methods.
    * Update a person's details (Name, Phone, Dept).
    * Update a user account's details (Password, Role, Linked ID).
    * Delete a user account.
    * Delete a person (Note: doesn't auto-delete account currently).
    * Check Analytics panel data reflects current state.
* **Faculty Tasks:**
    * Select own course, update capacity. Add a new course. Delete a course.
    * Manage syllabus name.
    * Open/Close enrollment for a course.
    * Add/Update/Delete assignments for a course. Test weight validation (>100%).
    * Select course/assignment, grade submissions for enrolled students. Test score validation (range).
    * Generate performance report, check GPA, distribution, ranking. Export report.
    * Check enrollment insights.
    * View enrolled student list. Select student, view transcript.
    * Update own profile.
* **Student Tasks:**
    * Select semester, view available courses (check that courses with closed enrollment or already enrolled courses don't appear).
    * Search available courses using all 3 methods. Clear search.
    * Enroll in a course. Verify credit limit check. Verify billing occurs (check Financials).
    * Drop a course. Verify refund/credit occurs (check Financials).
    * View transcript (try before and after paying tuition). Check GPA/Standing calculation. Filter by semester.
    * Check graduation audit status before/after completing requirements.
    * View/Submit assignments. Verify status updates. Check that already submitted assignments cannot be re-submitted.
    * Update own profile.

## 9. Challenges & Solutions
* **Challenge:** Integrating the separate `model`, `auth`, `directory`, and `ui` packages required careful refactoring of constructors and data passing.
    * **Solution:** Passed necessary directories down from `MainJFrame` through panel constructors. Ensured panels used the correct model objects.
* **Challenge:** NetBeans build cache sometimes caused errors (`Uncompilable code` despite `BUILD SUCCESSFUL`).
    * **Solution:** Used "Clean and Build" frequently to force recompilation.
* **Challenge:** Java lambda expressions requiring captured variables to be final or effectively final.
    * **Solution:** Created temporary `final` variables within methods before the lambda expression to hold values needed inside the lambda (e.g., `final String courseIdToFilter`).
* **Challenge:** `JComboBox` type mismatches between NetBeans designer defaults (`String`) and code using custom objects (`Course`, `Assignment`).
    * **Solution:** Manually edited variable declarations (`JComboBox<Course>`) and removed/modified default `setModel` calls in `initComponents()`.
* **Challenge:** Implementing automatic grade calculation based on weighted assignments.
    * **Solution:** Added `Assignment` and `Submission` models/directories. Implemented `calculateAndUpdateGrade` method in `Enrollment` model, triggered after faculty saves a submission score in `StudentGradingJPanel`.

## 10. Future Enhancements
* Implement detailed Student Progress Reports for faculty view.
* Implement full Coursework Management for students (e.g., file upload/download links).
* Add more robust input validation (e.g., email/phone number formats using regex).
* Implement cascading deletes (e.g., deleting a Person also deletes their UserAccount and related Enrollments/Submissions).
* Add database persistence instead of in-memory data.
* Refine GPA calculation to handle repeated courses if necessary.
* Add more sophisticated search/filtering options.
* Improve UI aesthetics.

## 11. Contribution Breakdown
*(Each team member should fill this section detailing their specific contributions)*
* **[Team Member 1 Name]:** [Describe contributions - e.g., Implemented Admin Use Case panels (ManagePersons, ManageAccounts, Analytics, Profile), refactored AuthManager, initial project setup...]
* **[Team Member 2 Name]:** [Describe contributions - e.g., Implemented Faculty Use Case panels (FacultyDashboard, CourseMgmt sub-panels, StudentMgmt sub-panels, PerformanceReporting, EnrollmentInsight), extended model for Assignments/Submissions, implemented auto-grading logic...]
* **[Team Member 3 Name]:** [Describe contributions - e.g., Implemented Student Use Case panels (StudentDashboard, CourseRegistration, Transcript, Audit, Financials, Profile, Assignments), implemented billing/refund logic, implemented transcript hold...]
* **Team Collaboration:** [Describe how the team collaborated, e.g., code reviews, debugging sessions, Git workflow management...]

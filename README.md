  Project Title
Digital University System with Role-Based Access Control

Zenish Borad	 002593475	Faculty Use Case Lead	Faculty UI Panels (Course Management, Grading, Syllabus, Performance Reporting), Faculty Directory Integration
Zihao Chen             		Admin Use Case Lead	Admin Panel (User Account Management, Faculty & Student Records, Analytics Dashboard)
Deep Prajapti  002539860	   	Student Use Case Lead	Course Registration, Transcript, Tuition Payment, Progress Tracking

Project Overview
This project implements a Digital University System that integrates authentication and role-based access control (RBAC).
Each role (Admin, Faculty, Student) accesses authorized features via the Access Control Layer.
Objectives:
Manage university data through secure user roles.
Provide interactive UI panels using Java Swing and CardLayout.
Enable authentication, authorization, and CRUD operations for university entities.

Installation & Setup Instructions
Prerequisites
Java JDK 17 or later
NetBeans IDE 12+ (or IntelliJ IDEA)
JDK-compatible Swing Framework
Project folder: DigitalUniversityModel

Default Login     Credentials
Role	 Username	  Password
Admin	  admin     admin123
Faculty	faculty1	pass1
Student	student1	pass1

Authentication
The MainJFrame handles user login through username and password fields.
Verification occurs via UserAccountDirectory.authenticateUser().
Access Control
Role-based access ensures users only see their assigned dashboard:
Admin → AdminJPanel
Faculty → FacultyJPanel
Student → StudentJPanel
Each panel uses CardLayout navigation for smooth transitions between subpanels.

Administrator Features
Manage user accounts (Add, Edit, Delete)
Register new persons (students/faculty)
Prevent duplicate registrations via email/ID checks
Auto-generate unique university IDs
Manage faculty & student records
Analytics dashboard: user counts, course totals, tuition summary (via JTable)

Faculty Features
Course Management: View/update course details, upload syllabus, manage enrollment
Student Management: View enrolled students, grade assignments, view progress
Performance Reporting: GPA, grade distribution, average grade reports
Profile Management



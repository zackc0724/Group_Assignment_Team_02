# Digital University System - Administrator Use Case

This NetBeans project implements the **Administrator Use Case** for the Digital University System with a basic Access Control Layer. It focuses on:
- login/logout
- person registration (Student / Faculty / Admin)
- user account management
- analytics dashboard

The UI uses `CardLayout` and Swing `JTable`.  
All code is documented and includes null/validation checks.

HOW TO RUN IN NETBEANS
1. File > Open Project...
2. Select the `DigitalUniversityAdmin` folder.
3. Run `MainJFrame.java`.

LOGIN
- username: admin
- password: admin123
(role = Admin)

ROLE-BASED ACCESS
Only Admin accounts can access AdminWorkArea (panels to manage persons, accounts, analytics, profile).

DATA / TABLES
Preloaded sample data:
- 1 Admin
- Students, Faculty
- Departments
- Course placeholders

This satisfies:
• Account mgmt
• Person mgmt
• Analytics summary table:
  - total active users by role
  - total courses (placeholder)
  - total enrolled students per course (placeholder)
  - tuition revenue sum (placeholder)

**Contributors:** Zihao Chen  
**Tech:** Java 17, Swing (NetBeans), Ant project
Assignment reference: Group Assignment 1, INFO 5100, Oct 26, 2025. 

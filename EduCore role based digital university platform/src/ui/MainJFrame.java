/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package ui;

// Import the correct Auth and Model classes
import auth.AuthManager;
import auth.UserAccount;
import info5100.university.example.model.Assignment;
import info5100.university.example.model.Submission;
import model.Admin;
import model.Course;
import model.Faculty;
import model.Student;
import model.directory.CourseDirectory;
import model.directory.EnrollmentDirectory;
import model.directory.PersonDirectory;
import ui.admin.AdminWorkAreaJPanel;
import info5100.university.example.model.directory.AssignmentDirectory;
import info5100.university.example.model.directory.SubmissionDirectory;
// We will need to refactor FacultyJPanel and create StudentJPanel
// import ui.student.StudentWorkAreaJPanel; 

import java.awt.CardLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import model.Enrollment;

/**
 *
 * @author zenishborad
 */
public class MainJFrame extends javax.swing.JFrame {

    // These will be our central data store, passed to all panels
    private AuthManager authManager;
    private PersonDirectory personDirectory;
    private CourseDirectory courseDirectory;
    private EnrollmentDirectory enrollmentDirectory;
    private AssignmentDirectory assignmentDirectory;
    private SubmissionDirectory submissionDirectory;

    /**
     * Creates new form MainJFrame
     */
    public MainJFrame() {
        initComponents();
        
        // Initialize our directories and auth manager
        this.authManager = new AuthManager();
        this.personDirectory = new PersonDirectory();
        this.courseDirectory = new CourseDirectory();
        this.enrollmentDirectory = new EnrollmentDirectory();
        this.assignmentDirectory = new AssignmentDirectory();
        this.submissionDirectory = new SubmissionDirectory();
        // Pre-populate data as required by the assignment
        configureData();
        
        // Set size and location
        setSize(800, 600);
        setLocationRelativeTo(null);
    }

    /**
     * This method creates and pre-populates all our users and data
     * according to the assignment requirements.
     */
    private void configureData() {
        // 1. Create Admin
        Admin admin = new Admin("U001", "Admin User", "admin@uni.com", "111-222-3333", "University");
        personDirectory.addPerson(admin);
        authManager.addAccount(new UserAccount("admin", "admin", "Admin", admin.getUniversityId()));

        // 2. Create Faculty (10)
        for (int i = 1; i <= 10; i++) {
            String id = "F10" + i;
            String name = "Faculty " + i;
            Faculty f = new Faculty(id, name, "f" + i + "@uni.com", "222-333-444" + i, "Info Systems", "Professor");
            personDirectory.addPerson(f);
            authManager.addAccount(new UserAccount("f" + i, "pass", "Faculty", id));
        }

        // 3. Create Students (10)
        for (int i = 1; i <= 10; i++) {
            String id = "S20" + i;
            String name = "Student " + i;
            Student s = new Student(id, name, "s" + i + "@uni.com", "555-666-777" + i, "Info Systems");
            personDirectory.addPerson(s);
            authManager.addAccount(new UserAccount("s" + i, "pass", "Student", id));
        }

        // 4. Create Courses (5) 
        // We'll assign faculty we just created
        Faculty prof1 = personDirectory.getAllFaculty().get(0);
        Faculty prof2 = personDirectory.getAllFaculty().get(1);

        Course c1 = new Course("INFO5100", "App Eng & Dev", "Fall2025", prof1, 50, 6000.0, 4); // Added 4 credits
        Course c2 = new Course("INFO6210", "Data Science", "Fall2025", prof2, 40, 6000.0, 4); // Added 4 credits
        Course c3 = new Course("INFO6150", "Web Design", "Fall2025", prof1, 40, 6000.0, 4); // Added 4 credits
        Course c4 = new Course("CSYE6200", "Concepts of OOP", "Fall2025", prof2, 50, 6000.0, 4); // Added 4 credits
        Course c5 = new Course("INFO5001", "Intro to Info Systems", "Fall2025", prof1, 50, 6000.0, 4); // Added 4 credits
        
        courseDirectory.addCourse(c1);
        courseDirectory.addCourse(c2);
        courseDirectory.addCourse(c3);
        courseDirectory.addCourse(c4);
        courseDirectory.addCourse(c5);
        
        // 5. Create 1 Semester 
        // This is handled by the "Fall2025" string in the courses.

        // 6. *** ADD SAMPLE ENROLLMENTS ***
        // Get some students
        Student stu1 = personDirectory.getAllStudents().get(0); // Student 1 (s1)
        Student stu2 = personDirectory.getAllStudents().get(1); // Student 2 (s2)
        Student stu3 = personDirectory.getAllStudents().get(2); // Student 3 (s3)
        Student stu4 = personDirectory.getAllStudents().get(3); // Student 4 (s4)

        // Enroll students in courses
        // Enroll stu1 and stu2 in c1 (INFO5100, taught by f1)
        enrollmentDirectory.addEnrollment(new Enrollment(stu1, c1));
        enrollmentDirectory.addEnrollment(new Enrollment(stu2, c1));

        // Enroll stu3 in c3 (INFO6150, taught by f1)
        enrollmentDirectory.addEnrollment(new Enrollment(stu3, c3));

        // Enroll stu4 in c2 (INFO6210, taught by f2)
        enrollmentDirectory.addEnrollment(new Enrollment(stu4, c2));
        
        // Enroll stu1 also in c4 (CSYE6200, taught by f2)
        enrollmentDirectory.addEnrollment(new Enrollment(stu1, c4));
        
        // --- Optional: Add Sample Assignments ---
        // Example for INFO5100
        assignmentDirectory.addAssignment(new Assignment("INFO5100_A1", "INFO5100", "Assignment 1 - UML", 100.0, 0.25)); // 25% weight
        assignmentDirectory.addAssignment(new Assignment("INFO5100_A2", "INFO5100", "Assignment 2 - Code", 100.0, 0.35)); // 35% weight
        assignmentDirectory.addAssignment(new Assignment("INFO5100_Final", "INFO5100", "Final Project", 100.0, 0.40)); // 40% weight
         // Example for INFO6150
        assignmentDirectory.addAssignment(new Assignment("INFO6150_HW1", "INFO6150", "Homework 1", 50.0, 0.5));
        assignmentDirectory.addAssignment(new Assignment("INFO6150_HW2", "INFO6150", "Homework 2", 50.0, 0.5));


        // --- Optional: Add Sample Submissions ---
        // Student s1 submits A1 for INFO5100
        submissionDirectory.addSubmission(new Submission("S5100A1_S201", "INFO5100_A1", "S201"));
        // Student s2 submits A1 for INFO5100
        submissionDirectory.addSubmission(new Submission("S5100A1_S202", "INFO5100_A1", "S202"));
         // Student s3 submits HW1 for INFO6150
        submissionDirectory.addSubmission(new Submission("S6150HW1_S203", "INFO6150_HW1", "S203"));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel3 = new javax.swing.JPanel();
        splitPane = new javax.swing.JSplitPane();
        actionPanel = new javax.swing.JPanel();
        lblUsername = new javax.swing.JLabel();
        txtUsername = new javax.swing.JTextField();
        lblPassword = new javax.swing.JLabel();
        btnLogIn = new javax.swing.JButton();
        pfPassword = new javax.swing.JPasswordField();
        workArea = new javax.swing.JPanel();
        lblTitle = new javax.swing.JLabel();

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        getContentPane().setLayout(new javax.swing.BoxLayout(getContentPane(), javax.swing.BoxLayout.LINE_AXIS));

        actionPanel.setBackground(new java.awt.Color(255, 204, 204));

        lblUsername.setText("Username");

        lblPassword.setText("Password");

        btnLogIn.setText("Log In");
        btnLogIn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnLogInActionPerformed(evt);
            }
        });

        pfPassword.setText("jPas");
        pfPassword.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                pfPasswordActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout actionPanelLayout = new javax.swing.GroupLayout(actionPanel);
        actionPanel.setLayout(actionPanelLayout);
        actionPanelLayout.setHorizontalGroup(
            actionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(actionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(actionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtUsername)
                    .addComponent(btnLogIn, javax.swing.GroupLayout.DEFAULT_SIZE, 88, Short.MAX_VALUE)
                    .addGroup(actionPanelLayout.createSequentialGroup()
                        .addGroup(actionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblPassword)
                            .addComponent(lblUsername))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(pfPassword))
                .addContainerGap())
        );
        actionPanelLayout.setVerticalGroup(
            actionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(actionPanelLayout.createSequentialGroup()
                .addGap(32, 32, 32)
                .addComponent(lblUsername)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtUsername, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(34, 34, 34)
                .addComponent(lblPassword)
                .addGap(18, 18, 18)
                .addComponent(pfPassword, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(43, 43, 43)
                .addComponent(btnLogIn)
                .addContainerGap(258, Short.MAX_VALUE))
        );

        splitPane.setLeftComponent(actionPanel);

        workArea.setLayout(new java.awt.CardLayout());

        lblTitle.setFont(new java.awt.Font("Helvetica Neue", 1, 24)); // NOI18N
        lblTitle.setText("Digital University Model");
        workArea.add(lblTitle, "card2");

        splitPane.setRightComponent(workArea);

        getContentPane().add(splitPane);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void pfPasswordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_pfPasswordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_pfPasswordActionPerformed

    private void btnLogInActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnLogInActionPerformed
        // TODO add your handling code here:
                                                                                     
    // Get username and password
        String username = txtUsername.getText();
        String password = new String(pfPassword.getPassword());

        // Try to log in using the AuthManager
        if (authManager.login(username, password)) {
            UserAccount user = authManager.getCurrentUser();
            
            // Clear the main work area
            workArea.removeAll();
            
            // Navigate based on role
            if (authManager.isAdmin()) {
                // Navigate to Admin Panel
                AdminWorkAreaJPanel adminPanel = new AdminWorkAreaJPanel(workArea, authManager, personDirectory, courseDirectory, enrollmentDirectory);
                workArea.add("AdminWorkAreaJPanel", adminPanel);
                ((CardLayout) workArea.getLayout()).show(workArea, "AdminWorkAreaJPanel"); // Use show for clarity

            } else if (authManager.isFaculty()) {
                // *** THIS IS THE CORRECTED LINE ***
                // We instantiate FacultyJPanel with its new, correct constructor
                FacultyJPanel facultyPanel = new FacultyJPanel(workArea, authManager, personDirectory, courseDirectory, enrollmentDirectory, assignmentDirectory, submissionDirectory); // ADDED DIRS
                workArea.add("FacultyWorkAreaJPanel", facultyPanel);
                ((CardLayout) workArea.getLayout()).show(workArea, "FacultyWorkAreaJPanel"); // Use show for clarity

            } else if (authManager.isStudent()) {
                // *** ADDED STUDENT NAVIGATION ***
                // Navigate to Student Panel
                StudentWorkAreaJPanel studentPanel = new StudentWorkAreaJPanel(workArea, authManager, personDirectory, courseDirectory, enrollmentDirectory, assignmentDirectory, submissionDirectory); // ADDED DIRS
                workArea.add("StudentWorkAreaJPanel", studentPanel);
                ((CardLayout) workArea.getLayout()).show(workArea, "StudentWorkAreaJPanel");

            } else {
                 // Should not happen, but handle unknown roles
                 JOptionPane.showMessageDialog(this, "Unknown user role.", "Login Error", JOptionPane.ERROR_MESSAGE);
                 // Go back to default view
                 workArea.add(lblTitle, "card2");
                ((CardLayout) workArea.getLayout()).show(workArea, "card2");
            }
            
            workArea.revalidate();
            workArea.repaint();

        } else {
            // Show login failed message
            JOptionPane.showMessageDialog(this, "Invalid username or password.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            pfPassword.setText("");
        }

   
    
    }//GEN-LAST:event_btnLogInActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        
        //</editor-fold>

        /* Create and display the form */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainJFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainJFrame().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel actionPanel;
    private javax.swing.JButton btnLogIn;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lblPassword;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JLabel lblUsername;
    private javax.swing.JPasswordField pfPassword;
    private javax.swing.JSplitPane splitPane;
    private javax.swing.JTextField txtUsername;
    private javax.swing.JPanel workArea;
    // End of variables declaration//GEN-END:variables

} 


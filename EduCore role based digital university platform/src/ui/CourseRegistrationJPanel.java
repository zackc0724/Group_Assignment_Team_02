/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ui;

import java.awt.CardLayout;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import model.Course;
import model.Enrollment;
import model.Student;
import model.directory.CourseDirectory;
import model.directory.EnrollmentDirectory;

/**
 *
 * @author Your Name
 */
public class CourseRegistrationJPanel extends javax.swing.JPanel {

    private JPanel workArea;
    private Student loggedInStudent;
    private CourseDirectory courseDirectory;
    private EnrollmentDirectory enrollmentDirectory;
    private final int MAX_CREDITS_PER_SEMESTER = 8; // Credit limit

    /**
     * Creates new form CourseRegistrationJPanel
     */
    public CourseRegistrationJPanel(JPanel workArea, Student loggedInStudent, CourseDirectory courseDirectory, EnrollmentDirectory enrollmentDirectory) {
        this.workArea = workArea;
        this.loggedInStudent = loggedInStudent;
        this.courseDirectory = courseDirectory;
        this.enrollmentDirectory = enrollmentDirectory;
        initComponents();
        populateSemesterComboBox();
        // Add listener to update tables when semester changes
        cmbSemester.addActionListener(e -> updateCourseTables());
        // Initial population
        updateCourseTables();
    }

    private void populateSemesterComboBox() {
        cmbSemester.removeAllItems();
        Set<String> semesters = new HashSet<>();
        for (Course course : courseDirectory.getCourses()) {
             // Only show semesters with courses open for enrollment potentially
            // if(course.isEnrollmentOpen()){
                 semesters.add(course.getSemester());
            // }
        }
         // Add future semesters if needed
        semesters.add("Spring2026");

        List<String> sortedSemesters = new ArrayList<>(semesters);
        sortedSemesters.sort(null); // Sort

        for (String semester : sortedSemesters) {
            cmbSemester.addItem(semester);
        }
        // Select the first available semester by default if list is not empty
        if (cmbSemester.getItemCount() > 0) {
            cmbSemester.setSelectedIndex(0);
        }
    }

    private void updateCourseTables() {
        populateAvailableCourses();
        populateMyCourses();
    }

    private void populateAvailableCourses() {
        DefaultTableModel model = (DefaultTableModel) tblAvailableCourses.getModel();
        model.setRowCount(0);
        String selectedSemester = (String) cmbSemester.getSelectedItem();

        if (selectedSemester == null) return;

        // Get courses already enrolled in by the student for this semester
        Set<String> enrolledCourseIds = new HashSet<>();
        for(Enrollment en : enrollmentDirectory.getEnrollments()){
            if(en.getStudent().getUniversityId().equals(loggedInStudent.getUniversityId()) && en.getCourse().getSemester().equals(selectedSemester)){
                enrolledCourseIds.add(en.getCourse().getCourseId());
            }
        }


        // Populate available courses table
        for (Course course : courseDirectory.getCourses()) {
            // Show only courses for the selected semester, open for enrollment, and not already enrolled in
            if (selectedSemester.equals(course.getSemester()) &&
                course.isEnrollmentOpen() &&
                !enrolledCourseIds.contains(course.getCourseId()) ) {

                Object[] row = new Object[5]; // Adjust size based on columns
                row[0] = course; // Store the Course object
                row[1] = course.getTitle();
                row[2] = course.getFaculty() != null ? course.getFaculty().getName() : "TBA";
                row[3] = course.getCredits();
                // row[4] = course.getSchedule(); // Add if you have a schedule field
                row[4] = course.getCapacity(); // Example: Show capacity or remaining seats
                model.addRow(row);
            }
        }
        // Custom renderer for the first column to display Course ID
         tblAvailableCourses.getColumnModel().getColumn(0).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof Course) {
                    value = ((Course) value).getCourseId();
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        });
    }

     private void populateMyCourses() {
        DefaultTableModel model = (DefaultTableModel) tblMyCourses.getModel();
        model.setRowCount(0);
        String selectedSemester = (String) cmbSemester.getSelectedItem();
        int currentCredits = 0;

        if (selectedSemester == null) {
             lblCurrentCredits.setText("Current Credits: 0 / " + MAX_CREDITS_PER_SEMESTER);
             return;
        }

        // Populate student's enrolled courses table
        for (Enrollment enrollment : enrollmentDirectory.getEnrollments()) {
            if (enrollment.getStudent().getUniversityId().equals(loggedInStudent.getUniversityId()) &&
                enrollment.getCourse().getSemester().equals(selectedSemester)) {
                Course course = enrollment.getCourse();
                Object[] row = new Object[3]; // CourseID, Title, Credits
                row[0] = enrollment; // Store Enrollment object
                row[1] = course.getTitle();
                row[2] = course.getCredits();
                model.addRow(row);
                currentCredits += course.getCredits();
            }
        }
         // Custom renderer for the first column to display Course ID from Enrollment
         tblMyCourses.getColumnModel().getColumn(0).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof Enrollment) {
                    value = ((Enrollment) value).getCourse().getCourseId();
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        });

        // Update credit label
        lblCurrentCredits.setText("Current Credits: " + currentCredits + " / " + MAX_CREDITS_PER_SEMESTER);
    }

    // Helper to calculate current credits for the selected semester
    private int getCurrentSemesterCredits() {
        String selectedSemester = (String) cmbSemester.getSelectedItem();
        if (selectedSemester == null) return 0;

        int currentCredits = 0;
        for (Enrollment enrollment : enrollmentDirectory.getEnrollments()) {
            if (enrollment.getStudent().getUniversityId().equals(loggedInStudent.getUniversityId()) &&
                enrollment.getCourse().getSemester().equals(selectedSemester)) {
                currentCredits += enrollment.getCourse().getCredits();
            }
        }
        return currentCredits;
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        cmbSemester = new javax.swing.JComboBox<>();
        cmbSearchType = new javax.swing.JComboBox<>();
        txtSearchTerm = new javax.swing.JTextField();
        btnSearch = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblAvailableCourses = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblMyCourses = new javax.swing.JTable();
        btnEnroll = new javax.swing.JButton();
        btnDrop = new javax.swing.JButton();
        btnBack = new javax.swing.JButton();
        btnClearSearch = new javax.swing.JButton();
        lblTitle = new javax.swing.JLabel();
        lblSemesterSelect = new javax.swing.JLabel();
        lblAvailableCourses = new javax.swing.JLabel();
        lblMyCourses = new javax.swing.JLabel();
        lblCurrentCredits = new javax.swing.JLabel();
        lblSearchPrompt = new javax.swing.JLabel();

        cmbSemester.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        cmbSearchType.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "By Course ID", "By Course Name", "By Faculty Name" }));

        btnSearch.setText("Search");
        btnSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSearchActionPerformed(evt);
            }
        });

        tblAvailableCourses.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Course ID", "Title", "Faculty", "Credits", "Semester", "Status"
            }
        ));
        jScrollPane1.setViewportView(tblAvailableCourses);

        tblMyCourses.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null},
                {null, null, null},
                {null, null, null},
                {null, null, null}
            },
            new String [] {
                "Course ID", "Title", "Credits"
            }
        ));
        jScrollPane2.setViewportView(tblMyCourses);

        btnEnroll.setText("Enroll");
        btnEnroll.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnrollActionPerformed(evt);
            }
        });

        btnDrop.setText("Drop");
        btnDrop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDropActionPerformed(evt);
            }
        });

        btnBack.setText("Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        btnClearSearch.setText("Clear Search");
        btnClearSearch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearSearchActionPerformed(evt);
            }
        });

        lblTitle.setText("Course Registration");

        lblSemesterSelect.setText("Select Semester");

        lblAvailableCourses.setText("Available courses for registration");

        lblMyCourses.setText("My current Schedule");

        lblCurrentCredits.setText("Current credits: 0 / 8");

        lblSearchPrompt.setText("Search available courses");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(15, 15, 15)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 601, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 601, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(26, 26, 26)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblTitle)
                                    .addComponent(lblAvailableCourses)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(cmbSemester, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(lblSemesterSelect))
                                        .addGap(18, 18, 18)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(lblSearchPrompt)
                                            .addGroup(layout.createSequentialGroup()
                                                .addComponent(cmbSearchType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(28, 28, 28)
                                                .addComponent(txtSearchTerm, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(21, 21, 21)
                                                .addComponent(btnSearch)
                                                .addGap(18, 18, 18)
                                                .addComponent(btnClearSearch))))))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addComponent(btnEnroll)
                        .addGap(47, 47, 47)
                        .addComponent(btnDrop)
                        .addGap(47, 47, 47)
                        .addComponent(btnBack)
                        .addGap(42, 42, 42)
                        .addComponent(lblCurrentCredits))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(44, 44, 44)
                        .addComponent(lblMyCourses)))
                .addContainerGap(14, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(lblTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 21, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSemesterSelect)
                    .addComponent(lblSearchPrompt))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbSemester, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmbSearchType, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtSearchTerm, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSearch)
                    .addComponent(btnClearSearch))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblAvailableCourses)
                .addGap(8, 8, 8)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblMyCourses)
                .addGap(9, 9, 9)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEnroll)
                    .addComponent(btnDrop)
                    .addComponent(btnBack)
                    .addComponent(lblCurrentCredits))
                .addGap(34, 34, 34))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
        workArea.remove(this);
        CardLayout layout = (CardLayout) workArea.getLayout();
        layout.previous(workArea);
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnEnrollActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnrollActionPerformed
        // TODO add your handling code here:
        int selectedRow = tblAvailableCourses.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a course from the 'Available Courses' table to enroll.");
            return;
        }

        Course courseToEnroll = (Course) tblAvailableCourses.getValueAt(selectedRow, 0); // Get Course object

        // Check credit limit
        int currentCredits = getCurrentSemesterCredits();
        if (currentCredits + courseToEnroll.getCredits() > MAX_CREDITS_PER_SEMESTER) {
            JOptionPane.showMessageDialog(this,
                "Cannot enroll. Enrolling in this course (" + courseToEnroll.getCredits() + " credits) would exceed the semester limit of " + MAX_CREDITS_PER_SEMESTER + " credits (Current: " + currentCredits + ").",
                "Credit Limit Exceeded",
                JOptionPane.WARNING_MESSAGE);
            return;
        }

        // Check if enrollment is actually open (redundant check based on table population, but good practice)
         if (!courseToEnroll.isEnrollmentOpen()) {
            JOptionPane.showMessageDialog(this, "Enrollment for this course is currently closed.", "Enrollment Closed", JOptionPane.WARNING_MESSAGE);
            return;
        }
         
        // --- BILLING LOGIC ---
        double tuitionCost = courseToEnroll.getTuitionAmountPerStudent();
        loggedInStudent.setBalanceDue(loggedInStudent.getBalanceDue() + tuitionCost); // Increase balance 
        loggedInStudent.addPaymentHistoryRecord("Billed $" + String.format("%.2f", tuitionCost) + " for enrolling in " + courseToEnroll.getCourseId() + " (" + courseToEnroll.getSemester() + ")");
        // --- END BILLING LOGIC ---
        
        // Create and add enrollment record
        Enrollment newEnrollment = new Enrollment(loggedInStudent, courseToEnroll);
        enrollmentDirectory.addEnrollment(newEnrollment);

        // Refresh tables
        updateCourseTables();

        JOptionPane.showMessageDialog(this, "Successfully enrolled in " + courseToEnroll.getTitle() + "!");
    }//GEN-LAST:event_btnEnrollActionPerformed

    private void btnDropActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDropActionPerformed
        // TODO add your handling code here:
        int selectedRow = tblMyCourses.getSelectedRow();
        if (selectedRow < 0) {
            JOptionPane.showMessageDialog(this, "Please select a course from 'My Current Schedule' table to drop.");
            return;
        }

        Enrollment enrollmentToDrop = (Enrollment) tblMyCourses.getValueAt(selectedRow, 0); // Get Enrollment object
        // Declare variables *before* the confirmation dialog
        Course courseToDrop = enrollmentToDrop.getCourse();
        double tuitionAmount = courseToDrop.getTuitionAmountPerStudent();

        int confirm = JOptionPane.showConfirmDialog(
            this,
            "Are you sure you want to drop " + courseToDrop.getTitle() + "?", // Use variable here
            "Confirm Drop",
            JOptionPane.YES_NO_OPTION
        );

        if (confirm == JOptionPane.YES_OPTION) {
            // Remove enrollment from the directory
            enrollmentDirectory.getEnrollments().remove(enrollmentToDrop); // Assumes getEnrollments returns the modifiable list

            // --- REFUND LOGIC (Simplified) ---
            loggedInStudent.setBalanceDue(loggedInStudent.getBalanceDue() - tuitionAmount); // Use variable here
            loggedInStudent.addPaymentHistoryRecord("Refunded/Credited $" + String.format("%.2f", tuitionAmount) + " for dropping " + courseToDrop.getCourseId() + " (" + courseToDrop.getSemester() + ")"); // Use variables here
            // --- END REFUND LOGIC ---

            // Refresh tables
            updateCourseTables();

            JOptionPane.showMessageDialog(this, "Successfully dropped " + courseToDrop.getTitle() + ". Balance adjusted by $" + String.format("%.2f", -tuitionAmount) + "."); // Use variables here
        }
    }//GEN-LAST:event_btnDropActionPerformed

    private void btnSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSearchActionPerformed
        // TODO add your handling code here:
        String searchTerm = txtSearchTerm.getText().trim().toLowerCase();
        String searchType = (String) cmbSearchType.getSelectedItem();
        String selectedSemester = (String) cmbSemester.getSelectedItem();

        if (searchTerm.isEmpty() || searchType == null || selectedSemester == null) {
            JOptionPane.showMessageDialog(this, "Please enter a search term, select search type, and ensure a semester is selected.");
            return;
        }

        DefaultTableModel model = (DefaultTableModel) tblAvailableCourses.getModel();
        model.setRowCount(0);

        // Get courses already enrolled in by the student for this semester
        Set<String> enrolledCourseIds = new HashSet<>();
         for(Enrollment en : enrollmentDirectory.getEnrollments()){
            if(en.getStudent().getUniversityId().equals(loggedInStudent.getUniversityId()) && en.getCourse().getSemester().equals(selectedSemester)){
                enrolledCourseIds.add(en.getCourse().getCourseId());
            }
        }

        // Filter available courses based on search
        for (Course course : courseDirectory.getCourses()) {
            boolean match = false;
            if (selectedSemester.equals(course.getSemester()) &&
                course.isEnrollmentOpen() &&
                 !enrolledCourseIds.contains(course.getCourseId())) {

                // Apply search logic
                switch (searchType) {
                    case "By Course ID":
                        if (course.getCourseId().toLowerCase().contains(searchTerm)) {
                            match = true;
                        }
                        break;
                    case "By Course Name":
                        if (course.getTitle().toLowerCase().contains(searchTerm)) {
                            match = true;
                        }
                        break;
                    case "By Faculty Name":
                        if (course.getFaculty() != null && course.getFaculty().getName().toLowerCase().contains(searchTerm)) {
                            match = true;
                        }
                        break;
                }
            }

            if (match) {
                 Object[] row = new Object[5];
                 row[0] = course;
                 row[1] = course.getTitle();
                 row[2] = course.getFaculty() != null ? course.getFaculty().getName() : "TBA";
                 row[3] = course.getCredits();
                 row[4] = course.getCapacity();
                 model.addRow(row);
            }
        }
         // Renderer still needed after search
         tblAvailableCourses.getColumnModel().getColumn(0).setCellRenderer(new javax.swing.table.DefaultTableCellRenderer() {
            @Override
            public java.awt.Component getTableCellRendererComponent(javax.swing.JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                if (value instanceof Course) {
                    value = ((Course) value).getCourseId();
                }
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        });

        if (model.getRowCount() == 0) {
            JOptionPane.showMessageDialog(this, "No available courses found matching your search criteria.");
        }
    }//GEN-LAST:event_btnSearchActionPerformed

    private void btnClearSearchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearSearchActionPerformed
        // TODO add your handling code here:
        txtSearchTerm.setText("");
       cmbSearchType.setSelectedIndex(0);
       populateAvailableCourses();
    }//GEN-LAST:event_btnClearSearchActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnClearSearch;
    private javax.swing.JButton btnDrop;
    private javax.swing.JButton btnEnroll;
    private javax.swing.JButton btnSearch;
    private javax.swing.JComboBox<String> cmbSearchType;
    private javax.swing.JComboBox<String> cmbSemester;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblAvailableCourses;
    private javax.swing.JLabel lblCurrentCredits;
    private javax.swing.JLabel lblMyCourses;
    private javax.swing.JLabel lblSearchPrompt;
    private javax.swing.JLabel lblSemesterSelect;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTable tblAvailableCourses;
    private javax.swing.JTable tblMyCourses;
    private javax.swing.JTextField txtSearchTerm;
    // End of variables declaration//GEN-END:variables
}

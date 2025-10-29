/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ui;

/**
 *
 * @author deep2
 */
import info5100.university.example.model.Assignment; // Explicit imports from old package structure
import info5100.university.example.model.Submission; // Explicit imports from old package structure
import info5100.university.example.model.directory.AssignmentDirectory; // Explicit imports from old package structure
import info5100.university.example.model.directory.SubmissionDirectory; // Explicit imports from old package structure
import java.awt.CardLayout;
import java.awt.Component; // Import Component for renderer
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import model.*; // Import models from the 'model' package
import model.directory.*; // Import directories from the 'model.directory' package


public class StudentAssignmentPanel extends JPanel {

    // --- Non-UI Member Variables ---
    private JPanel workArea;
    private Student loggedInStudent;
    private EnrollmentDirectory enrollmentDirectory;
    private AssignmentDirectory assignmentDirectory;
    private SubmissionDirectory submissionDirectory;
    private List<Enrollment> studentEnrollments; // To easily find courses

    // --- Constructor ---
    public StudentAssignmentPanel(JPanel workArea, Student loggedInStudent,
                                EnrollmentDirectory enrollmentDirectory, AssignmentDirectory assignmentDirectory,
                                SubmissionDirectory submissionDirectory) {
        this.workArea = workArea;
        this.loggedInStudent = loggedInStudent;
        this.enrollmentDirectory = enrollmentDirectory;
        this.assignmentDirectory = assignmentDirectory;
        this.submissionDirectory = submissionDirectory;
        this.studentEnrollments = new ArrayList<>(); // Initialize

        initComponents(); // Initialize NetBeans components
        findStudentEnrollments();
        setupListeners();// Get enrollments first
        populateSemesterComboBox(); // Then populate semesters
         // Setup listeners AFTER components exist
        // Initial table population is triggered by semester combo listener
    }

    // --- Setup and Listener Methods ---
    private void findStudentEnrollments() {
        studentEnrollments.clear();
        for (Enrollment en : enrollmentDirectory.getEnrollments()) {
            // Ensure student object within enrollment is not null before comparing ID
            if (en.getStudent() != null && en.getStudent().getUniversityId().equals(loggedInStudent.getUniversityId())) {
                studentEnrollments.add(en);
            }
        }
    }

    private void populateSemesterComboBox() {
        cmbSemester.removeAllItems();
        Set<String> semesters = new HashSet<>();
        for (Enrollment en : studentEnrollments) {
            // Ensure course object is not null
            if (en.getCourse() != null) {
                semesters.add(en.getCourse().getSemester());
            }
        }
        List<String> sortedSemesters = new ArrayList<>(semesters);
        Collections.sort(sortedSemesters); // Sort semesters

        DefaultComboBoxModel<String> semesterModel = new DefaultComboBoxModel<>(sortedSemesters.toArray(new String[0]));
        cmbSemester.setModel(semesterModel);

        if (cmbSemester.getItemCount() > 0) {
            cmbSemester.setSelectedIndex(0); // Select first semester by default
            // This selection should trigger the action listener added in setupListeners()
            // which will call populateCourseComboBox()
        } else {
            // No semesters found for this student, explicitly clear course combo and table
            populateCourseComboBox(); // Call this to handle the empty course state
            JOptionPane.showMessageDialog(this, "You are not enrolled in any courses yet.", "No Enrollments", JOptionPane.INFORMATION_MESSAGE);
        }
    }

     private void populateCourseComboBox() {
        // Use DefaultComboBoxModel for type safety
        DefaultComboBoxModel<Course> courseModel = new DefaultComboBoxModel<>();
        cmbCourse.setModel(courseModel); // Set model immediately

        String selectedSemester = (String) cmbSemester.getSelectedItem();
        List<Course> coursesInSemester = new ArrayList<>();

        if (selectedSemester != null) {
            for (Enrollment en : studentEnrollments) {
                // Ensure course object is not null
                if (en.getCourse() != null && en.getCourse().getSemester().equals(selectedSemester)) {
                    coursesInSemester.add(en.getCourse());
                }
            }
        }
        // Add courses to the model
        for(Course c : coursesInSemester){
            courseModel.addElement(c);
        }


        if (cmbCourse.getItemCount() > 0) {
            cmbCourse.setSelectedIndex(0); // Select first course, triggers assignment table population
        } else {
            populateAssignmentTable(); // Explicitly call to clear table if no courses in selected semester
        }
    }


    private void setupListeners() {
        // Semester change updates the course list
        cmbSemester.addActionListener(e -> populateCourseComboBox());
        // Course change updates the assignment list
        cmbCourse.addActionListener(e -> populateAssignmentTable());
        // Button actions
        btnSubmit.addActionListener(e -> submitAssignment());
        btnBack.addActionListener(e -> goBack());
    }

    // --- Data Population ---
    private void populateAssignmentTable() {
        DefaultTableModel model = (DefaultTableModel) tblAssignments.getModel();
        model.setRowCount(0);
        lblStatus.setText("..."); // Reset status

        Course selectedCourse = (Course) cmbCourse.getSelectedItem();
        if (selectedCourse == null) {
             // Clear table if no course selected
             model.setRowCount(0);
             return;
        }


        List<Assignment> assignments = assignmentDirectory.findAssignmentsByCourse(selectedCourse.getCourseId());

        for (Assignment assignment : assignments) {
            Submission submission = submissionDirectory.findSubmissionByStudentAndAssignment(
                                        loggedInStudent.getUniversityId(), assignment.getAssignmentId());

            String status;
            Object scoreDisplay; // Can be Double or String "-"

            if (submission != null) {
                if (submission.getScore() != null) {
                    status = "Graded";
                    scoreDisplay = submission.getScore();
                } else {
                    status = "Submitted, Ungraded";
                    scoreDisplay = "-";
                }
            } else {
                status = "Not Submitted";
                scoreDisplay = "-";
            }

            model.addRow(new Object[]{
                assignment, // Store Assignment object
                assignment.getTitle(),
                assignment.getMaxScore(),
                assignment.getWeightPercentage() * 100.0,
                status,
                scoreDisplay
            });
        }

        // Custom renderer for the first column to display Assignment ID
        tblAssignments.getColumnModel().getColumn(0).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                 if (value instanceof Assignment) {
                    value = ((Assignment) value).getAssignmentId();
                } else {
                    value = ""; // Handle potential null or unexpected type
                }
                // Let the default renderer handle displaying the string value
                return super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        });
    }


    // --- Action Methods ---
    private void submitAssignment() {
        int selectedRow = tblAssignments.getSelectedRow();
        if (selectedRow < 0) {
            lblStatus.setText("Please select an assignment to submit.");
            return;
        }

        // Get the Assignment object from the hidden first column
         int modelRow = tblAssignments.convertRowIndexToModel(selectedRow);
         Object assignValue = tblAssignments.getModel().getValueAt(modelRow, 0);
         if (!(assignValue instanceof Assignment)) {
             lblStatus.setText("Error retrieving assignment details.");
             return; // Should not happen
         }
         Assignment selectedAssignment = (Assignment) assignValue;


        // Check if already submitted
        Submission existingSubmission = submissionDirectory.findSubmissionByStudentAndAssignment(
                                            loggedInStudent.getUniversityId(), selectedAssignment.getAssignmentId());

        if (existingSubmission != null) {
            lblStatus.setText("You have already submitted '" + selectedAssignment.getTitle() + "'.");
             JOptionPane.showMessageDialog(this, "You have already submitted this assignment.", "Already Submitted", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        // Create a new Submission object
        // Generate a unique submission ID (simple example)
        String submissionId = selectedAssignment.getAssignmentId() + "_" + loggedInStudent.getUniversityId() + "_" + System.currentTimeMillis() % 100;
         // Ensure uniqueness (basic check)
         while(submissionDirectory.findSubmissionById(submissionId) != null){
             submissionId = selectedAssignment.getAssignmentId() + "_" + loggedInStudent.getUniversityId() + "_" + (System.currentTimeMillis()+1) % 100;
         }

        Submission newSubmission = new Submission(submissionId, selectedAssignment.getAssignmentId(), loggedInStudent.getUniversityId());

        // Add to the directory
        submissionDirectory.addSubmission(newSubmission);

        // Refresh the table to show the updated status
        populateAssignmentTable();
        lblStatus.setText("Successfully submitted '" + selectedAssignment.getTitle() + "'.");
         JOptionPane.showMessageDialog(this, "Assignment submitted successfully! It is now pending grading.", "Submission Successful", JOptionPane.INFORMATION_MESSAGE);
    }

    private void goBack() {
        workArea.remove(this);
        CardLayout layout = (CardLayout) workArea.getLayout();
        layout.previous(workArea);
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblSemesterSelect = new javax.swing.JLabel();
        cmbSemester = new javax.swing.JComboBox<>();
        lblCourseSelect = new javax.swing.JLabel();
        cmbCourse = new javax.swing.JComboBox<>();
        lblAssignmentsTableTitle = new javax.swing.JLabel();
        scrollPaneAssignments = new javax.swing.JScrollPane();
        tblAssignments = new javax.swing.JTable();
        btnSubmit = new javax.swing.JButton();
        lblStatus = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();

        lblSemesterSelect.setText("Select Semester:");

        lblCourseSelect.setText("Select Course:");

        lblAssignmentsTableTitle.setText("Assignments for Selected Course");

        tblAssignments.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null},
                {null, null, null, null, null, null}
            },
            new String [] {
                "Assignment", "Title", "Max Score", "Weight (%)", "Status", "My Score"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        scrollPaneAssignments.setViewportView(tblAssignments);

        btnSubmit.setText("Submit Selected Assignment");

        lblStatus.setText("...");

        btnBack.setText("Back");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(35, 35, 35)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblAssignmentsTableTitle)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lblSemesterSelect)
                                    .addComponent(lblCourseSelect))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(cmbCourse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(cmbSemester, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnSubmit)
                            .addComponent(scrollPaneAssignments, javax.swing.GroupLayout.PREFERRED_SIZE, 579, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblStatus)
                            .addComponent(btnBack))))
                .addContainerGap(30, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblSemesterSelect)
                    .addComponent(cmbSemester, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblCourseSelect)
                    .addComponent(cmbCourse, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(21, 21, 21)
                .addComponent(lblAssignmentsTableTitle)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(scrollPaneAssignments, javax.swing.GroupLayout.PREFERRED_SIZE, 131, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnSubmit)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblStatus)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 105, Short.MAX_VALUE)
                .addComponent(btnBack)
                .addGap(57, 57, 57))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnSubmit;
    private javax.swing.JComboBox<Course> cmbCourse;
    private javax.swing.JComboBox<String> cmbSemester;
    private javax.swing.JLabel lblAssignmentsTableTitle;
    private javax.swing.JLabel lblCourseSelect;
    private javax.swing.JLabel lblSemesterSelect;
    private javax.swing.JLabel lblStatus;
    private javax.swing.JScrollPane scrollPaneAssignments;
    private javax.swing.JTable tblAssignments;
    // End of variables declaration//GEN-END:variables
}

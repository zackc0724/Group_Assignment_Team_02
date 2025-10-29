/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JPanel.java to edit this template
 */
package ui;

import java.awt.CardLayout;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.table.DefaultTableModel;
import model.Course;
import model.Enrollment;
import model.Student;
import model.directory.EnrollmentDirectory;

/**
 *
 * @author Your Name
 */
public class TranscriptJPanel extends javax.swing.JPanel {

    private JPanel workArea;
    private Student loggedInStudent;
    private EnrollmentDirectory enrollmentDirectory;
    private static final DecimalFormat df = new DecimalFormat("0.00"); // Formatter for GPA

    /**
     * Creates new form TranscriptJPanel
     */
    public TranscriptJPanel(JPanel workArea, Student loggedInStudent, EnrollmentDirectory enrollmentDirectory) {
        this.workArea = workArea;
        this.loggedInStudent = loggedInStudent;
        this.enrollmentDirectory = enrollmentDirectory;
        initComponents();
        populateSemesterComboBox();
        // Listener to update transcript when semester changes
        cmbSemester.addActionListener(e -> displayTranscript());
        // Initial display
        displayTranscript();
    }

     private void populateSemesterComboBox() {
        cmbSemester.removeAllItems();
        cmbSemester.addItem("All Semesters"); // Option to view everything

        Set<String> semesters = new HashSet<>();
        for (Enrollment en : enrollmentDirectory.getEnrollments()) {
            if (en.getStudent().getUniversityId().equals(loggedInStudent.getUniversityId())) {
                semesters.add(en.getCourse().getSemester());
            }
        }

        List<String> sortedSemesters = new ArrayList<>(semesters);
        // Basic sort, might need a custom comparator for proper chronological order
        Collections.sort(sortedSemesters);

        for (String semester : sortedSemesters) {
            cmbSemester.addItem(semester);
        }
    }

     private void displayTranscript() {
        DefaultTableModel model = (DefaultTableModel) tblTranscript.getModel();
        model.setRowCount(0); // Clear table first

        // --- TRANSCRIPT BLOCKING --- 
        if (loggedInStudent.getBalanceDue() > 0) {
            lblTermGPAInfo.setText("Term GPA: N/A");
            lblOverallGPAInfo.setText("Overall GPA: N/A");
            lblAcademicStandingInfo.setText("Academic Standing: N/A");
            // Display message instead of transcript data
            JOptionPane.showMessageDialog(this,
                "Your transcript is currently unavailable due to an outstanding balance of $" + String.format("%.2f", loggedInStudent.getBalanceDue()) + ".\nPlease visit Tuition & Financials to make a payment.",
                "Transcript Unavailable",
                JOptionPane.WARNING_MESSAGE);
            // Optionally, disable semester selection or clear table explicitly
             cmbSemester.setEnabled(false); // Disable semester selection
             lblAcademicStandingInfo.setText("Transcript Blocked (Balance Due)"); // Update standing label
            return; // Stop processing further
        } else {
             cmbSemester.setEnabled(true); // Re-enable semester selection if balance is cleared
        }
        // --- END TRANSCRIPT BLOCKING ---


        String selectedSemester = (String) cmbSemester.getSelectedItem();

        List<Enrollment> allEnrollments = enrollmentDirectory.getEnrollments();
        List<Enrollment> relevantEnrollments = new ArrayList<>();
        List<Enrollment> termEnrollments = new ArrayList<>(); // For Term GPA calculation

        double totalQualityPointsOverall = 0;
        int totalCreditsOverall = 0;
        double termQualityPoints = 0;
        int termCredits = 0;

        // Filter enrollments and calculate overall GPA points (Logic remains the same)
        for (Enrollment en : allEnrollments) {
            if (en.getStudent().getUniversityId().equals(loggedInStudent.getUniversityId())) {
                 relevantEnrollments.add(en);
                 String grade = en.getGrade();
                 int credits = en.getCourse().getCredits();
                 if (!"In Progress".equals(grade)) {
                     totalQualityPointsOverall += getGradePoint(grade) * credits;
                     totalCreditsOverall += credits;
                 }
                 if (selectedSemester != null && (selectedSemester.equals("All Semesters") || selectedSemester.equals(en.getCourse().getSemester()))) {
                     termEnrollments.add(en);
                 }
            }
        }

        // Populate table and calculate term GPA points (Logic remains the same)
        for (Enrollment en : termEnrollments) {
            Course course = en.getCourse();
            String grade = en.getGrade();
            int credits = course.getCredits();

            Object[] row = new Object[5];
            row[0] = course.getSemester();
            row[1] = course.getCourseId();
            row[2] = course.getTitle();
            row[3] = credits;
            row[4] = grade;
            model.addRow(row);

            if (selectedSemester != null && !selectedSemester.equals("All Semesters") && selectedSemester.equals(course.getSemester()) && !"In Progress".equals(grade)) {
                 termQualityPoints += getGradePoint(grade) * credits;
                 termCredits += credits;
            }
        }

        // Calculate GPAs (Logic remains the same)
        double overallGPA = (totalCreditsOverall > 0) ? totalQualityPointsOverall / totalCreditsOverall : 0.0;
        double termGPA = 0.0;
        if (selectedSemester != null && !selectedSemester.equals("All Semesters")){
             termGPA = (termCredits > 0) ? termQualityPoints / termCredits : 0.0;
        } else {
             termGPA = overallGPA;
        }

        // Determine Academic Standing (Logic remains the same)
        String academicStanding = determineAcademicStanding(termGPA, overallGPA, selectedSemester);

        // Display results (Logic remains the same)
        lblTermGPAInfo.setText("Term GPA: " + (selectedSemester != null && !selectedSemester.equals("All Semesters") ? df.format(termGPA) : "N/A"));
        lblOverallGPAInfo.setText("Overall GPA: " + df.format(overallGPA));
        lblAcademicStandingInfo.setText("Academic Standing: " + academicStanding);
    }

    // GPA Calculation Helper
    private double getGradePoint(String letterGrade) {
        // Use a Map for cleaner lookup
        Map<String, Double> gradePoints = new HashMap<>();
        gradePoints.put("A", 4.0);
        gradePoints.put("A-", 3.7);
        gradePoints.put("B+", 3.3);
        gradePoints.put("B", 3.0);
        gradePoints.put("B-", 2.7);
        gradePoints.put("C+", 2.3);
        gradePoints.put("C", 2.0);
        gradePoints.put("C-", 1.7);
        // Assuming F = 0.0, D grades are not in the provided scale
        gradePoints.put("F", 0.0);

        return gradePoints.getOrDefault(letterGrade, 0.0); // Default to 0.0 if grade not found or "In Progress"
    }

    // Academic Standing Logic
    private String determineAcademicStanding(double termGPA, double overallGPA, String selectedSemester) {
         // Standing is only relevant if looking at a specific term's performance
        if (selectedSemester == null || selectedSemester.equals("All Semesters")) {
             // Determine standing based only on Overall GPA if looking at full transcript
             if (overallGPA < 3.0) return "Academic Probation";
             else return "Good Standing";
        }

        // Rules based on the assignment PDF
        if (overallGPA < 3.0) {
            return "Academic Probation"; // Overall GPA < 3.0 overrides everything
        } else if (termGPA < 3.0) {
            return "Academic Warning";   // Term GPA < 3.0, but Overall >= 3.0
        } else {
            return "Good Standing";      // Both Term and Overall >= 3.0
        }
    }


    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        lblTitle = new javax.swing.JLabel();
        cmbSemester = new javax.swing.JComboBox<>();
        jScrollPane1 = new javax.swing.JScrollPane();
        tblTranscript = new javax.swing.JTable();
        lblTermGPAInfo = new javax.swing.JLabel();
        lblOverallGPAInfo = new javax.swing.JLabel();
        lblAcademicStandingInfo = new javax.swing.JLabel();
        btnBack = new javax.swing.JButton();
        lblSemesterSelect = new javax.swing.JLabel();

        lblTitle.setText("Academic Transcript");

        cmbSemester.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "All Semesters", " " }));

        tblTranscript.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "Semester", "Course ID", "Course Name", "Credits", "Grade"
            }
        ));
        jScrollPane1.setViewportView(tblTranscript);

        lblTermGPAInfo.setText("Term GPA: N/A");

        lblOverallGPAInfo.setText("Overall GPA: 0.00");

        lblAcademicStandingInfo.setText("Academic Standing: Good Standing");

        btnBack.setText("Back");
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        lblSemesterSelect.setText("Select Semester");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 600, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(30, 30, 30)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lblAcademicStandingInfo)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(lblOverallGPAInfo)
                                .addComponent(lblTermGPAInfo))
                            .addComponent(btnBack)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(40, 40, 40)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lblSemesterSelect)
                                .addGap(53, 53, 53)
                                .addComponent(cmbSemester, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(lblTitle))))
                .addContainerGap(24, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(lblTitle)
                .addGap(33, 33, 33)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cmbSemester, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSemesterSelect))
                .addGap(27, 27, 27)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 116, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(40, 40, 40)
                .addComponent(lblTermGPAInfo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(lblOverallGPAInfo)
                .addGap(18, 18, 18)
                .addComponent(lblAcademicStandingInfo)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnBack)
                .addContainerGap(112, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
        workArea.remove(this);
        CardLayout layout = (CardLayout) workArea.getLayout();
        layout.previous(workArea);
    }//GEN-LAST:event_btnBackActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JComboBox<String> cmbSemester;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JLabel lblAcademicStandingInfo;
    private javax.swing.JLabel lblOverallGPAInfo;
    private javax.swing.JLabel lblSemesterSelect;
    private javax.swing.JLabel lblTermGPAInfo;
    private javax.swing.JLabel lblTitle;
    private javax.swing.JTable tblTranscript;
    // End of variables declaration//GEN-END:variables
}

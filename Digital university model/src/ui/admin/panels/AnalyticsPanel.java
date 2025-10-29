package ui.admin.panels;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map; // Import Map
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Course;
import model.directory.CourseDirectory;
import model.directory.EnrollmentDirectory;
import model.directory.PersonDirectory;

public class AnalyticsPanel extends javax.swing.JPanel {

    private PersonDirectory personDirectory;
    private CourseDirectory courseDirectory;
    private EnrollmentDirectory enrollmentDirectory;

    // UI Components
    private JTable tblUsers;
    private JTable tblCourses;
    private JTable tblCoursesPerSemester; // New table for course count
    private JLabel lblTotalTuition;

    public AnalyticsPanel(PersonDirectory pd, CourseDirectory cd, EnrollmentDirectory ed) {
        this.personDirectory = pd;
        this.courseDirectory = cd;
        this.enrollmentDirectory = ed;
        initComponents();
        populateUserSummary();
        populateCourseSummary();
        populateCoursesPerSemester(); // Call method to populate new table
    }

    private void populateUserSummary(){
        int adminCount = personDirectory.getAllAdmins().size();
        int facCount = personDirectory.getAllFaculty().size();
        int stuCount = personDirectory.getAllStudents().size();

        DefaultTableModel model = (DefaultTableModel) tblUsers.getModel();
        model.setRowCount(0);
        model.addRow(new Object[]{"Admin", adminCount});
        model.addRow(new Object[]{"Faculty", facCount});
        model.addRow(new Object[]{"Student", stuCount});
    }

    private void populateCourseSummary(){
        DefaultTableModel model = (DefaultTableModel) tblCourses.getModel();
        model.setRowCount(0);

        double totalTuition = 0.0;
        HashMap<String,Integer> enrolledMap = new HashMap<>(); // Not strictly needed here anymore

        for(Course c: courseDirectory.getCourses()){
            int enrolled = enrollmentDirectory.getEnrollmentCountForCourse(c.getCourseId());
            // enrolledMap.put(c.getCourseId(), enrolled); // No longer needed for this table
            double courseRevenue = enrolled * c.getTuitionAmountPerStudent();
            totalTuition += courseRevenue;

            model.addRow(new Object[]{
                c.getCourseId(),
                c.getTitle(),
                c.getSemester(),
                enrolled,
                String.format("$%.2f", courseRevenue) // Format revenue
            });
        }

        lblTotalTuition.setText("Total Tuition Revenue (All Semesters): $" + String.format("%.2f", totalTuition)); // Format total
    }

    // --- New Method to Populate Courses Per Semester Table ---
    private void populateCoursesPerSemester() {
        Map<String, Integer> semesterCounts = new HashMap<>();

        // Count courses for each semester
        for (Course c : courseDirectory.getCourses()) {
            String semester = c.getSemester();
            semesterCounts.put(semester, semesterCounts.getOrDefault(semester, 0) + 1);
        }

        DefaultTableModel model = (DefaultTableModel) tblCoursesPerSemester.getModel();
        model.setRowCount(0);

        // Populate the table
        // Sort keys (semesters) for consistent order if desired
        List<String> sortedSemesters = new ArrayList<>(semesterCounts.keySet());
        Collections.sort(sortedSemesters);

        for (String semester : sortedSemesters) {
            model.addRow(new Object[]{semester, semesterCounts.get(semester)});
        }
    }


    private void initComponents() {
        setBackground(new java.awt.Color(240,255,240));
        setLayout(null); // Using null layout

        int currentY = 10; // Track Y position

        // --- User Summary ---
        JLabel lblUsersTitle = new JLabel("Active Users by Role");
        lblUsersTitle.setBounds(20, currentY, 200, 20);
        add(lblUsersTitle);
        currentY += 20; // Move Y down

        JScrollPane scrollPaneUsers = new JScrollPane();
        tblUsers = new JTable();
        tblUsers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] { "Role", "Active Count" }
        ){
             @Override public boolean isCellEditable(int row, int col){ return false; } // Non-editable
         });
        scrollPaneUsers.setViewportView(tblUsers);
        scrollPaneUsers.setBounds(20, currentY, 300, 80); // Adjusted height
        add(scrollPaneUsers);
        currentY += 80 + 10; // Move Y down

        // --- Courses Per Semester Summary --- (NEW)
        JLabel lblSemesterCoursesTitle = new JLabel("Total Courses Offered Per Semester");
        lblSemesterCoursesTitle.setBounds(340, 10, 240, 20); // Position to the right
        add(lblSemesterCoursesTitle);

        JScrollPane scrollPaneSemesters = new JScrollPane();
        tblCoursesPerSemester = new JTable(); // Initialize new table
        tblCoursesPerSemester.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] { "Semester", "Course Count" } // Columns for new table
        ){
             @Override public boolean isCellEditable(int row, int col){ return false; } // Non-editable
         });
        scrollPaneSemesters.setViewportView(tblCoursesPerSemester);
        scrollPaneSemesters.setBounds(340, 30, 240, 80); // Position below title, height matches user table
        add(scrollPaneSemesters);

        // --- Course Enrollment/Revenue Summary ---
        JLabel lblCoursesTitle = new JLabel("Course Enrollment & Revenue");
        lblCoursesTitle.setBounds(20, currentY, 260, 20);
        add(lblCoursesTitle);
        currentY += 20;

        JScrollPane scrollPaneCourses = new JScrollPane();
        tblCourses = new JTable();
        tblCourses.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] { "CourseID", "Title", "Semester", "Enrolled", "Tuition$" }
        ){
             @Override public boolean isCellEditable(int row, int col){ return false; } // Non-editable
         });
        scrollPaneCourses.setViewportView(tblCourses);
        scrollPaneCourses.setBounds(20, currentY, 560, 140);
        add(scrollPaneCourses);
        currentY += 140 + 10;

        // --- Total Tuition ---
        lblTotalTuition = new JLabel("Total Tuition Revenue: $0.00");
        lblTotalTuition.setBounds(20, currentY, 560, 20);
        add(lblTotalTuition);
    }
}

package ui.admin.panels;

import java.util.HashMap;
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

    private JTable tblUsers;
    private JTable tblCourses;
    private JLabel lblTotalTuition;

    public AnalyticsPanel(PersonDirectory pd, CourseDirectory cd, EnrollmentDirectory ed) {
        this.personDirectory = pd;
        this.courseDirectory = cd;
        this.enrollmentDirectory = ed;
        initComponents();
        populateUserSummary();
        populateCourseSummary();
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
        HashMap<String,Integer> enrolledMap = new HashMap<>();

        for(Course c: courseDirectory.getCourses()){
            int enrolled = enrollmentDirectory.getEnrollmentCountForCourse(c.getCourseId());
            enrolledMap.put(c.getCourseId(), enrolled);
            totalTuition += enrolled * c.getTuitionAmountPerStudent();

            model.addRow(new Object[]{
                c.getCourseId(),
                c.getTitle(),
                c.getSemester(),
                enrolled,
                c.getTuitionAmountPerStudent() * enrolled
            });
        }

        lblTotalTuition.setText("Total Tuition Revenue: $" + totalTuition);
    }

    private void initComponents() {
        setBackground(new java.awt.Color(240,255,240));
        setLayout(null);

        JLabel lblUsersTitle = new JLabel("Active Users by Role");
        lblUsersTitle.setBounds(20,10,200,20);
        add(lblUsersTitle);

        JScrollPane jScrollPane1 = new JScrollPane();
        tblUsers = new JTable();
        tblUsers.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] { "Role", "Active Count" }
        ));
        jScrollPane1.setViewportView(tblUsers);
        jScrollPane1.setBounds(20,30,300,120);
        add(jScrollPane1);

        JLabel lblCoursesTitle = new JLabel("Courses / Enrollment / Revenue");
        lblCoursesTitle.setBounds(20,170,260,20);
        add(lblCoursesTitle);

        JScrollPane jScrollPane2 = new JScrollPane();
        tblCourses = new JTable();
        tblCourses.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] { "CourseID", "Title", "Semester", "Enrolled", "Tuition$" }
        ));
        jScrollPane2.setViewportView(tblCourses);
        jScrollPane2.setBounds(20,190,560,140);
        add(jScrollPane2);

        lblTotalTuition = new JLabel("Total Tuition Revenue: $0");
        lblTotalTuition.setBounds(20,340,560,20);
        add(lblTotalTuition);
        
        JLabel lblUpdated = new JLabel("Last updated: " + java.time.LocalDateTime.now());
        lblUpdated.setBounds(20, 370, 400, 20);
        add(lblUpdated);
    }
}

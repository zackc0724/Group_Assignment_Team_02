
package ui;

import java.awt.CardLayout;
import javax.swing.*;
import model.Admin;
import model.Faculty;
import model.Student;
import model.Course;
import model.directory.PersonDirectory;
import model.directory.CourseDirectory;
import model.directory.EnrollmentDirectory;
import auth.AuthManager;
import auth.UserAccount;
import ui.admin.AdminWorkAreaJPanel;

public class MainJFrame extends javax.swing.JFrame {

    private AuthManager authManager;
    private PersonDirectory personDirectory;
    private CourseDirectory courseDirectory;
    private EnrollmentDirectory enrollmentDirectory;

    private JPanel leftLoginPanel;
    private JPanel rightWorkArea;
    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin;
    private JButton btnLogout;
    private JLabel lblLoginStatus;

    public MainJFrame() {
        initDirectories();
        initComponents();
        seedSampleData();
    }

    private void initDirectories(){
        authManager = new AuthManager();
        personDirectory = new PersonDirectory();
        courseDirectory = new CourseDirectory();
        enrollmentDirectory = new EnrollmentDirectory();
    }

    private void seedSampleData(){
        Admin admin = new Admin("U0001","System Admin","admin@northeastern.edu","555-0000","Administration");
        personDirectory.addPerson(admin);

        Faculty f1 = new Faculty("U1001","Prof. McCarthy","mccarthy@northeastern.edu","555-1111","INFO","Professor");
        Faculty f2 = new Faculty("U1002","Prof. Lin","lin@northeastern.edu","555-2222","CS","Associate Prof");
        personDirectory.addPerson(f1);
        personDirectory.addPerson(f2);

        Student s1 = new Student("U2001","Alice Chen","alice.chen@northeastern.edu","555-3333","INFO");
        Student s2 = new Student("U2002","Bob Patel","bob.patel@northeastern.edu","555-4444","INFO");
        Student s3 = new Student("U2003","Carlos Diaz","carlos.diaz@northeastern.edu","555-5555","CS");
        personDirectory.addPerson(s1);
        personDirectory.addPerson(s2);
        personDirectory.addPerson(s3);

        Course c1 = new Course("INFO5100","App Eng & Dev","Fall 2025", f1, 30, 4000.0);
        Course c2 = new Course("INFO6150","Web Design","Fall 2025", f2, 25, 3800.0);
        courseDirectory.addCourse(c1);
        courseDirectory.addCourse(c2);

        authManager.addAccount(new UserAccount("admin","admin123","Admin","U0001"));
        authManager.addAccount(new UserAccount("alice","pw","Student","U2001"));
        authManager.addAccount(new UserAccount("mccarthy","pw","Faculty","U1001"));
    }

    private void pushWorkPanel(javax.swing.JPanel panel){
        String name = panel.getClass().getSimpleName()+"_"+System.nanoTime();
        rightWorkArea.add(panel, name);
        CardLayout cl = (CardLayout)rightWorkArea.getLayout();
        cl.show(rightWorkArea, name);
    }

    private void initComponents() {
        setTitle("Digital University - Admin Use Case Demo");
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setSize(900,500);
        setLocationRelativeTo(null);
        getContentPane().setLayout(null);

        leftLoginPanel = new JPanel();
        leftLoginPanel.setBackground(new java.awt.Color(200,200,255));
        leftLoginPanel.setLayout(null);
        leftLoginPanel.setBounds(0,0,220,500);

        JLabel lblUser = new JLabel("Username:");
        lblUser.setBounds(20,20,160,20);
        leftLoginPanel.add(lblUser);

        txtUsername = new JTextField("admin");
        txtUsername.setBounds(20,40,160,24);
        leftLoginPanel.add(txtUsername);

        JLabel lblPass = new JLabel("Password:");
        lblPass.setBounds(20,80,160,20);
        leftLoginPanel.add(lblPass);

        txtPassword = new JPasswordField("admin123");
        txtPassword.setBounds(20,100,160,24);
        leftLoginPanel.add(txtPassword);

        btnLogin = new JButton("Login");
        btnLogin.setBounds(20,140,160,24);
        leftLoginPanel.add(btnLogin);

        btnLogout = new JButton("Logout");
        btnLogout.setBounds(20,180,160,24);
        leftLoginPanel.add(btnLogout);

        lblLoginStatus = new JLabel("...");
        lblLoginStatus.setBounds(20,220,180,24);
        leftLoginPanel.add(lblLoginStatus);

        getContentPane().add(leftLoginPanel);

        rightWorkArea = new JPanel();
        rightWorkArea.setLayout(new CardLayout());
        rightWorkArea.setBounds(220,0,680,500);
        getContentPane().add(rightWorkArea);

        btnLogin.addActionListener(evt -> {
            String u = txtUsername.getText();
            String p = new String(txtPassword.getPassword());
            boolean ok = authManager.login(u,p);
            if(ok){
                lblLoginStatus.setText("Login OK: "+authManager.getCurrentUser().getRole());
                if(authManager.isAdmin()){
                    pushWorkPanel(new ui.admin.AdminWorkAreaJPanel(
                        rightWorkArea,
                        authManager,
                        personDirectory,
                        courseDirectory,
                        enrollmentDirectory
                    ));
                } else {
                    lblLoginStatus.setText("Logged in but only Admin UI is implemented.");
                }
            } else {
                lblLoginStatus.setText("Login failed.");
            }
        });

        btnLogout.addActionListener(evt -> {
            authManager.logout();
            lblLoginStatus.setText("Logged out.");
            rightWorkArea.removeAll();
            rightWorkArea.revalidate();
            rightWorkArea.repaint();
        });
    }

    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(() -> {
            new MainJFrame().setVisible(true);
        });
    }
}

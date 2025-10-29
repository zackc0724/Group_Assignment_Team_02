
package ui.admin;

import java.awt.CardLayout;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import auth.AuthManager;
import ui.admin.panels.ManagePersonsPanel;
import ui.admin.panels.ManageAccountsPanel;
import ui.admin.panels.AnalyticsPanel;
import ui.admin.panels.ProfilePanel;
import model.directory.PersonDirectory;
import model.directory.CourseDirectory;
import model.directory.EnrollmentDirectory;

public class AdminWorkAreaJPanel extends javax.swing.JPanel {

    private JPanel cardPanelRef;
    private AuthManager authManager;
    private PersonDirectory personDirectory;
    private CourseDirectory courseDirectory;
    private EnrollmentDirectory enrollmentDirectory;

    public AdminWorkAreaJPanel(JPanel cardPanelRef,
            AuthManager authManager,
            PersonDirectory personDirectory,
            CourseDirectory courseDirectory,
            EnrollmentDirectory enrollmentDirectory) {

        this.cardPanelRef = cardPanelRef;
        this.authManager = authManager;
        this.personDirectory = personDirectory;
        this.courseDirectory = courseDirectory;
        this.enrollmentDirectory = enrollmentDirectory;
        initComponents();
    }

    private void pushPanel(JPanel panel){
        String name = panel.getClass().getSimpleName() + "_" + System.nanoTime();
        cardPanelRef.add(panel, name);
        CardLayout cl = (CardLayout) cardPanelRef.getLayout();
        cl.show(cardPanelRef, name);
    }

    private void initComponents() {
        setBackground(new java.awt.Color(220,220,220));
        setLayout(null);

        JLabel lblHeader = new JLabel("Admin Dashboard - Select a Task");
        lblHeader.setBounds(20,20,260,20);
        add(lblHeader);

        JButton btnPersons = new JButton("Manage Persons");
        btnPersons.setBounds(20,60,200,24);
        btnPersons.addActionListener(evt -> {
            pushPanel(new ManagePersonsPanel(personDirectory));
        });
        add(btnPersons);

        JButton btnAccounts = new JButton("Manage Accounts");
        btnAccounts.setBounds(20,100,200,24);
        btnAccounts.addActionListener(evt -> {
            pushPanel(new ManageAccountsPanel(authManager));
        });
        add(btnAccounts);

        JButton btnAnalytics = new JButton("Analytics Dashboard");
        btnAnalytics.setBounds(20,140,200,24);
        btnAnalytics.addActionListener(evt -> {
            pushPanel(new AnalyticsPanel(personDirectory, courseDirectory, enrollmentDirectory));
        });
        add(btnAnalytics);

        JButton btnProfile = new JButton("My Profile");
        btnProfile.setBounds(20,180,200,24);
        btnProfile.addActionListener(evt -> {
            pushPanel(new ProfilePanel(personDirectory, authManager));
        });
        add(btnProfile);
    }
}

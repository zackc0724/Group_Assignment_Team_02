
package ui.admin.panels;

import auth.AuthManager;
import auth.UserAccount;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class ManageAccountsPanel extends javax.swing.JPanel {

    private AuthManager authManager;

    private JTable tblAccounts;
    private JTextField txtUsername;
    private JTextField txtPassword;
    private JTextField txtLinkedId;
    private JComboBox<String> cmbRole;
    private JLabel lblStatus;

    public ManageAccountsPanel(AuthManager am) {
        this.authManager = am;
        initComponents();
        populateAccounts();
    }

    private boolean isEmpty(String s){
        return s==null || s.trim().length()==0;
    }

    private void populateAccounts(){
        DefaultTableModel model = (DefaultTableModel) tblAccounts.getModel();
        model.setRowCount(0);
        for(UserAccount ua: authManager.getAccounts()){
            Object[] row = new Object[3];
            row[0] = ua.getUsername();
            row[1] = ua.getRole();
            row[2] = ua.getLinkedUniversityId();
            model.addRow(row);
        }
    }

    private void initComponents() {
        setBackground(new java.awt.Color(255,240,240));
        setLayout(null);

        JScrollPane jScrollPane1 = new JScrollPane();
        tblAccounts = new JTable();
        tblAccounts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] { "Username", "Role", "UnivID" }
        ));
        jScrollPane1.setViewportView(tblAccounts);
        jScrollPane1.setBounds(20,20,560,170);
        add(jScrollPane1);

        JLabel lUser = new JLabel("Username:");
        lUser.setBounds(20,210,80,20);
        add(lUser);

        txtUsername = new JTextField();
        txtUsername.setBounds(100,210,150,24);
        add(txtUsername);

        JLabel lPass = new JLabel("Password:");
        lPass.setBounds(270,210,80,20);
        add(lPass);

        txtPassword = new JTextField();
        txtPassword.setBounds(340,210,150,24);
        add(txtPassword);

        JLabel lRole = new JLabel("Role:");
        lRole.setBounds(20,250,80,20);
        add(lRole);

        cmbRole = new JComboBox<>(new String[] { "Admin", "Faculty", "Student" });
        cmbRole.setBounds(100,250,150,24);
        add(cmbRole);

        JLabel lLinked = new JLabel("Linked UnivID:");
        lLinked.setBounds(270,250,100,20);
        add(lLinked);

        txtLinkedId = new JTextField();
        txtLinkedId.setBounds(370,250,120,24);
        add(txtLinkedId);

        JButton btnCreate = new JButton("Create Account");
        btnCreate.setBounds(100,290,390,24);
        btnCreate.addActionListener(evt -> {
            String u = txtUsername.getText();
            String p = txtPassword.getText();
            String r = (String)cmbRole.getSelectedItem();
            String id = txtLinkedId.getText();

            if(isEmpty(u) || isEmpty(p) || isEmpty(r)){
                lblStatus.setText("Missing required field.");
                return;
            }
            authManager.addAccount(new UserAccount(u,p,r,id));
            populateAccounts();
            lblStatus.setText("Account created for " + u);
        });
        add(btnCreate);

        lblStatus = new JLabel("...");
        lblStatus.setBounds(20,330,540,20);
        add(lblStatus);
    }
}

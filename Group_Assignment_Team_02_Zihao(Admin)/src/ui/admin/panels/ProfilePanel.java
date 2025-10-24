
package ui.admin.panels;

import javax.swing.*;
import model.Admin;
import model.directory.PersonDirectory;
import auth.AuthManager;

public class ProfilePanel extends javax.swing.JPanel {

    private PersonDirectory personDirectory;
    private AuthManager authManager;

    private JTextField txtId;
    private JTextField txtName;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JTextField txtDept;
    private JLabel lblStatus;

    public ProfilePanel(PersonDirectory pd, AuthManager am) {
        this.personDirectory = pd;
        this.authManager = am;
        initComponents();
        loadProfile();
    }

    private void loadProfile(){
        if(authManager.getCurrentUser()==null) return;
        String id = authManager.getCurrentUser().getLinkedUniversityId();
        if(id==null) return;

        model.Person p = personDirectory.findById(id);
        if(p instanceof Admin){
            Admin a = (Admin)p;
            txtId.setText(a.getUniversityId());
            txtName.setText(a.getName());
            txtEmail.setText(a.getEmail());
            txtPhone.setText(a.getPhone());
            txtDept.setText(a.getDepartment());
        }
    }

    private boolean isEmpty(String s){
        return s==null || s.trim().isEmpty();
    }

    private void initComponents() {
        setBackground(new java.awt.Color(255,255,240));
        setLayout(null);

        JLabel lblInfo = new JLabel("My Profile (Admin)");
        lblInfo.setBounds(20,10,300,20);
        add(lblInfo);

        JLabel lId = new JLabel("UnivID:");
        lId.setBounds(20,50,60,20);
        add(lId);

        txtId = new JTextField();
        txtId.setEditable(false);
        txtId.setBounds(90,50,160,24);
        add(txtId);

        JLabel lName = new JLabel("Name:");
        lName.setBounds(20,90,60,20);
        add(lName);

        txtName = new JTextField();
        txtName.setBounds(90,90,160,24);
        add(txtName);

        JLabel lEmail = new JLabel("Email:");
        lEmail.setBounds(20,130,60,20);
        add(lEmail);

        txtEmail = new JTextField();
        txtEmail.setBounds(90,130,160,24);
        add(txtEmail);

        JLabel lPhone = new JLabel("Phone:");
        lPhone.setBounds(20,170,60,20);
        add(lPhone);

        txtPhone = new JTextField();
        txtPhone.setBounds(90,170,160,24);
        add(txtPhone);

        JLabel lDept = new JLabel("Dept:");
        lDept.setBounds(20,210,60,20);
        add(lDept);

        txtDept = new JTextField();
        txtDept.setBounds(90,210,160,24);
        add(txtDept);

        JButton btnSave = new JButton("Save Profile");
        btnSave.setBounds(20,260,230,24);
        add(btnSave);

        lblStatus = new JLabel("...");
        lblStatus.setBounds(20,300,300,20);
        add(lblStatus);

        btnSave.addActionListener(evt -> {
            String id = txtId.getText();
            model.Person p = personDirectory.findById(id);
            if(p instanceof Admin){
                if(isEmpty(txtName.getText()) || isEmpty(txtEmail.getText())){
                    lblStatus.setText("Name/Email required.");
                    return;
                }
                Admin a = (Admin)p;
                a.setName(txtName.getText());
                a.setEmail(txtEmail.getText());
                a.setPhone(txtPhone.getText());
                a.setDepartment(txtDept.getText());
                lblStatus.setText("Profile saved.");
            }
        });
    }
}


package ui.admin.panels;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import model.Admin;
import model.Faculty;
import model.Person;
import model.Student;
import model.directory.PersonDirectory;

public class ManagePersonsPanel extends javax.swing.JPanel {

    private PersonDirectory personDirectory;

    private JTable tblPeople;
    private JTextField txtName;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JTextField txtDept;
    private JComboBox<String> cmbRole;
    private JLabel lblStatus;

    public ManagePersonsPanel(PersonDirectory pd) {
        this.personDirectory = pd;
        initComponents();
        populatePeopleTable();
    }

    private void populatePeopleTable(){
        DefaultTableModel model = (DefaultTableModel) tblPeople.getModel();
        model.setRowCount(0);
        for(Person p: personDirectory.getPeople()){
            Object[] row = new Object[5];
            row[0] = p.getUniversityId();
            row[1] = p.getName();
            row[2] = p.getEmail();
            row[3] = p.getDepartment();
            row[4] = (p instanceof Student) ? "Student"
                   : (p instanceof Faculty) ? "Faculty"
                   : (p instanceof Admin) ? "Admin"
                   : "Unknown";
            model.addRow(row);
        }
    }

    private boolean isEmpty(String s){
        return s==null || s.trim().length()==0;
    }

    private String generateId(){
        return "U" + System.currentTimeMillis();
    }

    private void initComponents() {
        setBackground(new java.awt.Color(240,240,255));
        setLayout(null);

        JScrollPane jScrollPane1 = new JScrollPane();
        tblPeople = new JTable();
        tblPeople.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] { "UnivID", "Name", "Email", "Dept", "Role" }
        ));
        jScrollPane1.setViewportView(tblPeople);
        jScrollPane1.setBounds(20,20,560,170);
        add(jScrollPane1);

        JLabel lblN = new JLabel("Name:");
        lblN.setBounds(20,210,60,20);
        add(lblN);

        txtName = new JTextField();
        txtName.setBounds(80,210,150,24);
        add(txtName);

        JLabel lblE = new JLabel("Email:");
        lblE.setBounds(250,210,60,20);
        add(lblE);

        txtEmail = new JTextField();
        txtEmail.setBounds(300,210,150,24);
        add(txtEmail);

        JLabel lblP = new JLabel("Phone:");
        lblP.setBounds(20,250,60,20);
        add(lblP);

        txtPhone = new JTextField();
        txtPhone.setBounds(80,250,150,24);
        add(txtPhone);

        JLabel lblD = new JLabel("Dept:");
        lblD.setBounds(250,250,60,20);
        add(lblD);

        txtDept = new JTextField();
        txtDept.setBounds(300,250,150,24);
        add(txtDept);

        JLabel lblR = new JLabel("Role:");
        lblR.setBounds(20,290,60,20);
        add(lblR);

        cmbRole = new JComboBox<>(new String[] { "Student", "Faculty", "Admin" });
        cmbRole.setBounds(80,290,150,24);
        add(cmbRole);

        JButton btnAddPerson = new JButton("Register Person");
        btnAddPerson.setBounds(250,290,200,24);
        btnAddPerson.addActionListener(evt -> {
            String name = txtName.getText();
            String email = txtEmail.getText();
            String phone = txtPhone.getText();
            String dept = txtDept.getText();
            String role = (String)cmbRole.getSelectedItem();

            if(isEmpty(name) || isEmpty(email)){
                lblStatus.setText("Name and Email are required.");
                return;
            }
            if(personDirectory.emailExists(email)){
                lblStatus.setText("Email already exists.");
                return;
            }
            String newId = generateId();
            if(personDirectory.idExists(newId)){
                lblStatus.setText("Generated ID collision. Try again.");
                return;
            }

            Person p;
            if("Student".equalsIgnoreCase(role)){
                p = new Student(newId, name, email, phone, dept);
            } else if("Faculty".equalsIgnoreCase(role)){
                p = new Faculty(newId, name, email, phone, dept, "Professor");
            } else {
                p = new Admin(newId, name, email, phone, dept);
            }
            personDirectory.addPerson(p);
            populatePeopleTable();
            lblStatus.setText("Person added: " + newId);
        });
        add(btnAddPerson);

        lblStatus = new JLabel("...");
        lblStatus.setBounds(20,330,540,20);
        add(lblStatus);
    }
}

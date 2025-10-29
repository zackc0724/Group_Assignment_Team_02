package ui.admin.panels;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
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
    private JTextField txtId; // Add ID field for viewing/updating
    private JTextField txtName;
    private JTextField txtEmail;
    private JTextField txtPhone;
    private JTextField txtDept;
    private JComboBox<String> cmbRole;
    private JLabel lblStatus;
    // Add buttons
    private JButton btnUpdate;
    private JButton btnDelete;
    private JTextField txtSearchTerm;
private JComboBox<String> cmbSearchType;
private JButton btnSearch;
private JButton btnClearSearch;

    private Person selectedPerson = null; // To track selected person

    public ManagePersonsPanel(PersonDirectory pd) {
        this.personDirectory = pd;
        initComponents();
        populatePeopleTable();

        // Add Mouse Listener to table
        tblPeople.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tblPeople.getSelectedRow();
                if (selectedRow >= 0) {
                    String personId = (String) tblPeople.getValueAt(selectedRow, 0);
                    selectedPerson = personDirectory.findById(personId);

                    if (selectedPerson != null) {
                        txtId.setText(selectedPerson.getUniversityId());
                        txtName.setText(selectedPerson.getName());
                        txtEmail.setText(selectedPerson.getEmail());
                        txtPhone.setText(selectedPerson.getPhone());
                        txtDept.setText(selectedPerson.getDepartment());

                        // Determine and select role in ComboBox
                        if (selectedPerson instanceof Student) {
                            cmbRole.setSelectedItem("Student");
                        } else if (selectedPerson instanceof Faculty) {
                            cmbRole.setSelectedItem("Faculty");
                        } else if (selectedPerson instanceof Admin) {
                            cmbRole.setSelectedItem("Admin");
                        }
                        cmbRole.setEnabled(false); // Role shouldn't be changed during update
                        txtId.setEditable(false);  // ID is key, don't allow editing
                        txtEmail.setEditable(false); // Prevent changing email (used for duplicate check)
                        btnUpdate.setEnabled(true);
                        btnDelete.setEnabled(true);
                        lblStatus.setText("Selected: " + selectedPerson.getUniversityId());
                    } else {
                        clearFieldsAndSelection();
                    }
                } else {
                    clearFieldsAndSelection();
                }
            }
        });
    }

    private void clearFieldsAndSelection() {
        txtId.setText("");
        txtName.setText("");
        txtEmail.setText("");
        txtPhone.setText("");
        txtDept.setText("");
        cmbRole.setSelectedIndex(0);
        cmbRole.setEnabled(true); // Enable role selection for new entries
        txtId.setEditable(false); // ID is always non-editable by user
        txtEmail.setEditable(true); // Allow editing email for new entries
        selectedPerson = null;
        tblPeople.clearSelection();
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);
        lblStatus.setText("...");
    }


    // Keep the original method for showing all people
    private void populatePeopleTable(){
        populatePeopleTable(personDirectory.getPeople()); // Call the overloaded version with all people
    }

    // Create overloaded version to display a specific list
    private void populatePeopleTable(ArrayList<Person> peopleList){
        DefaultTableModel model = (DefaultTableModel) tblPeople.getModel();
        model.setRowCount(0);
        for(Person p: peopleList){ // Iterate over the provided list
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
         // Don't call clearFieldsAndSelection() here, let the calling method manage it
    }

    private boolean isEmpty(String s){
        return s==null || s.trim().isEmpty();
    }

    private String generateId(){
        // Simple ID generation, ensure it's unique enough for the scope
        // Consider checking uniqueness again right before adding
        return "P" + System.currentTimeMillis() % 100000; // Example ID
    }

    // Replace the entire initComponents method
    private void initComponents() {
        setBackground(new java.awt.Color(240,240,255));
        setLayout(null); // Using null layout

        // --- Search Area ---
        JLabel lblSearch = new JLabel("Search By:");
        lblSearch.setBounds(20, 20, 80, 20);
        add(lblSearch);

        cmbSearchType = new JComboBox<>(new String[] { "UnivID", "Name", "Department" });
        cmbSearchType.setBounds(100, 20, 120, 24);
        add(cmbSearchType);

        txtSearchTerm = new JTextField();
        txtSearchTerm.setBounds(230, 20, 150, 24);
        add(txtSearchTerm);

        btnSearch = new JButton("Search");
        btnSearch.setBounds(390, 20, 80, 24);
        btnSearch.addActionListener(evt -> searchPeople()); // Call search method
        add(btnSearch);

        btnClearSearch = new JButton("Clear");
        btnClearSearch.setBounds(480, 20, 80, 24);
        btnClearSearch.addActionListener(evt -> {
             txtSearchTerm.setText("");
             populatePeopleTable(); // Reset table to full list
        });
        add(btnClearSearch);

        // --- Table --- (Adjusted Y position)
        JScrollPane jScrollPane1 = new JScrollPane();
        tblPeople = new JTable();
        tblPeople.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] { "UnivID", "Name", "Email", "Dept", "Role" }
        ) {
             @Override
             public boolean isCellEditable(int row, int column) {
                 return false;
             }
         });
        jScrollPane1.setViewportView(tblPeople);
        jScrollPane1.setBounds(20, 60, 560, 150); // Adjusted Y and Height
        add(jScrollPane1);

         // Add Mouse Listener (same as before)
        tblPeople.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                 int selectedRow = tblPeople.getSelectedRow();
                 if (selectedRow >= 0) {
                     // Get ID from the *table model* in case it's filtered
                     String personId = (String) tblPeople.getModel().getValueAt(tblPeople.convertRowIndexToModel(selectedRow), 0);
                     selectedPerson = personDirectory.findById(personId); // Find in main directory

                     if (selectedPerson != null) {
                         txtId.setText(selectedPerson.getUniversityId());
                         txtName.setText(selectedPerson.getName());
                         txtEmail.setText(selectedPerson.getEmail());
                         txtPhone.setText(selectedPerson.getPhone());
                         txtDept.setText(selectedPerson.getDepartment());
                         if (selectedPerson instanceof Student) cmbRole.setSelectedItem("Student");
                         else if (selectedPerson instanceof Faculty) cmbRole.setSelectedItem("Faculty");
                         else if (selectedPerson instanceof Admin) cmbRole.setSelectedItem("Admin");
                         cmbRole.setEnabled(false);
                         txtId.setEditable(false);
                         txtEmail.setEditable(false);
                         btnUpdate.setEnabled(true);
                         btnDelete.setEnabled(true);
                         lblStatus.setText("Selected: " + selectedPerson.getUniversityId());
                     } else {
                         clearFieldsAndSelection();
                     }
                 } else {
                     clearFieldsAndSelection();
                 }
            }
        });


        // --- Detail Fields Area --- (Adjusted Y positions)
        int fieldYStart = 230; // Starting Y for the fields below the table
        int fieldYGap = 40;

        JLabel lblId = new JLabel("UnivID:");
        lblId.setBounds(20, fieldYStart, 60, 20);
        add(lblId);
        txtId = new JTextField();
        txtId.setBounds(80, fieldYStart, 150, 24);
        txtId.setEditable(false);
        add(txtId);

        JLabel lblN = new JLabel("Name:");
        lblN.setBounds(20, fieldYStart + fieldYGap, 60, 20);
        add(lblN);
        txtName = new JTextField();
        txtName.setBounds(80, fieldYStart + fieldYGap, 150, 24);
        add(txtName);

        JLabel lblE = new JLabel("Email:");
        lblE.setBounds(250, fieldYStart + fieldYGap, 60, 20);
        add(lblE);
        txtEmail = new JTextField();
        txtEmail.setBounds(300, fieldYStart + fieldYGap, 150, 24);
        add(txtEmail);

        JLabel lblP = new JLabel("Phone:");
        lblP.setBounds(20, fieldYStart + 2 * fieldYGap, 60, 20);
        add(lblP);
        txtPhone = new JTextField();
        txtPhone.setBounds(80, fieldYStart + 2 * fieldYGap, 150, 24);
        add(txtPhone);

        JLabel lblD = new JLabel("Dept:");
        lblD.setBounds(250, fieldYStart + 2 * fieldYGap, 60, 20);
        add(lblD);
        txtDept = new JTextField();
        txtDept.setBounds(300, fieldYStart + 2 * fieldYGap, 150, 24);
        add(txtDept);

        JLabel lblR = new JLabel("Role:");
        lblR.setBounds(20, fieldYStart + 3 * fieldYGap, 60, 20);
        add(lblR);
        cmbRole = new JComboBox<>(new String[] { "Student", "Faculty", "Admin" });
        cmbRole.setBounds(80, fieldYStart + 3 * fieldYGap, 150, 24);
        add(cmbRole);

        // --- Buttons Area --- (Adjusted Y positions)
        int buttonYStart = fieldYStart + 4 * fieldYGap;

        JButton btnAddPerson = new JButton("Register Person");
        btnAddPerson.setBounds(20, buttonYStart, 150, 24);
        // Action Listener remains the same as previous step
        btnAddPerson.addActionListener(evt -> {
            String name = txtName.getText(); String email = txtEmail.getText();
            String phone = txtPhone.getText(); String dept = txtDept.getText();
            String role = (String)cmbRole.getSelectedItem();
            if(isEmpty(name) || isEmpty(email)){ lblStatus.setText("Name and Email are required."); return; }
            if(personDirectory.emailExists(email)){ lblStatus.setText("Email already exists. Cannot register."); return; }
            String newId = generateId(); int tries = 0;
            while(personDirectory.idExists(newId) && tries < 5){ newId = generateId(); tries++; }
            if(personDirectory.idExists(newId)){ lblStatus.setText("Could not generate a unique ID. Please try again."); return; }
            Person p;
            if("Student".equalsIgnoreCase(role)) p = new Student(newId, name, email, phone, dept);
            else if("Faculty".equalsIgnoreCase(role)) p = new Faculty(newId, name, email, phone, dept, "Instructor");
            else p = new Admin(newId, name, email, phone, dept);
            personDirectory.addPerson(p); populatePeopleTable();
            lblStatus.setText("Person added: " + newId);
        });
        add(btnAddPerson);

        btnUpdate = new JButton("Update Person");
        btnUpdate.setBounds(180, buttonYStart, 150, 24);
        btnUpdate.setEnabled(false);
        // Action Listener remains the same as previous step
         btnUpdate.addActionListener(evt -> {
             if (selectedPerson == null) { lblStatus.setText("Please select a person to update."); return; }
             String name = txtName.getText(); String phone = txtPhone.getText(); String dept = txtDept.getText();
             if (isEmpty(name)) { lblStatus.setText("Name cannot be empty for update."); return; }
             String updatedId = selectedPerson.getUniversityId();
             selectedPerson.setName(name); selectedPerson.setPhone(phone); selectedPerson.setDepartment(dept);
             populatePeopleTable();
             lblStatus.setText("Person '" + updatedId + "' updated.");
        });
        add(btnUpdate);

        btnDelete = new JButton("Delete Person");
        btnDelete.setBounds(340, buttonYStart, 150, 24);
        btnDelete.setEnabled(false);
        // Action Listener remains the same as previous step
        btnDelete.addActionListener(evt -> {
            if (selectedPerson == null) { lblStatus.setText("Please select a person to delete."); return; }
             int confirm = JOptionPane.showConfirmDialog( this, "Delete '" + selectedPerson.getName() + "' (ID: " + selectedPerson.getUniversityId() + ")?", "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE );
             if (confirm == JOptionPane.YES_OPTION) {
                 String deletedId = selectedPerson.getUniversityId();
                 // TODO: Handle linked accounts/enrollments
                 boolean removed = personDirectory.removePerson(deletedId);
                 if (removed) { populatePeopleTable(); lblStatus.setText("Person '" + deletedId + "' deleted."); }
                 else { lblStatus.setText("Failed to delete person '" + deletedId + "'."); }
             }
        });
        add(btnDelete);

        JButton btnClear = new JButton("Clear Form");
        btnClear.setBounds(500, buttonYStart, 100, 24); // Adjusted width/position
        btnClear.addActionListener(evt -> clearFieldsAndSelection());
        add(btnClear);

        // --- Status Label --- (Adjusted Y position)
        lblStatus = new JLabel("Select a person to modify/delete, or enter details to register.");
        lblStatus.setBounds(20, buttonYStart + fieldYGap, 560, 20);
        add(lblStatus);
    }


    // Add this method
    private void searchPeople() {
        String searchTerm = txtSearchTerm.getText().trim().toLowerCase();
        String searchType = (String) cmbSearchType.getSelectedItem();
        ArrayList<Person> results = new ArrayList<>();

        if (searchTerm.isEmpty()) {
            // If search term is empty, show all people (same as clear search)
            populatePeopleTable();
            return;
        }

        if (searchType == null) return; // Should not happen

        // Perform search based on type
        switch (searchType) {
            case "UnivID":
                Person pById = personDirectory.findById(searchTerm); // findById is case-insensitive already
                if (pById != null) {
                    results.add(pById);
                }
                break;
            case "Name":
                // Use the existing searchByName which is case-insensitive and uses contains
                results = personDirectory.searchByName(searchTerm);
                break;
            case "Department":
                // Use the existing searchByDepartment which is case-insensitive
                results = personDirectory.searchByDepartment(searchTerm);
                break;
        }

        // Populate table with results
        populatePeopleTable(results); // Use overloaded method

        if (results.isEmpty()) {
            lblStatus.setText("No persons found matching criteria.");
        } else {
            lblStatus.setText(results.size() + " person(s) found.");
        }
        // Clear detail fields after search
        clearFieldsAndSelection();
    }
    
}
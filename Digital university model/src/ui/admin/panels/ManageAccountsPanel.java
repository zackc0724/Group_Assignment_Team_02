package ui.admin.panels;

import auth.AuthManager;
import auth.UserAccount;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
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
    // Add buttons for update/delete
    private JButton btnUpdate;
    private JButton btnDelete;

    private UserAccount selectedAccount = null; // To track selected account

    public ManageAccountsPanel(AuthManager am) {
        this.authManager = am;
        initComponents();
        populateAccounts();

        // Add Mouse Listener to table
        tblAccounts.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = tblAccounts.getSelectedRow();
                if (selectedRow >= 0) {
                    String username = (String) tblAccounts.getValueAt(selectedRow, 0);
                    selectedAccount = authManager.findAccountByUsername(username); // Use helper method

                    if (selectedAccount != null) {
                        txtUsername.setText(selectedAccount.getUsername());
                        txtPassword.setText(selectedAccount.getPassword()); // Display current password
                        cmbRole.setSelectedItem(selectedAccount.getRole());
                        txtLinkedId.setText(selectedAccount.getLinkedUniversityId());
                        txtUsername.setEditable(false); // Username is the key, don't allow editing
                        btnUpdate.setEnabled(true);
                        btnDelete.setEnabled(true);
                    } else {
                        clearFieldsAndSelection(); // Account not found (shouldn't happen)
                    }
                } else {
                    clearFieldsAndSelection();
                }
            }
        });
    }

     private void clearFieldsAndSelection() {
        txtUsername.setText("");
        txtPassword.setText("");
        cmbRole.setSelectedIndex(0);
        txtLinkedId.setText("");
        txtUsername.setEditable(true); // Allow editing for new entries
        selectedAccount = null;
        tblAccounts.clearSelection();
        btnUpdate.setEnabled(false);
        btnDelete.setEnabled(false);
        lblStatus.setText("...");
    }

    private boolean isEmpty(String s){
        return s==null || s.trim().isEmpty();
    }

    private void populateAccounts(){
        DefaultTableModel model = (DefaultTableModel) tblAccounts.getModel();
        model.setRowCount(0);
        for(UserAccount ua: authManager.getAccounts()){
            Object[] row = new Object[3];
            row[0] = ua.getUsername(); // Store username (key)
            row[1] = ua.getRole();
            row[2] = ua.getLinkedUniversityId();
            model.addRow(row);
        }
         clearFieldsAndSelection(); // Reset form after populating
    }

    private void initComponents() {
        setBackground(new java.awt.Color(255,240,240));
        setLayout(null); // Using null layout for manual bounds

        JScrollPane jScrollPane1 = new JScrollPane();
        tblAccounts = new JTable();
        tblAccounts.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {},
            new String [] { "Username", "Role", "UnivID" }
        ){
             @Override // Make table non-editable
             public boolean isCellEditable(int row, int column) {
                 return false;
             }
         });
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
        btnCreate.setBounds(20,290,150,24); // Adjusted position
        btnCreate.addActionListener(evt -> {
            String u = txtUsername.getText();
            String p = txtPassword.getText();
            String r = (String)cmbRole.getSelectedItem();
            String id = txtLinkedId.getText();

            if(isEmpty(u) || isEmpty(p) || isEmpty(r)){
                lblStatus.setText("Missing required field (Username, Password, Role).");
                return;
            }
            if (authManager.findAccountByUsername(u) != null) {
                 lblStatus.setText("Username '" + u + "' already exists.");
                 return;
            }

            authManager.addAccount(new UserAccount(u,p,r,id));
            populateAccounts();
            lblStatus.setText("Account created for " + u);
        });
        add(btnCreate);

        btnUpdate = new JButton("Update Account");
        btnUpdate.setBounds(180, 290, 150, 24);
        btnUpdate.setEnabled(false);
        btnUpdate.addActionListener(evt -> {
             if (selectedAccount == null) {
                 lblStatus.setText("Please select an account from the table to update.");
                 return;
             }
             String p = txtPassword.getText();
             String r = (String)cmbRole.getSelectedItem();
             String id = txtLinkedId.getText();

             if (isEmpty(p) || isEmpty(r)) {
                 lblStatus.setText("Password and Role cannot be empty for update.");
                 return;
             }

             // *** FIX: Store username before clearing ***
             String updatedUsername = selectedAccount.getUsername();

             // Update the selected account object
             selectedAccount.setPassword(p);
             selectedAccount.setRole(r);
             selectedAccount.setLinkedUniversityId(id);

             populateAccounts(); // Refresh table and clear selection (which nullifies selectedAccount)

             // *** FIX: Use stored username for message ***
             lblStatus.setText("Account '" + updatedUsername + "' updated.");
        });
        add(btnUpdate);

        btnDelete = new JButton("Delete Account");
        btnDelete.setBounds(340, 290, 150, 24);
        btnDelete.setEnabled(false);
        btnDelete.addActionListener(evt -> {
             if (selectedAccount == null) {
                 lblStatus.setText("Please select an account from the table to delete.");
                 return;
             }

             int confirm = JOptionPane.showConfirmDialog(
                 this,
                 "Are you sure you want to delete account '" + selectedAccount.getUsername() + "'?",
                 "Confirm Deletion",
                 JOptionPane.YES_NO_OPTION,
                 JOptionPane.WARNING_MESSAGE
             );

             if (confirm == JOptionPane.YES_OPTION) {
                 // *** Store username before potential deletion ***
                 String deletedUsername = selectedAccount.getUsername();
                 boolean removed = authManager.removeAccount(deletedUsername);

                 if (removed) {
                    populateAccounts(); // Refresh table and clear selection
                    lblStatus.setText("Account '" + deletedUsername + "' deleted.");
                 } else {
                     lblStatus.setText("Failed to delete account '" + deletedUsername + "'.");
                 }
             }
        });
        add(btnDelete);

         JButton btnClear = new JButton("Clear");
         btnClear.setBounds(500, 290, 80, 24);
         btnClear.addActionListener(evt -> {
             clearFieldsAndSelection();
         });
         add(btnClear);

        lblStatus = new JLabel("Select an account to modify or delete, or enter details to create.");
        lblStatus.setBounds(20,330,560,20);
        add(lblStatus);
    }
}
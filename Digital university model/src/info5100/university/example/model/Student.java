
package model;
import java.util.ArrayList; // Import ArrayList
import java.util.List;
public class Student extends Person {
    private boolean active;
    private double balanceDue; // tuition balance
    private List<String> paymentHistory;

    public Student(String universityId, String name, String email, String phone, String department) {
        super(universityId, name, email, phone, department);
        this.active = true;
        this.balanceDue = 0.0; // Starts at 0 
        this.paymentHistory = new ArrayList<>(); // <-- INITIALIZE LIST
    }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public double getBalanceDue() { return balanceDue; }
    public void setBalanceDue(double balanceDue) { this.balanceDue = balanceDue; }

    // <-- ADD THESE METHODS -->
    public List<String> getPaymentHistory() {
        return paymentHistory;
    }

    public void addPaymentHistoryRecord(String record) {
        if (record != null && !record.isBlank()) {
            this.paymentHistory.add(record);
        }
    }
}

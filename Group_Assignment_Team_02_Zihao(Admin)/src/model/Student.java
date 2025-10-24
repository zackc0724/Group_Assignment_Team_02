
package model;

public class Student extends Person {
    private boolean active;
    private double balanceDue; // tuition balance

    public Student(String universityId, String name, String email, String phone, String department) {
        super(universityId, name, email, phone, department);
        this.active = true;
        this.balanceDue = 0.0;
    }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public double getBalanceDue() { return balanceDue; }
    public void setBalanceDue(double balanceDue) { this.balanceDue = balanceDue; }
}

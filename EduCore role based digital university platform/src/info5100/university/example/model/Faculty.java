
package model;

public class Faculty extends Person {
    private String title;
    private boolean active;

    public Faculty(String universityId, String name, String email, String phone, String department, String title) {
        super(universityId, name, email, phone, department);
        this.title = title;
        this.active = true;
    }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}

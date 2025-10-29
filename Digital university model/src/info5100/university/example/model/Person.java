
package model;

/**
 * Base Person record.
 * UniversityID is unique per person.
 */
public class Person {
    private String universityId;
    private String name;
    private String email;
    private String phone;
    private String department;

    public Person(String universityId, String name, String email, String phone, String department) {
        this.universityId = universityId;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.department = department;
    }

    public String getUniversityId() { return universityId; }
    public void setUniversityId(String universityId) { this.universityId = universityId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getDepartment() { return department; }
    public void setDepartment(String department) { this.department = department; }

    @Override
    public String toString() {
        return universityId + " - " + name;
    }
}

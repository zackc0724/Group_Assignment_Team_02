
package auth;

public class UserAccount {
    private String username;
    private String password;
    private String role; // "Admin", "Faculty", "Student"
    private String linkedUniversityId;

    public UserAccount(String username, String password, String role, String linkedUniversityId){
        this.username = username;
        this.password = password;
        this.role = role;
        this.linkedUniversityId = linkedUniversityId;
    }

    public String getUsername(){ return username; }
    public void setUsername(String username){ this.username = username; }

    public String getPassword(){ return password; }
    public void setPassword(String password){ this.password = password; }

    public String getRole(){ return role; }
    public void setRole(String role){ this.role = role; }

    public String getLinkedUniversityId(){ return linkedUniversityId; }
    public void setLinkedUniversityId(String id){ this.linkedUniversityId = id; }

    @Override
    public String toString(){
        return username + " (" + role + ")";
    }
}

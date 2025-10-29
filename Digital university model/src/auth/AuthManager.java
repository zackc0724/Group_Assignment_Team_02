
package auth;

import java.util.ArrayList;

public class AuthManager {
    private ArrayList<UserAccount> accounts;
    private UserAccount currentUser;

    public AuthManager(){
        accounts = new ArrayList<>();
        currentUser = null;
    }

    public void addAccount(UserAccount ua){
        if(ua!=null){
            accounts.add(ua);
        }
    }

    public UserAccount getCurrentUser(){
        return currentUser;
    }

    public void logout(){
        currentUser = null;
    }

    public boolean isAdmin(){
        return currentUser!=null && "Admin".equalsIgnoreCase(currentUser.getRole());
    }

    public boolean isFaculty(){
        return currentUser!=null && "Faculty".equalsIgnoreCase(currentUser.getRole());
    }

    public boolean isStudent(){
        return currentUser!=null && "Student".equalsIgnoreCase(currentUser.getRole());
    }

    public boolean login(String username, String password){
        for(UserAccount ua: accounts){
            if(ua.getUsername().equals(username) && ua.getPassword().equals(password)){
                currentUser = ua;
                return true;
            }
        }
        return false;
    }

    public ArrayList<UserAccount> getAccounts(){
        return accounts;
    }
    public UserAccount findAccountByLinkedId(String linkedId) {
        if (linkedId == null) return null;
        for (UserAccount ua : accounts) {
            if (linkedId.equals(ua.getLinkedUniversityId())) {
                return ua;
            }
        }
        return null;
    }
    public boolean removeAccount(String username) {
        UserAccount accountToRemove = null;
        for (UserAccount ua : accounts) {
            if (ua.getUsername().equals(username)) {
                accountToRemove = ua;
                break;
            }
        }
        if (accountToRemove != null) {
            // Prevent deleting the currently logged-in admin? (Optional safety)
            // if (currentUser != null && currentUser.getUsername().equals(username)) {
            //     return false; // Cannot delete self
            // }
            accounts.remove(accountToRemove);
            return true;
        }
        return false; // Account not found
    }

    // Optional: Add a method to find an account by username
     public UserAccount findAccountByUsername(String username) {
        if (username == null) return null;
        for (UserAccount ua : accounts) {
            if (username.equals(ua.getUsername())) {
                return ua;
            }
        }
        return null;
    }
}

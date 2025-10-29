/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info5100.university.example.Persona;

/**
 *
 * @author kal bugrara
 */

import info5100.university.example.Persona.Faculty.FacultyDirectory;
import info5100.university.example.Persona.Faculty.FacultyProfile;
import info5100.university.example.workareas.Workarea;

/**
 *
 * @author kal bugrara
 */
public class UserAccount {
    private String username;
    private String password;
    private Person person;
    private Workarea landingworkarea;
    private String role;

    public UserAccount(String username, String password, Person person, Workarea landingworkarea, String role) {
        this.username = username;
        this.password = password;
        this.person = person;
        this.landingworkarea = landingworkarea;
        this.role = role;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Person getPerson() {
        return person;
    }

    public Workarea getLandingworkarea() {
        return landingworkarea;
    }

    public Object getRole() {
        return role;

    }
   


}

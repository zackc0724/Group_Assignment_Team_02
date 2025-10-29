/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info5100.university.example.Persona;

import java.util.ArrayList;

public class PersonDirectory {
    
    private ArrayList<Person> personlist;
    
    public PersonDirectory() {
        personlist = new ArrayList<Person>();
    }

  public Person newPerson(String id, String name, String email, String phone, String password) {
    Person p = new Person(id, name, email, phone, password);
    personlist.add(p);
    return p;

}


    public Person findPerson(String id) {
        for (Person p : personlist) {
            if (p.isMatch(id)) {
                return p;
            }
        }
        return null; // not found
    }
}

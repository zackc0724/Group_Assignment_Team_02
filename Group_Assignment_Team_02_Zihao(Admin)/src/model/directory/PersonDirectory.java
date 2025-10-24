
package model.directory;

import java.util.ArrayList;
import model.Person;
import model.Student;
import model.Faculty;
import model.Admin;

public class PersonDirectory {
    private ArrayList<Person> people;

    public PersonDirectory(){
        people = new ArrayList<>();
    }

    public ArrayList<Person> getPeople(){ return people; }

    public void addPerson(Person p){
        if(p == null) return;
        people.add(p);
    }

    public boolean emailExists(String email){
        if(email == null) return false;
        for(Person p: people){
            if(p.getEmail() != null && p.getEmail().equalsIgnoreCase(email)){
                return true;
            }
        }
        return false;
    }

    public boolean idExists(String id){
        if(id == null) return false;
        for(Person p: people){
            if(p.getUniversityId() != null && p.getUniversityId().equalsIgnoreCase(id)){
                return true;
            }
        }
        return false;
    }

    public ArrayList<Student> getAllStudents(){
        ArrayList<Student> res = new ArrayList<>();
        for(Person p: people){
            if(p instanceof Student){
                res.add((Student)p);
            }
        }
        return res;
    }

    public ArrayList<Faculty> getAllFaculty(){
        ArrayList<Faculty> res = new ArrayList<>();
        for(Person p: people){
            if(p instanceof Faculty){
                res.add((Faculty)p);
            }
        }
        return res;
    }

    public ArrayList<Admin> getAllAdmins(){
        ArrayList<Admin> res = new ArrayList<>();
        for(Person p: people){
            if(p instanceof Admin){
                res.add((Admin)p);
            }
        }
        return res;
    }

    public Person findById(String id){
        for(Person p: people){
            if(p.getUniversityId().equalsIgnoreCase(id)){
                return p;
            }
        }
        return null;
    }

    public ArrayList<Person> searchByName(String name){
        ArrayList<Person> list = new ArrayList<>();
        for(Person p: people){
            if(p.getName().toLowerCase().contains(name.toLowerCase())){
                list.add(p);
            }
        }
        return list;
    }

    public ArrayList<Person> searchByDepartment(String dept){
        ArrayList<Person> list = new ArrayList<>();
        for(Person p: people){
            if(p.getDepartment() != null &&
               p.getDepartment().equalsIgnoreCase(dept)){
                list.add(p);
            }
        }
        return list;
    }
}

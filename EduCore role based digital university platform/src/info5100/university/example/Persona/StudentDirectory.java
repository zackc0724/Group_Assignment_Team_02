/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info5100.university.example.Persona;

import info5100.university.example.Department.Department;
import java.util.ArrayList;

/**
 *
 * @author kal bugrara
 */
public class StudentDirectory {

    Department department;
    ArrayList<StudentProfile> studentlist;

    public StudentDirectory(Department d) {

        department = d;
        studentlist = new ArrayList();
   
    // Add default students
    studentlist.add(new StudentProfile(new Person("S001", "Alice Johnson", "alice@uni.com", "123-456-7890", "pass1")));
    studentlist.add(new StudentProfile(new Person("S002", "Bob Smith", "bob@uni.com", "123-456-7891", "pass2")));
    studentlist.add(new StudentProfile(new Person("S003", "Charlie Lee", "charlie@uni.com", "123-456-7892", "pass3")));
    studentlist.add(new StudentProfile(new Person("S004", "Diana Prince", "diana@uni.com", "123-456-7893", "pass4")));
    studentlist.add(new StudentProfile(new Person("S005", "Ethan Hunt", "ethan@uni.com", "123-456-7894", "pass5")));
    studentlist.add(new StudentProfile(new Person("S006", "Fiona Gallagher", "fiona@uni.com", "123-456-7895", "pass6")));



    }

    public StudentProfile newStudentProfile(Person p) {

        StudentProfile sp = new StudentProfile(p);
        studentlist.add(sp);
        return sp;
    }

    public StudentProfile findStudent(String id) {

        for (StudentProfile sp : studentlist) {

            if (sp.isMatch(id)) {
                return sp;
            }
        }
            return null; //not found after going through the whole list
         }
    

    
}

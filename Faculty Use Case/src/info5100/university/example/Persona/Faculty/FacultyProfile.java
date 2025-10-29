/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info5100.university.example.Persona.Faculty;

import info5100.university.example.Persona.*;
import info5100.university.example.CourseSchedule.CourseOffer;
import java.util.ArrayList;
import info5100.university.example.CourseCatalog.Course;
import info5100.university.example.Department.Department;
import java.util.List;
import java.util.stream.Collectors;


/**
 *
 * @author kal bugrara
 */

public class FacultyProfile {

    Person person;
    ArrayList<FacultyAssignment> facultyassignments; 
    private Department department;  // new field


    public FacultyProfile(Person p, Department dept) {
        person = p;
        this.department = dept;
        facultyassignments = new ArrayList<>();
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }
    

    public double getProfAverageOverallRating() {
        double sum = 0.0;
        for(FacultyAssignment fa : facultyassignments){
            sum += fa.getRating();
        }
        return facultyassignments.size() > 0 ? sum / facultyassignments.size() : 0.0;
    }

    public FacultyAssignment AssignAsTeacher(CourseOffer co){
        FacultyAssignment fa = new FacultyAssignment(this, co);
        facultyassignments.add(fa);
        return fa;
    }

    public boolean isMatch(String id) {
        return person.getPersonId().equals(id);
    }

    // Return list of CourseOffer objects
  public ArrayList<CourseOffer> getAssignedCourses() {
    ArrayList<CourseOffer> assignedCourses = new ArrayList<>();
    for (FacultyAssignment fa : facultyassignments) {
        assignedCourses.add(fa.getCourseOffer());
    }
    return assignedCourses;
}


    public ArrayList<FacultyAssignment> getFacultyAssignments() {
        return facultyassignments;
    }

    public Person getPerson() {
        return person;
    }
}


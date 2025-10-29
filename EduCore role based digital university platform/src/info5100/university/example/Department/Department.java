/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info5100.university.example.Department;

import info5100.university.example.CourseCatalog.Course;
import info5100.university.example.CourseCatalog.CourseCatalog;
import info5100.university.example.CourseSchedule.CourseLoad;
import info5100.university.example.CourseSchedule.CourseOffer;
import info5100.university.example.CourseSchedule.CourseSchedule;
import info5100.university.example.Degree.Degree;
import info5100.university.example.Employer.EmployerDirectory;
import info5100.university.example.Persona.Faculty.FacultyDirectory;
import info5100.university.example.Persona.PersonDirectory;
import info5100.university.example.Persona.StudentDirectory;
import info5100.university.example.Persona.StudentProfile;
import java.util.HashMap;

/**
 *
 * @author kal bugrara
 */
public class Department {

    String name;
    CourseCatalog coursecatalog;
    PersonDirectory persondirectory;
    StudentDirectory studentdirectory;
    FacultyDirectory facultydirectory;
    EmployerDirectory employerdirectory;
    Degree degree;

    HashMap<String, CourseSchedule> mastercoursecatalog;

  public Department(String n) {
    name = n;
    mastercoursecatalog = new HashMap<>();

    // Course Catalog
    coursecatalog = new CourseCatalog(this);

    // Directories
    studentdirectory = new StudentDirectory(this);
    facultydirectory = new FacultyDirectory(this);  // ✅ initialize
    employerdirectory = new EmployerDirectory(this);    // ✅ initialize
    persondirectory = new PersonDirectory();

    // Degree
    degree = new Degree("MSIS");

    }
    public void addCoreCourse(Course c){
        degree.addCoreCourse(c);
        
    }
public void addElectiveCourse(Course c){
        degree.addElectiveCourse(c);
        
    }
    public PersonDirectory getPersonDirectory() {

        return persondirectory;

    }

    public StudentDirectory getStudentDirectory() {
    return studentdirectory;
    }

    public CourseSchedule newCourseSchedule(String semester) {

        CourseSchedule cs = new CourseSchedule(semester, coursecatalog);
        mastercoursecatalog.put(semester, cs);
        return cs;
    }

    public CourseSchedule getCourseSchedule(String semester) {

        return mastercoursecatalog.get(semester);

    }

    public CourseCatalog getCourseCatalog() {

        return coursecatalog;

    }

    public Course newCourse(String n, String nm, int cr) {

        Course c = coursecatalog.newCourse(n, nm, cr);
        return c;
    }

    public int calculateRevenuesBySemester(String semester) {

        CourseSchedule css = mastercoursecatalog.get(semester);

        return css.calculateTotalRevenues();

    }

public void RegisterForAClass(String studentid, String cn, String semester) {

    StudentProfile sp = studentdirectory.findStudent(studentid);
    if (sp == null) {
        System.out.println("Student not found: " + studentid);
        return;
    }

    // Get course load for the semester, create if null
    CourseLoad cl = sp.getCourseLoadBySemester(semester);
    if (cl == null) {
        cl = sp.newCourseLoad(semester); // Create a new CourseLoad
    }

    CourseSchedule cs = mastercoursecatalog.get(semester);
    if (cs == null) {
        System.out.println("Semester not found: " + semester);
        return;
    }

    CourseOffer co = cs.getCourseOfferByNumber(cn);
    if (co == null) {
        System.out.println("Course not found: " + cn + " in " + semester);
        return;
    }

    co.assignEmptySeat(cl); // Now cl is guaranteed non-null
}



    public String getName() {
        return name;
    }
    public HashMap<String, CourseSchedule> getMastercoursecatalog() {
    return mastercoursecatalog;
}

    public FacultyDirectory getFacultydirectory() {
        return facultydirectory;
    }

    public EmployerDirectory getEmployerdirectory() {
        return employerdirectory;
    }

}

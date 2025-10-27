/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info5100.university.example.CourseSchedule;

import info5100.university.example.CourseCatalog.Course;
import info5100.university.example.Persona.Faculty.FacultyAssignment;
import info5100.university.example.Persona.Faculty.FacultyProfile;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author kal bugrara
 */
public class CourseOffer {

    Course course;
    ArrayList<Seat> seatlist;
    FacultyAssignment facultyassignment;
    private int capacity;
    private String schedule;
    
    // 1. ADD NEW PRIVATE FIELD FOR SEMESTER
    private String semester; 
    
    // Add this field
    private boolean enrollmentOpen = false; // default closed

    // Setter
    public void setEnrollmentOpen(boolean status) {
        this.enrollmentOpen = status;
    }

    // Getter
    public boolean isEnrollmentOpen() {
        return this.enrollmentOpen;
    }


    public CourseOffer(Course c) {
        course = c;
        seatlist = new ArrayList();
        capacity = 40;
    }
    
    // OPTIONAL: Overload Constructor to include Semester (Recommended)
    public CourseOffer(Course c, String semester) {
        this.course = c;
        this.seatlist = new ArrayList();
        this.capacity = 40;
        this.semester = semester; // Initialize the semester
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }
    
    
    // 2. ADD THE REQUIRED GETTER METHOD
    public String getSemester() {
        return semester;
    }

    // 3. ADD A SETTER METHOD (Optional, but good practice)
    public void setSemester(String semester) {
        this.semester = semester;
    }
    
    
    public void AssignAsTeacher(FacultyProfile fp) {

        facultyassignment = new FacultyAssignment(fp, this);
    }

    public FacultyProfile getFacultyProfile() {
        return facultyassignment.getFacultyProfile();
    }

    public String getCourseNumber() {
        // Assuming your Course class has getCOurseNumber()
        return course.getCOurseNumber(); 
    }

    public void generatSeats(int n) {

        for (int i = 0; i < n; i++) {

            seatlist.add(new Seat(this, i));

        }

    }

    public Seat getEmptySeat() {

        for (Seat s : seatlist) {

            if (!s.isOccupied()) {
                return s;
            }
        }
        return null;
    }


    public SeatAssignment assignEmptySeat(CourseLoad cl) {

        Seat seat = getEmptySeat();
        if (seat == null) {
            return null;
        }
        SeatAssignment sa = seat.newSeatAssignment(cl); //seat is already linked to course offer
        cl.registerStudent(sa); //coures offer seat is now linked to student
        return sa;
    }

    public int getTotalCourseRevenues() {

        int sum = 0;

        for (Seat s : seatlist) {
            if (s.isOccupied() == true) {
                sum = sum + course.getCoursePrice();
            }

        }
        return sum;
    }
    public Course getSubjectCourse(){
        return course;
    }
    public int getCreditHours(){
        return course.getCredits();
    }

    private String syllabusName;

    public void setSyllabusName(String s) {
        this.syllabusName = s;
    }

    public String getSyllabusName() {
        return this.syllabusName;
    }

    public ArrayList<Seat> getSeatlist() {
        return seatlist;
    }
    
    
}


/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info5100.university.example.CourseSchedule;

import info5100.university.example.CourseCatalog.Course;

/**
 *
 * @author kal bugrara
 */
public class SeatAssignment {
    private float grade; // numeric GPA
    private Seat seat;
    private boolean like;
    private CourseLoad courseload;

    public SeatAssignment(CourseLoad cl, Seat s){
        seat = s;
        courseload = cl;
    }

    public boolean getLike() {
        return like;
    }

    public void setLike(boolean val) {
        like = val;
    }

    public void assignSeatToStudent(CourseLoad cl){
        courseload = cl;
    }

    public int getCreditHours(){
        return seat.getCourseCredits();
    }

    public Seat getSeat(){
        return seat;
    }

    public CourseOffer getCourseOffer(){
        return seat.getCourseOffer();
    }

    public Course getAssociatedCourse(){
        return getCourseOffer().getSubjectCourse();
    }

    public float getCourseStudentScore(){
        return getCreditHours() * grade;
    }

    public String getGrade() {
        if (grade >= 4.0) return "A";
        else if (grade >= 3.7) return "A-";
        else if (grade >= 3.3) return "B+";
        else if (grade >= 3.0) return "B";
        else if (grade >= 2.7) return "B-";
        else if (grade >= 2.3) return "C+";
        else if (grade >= 2.0) return "C";
        else if (grade >= 1.7) return "C-";
        else if (grade >= 1.3) return "D+";
        else if (grade >= 1.0) return "D";
        else return "F";
    }

    public void setGrade(float grade) {
        this.grade = grade;
    }

    float GetCourseStudentScore() {
    return getCreditHours() * grade;
}

}

    


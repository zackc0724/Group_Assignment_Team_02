/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package info5100.university.example.CourseSchedule;

/**
 *
 * @author kal bugrara
 */
public class Seat {
    
    Boolean occupied; 
    int number;
    SeatAssignment seatassignment; // links back to student profile
    CourseOffer courseoffer;

    public Seat(CourseOffer co, int n){
        courseoffer = co;
        number = n;
        occupied = false;
    }
    
    public Boolean isOccupied(){
        return occupied;
    }

    public SeatAssignment newSeatAssignment(CourseLoad cl){
        seatassignment = new SeatAssignment(cl, this); // links seatassignment to seat
        occupied = true;   
        return seatassignment;
    }

    public CourseOffer getCourseOffer(){
        return courseoffer;
    }

    public int getCourseCredits(){
        return courseoffer.getCreditHours();
    }

    // --- FIXED getGrade() ---
    public String getGrade() {
        if (seatassignment != null) {
            return seatassignment.getGrade(); // get grade from SeatAssignment
        }
        return ""; // or null if ungraded
    }

    public void setSeatAssignment(SeatAssignment sa) {
        this.seatassignment = sa;
        occupied = (sa != null);
    }
}

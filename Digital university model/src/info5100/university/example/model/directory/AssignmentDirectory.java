/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package info5100.university.example.model.directory;

/**
 *
 * @author deep2
 */
import java.util.ArrayList;
import java.util.List;
import info5100.university.example.model.Assignment;

public class AssignmentDirectory {
    private List<Assignment> assignments;

    public AssignmentDirectory() {
        this.assignments = new ArrayList<>();
    }

    public List<Assignment> getAssignments() {
        return assignments;
    }

    public void addAssignment(Assignment assignment) {
        if (assignment != null) {
            // Optional: Check for duplicate assignmentId
            this.assignments.add(assignment);
        }
    }

    public Assignment findAssignmentById(String assignmentId) {
        for (Assignment a : assignments) {
            if (a.getAssignmentId().equalsIgnoreCase(assignmentId)) {
                return a;
            }
        }
        return null;
    }

    // Helper to get all assignments for a specific course
    public List<Assignment> findAssignmentsByCourse(String courseId) {
        List<Assignment> courseAssignments = new ArrayList<>();
        for (Assignment a : assignments) {
            if (a.getCourseId().equalsIgnoreCase(courseId)) {
                courseAssignments.add(a);
            }
        }
        return courseAssignments;
    }

     public void removeAssignment(String assignmentId) {
        assignments.removeIf(a -> a.getAssignmentId().equalsIgnoreCase(assignmentId));
    }

    // Helper to check if total weight for a course exceeds 1.0 (100%)
    public double getTotalWeightForCourse(String courseId) {
        double totalWeight = 0;
        for (Assignment a : assignments) {
            if (a.getCourseId().equalsIgnoreCase(courseId)) {
                totalWeight += a.getWeightPercentage();
            }
        }
        return totalWeight;
    }
}

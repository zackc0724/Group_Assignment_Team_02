
package model;

import info5100.university.example.model.Assignment;
import info5100.university.example.model.Submission;
import info5100.university.example.model.directory.AssignmentDirectory;
import info5100.university.example.model.directory.SubmissionDirectory;
import java.util.List; // Import List


public class Enrollment {
    private Student student;
    private Course course;
    private String grade; // Final Letter Grade
    private Double calculatedPercentage; // Store the calculated percentage

    public Enrollment(Student student, Course course){
        this.student = student;
        this.course = course;
        this.grade = "In Progress";
        this.calculatedPercentage = null; // Initially null
    }

    public Student getStudent(){ return student; }
    public Course getCourse(){ return course; }
    public String getGrade() { return grade; }
    public Double getCalculatedPercentage() { return calculatedPercentage; } // Getter for percentage

    // Setter for grade might be used for overrides, or made private/removed
    public void setGrade(String grade) { this.grade = grade; }

    // --- New Method for Automatic Grade Calculation ---
    public void calculateAndUpdateGrade(AssignmentDirectory assignmentDir, SubmissionDirectory submissionDir) {
        List<Assignment> courseAssignments = assignmentDir.findAssignmentsByCourse(this.course.getCourseId());
        List<Submission> studentSubmissions = submissionDir.findSubmissionsByStudentAndCourse(this.student.getUniversityId(), this.course.getCourseId(), assignmentDir);

        if (courseAssignments.isEmpty()) {
            this.calculatedPercentage = null; // No assignments, percentage is undefined
            // Decide what grade should be: "In Progress" or something else?
            // this.grade = "Not Applicable";
            return;
        }

        double totalWeightedScore = 0;
        double totalWeightAchieved = 0; // Sum of weights for GRADED assignments

        for (Assignment assignment : courseAssignments) {
            Submission submission = null;
            // Find the submission for this specific assignment
            for(Submission s : studentSubmissions) {
                if(s.getAssignmentId().equals(assignment.getAssignmentId())) {
                    submission = s;
                    break;
                }
            }

            // If submission exists and is graded
            if (submission != null && submission.getScore() != null) {
                double score = submission.getScore();
                double maxScore = assignment.getMaxScore();
                double weight = assignment.getWeightPercentage();

                // Ensure score doesn't exceed maxScore (or handle appropriately)
                score = Math.min(score, maxScore);
                if (maxScore > 0) { // Avoid division by zero
                    totalWeightedScore += (score / maxScore) * weight;
                }
                totalWeightAchieved += weight;
            } else {
                 // How to handle missing or ungraded submissions?
                 // Option 1: Treat as 0 for calculation (affects percentage)
                 totalWeightedScore += 0;
                 totalWeightAchieved += assignment.getWeightPercentage();

                 // Option 2: Ignore for now (percentage based only on graded items)
                 // Do nothing, totalWeightAchieved won't include this assignment's weight
            }
        }

        // Calculate final percentage
        if (totalWeightAchieved > 0) { // Avoid division by zero
             // Base percentage on the weight of graded assignments
            this.calculatedPercentage = (totalWeightedScore / totalWeightAchieved) * 100.0;
        } else {
            // No assignments graded yet
            this.calculatedPercentage = 0.0; // Or null?
        }

        // Update letter grade based on percentage (using a simple scale)
        this.grade = calculateLetterGrade(this.calculatedPercentage);
    }

    // --- Helper to convert percentage to letter grade ---
    // (This scale might differ from the GPA points scale)
    private String calculateLetterGrade(Double percentage) {
        if (percentage == null) return "In Progress"; // Or "N/A"
        if (percentage >= 93) return "A";
        if (percentage >= 90) return "A-";
        if (percentage >= 87) return "B+";
        if (percentage >= 83) return "B";
        if (percentage >= 80) return "B-";
        if (percentage >= 77) return "C+";
        if (percentage >= 73) return "C";
        if (percentage >= 70) return "C-";
        //[cite_start]// Assuming below C- is F (no D grades mentioned for GPA) [cite: 415]
        else return "F";
    }

}

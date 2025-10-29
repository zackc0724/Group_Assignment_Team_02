/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package info5100.university.example.model;

/**
 *
 * @author deep2
 */
import java.util.Date; // For submission date

public class Submission {
    private String submissionId; // Unique ID (e.g., assignmentId + "_" + studentId)
    private String assignmentId;
    private String studentId;
    private Double score; // Use Double object to allow null (ungraded)
    private Date submissionDate; // Optional: Track when submitted
    // private String filePath; // Optional: Path to submitted file

    public Submission(String submissionId, String assignmentId, String studentId) {
        this.submissionId = submissionId;
        this.assignmentId = assignmentId;
        this.studentId = studentId;
        this.score = null; // Initially ungraded
        this.submissionDate = new Date(); // Record submission time
    }

    // --- Getters ---
    public String getSubmissionId() { return submissionId; }
    public String getAssignmentId() { return assignmentId; }
    public String getStudentId() { return studentId; }
    public Double getScore() { return score; }
    public Date getSubmissionDate() { return submissionDate; }

    // --- Setters ---
    public void setScore(Double score) {
        // Optional: Add validation (score <= maxScore from Assignment?)
        // Requires fetching the Assignment object, might be better done in the calling code.
        this.score = score;
    }

    // No setter for submissionDate assumed, set on creation.

    @Override
    public String toString() {
        return "Submission [" + submissionId + "] Score: " + (score != null ? score : "Ungraded");
    }
}

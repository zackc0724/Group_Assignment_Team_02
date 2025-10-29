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
import info5100.university.example.model.Submission;

public class SubmissionDirectory {
    private List<Submission> submissions;

    public SubmissionDirectory() {
        this.submissions = new ArrayList<>();
    }

    public List<Submission> getSubmissions() {
        return submissions;
    }

    public void addSubmission(Submission submission) {
        if (submission != null) {
            // Optional: Check for duplicate submissionId
            this.submissions.add(submission);
        }
    }

    public Submission findSubmissionById(String submissionId) {
        for (Submission s : submissions) {
            if (s.getSubmissionId().equalsIgnoreCase(submissionId)) {
                return s;
            }
        }
        return null;
    }

    // Helper to get all submissions for a specific student in a specific course
    public List<Submission> findSubmissionsByStudentAndCourse(String studentId, String courseId, AssignmentDirectory assignmentDir) {
        List<Submission> results = new ArrayList<>();
        // Find all assignment IDs for the course first
        List<String> courseAssignmentIds = new ArrayList<>();
        assignmentDir.findAssignmentsByCourse(courseId).forEach(a -> courseAssignmentIds.add(a.getAssignmentId()));

        // Now find submissions matching student and any of those assignment IDs
        for (Submission s : submissions) {
            if (s.getStudentId().equalsIgnoreCase(studentId) && courseAssignmentIds.contains(s.getAssignmentId())) {
                results.add(s);
            }
        }
        return results;
    }

     // Helper to get a specific submission for a student and assignment
    public Submission findSubmissionByStudentAndAssignment(String studentId, String assignmentId) {
        for (Submission s : submissions) {
            if (s.getStudentId().equalsIgnoreCase(studentId) && s.getAssignmentId().equalsIgnoreCase(assignmentId)) {
                return s;
            }
        }
        return null; // No submission found
    }

    // Helper to remove submissions if an assignment or student is deleted (use carefully)
     public void removeSubmissionsForAssignment(String assignmentId) {
        submissions.removeIf(s -> s.getAssignmentId().equalsIgnoreCase(assignmentId));
    }

     public void removeSubmissionsForStudent(String studentId) {
        submissions.removeIf(s -> s.getStudentId().equalsIgnoreCase(studentId));
    }
}

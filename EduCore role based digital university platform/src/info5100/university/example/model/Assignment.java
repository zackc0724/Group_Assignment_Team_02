/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package info5100.university.example.model;

/**
 *
 * @author deep2
 */
public class Assignment {
    private String assignmentId; // Unique ID (e.g., courseId + "_Assign1")
    private String courseId;     // Course this assignment belongs to
    private String title;
    private double maxScore;
    private double weightPercentage; // Weight towards the final grade (e.g., 0.2 for 20%)

    public Assignment(String assignmentId, String courseId, String title, double maxScore, double weightPercentage) {
        // Basic validation
        if (weightPercentage < 0 || weightPercentage > 1.0) {
            throw new IllegalArgumentException("Weight percentage must be between 0.0 and 1.0");
        }
        this.assignmentId = assignmentId;
        this.courseId = courseId;
        this.title = title;
        this.maxScore = maxScore;
        this.weightPercentage = weightPercentage;
    }

    // --- Getters ---
    public String getAssignmentId() { return assignmentId; }
    public String getCourseId() { return courseId; }
    public String getTitle() { return title; }
    public double getMaxScore() { return maxScore; }
    public double getWeightPercentage() { return weightPercentage; }

    // --- Setters (Optional - allow modification?) ---
    public void setTitle(String title) { this.title = title; }
    public void setMaxScore(double maxScore) { this.maxScore = maxScore; }
    public void setWeightPercentage(double weightPercentage) {
         if (weightPercentage < 0 || weightPercentage > 1.0) {
            throw new IllegalArgumentException("Weight percentage must be between 0.0 and 1.0");
        }
        this.weightPercentage = weightPercentage;
    }

    @Override
    public String toString() {
        return title + " (Max: " + maxScore + ", Weight: " + (weightPercentage * 100) + "%)";
    }
}
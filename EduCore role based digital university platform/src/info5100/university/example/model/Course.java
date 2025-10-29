
package model;

public class Course {
    private String courseId;
    private String title;
    private String semester;
    private Faculty faculty; // assigned instructor
    private int capacity;
    private double tuitionAmountPerStudent;
    private String syllabusName;
    private boolean enrollmentOpen;
    private int credits;

    public Course(String courseId, String title, String semester, Faculty faculty, int capacity, double tuitionAmountPerStudent, int credits){
        this.courseId = courseId;
        this.title = title;
        this.semester = semester;
        this.faculty = faculty;
        this.capacity = capacity;
        this.tuitionAmountPerStudent = tuitionAmountPerStudent;
        this.syllabusName = null;
        this.enrollmentOpen = false;
        this.credits = credits;
    }

    public String getCourseId(){ return courseId; }
    public void setCourseId(String courseId){ this.courseId = courseId; }

    public String getTitle(){ return title; }
    public void setTitle(String title){ this.title = title; }

    public String getSemester(){ return semester; }
    public void setSemester(String semester){ this.semester = semester; }

    public Faculty getFaculty(){ return faculty; }
    public void setFaculty(Faculty faculty){ this.faculty = faculty; }

    public int getCapacity(){ return capacity; }
    public void setCapacity(int capacity){ this.capacity = capacity; }

    public double getTuitionAmountPerStudent(){ return tuitionAmountPerStudent; }
    public void setTuitionAmountPerStudent(double amt){ this.tuitionAmountPerStudent = amt; }
    
    public String getSyllabusName() {
        return syllabusName;
    }

    public void setSyllabusName(String syllabusName) {
        this.syllabusName = syllabusName;
    }
    public boolean isEnrollmentOpen() {
        return enrollmentOpen;
    }

    public void setEnrollmentOpen(boolean enrollmentOpen) {
        this.enrollmentOpen = enrollmentOpen;
    }
    
    public int getCredits() {
        return credits;
    }
    // <-- Optional: ADD SETTER FOR credits -->
    public void setCredits(int credits) {
        this.credits = credits;
    }
    @Override
    public String toString(){
        return courseId + " - " + title + " (" + semester + ", " + credits + "cr)";
    }
}

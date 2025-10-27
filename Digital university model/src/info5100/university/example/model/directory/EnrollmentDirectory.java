
package model.directory;

import java.util.ArrayList;
import model.Enrollment;

public class EnrollmentDirectory {
    private ArrayList<Enrollment> enrollments;

    public EnrollmentDirectory(){
        enrollments = new ArrayList<>();
    }

    public ArrayList<Enrollment> getEnrollments(){ return enrollments; }

    public void addEnrollment(Enrollment e){
        if(e!=null) enrollments.add(e);
    }

    public int getEnrollmentCountForCourse(String courseId){
        int count = 0;
        for(Enrollment e: enrollments){
            if(e.getCourse().getCourseId().equalsIgnoreCase(courseId)){
                count++;
            }
        }
        return count;
    }
}

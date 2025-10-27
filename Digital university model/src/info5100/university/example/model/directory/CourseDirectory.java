
package model.directory;

import java.util.ArrayList;
import model.Course;

public class CourseDirectory {
    private ArrayList<Course> courses;

    public CourseDirectory(){
        courses = new ArrayList<>();
    }

    public ArrayList<Course> getCourses(){ return courses; }

    public void addCourse(Course c){
        if(c!=null) courses.add(c);
    }
}

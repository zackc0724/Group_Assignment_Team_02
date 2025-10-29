package info5100.university.example.CourseCatalog;

/**
 *
 * @author kal bugrara
 */
public class Course {

    private String number;
    private String name;
    private int credits;
    private int price = 1500; // per credit hour

    public Course(String name, String number, int credits) {
        this.name = name;
        this.number = number;
        this.credits = credits;
    }

    public String getCOurseNumber() {  // keep the existing method name for compatibility
        return number;
    }

    public int getCoursePrice() {
        return price * credits;
    }

    public int getCredits() {
        return credits;
    }

    public String getCourseName() {
        return name;
    }

    // ✅ Clean, standard name for UI access (offer.getSubjectCourse().getName())
    public String getName() {
        return name;
    }

    // ✅ Optional for printing/debugging or JComboBox display
    @Override
    public String toString() {
        return number + " - " + name;
    }
}

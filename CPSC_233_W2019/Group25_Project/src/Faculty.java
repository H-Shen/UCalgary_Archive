import java.util.ArrayList;

/**
 * The {@code Faculty} class inherits class User and contains methods that initializes the faculty user.
 *
 * @author Group 25
 * @date 2019/04/08
 */
public class Faculty extends User {

    /**
     * @Fields the properties of the object
     */
    private ArrayList<String> coursesTeaching = new ArrayList<>();

    /**
     * The default constructor of Faculty class, it sets the property of role to 'FACULTY'.
     *
     * @date 2019/04/08
     */
    public Faculty() {
        setRole(Constants.ROLE[1]);
    }

    /**
     * Getter of the courses that are currently being taught.
     *
     * @return an arrayList that contains the courses teaching
     * @date 2019/04/08
     */
    public ArrayList<String> getCoursesTeaching() {
        ArrayList<String> result = new ArrayList<>();
        if (coursesTeaching != null) {
            result.addAll(coursesTeaching);
        }
        return result;
    }

    /**
     * Adder of the courses that are currently being taught into an arrayList.
     *
     * @param courseId a string contains the course id courses teaching
     * @date 2019/04/08
     */
    public void addCoursesTeaching(String courseId) {
        if (courseId != null) {
            if (coursesTeaching == null) {
                coursesTeaching = new ArrayList<>();
            }
            if (!coursesTeaching.contains(courseId)) {
                coursesTeaching.add(courseId);
            }
        }
    }
}

import java.util.ArrayList;

/**
 * The {@code Faculty} class inherits class User and contains methods that initializes the faculty user.
 */

public class Faculty extends User {

    private ArrayList<String> coursesTeaching;

    /**
     * Default constructor of Admin class.
     */
    public Faculty() {
        setRole(Constants.ROLE[1]);
    }

    /**
     * Gets the courses that are currently being taught.
     *
     * @return an arrayList that contains the courses teaching.
     */
    public ArrayList<String> getCoursesTeaching() {
        ArrayList<String> result = new ArrayList<>();
        if (coursesTeaching != null) {
            for (String i : coursesTeaching) {
                result.add(i);
            }
        }
        return result;
    }

    /**
     * Adds the courses that are currently being taught into an arrayList.
     *
     * @param courseId a String containing the course id courses teaching.
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

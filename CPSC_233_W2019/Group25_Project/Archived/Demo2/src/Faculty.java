import java.util.ArrayList;

public class Faculty extends User {

    private ArrayList<String> coursesTeaching;

    public Faculty() {
        setRole(Constants.ROLE[1]);
    }

    public ArrayList<String> getCoursesTeaching() {
        ArrayList<String> result = new ArrayList<>();
        if (coursesTeaching != null) {
            for (String i : coursesTeaching) {
                result.add(i);
            }
        }
        return result;
    }

    public void addCoursesTeaching(String courseId) {
        if (courseId != null) {
            if (coursesTeaching == null) {
                coursesTeaching = new ArrayList<>();
            }
            coursesTeaching.add(courseId);
        }
    }
}

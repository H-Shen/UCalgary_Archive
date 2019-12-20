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

    public void setCoursesTeaching(ArrayList<String> coursesTeaching) {
        if (coursesTeaching != null) {
            if (this.coursesTeaching == null) {
                this.coursesTeaching = new ArrayList<>();
            }
            for (String i : coursesTeaching) {
                this.coursesTeaching.add(i);
            }
        }
    }
}

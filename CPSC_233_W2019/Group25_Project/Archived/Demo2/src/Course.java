import java.util.ArrayList;
import java.util.HashSet;

public class Course implements Comparable<Course> {

    /**
     * courseId cannot be changed after the initialization
     */
    private String          courseId;
    private String          courseName;
    private String          courseDescription;
    private String          courseUnits;
    private HashSet<String> prerequisites;
    private HashSet<String> antirequisites;
    private String          canBeRepeated;
    private HashSet<String> studentsWhoAreTaking;
    Course() {
        prerequisites = new HashSet<>();
        antirequisites = new HashSet<>();
        studentsWhoAreTaking = new HashSet<>();
    }

    /**
     * Prerequisites and antirequisites purser
     *
     * @param s
     * @return
     */
    public static HashSet<String> purser(String s) {
        HashSet<String> result = new HashSet<>();
        if (s != null && !s.isEmpty()) {
            String[] temp = s.split(" ");
            for (String str : temp) {
                result.add(str);
            }
        }
        return result;
    }

    /**
     * 3 way-comparison by the course id's lexicographical order.
     *
     * @param o
     * @return -1 if o cannot be cast to User or its course id is smaller, 0 if the course ids are the same, 1 otherwise.
     */
    @Override
    public int compareTo(Course o) {
        if (o != null) {
            return getCourseId().compareTo(o.getCourseId());
        }
        return -1;
    }

    public HashSet<String> getStudentsWhoAreTaking() {
        HashSet<String> result = new HashSet<>();
        for (String i : studentsWhoAreTaking) {
            result.add(i);
        }
        return result;
    }

    public void addStudentsWhoAreTaking(String uuid) {
        studentsWhoAreTaking.add(uuid);
    }

    /**
     * Getter of course Id
     *
     * @return
     */
    public String getCourseId() {
        return courseId;
    }

    /**
     * Setter of course Id
     *
     * @param courseId
     */
    public void setCourseId(String courseId) {

        // Make sure the courseId cannot be changed after being initialization
        if (this.courseId == null && Validation.isCourseIdValid(courseId)) {
            this.courseId = courseId;
        }
    }

    /**
     * Getter of course name
     *
     * @return
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * Setter of course name
     *
     * @param courseName
     */
    public void setCourseName(String courseName) {
        if (Validation.isCourseNameValid(courseName)) {
            this.courseName = courseName;
        }
    }

    /**
     * Getter of course description
     *
     * @return
     */
    public String getCourseDescription() {
        return courseDescription;
    }

    /**
     * Setter of course description
     *
     * @param courseDescription
     */
    public void setCourseDescription(String courseDescription) {
        if (Validation.isCourseDescriptionValid(courseDescription)) {
            this.courseDescription = courseDescription;
        }
    }

    /**
     * Getter of course units
     *
     * @return
     */
    public String getCourseUnits() {
        return courseUnits;
    }

    /**
     * Setter of course units
     *
     * @param courseUnits
     */
    public void setCourseUnits(String courseUnits) {
        if (Validation.isCourseUnitsValid(courseUnits)) {
            this.courseUnits = courseUnits;
        }
    }

    /**
     * Getter of prerequisites array list
     *
     * @return
     */
    public HashSet<String> getPrerequisites() {
        HashSet<String> result = new HashSet<>();
        for (String str : prerequisites) {
            result.add(str);
        }
        return result;
    }

    /**
     * Setter of a course prerequisites
     *
     * @param prerequisites
     */
    public void setPrerequisites(String prerequisites) {
        if (Validation.isPrerequisitesValid(prerequisites)) {
            this.prerequisites = purser(prerequisites);
        }
    }

    public void setPrerequisites(HashSet<String> prerequisites) {
        if (Validation.isAntirequisitesValid(prerequisites)) {
            this.prerequisites.clear();
            for (String str : prerequisites) {
                this.prerequisites.add(str);
            }
        }
    }

    /**
     * Getter of antirequisites hashset
     *
     * @return
     */
    public HashSet<String> getAntirequisites() {
        HashSet<String> result = new HashSet<>();
        for (String str : antirequisites) {
            result.add(str);
        }
        return result;
    }

    public void setAntirequisites(String antirequisites) {
        if (Validation.isAntirequisitesValid(antirequisites)) {
            this.antirequisites = purser(antirequisites);
        }
    }

    public void setAntirequisites(HashSet<String> antirequisites) {
        if (Validation.isAntirequisitesValid(antirequisites)) {
            this.antirequisites.clear();
            for (String str : antirequisites) {
                this.antirequisites.add(str);
            }
        }
    }

    public String getCanBeRepeated() {
        return canBeRepeated;
    }

    public void setCanBeRepeated(String canBeRepeated) {
        if (Validation.isCanBeRepeatedValid(canBeRepeated)) {
            this.canBeRepeated = canBeRepeated;
        }
    }

    // highlight the course property keyword
    public String toString(Constants.COURSE_FIELD highlightField, String keyword) {

        StringBuilder sb = new StringBuilder();

        String courseId;
        if (highlightField == Constants.COURSE_FIELD.COURSE_ID) {
            courseId = TextUI.colorHighlightedSubstring(getCourseId(), keyword);
        } else {
            courseId = getCourseId();
        }
        String courseName;
        if (highlightField == Constants.COURSE_FIELD.COURSE_NAME) {
            courseName = TextUI.colorHighlightedSubstring(getCourseName(), keyword);
        } else {
            courseName = getCourseName();
        }
        String courseDescription;
        if (highlightField == Constants.COURSE_FIELD.COURSE_DESCRIPTION) {
            courseDescription = TextUI.colorHighlightedSubstring(getCourseDescription(), keyword);
        } else {
            courseDescription = getCourseDescription();
        }

        sb.append(Constants.COLOR_WRAPPER("         COURSE_ID : ", Constants.WHITE_BOLD) + courseId + "\n");
        sb.append(Constants.COLOR_WRAPPER("       COURSE_NAME : ", Constants.WHITE_BOLD) + courseName + "\n");
        sb.append(Constants.COLOR_WRAPPER("COURSE DESCRIPTION : ", Constants.WHITE_BOLD) + courseDescription + "\n");
        sb.append(Constants.COLOR_WRAPPER("      COURSE_UNITS : ", Constants.WHITE_BOLD) + getCourseUnits() + "\n");
        sb.append(Constants.COLOR_WRAPPER("     PREREQUISITES : ", Constants.WHITE_BOLD) + String.join(" and ",
                getPrerequisites()) + "\n");
        sb.append(Constants.COLOR_WRAPPER("    ANTIREQUISITES : ", Constants.WHITE_BOLD) + String.join(" and ", getAntirequisites()) + "\n");
        sb.append(Constants.COLOR_WRAPPER("   CAN_BE_REPEATED : ", Constants.WHITE_BOLD) + getCanBeRepeated());

        return sb.toString();

    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append(Constants.COLOR_WRAPPER("         COURSE_ID : ", Constants.WHITE_BOLD) + getCourseId() + "\n");
        sb.append(Constants.COLOR_WRAPPER("       COURSE_NAME : ", Constants.WHITE_BOLD) + getCourseName() + "\n");
        sb.append(Constants.COLOR_WRAPPER("COURSE DESCRIPTION : ", Constants.WHITE_BOLD) + getCourseDescription() + "\n");
        sb.append(Constants.COLOR_WRAPPER("      COURSE_UNITS : ", Constants.WHITE_BOLD) + getCourseUnits() + "\n");
        sb.append(Constants.COLOR_WRAPPER("     PREREQUISITES : ", Constants.WHITE_BOLD) + String.join(" and ",
                getPrerequisites()) + "\n");
        sb.append(Constants.COLOR_WRAPPER("    ANTIREQUISITES : ", Constants.WHITE_BOLD) + String.join(" and ", getAntirequisites()) + "\n");
        sb.append(Constants.COLOR_WRAPPER("   CAN_BE_REPEATED : ", Constants.WHITE_BOLD) + getCanBeRepeated());

        return sb.toString();
    }

    /**
     * Generate the value of corresponding value stored in the database
     *
     * @return
     */
    public String prerequisitesToString() {
        ArrayList<String> temp = new ArrayList<>();
        for (String i : prerequisites) {
            temp.add(i);
        }
        return String.join(" ", temp);
    }

    /**
     * Generate the value of corresponding value stored in the database
     *
     * @return
     */
    public String antirequisitesToString() {
        ArrayList<String> temp = new ArrayList<>();
        for (String i : antirequisites) {
            temp.add(i);
        }
        return String.join(" ", temp);
    }
}

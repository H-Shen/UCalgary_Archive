import java.util.ArrayList;
import java.util.HashSet;

/**
 * The {@code Course} class stores all the course information.
 */
public class Course implements Comparable<Course> {

    /**
     * Fields
     */
    private String          courseId;
    private String          courseName;
    private String          courseDescription;
    private String          courseUnits;
    private HashSet<String> prerequisites;
    private HashSet<String> antirequisites;
    private String          canBeRepeated;
    private HashSet<String> studentsWhoAreTaking;

    /**
     * Constructor
     */
    Course() {
        prerequisites = new HashSet<>();
        antirequisites = new HashSet<>();
        studentsWhoAreTaking = new HashSet<>();
    }

    /**
     * Prerequisites and antirequisites purser
     *
     * @param s
     * @return a Hashset of the results.
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

    /**
     * Allows to the user to remove students who are taking the course.
     *
     * @param uuid a String containing the uuid of the student user.
     */
    public void removeStudentsWhoAreTaking(String uuid) {
        studentsWhoAreTaking.remove(uuid);
    }

    /**
     * Gets the students who are taking the course.
     *
     * @return a Hashset containing the students who are taking the course.
     */
    public HashSet<String> getStudentsWhoAreTaking() {
        HashSet<String> result = new HashSet<>();
        for (String i : studentsWhoAreTaking) {
            result.add(i);
        }
        return result;
    }

    /**
     * Adds the students who are taking the course.
     *
     * @param uuid a String containing the uuid of the student user.
     */
    public void addStudentsWhoAreTaking(String uuid) {
        studentsWhoAreTaking.add(uuid);
    }

    /**
     * Gets the course Id.
     *
     * @return a String containing the course id.
     */
    public String getCourseId() {
        return courseId;
    }

    /**
     * Sets the course Id.
     * The course id is only set if it is valid.
     *
     * @param courseId a String containing the course id.
     */
    public void setCourseId(String courseId) {

        // Make sure the courseId cannot be changed after being initialization
        if (this.courseId == null && Validation.isCourseIdValid(courseId)) {
            this.courseId = courseId;
        }
    }

    /**
     * Gets the course name.
     *
     * @return a String containing the course name.
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * Sets the course name.
     * The course name is only set if it is valid.
     *
     * @param courseName a String containing the course name.
     */
    public void setCourseName(String courseName) {
        if (Validation.isCourseNameValid(courseName)) {
            this.courseName = courseName;
        }
    }

    /**
     * Gets the course description.
     *
     * @return a String containing the course description.
     */
    public String getCourseDescription() {
        return courseDescription;
    }

    /**
     * Sets the course description
     * The course description is only set if it is valid.
     *
     * @param courseDescription a String containing the course description.
     */
    public void setCourseDescription(String courseDescription) {
        if (Validation.isCourseDescriptionValid(courseDescription)) {
            this.courseDescription = courseDescription;
        }
    }

    /**
     * Gets the course units.
     *
     * @return a String containing the course units.
     */
    public String getCourseUnits() {
        return courseUnits;
    }

    /**
     * Sets the course units.
     * The course unit is only set if it is valid.
     *
     * @param courseUnits
     */
    public void setCourseUnits(String courseUnits) {
        if (Validation.isCourseUnitsValid(courseUnits)) {
            this.courseUnits = courseUnits;
        }
    }

    /**
     * Gets the prerequisites as a string.
     *
     * @return a String containing the prerequisites.
     */
    public String getPrerequisitesAsString() {
        return String.join(" ", getPrerequisites());
    }

    /**
     * Getter of antirequisites as a string
     */
    public String getAntirequisitesAsString() {
        return String.join(" ", getAntirequisites());
    }

    /**
     * Gets the prerequisites HashSet.
     *
     * @return a HashSet containing the prerequisites.
     */
    public HashSet<String> getPrerequisites() {
        HashSet<String> result = new HashSet<>();
        for (String str : prerequisites) {
            result.add(str);
        }
        return result;
    }

    /**
     * Sets the prerequisites.
     */
    public void setPrerequisites(HashSet<String> prerequisites) {
        if (prerequisites != null) {
            this.prerequisites.clear();
            for (String str : prerequisites) {
                this.prerequisites.add(str);
            }
        }
    }

    /**
     * Sets the course prerequisites based on the courses exist.
     * The prerequisite  is only set if it is valid.
     *
     * @param prerequisites
     */
    public void setPrerequisitesAsNew(String prerequisites) {
        if (Validation.isPrerequisitesValid(prerequisites)) {
            this.prerequisites = purser(prerequisites);
        }
    }

    /**
     * Gets the antirequisites hashset.
     *
     * @return a hashset containing the antirequisites.
     */
    public HashSet<String> getAntirequisites() {
        HashSet<String> result = new HashSet<>();
        for (String str : antirequisites) {
            result.add(str);
        }
        return result;
    }

    /**
     * Sets the course antirequisites without context.
     * The antirequisites cannot be null.
     *
     * @param antirequisites a hashset containing antirequisites.
     */
    public void setAntirequisites(HashSet<String> antirequisites) {
        if (antirequisites != null) {
            this.antirequisites.clear();
            for (String str : antirequisites) {
                this.antirequisites.add(str);
            }
        }
    }

    /**
     * Sets course antirequisites based on the courses exist.
     * The course antirequisite is only set if it is valid.
     *
     * @param antirequisites a String containing the antirequisites.
     */
    public void setAntirequisitesAsNew(String antirequisites) {
        if (Validation.isAntirequisitesValidAsAString(antirequisites)) {
            this.antirequisites = purser(antirequisites);
        }
    }

    /**
     * Gets the courses that can be repeated.
     *
     * @return A String containing the course that can be repeated.
     */
    public String getCanBeRepeated() {
        return canBeRepeated;
    }

    /**
     * Sets the courses that can be reapeated.
     * The courses is only set if valid.
     *
     * @param canBeRepeated A String containing the courses that can be repeated.
     */
    public void setCanBeRepeated(String canBeRepeated) {
        if (Validation.isCanBeRepeatedValid(canBeRepeated)) {
            this.canBeRepeated = canBeRepeated;
        }
    }

    /**
     * Hightlights the keyword that was searched by the user in the results.
     *
     * @param highlightField the keyword that would be highlighted.
     * @param keyword        a String containing the keyword that the user searches.
     * @return a String containing
     */
    public String toStringWithHighLight(Constants.COURSE_FIELD highlightField, String keyword) {

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

        String courseUnits;
        if (highlightField == Constants.COURSE_FIELD.COURSE_UNITS) {
            courseUnits = TextUI.colorHighlightedSubstring(getCourseUnits(), keyword);
        } else {
            courseUnits = getCourseUnits();
        }

        String prerequisites;
        if (highlightField == Constants.COURSE_FIELD.PREREQUISITES) {
            prerequisites = TextUI.colorHighlightedSubstring(getPrerequisitesAsString(), keyword);
        } else {
            prerequisites = getPrerequisitesAsString();
        }

        String antirequisites;
        if (highlightField == Constants.COURSE_FIELD.ANTIREQUISITES) {
            antirequisites = TextUI.colorHighlightedSubstring(getAntirequisitesAsString(), keyword);
        } else {
            antirequisites = getAntirequisitesAsString();
        }

        sb.append(Constants.COLOR_WRAPPER("         COURSE_ID : ", Constants.WHITE_BOLD) + courseId + "\n");
        sb.append(Constants.COLOR_WRAPPER("       COURSE_NAME : ", Constants.WHITE_BOLD) + courseName + "\n");
        sb.append(Constants.COLOR_WRAPPER("COURSE DESCRIPTION : ", Constants.WHITE_BOLD) + courseDescription + "\n");
        sb.append(Constants.COLOR_WRAPPER("      COURSE_UNITS : ", Constants.WHITE_BOLD) + courseUnits + "\n");
        sb.append(Constants.COLOR_WRAPPER("     PREREQUISITES : ", Constants.WHITE_BOLD) + prerequisites + "\n");
        sb.append(Constants.COLOR_WRAPPER("    ANTIREQUISITES : ", Constants.WHITE_BOLD) + antirequisites + "\n");
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
        sb.append(Constants.COLOR_WRAPPER("     PREREQUISITES : ", Constants.WHITE_BOLD) + getPrerequisitesAsString() + "\n");
        sb.append(Constants.COLOR_WRAPPER("    ANTIREQUISITES : ", Constants.WHITE_BOLD) + getAntirequisitesAsString() + "\n");
        sb.append(Constants.COLOR_WRAPPER("   CAN_BE_REPEATED : ", Constants.WHITE_BOLD) + getCanBeRepeated());

        return sb.toString();
    }

    /**
     * Generate the value of corresponding value stored in the database
     *
     * @return a String containing the prerequisites.
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
     * @return a String containing the antirequisites.
     */
    public String antirequisitesToString() {
        ArrayList<String> temp = new ArrayList<>();
        for (String i : antirequisites) {
            temp.add(i);
        }
        return String.join(" ", temp);
    }
}

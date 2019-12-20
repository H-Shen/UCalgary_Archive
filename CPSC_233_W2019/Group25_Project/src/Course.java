import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

/**
 * The {@code Course} class defines all properties and methods of a course object.
 *
 * @author Group 25
 * @date 2019/04/08
 */
public class Course implements Comparable<Course> {

    /**
     * @Fields properties of a course
     */
    private final HashSet<String> studentsWhoAreTaking;
    private       String          courseId;
    private       String          courseName;
    private       String          courseDescription;
    private       String          courseUnits;
    private       HashSet<String> prerequisites;
    private       HashSet<String> antirequisites;
    private       String          canBeRepeated;

    /**
     * The default constructor, which initializes prerequisites, antirequisites, studentsWhoAreTaking
     *
     * @date 2019/04/08
     */
    public Course() {
        prerequisites = new HashSet<>();
        antirequisites = new HashSet<>();
        studentsWhoAreTaking = new HashSet<>();
    }

    /**
     * The copy constructor, which assigns fields manually instead of using setters since setters will run redundant
     * functions of validation
     *
     * @param other another Course object
     * @date 2019/04/08
     */
    public Course(Course other) {
        courseId = other.getCourseId();
        courseName = other.getCourseName();
        courseDescription = other.getCourseDescription();
        courseUnits = other.getCourseUnits();
        prerequisites = other.getPrerequisites();
        antirequisites = other.getAntirequisites();
        canBeRepeated = other.getCanBeRepeated();
        studentsWhoAreTaking = other.getStudentsWhoAreTaking();
    }

    /**
     * The method parses a string of course Ids and generates a hash set contain all unique course Ids.
     *
     * @param s String to parse
     * @return a hash set of unique course Ids
     * @date 2019/04/08
     */
    public static HashSet<String> parser(String s) {
        HashSet<String> result = new HashSet<>();
        if (s != null && !s.isEmpty()) {
            String[] temp = s.split(" ");
            result.addAll(Arrays.asList(temp));
        }
        return result;
    }

    /**
     * The method defines a 3 way-comparison by the course id's lexicographical order.
     *
     * @param o Another course
     * @return -1 if o cannot be cast to User or its course id is smaller, 0 if the course ids are the same, 1 otherwise.
     * @date 2019/04/08
     */
    @Override
    public int compareTo(Course o) {
        if (o != null) {
            return getCourseId().compareTo(o.getCourseId());
        }
        return -1;
    }

    /**
     * The method allows to the user to remove students who are taking the course, will be used when a faculty is
     * grading
     *
     * @param uuid a String containing the uuid of the student user.
     * @date 2019/04/08
     */
    public void removeStudentsWhoAreTaking(String uuid) {
        studentsWhoAreTaking.remove(uuid);
    }

    /**
     * The method gets the students who are taking the course.
     *
     * @return a hash set containing the students who are taking the course.
     * @date 2019/04/08
     */
    public HashSet<String> getStudentsWhoAreTaking() {
        return new HashSet<>(studentsWhoAreTaking);
    }

    /**
     * The method adds the students who are taking the course to studentsWhoAreTaking.
     *
     * @param uuid a String containing the uuid of the student user.
     * @date 2019/04/08
     */
    public void addStudentsWhoAreTaking(String uuid) {
        studentsWhoAreTaking.add(uuid);
    }

    /**
     * Getter of the course Id.
     *
     * @return a string contains the course id.
     * @date 2019/04/08
     */
    public String getCourseId() {
        return courseId;
    }

    /**
     * Setter the course Id.
     * The course id is only set if it is valid, after the course Id is initialized, it cannot be changed.
     *
     * @param courseId a string contains the course id.
     * @date 2019/04/08
     */
    public void setCourseId(String courseId) {
        if (this.courseId == null && Validation.isCourseIdValid(courseId)) {
            this.courseId = courseId;
        }
    }

    /**
     * Getter the course name.
     *
     * @return a String containing the course name.
     * @date 2019/04/08
     */
    public String getCourseName() {
        return courseName;
    }

    /**
     * Setter the course name.
     * The course name is only set if it is valid.
     *
     * @param courseName a string contains the course name.
     * @date 2019/04/08
     */
    public void setCourseName(String courseName) {
        if (Validation.isCourseNameValid(courseName)) {
            this.courseName = courseName;
        }
    }

    /**
     * Getter of the course description.
     *
     * @return a string containing the course description.
     * @date 2019/04/08
     */
    public String getCourseDescription() {
        return courseDescription;
    }

    /**
     * Setter the course description
     * The course description is only set if it is valid.
     *
     * @param courseDescription a string contains the course description.
     * @date 2019/04/08
     */
    public void setCourseDescription(String courseDescription) {
        if (Validation.isCourseDescriptionValid(courseDescription)) {
            this.courseDescription = courseDescription;
        }
    }

    /**
     * Getter of the course units.
     *
     * @return a string contains the course units.
     * @date 2019/04/08
     */
    public String getCourseUnits() {
        return courseUnits;
    }

    /**
     * Setter of the course units. The course unit is only set if it is valid.
     *
     * @param courseUnits a string contains the course units
     * @date 2019/04/08
     */
    public void setCourseUnits(String courseUnits) {
        if (Validation.isCourseUnitsValid(courseUnits)) {
            this.courseUnits = courseUnits;
        }
    }

    /**
     * Getter the prerequisites as a string.
     *
     * @return a string containing the prerequisites.
     * @date 2019/04/08
     */
    public String getPrerequisitesAsString() {
        return String.join(" ", getPrerequisites());
    }

    /**
     * Getter of antirequisites as a string
     *
     * @return a string containing the antirequisites.
     * @date 2019/04/08
     */
    public String getAntirequisitesAsString() {
        return String.join(" ", getAntirequisites());
    }

    /**
     * Getter of the prerequisites as a hash set.
     *
     * @return a hash set contains the prerequisites.
     * @date 2019/04/08
     */
    public HashSet<String> getPrerequisites() {
        return new HashSet<>(prerequisites);
    }

    /**
     * Setter the prerequisites.
     *
     * @param prerequisites a hash set contains all prerequisites of the course
     * @date 2019/04/08
     */
    public void setPrerequisites(HashSet<String> prerequisites) {
        if (prerequisites != null) {
            this.prerequisites.clear();
            this.prerequisites.addAll(prerequisites);
        }
    }

    /**
     * Setter of the course prerequisites based on the courses exist.
     * The prerequisite is only set if it is valid.
     *
     * @param prerequisites a string represents all prerequisites of a course
     * @date 2019/04/08
     */
    public void setPrerequisitesAsNew(String prerequisites) {
        if (Validation.isPrerequisitesValid(prerequisites)) {
            this.prerequisites = parser(prerequisites);
        }
    }

    /**
     * Getter of the antirequisites hash set.
     *
     * @return a hash set containing the antirequisites
     * @date 2019/04/08
     */
    public HashSet<String> getAntirequisites() {
        return new HashSet<>(antirequisites);
    }

    /**
     * Setter of the course antirequisites without context. The antirequisites cannot be null.
     *
     * @param antirequisites a hash set contains antirequisites
     * @date 2019/04/08
     */
    public void setAntirequisites(HashSet<String> antirequisites) {
        if (antirequisites != null) {
            this.antirequisites.clear();
            this.antirequisites.addAll(antirequisites);
        }
    }

    /**
     * Setter of the course antirequisites based on the courses exist. The course anti-requisite is only set if it is
     * valid.
     *
     * @param antirequisites a String containing the antirequisites
     * @date 2019/04/08
     */
    public void setAntirequisitesAsNew(String antirequisites) {
        if (Validation.isAntirequisitesValidAsAString(antirequisites)) {
            this.antirequisites = parser(antirequisites);
        }
    }

    /**
     * Getter of the courses that can be repeated.
     *
     * @return a String containing the course that can be repeated
     * @date 2019/04/08
     */
    public String getCanBeRepeated() {
        return canBeRepeated;
    }

    /**
     * Setter of the courses that can be repeated.
     * The courses is only set if valid.
     *
     * @param canBeRepeated a string containing the courses that can be repeated
     * @date 2019/04/08
     */
    public void setCanBeRepeated(String canBeRepeated) {
        if (Validation.isCanBeRepeatedValid(canBeRepeated)) {
            this.canBeRepeated = canBeRepeated;
        }
    }

    /**
     * The method highlights the keyword that was searched by the user in the results.
     *
     * @param highlightField the keyword that would be highlighted.
     * @param keyword        a String containing the keyword that the user searches.
     * @return a string whose all occurrences of the keyword are highlighted
     * @date 2019/04/08
     */
    public String toStringWithHighLight(Constants.COURSE_FIELD highlightField, String keyword) {
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

        return Constants.colorWrapper("         COURSE_ID : ", Constants.WHITE_BOLD) + courseId + "\n" +
                Constants.colorWrapper("       COURSE_NAME : ", Constants.WHITE_BOLD) + courseName + "\n" +
                Constants.colorWrapper("COURSE DESCRIPTION : ", Constants.WHITE_BOLD) + courseDescription + "\n" +
                Constants.colorWrapper("      COURSE_UNITS : ", Constants.WHITE_BOLD) + courseUnits + "\n" +
                Constants.colorWrapper("     PREREQUISITES : ", Constants.WHITE_BOLD) + prerequisites + "\n" +
                Constants.colorWrapper("    ANTIREQUISITES : ", Constants.WHITE_BOLD) + antirequisites + "\n" +
                Constants.colorWrapper("   CAN_BE_REPEATED : ", Constants.WHITE_BOLD) + getCanBeRepeated();
    }

    /**
     * The method defines the result of printing a Course object directly.
     *
     * @return a string represents the course
     * @date 2019/04/08
     */
    @Override
    public String toString() {
        return Constants.colorWrapper("         COURSE_ID : ", Constants.WHITE_BOLD) + getCourseId() + "\n" +
                Constants.colorWrapper("       COURSE_NAME : ", Constants.WHITE_BOLD) + getCourseName() + "\n" +
                Constants.colorWrapper("COURSE DESCRIPTION : ", Constants.WHITE_BOLD) + getCourseDescription() + "\n" +
                Constants.colorWrapper("      COURSE_UNITS : ", Constants.WHITE_BOLD) + getCourseUnits() + "\n" +
                Constants.colorWrapper("     PREREQUISITES : ", Constants.WHITE_BOLD) + getPrerequisitesAsString() + "\n" +
                Constants.colorWrapper("    ANTIREQUISITES : ", Constants.WHITE_BOLD) + getAntirequisitesAsString() + "\n" +
                Constants.colorWrapper("   CAN_BE_REPEATED : ", Constants.WHITE_BOLD) + getCanBeRepeated();
    }

    /**
     * The method generates the value of corresponding value stored in the database
     *
     * @return a string contains the prerequisites
     * @date 2019/04/08
     */
    public String prerequisitesToString() {
        return String.join(" ", new ArrayList<>(prerequisites));
    }

    /**
     * The method generates the value of corresponding value stored in the database
     *
     * @return a string contains the antirequisites
     * @date 2019/04/08
     */
    public String antirequisitesToString() {
        return String.join(" ", new ArrayList<>(antirequisites));
    }
}

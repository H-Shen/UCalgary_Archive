import java.util.ArrayList;
import java.util.HashSet;

public class Course {

    /**
     * courseId cannot be changed after the initialization
     */
    private String                     courseId;
    private String                     courseName;
    private String                     courseDescription;
    private String                     courseUnits;
    private ArrayList<HashSet<String>> prerequisites;
    private HashSet<String>            antirequisites;
    private String                     canBeRepeated;


    Course() {
        prerequisites = new ArrayList<>();
        antirequisites = new HashSet<>();
    }

    /**
     * Prerequisites purser
     *
     * @param s
     * @return
     */
    public static ArrayList<HashSet<String>> pursePrerequisites(String s) {

        ArrayList<HashSet<String>> result = new ArrayList<>();
        if (s == null) {
            return result;
        }

        boolean         inBrackets = false;
        HashSet<String> temp       = new HashSet<>();
        StringBuilder   tempSb     = new StringBuilder();
        for (int i = 0; i < s.length(); ++i) {
            if (inBrackets) {
                if (s.charAt(i) == ' ') {
                    temp.add(tempSb.toString());
                    tempSb = new StringBuilder();
                } else if (s.charAt(i) == ']') {
                    temp.add(tempSb.toString());
                    result.add(temp);
                    tempSb = new StringBuilder();
                    temp = new HashSet<>();
                    inBrackets = false;
                } else {
                    tempSb.append(s.charAt(i));
                }
            } else {
                if (s.charAt(i) == '[') {
                    inBrackets = true;
                } else if (s.charAt(i) == ' ' && tempSb.length() > 0) {
                    temp.add(tempSb.toString());
                    result.add(temp);
                    tempSb = new StringBuilder();
                    temp = new HashSet<>();
                } else if (s.charAt(i) != ' ') {
                    tempSb.append(s.charAt(i));
                }
            }
        }
        if (tempSb.length() > 0) {
            temp.add(tempSb.toString());
            result.add(temp);
        }
        return result;
    }

    /**
     * Antirequisites purser
     *
     * @param s
     * @return
     */
    public static HashSet<String> purseAntirequisites(String s) {
        HashSet<String> result = new HashSet<>();
        if (s != null) {
            String[] temp = s.split(" ");
            for (String str : temp) {
                result.add(str);
            }
        }
        return result;
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
    public ArrayList<HashSet<String>> getPrerequisites() {
        ArrayList<HashSet<String>> result = new ArrayList<>();
        for (HashSet<String> item : prerequisites) {
            HashSet<String> itemDeepCopy = new HashSet<>();
            for (String str : item) {
                itemDeepCopy.add(str);
            }
            result.add(itemDeepCopy);
        }
        return result;
    }

    /**
     * Setter of course prerequisites which accepts an arraylist
     *
     * @param prerequisites
     */
    public void setPrerequisites(ArrayList<HashSet<String>> prerequisites) {
        if (Validation.isPrerequisitesValid(prerequisites)) {
            this.prerequisites.clear();
            for (HashSet<String> item : prerequisites) {
                HashSet<String> itemDeepCopy = new HashSet<>();
                for (String str : item) {
                    itemDeepCopy.add(str);
                }
                this.prerequisites.add(itemDeepCopy);
            }
        }
    }

    /**
     * Setter of a course prerequisites
     *
     * @param prerequisites
     */
    public void setPrerequisites(String prerequisites) {
        if (Validation.isPrerequisitesValid(prerequisites)) {
            this.prerequisites = pursePrerequisites(prerequisites);
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
            this.antirequisites = purseAntirequisites(antirequisites);
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

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("         COURSE_ID : " + getCourseId() + "\n");
        sb.append("       COURSE_NAME : " + getCourseName() + "\n");
        sb.append("COURSE DESCRIPTION : " + getCourseDescription() + "\n");
        sb.append("      COURSE_UNITS : " + getCourseUnits() + "\n");

        String                     temp;
        ArrayList<HashSet<String>> prerequisites = getPrerequisites();
        sb.append("     PREREQUISITES : ");
        for (int i = 0; i < prerequisites.size(); ++i) {
            temp = String.join(" or ", prerequisites.get(i));
            if (temp.equals("CONSENT")) {
                temp = "Consent by the department";
            }
            if (i != 0) {
                sb.append("                     ");
            }
            sb.append(temp + "\n");
        }
        sb.append("\n");
        sb.append("    ANTIREQUISITES : " + String.join(" and ", getAntirequisites()) + "\n");
        sb.append("   CAN_BE_REPEATED : " + getCanBeRepeated());
        return sb.toString();
    }

    /**
     * Generate the value of corresponding value stored in the database
     *
     * @return
     */
    public String prerequisitesToString() {
        ArrayList<String> result = new ArrayList<>();
        for (HashSet<String> i : prerequisites) {
            String temp = String.join(" ", i);
            if (i.size() == 1) {
                result.add(temp);
            } else {
                result.add("[" + temp + "]");
            }
        }
        return String.join(" ", result);
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

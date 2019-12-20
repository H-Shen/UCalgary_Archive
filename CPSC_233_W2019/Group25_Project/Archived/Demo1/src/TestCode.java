/**
 * Store all temporary testing code snipet.
 */

public class TestCode {

    public static void accountUpdateCommitTest() {
        Database.initializeAccounts();
        System.out.println(Database.accounts.get("3e45b1dc-1c3e-4db6-a1a3-951d67392874").getPassword());
        Database.accounts.get("3e45b1dc-1c3e-4db6-a1a3-951d67392874").setPassword("8oZ6coeTEbwCdVIh7yed7k7NbiDltVU3qlU9Dfcu");
        Database.accountUpdateCommit("3e45b1dc-1c3e-4db6-a1a3-951d67392874", Constants.USER_FIELD.PASSWORD);
        Database.initializeAccounts();
        System.out.println(Database.accounts.get("3e45b1dc-1c3e-4db6-a1a3-951d67392874").getPassword());
    }

    public static void courseUpdateCommitTest() {

        Database.initializeCourses();
        System.out.println(Database.courses.get("ENGG200"));
        Database.courses.get("ENGG200").setCourseDescription("1xxx23");
        System.out.println();
        System.out.println(Database.courses.get("ENGG200"));
        Database.courseUpdateCommit("ENGG200", Constants.COURSE_FIELD.COURSE_DESCRIPTION);
        System.out.println();
        System.out.println(Database.courses.get("ENGG200"));
    }

    public static void createCourseCommitTest() {

        Database.initializeCourses();
        Course a = new Course();
        a.setCourseId("CPSC2310");
        a.setCourseName("Computer Science 2310");
        a.setCourseDescription("Whatever");
        a.setCourseUnits("1.0");
        a.setPrerequisites("[CPSC231 CPSC233] CPSC331");
        a.setAntirequisites("CPSC219");
        a.setCanBeRepeated("YES");
        Database.createCourseCommit(a);
    }
}

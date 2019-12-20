import org.junit.Test;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Objects;

import static org.junit.Assert.*;

/**
 * The {@code DatabaseTest} class for testing Database class.
 *
 * @author Group 25
 * @date 2019/04/08
 */
public class DatabaseTest {

    /**
     * Test if every database file can be successfully connected.
     *
     * @date 2019/04/08
     */
    @Test
    public void test_createConnection() {
        assertTrue(Database.isValid(Database.ACCOUNT_DATA));
        assertTrue(Database.isValid(Database.COURSES));
        assertTrue(Database.isValid(Database.ACADEMIC_REQUIREMENTS));
        assertTrue(Database.isValid(Database.FACULTY));
        assertTrue(Database.isValid(Database.STUDENTS));
        assertTrue(Database.isValid(Database.INTERNSHIP_REQUIREMENTS));
        assertTrue(Database.isValid(Database.COURSES_ORIGINAL));
    }

    /**
     * Test if every containers can be cleared after the execution of Database.releaseAllResources()
     *
     * @date 2019/04/08
     */
    @Test
    public void test_releaseAllResources() {
        Database.initialization();
        Database.releaseAllResources();
        assertTrue(Database.accounts.isEmpty());
        assertTrue(Database.accountsForGUI.isEmpty());
        assertTrue(Database.courses.isEmpty());
        assertTrue(Database.coursesForGUI.isEmpty());
        assertTrue(Database.optionalCourses.isEmpty());
        assertTrue(Database.optionalCoursesForInternship.isEmpty());
        assertTrue(Database.mandatoryCoursesForInternship.isEmpty());
        assertTrue(Database.mandatoryCourses.isEmpty());
        assertTrue(Database.usernameToUuid.isEmpty());
    }

    @Test
    public void test_initializeAccounts() {
        Database.initializeAccounts();
        User demo = Database.userAuthentication("demoStudent", "demodemo");
        assertNotNull(Objects.requireNonNull(demo).getAddress());
    }

    /**
     * Make sure the item of student in STUDENT.db is one-to-one and onto to ACCOUNT.db
     *
     * @date 2019/04/12
     */
    @Test
    public void test_student_database_integrity() {

        HashSet<String> a = new HashSet<>();
        HashSet<String> b = new HashSet<>();

        if (Database.isValid(Database.ACCOUNT_DATA) && Database.isValid(Database.STUDENTS)) {
            String selectA = "SELECT * FROM USER;";
            String selectB = "SELECT * FROM STUDENTS";
            try (Connection connectionA = Database.createConnection(Database.ACCOUNT_DATA, true);
                 Connection connectionB = Database.createConnection(Database.STUDENTS, true);
                 Statement statementA = connectionA.createStatement();
                 Statement statementB = connectionB.createStatement();
                 ResultSet resultSetA = statementA.executeQuery(selectA);
                 ResultSet resultSetB = statementB.executeQuery(selectB)
            ) {
                while (resultSetA.next()) {
                    if ("STUDENT".equals(resultSetA.getString("ROLE"))) {
                        a.add(resultSetA.getString("UUID"));
                    }
                }
                while (resultSetB.next()) {
                    b.add(resultSetB.getString("UUID"));
                }

                // make sure two hash sets contain exactly the same items
                assertEquals(a.size(), b.size());
                for (String i : a) {
                    assertTrue(b.contains(i));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Make sure the item of faculty in FACULTY.db is one-to-one and onto to ACCOUNT.db
     *
     * @date 2019/04/12
     */
    @Test
    public void test_faculty_database_integrity() {

        HashSet<String> a = new HashSet<>();
        HashSet<String> b = new HashSet<>();

        if (Database.isValid(Database.ACCOUNT_DATA) && Database.isValid(Database.FACULTY)) {
            String selectA = "SELECT * FROM USER;";
            String selectB = "SELECT * FROM FACULTY";
            try (Connection connectionA = Database.createConnection(Database.ACCOUNT_DATA, true);
                 Connection connectionB = Database.createConnection(Database.FACULTY, true);
                 Statement statementA = connectionA.createStatement();
                 Statement statementB = connectionB.createStatement();
                 ResultSet resultSetA = statementA.executeQuery(selectA);
                 ResultSet resultSetB = statementB.executeQuery(selectB)
            ) {
                while (resultSetA.next()) {
                    if ("FACULTY".equals(resultSetA.getString("ROLE"))) {
                        a.add(resultSetA.getString("UUID"));
                    }
                }
                while (resultSetB.next()) {
                    b.add(resultSetB.getString("UUID"));
                }

                // make sure two hash sets contain exactly the same items
                assertEquals(a.size(), b.size());
                for (String i : a) {
                    assertTrue(b.contains(i));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
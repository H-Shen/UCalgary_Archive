import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.regex.Pattern;

/**
 * the {@code Constants} class stores all constants the app needs to use.
 */
public class Constants {

    /**
     * Collection of constants
     */
    public static final Pattern  INTEGER_REGEX            = Pattern.compile("^([-]?[1-9]\\d*|0)$");
    public static final Pattern  DOUBLE_REGEX             = Pattern.compile("^[-]?([1-9][0-9]*|0)\\.?[0-9]*[1-9]$");
    public static final String   FEBRUARY                 = "02";
    public static final String   RESPONSE_YES             = "YES";
    public static final String   RESPONSE_NO              = "NO";
    public static final String   ENROLLMENT_CLOSE         = "CLOSE";
    public static final String   ENROLLMENT_OPEN          = "OPEN";
    public static final String   CONSENT_BY_DEPARTMENT    = "CONSENT_BY_DEPARTMENT";
    public static final int      MAX_ITEMS_SCROLL_FORWARD = 20;
    public static final int      MAX_COURSES_ID_A_LINE    = 6;
    public static final boolean  TEST_IN_IDE              = false;
    public static final int      MAX_COURSES_TO_SELECT    = 6;
    public static final TimeZone TIME_ZONE                = TimeZone.getTimeZone("America/Edmonton");

    public static final HashSet<Character> LEGAL_USERNAME_CHARSET = new HashSet<Character>() {{
        add('0');
        add('1');
        add('2');
        add('3');
        add('4');
        add('5');
        add('6');
        add('7');
        add('8');
        add('9');
        add('A');
        add('B');
        add('C');
        add('D');
        add('E');
        add('F');
        add('G');
        add('H');
        add('I');
        add('J');
        add('K');
        add('L');
        add('M');
        add('N');
        add('O');
        add('P');
        add('Q');
        add('R');
        add('S');
        add('T');
        add('U');
        add('V');
        add('W');
        add('X');
        add('Y');
        add('Z');
        add('a');
        add('b');
        add('c');
        add('d');
        add('e');
        add('f');
        add('g');
        add('h');
        add('i');
        add('j');
        add('k');
        add('l');
        add('m');
        add('n');
        add('o');
        add('p');
        add('q');
        add('r');
        add('s');
        add('t');
        add('u');
        add('v');
        add('w');
        add('x');
        add('y');
        add('z');
        add('_');
        add(' ');
    }};


    public static final String   ASCII_UPPERCASE          = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String   DIGITS                   = "0123456789";
    public static final String   PRINTABLE                = "'0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!\"#$%&()*+,-./:;<=>?@[\\]^_`{|}~\'";
    public static final String[] ROLE                     = {"ADMIN", "FACULTY", "STUDENT"};
    public static final int      ROLE_LENGTH              = 3;
    public static final String[] GENDER                   = {"M", "F"};
    public static final int      GENDER_LENGTH            = 2;
    public static final String[] COURSES_TYPE             = {"MANDATORY", "OPTIONAL"};
    public static final int      MIN_FULL_NAME_LENGTH     = 3;
    public static final int      MAX_FULL_NAME_LENGTH     = 40;
    public static final int      MAX_USERNAME_LENGTH      = 20;
    public static final int      MIN_USERNAME_LENGTH      = 4;
    public static final int      MAX_COURSE_ID_LENGTH     = 10;
    public static final int      MAX_EMAIL_ADDRESS_LENGTH = 40;
    public static final int      MAX_ADDRESS_LENGTH       = 80;
    public static final String[] MONTH_DAYS               = {"31", "28", "31", "30", "31", "30", "31", "31", "30", "31", "30", "31"};

    public static final HashSet<String> VALID_MONTH             = new HashSet<>(Arrays.asList("01", "02", "03", "04", "05",
            "06", "07", "08", "09", "10", "11", "12"));
    public static final Pattern         DAY_BELOW_TEN           = Pattern.compile("^0[1-9]$");
    public static final BigInteger      MIN_YEAR                = new BigInteger("1900");
    public static final BigInteger      CURRENT_YEAR            = new BigInteger(initializeCurrentYear());
    public static final int             DEFAULT_PASSWORD_LENGTH = 10;
    public static final int             MIN_PASSWORD_LENGTH     = 6;
    public static final BigDecimal      MAX_UNITS_AS_DOUBLE     = new BigDecimal("120.0");
    public static final BigInteger      MAX_UNITS_AS_INT        = new BigInteger("120");
    public static final BigDecimal      MAX_GRADE_AS_DOUBLE     = new BigDecimal("4.0");
    public static final BigInteger      MAX_GRADE_AS_INT        = new BigInteger("4");

    public static final Pattern COURSE_ID_REGEX     = Pattern.compile("^[A-Z]+[0-9]+$");
    /**
     * A regex to validate the email address from the https://emailregex.com/
     */
    public static final Pattern EMAIL_ADDRESS_REGEX = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\" +
            ".[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f" +
            "]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])");
    public static final Pattern PHONE_NUMBER_REGEX  = Pattern.compile("^[0-9]{3}-[0-9]{3}-[0-9]{4}$");
    /**
     * Color output wrapper
     */
    public static final String  RESET               = "\u001B[0m";
    public static final String  RED                 = "\u001B[31m";
    public static final String  INVALID_INPUT       = COLOR_WRAPPER("Illegal input!", Constants.RED);
    public static final String  GREEN_BOLD          = "\033[1;32m";
    public static final String  WHITE_BOLD          = "\033[1;37m";
    public static final String  RED_BOLD            = "\033[1;31m";
    public static final String  YELLOW_BOLD         = "\033[1;33m";
    public static final String  WHITE_BACKGROUND    = "\u001B[47m";
    public static final String  BLACK               = "\u001B[30m";
    public static final String  BANNER_FILEPATH     = getResFilePath(Constants.TEST_IN_IDE) + "banner.txt";
    public static final String  ENROLLMENT_CONF     = getResFilePath(Constants.TEST_IN_IDE) + "enrollment.txt";
    public static       Scanner IN                  = new Scanner(System.in);

    /**
     * @param s
     * @param color
     * @return
     */
    public static final String COLOR_WRAPPER(String s, String color) {
        return color + s + RESET;
    }

    /**
     * @param s
     * @param color
     * @return
     */
    public static final String COLOR_WRAPPER(char s, String color) {
        return color + s + RESET;
    }

    /**
     *
     */
    public static final void MORE() {
        System.out.print(WHITE_BACKGROUND + BLACK + "--More--" + RESET);
        IN.nextLine();
        IN = new Scanner(System.in);
    }

    /**
     * Allows for the user to input something again in the next line.
     */
    public static final void PRESS_ENTER_KEY_TO_CONTINUE() {
        System.out.print("Press Enter to continue... ");
        IN.nextLine();
        IN = new Scanner(System.in);
    }

    /**
     * Initializes the current year.
     *
     * @return
     */
    private static final String initializeCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return String.valueOf(calendar.get(Calendar.YEAR));
    }

    /**
     * Initializes the enrollment status.
     */
    public static final void initializeEnrollmentStatus() {

        try (Scanner in = new Scanner(new FileInputStream(ENROLLMENT_CONF))) {
            while (in.hasNextLine()) {
                Admin.setEnrollmentStatus(in.nextLine());
                break;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Since The SQL standard specifies that single-quotes in strings are escaped by putting two single quotes in a row,
     * this function returns a string which replaces all "'" with "''", before updating the database files.
     *
     * @param s
     * @return
     */
    public static final String ESCAPE_CHARACTER(String s) {
        // ' must be changed into '' before commiting to the database
        return s.replace("\'", "\'\'");
    }

    /**
     * Get the banner filepath of the app
     *
     * @return
     */
    public static String getResFilePath(boolean testInIDE) {
        if (testInIDE) {
            return "/Users/hshen/IdeaProjects/Project/res/";
        }
        return new File(System.getProperty("user.dir")).getParent() + "/res/";
    }

    public enum COURSE_FIELD {
        COURSE_ID, COURSE_NAME, COURSE_DESCRIPTION, COURSE_UNITS, PREREQUISITES, ANTIREQUISITES, CAN_BE_REPEATED
    }

    public enum USER_FIELD {
        UUID, USERNAME, FULLNAME, ROLE, GENDER, DATE_OF_BIRTH, EMAIL, PHONE, ADDRESS, PASSWORD
    }
}

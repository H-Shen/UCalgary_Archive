import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.regex.Pattern;

/**
 * The {@code Constants} class stores all constants the app needs to use.
 *
 * @author Group 25
 * @date 2019/04/08
 */
public class Constants {

    /**
     * @Fields Collection of constants
     */
    public static final Pattern  INTEGER_REGEX            = Pattern.compile("^([-]?[1-9]\\d*|0)$");
    public static final Pattern  DOUBLE_REGEX             = Pattern.compile("^[-]?([1-9][0-9]*|0)\\.?[0-9]*[1-9]$");
    public static final String   FEBRUARY                 = "02";
    public static final String   RESPONSE_YES             = "YES";
    public static final String   RESPONSE_NO              = "NO";
    public static final String   CONSENT_BY_DEPARTMENT    = "CONSENT_BY_DEPARTMENT";
    public static final int      MAX_ITEMS_SCROLL_FORWARD = 30;
    public static final int      MAX_COURSES_ID_A_LINE    = 6;
    public static final TimeZone TIME_ZONE                = TimeZone.getTimeZone("America/Edmonton");

    public static final HashSet<Character> LEGAL_USERNAME_CHARSET = new HashSet<>(
            Arrays.asList(' ', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
                    'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '_', 'a',
                    'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
                    'v', 'w', 'x', 'y', 'z'));

    public static final HashSet<Character> LEGAL_PASSWORD_CHARSET = new HashSet<>(
            Arrays.asList(' ', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
                    'i', 'j', 'k', 'l', 'm', 'n', 'o',
                    'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
                    'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '!', '"', '#', '$', '%',
                    '&', '(', ')', '*', '+', ',', '-', '.', '/', ':', ';', '<', '=', '>', '?', '@', '[', '\\', ']', '^', '_',
                    '`', '{', '|', '}', '~', '\''));

    public static final String   SPECIAL_CHARSET          = "!\"#$%&()*+,-./:;<=>?@ [\\]^_`{|}~\'";
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

    public static final HashSet<String> VALID_MONTH         = new HashSet<>(
            Arrays.asList("01", "02", "03", "04", "05", "06", "07", "08", "09", "10", "11", "12"));
    public static final Pattern         DAY_BELOW_TEN       = Pattern.compile("^0[1-9]$");
    public static final BigInteger      MIN_YEAR            = new BigInteger("1900");
    public static final BigInteger      CURRENT_YEAR        = new BigInteger(initializeCurrentYear());
    public static final int             MIN_PASSWORD_LENGTH = 6;
    public static final BigDecimal      MAX_UNITS_AS_DOUBLE = new BigDecimal("15.0");
    public static final BigInteger      MAX_UNITS_AS_INT    = new BigInteger("15");
    public static final BigDecimal      MAX_GRADE_AS_DOUBLE = new BigDecimal("4.0");
    public static final BigInteger      MAX_GRADE_AS_INT    = new BigInteger("4");

    public static final Pattern COURSE_ID_REGEX = Pattern.compile("^[A-Z]+[0-9]+$");

    /**
     * A regex to validate the email address which is referred from the https://emailregex.com/
     */
    public static final Pattern EMAIL_ADDRESS_REGEX = Pattern.compile("(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\" +
            ".[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f" +
            "]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:" +
            "[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]" +
            "?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-" +
            "\\x09\\x0b\\x0c\\x0e-\\x7f])+)])");

    public static final  Pattern PHONE_NUMBER_REGEX = Pattern.compile("^[0-9]{3}-[0-9]{3}-[0-9]{4}$");
    public static final  String  RED                = "\u001B[31m";
    public static final  String  GREEN_BOLD         = "\033[1;32m";
    public static final  String  WHITE_BOLD         = "\033[1;37m";
    public static final  String  RED_BOLD           = "\033[1;31m";
    public static final  String  YELLOW_BOLD        = "\033[1;33m";
    public static final  String  GREEN              = "\033[0;32m";
    public static final  String  BANNER_FILEPATH    = getResFilePath() + "banner.txt";
    public static final  String  LOG_FILEPATH       = getResFilePath() + "log.txt";
    public static final  int     MAX_HISTORY_LINES  = 1000;
    private static final String  WHITE_BACKGROUND   = "\u001B[47m";
    private static final String  BLACK              = "\u001B[30m";
    private static final String  RESET              = "\u001B[0m";
    public static final  String  INVALID_INPUT      = colorWrapper("Illegal input!", Constants.RED);
    public static        Scanner IN                 = new Scanner(System.in);

    /**
     * The method wraps the assigned string with the assigned color.
     *
     * @param s     string to wrap
     * @param color color to use
     * @return a string written in the provided color, and returns the color
     * @date 2019/04/08
     */
    public static String colorWrapper(String s, String color) {
        return color + s + RESET;
    }

    /**
     * The method wraps the assigned character with the assigned color.
     *
     * @param ch    character to wrap
     * @param color color to use
     * @return character written in provided color, and returns color
     * @date 2019/04/08
     */
    public static String colorWrapper(char ch, String color) {
        return color + ch + RESET;
    }

    /**
     * The method prints a a line labeled "--More--" in order to neatly display the information in the terminal.
     *
     * @date 2019/04/08
     */
    public static void displayLineSeparator() {
        System.out.print(WHITE_BACKGROUND + BLACK + "--More--" + RESET);
        IN.nextLine();
        IN = new Scanner(System.in);
    }

    /**
     * The method allows for the user to input something again and press 'Enter' to show the next line.
     *
     * @date 2019/04/08
     */
    public static void pressEnterToContinue() {
        System.out.print("Press Enter to continue... ");
        IN.nextLine();
        IN = new Scanner(System.in);
    }

    /**
     * The method initializes the current year.
     *
     * @return current year according to calendar
     * @date 2019/04/08
     */
    private static String initializeCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return String.valueOf(calendar.get(Calendar.YEAR));
    }

    /**
     * The method gets the banner filepath of the app
     *
     * @return the file path of the banner
     * @date 2019/04/08
     */
    private static String getResFilePath() {
        return new File(System.getProperty("user.dir")).getParent() + "/res/";
    }

    /**
     * The enum class defines the fields that make up a course
     * All course information taken from https://www.ucalgary.ca/pubs/calendar/current/index.html
     *
     * @date 2019/04/08
     */
    public enum COURSE_FIELD {
        /**
         * Primary Key
         * course ID is a string that consists of its 4 letter Identifier + its 3 number identifier (eg. POLI201)
         * within the database and the hash maps allows us to search and find the attributes of a course
         */
        COURSE_ID,
        /**
         * course name is the courses full name as a string (eg. Introduction to Government and Politics)
         */
        COURSE_NAME,
        /**
         * courses description is a string consisting of the courses description provided by University of Calgary's calendar
         */
        COURSE_DESCRIPTION,
        /**
         * a number representing how many credits the course counts for, usually either 3 or 6.
         */
        COURSE_UNITS,
        /**
         * a string consisting of zero or more other courses objects that a user needs to take before being eligible for this
         * course object
         */
        PREREQUISITES,
        /**
         * a string consisting of zero or more other courses objects that a user can not take before being eligible for this
         * course object
         */
        ANTIREQUISITES,
        /**
         * a boolean representing whether or not the course can be repeatedly taken for credit
         */
        CAN_BE_REPEATED
    }

    /**
     * The enum class defines the fields that make up a user
     *
     * @date 2019/04/08
     */
    public enum USER_FIELD {
        /**
         * Primary Key
         * a UUID is a unique set of numbers and letters as a string, identified in its instance variable.
         * within the database and the hash maps allows us to search and find the attributes of a user
         */
        UUID,
        /**
         * the field which a user uses to log in, defined within an instance variable as a string
         */
        USERNAME,
        /**
         * the user's first and last name as a string
         */
        FULL_NAME,
        /**
         * a string attribute that allows a single user to either be an admin, a student or a faculty
         */
        ROLE,
        /**
         * a string that either forces a student to be M or F
         */
        GENDER,
        /**
         * type DateOfBirth is a class defined in another file, that turns a users date of birth into a string, consisting
         * of month/date/year
         */
        DATE_OF_BIRTH,
        /**
         * type pattern defined as an instance variable above by a regex
         */
        EMAIL,
        /**
         * type Pattern defined by the regex in instance variables
         */
        PHONE,
        /**
         * a string that defines the users address
         */
        ADDRESS,
        /**
         * a string of characters represent the user's password
         */
        PASSWORD
    }
}

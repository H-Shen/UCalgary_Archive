import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import java.util.TimeZone;
import java.util.regex.Pattern;

/**
 * This class stores all constants the app needs to use.
 */
public class Constants {

    public static final String     VERSION                    = "v0.01";
    public static final TimeZone   TIME_ZONE                  = TimeZone.getTimeZone("America/Edmonton");
    public static final String     ASCII_UPPERCASE            = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String     DIGITS                     = "0123456789";
    public static final String     PRINTABLE                  = "'0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!\"#$%&()*+,-./:;<=>?@[\\]^_`{|}~\'";
    public static final String[]   ROLE                       = {"ADMIN", "FACULTY", "STUDENT"};
    public static final int        ROLE_LENGTH                = 3;
    public static final String[]   GENDER                     = {"M", "F"};
    public static final int        GENDER_LENGTH              = 2;
    public static final int        MAX_RANDOM_USERNAME_LENGTH = 15;
    public static final int        MIN_RANDOM_USERNAME_LENGTH = 6;
    public static final int        MIN_FULL_NAME_LENGTH       = 3;
    public static final int[]      MONTH_DAYS                 = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    public static final int        MIN_MONTH                  = 1;
    public static final int        MAX_MONTH                  = 12;
    public static final int        MIN_DAY                    = 1;
    public static final int        MIN_YEAR                   = 1900;
    public static final int        MAX_YEAR                   = 2200;
    public static final int        CURRENT_YEAR               = getCurrentYear();
    public static final int        DEFAULT_PASSWORD_LENGTH    = 10;
    public static final int        MIN_PASSWORD_LENGTH        = 6;
    public static final BigDecimal MAX_UNITS                  = new BigDecimal("120.0");
    public static final Pattern    EMAIL_ADDRESS_REGEX        = Pattern.compile("[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+(?:" +
            ".[A-Za-z0-9!#$%&'*+/=?^_`{|}~-]+)*@" +
            "(?:[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?.)+[A-Za-z0-9](?:[A-Za-z0-9-]*[A-Za-z0-9])?");
    public static final Pattern    PHONE_NUMBER_REGEX         = Pattern.compile("^[0-9]{3}-[0-9]{3}-[0-9]{4}$");
    /**
     * Color output wrapper
     */
    public static final String     RESET                      = "\u001B[0m";
    public static final String     RED                        = "\u001B[31m";
    public static final String     INVALID_INPUT              = COLOR_WRAPPER("Illegal input!", Constants.RED);
    public static final String     RED_BOLD                   = "\033[1;31m";
    public static final String     GREEN_BOLD                 = "\033[1;32m";
    public static final String     WHITE_BOLD                 = "\033[1;37m";
    /**
     * Collection of constants
     */
    public static       Scanner    IN                         = new Scanner(System.in);

    public static final String COLOR_WRAPPER(String s, String color) {
        return color + s + RESET;
    }

    public static final void PRESS_ENTER_KEY_TO_CONTINUE() {
        System.out.print("Press Enter key to continue... ");
        IN.nextLine();
        IN = new Scanner(System.in);
    }

    private static final int getCurrentYear() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        return calendar.get(Calendar.YEAR);
    }

    public static final String ESCAPE_CHARACTER(String s) {
        // ' must be changed into '' before commiting to the database
        return s.replace("\'", "\'\'");
    }

    public enum COURSE_FIELD {
        COURSE_NAME, COURSE_DESCRIPTION, COURSE_UNITS, PREREQUISITES, ANTIREQUISITES, CAN_BE_REPEATED
    }

    public enum USER_FIELD {
        USERNAME, FULLNAME, ROLE, GENDER, DATE_OF_BIRTH, EMAIL, PHONE, ADDRESS, PASSWORD
    }
}

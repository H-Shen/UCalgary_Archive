import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The {@code RandomUserGenerator} class contains static methods that creates a full account for a random user.
 */
public class RandomUserGenerator {

    // Creating a random string with a specified length and candidate characters
    private static StringBuilder createRandomString(String candidate, int length) {
        StringBuilder result          = new StringBuilder();
        int           candidateLength = candidate.length();
        for (int i = 0; i < length; i++) {
            result.append(candidate.charAt(ThreadLocalRandom.current().nextInt(0, candidateLength)));
        }
        return result;
    }

    /**
     * Creating a random UUID (Universally unique identifier).
     *
     * @return a String containing the UUID generated.
     */
    public static String createRandomUuid() {
        String uuid;
        do {
            uuid = UUID.randomUUID().toString();
        } while (Database.accounts.containsKey(uuid));
        return uuid;
    }

    /**
     * Creating a random username.
     *
     * @return a String containing the username generated.
     */
    public static String createRandomUsername() {

        StringBuilder username       = new StringBuilder();
        int           usernameLength = ThreadLocalRandom.current().nextInt(Constants.MIN_USERNAME_LENGTH, Constants.MAX_USERNAME_LENGTH + 1);

        while (true) {
            for (int i = 0; i < usernameLength; ++i) {
                int                 length = ThreadLocalRandom.current().nextInt(Constants.LEGAL_USERNAME_CHARSET.size());
                Iterator<Character> iter   = Constants.LEGAL_USERNAME_CHARSET.iterator();
                for (int j = 0; j < length; ++j) {
                    iter.next();
                }
                username.append(iter.next());
            }
            if (!Database.usernameToUuid.containsKey(username.toString())) {
                break;
            }
        }
        return username.toString();
    }

    /**
     * Creating a random role (Administrator, Student, or Faculty)
     *
     * @return a String containing the random role generated.
     */
    public static String createRandomRole() {
        return Constants.ROLE[ThreadLocalRandom.current().nextInt(0, Constants.ROLE_LENGTH)];
    }

    /**
     * Creating a random full name.
     *
     * @return a String containing the random full name generated.
     */
    public static String createRandomFullName() {
        int firstNameLength = ThreadLocalRandom.current().nextInt(3, 10);
        int lastNameLength  = ThreadLocalRandom.current().nextInt(3, 10);
        return createRandomString(Constants.ASCII_UPPERCASE, firstNameLength) + " " + createRandomString(Constants.ASCII_UPPERCASE, lastNameLength);
    }

    /**
     * Creating a random gender.
     *
     * @return a String containing the random gender generated.
     */
    public static String createRandomGender() {
        return Constants.GENDER[ThreadLocalRandom.current().nextInt(0, Constants.GENDER_LENGTH)];
    }

    /**
     * Creating a random date of birth.
     *
     * @return a String containing the random date of birth generated.
     */
    public static DateOfBirth createRandomDateOfBirth() {

        int    year    = ThreadLocalRandom.current().nextInt(Constants.MIN_YEAR.intValue(), Constants.CURRENT_YEAR.intValue() + 1);
        String yearStr = String.valueOf(year);
        int    month   = ThreadLocalRandom.current().nextInt(1, 12 + 1);
        String monthStr;
        if (month < 10) {
            monthStr = "0" + month;
        } else {
            monthStr = String.valueOf(month);
        }

        int day;
        if (Validation.isYearLeap(yearStr) && month == 2) {
            day = ThreadLocalRandom.current().nextInt(1, Integer.parseInt(Constants.MONTH_DAYS[1]) + 2);
        } else {
            day = ThreadLocalRandom.current().nextInt(1, Integer.parseInt(Constants.MONTH_DAYS[month - 1]) + 1);
        }
        String dayStr;
        if (day < 10) {
            dayStr = "0" + day;
        } else {
            dayStr = String.valueOf(day);
        }

        return new DateOfBirth(yearStr, monthStr, dayStr);
    }

    /**
     * Creating a random email address.
     *
     * @return a String containing the random email address generated.
     */
    public static String createRandomEmailAddress() {
        return createRandomFullName().toLowerCase().replace(" ", "_") + "@example.com";
    }

    /**
     * Creating a random phone number.
     *
     * @return a String containing the random phone number generated.
     */
    public static String createRandomPhoneNumber() {
        return createRandomString(Constants.DIGITS, 3) + "-" + createRandomString(Constants.DIGITS, 3) + "-" + createRandomString(Constants.DIGITS, 4);
    }

    /**
     * Creating a random address.
     *
     * @return a String containing the random address generated.
     */
    public static String createRandomAddress() {
        return createRandomFullName().toLowerCase() + " " + createRandomFullName();
    }

    /**
     * Creating a random password.
     *
     * @return a String containing the random password generated.
     */
    public static String createRandomPassword() {
        String temp = createRandomString(Constants.PRINTABLE, Constants.DEFAULT_PASSWORD_LENGTH).toString();
        temp = Constants.ESCAPE_CHARACTER(temp);
        return temp;
    }

    /**
     * Creating a random user with an assigned role.
     *
     * @param role a String containing the role that was randomly generated.
     */
    public static User createRandomUser(String role) throws IllegalArgumentException {
        User user;
        switch (role) {
            case "ADMIN":
                user = new Admin();
                break;
            case "FACULTY":
                user = new Faculty();
                break;
            case "STUDENT":
                user = new Student();
                break;
            default:
                throw new IllegalArgumentException();
        }
        user.setUuid(createRandomUuid());
        user.setPassword(createRandomPassword());
        user.setAddress(createRandomAddress());
        user.setEmailAddress(createRandomEmailAddress());
        user.setPhoneNumber(createRandomPhoneNumber());
        user.setDateOfBirth(createRandomDateOfBirth());
        user.setGender(createRandomGender());
        user.setUsername(createRandomUsername());
        user.setFullName(createRandomFullName());
        return user;
    }

    /**
     * Select a random user from RANDOM_USER in ACCOUNT_DATA.db and assign a specific role
     *
     * @param role A String containing the role that was randomly generated.
     * @return A String containing the user that was randomly selected.
     */
    public static User selectRandomUser(String role) {

        try {
            if (!Validation.isRoleValid(role) || !Database.isValid(Database.ACCOUNT_DATA)) {
                throw new Exception();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String select = "SELECT * FROM RANDOM_USER ORDER BY RANDOM() LIMIT 1;";
        User   user   = null;
        try (Connection connection = Database.createConnection(Database.ACCOUNT_DATA, true);
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(select)
        ) {
            if (!resultSet.next()) {
                throw new Exception();
            }
            if (role.equals(Constants.ROLE[0])) {
                user = new Admin();
            } else if (role.equals(Constants.ROLE[1])) {
                user = new Faculty();
            } else {
                user = new Student();
            }
            user.setUuid(resultSet.getString("UUID"));
            user.setUsername(resultSet.getString("USERNAME"));
            user.setFullName(resultSet.getString("FULLNAME"));
            user.setGender(resultSet.getString("GENDER"));
            user.setDateOfBirth(new DateOfBirth(resultSet.getString("DATE_OF_BIRTH")));
            user.setEmailAddress(resultSet.getString("EMAIL"));
            user.setPhoneNumber(resultSet.getString("PHONE"));
            user.setAddress(resultSet.getString("ADDRESS"));
            user.setPassword(resultSet.getString("PASSWORD"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
}

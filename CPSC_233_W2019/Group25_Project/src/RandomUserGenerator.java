import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Iterator;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The {@code RandomUserGenerator} class contains static methods that creates a full account for a random user.
 *
 * @author Group 25
 * @date 2019/04/08
 */
public class RandomUserGenerator {

    /**
     * Creating a random string with a specified length and a candidate string
     *
     * @param candidate a string whose characters will be used as candidates when generating a random string
     * @param length    the length of the random string
     * @return a StringBuilder object which length and contents are assigned
     * @date 2019/04/08
     */
    private static StringBuilder createRandomString(String candidate, int length) {
        StringBuilder result          = new StringBuilder();
        int           candidateLength = candidate.length();
        for (int i = 0; i < length; ++i) {
            result.append(candidate.charAt(ThreadLocalRandom.current().nextInt(0, candidateLength)));
        }
        return result;
    }

    /**
     * Creating a random UUID (Universally unique identifier).
     *
     * @return a string contains the UUID generated
     * @date 2019/04/08
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
     * @return a string contains the username generated
     * @date 2019/04/08
     */
    public static String createRandomUsername() {

        StringBuilder username       = new StringBuilder();
        int           usernameLength = ThreadLocalRandom.current().nextInt(Constants.MIN_USERNAME_LENGTH, Constants.MAX_USERNAME_LENGTH + 1);

        do {
            for (int i = 0; i < usernameLength; ++i) {
                int                 length = ThreadLocalRandom.current().nextInt(Constants.LEGAL_USERNAME_CHARSET.size());
                Iterator<Character> iter   = Constants.LEGAL_USERNAME_CHARSET.iterator();
                for (int j = 0; j < length; ++j) {
                    iter.next();
                }
                username.append(iter.next());
            }
        } while (Database.usernameToUuid.containsKey(username.toString()));
        return username.toString();
    }

    /**
     * Creating a random role (Administrator, Student, or Faculty)
     *
     * @return a string containing the random role generated
     * @date 2019/04/08
     */
    public static String createRandomRole() {
        return Constants.ROLE[ThreadLocalRandom.current().nextInt(0, Constants.ROLE_LENGTH)];
    }

    /**
     * Creating a random full name.
     *
     * @return a String containing the random full name generated
     * @date 2019/04/08
     */
    public static String createRandomFullName() {
        int firstNameLength = ThreadLocalRandom.current().nextInt(Constants.MIN_FULL_NAME_LENGTH, 10);
        int lastNameLength  = ThreadLocalRandom.current().nextInt(Constants.MIN_FULL_NAME_LENGTH, 10);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < firstNameLength; ++i) {
            sb.append((char) ThreadLocalRandom.current().nextInt(65, 91));
        }
        sb.append(" ");
        for (int i = 0; i < lastNameLength; ++i) {
            sb.append((char) ThreadLocalRandom.current().nextInt(65, 91));
        }
        return sb.toString();
    }

    /**
     * Creating a random gender.
     *
     * @return a string contains the random gender generated
     * @date 2019/04/08
     */
    public static String createRandomGender() {
        return Constants.GENDER[ThreadLocalRandom.current().nextInt(0, Constants.GENDER_LENGTH)];
    }

    /**
     * Creating a random date of birth.
     *
     * @return a string contains the random date of birth generated
     * @date 2019/04/08
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
     * @return a string contains the random email address generated
     * @date 2019/04/08
     */
    public static String createRandomEmailAddress() {
        return createRandomFullName().toLowerCase().replace(" ", "_") + "@example.com";
    }

    /**
     * Creating a random phone number.
     *
     * @return a string contains the random phone number generated
     * @date 2019/04/08
     */
    public static String createRandomPhoneNumber() {

        StringBuilder sb                = new StringBuilder();
        int           firstFieldLength  = 3;
        int           secondFieldLength = 3;
        int           thirdFieldLength  = 4;

        for (int i = 0; i < firstFieldLength; ++i) {
            sb.append(ThreadLocalRandom.current().nextInt(0, 10));
        }
        sb.append("-");
        for (int i = 0; i < secondFieldLength; ++i) {
            sb.append(ThreadLocalRandom.current().nextInt(0, 10));
        }
        sb.append("-");
        for (int i = 0; i < thirdFieldLength; ++i) {
            sb.append(ThreadLocalRandom.current().nextInt(0, 10));
        }
        return sb.toString();
    }

    /**
     * Creating a random address.
     *
     * @return a string contains the random address generated
     * @date 2019/04/08
     */
    public static String createRandomAddress() {
        return createRandomFullName().toLowerCase() + " " + createRandomFullName();
    }

    /**
     * Creating a random password.
     *
     * @return a string containing the random password generated
     * @date 2019/04/08
     */
    public static String createRandomPassword() {
        int passwordLength = ThreadLocalRandom.current().nextInt(Constants.MIN_PASSWORD_LENGTH,
                Constants.MIN_PASSWORD_LENGTH + 20);
        String legalPasswordSet = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ!\"#$%&()*+,-./:;<=>?@" +
                "[\\]^_`{|}~\'";
        return createRandomString(legalPasswordSet, passwordLength).toString();
    }

    /**
     * Creating a random user with an assigned role.
     *
     * @param role the role of the random user
     * @return a user with the assigned role
     * @date 2019/04/08
     */
    public static User createRandomUser(String role) {
        User user;
        switch (role) {
            case "FACULTY":
                user = new Faculty();
                break;
            case "ADMIN":
                user = new Admin();
                break;
            default:
                user = new Student();
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
     * The method selects a random user from RANDOM_USER in ACCOUNT_DATA.db and assigns a specific role
     *
     * @param role the role of the user
     * @return A User object selected from RANDOM_USER
     * @date 2019/04/08
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
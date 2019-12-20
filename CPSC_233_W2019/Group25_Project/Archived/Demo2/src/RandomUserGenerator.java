import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

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

    // Creating a random UUID (Universally unique identifier)
    public static String createRandomUuid() {
        String uuid;
        do {
            uuid = UUID.randomUUID().toString();
        } while (Database.accounts.containsKey(uuid));
        return uuid;
    }

    public static String createRandomUsername() {
        StringBuilder username = new StringBuilder();
        int usernameLength = ThreadLocalRandom.current().nextInt(Constants.MIN_RANDOM_USERNAME_LENGTH,
                Constants.MAX_RANDOM_USERNAME_LENGTH + 1);
        while (true) {
            int length = Constants.ASCII_UPPERCASE.length();
            for (int i = 0; i < usernameLength; ++i) {
                username.append(Constants.ASCII_UPPERCASE.charAt(ThreadLocalRandom.current().nextInt(length)));
            }
            if (!Database.usernameToUuid.containsKey(username.toString().toLowerCase())) {
                break;
            }
        }
        return username.toString().toLowerCase();
    }

    // Creating a random role
    public static String createRandomRole() {
        return Constants.ROLE[ThreadLocalRandom.current().nextInt(0, Constants.ROLE_LENGTH)];
    }

    // Creating a random fullName
    public static String createRandomFullName() {
        // Select random fullName from RANDOM_USER in ACCOUNT_DATA.db
        int firstNameLength = ThreadLocalRandom.current().nextInt(3, 10);
        int lastNameLength  = ThreadLocalRandom.current().nextInt(3, 10);
        return createRandomString(Constants.ASCII_UPPERCASE, firstNameLength) + " " + createRandomString(Constants.ASCII_UPPERCASE, lastNameLength);
    }

    // Creating a random gender
    public static String createRandomGender() {
        return Constants.GENDER[ThreadLocalRandom.current().nextInt(0, Constants.GENDER_LENGTH)];
    }

    // Creating a random dateOfBirth
    public static DateOfBirth createRandomDateOfBirth() {

        int year  = ThreadLocalRandom.current().nextInt(Constants.MIN_YEAR, Constants.CURRENT_YEAR + 1);
        int month = ThreadLocalRandom.current().nextInt(Constants.MIN_MONTH, Constants.MAX_MONTH + 1);
        int day;

        if (Validation.isYearLeap(year) && month == 2) {
            day = ThreadLocalRandom.current().nextInt(Constants.MIN_DAY, Constants.MONTH_DAYS[1] + 2);
        } else {
            day = ThreadLocalRandom.current().nextInt(Constants.MIN_DAY, Constants.MONTH_DAYS[month - 1] + 1);
        }
        return new DateOfBirth(year, month, day);
    }

    // Creating a random emailAddress
    public static String createRandomEmailAddress() {
        return createRandomFullName().toLowerCase().replace(" ", "_") + "@example.com";
    }

    // Creating a random phoneNumber
    public static String createRandomPhoneNumber() {
        return createRandomString(Constants.DIGITS, 3) + "-" + createRandomString(Constants.DIGITS, 3) + "-" + createRandomString(Constants.DIGITS, 4);
    }

    // Creating a random address
    public static String createRandomAddress() {
        return createRandomFullName().toLowerCase() + " " + createRandomFullName();
    }

    // Creating a random password
    public static String createRandomPassword() {
        String temp = createRandomString(Constants.PRINTABLE, Constants.DEFAULT_PASSWORD_LENGTH).toString();
        temp = Constants.ESCAPE_CHARACTER(temp);
        return temp;
    }

    // Create a random user with assigned role
    public static void createRandomUser(String role) {

        Database.initializeAccounts();

        User a;
        switch (role) {
            case "ADMIN":
                a = new Admin();
                break;
            case "FACULTY":
                a = new Faculty();
                break;
            case "STUDENT":
                a = new Student();
                break;
            default:
                return;
        }

        a.setUuid(RandomUserGenerator.createRandomUuid());
        a.setPassword(RandomUserGenerator.createRandomPassword());
        a.setAddress(RandomUserGenerator.createRandomAddress());
        a.setEmailAddress(RandomUserGenerator.createRandomEmailAddress());
        a.setPhoneNumber(RandomUserGenerator.createRandomPhoneNumber());
        a.setDateOfBirth(RandomUserGenerator.createRandomDateOfBirth());
        a.setGender(RandomUserGenerator.createRandomGender());
        a.setUsername(RandomUserGenerator.createRandomUsername());
        a.setFullName(RandomUserGenerator.createRandomFullName());
        Database.createAccountCommit(a);
    }

    // Select a random user from RANDOM_USER and assign a specific role
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

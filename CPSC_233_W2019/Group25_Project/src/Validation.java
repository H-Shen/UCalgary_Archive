import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * The {@code Validation} class contains static methods used to validate all types of data involved in the app.
 *
 * @author Group 25
 * @date 2019/04/08
 */

public class Validation {

    /**
     * Judges if the uuid is valid, a uuid is valid if and only if
     * 1. it is not null
     * 2. it has no duplicates
     *
     * @param uuid a String containing the uuid being checked.
     * @return true if uuid is valid, false otherwise.
     */
    public static boolean isUuidValid(String uuid) {
        return (uuid != null && !Database.accounts.containsKey(uuid));
    }

    /**
     * Judges if the role is valid, a role is valid if and only if
     * 1. it is not null
     * 2. it is 'ADMIN' or 'FACULTY' or 'STUDENT'
     *
     * @param role a String containing the role being checked.
     * @return true if role is valid, false otherwise.
     */
    public static boolean isRoleValid(String role) {
        if (role != null) {
            for (String i : Constants.ROLE) {
                if (role.equals(i)) {
                    return true;
                }
            }
        }
        // pop out an error message if the role is not 'ADMIN' or 'FACULTY' or 'STUDENT'
        System.out.println(Constants.colorWrapper("The ROLE must be ADMIN or FACULTY or STUDENT!", Constants.RED));
        return false;
    }

    /**
     * Judges if the username is valid, a username is valid if and only if
     * 1. it is not null
     * 2. it is not empty
     * 3. it cannot contain whitespaces only
     * 4. it has no duplicates
     * 5. its length is not greater than 20
     * 6. its length is not smaller than 4
     * 7.
     *
     * @param username String containing the username being checked.
     * @return true if username is valid, false otherwise
     */
    public static boolean isUsernameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.trim().isEmpty()) {
            // pop out an error message if the username is empty or contain whitespaces only!
            System.out.println(Constants.colorWrapper("The username cannot be empty or contain whitespaces only!", Constants.RED));
            return false;
        }
        // check if username contains characters not in Constants.LEGAL_USERNAME_CHARSET
        for (int i = 0; i < username.length(); ++i) {
            if (!Constants.LEGAL_USERNAME_CHARSET.contains(username.charAt(i))) {
                // pop out an error message if the username contains characters not in Constants.LEGAL_USERNAME_CHARSET
                System.out.println(Constants.colorWrapper("The username can only contain spaces, underscores, " +
                        "digits and letters!", Constants.RED));
                return false;
            }
        }

        if (Database.usernameToUuid.containsKey(username)) {
            // pop out an error message if the username is used other users
            System.out.println(Constants.colorWrapper("The username is being used!", Constants.RED));
            return false;
        }
        if (username.length() > Constants.MAX_USERNAME_LENGTH) {
            // pop out an error message if the length of username is greater than 20
            System.out.println(Constants.colorWrapper("The length cannot be greater than 20!", Constants.RED));
            return false;
        }
        if (username.length() < Constants.MIN_USERNAME_LENGTH) {
            // pop out an error message if the length of username is smaller than 4
            System.out.println(Constants.colorWrapper("The length cannot be smaller than 4!", Constants.RED));
            return false;
        }
        return true;
    }

    /**
     * Judges if the full name is valid, a full name is valid if and only if
     * 1. it is not null
     * 2. it length is not smaller than 2 and not greater than 40
     * 3. it contains uppercase letters and spaces only
     * 4. it must contain at least one uppercase letter
     * 5. it has no leading and trailing whitespaces
     *
     * @param fullName a String containing the full name being checked.
     * @return true if full name is valid, false otherwise
     */
    public static boolean isFullNameValid(String fullName) {
        if (fullName == null) {
            return false;
        }
        if (fullName.length() < Constants.MIN_FULL_NAME_LENGTH) {
            // pop out an error message if the length of full name is not smaller than 3
            System.out.println(Constants.colorWrapper("The length cannot be smaller than 3!", Constants.RED));
            return false;
        }
        if (fullName.length() > Constants.MAX_FULL_NAME_LENGTH) {
            // pop out an error message if the length of full name is greater than 40
            System.out.println(Constants.colorWrapper("The length cannot be greater than 40!", Constants.RED));
            return false;
        }
        for (int i = 0; i < fullName.length(); ++i) {
            char ch = fullName.charAt(i);
            if (ch >= 'A' && ch <= 'Z') {
                continue;
            }
            if (ch == ' ') {
                continue;
            }
            // pop out an error message if the full name contains characters other than uppercase letters and whitespaces
            System.out.println(Constants.colorWrapper("Full name must only contain uppercase letters and whitespaces!",
                    Constants.RED));
            return false;
        }

        boolean hasUpperCase = false;
        for (int i = 0; i < fullName.length(); ++i) {
            char ch = fullName.charAt(i);
            if (ch >= 'A' && ch <= 'Z') {
                hasUpperCase = true;
                break;
            }
        }
        if (!hasUpperCase) {
            // pop out an error message if the full name does not contain any uppercase letter
            System.out.println(Constants.colorWrapper("Full name must contain at least one uppercase letter!",
                    Constants.RED));
            return false;
        }

        if (!fullName.trim().equals(fullName)) {
            // pop out an error message if the full name contains any redundant trailing whitespaces
            System.out.println(Constants.colorWrapper("Full name must not contain leading or trailing whitespaces!",
                    Constants.RED));
            return false;
        }

        return true;
    }

    /**
     * Judges if the gender is valid, a gender is valid if and only if
     * 1. it is not null
     * 2. it is 'M' or 'F'
     *
     * @param gender a String containing the gender being checked.
     * @return true if gender is valid, false otherwise.
     */
    public static boolean isGenderValid(String gender) {
        if (gender == null) {
            return false;
        }
        if (!gender.equals(Constants.GENDER[0]) && !gender.equals(Constants.GENDER[1])) {
            // pop out an error message if the gender is not 'M' and not 'F'
            System.out.println(Constants.colorWrapper("Gender must be M or F!", Constants.RED));
            return false;
        }
        return true;
    }

    /**
     * Judges if the year as a string is a leap year, the year is a leap year if and only if
     * 1. it is an integer
     * 2. it is in the range of int
     * 3. it is greater than 0
     * 4. it is not divisible by 100 but divisible by 4, or
     * 5. it is divisible by 400
     *
     * @param yearStr a String containing the year being checked fro a leap year.
     * @return true if year is a leap year, false otherwise.
     */
    public static boolean isYearLeap(String yearStr) {
        if (!isInteger(yearStr)) {
            return false;
        }
        int year;
        try {
            year = Integer.parseInt(yearStr);
            if (year <= 0) {
                throw new IllegalArgumentException();
            }
            return ((year % 100 != 0 && year % 4 == 0) || (year % 400 == 0));
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * Judges if the year is valid as a string, the year is valid if and only if
     * it is an integer and not smaller than 1900 and not greater than current year
     *
     * @param year a String containing the year being checked.
     * @return true if year is valid, false otherwise
     */
    public static boolean isYearValid(String year) {
        if (!isInteger(year)) {
            return false;
        }
        BigInteger temp = new BigInteger(year);
        if (temp.compareTo(Constants.MIN_YEAR) < 0) {
            // pop out an error message if the year < 1900
            System.out.println(Constants.colorWrapper("Year must be not smaller than 1900!", Constants.RED));
            return false;
        }
        if (temp.compareTo(Constants.CURRENT_YEAR) > 0) {
            // pop out an error message if the year > current year
            System.out.println(Constants.colorWrapper("Year must be not greater than current year!", Constants.RED));
            return false;
        }
        return true;
    }

    /**
     * Judges if the month is valid, the month is valid if and only if
     * it is not smaller than 1 and not greater than 12
     *
     * @param month a String containing the month being checked.
     * @return true if month is valid, false otherwise.
     */
    public static boolean isMonthValid(String month) {
        if (month == null) {
            return false;
        }
        if (!Constants.VALID_MONTH.contains(month)) {
            // pop out an error message the format is error or out of range
            System.out.println(Constants.colorWrapper("Month out of range or invalid format(month < 10 must contain " +
                    "exactly 1 leading zero)!", Constants.RED));
            return false;
        }

        return true;
    }

    /**
     * Judges if the day is valid as string, the day is valid if and only if
     * it is not smaller than 1 and not greater than the maximum days of the month.
     * Pre-condition: the year and the month are all valid.
     *
     * @param year  a String containing the year.
     * @param month a String containing the month.
     * @param day   a String containing the day being checked.
     * @return true if year is valid, false otherwise.
     */
    public static boolean isDayValid(String year, String month, String day) {
        if (!isYearValid(year)) {
            return false;
        }
        if (!isMonthValid(month)) {
            return false;
        }
        if (day == null) {
            return false;
        }
        if (Constants.DAY_BELOW_TEN.matcher(day).matches()) {
            return true;
        }

        int legalDayLength = 2;
        if (day.length() != legalDayLength || !isInteger(day)) {
            // pop out an error message the format is error
            System.out.println(Constants.colorWrapper("Day is in invalid format(day < 10 must contain " +
                    "exactly 1 leading zero)!", Constants.RED));
            return false;
        } else {
            int dayInt   = Integer.parseInt(day);
            int monthInt = Integer.parseInt(month);
            if (isYearLeap(year) && month.equals(Constants.FEBRUARY) && dayInt > 0 && dayInt <= Integer.parseInt(Constants.MONTH_DAYS[monthInt - 1]) + 1) {
                return true;
            }
            if (dayInt > 0 && dayInt <= Integer.parseInt(Constants.MONTH_DAYS[monthInt - 1])) {
                return true;
            }
            // pop out an error message the format is out of range
            System.out.println(Constants.colorWrapper("Day is out of range!", Constants.RED));
            return false;
        }
    }

    /**
     * Judges if the date of birth is valid as a {@code DataOfBirth} class, it is valid if and only if
     * 1. it is not null
     * 2. every field of the object is valid
     *
     * @param dateOfBirth a String containing the date of birth being checked.
     * @return true if date of birth is valid, false otherwise.
     */
    public static boolean isDateOfBirthValid(DateOfBirth dateOfBirth) {
        if (dateOfBirth == null) {
            return false;
        }
        return isDayValid(dateOfBirth.getYear(), dateOfBirth.getMonth(), dateOfBirth.getDay());
    }

    /**
     * Judges if the email address is valid, an email address is valid if and only if
     * 1. it is not null
     * 2. its length is greater than 40
     * 3. email address only contains lowercase letters, digits, dashes, underlines, dots, @
     * 4. its format satisfies the pre-defined regex
     *
     * @param emailAddress a String containing the email address being checked.
     * @return true if email address is valid, false otherwise.
     */
    public static boolean isEmailAddressValid(String emailAddress) {
        if (emailAddress == null) {
            return false;
        }
        if (emailAddress.length() > Constants.MAX_EMAIL_ADDRESS_LENGTH) {
            // pop out an error message if the length of email address > 40
            System.out.println(Constants.colorWrapper("Email address should not be longer than 40!", Constants.RED));
            return false;
        }

        for (int i = 0; i < emailAddress.length(); ++i) {
            char ch = emailAddress.charAt(i);
            if (ch >= 'a' && ch <= 'z') {
                continue;
            }
            if (ch >= '0' && ch <= '9') {
                continue;
            }
            if (ch == '-' || ch == '_' || ch == '@' || ch == '.') {
                continue;
            }
            System.out.println(Constants.colorWrapper("Email address contains illegal characters!", Constants.RED));
            return false;
        }

        if (!Constants.EMAIL_ADDRESS_REGEX.matcher(emailAddress).matches()) {
            // pop out an error message if the email address failed to pass the regex
            System.out.println(Constants.colorWrapper("Email address format invalid!", Constants.RED));
            return false;
        }
        return true;
    }

    /**
     * Judges if the phone number is valid, a phone number is valid if and only if
     * 1. it is not null
     * 2. its format satisfies the pre-defined regex
     *
     * @param phoneNumber a String containing the phone number being checked.
     * @return true if phone number is valid, false otherwise.
     */
    public static boolean isPhoneNumberValid(String phoneNumber) {
        if (phoneNumber == null) {
            return false;
        }
        if (!Constants.PHONE_NUMBER_REGEX.matcher(phoneNumber).matches()) {
            // pop out an error message if the phone number failed to pass the regex
            System.out.println(Constants.colorWrapper("Phone number format invalid!", Constants.RED));
            return false;
        }
        return true;
    }

    /**
     * Judges if the address is valid, an address is valid if and only if
     * 1. it is not null
     * 2. it is not empty
     * 3. its length is not greater than 50
     * 4. it contains letters, digits, spaces, dashes, dots and commas only
     *
     * @param address a String containing the address being checked.
     * @return true if address is valid, false otherwise.
     */
    public static boolean isAddressValid(String address) {
        if (address == null) {
            return false;
        }
        if (address.isEmpty()) {
            // pop out an error message if the address is empty
            System.out.println(Constants.colorWrapper("Address cannot be empty!", Constants.RED));
            return false;
        }
        if (address.length() > Constants.MAX_ADDRESS_LENGTH) {
            // pop out an error message if the length of address > 80
            System.out.println(Constants.colorWrapper("The length of address cannot be longer than 80!",
                    Constants.RED));
            return false;
        }

        for (int i = 0; i < address.length(); ++i) {
            char ch = address.charAt(i);
            if (ch >= 'A' && ch <= 'Z') {
                continue;
            }
            if (ch >= 'a' && ch <= 'z') {
                continue;
            }
            if (ch >= '0' && ch <= '9') {
                continue;
            }
            if (ch == ' ' || ch == '-' || ch == ',' || ch == '.') {
                continue;
            }
            // pop out an error message if it contains illegal characters
            System.out.println(Constants.colorWrapper("The address can only contains letters, digits, spaces, dashes" +
                            ", dots and commas!",
                    Constants.RED));
            return false;
        }
        return true;
    }

    /**
     * Judges if the password is valid, a password is valid if and only if
     * 1. it is not null
     * 2. its length is equal to or greater than 6
     *
     * @param password a String containing the password being checked.
     * @return true if password is valid, false otherwise.
     */
    public static boolean isPasswordValid(String password) {
        if (password == null) {
            return false;
        }
        if (password.length() < Constants.MIN_PASSWORD_LENGTH) {
            // pop out an error message if the length of address < 6
            System.out.println(Constants.colorWrapper("The length of password cannot be smaller than 6!",
                    Constants.RED));
            return false;
        }
        boolean isLegal = true;
        for (int i = 0; i < password.length(); ++i) {
            char ch = password.charAt(i);
            if (!Constants.LEGAL_PASSWORD_CHARSET.contains(ch)) {
                isLegal = false;
                break;
            }
        }
        if (!isLegal) {
            // pop out an error message if the password contains a non-legal characters
            System.out.println(Constants.colorWrapper("The password cannot contain a non-legal character!",
                    Constants.RED));
            return false;
        }
        return true;
    }

    /**
     * Judges if the course id is valid, a course id is valid if and only if
     * 1. it is not null
     * 2. it is not empty
     * 3. it has no duplicates
     * 4. it must satisfy the format of Constants.COURSE_ID_REGEX
     * 5. its length is not greater than 10
     *
     * @param courseId a String containing the course id being checked.
     * @return true if course id is valid, false otherwise.
     */
    public static boolean isCourseIdValid(String courseId) {

        if (courseId == null) {
            return false;
        }
        if (courseId.isEmpty()) {
            // pop out an error message if courseId is empty
            System.out.println(Constants.colorWrapper("The course Id cannot be empty!", Constants.RED));
            return false;
        }
        if (Database.courses.containsKey(courseId)) {
            // pop out an error message if courseId does not exist in the database
            System.out.println(Constants.colorWrapper("The course Id exists already!", Constants.RED));
            return false;
        }
        if (courseId.length() > Constants.MAX_COURSE_ID_LENGTH) {
            // pop out an error message if length of courseId is longer than 10
            System.out.println(Constants.colorWrapper("The length of course Id cannot be longer than 10!",
                    Constants.RED));
            return false;
        }

        if (!Constants.COURSE_ID_REGEX.matcher(courseId).matches()) {
            System.out.println(Constants.colorWrapper("The course Id does not satisfy the format!", Constants.RED));
            return false;
        }

        return true;
    }

    /**
     * Judges if the course name is valid, a course name is valid if and only if
     * 1. it is not null
     * 2. it is not empty
     *
     * @param courseName a String containing the course name being checked.
     * @return true if course name is valid, false otherwise.
     */
    public static boolean isCourseNameValid(String courseName) {

        if (courseName == null) {
            return false;
        }
        if (courseName.isEmpty()) {
            // pop out an error message if courseName is empty
            System.out.println(Constants.colorWrapper("The course name cannot be empty!", Constants.RED));
            return false;
        }
        return true;
    }

    /**
     * Judges if the course description is valid, a course description is valid if and only if
     * 1. it is not null
     * 2. it is not empty
     *
     * @param courseDescription a String containing the course description being checked.
     * @return true if course description is valid, false otherwise.
     */
    public static boolean isCourseDescriptionValid(String courseDescription) {

        if (courseDescription == null) {
            return false;
        }
        if (courseDescription.isEmpty()) {
            // pop out an error message if course description is empty
            System.out.println(Constants.colorWrapper("The course description cannot be empty!", Constants.RED));
            return false;
        }
        return true;
    }

    /**
     * Judges if the num is a legal integer
     * 1. the num must not be null
     * 2. the num must satisfy the regex, redundant '+' like '+12' or redundant trailing zeroes like '12.000' are not
     * allowed
     *
     * @param num the number to check
     * @return true if the num is a valid integer, false otherwise
     */
    public static boolean isInteger(String num) {
        return (num != null && Constants.INTEGER_REGEX.matcher(num).matches());
    }

    /**
     * Judges if the num is a legal integer
     * 1. the num must not be null
     * 2. the num must satisfy the regex, E-notation or redundant '+' like '+1.5' or redundant trailing zeroes like
     * '0.0' or lacking integer parts like '.5' are not allowed
     *
     * @param num the number to check
     * @return true if the num is a valid double, false otherwise
     */
    public static boolean isDouble(String num) {
        return (num != null && Constants.DOUBLE_REGEX.matcher(num).matches());
    }

    /**
     * Judges if the course units is valid as a string, the string is valid if and only if
     * 1. it can be converted to a number
     * 2. it has no redundant leading zeroes or trailing zeroes
     * 3. it is greater than 0
     * 4. it is not greater than 15
     *
     * @param courseUnits a String containing the course units being checked.
     * @return true if course units is valid as a string, false otherwise.
     */
    public static boolean isCourseUnitsValid(String courseUnits) {

        boolean result;
        if (isInteger(courseUnits)) {
            try {
                BigInteger temp = new BigInteger(courseUnits);
                if (temp.compareTo(BigInteger.ZERO) <= 0) {
                    throw new IllegalArgumentException();
                }
                if (temp.compareTo(Constants.MAX_UNITS_AS_INT) > 0) {
                    throw new IllegalArgumentException();
                }
                result = true;
            } catch (Exception e) {
                result = false;
            }
        } else if (isDouble(courseUnits)) {
            try {
                BigDecimal temp = new BigDecimal(courseUnits);
                if (temp.compareTo(BigDecimal.ZERO) <= 0) {
                    throw new IllegalArgumentException();
                }
                if (temp.compareTo(Constants.MAX_UNITS_AS_DOUBLE) > 0) {
                    throw new IllegalArgumentException();
                }
                result = true;
            } catch (Exception e) {
                result = false;
            }
        } else {
            result = false;
        }

        if (!result) {
            // pop out an error message if the format of course units is invalid
            System.out.println(Constants.colorWrapper("The format of course units is invalid!", Constants.RED));
        }

        return result;
    }


    /**
     * Judge if the grade(GPA) is valid as a string, the string is valid if and only if
     * 1. it can be converted to a number
     * 2. it has no redundant leading zeroes or trailing zeroes, cases like '0.0', '4.0' are invalid
     * 3. it is not smaller than 0
     * 4. it is not greater than 4
     * 5. if it contains decimals, only one decimal is allowed, thus cases like '3.12', '1.1841' are invalid
     *
     * @param grade a String containing the grade being checked.
     * @return true if grade is valid, false otherwise.
     */
    public static boolean isGradeValid(String grade) {

        boolean result;
        if (isInteger(grade)) {

            try {
                BigInteger temp = new BigInteger(grade);
                if (temp.compareTo(BigInteger.ZERO) < 0) {
                    throw new IllegalArgumentException();
                }
                if (temp.compareTo(Constants.MAX_GRADE_AS_INT) > 0) {
                    throw new IllegalArgumentException();
                }
                result = true;
            } catch (Exception e) {
                result = false;
            }
        } else if (isDouble(grade)) {

            try {
                BigDecimal temp = new BigDecimal(grade);
                if (temp.compareTo(BigDecimal.ZERO) < 0) {
                    throw new IllegalArgumentException();
                }
                if (temp.compareTo(Constants.MAX_GRADE_AS_DOUBLE) > 0) {
                    throw new IllegalArgumentException();
                }

                // only one decimal is allowed
                int countOfDigits = 0;
                int i             = 0;
                for (; i < grade.length(); ++i) {
                    if (grade.charAt(i) == '.') {
                        break;
                    }
                }
                ++i;
                for (; i < grade.length(); ++i) {
                    ++countOfDigits;
                }
                if (countOfDigits != 1) {
                    throw new IllegalArgumentException();
                }

                result = true;
            } catch (Exception e) {
                result = false;
            }

        } else {
            result = false;
        }
        return result;
    }


    /**
     * Judge if the prerequisites of a course is valid as a string, the string is valid if and only if
     * 1. it is not null
     * 2. it does not contains redundant leading or trailing whitespaces
     * 3. it exists in the database
     * 4. it does not contain duplicated course ids.
     *
     * @param prerequisites a String containing the prerequisites being checked.
     * @return true if prerequisite of a course is valid as a string, false otherwise.
     */
    public static boolean isPrerequisitesValid(String prerequisites) {

        if (prerequisites == null) {
            return false;
        }

        if (!prerequisites.equals(prerequisites.trim())) {
            // pop out an error message if the string contains redundant leading or trailing whitespaces
            System.out.println(Constants.colorWrapper("The string of course ids cannot contain redundant leading or " +
                    "trailing whitespaces!", Constants.RED));
            return false;
        }

        HashSet<String> temp = Course.parser(prerequisites);
        if (!temp.isEmpty()) {
            for (String i : temp) {
                if (!Database.courses.containsKey(i) && !i.equals(Constants.CONSENT_BY_DEPARTMENT)) {
                    // pop out an error message if the course does not exist
                    System.out.println(Constants.colorWrapper("The course " + i + " does not exist!",
                            Constants.RED));
                    return false;
                }
            }

            String[] tempArr = prerequisites.split(" ");
            if (tempArr.length != temp.size()) {
                // pop out an error message if the string of prerequisites contains duplicated courses
                System.out.println(Constants.colorWrapper("The prerequisites contain duplicated courses!", Constants.RED));
                return false;
            }
        }
        return true;
    }

    /**
     * Judges if the antirequisites of a course is valid as a hash set, the hash set is valid if and only if
     * it is not null
     * it exists in the database
     *
     * @param antirequisites a String containing the anti-requisites being checked.
     * @return true if antirequisites is valid as a hash set, false otherwise.
     */
    public static boolean isAntirequisitesValid(HashSet<String> antirequisites) {

        if (antirequisites == null) {
            return false;
        }
        for (String i : antirequisites) {
            if (!Database.courses.containsKey(i)) {
                // pop out an error message if the course does not exist
                System.out.println(Constants.colorWrapper("The course " + i + " does not exist!",
                        Constants.RED));
                return false;
            }
        }

        return true;
    }

    /**
     * Judges if the antirequisites of a course is valid as a string, the string is valid if and only if
     * 1. it is not null
     * 2. it has no redundant leading or trailing whitespaces
     * 3. it exists in the database
     * 4. it does not contain duplicated course ids.
     *
     * @param antirequisites a String containing the antirequisites being checked.
     * @return true if anti-requisite of a course is valid, false otherwise
     */
    public static boolean isAntirequisitesValidAsAString(String antirequisites) {

        if (antirequisites == null) {
            return false;
        }

        if (!antirequisites.trim().equals(antirequisites)) {
            // pop out an error message if the course does not exist
            System.out.println(Constants.colorWrapper("The string of course ids cannot contain redundant leading or " +
                    "trailing whitespaces!", Constants.RED));
            return false;
        }

        HashSet<String> temp = Course.parser(antirequisites);
        if (!temp.isEmpty()) {

            if (!isAntirequisitesValid(temp)) {
                return false;
            }

            String[] tempArr = antirequisites.split(" ");
            if (tempArr.length != temp.size()) {
                // pop out an error message if the string of antirequisites contains duplicated courses
                System.out.println(Constants.colorWrapper("The antirequisites contain duplicated courses!",
                        Constants.RED));
                return false;
            }
        }
        return true;
    }

    /**
     * Judges if the property that if a course can be repeated is valid as a string, the string is valid if and only if
     * 1. it is not null
     * 2. it is 'YES' or 'NO'
     *
     * @param canBeRepeated a String containing the course that can be repeated being checked.
     * @return true if course that can be repeated is valid as a string, false otherwise.
     */
    public static boolean isCanBeRepeatedValid(String canBeRepeated) {

        if (canBeRepeated == null) {
            return false;
        }
        if (!canBeRepeated.equals(Constants.RESPONSE_YES) && !canBeRepeated.equals(Constants.RESPONSE_NO)) {
            // pop out an error message if the string of is not 'YES' and not 'NO
            System.out.println(Constants.colorWrapper("The answer must be 'YES' or 'NO'!", Constants.RED));
            return false;
        }

        return true;
    }

    /**
     * Judges if the student has taken all courses of the prerequisites of the course id, pop out an error message it
     * the
     * prerequisites are not satisfied
     *
     * @param student  a String containing information os the student user.
     * @param courseId a String containing the course ID being checked.
     * @return true if no courses in the antirerequisites are taken, false otherwise.
     */
    public static boolean satisfyPrerequisites(Student student, String courseId) {

        if (student == null) {
            return false;
        }
        if (!Database.courses.containsKey(courseId)) {
            return false;
        }

        // add all courses from current courses list to a hash set
        HashSet<String> allCourses = new HashSet<>(student.getCurrentCoursesList());

        // add all courses which were taken by the student to the hash set
        ArrayList<CourseAndGrade> temp = student.getTakenCoursesList();
        for (CourseAndGrade i : temp) {
            allCourses.add(i.getCourseId());
        }

        // compare the prerequisites of courseId, if there is one which does not exist in allCourses, pop out a msg and
        // return false
        HashSet<String> prerequisites = Database.courses.get(courseId).getPrerequisites();
        for (String i : prerequisites) {
            if (!allCourses.contains(i)) {
                System.out.println(Constants.colorWrapper("You MUST take " + i + " before taking " + courseId + "!",
                        Constants.RED));
                return false;
            }
        }

        return true;
    }

    /**
     * Judge if the student has taken any course which is in the antirerequisites of the course id, pop out an error
     * message when it is true
     *
     * @param student  the student to check
     * @param courseId the course id to check
     * @return true if no courses in the antirerequisites are taken, false otherwise
     */
    public static boolean satisfyAntirerequisites(Student student, String courseId) {

        if (student == null || !Database.courses.containsKey(courseId)) {
            return false;
        }

        // add all courses from current courses list to a hash set
        HashSet<String> allCourses = new HashSet<>(student.getCurrentCoursesList());

        // add all courses which were taken by the student to the hash set
        ArrayList<CourseAndGrade> temp = student.getTakenCoursesList();
        for (CourseAndGrade i : temp) {
            allCourses.add(i.getCourseId());
        }

        // compare the antirequisites of courseId, if any one of allCourses exist in the antirequisites , pop out a msg and
        // return false
        HashSet<String> antirequisites = Database.courses.get(courseId).getAntirequisites();
        for (String i : antirequisites) {
            if (allCourses.contains(i)) {
                System.out.println(Constants.colorWrapper("You have taken or are taking " + i + " which is equivalent to " + courseId +
                        "!", Constants.RED));
                return false;
            }
        }
        return true;
    }
}

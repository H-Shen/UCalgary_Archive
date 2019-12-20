import java.math.BigDecimal;
import java.util.HashSet;

/**
 * A collection of functions use to validate all types of data involved in the app.
 */

public class Validation {

    // Validation of Uuid
    public static boolean isUuidValid(String uuid) {
        // Make sure the role cannot be reset to null
        // Make sure the Uuid is unique
        return (uuid != null && !Database.accounts.containsKey(uuid));
    }

    // Validation of role
    public static boolean isRoleValid(String role) {

        // Make sure the role cannot be reset to null
        if (role != null) {
            for (String i : Constants.ROLE) {
                if (role.compareTo(i) == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    // Validation of username
    public static boolean isUsernameValid(String username) {
        // Make sure the username cannot be reset to null
        // Make sure the username cannot be empty
        // Make sure the username cannot be the same and is unique
        return (username != null && !username.isEmpty() && !Database.usernameToUuid.containsKey(username));
    }

    // Validation of fullName
    public static boolean isFullNameValid(String fullName) {

        // Make sure the fullname cannot be reset to null
        // at least Constants.MIN_FULL_NAME_LENGTH
        // contain letters and spaces only
        // must contain at least one letter
        if (fullName != null) {
            boolean hasLetter = false;
            if (fullName.length() >= Constants.MIN_FULL_NAME_LENGTH) {
                for (int i = 0; i < fullName.length(); ++i) {
                    char ch = fullName.charAt(i);
                    if (ch >= 'A' && ch <= 'Z') {
                        hasLetter = true;
                    } else if (ch != ' ') {
                        return false;
                    }
                }
                return hasLetter;
            }
        }
        return false;
    }

    // Validation of gender
    public static boolean isGenderValid(String gender) {

        // Make sure the gender cannot be reset to null
        // Make sure the gender is 'M' or 'F'
        if (gender != null) {
            for (String i : Constants.GENDER) {
                if (gender.equals(i)) {
                    return true;
                }
            }
        }
        return false;
    }

    // Validation of DateOfBirth
    public static boolean isYearLeap(int year) {
        return ((year % 100 != 0 && year % 4 == 0) || (year % 400 == 0));
    }

    public static boolean isYearLegal(int year) {
        return (year >= Constants.MIN_YEAR && year <= Constants.CURRENT_YEAR);
    }

    public static boolean isMonthLegal(int month) {
        return (month >= Constants.MIN_MONTH && month <= Constants.MAX_MONTH);
    }

    public static boolean isDayLegal(int year, int month, int day) {
        if (isYearLeap(year) && month == 2) {
            return (day >= Constants.MIN_DAY && day <= Constants.MONTH_DAYS[month - 1] + 1);
        }
        return (day >= Constants.MIN_DAY && day <= Constants.MONTH_DAYS[month - 1]);
    }

    public static void main(String[] args) {
        System.out.println(isYearLegal("00"));
    }

    public static boolean isYearLegal(String year) {
        // year must be a number
        // year must be without a redundant leading 0s
        // year must be <= MAX_YEAR
        int temp;
        try {
            temp = Integer.parseInt(year);
            if (!String.valueOf(temp).equals(year)) {
                throw new IllegalArgumentException();
            }
            if (temp > Constants.MAX_YEAR) {
                throw new IllegalArgumentException();
            }
        } catch (Exception e) {
            return false;
        }
        return isYearLegal(temp);
    }

    public static boolean isMonthLegal(String month) {
        // month must be a number
        // month must be without a redundant leading 0s
        // month must be in the legal range
        int temp;
        try {
            temp = Integer.parseInt(month);
            if (!String.valueOf(temp).equals(month)) {
                throw new IllegalArgumentException();
            }
        } catch (Exception e) {
            return false;
        }
        return isMonthLegal(temp);
    }

    public static boolean isDayLegal(int year, int month, String day) {
        // month must be a number
        // month must be without a redundant leading 0s
        // month must be in the legal range
        int temp;
        try {
            temp = Integer.parseInt(day);
            if (!String.valueOf(temp).equals(day)) {
                throw new IllegalArgumentException();
            }
        } catch (Exception e) {
            return false;
        }
        return isDayLegal(year, month, temp);
    }

    public static boolean isDateOfBirthValid(DateOfBirth dateOfBirth) {

        // Make sure the DateOfBirth cannot be reset to null
        if (dateOfBirth == null) {
            return false;
        }

        if (isYearLegal(dateOfBirth.getYear())) {
            if (isMonthLegal(dateOfBirth.getMonth())) {
                return isDayLegal(dateOfBirth.getYear(), dateOfBirth.getMonth(), dateOfBirth.getDay());
            }
        }
        return false;
    }

    // Validation of emailAddress
    public static boolean isEmailAddressValid(String emailAddress) {

        // Make sure the emailAddress cannot be reset to null
        // Make sure the formation satisfied the regex
        return (emailAddress != null && Constants.EMAIL_ADDRESS_REGEX.matcher(emailAddress).matches());
    }

    // Validation of phoneNumber
    public static boolean isPhoneNumberValid(String phoneNumber) {

        // Make sure the phoneAddress cannot be reset to null
        // Make sure the formation satisfied the regex
        return (phoneNumber != null && Constants.PHONE_NUMBER_REGEX.matcher(phoneNumber).matches());
    }

    // Validation of address
    public static boolean isAddressValid(String address) {

        // Make sure the address is not null and not empty
        return (address != null && !address.isEmpty());
    }

    // Validation of password
    public static boolean isPasswordValid(String password) {

        // Make sure the password is not null and >= minimum length
        return (password != null && password.length() >= Constants.MIN_PASSWORD_LENGTH);
    }

    // Validation of courseId
    public static boolean isCourseIdValid(String courseId) {

        // Make sure the password is not null
        // Make sure the courseId is not empty
        // Make sure the courseId is unique
        return (courseId != null && !Database.courses.containsKey(courseId) && !courseId.isEmpty());
    }

    // Validation of courseName
    public static boolean isCourseNameValid(String courseName) {

        // Make sure the courseName is not null and not empty
        return (courseName != null && !courseName.isEmpty());
    }

    // Validation of CourseDescription
    public static boolean isCourseDescriptionValid(String courseDescription) {

        // Make sure the courseName is not null and not empty
        return (courseDescription != null && !courseDescription.isEmpty());
    }

    // Validation of courseUnits
    public static boolean isCourseUnitsValid(String courseUnits) {
        // Make sure the courseUnits must be a number
        // Make sure the courseUnits has no redundant leading 0s
        // Make sure the courseUnits > 0
        // Make sure the courseUnits <= Constants.MAX_UNITS;
        try {
            BigDecimal temp = new BigDecimal(courseUnits);
            if (!temp.toString().equals(courseUnits)) {
                throw new IllegalArgumentException();
            }
            if (temp.compareTo(BigDecimal.ZERO) <= 0) {
                throw new IllegalArgumentException();
            }
            return (temp.compareTo(Constants.MAX_UNITS) <= 0);
        } catch (Exception e) {
            return false;
        }
    }

    // Validation of a string of prerequisites
    public static boolean isPrerequisitesValid(String prerequisites) {

        // Make sure the prerequisites is not null
        if (prerequisites == null) {
            return false;
        }

        // make sure the course id in prerequisites list exist in Database.course
        HashSet<String> temp = Course.purser(prerequisites);
        // check when there are prerequisites
        if (!temp.isEmpty()) {
            for (String i : temp) {
                if (!Database.courses.containsKey(i) && !i.equals("CONSENT_BY_DEPARTMENT")) {
                    return false;
                }
            }

            // check no duplicates
            String[] tempArr = prerequisites.split(" ");
            if (tempArr.length != temp.size()) {
                return false;
            }
        }

        return true;
    }


    // Validation of antirequisites
    public static boolean isAntirequisitesValid(HashSet<String> antirequisites) {
        // Make sure the antirequisites is not null
        return (antirequisites != null);
    }

    // Validation of string of antirequisites
    public static boolean isAntirequisitesValid(String antirequisites) {

        // Make sure the antirequisites is not null
        if (antirequisites == null) {
            return false;
        }

        // make sure the course id in antirequisites list exist in Database.course
        HashSet<String> temp = Course.purser(antirequisites);
        // check when there are antirequisites
        if (!temp.isEmpty()) {
            for (String i : temp) {
                if (!Database.courses.containsKey(i)) {
                    return false;
                }
            }

            // check no duplicates
            String[] tempArr = antirequisites.split(" ");
            if (tempArr.length != temp.size()) {
                return false;
            }
        }
        return true;
    }

    public static boolean isCanBeRepeatedValid(String canBeRepeated) {

        // Make sure the canBeRepeated is not null
        // Make sure the canBeRepeated is YES or NO
        return (canBeRepeated != null && (canBeRepeated.equals("YES") || canBeRepeated.equals("NO")));
    }
}

import java.util.Scanner;

/**
 * The definition of the User generic class.
 */

abstract class User implements Comparable<User> {

    /**
     * Filepath configuration
     */
    private static String enrollmentStatus = "CLOSE";
    private static String ENROLLMENT_CONF  = "/Users/hshen/IdeaProjects/Project/res/enrollment.conf";
    /**
     * Once uuid is initialized, it cannot be changed.
     */
    private String      uuid;
    private String      role;
    private String      username;
    private String      fullName;
    private String      gender;
    private DateOfBirth dateOfBirth;
    private String      emailAddress;
    private String      phoneNumber;
    private String      address;
    private String      password;
    public User() {
        initializeEnrollmentStatus();
    }

    public static String getEnrollmentStatus() {
        return enrollmentStatus;
    }

    public static void setEnrollmentStatus(String status) {
        switch (status) {
            case "OPEN":
                enrollmentStatus = "OPEN";
                updateEnrollmentStatusToFile(enrollmentStatus);
                break;
            case "CLOSE":
                enrollmentStatus = "CLOSE";
                updateEnrollmentStatusToFile(enrollmentStatus);
                break;
            default:
                break;
        }
    }

    public static void updateEnrollmentStatusToFile(String status) {
        try (BufferedWriter out = new BufferedWriter(new FileWriter(ENROLLMENT_CONF))) {
            out.write(status);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void initializeEnrollmentStatus() {

        if (!Constants.TEST_IN_IDE) {
            ENROLLMENT_CONF = new File(System.getProperty("user.dir")).getParent() + "/res/enrollment.conf";
        }

        try (Scanner in = new Scanner(new FileInputStream(ENROLLMENT_CONF))) {
            while (in.hasNextLine()) {
                setEnrollmentStatus(in.nextLine());
                break;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 3 way-comparison by the username's lexicographical order.
     *
     * @param o
     * @return -1 if o cannot be cast to User or its username is smaller, 0 if the usernames are the same, 1 otherwise.
     */
    @Override
    public int compareTo(User o) {
        if (o != null) {
            return getUsername().compareTo(o.getUsername());
        }
        return -1;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {

        // Make sure the uuid cannot be changed after being initialization
        // Make sure the uuid is valid.
        if (this.uuid == null && Validation.isUuidValid(uuid)) {
            this.uuid = uuid;
        }
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {

        if (Validation.isRoleValid(role)) {
            this.role = role;
        }
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {

        // Make sure the username is valid
        if (Validation.isUsernameValid(username)) {
            this.username = username;
        }
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {

        // Make sure the fullname is valid
        if (Validation.isFullNameValid(fullName)) {
            this.fullName = fullName;
        }
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {

        // Make sure the gender is valid
        if (Validation.isGenderValid(gender)) {
            this.gender = gender;
        }
    }

    public DateOfBirth getDateOfBirth() {
        return new DateOfBirth(dateOfBirth);
    }

    public void setDateOfBirth(DateOfBirth dateOfBirth) {

        // Make sure the DateOfBirth is valid
        if (Validation.isDateOfBirthValid(dateOfBirth)) {
            this.dateOfBirth = new DateOfBirth(dateOfBirth);
        }
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {

        // Make sure the emailAddress is valid
        if (Validation.isEmailAddressValid(emailAddress)) {
            this.emailAddress = emailAddress;
        }
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {

        // Make sure the emailAddress is valid
        if (Validation.isPhoneNumberValid(phoneNumber)) {
            this.phoneNumber = phoneNumber;
        }
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {

        // Make sure the password is valid
        if (Validation.isPasswordValid(password)) {
            this.password = password;
        }
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {

        // Make sure the emailAddress is valid
        if (Validation.isAddressValid(address)) {
            this.address = address;
        }
    }

    public String toString(Constants.USER_FIELD highlightField, String keyword) {

        StringBuilder sb = new StringBuilder();

        String username;
        if (highlightField == Constants.USER_FIELD.USERNAME) {
            username = TextUI.colorHighlightedSubstring(getUsername(), keyword);
        } else {
            username = getUsername();
        }
        String role;
        if (highlightField == Constants.USER_FIELD.ROLE) {
            role = TextUI.colorHighlightedSubstring(getRole(), keyword);
        } else {
            role = getRole();
        }
        String gender;
        if (highlightField == Constants.USER_FIELD.GENDER) {
            gender = TextUI.colorHighlightedSubstring(getGender(), keyword);
        } else {
            gender = getGender();
        }

        sb.append(Constants.COLOR_WRAPPER("         UUID : ", Constants.WHITE_BOLD) + getUuid() + "\n");
        sb.append(Constants.COLOR_WRAPPER("     USERNAME : ", Constants.WHITE_BOLD) + username + "\n");
        sb.append(Constants.COLOR_WRAPPER("     FULLNAME : ", Constants.WHITE_BOLD) + getFullName() + "\n");
        sb.append(Constants.COLOR_WRAPPER("         ROLE : ", Constants.WHITE_BOLD) + role + "\n");
        sb.append(Constants.COLOR_WRAPPER("       GENDER : ", Constants.WHITE_BOLD) + gender + "\n");
        sb.append(Constants.COLOR_WRAPPER("      ADDRESS : ", Constants.WHITE_BOLD) + getAddress() + "\n");
        sb.append(Constants.COLOR_WRAPPER(" PHONE NUMBER : ", Constants.WHITE_BOLD) + getPhoneNumber() + "\n");
        sb.append(Constants.COLOR_WRAPPER("DATE OF BIRTH : ", Constants.WHITE_BOLD) + getDateOfBirth() + "\n");
        sb.append(Constants.COLOR_WRAPPER("EMAIL ADDRESS : ", Constants.WHITE_BOLD) + getEmailAddress());

        return sb.toString();

    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();

        sb.append(Constants.COLOR_WRAPPER("         UUID : ", Constants.WHITE_BOLD) + getUuid() + "\n");
        sb.append(Constants.COLOR_WRAPPER("     USERNAME : ", Constants.WHITE_BOLD) + getUsername() + "\n");
        sb.append(Constants.COLOR_WRAPPER("     FULLNAME : ", Constants.WHITE_BOLD) + getFullName() + "\n");
        sb.append(Constants.COLOR_WRAPPER("         ROLE : ", Constants.WHITE_BOLD) + getRole() + "\n");
        sb.append(Constants.COLOR_WRAPPER("       GENDER : ", Constants.WHITE_BOLD) + getGender() + "\n");
        sb.append(Constants.COLOR_WRAPPER("      ADDRESS : ", Constants.WHITE_BOLD) + getAddress() + "\n");
        sb.append(Constants.COLOR_WRAPPER(" PHONE NUMBER : ", Constants.WHITE_BOLD) + getPhoneNumber() + "\n");
        sb.append(Constants.COLOR_WRAPPER("DATE OF BIRTH : ", Constants.WHITE_BOLD) + getDateOfBirth() + "\n");
        sb.append(Constants.COLOR_WRAPPER("EMAIL ADDRESS : ", Constants.WHITE_BOLD) + getEmailAddress());

        return sb.toString();
    }
}






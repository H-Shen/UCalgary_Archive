import java.util.Map;

/**
 * The part of TextUI for Administrators. The GUI design will refer its logic.
 */

public class AdminTextUI extends TextUI {

    public AdminTextUI(String uuid) {

        super(uuid);
        Admin user = (Admin) Database.accounts.get(uuid);

        String select;

        while (true) {

            System.out.println(Constants.COLOR_WRAPPER("ADMINISTRATOR PANEL", Constants.WHITE_BOLD));
            System.out.println();

            System.out.println(" 1. List all available accounts");
            System.out.println(" 2. List all available courses");
            System.out.println(" 3. Search a course");
            System.out.println(" 4. Search an account");
            System.out.println(" 5. Modify a course");
            System.out.println(" 6. Modify an account");
            System.out.println(" 7. Enrollment Management");
            System.out.println(" 8. Create an account");
            System.out.println(" 9. Create a course");
            System.out.println("10. Remove an account");
            System.out.println("11. Remove a course");
            System.out.println("12. Personal information");
            System.out.println("13. History log");
            System.out.println("14. Logout");

            System.out.println();
            System.out.print(Constants.COLOR_WRAPPER("Select number: ", Constants.WHITE_BOLD));
            select = Constants.IN.nextLine();
            System.out.println();

            switch (select) {

                // List all available accounts
                case "1":
                    listAllAccounts();
                    break;

                // List all available courses
                case "2":

                    listAllCourses();
                    break;

                // Search a course by the course id keyword
                case "3":
                    searchCourses();
                    System.out.println();
                    break;

                // Search an account by username
                case "4":
                    searchAccounts();
                    System.out.println();
                    break;

                // Modify a course
                case "5":

                    modifyACourse();
                    break;

                // Modify an account
                case "6":

                    modifyAccount();
                    break;

                // Modify enrollment status
                case "7":

                    enrollmentManagement();
                    break;

                // Create an account
                case "8":

                    createAnAccount();
                    break;

                // Create a course
                case "9":

                    createACourse();
                    break;

                // Remove an account
                case "10":

                    removeAnAccount(user.getUsername());
                    break;

                // Remove a course
                case "11":

                    removeACourse();
                    break;

                // Check personal information
                case "12":

                    showAccountInformation(user);
                    break;

                // Check history log
                case "13":
                    checkHistoryLog();
                    break;

                // Return to the main menu
                case "14":
                    Log.userLogout(user.getUuid());
                    return;

                default:
                    System.out.println(Constants.INVALID_INPUT);
                    System.out.println();
            }
        }
    }

    public static void modifyAccount() {

        System.out.print(Constants.COLOR_WRAPPER("Account username: ", Constants.WHITE_BOLD));
        String username = Constants.IN.nextLine();

        if (!Database.usernameToUuid.containsKey(username)) {
            System.out.println(Constants.COLOR_WRAPPER("Account does not exist!", Constants.RED));
            Constants.PRESS_ENTER_KEY_TO_CONTINUE();
            System.out.println();
            return;
        }

        boolean exitMenu = false;
        User    user     = Database.accounts.get(Database.usernameToUuid.get(username));
        showAccountInformation(user);

        while (!exitMenu) {

            String fieldToModify;
            System.out.println();
            System.out.println(" 1. Modify USERNAME");
            System.out.println(" 2. Modify FULLNAME");
            System.out.println(" 3. Modify ROLE");
            System.out.println(" 4. Modify GENDER");
            System.out.println(" 5. Modify ADDRESS");
            System.out.println(" 6. Modify PHONE NUMBER");
            System.out.println(" 7. Modify DATE OF BIRTH");
            System.out.println(" 8. Modify EMAIL ADDRESS");
            System.out.println(" 9. Modify PASSWORD");
            System.out.println("10. Return");
            System.out.println();
            System.out.println(" Note : Invalid value will remain the original data unchanged!");
            System.out.println();

            System.out.print(Constants.COLOR_WRAPPER("Select number: ", Constants.WHITE_BOLD));
            fieldToModify = Constants.IN.nextLine();
            switch (fieldToModify) {

                // new username
                case "1":

                    modifyUsername(user);

                    // print the new user information
                    System.out.println();
                    System.out.println(Database.accounts.get(user.getUuid()));
                    System.out.println();
                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    System.out.println();
                    break;

                // new fullname
                case "2":

                    modifyFullName(user);

                    // print the new user information
                    System.out.println();
                    System.out.println(Database.accounts.get(user.getUuid()));
                    System.out.println();
                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    System.out.println();
                    break;

                // new role
                case "3":

                    modifyRole(user);

                    // print the new user information
                    System.out.println();
                    System.out.println(Database.accounts.get(user.getUuid()));
                    System.out.println();
                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    System.out.println();
                    break;

                // new gender
                case "4":

                    modifyGender(user);

                    // print the new user information
                    System.out.println();
                    System.out.println(Database.accounts.get(user.getUuid()));
                    System.out.println();
                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    System.out.println();
                    break;

                // new address
                case "5":

                    modifyAddress(user);

                    // print the new user information
                    System.out.println();
                    System.out.println(Database.accounts.get(user.getUuid()));
                    System.out.println();
                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    System.out.println();
                    break;

                // new phone number
                case "6":

                    modifyPhoneNumber(user);

                    // print the new user information
                    System.out.println();
                    System.out.println(Database.accounts.get(user.getUuid()));
                    System.out.println();
                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    System.out.println();
                    break;

                // new date of birth
                case "7":

                    modifyDateOfBirth(user);

                    // print the new user information
                    System.out.println();
                    System.out.println(Database.accounts.get(user.getUuid()));
                    System.out.println();
                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    System.out.println();
                    break;

                // new email address
                case "8":

                    modifyEmailAddress(user);

                    // print the new user information
                    System.out.println();
                    System.out.println(Database.accounts.get(user.getUuid()));
                    System.out.println();
                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    System.out.println();
                    break;

                // new password
                case "9":

                    modifyPassword(user);

                    // print the new user information
                    System.out.println();
                    System.out.println(Database.accounts.get(user.getUuid()));
                    System.out.println();
                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    System.out.println();
                    break;

                // return
                case "10":

                    exitMenu = true;
                    break;

                default:

                    System.out.println(Constants.INVALID_INPUT);
                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    System.out.println();
            }
        }
    }

    public static void main(String[] args) {
        enrollmentManagement();
    }

    public static void createACourseSchedule() {
        System.out.println();
        String courseId;
        while (true) {
            System.out.print("Course id: ");
            courseId = Constants.IN.nextLine();
            if (!Database.courses.containsKey(courseId)) {
                System.out.println(Constants.COLOR_WRAPPER("Course id does not exist!", Constants.RED));
            } else {
                break;
            }
        }
        String facultyUsername;
        while (true) {
            System.out.println("Faculty username: ");
            facultyUsername = Constants.IN.nextLine();
            if (Database.usernameToUuid.containsKey(facultyUsername)) {
                User tempUser = Database.accounts.get(Database.usernameToUuid.get(facultyUsername));
                if (tempUser.getRole().equals("FACULTY")) {
                    Faculty tempFaculty = (Faculty) tempUser;
                    if (!tempFaculty.getCoursesTeaching().contains(courseId)) {
                        break;
                    } else {
                        System.out.println(Constants.COLOR_WRAPPER("The course is already being taught by the faculty!",
                                Constants.RED));
                    }
                } else {
                    System.out.println(Constants.COLOR_WRAPPER("Account is not a faculty!", Constants.RED));
                }
            } else {
                System.out.println(Constants.COLOR_WRAPPER("Account does not exist!", Constants.RED));
            }
        }
        //Database.accounts
        // TODO
    }

    public static void enrollmentManagement() {

        String  select;
        boolean exitMenu = false;

        while (!exitMenu) {
            System.out.println(" 1. Create a course schedule");
            System.out.println(" 2. Modify status of enrollment");
            System.out.println(" 3. Return");
            System.out.println();

            System.out.print("Select Number: ");
            select = Constants.IN.nextLine();
            switch (select) {
                case "1":
                    createACourseSchedule();
                    break;
                case "2":
                    modifyStatusOfEnrollment();
                    break;
                case "3":
                    exitMenu = true;
                    break;
                default:
                    System.out.println(Constants.INVALID_INPUT);
                    System.out.println();
            }
        }
    }

    public static void modifyStatusOfEnrollment() {
        System.out.println("Current enrollment status: " + Constants.COLOR_WRAPPER(Admin.getEnrollmentStatus(),
                Constants.WHITE_BOLD));
        System.out.println();

        boolean exitMenu = false;
        while (!exitMenu) {
            System.out.print("New enrollment status: (OPEN/CLOSE): ");
            String enrollment = Constants.IN.nextLine();
            switch (enrollment) {
                case "OPEN":
                    Admin.setEnrollmentStatus(enrollment);
                    System.out.println(Constants.COLOR_WRAPPER("Updated successfully!",
                            Constants.GREEN_BOLD));
                    System.out.println("Current enrollment status: " + Constants.COLOR_WRAPPER(String.valueOf(Admin.getEnrollmentStatus()),
                            Constants.WHITE_BOLD));
                    System.out.println();
                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    System.out.println();
                    exitMenu = true;
                    break;
                case "CLOSE":
                    Admin.setEnrollmentStatus(enrollment);
                    System.out.println(Constants.COLOR_WRAPPER("Updated successfully!",
                            Constants.GREEN_BOLD));
                    System.out.println("Current enrollment status: " + Constants.COLOR_WRAPPER(Admin.getEnrollmentStatus(),
                            Constants.WHITE_BOLD));
                    System.out.println();
                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    System.out.println();
                    exitMenu = true;
                    break;
                default:
                    System.out.println(Constants.INVALID_INPUT);
            }
        }
    }

    public void listAllAccounts() {
        for (Map.Entry<String, User> i : Database.accounts.entrySet()) {
            System.out.println(i.getValue());
            System.out.println();
        }
        System.out.println("Total accounts: " + Database.accounts.size());
        System.out.println();
        Constants.PRESS_ENTER_KEY_TO_CONTINUE();
        System.out.println();
    }

    public void modifyACourse() {
        System.out.print(Constants.COLOR_WRAPPER("Course id: ", Constants.WHITE_BOLD));
        String courseId = Constants.IN.nextLine();
        if (Database.courses.containsKey(courseId)) {

            System.out.println();
            System.out.println(Database.courses.get(courseId));

            boolean exitMenu = false;
            while (!exitMenu) {

                String fieldToModify;
                System.out.println();
                System.out.println(" 1. Modify COURSE NAME");
                System.out.println(" 2. Modify COURSE DESCRIPTION");
                System.out.println(" 3. Modify COURSE UNITS");
                System.out.println(" 4. Modify PREREQUISITES");
                System.out.println(" 5. Modify ANTIREQUISITES");
                System.out.println(" 6. Modify the property that if the course can be repeated");
                System.out.println(" 7. Return");
                System.out.println();
                System.out.println(" Note : Invalid value will remain the original data unchanged!");
                System.out.println();

                System.out.print(Constants.COLOR_WRAPPER("Select number: ", Constants.WHITE_BOLD));
                fieldToModify = Constants.IN.nextLine();
                switch (fieldToModify) {
                    case "1":
                        System.out.print(Constants.COLOR_WRAPPER("New COURSE NAME (must be not empty): ", Constants.WHITE_BOLD));
                        String newCourseName = Constants.IN.nextLine();
                        Database.courses.get(courseId).setCourseName(newCourseName);
                        Database.courseUpdateCommit(courseId, Constants.COURSE_FIELD.COURSE_NAME);

                        System.out.println();
                        System.out.println(Database.courses.get(courseId));
                        System.out.println();
                        Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                        break;
                    case "2":
                        System.out.print(Constants.COLOR_WRAPPER("New COURSE DESCRIPTION (must be not empty): ", Constants.WHITE_BOLD));
                        String newCourseDescription = Constants.IN.nextLine();
                        Database.courses.get(courseId).setCourseDescription(newCourseDescription);
                        Database.courseUpdateCommit(courseId, Constants.COURSE_FIELD.COURSE_DESCRIPTION);

                        System.out.println();
                        System.out.println(Database.courses.get(courseId));
                        System.out.println();
                        Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                        break;
                    case "3":
                        System.out.print(Constants.COLOR_WRAPPER("New COURSE UNITS (must > 0 and <= " + Constants.MAX_UNITS + "): ", Constants.WHITE_BOLD));
                        String newCourseUnits = Constants.IN.nextLine();
                        Database.courses.get(courseId).setCourseUnits(newCourseUnits);
                        Database.courseUpdateCommit(courseId, Constants.COURSE_FIELD.COURSE_UNITS);

                        System.out.println();
                        System.out.println(Database.courses.get(courseId));
                        System.out.println();
                        Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                        break;
                    case "4":
                        System.out.print(Constants.COLOR_WRAPPER("New PREREQUISITES (Format: A B C): ",
                                Constants.WHITE_BOLD));
                        String newPrerequisites = Constants.IN.nextLine();
                        Database.courses.get(courseId).setPrerequisites(newPrerequisites);
                        Database.courseUpdateCommit(courseId, Constants.COURSE_FIELD.PREREQUISITES);

                        System.out.println();
                        System.out.println(Database.courses.get(courseId));
                        System.out.println();
                        Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                        break;
                    case "5":
                        System.out.print(Constants.COLOR_WRAPPER("New ANTIREQUISITES (Format: A B C): ", Constants.WHITE_BOLD));
                        String newAntirequisites = Constants.IN.nextLine();
                        Database.courses.get(courseId).setAntirequisites(newAntirequisites);
                        Database.courseUpdateCommit(courseId, Constants.COURSE_FIELD.ANTIREQUISITES);

                        System.out.println();
                        System.out.println(Database.courses.get(courseId));
                        System.out.println();
                        Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                        break;
                    case "6":
                        System.out.print(Constants.COLOR_WRAPPER("Can the course be repeated for GPA? ",
                                Constants.WHITE_BOLD) + " (YES/NO) ");
                        String newCanBeRepeated = Constants.IN.nextLine();
                        Database.courses.get(courseId).setCanBeRepeated(newCanBeRepeated);
                        Database.courseUpdateCommit(courseId, Constants.COURSE_FIELD.CAN_BE_REPEATED);

                        System.out.println();
                        System.out.println(Database.courses.get(courseId));
                        System.out.println();
                        Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                        break;
                    case "7":
                        exitMenu = true;
                        break;
                    default:
                        System.out.println(Constants.INVALID_INPUT);
                        Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                }
            }
        } else {
            System.out.println(Constants.COLOR_WRAPPER("Course id does not exist!", Constants.RED));
            System.out.println();
            Constants.PRESS_ENTER_KEY_TO_CONTINUE();
            System.out.println();
        }
    }

    public void createAnAccount() {
        String createUuid = RandomUserGenerator.createRandomUuid();
        // prompt user to input the info, iterate until the input is valid.
        String createUsername;
        while (true) {
            System.out.print(Constants.COLOR_WRAPPER("USERNAME", Constants.WHITE_BOLD) + " (must be not " +
                    "empty and must be " +
                    "unique): ");
            createUsername = Constants.IN.nextLine();
            if (Validation.isUsernameValid(createUsername)) {
                break;
            }
            System.out.println(Constants.INVALID_INPUT);
        }

        String createFullName;
        while (true) {
            System.out.print(Constants.COLOR_WRAPPER("FULLNAME", Constants.WHITE_BOLD) + " (contain " +
                    "uppercases and spaces only," +
                    " at least 1 letter and the " +
                    "length >= 3): ");
            createFullName = Constants.IN.nextLine();
            if (Validation.isFullNameValid(createFullName)) {
                break;
            }
            System.out.println(Constants.INVALID_INPUT);
        }

        String createRole;
        while (true) {
            System.out.print(Constants.COLOR_WRAPPER("ROLE", Constants.WHITE_BOLD) + " (ADMIN or STUDENT " +
                    "or FACULTY): ");
            createRole = Constants.IN.nextLine();
            if (Validation.isRoleValid(createRole)) {
                break;
            }
            System.out.println(Constants.INVALID_INPUT);
        }

        String createGender;
        while (true) {
            System.out.print(Constants.COLOR_WRAPPER("GENDER", Constants.WHITE_BOLD) + " (M or F): ");
            createGender = Constants.IN.nextLine();
            if (Validation.isGenderValid(createGender)) {
                break;
            }
            System.out.println(Constants.INVALID_INPUT);
        }

        System.out.println(Constants.COLOR_WRAPPER("DATE OF BIRTH", Constants.WHITE_BOLD) + " : ");
        String createYear;
        while (true) {
            System.out.print("YEAR (>= " + Constants.MIN_YEAR + " and <= " + Constants.CURRENT_YEAR + "): ");
            createYear = Constants.IN.nextLine();
            if (Validation.isYearLegal(createYear)) {
                break;
            }
            System.out.println(Constants.INVALID_INPUT);
        }
        String createMonth;
        while (true) {
            System.out.print("MONTH : ");
            createMonth = Constants.IN.nextLine();
            if (Validation.isMonthLegal(createMonth)) {
                break;
            }
            System.out.println(Constants.INVALID_INPUT);
        }
        String createDay;
        while (true) {
            System.out.print("DAY : ");
            createDay = Constants.IN.nextLine();
            if (Validation.isDayLegal(Integer.parseInt(createYear), Integer.parseInt(createMonth),
                    createDay)) {
                break;
            }
            System.out.println(Constants.INVALID_INPUT);
        }
        DateOfBirth newDateOfBirth = new DateOfBirth(
                Integer.parseInt(createYear),
                Integer.parseInt(createMonth),
                Integer.parseInt(createDay));

        String createEmailAddress;
        while (true) {
            System.out.print(Constants.COLOR_WRAPPER("EMAIL ADDRESS", Constants.WHITE_BOLD) + " : ");
            createEmailAddress = Constants.IN.nextLine();
            if (Validation.isEmailAddressValid(createEmailAddress)) {
                break;
            }
            System.out.println(Constants.INVALID_INPUT);
        }

        String createPhoneNumber;
        while (true) {
            System.out.print(Constants.COLOR_WRAPPER("PHONE NUMBER", Constants.WHITE_BOLD) + " (Format: XXX-XXX-XXXX)" +
                    ": ");
            createPhoneNumber = Constants.IN.nextLine();
            if (Validation.isPhoneNumberValid(createPhoneNumber)) {
                break;
            }
            System.out.println(Constants.INVALID_INPUT);
        }

        String createAddress;
        while (true) {
            System.out.print(Constants.COLOR_WRAPPER("ADDRESS", Constants.WHITE_BOLD) + " (must not be " +
                    "empty): ");
            createAddress = Constants.IN.nextLine();
            if (Validation.isAddressValid(createAddress)) {
                break;
            }
            System.out.println(Constants.INVALID_INPUT);
        }

        String createPassword;
        while (true) {
            createPassword =
                    MenuTextUI.enterPassword(Constants.COLOR_WRAPPER("PASSWORD", Constants.WHITE_BOLD) + " (the" +
                            " length must be >= " + Constants.MIN_PASSWORD_LENGTH + "): ");
            if (Validation.isPasswordValid(createPassword)) {
                break;
            }
            System.out.println(Constants.INVALID_INPUT);
        }

        // create the User object
        User createUser;
        switch (createRole) {
            case "ADMIN":
                createUser = new Admin();
                break;
            case "FACULTY":
                createUser = new Faculty();
                break;
            default:
                createUser = new Student();
                break;
        }

        createUser.setUuid(createUuid);
        createUser.setRole(createRole);
        createUser.setUsername(createUsername);
        createUser.setFullName(createFullName);
        createUser.setGender(createGender);
        createUser.setDateOfBirth(newDateOfBirth);
        createUser.setEmailAddress(createEmailAddress);
        createUser.setPhoneNumber(createPhoneNumber);
        createUser.setAddress(createAddress);
        createUser.setPassword(createPassword);

        Database.accounts.put(createUuid, createUser);
        Database.createAccountCommit(createUser);

        System.out.println();
        System.out.println(Database.accounts.get(createUuid));
        System.out.println();
        System.out.println(Constants.COLOR_WRAPPER("New account successfully created!", Constants.GREEN_BOLD));
        System.out.println();
        Constants.PRESS_ENTER_KEY_TO_CONTINUE();
        System.out.println();
    }

    public void createACourse() {
// prompt user to input the info, iterate until the input is valid.
        String createCourseId;
        while (true) {
            System.out.print(Constants.COLOR_WRAPPER("COURSE ID", Constants.WHITE_BOLD) + " (must be not " +
                    "empty and unique): ");
            createCourseId = Constants.IN.nextLine();
            if (Validation.isCourseIdValid(createCourseId)) {
                break;
            }
            System.out.println(Constants.INVALID_INPUT);
        }
        String createCourseName;
        while (true) {
            System.out.print(Constants.COLOR_WRAPPER("COURSE NAME", Constants.WHITE_BOLD) + " (must be " +
                    "not empty): ");
            createCourseName = Constants.IN.nextLine();
            if (Validation.isCourseNameValid(createCourseName)) {
                break;
            }
            System.out.println(Constants.INVALID_INPUT);
        }
        String createCourseDescription;
        while (true) {
            System.out.print(Constants.COLOR_WRAPPER(
                    "COURSE DESCRIPTION", Constants.WHITE_BOLD) + " (must be not empty): ");
            createCourseDescription = Constants.IN.nextLine();
            if (Validation.isCourseDescriptionValid(createCourseDescription)) {
                break;
            }
            System.out.println(Constants.INVALID_INPUT);
        }
        String createCourseUnits;
        while (true) {
            System.out.print(Constants
                    .COLOR_WRAPPER("COURSE UNITS", Constants.WHITE_BOLD) + " (must > 0.0 and <= " + Constants.MAX_UNITS + "): ");
            createCourseUnits = Constants.IN.nextLine();
            if (Validation.isCourseUnitsValid(createCourseUnits)) {
                break;
            }
            System.out.println(Constants.INVALID_INPUT);
        }
        String createPrerequisites;
        while (true) {
            System.out.print(Constants
                    .COLOR_WRAPPER("COURSE PREREQUISITES  (Format: A B C): ", Constants.WHITE_BOLD));
            createPrerequisites = Constants.IN.nextLine();
            if (Validation.isPrerequisitesValid(createPrerequisites)) {
                break;
            }
            System.out.println(Constants.INVALID_INPUT);
        }
        String createAntirequisites;
        while (true) {
            System.out.print(Constants
                    .COLOR_WRAPPER("COURSE ANTIREQUISITES  (Format: A B C): ", Constants.WHITE_BOLD));
            createAntirequisites = Constants.IN.nextLine();
            if (Validation.isPrerequisitesValid(createAntirequisites)) {
                break;
            }
            System.out.println(Constants.INVALID_INPUT);
        }
        String canBeRepeatedForGPA;
        while (true) {
            System.out.print("Can be repeated for GPA? (YES/NO) ");
            canBeRepeatedForGPA = Constants.IN.nextLine();
            if (Validation.isCanBeRepeatedValid(canBeRepeatedForGPA)) {
                break;
            }
            System.out.println(Constants.INVALID_INPUT);
        }

        Course createCourse = new Course();
        createCourse.setCourseId(createCourseId);
        createCourse.setCourseName(createCourseName);
        createCourse.setCourseDescription(createCourseDescription);
        createCourse.setCourseUnits(createCourseUnits);
        createCourse.setPrerequisites(createPrerequisites);
        createCourse.setAntirequisites(createAntirequisites);
        createCourse.setCanBeRepeated(canBeRepeatedForGPA);
        Database.courses.put(createCourseId, createCourse);
        Database.createCourseCommit(createCourse);

        // ask the admin it is mandatory or optional for academic requirements
        String extraInformation = " + It is ";
        String mandatoryOrOptional;
        while (true) {
            System.out.print("Is the course mandatory or optional for academic requirements? " +
                    "(MANDATORY/OPTIONAL) ");
            mandatoryOrOptional = Constants.IN.nextLine();
            if (mandatoryOrOptional.equals("MANDATORY")) {
                Database.mandatoryCourses.add(createCourseId);
                Database.courseUpdateCommitToRequirements(createCourseId, true, true);
                extraInformation += "MANDATORY for academic requirements.";
                break;

            } else if (mandatoryOrOptional.equals("OPTIONAL")) {
                Database.optionalCourses.add(createCourseId);
                Database.courseUpdateCommitToRequirements(createCourseId, true, false);
                extraInformation += "OPTIONAL for academic requirements.";
                break;
            }
        }
        extraInformation += "\n";
        extraInformation += " + It is ";
        // ask the admin it is mandatory or optional for internship requirements
        while (true) {
            System.out.print("Is the course mandatory or optional for internship requirements? (MANDATORY/OPTIONAL) ");
            mandatoryOrOptional = Constants.IN.nextLine();
            if (mandatoryOrOptional.equals("MANDATORY")) {
                Database.mandatoryCoursesForInternship.add(createCourseId);
                Database.courseUpdateCommitToRequirements(createCourseId, false, true);
                extraInformation += "MANDATORY for internship requirements.";
                break;
            } else if (mandatoryOrOptional.equals("OPTIONAL")) {
                Database.optionalCourses.add(createCourseId);
                Database.courseUpdateCommitToRequirements(createCourseId, false, false);
                extraInformation += "OPTIONAL for internship requirements.\n";
                break;
            }
        }

        System.out.println();
        System.out.println(Database.courses.get(createCourseId));
        System.out.println();
        System.out.println(extraInformation);
        System.out.println(Constants.COLOR_WRAPPER("New course successfully created!", Constants.GREEN_BOLD));
        System.out.println();
        Constants.PRESS_ENTER_KEY_TO_CONTINUE();
        System.out.println();
    }

    public void removeAnAccount(String adminUsername) {

        System.out.print(Constants.COLOR_WRAPPER("Account username: ", Constants.WHITE_BOLD));
        String username = Constants.IN.nextLine();

        if (username.equals(adminUsername)) {

            System.out.println(Constants.COLOR_WRAPPER("Cannot delete the user itself!", Constants.RED));
            Constants.PRESS_ENTER_KEY_TO_CONTINUE();

        } else if (Database.usernameToUuid.containsKey(username)) {

            String uuid;
            uuid = Database.usernameToUuid.get(username);

            System.out.println();
            System.out.println(Database.accounts.get(uuid));
            System.out.println();

            Database.accountRemovalCommit(uuid);
            Database.accounts.remove(uuid);
            Database.usernameToUuid.remove(username);

            System.out.println(Constants
                    .COLOR_WRAPPER("Account successfully removed!", Constants.GREEN_BOLD));
        } else {
            System.out.println(Constants.COLOR_WRAPPER("Account does not exist!", Constants.RED));
            Constants.PRESS_ENTER_KEY_TO_CONTINUE();
        }
        System.out.println();
    }

    public void removeACourse() {

        // the admin can only remove a course which is not taken by student or taught by any faculty
        // the admin cannot delete a course id which does not exist

        System.out.print("Course id: ");
        String courseId = Constants.IN.nextLine();

        if (Database.coursesCurrentlyTaught.contains(courseId)) {

            System.out.println(Constants.COLOR_WRAPPER("Cannot remove a course which is currently being taught!",
                    Constants.RED));
        } else if (Database.courses.containsKey(courseId)) {

            System.out.println();
            System.out.println(Database.courses.get(courseId));

            Database.courses.remove(courseId);
            Database.courseRemovalCommit(courseId);
            if (Database.mandatoryCourses.contains(courseId)) {
                Database.mandatoryCourses.remove(courseId);
                Database.courseRemovalCommitToRequirements(courseId, true, true);
            } else {
                Database.optionalCourses.remove(courseId);
                Database.courseRemovalCommitToRequirements(courseId, true, false);
            }
            if (Database.mandatoryCoursesForInternship.contains(courseId)) {
                Database.mandatoryCoursesForInternship.remove(courseId);
                Database.courseRemovalCommitToRequirements(courseId, false, true);
            } else {
                Database.optionalCoursesForInternship.remove(courseId);
                Database.courseRemovalCommitToRequirements(courseId, false, false);
            }
            System.out.println();
            System.out.println(Constants.COLOR_WRAPPER("Course successfully removed!", Constants.GREEN_BOLD));

        } else {
            System.out.println(Constants.COLOR_WRAPPER("Course id does not exist!", Constants.RED));
            Constants.PRESS_ENTER_KEY_TO_CONTINUE();
        }

        System.out.println();
    }

    public void checkHistoryLog() {
        // TODO
    }
}

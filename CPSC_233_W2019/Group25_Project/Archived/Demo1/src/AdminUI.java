import java.util.Map;

/**
 * The part of textUI for Administrators. The GUI design will refer its logic.
 */

public class AdminUI {

    public AdminUI(String uuid) {

        Admin user = (Admin) Database.accounts.get(uuid);

        String  select;
        String  courseId;
        String  uuidInput;
        String  username;
        boolean exitSubMenu;

        while (true) {

            System.out.println(Constants.COLOR_WRAPPER("ADMINISTRATOR PANEL", Constants.WHITE_BOLD));
            System.out.println();

            System.out.println(" 1. List all available accounts");
            System.out.println(" 2. List all available courses");
            System.out.println(" 3. Search a course by the course id");
            System.out.println(" 4. Search an account");
            System.out.println(" 5. Modify a course");
            System.out.println(" 6. Modify an account");
            System.out.println(" 7. Create an account");
            System.out.println(" 8. Create a course");
            System.out.println(" 9. Remove an account");
            System.out.println("10. Remove a course");
            System.out.println("11. Personal information");
            System.out.println("12. Logout");

            System.out.println();
            System.out.print(Constants.COLOR_WRAPPER("Select number: ", Constants.WHITE_BOLD));
            select = Constants.IN.nextLine();
            System.out.println();
            switch (select) {

                // List all available accounts
                case "1":
                    for (Map.Entry<String, User> i : Database.accounts.entrySet()) {
                        System.out.println(i.getValue());
                        System.out.println();
                    }
                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    System.out.println();
                    break;

                // List all available courses
                case "2":

                    for (Map.Entry<String, Course> i : Database.courses.entrySet()) {
                        System.out.println(i.getValue());
                        System.out.println();
                    }
                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    System.out.println();
                    break;

                // Search a course by the course id
                case "3":

                    System.out.print("Course id: ");
                    courseId = Constants.IN.nextLine();
                    if (Database.courses.containsKey(courseId)) {
                        System.out.println();
                        System.out.println(Database.courses.get(courseId));
                        System.out.println();
                    } else {
                        System.out.println(Constants.COLOR_WRAPPER("Course id does not exist!", Constants.RED));
                        System.out.println();
                    }
                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    System.out.println();
                    break;

                // Search an account by uuid or username
                case "4":

                    String subSelect;
                    boolean exitSubSubMenu = false;
                    while (!exitSubSubMenu) {
                        System.out.println(" 1. Search by the uuid");
                        System.out.println(" 2. Search by the username");
                        System.out.println();

                        System.out.print(Constants.COLOR_WRAPPER("Select number: ", Constants.WHITE_BOLD));
                        subSelect = Constants.IN.nextLine();
                        System.out.println();
                        switch (subSelect) {
                            case "1":
                                System.out.print(Constants.COLOR_WRAPPER("Account uuid: ", Constants.WHITE_BOLD));
                                uuidInput = Constants.IN.nextLine();
                                if (Database.accounts.containsKey(uuidInput)) {
                                    System.out.println();
                                    System.out.println(Database.accounts.get(uuidInput));
                                    System.out.println();
                                } else {
                                    System.out.println(Constants.COLOR_WRAPPER("Account does not exist!", Constants.RED));
                                    System.out.println();
                                }
                                Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                                System.out.println();
                                exitSubSubMenu = true;
                                break;
                            case "2":
                                System.out.print(Constants.COLOR_WRAPPER("Account username: ", Constants.WHITE_BOLD));
                                username = Constants.IN.nextLine();

                                if (Database.usernameToUuid.containsKey(username)) {
                                    System.out.println();
                                    System.out.println(Database.accounts.get(Database.usernameToUuid.get(username)));
                                    System.out.println();
                                } else {
                                    System.out.println(Constants.COLOR_WRAPPER("Account does not exist!", Constants.RED));
                                    System.out.println();
                                }
                                Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                                System.out.println();
                                exitSubSubMenu = true;
                                break;
                            default:
                                System.out.println(Constants.INVALID_INPUT);
                                System.out.println();
                        }
                    }
                    break;

                // Modify a course
                case "5":

                    System.out.print(Constants.COLOR_WRAPPER("Course id: ", Constants.WHITE_BOLD));
                    courseId = Constants.IN.nextLine();
                    if (Database.courses.containsKey(courseId)) {

                        System.out.println();
                        System.out.println(Database.courses.get(courseId));

                        exitSubMenu = false;
                        while (!exitSubMenu) {

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
                            // If the enter is empty or illegal then remain unchanged, which is defined in Validation class.
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
                                    System.out.print(Constants.COLOR_WRAPPER("New PREREQUISITES (Format: [A B] C): ", Constants.WHITE_BOLD));
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
                                    exitSubMenu = true;
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
                    break;

                // Modify an account
                case "6":

                    System.out.print(Constants.COLOR_WRAPPER("Account uuid: ", Constants.WHITE_BOLD));
                    uuidInput = Constants.IN.nextLine();
                    if (Database.accounts.containsKey(uuidInput)) {

                        System.out.println();
                        System.out.println(Database.accounts.get(uuidInput));
                        System.out.println();

                        exitSubMenu = false;
                        while (!exitSubMenu) {

                            String fieldToModify;

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
                            // If the enter is empty or illegal then remain unchanged, which is defined in Validation class.
                            fieldToModify = Constants.IN.nextLine();
                            switch (fieldToModify) {
                                case "1":
                                    System.out.print(Constants.COLOR_WRAPPER("New USERNAME", Constants.WHITE_BOLD) +
                                            " (must be not empty" +
                                            " and must be unique): ");
                                    String newUsername = Constants.IN.nextLine();

                                    Database.accounts.get(uuidInput).setUsername(newUsername);
                                    System.out.println();
                                    System.out.println(Database.accounts.get(uuidInput));
                                    System.out.println();
                                    Database.accountUpdateCommit(uuidInput, Constants.USER_FIELD.USERNAME);
                                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                                    System.out.println();
                                    break;
                                case "2":
                                    System.out.println(Constants.COLOR_WRAPPER("New FULLNAME", Constants.WHITE_BOLD) +
                                            " (contain " +
                                            "uppercases and spaces only, at least 1 " +
                                            "letter and the length >= 3):");
                                    String newFullName = Constants.IN.nextLine();

                                    Database.accounts.get(uuidInput).setFullName(newFullName);
                                    System.out.println();
                                    System.out.println(Database.accounts.get(uuidInput));
                                    System.out.println();
                                    Database.accountUpdateCommit(uuidInput, Constants.USER_FIELD.FULLNAME);
                                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                                    System.out.println();
                                    break;
                                case "3":
                                    System.out.print(Constants.COLOR_WRAPPER("New ROLE", Constants.WHITE_BOLD) + " " +
                                            "(ADMIN or STUDENT or " +
                                            "FACULTY): ");
                                    String newRole = Constants.IN.nextLine();

                                    Database.accounts.get(uuidInput).setRole(newRole);
                                    System.out.println();
                                    System.out.println(Database.accounts.get(uuidInput));
                                    System.out.println();
                                    Database.accountUpdateCommit(uuidInput, Constants.USER_FIELD.ROLE);
                                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                                    System.out.println();
                                    break;
                                case "4":
                                    System.out.print(Constants.COLOR_WRAPPER("New GENDER", Constants.WHITE_BOLD) + " " +
                                            "(M or F): ");
                                    String newGender = Constants.IN.nextLine();

                                    Database.accounts.get(uuidInput).setGender(newGender);
                                    System.out.println();
                                    System.out.println(Database.accounts.get(uuidInput));
                                    System.out.println();
                                    Database.accountUpdateCommit(uuidInput, Constants.USER_FIELD.GENDER);
                                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                                    System.out.println();
                                    break;
                                case "5":
                                    System.out.print(Constants.COLOR_WRAPPER("New ADDRESS", Constants.WHITE_BOLD) +
                                            " (must be not empty):" +
                                            " ");
                                    String newAddress = Constants.IN.nextLine();

                                    Database.accounts.get(uuidInput).setAddress(newAddress);
                                    System.out.println();
                                    System.out.println(Database.accounts.get(uuidInput));
                                    System.out.println();
                                    Database.accountUpdateCommit(uuidInput, Constants.USER_FIELD.ADDRESS);
                                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                                    System.out.println();
                                    break;
                                case "6":
                                    System.out.print(Constants.COLOR_WRAPPER("New PHONE NUMBER",
                                            Constants.WHITE_BOLD) + " (Format: " +
                                            "XXX-XXX-XXXX): ");
                                    String newPhoneNumber = Constants.IN.nextLine();

                                    Database.accounts.get(uuidInput).setPhoneNumber(newPhoneNumber);
                                    System.out.println();
                                    System.out.println(Database.accounts.get(uuidInput));
                                    System.out.println();
                                    Database.accountUpdateCommit(uuidInput, Constants.USER_FIELD.PHONE);
                                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                                    System.out.println();
                                    break;
                                case "7":
                                    System.out.println(Constants.COLOR_WRAPPER("New DATE OF BIRTH", Constants.WHITE_BOLD) + " : ");
                                    String newYear;
                                    while (true) {
                                        System.out.print("YEAR (>= " + Constants.MIN_YEAR + " and <= " + Constants.CURRENT_YEAR + "): ");
                                        newYear = Constants.IN.nextLine();
                                        if (Validation.isYearLegal(newYear)) {
                                            break;
                                        }
                                        System.out.println(Constants.INVALID_INPUT);
                                    }
                                    String newMonth;
                                    while (true) {
                                        System.out.print("MONTH : ");
                                        newMonth = Constants.IN.nextLine();
                                        if (Validation.isMonthLegal(newMonth)) {
                                            break;
                                        }
                                        System.out.println(Constants.INVALID_INPUT);
                                    }
                                    String createDay;
                                    while (true) {
                                        System.out.print("DAY : ");
                                        createDay = Constants.IN.nextLine();
                                        if (Validation.isDayLegal(Integer.parseInt(newYear), Integer.parseInt(newMonth),
                                                createDay)) {
                                            break;
                                        }
                                        System.out.println(Constants.INVALID_INPUT);
                                    }

                                    Database.accounts.get(uuidInput).setDateOfBirth(new DateOfBirth(
                                            Integer.parseInt(newYear),
                                            Integer.parseInt(newMonth),
                                            Integer.parseInt(createDay)));

                                    Database.accountUpdateCommit(uuidInput, Constants.USER_FIELD.DATE_OF_BIRTH);
                                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                                    System.out.println();
                                    break;
                                case "8":
                                    System.out.print(Constants.COLOR_WRAPPER("New EMAIL ADDRESS",
                                            Constants.WHITE_BOLD) + " : ");
                                    String newEmailAddress = Constants.IN.nextLine();

                                    Database.accounts.get(uuidInput).setEmailAddress(newEmailAddress);
                                    System.out.println();
                                    System.out.println(Database.accounts.get(uuidInput));
                                    System.out.println();
                                    Database.accountUpdateCommit(uuidInput, Constants.USER_FIELD.EMAIL);
                                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                                    System.out.println();
                                    break;
                                case "9":
                                    String newPassword =
                                            Menu.enterPassword(Constants.COLOR_WRAPPER("New PASSWORD",
                                                    Constants.WHITE_BOLD) + " (the " +
                                                    "length must be >= " + Constants.MIN_PASSWORD_LENGTH + "): ");

                                    Database.accounts.get(uuidInput).setPassword(newPassword);
                                    System.out.println();
                                    Database.accountUpdateCommit(uuidInput, Constants.USER_FIELD.PASSWORD);
                                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                                    System.out.println();
                                    break;
                                case "10":
                                    exitSubMenu = true;
                                    break;
                                default:
                                    System.out.println(Constants.INVALID_INPUT);
                                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                                    System.out.println();
                            }
                        }
                    } else {
                        System.out.println(Constants.COLOR_WRAPPER("Account does not exist!", Constants.RED));
                        Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                        System.out.println();
                    }
                    break;

                // Create an account
                case "7":

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
                                Menu.enterPassword(Constants.COLOR_WRAPPER("PASSWORD", Constants.WHITE_BOLD) + " (the" +
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
                    break;

                // Create a course
                case "8":

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
                                .COLOR_WRAPPER("COURSE UNITS", Constants.WHITE_BOLD) + " (must > 0 and <= " + Constants.MAX_UNITS + "): ");
                        createCourseUnits = Constants.IN.nextLine();
                        if (Validation.isCourseUnitsValid(createCourseUnits)) {
                            break;
                        }
                        System.out.println(Constants.INVALID_INPUT);
                    }
                    // TODO
                    // String createPrerequisites
                    // String createAntirequisites
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

                    // TODO Temporary empty value
                    createCourse.setPrerequisites("");
                    createCourse.setAntirequisites("");
                    createCourse.setCanBeRepeated(canBeRepeatedForGPA);

                    Database.courses.put(createCourseId, createCourse);

                    // TODO All courses considered to be optional at this moment.
                    Database.optionalCourses.add(createCourseId);

                    Database.createCourseCommit(createCourse);
                    System.out.println();
                    System.out.println(Database.courses.get(createCourseId));
                    System.out.println();
                    System.out.println(Constants.COLOR_WRAPPER("New course successfully created!", Constants.GREEN_BOLD));
                    System.out.println();
                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    System.out.println();
                    break;

                // Remove an account
                case "9":

                    System.out.print("Account uuid: ");
                    uuidInput = Constants.IN.nextLine();
                    // cannot delete itself
                    if (uuidInput.equals(user.getUuid())) {
                        System.out.println(Constants.COLOR_WRAPPER("Cannot delete the user itself!", Constants.RED));
                        Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    } else if (Database.accounts.containsKey(uuidInput)) {
                        System.out.println();
                        System.out.println(Database.accounts.get(uuidInput));
                        System.out.println();
                        Database.accountRemovalCommit(uuidInput);

                        // the pair in the Database.accounts must delete after line669
                        // because Database.accountRemovalCommit() has to access the role of the uuid

                        Database.accounts.remove(uuidInput);
                        System.out.println(Constants
                                .COLOR_WRAPPER("Account successfully deleted!", Constants.GREEN_BOLD));
                    } else {
                        System.out.println(Constants.COLOR_WRAPPER("Account does not exist!", Constants.RED));
                        Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    }
                    System.out.println();

                    break;

                // Remove a course
                case "10":

                    System.out.print("Course id: ");
                    courseId = Constants.IN.nextLine();
                    if (Database.courses.containsKey(courseId)) {
                        System.out.println();
                        System.out.println(Database.courses.get(courseId));
                        Database.courses.remove(courseId);
                        Database.courseRemovalCommit(courseId);
                        System.out.println(Constants.COLOR_WRAPPER("Course successfully deleted!", Constants.GREEN_BOLD));
                    } else {
                        System.out.println(Constants.COLOR_WRAPPER("Course id does not exist!", Constants.RED));
                        Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    }
                    System.out.println();
                    break;

                // Check personal information
                case "11":

                    System.out.println(Constants.COLOR_WRAPPER("Personal Information : ", Constants.WHITE_BOLD));
                    System.out.println();
                    System.out.println(user);
                    System.out.println();
                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    System.out.println();
                    break;

                // Return to the main menu
                case "12":
                    return;

                default:
                    System.out.println(Constants.INVALID_INPUT);
                    System.out.println();
            }
        }
    }
}

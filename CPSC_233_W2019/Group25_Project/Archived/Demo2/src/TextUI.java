import java.io.Console;

/**
 * Define all common methods used in TextUI for three kinds of users.
 */

public class TextUI {

    public TextUI() {
    }

    public TextUI(String uuid) {

        Database.initializeAccounts();
        Database.initializeCourses();
        Database.initializeCoursesCurrentlyTaught();

        Log.userLoginSuccessfully(uuid, Database.accounts.get(uuid).getUsername());
        System.out.println(Constants.COLOR_WRAPPER("Successfully logined!", Constants.GREEN_BOLD));
        System.out.println();
        System.out.println("Good day, " + (Database.accounts.get(uuid).getGender().equals(Constants.GENDER[0]) ? "Mr." : "Ms.") + Database.accounts.get(uuid).getFullName());
        System.out.println("Current login time is : " + getCurrentTime());

        System.out.println();
    }

    /**
     * Get the current time of the time zone 'America/Edmonton'
     *
     * @return
     */
    public static String getCurrentTime() {
        Date currentDate = Calendar.getInstance(Constants.TIME_ZONE).getTime();
        return currentDate.toString();
    }

    /**
     * Clean the terminal screen in *nix system.
     */
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * A function make the user's password invisible during the input.
     *
     * @param prompt
     * @return
     */
    public static String enterPassword(String prompt) {

        String password = "";
        if (Constants.TEST_IN_IDE) {
            System.out.print(prompt);
            password = Constants.IN.nextLine();
        } else {
            try {
                Console console = System.console();
                if (console == null) {
                    throw new Exception();
                }
                password = String.valueOf(console.readPassword(prompt));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return password;
    }

    public static String colorHighlightedSubstring(String s, String keyword) {

        HashSet<Integer> hightlightPostion = new HashSet<>();
        int              index             = 0;
        do {
            index = s.indexOf(keyword, index);
            if (index == -1) {
                break;
            }
            for (int i = index; i < index + keyword.length(); ++i) {
                hightlightPostion.add(i);
            }
            index += 2;
        } while (true);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); ++i) {
            if (hightlightPostion.contains(i)) {
                sb.append(Constants.COLOR_WRAPPER(s.charAt(i), Constants.RED_BOLD));
            } else {
                sb.append(s.charAt(i));
            }
        }
        return sb.toString();
    }

    public static ArrayList<Course> searchCourseIdByKeywordAndSort(String keyword) {

        ArrayList<Course> result = new ArrayList<>();
        if (!keyword.isEmpty()) {
            // search
            for (Map.Entry<String, Course> i : Database.courses.entrySet()) {
                if (i.getKey().contains(keyword)) {
                    result.add(i.getValue());
                }
            }

            // sort
            Collections.sort(result);
        }
        return result;
    }

    public static ArrayList<Course> searchCourseNameByKeywordAndSort(String keyword) {

        ArrayList<Course> result = new ArrayList<>();
        if (!keyword.isEmpty()) {
            // search
            for (Map.Entry<String, Course> i : Database.courses.entrySet()) {
                if (i.getValue().getCourseName().contains(keyword)) {
                    result.add(i.getValue());
                }
            }

            // sort
            Collections.sort(result);
        }
        return result;
    }

    public static ArrayList<User> searchSpecificRolesAndSort(String role) {

        ArrayList<User> result = new ArrayList<>();

        if (Validation.isRoleValid(role)) {

            // search
            for (Map.Entry<String, User> i : Database.accounts.entrySet()) {
                if (i.getValue().getRole().equals(role)) {
                    result.add(i.getValue());
                }
            }

            // sort
            Collections.sort(result);
        }
        return result;
    }

    public static ArrayList<User> searchSpecificGenderAndSort(String gender) {

        ArrayList<User> result = new ArrayList<>();

        if (Validation.isGenderValid(gender)) {
            // search
            for (Map.Entry<String, User> i : Database.accounts.entrySet()) {
                if (i.getValue().getGender().equals(gender)) {
                    result.add(i.getValue());
                }
            }

            // sort
            Collections.sort(result);
        }
        return result;
    }

    public static ArrayList<User> searchUsernameByKeywordAndSort(String keyword) {

        ArrayList<User> result = new ArrayList<>();
        if (!keyword.isEmpty()) {
            // search
            for (Map.Entry<String, User> i : Database.accounts.entrySet()) {
                if (i.getValue().getUsername().contains(keyword)) {
                    result.add(i.getValue());
                }
            }
            // sort
            Collections.sort(result);
        }
        return result;
    }

    public static void searchAccounts() {

        boolean exitSubMenu = false;
        while (!exitSubMenu) {

            System.out.println(" 1. Find by username");
            System.out.println(" 2. Find by role");
            System.out.println(" 3. Find by gender");
            System.out.println(" 4. Return");
            System.out.println();
            System.out.print(Constants.COLOR_WRAPPER("Select Number: ", Constants.WHITE_BOLD));
            String          select = Constants.IN.nextLine();
            ArrayList<User> result;
            String          keyword;

            switch (select) {
                case "1":
                    System.out.print(Constants.COLOR_WRAPPER("Find username: ", Constants.WHITE_BOLD));
                    keyword = Constants.IN.nextLine();
                    result = searchUsernameByKeywordAndSort(keyword);

                    // output
                    if (!result.isEmpty()) {
                        for (User i : result) {
                            System.out.println();
                            System.out.println(i.toString(Constants.USER_FIELD.USERNAME, keyword));
                        }
                        // stats information
                        System.out.println();
                        System.out.println("Matches: " + result.size());
                        System.out.println();
                    } else {
                        System.out.println(Constants.COLOR_WRAPPER("Account does not exist!", Constants.RED));
                    }
                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    System.out.println();
                    break;
                case "2":
                    System.out.print(Constants.COLOR_WRAPPER("Find role: ", Constants.WHITE_BOLD) + "(ADMIN/FACULTY" +
                            "/STUDENT) ");
                    keyword = Constants.IN.nextLine();
                    result = searchSpecificRolesAndSort(keyword);

                    // output
                    if (!result.isEmpty()) {
                        for (User i : result) {
                            System.out.println();
                            System.out.println(i.toString(Constants.USER_FIELD.ROLE, keyword));
                        }
                        // stats information
                        System.out.println();
                        System.out.println("Matches: " + result.size());
                        System.out.println();
                    } else {
                        System.out.println(Constants.COLOR_WRAPPER("Account does not exist!", Constants.RED));
                    }
                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    System.out.println();
                    break;
                case "3":
                    System.out.print(Constants.COLOR_WRAPPER("Find gender: ", Constants.WHITE_BOLD) + "(M/F) ");
                    keyword = Constants.IN.nextLine();
                    result = searchSpecificGenderAndSort(keyword);

                    // output
                    if (!result.isEmpty()) {
                        for (User i : result) {
                            System.out.println();
                            System.out.println(i.toString(Constants.USER_FIELD.GENDER, keyword));
                        }
                        // stats information
                        System.out.println();
                        System.out.println("Matches: " + result.size());
                        System.out.println();
                    } else {
                        System.out.println(Constants.COLOR_WRAPPER("Account does not exist!", Constants.RED));
                    }
                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    System.out.println();
                    break;
                case "4":
                    exitSubMenu = true;
                    break;
                default:
                    System.out.println(Constants.INVALID_INPUT);
                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    System.out.println();
            }
        }
    }

    // search a course id with keyword provided, show all satisfied results with keyword highlighted
    public static void searchCourses() {

        boolean exitSubMenu = false;
        while (!exitSubMenu) {

            System.out.println(" 1. Find by course id");
            System.out.println(" 2. Find by course name");
            System.out.println(" 3. Return");
            System.out.println();
            System.out.print(Constants.COLOR_WRAPPER("Select Number: ", Constants.WHITE_BOLD));
            String            select = Constants.IN.nextLine();
            String            keyword;
            ArrayList<Course> result;

            switch (select) {
                case "1":
                    System.out.print(Constants.COLOR_WRAPPER("Course id: ", Constants.WHITE_BOLD));
                    keyword = Constants.IN.nextLine();
                    result = searchCourseIdByKeywordAndSort(keyword);

                    // output
                    if (!result.isEmpty()) {
                        for (Course i : result) {
                            System.out.println();
                            System.out.println(i.toString(Constants.COURSE_FIELD.COURSE_ID, keyword));
                        }
                        // stats information
                        System.out.println();
                        System.out.println("Matches: " + result.size());
                        System.out.println();
                    } else {
                        System.out.println(Constants.COLOR_WRAPPER("Course id does not exist!", Constants.RED));
                    }
                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    System.out.println();
                    break;
                case "2":
                    System.out.print(Constants.COLOR_WRAPPER("Course name: ", Constants.WHITE_BOLD));
                    keyword = Constants.IN.nextLine();
                    result = searchCourseNameByKeywordAndSort(keyword);

                    // output
                    if (!result.isEmpty()) {
                        for (Course i : result) {
                            System.out.println();
                            System.out.println(i.toString(Constants.COURSE_FIELD.COURSE_NAME, keyword));
                        }
                        // stats information
                        System.out.println();
                        System.out.println("Matches: " + result.size());
                        System.out.println();
                    } else {
                        System.out.println(Constants.COLOR_WRAPPER("Course name does not exist!", Constants.RED));
                    }
                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    System.out.println();
                    break;
                case "3":
                    exitSubMenu = true;
                    break;
                default:
                    System.out.println(Constants.INVALID_INPUT);
                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    System.out.println();
            }
        }
    }

    public static void listAllCourses() {
        if (Database.courses != null) {
            for (Course i : Database.courses.values()) {
                System.out.println(i);
                System.out.println();
            }
            System.out.println("Total courses: " + Database.courses.size());
            System.out.println();
            Constants.PRESS_ENTER_KEY_TO_CONTINUE();
            System.out.println();
        }
    }

    public static void showAccountInformation(User user) {

        System.out.println(Constants.COLOR_WRAPPER("Account Information : ", Constants.WHITE_BOLD));
        System.out.println();
        System.out.println(user);
        System.out.println();
        Constants.PRESS_ENTER_KEY_TO_CONTINUE();
        System.out.println();

    }

    public static void modifyUsername(User user) {

        System.out.print(Constants.COLOR_WRAPPER("New USERNAME", Constants.WHITE_BOLD) +
                " (must be not empty" +
                " and must be unique): ");
        String newUsername = Constants.IN.nextLine();

        // update Database.usernameToUuid and Database.account
        Database.usernameToUuid.remove(user.getUsername());
        user.setUsername(newUsername);
        Database.usernameToUuid.put(user.getUsername(), user.getUuid());

        // commit to the database file
        Database.accountUpdateCommit(user.getUuid(), Constants.USER_FIELD.USERNAME);

    }

    public static void modifyFullName(User user) {

        System.out.print(Constants.COLOR_WRAPPER("New FULLNAME", Constants.WHITE_BOLD) +
                " (contain " +
                "uppercases and spaces only, at least 1 " +
                "letter and the length >= 3): ");
        String newFullName = Constants.IN.nextLine();

        // update Database.account
        user.setFullName(newFullName);

        // commit to the database file
        Database.accountUpdateCommit(user.getUuid(), Constants.USER_FIELD.FULLNAME);
    }

    public static void modifyRole(User user) {

        System.out.print(Constants.COLOR_WRAPPER("New ROLE", Constants.WHITE_BOLD) + " " +
                "(ADMIN or STUDENT or " +
                "FACULTY): ");
        String newRole = Constants.IN.nextLine();

        // update Database.account
        user.setRole(newRole);

        // commit to the database file
        Database.accountUpdateCommit(user.getUuid(), Constants.USER_FIELD.ROLE);

    }

    public static void modifyGender(User user) {

        System.out.print(Constants.COLOR_WRAPPER("New GENDER", Constants.WHITE_BOLD) + " " +
                "(M or F): ");
        String newGender = Constants.IN.nextLine();

        // update Database.account
        user.setGender(newGender);

        // commit to the database file
        Database.accountUpdateCommit(user.getUuid(), Constants.USER_FIELD.GENDER);

    }

    public static void modifyAddress(User user) {

        System.out.print(Constants.COLOR_WRAPPER("New ADDRESS", Constants.WHITE_BOLD) +
                " (must be not empty):" +
                " ");
        String newAddress = Constants.IN.nextLine();

        // update Database.account
        user.setAddress(newAddress);

        // commit to the database file
        Database.accountUpdateCommit(user.getUuid(), Constants.USER_FIELD.ADDRESS);
    }

    public static void modifyPhoneNumber(User user) {

        System.out.print(Constants.COLOR_WRAPPER("New PHONE NUMBER",
                Constants.WHITE_BOLD) + " (Format: " +
                "XXX-XXX-XXXX): ");
        String newPhoneNumber = Constants.IN.nextLine();

        // update Database.account
        user.setPhoneNumber(newPhoneNumber);

        // commit to the database file
        Database.accountUpdateCommit(user.getUuid(), Constants.USER_FIELD.PHONE);
    }

    public static void modifyDateOfBirth(User user) {
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

        String newDay;
        while (true) {
            System.out.print("DAY : ");
            newDay = Constants.IN.nextLine();
            if (Validation.isDayLegal(Integer.parseInt(newYear), Integer.parseInt(newMonth),
                    newDay)) {
                break;
            }
            System.out.println(Constants.INVALID_INPUT);
        }

        // update Database.account
        user.setDateOfBirth(new DateOfBirth(Integer.parseInt(newYear),
                Integer.parseInt(newMonth),
                Integer.parseInt(newDay)));

        // commit to the database file
        Database.accountUpdateCommit(user.getUuid(), Constants.USER_FIELD.DATE_OF_BIRTH);
    }

    public static void modifyEmailAddress(User user) {
        System.out.print(Constants.COLOR_WRAPPER("New EMAIL ADDRESS",
                Constants.WHITE_BOLD) + " : ");
        String newEmailAddress = Constants.IN.nextLine();

        // update Database.account
        user.setEmailAddress(newEmailAddress);

        // commit to the database file
        Database.accountUpdateCommit(user.getUuid(), Constants.USER_FIELD.EMAIL);
    }

    public static void modifyPassword(User user) {

        String newPassword = MenuTextUI.enterPassword(Constants.COLOR_WRAPPER("New PASSWORD",
                Constants.WHITE_BOLD) + " (the " +
                "length must be >= " + Constants.MIN_PASSWORD_LENGTH + "): ");

        // update Database.account
        user.setPassword(newPassword);

        // commit to the database file
        Database.accountUpdateCommit(user.getUuid(), Constants.USER_FIELD.PASSWORD);

    }

    public static void modifyAccountForNonAdmin(User user) {

        boolean exitSubMenu = false;
        showAccountInformation(user);

        while (!exitSubMenu) {

            String fieldToModify;
            System.out.println(" 1. Modify USERNAME");
            System.out.println(" 2. Modify ADDRESS");
            System.out.println(" 3. Modify PHONE NUMBER");
            System.out.println(" 4. Modify DATE OF BIRTH");
            System.out.println(" 5. Modify EMAIL ADDRESS");
            System.out.println(" 6. Modify PASSWORD");
            System.out.println(" 7. Return");
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

                // new address
                case "2":

                    modifyAddress(user);

                    // print the new user information
                    System.out.println();
                    System.out.println(Database.accounts.get(user.getUuid()));
                    System.out.println();
                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    System.out.println();
                    break;

                // new phone number
                case "3":

                    modifyPhoneNumber(user);

                    // print the new user information
                    System.out.println();
                    System.out.println(Database.accounts.get(user.getUuid()));
                    System.out.println();
                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    System.out.println();
                    break;

                // new date of birth
                case "4":

                    modifyDateOfBirth(user);

                    // print the new user information
                    System.out.println();
                    System.out.println(Database.accounts.get(user.getUuid()));
                    System.out.println();
                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    System.out.println();
                    break;

                // new email address
                case "5":

                    modifyEmailAddress(user);

                    // print the new user information
                    System.out.println();
                    System.out.println(Database.accounts.get(user.getUuid()));
                    System.out.println();
                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    System.out.println();
                    break;

                // new password
                case "6":

                    modifyPassword(user);

                    // print the new user information
                    System.out.println();
                    System.out.println(Database.accounts.get(user.getUuid()));
                    System.out.println();
                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    System.out.println();
                    break;

                // return
                case "7":

                    exitSubMenu = true;
                    break;

                default:

                    System.out.println(Constants.INVALID_INPUT);
                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    System.out.println();
            }
        }

    }
}

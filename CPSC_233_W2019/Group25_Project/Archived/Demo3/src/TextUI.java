import java.io.Console;

/**
 * The {@code TextUI} class contains all the common (statics) methods used in TextUI for three different kinds of users.
 * Depending on the user's role, a specific main menu is displayed with different options available to the user.
 */

public class TextUI {

    /**
     * Default Constructor
     */
    public TextUI() {
    }

    /**
     * Constructor that initializes the TextUI.
     *
     * @param uuid a String containing the uuid of the user.
     */
    public TextUI(String uuid) {

        HistoryLog.userLoginSuccessfully(uuid, Database.accounts.get(uuid).getUsername());
        System.out.println(Constants.COLOR_WRAPPER("Successfully logined!", Constants.GREEN_BOLD));
        System.out.println();
        System.out.println("Good day, " + (Database.accounts.get(uuid).getGender().equals(Constants.GENDER[0]) ? "Mr." : "Ms.") + Database.accounts.get(uuid).getFullName());
        System.out.println("Current login time is : " + getCurrentTime());

        System.out.println();
    }

    /**
     * Gets the current time of the time zone 'America/Edmonton'
     *
     * @return a String containing the current date.
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
     * @param prompt a String containing the password.
     * @return a String containing the password that the user inputs
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

    /**
     * Highlights the keyword that was search in the results that are displayed.
     *
     * @param s
     * @param keyword A String that contains the keyword that the user inputs.
     * @return A String that contains the highlighted characters of what the user searched in the results.
     */
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

    /**
     * Creates an arrayList that contains all the courses with the course ID that matches the keyword that the user inputs.
     *
     * @param keyword a String containing the course ID keyword that the user wants to search.
     * @return an arrayList with the results that contains the keyword in the course ID.
     */
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

    /**
     * Creates an arrayList that contains all the courses with the course name that matches the keyword that the user
     * inputs.
     *
     * @param keyword a String containing the course name keyword that the user wants to search.
     * @return an arrayList with the results that contains the keyword course name.
     */
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

    /**
     * Creates an arrayList that contains all the courses with a course name that matches the keyword that the user
     * inputs.
     *
     * @param keyword a String containing the course description keyword that the user wants to search.
     * @return an arrayList with the results that contains the keyword couse description.
     */
    public static ArrayList<Course> searchCourseDescriptionByKeywordAndSort(String keyword) {

        ArrayList<Course> result = new ArrayList<>();
        if (!keyword.isEmpty()) {
            // search
            for (Map.Entry<String, Course> i : Database.courses.entrySet()) {
                if (i.getValue().getCourseDescription().contains(keyword)) {
                    result.add(i.getValue());
                }
            }

            // sort
            Collections.sort(result);
        }
        return result;
    }

    /**
     * Creates an arrayList that contains all the courses with the course units that matches the keyword that the user
     * inputs.
     *
     * @param keyword a String containing the course units keyword that the user wants to search.
     * @return an arrayList with the results that contains the keyword couse units.
     */
    public static ArrayList<Course> searchCourseUnitsByKeywordAndSort(String keyword) {

        ArrayList<Course> result = new ArrayList<>();
        if (!keyword.isEmpty()) {
            // search
            for (Map.Entry<String, Course> i : Database.courses.entrySet()) {
                if (i.getValue().getCourseUnits().contains(keyword)) {
                    result.add(i.getValue());
                }
            }

            // sort
            Collections.sort(result);
        }
        return result;
    }

    /**
     * Creates an arrayList that contains all the courses with the course course prerequisites that matches the keyword
     * that the user inputs.
     *
     * @param keyword a String containing the course prerequiites keyword that the user wants to search.
     * @return an arrayList with the results that contains the keyword couse prerequisites.
     */
    public static ArrayList<Course> searchCoursePrerequisitesByKeywordAndSort(String keyword) {

        ArrayList<Course> result = new ArrayList<>();
        if (!keyword.isEmpty()) {
            // search
            for (Map.Entry<String, Course> i : Database.courses.entrySet()) {
                if (i.getValue().getPrerequisitesAsString().contains(keyword)) {
                    result.add(i.getValue());
                }
            }

            // sort
            Collections.sort(result);
        }
        return result;
    }

    /**
     * Creates an arrayList that contains all the courses with the course course antirequisites that matches the keyword
     * that the user inputs.
     *
     * @param keyword a String containing the course antirequiites keyword that the user wants to search.
     * @return an arrayList with the results that contains the keyword couse antirequisites.
     */
    public static ArrayList<Course> searchCourseAntirequisitesByKeywordAndSort(String keyword) {

        ArrayList<Course> result = new ArrayList<>();
        if (!keyword.isEmpty()) {
            // search
            for (Map.Entry<String, Course> i : Database.courses.entrySet()) {
                if (i.getValue().getAntirequisitesAsString().contains(keyword)) {
                    result.add(i.getValue());
                }
            }

            // sort
            Collections.sort(result);
        }
        return result;
    }

    /**
     * Search a course with keyword provided, show all satisfied results with keyword highlighted.
     * There are 6 different ways in which a user can search for a course. Depending on which option the user chooses,
     * the user can then input the keyword and a search is done. The results with are in an arrayList are then displayed.
     * If there are no matches found with the courses in the database and the keyword, the case would break and the user
     * must reselect what they want to search by.
     */
    public static void searchCourses() {

        boolean exitSubMenu = false;

        while (!exitSubMenu) {

            System.out.println("    +-----------------------------------+");
            System.out.println("    |          " + Constants.COLOR_WRAPPER("SEARCH SETTING", Constants.WHITE_BOLD) + "           |");
            System.out.println("    +-----------------------------------+");
            System.out.println("    |   1. Find by course id            |");
            System.out.println("    |   2. Find by course name          |");
            System.out.println("    |   3. Find by course description   |");
            System.out.println("    |   4. Find by course units         |");
            System.out.println("    |   5. Find by prerequisites        |");
            System.out.println("    |   6. Find by antirequisites       |");
            System.out.println("    |   7. Return                       |");
            System.out.println("    +-----------------------------------+");
            System.out.println();

            System.out.print(Constants.COLOR_WRAPPER("Select Number: ", Constants.WHITE_BOLD));
            String            select = Constants.IN.nextLine();
            String            keyword;
            ArrayList<Course> result;
            int               count  = 0;

            switch (select) {
                case "1":
                    System.out.print(Constants.COLOR_WRAPPER("Course id: ", Constants.WHITE_BOLD));
                    keyword = Constants.IN.nextLine();
                    result = searchCourseIdByKeywordAndSort(keyword);

                    // output
                    if (!result.isEmpty()) {
                        for (Course i : result) {
                            System.out.println();
                            System.out.println(i.toStringWithHighLight(Constants.COURSE_FIELD.COURSE_ID, keyword));
                            if (count == Constants.MAX_ITEMS_SCROLL_FORWARD) {
                                Constants.MORE();
                                count = 1;
                            } else {
                                ++count;
                            }
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
                            System.out.println(i.toStringWithHighLight(Constants.COURSE_FIELD.COURSE_NAME, keyword));
                            if (count == Constants.MAX_ITEMS_SCROLL_FORWARD) {
                                Constants.MORE();
                                count = 1;
                            } else {
                                ++count;
                            }
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

                    System.out.print(Constants.COLOR_WRAPPER("Course description: ", Constants.WHITE_BOLD));
                    keyword = Constants.IN.nextLine();
                    result = searchCourseDescriptionByKeywordAndSort(keyword);

                    // output
                    if (!result.isEmpty()) {
                        for (Course i : result) {
                            System.out.println();
                            System.out.println(i.toStringWithHighLight(Constants.COURSE_FIELD.COURSE_DESCRIPTION, keyword));
                            if (count == Constants.MAX_ITEMS_SCROLL_FORWARD) {
                                Constants.MORE();
                                count = 1;
                            } else {
                                ++count;
                            }
                        }
                        // stats information
                        System.out.println();
                        System.out.println("Matches: " + result.size());
                        System.out.println();
                    } else {
                        System.out.println(Constants.COLOR_WRAPPER("Course description does not exist!", Constants.RED));
                    }
                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    System.out.println();
                    break;
                case "4":
                    System.out.print(Constants.COLOR_WRAPPER("Course units: ", Constants.WHITE_BOLD));
                    keyword = Constants.IN.nextLine();
                    result = searchCourseUnitsByKeywordAndSort(keyword);

                    // output
                    if (!result.isEmpty()) {
                        for (Course i : result) {
                            System.out.println();
                            System.out.println(i.toStringWithHighLight(Constants.COURSE_FIELD.COURSE_UNITS, keyword));
                            if (count == Constants.MAX_ITEMS_SCROLL_FORWARD) {
                                Constants.MORE();
                                count = 1;
                            } else {
                                ++count;
                            }
                        }
                        // stats information
                        System.out.println();
                        System.out.println("Matches: " + result.size());
                        System.out.println();
                    } else {
                        System.out.println(Constants.COLOR_WRAPPER("Course units do not exist!", Constants.RED));
                    }
                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    System.out.println();
                    break;
                case "5":
                    System.out.print(Constants.COLOR_WRAPPER("Course prerequisites: ", Constants.WHITE_BOLD));
                    keyword = Constants.IN.nextLine();
                    result = searchCoursePrerequisitesByKeywordAndSort(keyword);

                    // output
                    if (!result.isEmpty()) {
                        for (Course i : result) {
                            System.out.println();
                            System.out.println(i.toStringWithHighLight(Constants.COURSE_FIELD.PREREQUISITES, keyword));
                            if (count == Constants.MAX_ITEMS_SCROLL_FORWARD) {
                                Constants.MORE();
                                count = 1;
                            } else {
                                ++count;
                            }
                        }
                        // stats information
                        System.out.println();
                        System.out.println("Matches: " + result.size());
                        System.out.println();
                    } else {
                        System.out.println(Constants.COLOR_WRAPPER("Course prerequisites do not exist!", Constants.RED));
                    }
                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    System.out.println();
                    break;
                case "6":
                    System.out.print(Constants.COLOR_WRAPPER("Course antirequisites: ", Constants.WHITE_BOLD));
                    keyword = Constants.IN.nextLine();
                    result = searchCourseAntirequisitesByKeywordAndSort(keyword);

                    // output
                    if (!result.isEmpty()) {
                        for (Course i : result) {
                            System.out.println();
                            System.out.println(i.toStringWithHighLight(Constants.COURSE_FIELD.ANTIREQUISITES, keyword));
                            if (count == Constants.MAX_ITEMS_SCROLL_FORWARD) {
                                Constants.MORE();
                                count = 1;
                            } else {
                                ++count;
                            }
                        }
                        // stats information
                        System.out.println();
                        System.out.println("Matches: " + result.size());
                        System.out.println();
                    } else {
                        System.out.println(Constants.COLOR_WRAPPER("Course antirequisites do not exist!", Constants.RED));
                    }
                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    System.out.println();
                    break;
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

    /**
     * Displays all the courses in the database in the screen.
     */
    public static void listAllCourses() {
        int count = 0;
        if (Database.courses != null) {
            for (Course i : Database.courses.values()) {
                System.out.println(i);
                System.out.println();
                if (count == Constants.MAX_ITEMS_SCROLL_FORWARD) {
                    Constants.MORE();
                    count = 1;
                } else {
                    ++count;
                }
            }
            System.out.println("TOTAL COURSES: " + Database.courses.size());
            System.out.println();
            Constants.PRESS_ENTER_KEY_TO_CONTINUE();
            System.out.println();
        }
    }

    /**
     * Displays all the account/personal information of the user logged in on the screen.
     *
     * @param user the user that is logged in.
     */
    public static void showAccountInformation(User user) {

        System.out.println();
        System.out.println(Constants.COLOR_WRAPPER("ACCOUNT INFORMATION : ", Constants.WHITE_BOLD));
        System.out.println();
        System.out.println(user);
        System.out.println();
        //Constants.PRESS_ENTER_KEY_TO_CONTINUE();

    }

    /**
     * Allows for the user to modify their username in the terminal.
     * The user can input their new username. The username would be modified only if the input is not empty and that
     * username does not already exist in the database. If it meets the requirements, the user's username would be set
     * to the new one and would be updated in the database.
     *
     * @param user the user that is logged in.
     */
    public static void modifyUsername(User user) {

        System.out.print(Constants.COLOR_WRAPPER("New USERNAME: ", Constants.WHITE_BOLD));
        String newUsername = Constants.IN.nextLine();

        // update Database.usernameToUuid and Database.account
        Database.usernameToUuid.remove(user.getUsername());
        user.setUsername(newUsername);
        Database.usernameToUuid.put(user.getUsername(), user.getUuid());

        // commit to the database file
        Database.accountUpdateCommit(user.getUuid(), Constants.USER_FIELD.USERNAME);

    }

    /**
     * Allows for the user to modify their full name in the terminal.
     * The user can input their new name. The name would be modified only the input contains the allowed characters and
     * the minimum length. If it meets the requirements,the user's name would be set to the new one and would be updated
     * in the database.
     *
     * @param user the user that is logged in.
     */
    public static void modifyFullName(User user) {

        System.out.print(Constants.COLOR_WRAPPER("New FULL NAME", Constants.WHITE_BOLD) +
                " (contain " +
                "uppercases and spaces only, at least 1 " +
                "letter and the length >= 3): ");
        String newFullName = Constants.IN.nextLine();

        // update Database.account
        user.setFullName(newFullName);

        // commit to the database file
        Database.accountUpdateCommit(user.getUuid(), Constants.USER_FIELD.FULLNAME);
    }

    /**
     * Allows for the user to modify their role in the terminal.
     * The user can input their new role. The role would be modified only if the input is admin, student, or faculty.
     * If it meets these requirements, the user's role would be set to the new one and would be updated in the
     * database.
     *
     * @param user the user that is logged in.
     */
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

    /**
     * Allows for the user to modify their gender in the terminal.
     * The user can input their new gender. The gender would be modified only if the input is M or F. If it meets this
     * requirement, the user's gender would be set to the new one and would be updated in the database.
     *
     * @param user the user that is logged in.
     */
    public static void modifyGender(User user) {

        System.out.print(Constants.COLOR_WRAPPER("New GENDER", Constants.WHITE_BOLD) + " (M or F): ");
        String newGender = Constants.IN.nextLine();

        // update Database.account
        user.setGender(newGender);

        // commit to the database file
        Database.accountUpdateCommit(user.getUuid(), Constants.USER_FIELD.GENDER);

    }

    /**
     * Allows for the user to modify their address in the terminal.
     * The user can input their new address. The address would be modified only if the input is not empty. If it meets
     * this requirement, the user's address would be set to the new one and would be updated in the database.
     *
     * @param user the user that is logged in.
     */
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

    /**
     * Allows for the user to modify their phone number in the terminal.
     * The user can input their new phone number. The phone number would be modified only if the input follows the
     * specific format. If it meets this requirement, the user's phone number would be set to the new one and would be
     * updated in the database.
     *
     * @param user the user that is logged in.
     */
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

    /**
     * Allows for the user to modify their date of birth in the terminal.
     * The user can input their new date of birth. The date of birth would be modified only if the input is the String
     * for year month and date is valid. If it meets this requirement, the user's phone number would be set to the new
     * one and would be updated in the database.
     *
     * @param user the user that is logged in.
     */
    public static void modifyDateOfBirth(User user) {
        System.out.println(Constants.COLOR_WRAPPER("New DATE OF BIRTH", Constants.WHITE_BOLD) + " : ");

        String newYear;
        while (true) {
            System.out.print("YEAR (>= " + Constants.MIN_YEAR + " and <= " + Constants.CURRENT_YEAR + "): ");
            newYear = Constants.IN.nextLine();
            if (Validation.isYearValid(newYear)) {
                break;
            }
            System.out.println(Constants.INVALID_INPUT);
        }

        String newMonth;
        while (true) {
            System.out.print("MONTH : ");
            newMonth = Constants.IN.nextLine();
            if (Validation.isMonthValid(newMonth)) {
                break;
            }
            System.out.println(Constants.INVALID_INPUT);
        }

        String newDay;
        while (true) {
            System.out.print("DAY : ");
            newDay = Constants.IN.nextLine();
            if (Validation.isDayValid(newYear, newMonth, newDay)) {
                break;
            }
            System.out.println(Constants.INVALID_INPUT);
        }

        // update Database.account
        user.setDateOfBirth(new DateOfBirth(newYear, newMonth, newDay));

        // commit to the database file
        Database.accountUpdateCommit(user.getUuid(), Constants.USER_FIELD.DATE_OF_BIRTH);
    }

    /**
     * Allows for the user to modify their email address in the terminal.
     * The user can input their new email address. The email address would be set to the new one and would be updated in
     * the database.
     *
     * @param user The user that is logged in.
     */
    public static void modifyEmailAddress(User user) {
        System.out.print(Constants.COLOR_WRAPPER("New EMAIL ADDRESS",
                Constants.WHITE_BOLD) + " : ");
        String newEmailAddress = Constants.IN.nextLine();

        // update Database.account
        user.setEmailAddress(newEmailAddress);

        // commit to the database file
        Database.accountUpdateCommit(user.getUuid(), Constants.USER_FIELD.EMAIL);
    }

    /**
     * Allows for the user to modify their password in the terminal.
     * The user can input their new password. The password would be modified only if the length of the String input
     * greater than the minimum length. If it meets this requirement, the user's password would be set to the new
     * one and would be updated in the database.
     *
     * @param user The user that is logged in.
     */
    public static void modifyPassword(User user) {

        String newPassword = MenuTextUI.enterPassword(Constants.COLOR_WRAPPER("New PASSWORD",
                Constants.WHITE_BOLD) + " (the " +
                "length must be >= " + Constants.MIN_PASSWORD_LENGTH + "): ");

        // update Database.account
        user.setPassword(newPassword);

        // commit to the database file
        Database.accountUpdateCommit(user.getUuid(), Constants.USER_FIELD.PASSWORD);

    }

    /**
     * Allows for a non admin user to modify their information in their account .
     * The user choose what they want to modify in the list provided (7 cases), and if the input is valid, then the
     * information would changed and updated in the database.
     *
     * @param user The user that is logged in.
     */
    public static void modifyAccountForNonAdmin(User user) {

        boolean exitSubMenu = false;
        while (!exitSubMenu) {

            String fieldToModify;
            System.out.println();
            System.out.println("    +----------------------------+");
            System.out.println("    |      " + Constants.COLOR_WRAPPER("ACCOUNT SETTING", Constants.WHITE_BOLD) + "  " +
                    "     |");
            System.out.println("    +----------------------------+");
            System.out.println("    |   1. Modify username       |");
            System.out.println("    |   2. Modify address        |");
            System.out.println("    |   3. Modify phone number   |");
            System.out.println("    |   4. Modify date of birth  |");
            System.out.println("    |   5. Modify email address  |");
            System.out.println("    |   6. Modify password       |");
            System.out.println("    |   7. Return                |");
            System.out.println("    +----------------------------+");
            System.out.println();
            System.out.println(Constants.COLOR_WRAPPER(" Note : Invalid value will remain the original data " +
                    "unchanged!", Constants.YELLOW_BOLD));
            System.out.println();

            System.out.print(Constants.COLOR_WRAPPER("Select number: ", Constants.WHITE_BOLD));
            fieldToModify = Constants.IN.nextLine();
            switch (fieldToModify) {

                // new username
                case "1":

                    modifyUsername(user);
                    break;

                // new address
                case "2":

                    modifyAddress(user);
                    break;

                // new phone number
                case "3":

                    modifyPhoneNumber(user);
                    break;

                // new date of birth
                case "4":

                    modifyDateOfBirth(user);
                    break;

                // new email address
                case "5":

                    modifyEmailAddress(user);
                    break;

                // new password
                case "6":

                    modifyPassword(user);
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
            // print the new user information if 1 <= selected number <= 6
            for (int i = 1; i <= 6; ++i) {
                if (fieldToModify.equals(String.valueOf(i))) {
                    System.out.println();
                    System.out.println(Database.accounts.get(user.getUuid()));
                    System.out.println();
                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    System.out.println();
                    break;
                }
            }
        }
    }
}

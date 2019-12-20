import java.io.Console;
import java.sql.Timestamp;

/**
 * The {@code TextUI} class contains all the common (statics) methods used in TextUI for three different kinds of users.
 * Depending on the user's role, a specific main menu is displayed with different options available to the user.
 *
 * @author Group 25
 * @date 2019/04/08
 */

public class TextUI {

    /**
     * Default Constructor
     *
     * @date 2019/04/08
     */
    public TextUI() {
    }

    /**
     * Constructor that initializes the TextUI.
     *
     * @param uuid a string contains the uuid of the user
     * @date 2019/04/08
     */
    public TextUI(String uuid) {
        System.out.println(Constants.colorWrapper("Successfully login!", Constants.GREEN_BOLD));
        System.out.println();
        System.out.println("Good day, " + (Database.accounts.get(uuid).getGender().equals(Constants.GENDER[0]) ? "Mr." : "Ms.") + Database.accounts.get(uuid).getFullName());
        System.out.println("Current login time is : " + getCurrentTime());
        System.out.println();
    }

    /**
     * The method gets the current time of the time zone 'America/Edmonton'.
     *
     * @return a string contains the current date
     * @date 2019/04/08
     */
    private static String getCurrentTime() {
        Date currentDate = Calendar.getInstance(Constants.TIME_ZONE).getTime();
        return currentDate.toString();
    }

    /**
     * The method cleans the terminal screen in *nix system.
     *
     * @date 2019/04/08
     */
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * A function make the user's password invisible during the input.
     *
     * @param prompt a String containing the password.
     * @return a string contains the password that the user inputs
     * @date 2019/04/08
     */
    public static String enterPassword(String prompt) {

        String password = "";
        try {
            Console console = System.console();
            if (console == null) {
                throw new Exception();
            }
            password = String.valueOf(console.readPassword(prompt));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return password;
    }

    /**
     * Highlights the keyword that was search in the results that are displayed.
     *
     * @param s       the string to decorate
     * @param keyword A String that contains the keyword that the user inputs.
     * @return A String that contains the highlighted characters of what the user searched in the results.
     * @date 2019/04/08
     */
    public static String colorHighlightedSubstring(String s, String keyword) {

        HashSet<Integer> highlightPosition = new HashSet<>();
        int              index             = 0;
        do {
            index = s.indexOf(keyword, index);
            if (index == -1) {
                break;
            }
            for (int i = index; i < index + keyword.length(); ++i) {
                highlightPosition.add(i);
            }
            index += 2;
        } while (true);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); ++i) {
            if (highlightPosition.contains(i)) {
                sb.append(Constants.colorWrapper(s.charAt(i), Constants.RED_BOLD));
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
     * @date 2019/04/08
     */
    private static ArrayList<Course> searchCourseIdByKeywordAndSort(String keyword) {

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
     * @date 2019/04/08
     */
    private static ArrayList<Course> searchCourseNameByKeywordAndSort(String keyword) {

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
     * @return an arrayList with the results that contains the keyword course description.
     * @date 2019/04/08
     */
    private static ArrayList<Course> searchCourseDescriptionByKeywordAndSort(String keyword) {

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
     * @return an arrayList with the results that contains the keyword course units.
     * @date 2019/04/08
     */
    private static ArrayList<Course> searchCourseUnitsByKeywordAndSort(String keyword) {

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
     * @param keyword a String containing the course prerequisites keyword that the user wants to search.
     * @return an arrayList with the results that contains the keyword course prerequisites.
     * @date 2019/04/08
     */
    private static ArrayList<Course> searchCoursePrerequisitesByKeywordAndSort(String keyword) {

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
     * @param keyword a String containing the course anti-requisites keyword that the user wants to search.
     * @return an arrayList with the results that contains the keyword course antirequisites.
     * @date 2019/04/08
     */
    private static ArrayList<Course> searchCourseAntirequisitesByKeywordAndSort(String keyword) {

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
     *
     * @date 2019/04/08
     */
    public static void searchCourses() {

        boolean exitSubMenu = false;
        System.out.println();

        while (!exitSubMenu) {

            System.out.println("    +-----------------------------------+");
            System.out.println("    |          " + Constants.colorWrapper("SEARCH SETTING", Constants.WHITE_BOLD) + "           |");
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

            System.out.print(Constants.colorWrapper("Select Number: ", Constants.WHITE_BOLD));
            String            select = Constants.IN.nextLine();
            String            keyword;
            ArrayList<Course> result;
            int               count  = 0;

            switch (select) {
                case "1":
                    System.out.print(Constants.colorWrapper("Course id: ", Constants.WHITE_BOLD));
                    keyword = Constants.IN.nextLine();
                    result = searchCourseIdByKeywordAndSort(keyword);
                    // output
                    if (!result.isEmpty()) {
                        for (Course i : result) {
                            System.out.println();
                            System.out.println(i.toStringWithHighLight(Constants.COURSE_FIELD.COURSE_ID, keyword));
                            if (count == Constants.MAX_ITEMS_SCROLL_FORWARD) {
                                Constants.displayLineSeparator();
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
                        System.out.println(Constants.colorWrapper("Course id does not exist!", Constants.RED));
                    }
                    Constants.pressEnterToContinue();
                    System.out.println();
                    break;
                case "2":
                    System.out.print(Constants.colorWrapper("Course name: ", Constants.WHITE_BOLD));
                    keyword = Constants.IN.nextLine();
                    result = searchCourseNameByKeywordAndSort(keyword);
                    // output
                    if (!result.isEmpty()) {
                        for (Course i : result) {
                            System.out.println();
                            System.out.println(i.toStringWithHighLight(Constants.COURSE_FIELD.COURSE_NAME, keyword));
                            if (count == Constants.MAX_ITEMS_SCROLL_FORWARD) {
                                Constants.displayLineSeparator();
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
                        System.out.println(Constants.colorWrapper("Course name does not exist!", Constants.RED));
                    }
                    Constants.pressEnterToContinue();
                    System.out.println();
                    break;
                case "3":

                    System.out.print(Constants.colorWrapper("Course description: ", Constants.WHITE_BOLD));
                    keyword = Constants.IN.nextLine();
                    result = searchCourseDescriptionByKeywordAndSort(keyword);
                    // output
                    if (!result.isEmpty()) {
                        for (Course i : result) {
                            System.out.println();
                            System.out.println(i.toStringWithHighLight(Constants.COURSE_FIELD.COURSE_DESCRIPTION, keyword));
                            if (count == Constants.MAX_ITEMS_SCROLL_FORWARD) {
                                Constants.displayLineSeparator();
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
                        System.out.println(Constants.colorWrapper("Course description does not exist!", Constants.RED));
                    }
                    Constants.pressEnterToContinue();
                    System.out.println();
                    break;
                case "4":
                    System.out.print(Constants.colorWrapper("Course units: ", Constants.WHITE_BOLD));
                    keyword = Constants.IN.nextLine();
                    result = searchCourseUnitsByKeywordAndSort(keyword);
                    // output
                    if (!result.isEmpty()) {
                        for (Course i : result) {
                            System.out.println();
                            System.out.println(i.toStringWithHighLight(Constants.COURSE_FIELD.COURSE_UNITS, keyword));
                            if (count == Constants.MAX_ITEMS_SCROLL_FORWARD) {
                                Constants.displayLineSeparator();
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
                        System.out.println(Constants.colorWrapper("Course units do not exist!", Constants.RED));
                    }
                    Constants.pressEnterToContinue();
                    System.out.println();
                    break;
                case "5":
                    System.out.print(Constants.colorWrapper("Course prerequisites: ", Constants.WHITE_BOLD));
                    keyword = Constants.IN.nextLine();
                    result = searchCoursePrerequisitesByKeywordAndSort(keyword);
                    // output
                    if (!result.isEmpty()) {
                        for (Course i : result) {
                            System.out.println();
                            System.out.println(i.toStringWithHighLight(Constants.COURSE_FIELD.PREREQUISITES, keyword));
                            if (count == Constants.MAX_ITEMS_SCROLL_FORWARD) {
                                Constants.displayLineSeparator();
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
                        System.out.println(Constants.colorWrapper("Course prerequisites do not exist!", Constants.RED));
                    }
                    Constants.pressEnterToContinue();
                    System.out.println();
                    break;
                case "6":
                    System.out.print(Constants.colorWrapper("Course antirequisites: ", Constants.WHITE_BOLD));
                    keyword = Constants.IN.nextLine();
                    result = searchCourseAntirequisitesByKeywordAndSort(keyword);
                    // output
                    if (!result.isEmpty()) {
                        for (Course i : result) {
                            System.out.println();
                            System.out.println(i.toStringWithHighLight(Constants.COURSE_FIELD.ANTIREQUISITES, keyword));
                            if (count == Constants.MAX_ITEMS_SCROLL_FORWARD) {
                                Constants.displayLineSeparator();
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
                        System.out.println(Constants.colorWrapper("Course antirequisites do not exist!", Constants.RED));
                    }
                    Constants.pressEnterToContinue();
                    System.out.println();
                    break;
                case "7":
                    exitSubMenu = true;
                    break;
                default:
                    System.out.println(Constants.INVALID_INPUT);
                    Constants.pressEnterToContinue();
                    System.out.println();
            }
        }
    }

    /**
     * Displays all the courses in the database in the screen.
     *
     * @date 2019/04/08
     */
    public static void listAllCourses() {
        int count = 0;
        for (Course i : Database.courses.values()) {
            System.out.println(i);
            System.out.println();
            if (count == Constants.MAX_ITEMS_SCROLL_FORWARD) {
                Constants.displayLineSeparator();
                count = 1;
            } else {
                ++count;
            }
        }
        System.out.println("TOTAL COURSES: " + Database.courses.size());
        System.out.println();
        Constants.pressEnterToContinue();
        System.out.println();
    }

    /**
     * Displays all the account/personal information of the user logged in on the screen.
     *
     * @param user the user that is logged in.
     * @date 2019/04/08
     */
    public static void showAccountInformation(User user) {
        System.out.println();
        System.out.println(Constants.colorWrapper("ACCOUNT INFORMATION : ", Constants.WHITE_BOLD));
        System.out.println();
        System.out.println(user);
        System.out.println();
    }

    /**
     * Allows for the user to modify their username in the terminal.
     * The user can input their new username. The username would be modified only if the input is not empty and that
     * username does not already exist in the database. If it meets the requirements, the user's username would be set
     * to the new one and would be updated in the database.
     *
     * @param user the user that is logged in
     * @date 2019/04/08
     */
    public static void modifyUsername(User user) {

        System.out.print(Constants.colorWrapper("New USERNAME: ", Constants.WHITE_BOLD));
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
     * @param user the user that is logged in
     * @date 2019/04/08
     */
    public static void modifyFullName(User user) {

        System.out.print(Constants.colorWrapper("New FULL NAME: ", Constants.WHITE_BOLD));
        String newFullName = Constants.IN.nextLine();

        // update Database.account
        user.setFullName(newFullName);

        // commit to the database file
        Database.accountUpdateCommit(user.getUuid(), Constants.USER_FIELD.FULL_NAME);
    }

    /**
     * Allows for the user to modify their gender in the terminal.
     * The user can input their new gender. The gender would be modified only if the input is M or F. If it meets this
     * requirement, the user's gender would be set to the new one and would be updated in the database.
     *
     * @param user the user that is logged in
     * @date 2019/04/08
     */
    public static void modifyGender(User user) {

        System.out.print(Constants.colorWrapper("New GENDER: ", Constants.WHITE_BOLD));
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
     * @param user the user that is logged in
     * @date 2019/04/08
     */
    public static void modifyAddress(User user) {

        System.out.print(Constants.colorWrapper("New ADDRESS: ", Constants.WHITE_BOLD));
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
     * @param user the user that is logged in
     * @date 2019/04/08
     */
    public static void modifyPhoneNumber(User user) {

        System.out.print(Constants.colorWrapper("New PHONE NUMBER", Constants.WHITE_BOLD) + " (Format: " +
                "###-###-####): ");
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
     * @param user the user that is logged in
     * @date 2019/04/08
     */
    public static void modifyDateOfBirth(User user) {
        System.out.println(Constants.colorWrapper("New DATE OF BIRTH", Constants.WHITE_BOLD) + ": ");

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
     * @param user The user that is logged in
     * @date 2019/04/08
     */
    public static void modifyEmailAddress(User user) {
        System.out.print(Constants.colorWrapper("New EMAIL ADDRESS: ", Constants.WHITE_BOLD));
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
     * @param user The user that is logged in
     * @date 2019/04/08
     */
    public static void modifyPassword(User user) {

        // Student and faculty must validate their old passwords, no need for admin
        if (!Constants.ROLE[0].equals(Database.accounts.get(MenuTextUI.uuid).getRole())) {
            String oldPassword = MenuTextUI.enterPassword(Constants.colorWrapper("Old PASSWORD: ", Constants.WHITE_BOLD));
            if (!oldPassword.equals(user.getPassword())) {
                System.out.println(Constants.colorWrapper("Password incorrect!", Constants.RED));
                return;
            }
        }

        String newPassword = MenuTextUI.enterPassword(Constants.colorWrapper("New PASSWORD: ", Constants.WHITE_BOLD));
        String newPasswordAgain = MenuTextUI.enterPassword(Constants.colorWrapper("Press PASSWORD again: ",
                Constants.WHITE_BOLD));

        if (!newPassword.equals(newPasswordAgain)) {
            System.out.println(Constants.colorWrapper("Password not matched!", Constants.RED));
            return;
        }

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
     * @param user the user that is logged in
     * @date 2019/04/08
     */
    public static void modifyAccountForNonAdmin(User user) {

        // if the intensity of current password is weak, pop out a message to prompt the user to change
        if (Utility.passwordIntensity(user.getPassword()) < 25) {
            System.out.println(Constants.colorWrapper("Warning! The password is weak! For the purpose of safety," +
                    " changing the password is strongly recommended!", Constants.RED));
        }

        boolean exitSubMenu = false;
        while (!exitSubMenu) {

            String fieldToModify;
            System.out.println();
            System.out.println("    +----------------------------+");
            System.out.println("    |      " + Constants.colorWrapper("ACCOUNT SETTING", Constants.WHITE_BOLD) + "  " +
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
            System.out.println(Constants.colorWrapper(" Note : Invalid value will remain the original data " +
                    "unchanged!", Constants.YELLOW_BOLD));
            System.out.println();

            System.out.print(Constants.colorWrapper("Select number: ", Constants.WHITE_BOLD));
            fieldToModify = Constants.IN.nextLine();
            switch (fieldToModify) {
                case "1":
                    modifyUsername(user);
                    break;
                case "2":
                    modifyAddress(user);
                    break;
                case "3":
                    modifyPhoneNumber(user);
                    break;
                case "4":
                    modifyDateOfBirth(user);
                    break;
                case "5":
                    modifyEmailAddress(user);
                    break;
                case "6":
                    modifyPassword(user);
                    break;
                case "7":
                    exitSubMenu = true;
                    break;
                default:
                    System.out.println(Constants.INVALID_INPUT);
                    Constants.pressEnterToContinue();
                    System.out.println();
            }

            // print the new user information if 1 <= selected number <= 6
            for (int i = 1; i <= 6; ++i) {
                if (fieldToModify.equals(String.valueOf(i))) {
                    System.out.println();
                    System.out.println(Database.accounts.get(user.getUuid()));
                    System.out.println();

                    // log if the account information is changed
                    Log.updateOwnAccount(new Timestamp(System.currentTimeMillis()), MenuTextUI.uuid);
                    Constants.pressEnterToContinue();
                    break;
                }
            }
        }
    }
}

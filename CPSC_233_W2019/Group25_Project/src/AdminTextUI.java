import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;

/**
 * The {@code AdminTextUI} class contains static methods used specifically for Administrators in the the TextUI.
 *
 * @author Group 25
 * @date 2019/04/08
 */

public class AdminTextUI extends TextUI {

    /**
     * The constructor that initializes the admin user in the database and displays the main admin account page in
     * text-mode.
     *
     * @param uuid a string containing the uuid of the admin user.
     * @date 2019/04/08
     */
    public AdminTextUI(String uuid) {

        super(uuid);
        Admin  user = (Admin) Database.accounts.get(uuid);
        String select;

        while (true) {

            System.out.println("    +------------------------------------+");
            System.out.println("    |          " + Constants.colorWrapper("CONTROL  PANEL", Constants.WHITE_BOLD) +
                    "            |");
            System.out.println("    |------------------------------------|");
            System.out.println("    |   1. List all available accounts   |");
            System.out.println("    |   2. List all available courses    |");
            System.out.println("    |   3. Search a course               |");
            System.out.println("    |   4. Search an account             |");
            System.out.println("    |   5. Modify a course               |");
            System.out.println("    |   6. Modify an account             |");
            System.out.println("    |   7. Show filepath of database     |");
            System.out.println("    |   8. Create an account             |");
            System.out.println("    |   9. Create a course               |");
            System.out.println("    |  10. Remove an account             |");
            System.out.println("    |  11. Remove a course               |");
            System.out.println("    |  12. Personal information          |");
            System.out.println("    |  13. History Log                   |");
            System.out.println("    |  14. Logout                        |");
            System.out.println("    +------------------------------------+");
            System.out.println();
            System.out.print(Constants.colorWrapper("Select number: ", Constants.WHITE_BOLD));
            select = Constants.IN.nextLine();

            switch (select) {
                case "1":
                    listAllAccounts();
                    break;
                case "2":
                    listAllCourses();
                    break;
                case "3":
                    searchCourses();
                    System.out.println();
                    break;
                case "4":
                    searchAccounts();
                    System.out.println();
                    break;
                case "5":
                    modifyACourse();
                    break;
                case "6":
                    modifyAccount();
                    break;
                case "7":
                    showDatabaseFilePath();
                    break;
                case "8":
                    createAnAccount();
                    break;
                case "9":
                    createACourse();
                    break;
                case "10":
                    removeAnAccount(user.getUsername());
                    break;
                case "11":
                    removeACourse();
                    break;
                case "12":
                    System.out.println();
                    showAccountInformation(user);
                    break;
                case "13":
                    checkHistoryLog();
                    break;
                case "14":
                    return;
                default:
                    System.out.println(Constants.INVALID_INPUT);
                    System.out.println();
            }
        }
    }

    /**
     * The method allows for the admin to search accounts by role.
     *
     * @param keyword a String containing the keyword that the user inputs/searches.
     * @return an arrayList containing the results of the search.
     * @date 2019/04/08
     */
    private static ArrayList<User> searchRolesAndSort(String keyword) {
        ArrayList<User> result = new ArrayList<>();
        // search
        for (Map.Entry<String, User> i : Database.accounts.entrySet()) {
            if (i.getValue().getRole().equals(keyword)) {
                result.add(i.getValue());
            }
        }
        // sort
        Collections.sort(result);
        return result;
    }

    /**
     * The method allows for the admin to search accounts by gender.
     *
     * @param keyword a String containing the keyword that the user inputs/searches.
     * @return an arrayList containing the results of the search.
     * @date 2019/04/08
     */
    private static ArrayList<User> searchGenderAndSort(String keyword) {
        ArrayList<User> result = new ArrayList<>();
        if (Validation.isGenderValid(keyword)) {
            // search
            for (Map.Entry<String, User> i : Database.accounts.entrySet()) {
                if (i.getValue().getGender().equals(keyword)) {
                    result.add(i.getValue());
                }
            }
            // sort
            Collections.sort(result);
        }
        return result;
    }

    /**
     * The method allows for the admin to search accounts by username.
     *
     * @param keyword a String containing the keyword that the user inputs/searches.
     * @return an arrayList containing the results of the search.
     * @date 2019/04/08
     */
    private static ArrayList<User> searchUsernameAndSort(String keyword) {
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

    /**
     * The method allows for the admin to search accounts by full names.
     *
     * @param keyword a String containing the keyword that the user inputs/searches.
     * @return an array list containing the results of the search.
     * @date 2019/04/08
     */
    private static ArrayList<User> searchFullNameAndSort(String keyword) {
        ArrayList<User> result = new ArrayList<>();
        if (!keyword.isEmpty()) {
            // search
            for (Map.Entry<String, User> i : Database.accounts.entrySet()) {
                if (i.getValue().getFullName().contains(keyword)) {
                    result.add(i.getValue());
                }
            }
            // sort
            Collections.sort(result);
        }
        return result;
    }

    /**
     * The method allows for the admin to search accounts by uuid.
     *
     * @param keyword a String containing the keyword that the user inputs/searches.
     * @return an arrayList containing the results of the search.
     * @date 2019/04/08
     */
    private static ArrayList<User> searchUuidAndSort(String keyword) {
        ArrayList<User> result = new ArrayList<>();
        if (!keyword.isEmpty()) {
            // search
            for (Map.Entry<String, User> i : Database.accounts.entrySet()) {
                if (i.getValue().getUuid().contains(keyword)) {
                    result.add(i.getValue());
                }
            }
            // sort
            Collections.sort(result);
        }
        return result;
    }

    /**
     * The method allows for the admin to search accounts by email.
     *
     * @param keyword a String containing the keyword that the user inputs/searches.
     * @return an arrayList containing the results of the search.
     * @date 2019/04/08
     */
    private static ArrayList<User> searchEmailAndSort(String keyword) {
        ArrayList<User> result = new ArrayList<>();
        if (!keyword.isEmpty()) {
            // search
            for (Map.Entry<String, User> i : Database.accounts.entrySet()) {
                if (i.getValue().getEmailAddress().contains(keyword)) {
                    result.add(i.getValue());
                }
            }
            // sort
            Collections.sort(result);
        }
        return result;
    }

    /**
     * The method allows for the admin to search accounts by address.
     *
     * @param keyword a String containing the keyword that the user inputs/searches.
     * @return an arrayList containing the results of the search.
     * @date 2019/04/08
     */
    private static ArrayList<User> searchAddressAndSort(String keyword) {
        ArrayList<User> result = new ArrayList<>();
        if (!keyword.isEmpty()) {
            // search
            for (Map.Entry<String, User> i : Database.accounts.entrySet()) {
                if (i.getValue().getAddress().contains(keyword)) {
                    result.add(i.getValue());
                }
            }
            // sort
            Collections.sort(result);
        }
        return result;
    }

    /**
     * THe method allows for the admin to search accounts by phone number.
     *
     * @param keyword a String containing the keyword that the user inputs/searches.
     * @return an arrayList containing the results of the search.
     * @date 2019/04/08
     */
    private static ArrayList<User> searchPhoneNumberAndSort(String keyword) {
        ArrayList<User> result = new ArrayList<>();
        if (!keyword.isEmpty()) {
            // search
            for (Map.Entry<String, User> i : Database.accounts.entrySet()) {
                if (i.getValue().getPhoneNumber().contains(keyword)) {
                    result.add(i.getValue());
                }
            }
            // sort
            Collections.sort(result);
        }
        return result;
    }

    /**
     * The method allows for the admin to search accounts by date of birth.
     *
     * @param keyword a String containing the keyword that the user inputs/searches.
     * @return an arrayList containing the results of the search.
     * @date 2019/04/08
     */
    private static ArrayList<User> searchDateOfBirthAndSort(String keyword) {
        ArrayList<User> result = new ArrayList<>();
        if (!keyword.isEmpty()) {
            // search
            for (Map.Entry<String, User> i : Database.accounts.entrySet()) {
                if (i.getValue().getDateOfBirth().toString().contains(keyword)) {
                    result.add(i.getValue());
                }
            }
            // sort
            Collections.sort(result);
        }
        return result;
    }

    /**
     * The method displays the menu that allows the admin to search for accounts through various different ways.
     * The different options to do so are displayed and depending on what the user selects, corresponding results would
     * be displayed.
     *
     * @date 2019/04/08
     */
    private static void searchAccounts() {

        boolean exitSubMenu = false;
        System.out.println();

        while (!exitSubMenu) {

            System.out.println("    +-----------------------------+");
            System.out.println("    |       " + Constants.colorWrapper("SEARCH SETTING", Constants.WHITE_BOLD) + "        |");
            System.out.println("    +-----------------------------+");
            System.out.println("    |   1. Find by username       |");
            System.out.println("    |   2. Find by role           |");
            System.out.println("    |   3. Find by gender         |");
            System.out.println("    |   4. Find by full name      |");
            System.out.println("    |   5. Find by uuid           |");
            System.out.println("    |   6. Find by email          |");
            System.out.println("    |   7. Find by address        |");
            System.out.println("    |   8. Find by phone number   |");
            System.out.println("    |   9. Find by date of birth  |");
            System.out.println("    |  10. Return                 |");
            System.out.println("    +-----------------------------+");
            System.out.println();
            System.out.print(Constants.colorWrapper("Select Number: ", Constants.WHITE_BOLD));
            String          select = Constants.IN.nextLine();
            ArrayList<User> result;
            String          keyword;
            int             count  = 0;

            switch (select) {
                case "1":
                    System.out.print(Constants.colorWrapper("Find username: ", Constants.WHITE_BOLD));
                    keyword = Constants.IN.nextLine();
                    result = searchUsernameAndSort(keyword);
                    // output
                    if (!result.isEmpty()) {
                        for (User i : result) {
                            System.out.println();
                            System.out.println(i.toStringWithHighLight(Constants.USER_FIELD.USERNAME, keyword));
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
                        System.out.println(Constants.colorWrapper("Account does not exist!", Constants.RED));
                    }
                    Constants.pressEnterToContinue();
                    System.out.println();
                    break;
                case "2":
                    System.out.print(Constants.colorWrapper("Find role: ", Constants.WHITE_BOLD) + "(ADMIN/FACULTY" +
                            "/STUDENT) ");
                    keyword = Constants.IN.nextLine();
                    result = searchRolesAndSort(keyword);
                    // output
                    if (!result.isEmpty()) {
                        for (User i : result) {
                            System.out.println();
                            System.out.println(i.toStringWithHighLight(Constants.USER_FIELD.ROLE, keyword));
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
                        System.out.println(Constants.colorWrapper("Account does not exist!", Constants.RED));
                    }
                    Constants.pressEnterToContinue();
                    System.out.println();
                    break;
                case "3":
                    System.out.print(Constants.colorWrapper("Find gender: ", Constants.WHITE_BOLD) + "(M/F) ");
                    keyword = Constants.IN.nextLine();
                    result = searchGenderAndSort(keyword);
                    // output
                    if (!result.isEmpty()) {
                        for (User i : result) {
                            System.out.println();
                            System.out.println(i.toStringWithHighLight(Constants.USER_FIELD.GENDER, keyword));
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
                        System.out.println(Constants.colorWrapper("Account does not exist!", Constants.RED));
                    }
                    Constants.pressEnterToContinue();
                    System.out.println();
                    break;
                case "4":
                    System.out.print(Constants.colorWrapper("Find full name: ", Constants.WHITE_BOLD));
                    keyword = Constants.IN.nextLine();
                    result = searchFullNameAndSort(keyword);
                    // output
                    if (!result.isEmpty()) {
                        for (User i : result) {
                            System.out.println();
                            System.out.println(i.toStringWithHighLight(Constants.USER_FIELD.FULL_NAME, keyword));
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
                        System.out.println(Constants.colorWrapper("Account does not exist!", Constants.RED));
                    }
                    Constants.pressEnterToContinue();
                    System.out.println();
                    break;
                case "5":
                    System.out.print(Constants.colorWrapper("Find UUID: ", Constants.WHITE_BOLD));
                    keyword = Constants.IN.nextLine();
                    result = searchUuidAndSort(keyword);
                    // output
                    if (!result.isEmpty()) {
                        for (User i : result) {
                            System.out.println();
                            System.out.println(i.toStringWithHighLight(Constants.USER_FIELD.UUID, keyword));
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
                        System.out.println(Constants.colorWrapper("Account does not exist!", Constants.RED));
                    }
                    Constants.pressEnterToContinue();
                    System.out.println();
                    break;
                case "6":
                    System.out.print(Constants.colorWrapper("Find email address: ", Constants.WHITE_BOLD));
                    keyword = Constants.IN.nextLine();
                    result = searchEmailAndSort(keyword);
                    // output
                    if (!result.isEmpty()) {
                        for (User i : result) {
                            System.out.println();
                            System.out.println(i.toStringWithHighLight(Constants.USER_FIELD.EMAIL, keyword));
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
                        System.out.println(Constants.colorWrapper("Account does not exist!", Constants.RED));
                    }
                    Constants.pressEnterToContinue();
                    System.out.println();
                    break;
                case "7":
                    System.out.print(Constants.colorWrapper("Find address: ", Constants.WHITE_BOLD));
                    keyword = Constants.IN.nextLine();
                    result = searchAddressAndSort(keyword);
                    // output
                    if (!result.isEmpty()) {
                        for (User i : result) {
                            System.out.println();
                            System.out.println(i.toStringWithHighLight(Constants.USER_FIELD.ADDRESS, keyword));
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
                        System.out.println(Constants.colorWrapper("Account does not exist!", Constants.RED));
                    }
                    Constants.pressEnterToContinue();
                    System.out.println();
                    break;
                case "8":
                    System.out.print(Constants.colorWrapper("Find phone number: ", Constants.WHITE_BOLD));
                    keyword = Constants.IN.nextLine();
                    result = searchPhoneNumberAndSort(keyword);
                    // output
                    if (!result.isEmpty()) {
                        for (User i : result) {
                            System.out.println();
                            System.out.println(i.toStringWithHighLight(Constants.USER_FIELD.PHONE, keyword));
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
                        System.out.println(Constants.colorWrapper("Account does not exist!", Constants.RED));
                    }
                    Constants.pressEnterToContinue();
                    System.out.println();
                    break;
                case "9":
                    System.out.print(Constants.colorWrapper("Find date of birth: ", Constants.WHITE_BOLD));
                    keyword = Constants.IN.nextLine();
                    result = searchDateOfBirthAndSort(keyword);
                    // output
                    if (!result.isEmpty()) {
                        for (User i : result) {
                            System.out.println();
                            System.out.println(i.toStringWithHighLight(Constants.USER_FIELD.DATE_OF_BIRTH, keyword));
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
                        System.out.println(Constants.colorWrapper("Account does not exist!", Constants.RED));
                    }
                    Constants.pressEnterToContinue();
                    System.out.println();
                    break;
                case "10":
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
     * The method allows for the admin to modify an account. The pre-condition is the account must exist in the
     * database for the user to do so. If the account does exist, the admin can choose which property of the account
     * to modify.
     *
     * @date 2019/04/11
     */
    private static void modifyAccount() {

        System.out.print(Constants.colorWrapper("Account username: ", Constants.WHITE_BOLD));
        String username = Constants.IN.nextLine();

        if (!Database.usernameToUuid.containsKey(username)) {
            System.out.println(Constants.colorWrapper("Account does not exist!", Constants.RED));
            Constants.pressEnterToContinue();
            System.out.println();
            return;
        }

        boolean exitMenu = false;
        User    user     = Database.accounts.get(Database.usernameToUuid.get(username));
        showAccountInformation(user);

        // if the user is a student, list the user's courses history
        if (user.getRole().equals(Constants.ROLE[2])) {
            Student student = (Student) user;
            System.out.println();
            StudentTextUI.courseHistory(student);
        }

        // if the user is a faculty, list all courses the user is teaching
        else if (user.getRole().equals(Constants.ROLE[1])) {
            Faculty faculty = (Faculty) user;
            System.out.println();
            FacultyTextUI.coursesCurrentlyTeaching(faculty);
        }

        while (!exitMenu) {

            String fieldToModify;
            System.out.println();
            System.out.println("    +-----------------------------+");
            System.out.println("    |      " + Constants.colorWrapper("ACCOUNT SETTING", Constants.WHITE_BOLD) + "  " +
                    "      |");
            System.out.println("    +-----------------------------+");
            System.out.println("    |   1. Modify username        |");
            System.out.println("    |   2. Modify full name       |");
            System.out.println("    |   3. Modify gender          |");
            System.out.println("    |   4. Modify address         |");
            System.out.println("    |   5. Modify phone number    |");
            System.out.println("    |   6. Modify date of birth   |");
            System.out.println("    |   7. Modify email address   |");
            System.out.println("    |   8. Modify password        |");
            System.out.println("    |   9. Return                 |");
            System.out.println("    +-----------------------------+");
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
                    modifyFullName(user);
                    break;
                case "3":
                    modifyGender(user);
                    break;
                case "4":
                    modifyAddress(user);
                    break;
                case "5":
                    modifyPhoneNumber(user);
                    break;
                case "6":
                    modifyDateOfBirth(user);
                    break;
                case "7":
                    modifyEmailAddress(user);
                    break;
                case "8":
                    modifyPassword(user);
                    break;
                case "9":
                    exitMenu = true;
                    break;
                default:
                    System.out.println(Constants.INVALID_INPUT);
                    Constants.pressEnterToContinue();
                    System.out.println();
            }

            // print the new user information if 1 <= selected number <= 9
            for (int i = 1; i <= 8; ++i) {
                if (fieldToModify.equals(String.valueOf(i))) {
                    System.out.println();
                    System.out.println(Database.accounts.get(user.getUuid()));
                    System.out.println();
                    // Log
                    Log.updateAnAccount(new Timestamp(System.currentTimeMillis()), MenuTextUI.uuid, user.getUuid());
                    Constants.pressEnterToContinue();
                    break;
                }
            }
        }
    }

    /**
     * The method allows for the admin to check the filepath of all available databases.
     *
     * @date 2019/04/11
     */
    private static void showDatabaseFilePath() {
        System.out.println();
        System.out.println(Constants.colorWrapper(Database.ACCOUNT_DATA, Constants.GREEN));
        System.out.println(Constants.colorWrapper(Database.COURSES, Constants.GREEN));
        System.out.println(Constants.colorWrapper(Database.ACADEMIC_REQUIREMENTS, Constants.GREEN));
        System.out.println(Constants.colorWrapper(Database.INTERNSHIP_REQUIREMENTS, Constants.GREEN));
        System.out.println(Constants.colorWrapper(Database.STUDENTS, Constants.GREEN));
        System.out.println(Constants.colorWrapper(Database.FACULTY, Constants.GREEN));
        System.out.println();
    }

    /**
     * The method allows for the admin to check the history of operations of all users.
     *
     * @date 2019/04/11
     */
    private void checkHistoryLog() {

        String select;
        System.out.println();
        boolean exitSubMenu = false;

        while (!exitSubMenu) {
            System.out.println(Constants.colorWrapper("   HISTORY LOG PANEL", Constants.WHITE_BOLD));
            System.out.println();
            System.out.println("   1. Check all history");
            System.out.println("   2. Search by keyword");
            System.out.println("   3. Return");
            System.out.println();
            System.out.print(Constants.colorWrapper("Select Number: ", Constants.WHITE_BOLD));
            select = Constants.IN.nextLine();

            switch (select) {
                case "1":
                    System.out.println();
                    for (String i : Log.logArray) {
                        System.out.println(i);
                    }
                    System.out.println();
                    Constants.pressEnterToContinue();
                    System.out.println();
                    break;
                case "2":
                    String keyword;
                    System.out.print(Constants.colorWrapper("Keyword: ", Constants.WHITE_BOLD));
                    keyword = Constants.IN.nextLine();
                    if (keyword.isEmpty()) {
                        System.out.println(Constants.INVALID_INPUT);
                        System.out.println();
                    } else {
                        System.out.println();
                        for (String i : Log.logArray) {
                            if (i.contains(keyword)) {
                                // highlight the keyword
                                System.out.println(colorHighlightedSubstring(i, keyword));
                            }
                        }
                        System.out.println();
                        Constants.pressEnterToContinue();
                        System.out.println();
                    }
                    break;
                case "3":
                    exitSubMenu = true;
                    break;
                default:
                    System.out.println(Constants.INVALID_INPUT);
                    System.out.println();
                    break;
            }
        }
    }

    /**
     * The method displays all the accounts in the database.
     *
     * @date 2019/04/11
     */
    private void listAllAccounts() {
        int count = 0;
        for (Map.Entry<String, User> i : Database.accounts.entrySet()) {
            System.out.println(i.getValue());
            System.out.println();
            if (count == Constants.MAX_ITEMS_SCROLL_FORWARD) {
                Constants.displayLineSeparator();
                count = 1;
            } else {
                ++count;
            }
        }
        System.out.println("TOTAL ACCOUNTS: " + Database.accounts.size());
        System.out.println();
        Constants.pressEnterToContinue();
        System.out.println();
    }

    /**
     * The method allows for the admin to modify different aspects of a course.
     * The course must exist in the database for the user to do so.
     *
     * @date 2019/04/11
     */
    private void modifyACourse() {
        System.out.print(Constants.colorWrapper("Course id: ", Constants.WHITE_BOLD));
        String courseId = Constants.IN.nextLine();
        if (Database.courses.containsKey(courseId)) {

            System.out.println();
            System.out.println(Database.courses.get(courseId));
            boolean exitMenu = false;

            while (!exitMenu) {

                String fieldToModify;
                System.out.println();
                System.out.println("    +------------------------------------+");
                System.out.println("    |           " + Constants.colorWrapper("COURSE SETTING", Constants.WHITE_BOLD) + "           |");
                System.out.println("    +------------------------------------+");
                System.out.println("    |   1. Modify course name            |");
                System.out.println("    |   2. Modify course description     |");
                System.out.println("    |   3. Modify course prerequisites   |");
                System.out.println("    |   4. Modify course antirequisites  |");
                System.out.println("    |   5. Modify course repeats         |");
                System.out.println("    |   6. Return                        |");
                System.out.println("    +------------------------------------+");
                System.out.println();
                System.out.println(Constants.colorWrapper(" Note : Invalid value will remain the original data " +
                        "unchanged!", Constants.YELLOW_BOLD));
                System.out.println();
                System.out.print(Constants.colorWrapper("Select number: ", Constants.WHITE_BOLD));
                fieldToModify = Constants.IN.nextLine();

                switch (fieldToModify) {
                    case "1":
                        System.out.print(Constants.colorWrapper("New COURSE NAME: ", Constants.WHITE_BOLD));
                        String newCourseName = Constants.IN.nextLine();
                        Database.courses.get(courseId).setCourseName(newCourseName);
                        Database.courseUpdateCommit(courseId, Constants.COURSE_FIELD.COURSE_NAME);
                        break;
                    case "2":
                        System.out.print(Constants.colorWrapper("New COURSE DESCRIPTION: ", Constants.WHITE_BOLD));
                        String newCourseDescription = Constants.IN.nextLine();
                        Database.courses.get(courseId).setCourseDescription(newCourseDescription);
                        Database.courseUpdateCommit(courseId, Constants.COURSE_FIELD.COURSE_DESCRIPTION);
                        break;
                    case "3":
                        System.out.print(Constants.colorWrapper("New PREREQUISITES (Format: A B C): ",
                                Constants.WHITE_BOLD));
                        String newPrerequisites = Constants.IN.nextLine();

                        // new prerequisites cannot contain course id which are already in the anti-requisites
                        HashSet<String> temp0 = Course.parser(newPrerequisites);
                        HashSet<String> temp1 = Database.courses.get(courseId).getAntirequisites();
                        boolean noCommonCourses0 = true;
                        for (String i : temp0) {
                            if (temp1.contains(i)) {
                                noCommonCourses0 = false;
                                System.out.print(Constants.colorWrapper(i + " is already " +
                                        "in the anti-requisites ", Constants.RED));
                                System.out.println();
                                break;
                            }
                        }
                        if (noCommonCourses0) {
                            Database.courses.get(courseId).setPrerequisitesAsNew(newPrerequisites);
                            Database.courseUpdateCommit(courseId, Constants.COURSE_FIELD.PREREQUISITES);
                        }

                        break;
                    case "4":
                        System.out.print(Constants.colorWrapper("New ANTIREQUISITES (Format: A B C): ", Constants.WHITE_BOLD));
                        String newAntirequisites = Constants.IN.nextLine();

                        // new antirequisites cannot contain course id which are already in the prerequisites
                        HashSet<String> temp2 = Course.parser(newAntirequisites);
                        HashSet<String> temp3 = Database.courses.get(courseId).getPrerequisites();
                        boolean noCommonCourses1 = true;
                        for (String i : temp2) {
                            if (temp3.contains(i)) {
                                noCommonCourses1 = false;
                                System.out.print(Constants.colorWrapper(i + " is already " +
                                        "in the prerequisites ", Constants.RED));
                                System.out.println();
                                break;
                            }
                        }
                        if (noCommonCourses1) {
                            Database.courses.get(courseId).setAntirequisitesAsNew(newAntirequisites);
                            Database.courseUpdateCommit(courseId, Constants.COURSE_FIELD.ANTIREQUISITES);
                        }

                        break;
                    case "5":
                        System.out.print(Constants.colorWrapper("Can the course be repeated for GPA? ",
                                Constants.WHITE_BOLD) + " (YES/NO) ");
                        String newCanBeRepeated = Constants.IN.nextLine();
                        Database.courses.get(courseId).setCanBeRepeated(newCanBeRepeated);
                        Database.courseUpdateCommit(courseId, Constants.COURSE_FIELD.CAN_BE_REPEATED);
                        break;
                    case "6":
                        exitMenu = true;
                        break;
                    default:
                        System.out.println(Constants.INVALID_INPUT);
                        Constants.pressEnterToContinue();
                }

                // print course info after the modification if the user chose from 1-5
                for (int i = 1; i <= 5; ++i) {
                    if (fieldToModify.equals(String.valueOf(i))) {
                        System.out.println();
                        System.out.println(Database.courses.get(courseId));
                        System.out.println();
                        // log
                        Log.updateACourse(new Timestamp(System.currentTimeMillis()), MenuTextUI.uuid, courseId);
                        Constants.pressEnterToContinue();
                        break;
                    }
                }
            }
        } else {
            System.out.println(Constants.colorWrapper("Course id does not exist!", Constants.RED));
            Constants.pressEnterToContinue();
            System.out.println();
        }
    }

    /**
     * The method allows for the admin to create an account.
     * If all aspects of the new account is valid, then the account would be created.
     *
     * @date 2019/04/11
     */
    private void createAnAccount() {
        String createUuid = RandomUserGenerator.createRandomUuid();
        // prompt user to input the info, iterate until the input is valid.
        String createUsername;
        do {
            System.out.print(Constants.colorWrapper("USERNAME: ", Constants.WHITE_BOLD));
            createUsername = Constants.IN.nextLine();
        } while (!Validation.isUsernameValid(createUsername));

        String createFullName;
        do {
            System.out.print(Constants.colorWrapper("FULL NAME: ", Constants.WHITE_BOLD));
            createFullName = Constants.IN.nextLine();
        } while (!Validation.isFullNameValid(createFullName));

        String createRole;
        do {
            System.out.print(Constants.colorWrapper("ROLE: ", Constants.WHITE_BOLD));
            createRole = Constants.IN.nextLine();
        } while (!Validation.isRoleValid(createRole));

        String createGender;
        do {
            System.out.print(Constants.colorWrapper("GENDER: ", Constants.WHITE_BOLD));
            createGender = Constants.IN.nextLine();
        } while (!Validation.isGenderValid(createGender));

        System.out.println(Constants.colorWrapper("DATE OF BIRTH", Constants.WHITE_BOLD) + ": ");
        String createYear;
        do {
            System.out.print("YEAR: ");
            createYear = Constants.IN.nextLine();
        } while (!Validation.isYearValid(createYear));
        String createMonth;
        do {
            System.out.print("MONTH: ");
            createMonth = Constants.IN.nextLine();
        } while (!Validation.isMonthValid(createMonth));
        String createDay;
        do {
            System.out.print("DAY: ");
            createDay = Constants.IN.nextLine();
        } while (!Validation.isDayValid(createYear, createMonth, createDay));
        DateOfBirth newDateOfBirth = new DateOfBirth(createYear, createMonth, createDay);

        String createEmailAddress;
        do {
            System.out.print(Constants.colorWrapper("EMAIL ADDRESS: ", Constants.WHITE_BOLD));
            createEmailAddress = Constants.IN.nextLine();
        } while (!Validation.isEmailAddressValid(createEmailAddress));

        String createPhoneNumber;
        do {
            System.out.print(Constants.colorWrapper("PHONE NUMBER", Constants.WHITE_BOLD) + " (Format: ###-###-####)" +
                    ": ");
            createPhoneNumber = Constants.IN.nextLine();
        } while (!Validation.isPhoneNumberValid(createPhoneNumber));

        String createAddress;
        do {
            System.out.print(Constants.colorWrapper("ADDRESS: ", Constants.WHITE_BOLD));
            createAddress = Constants.IN.nextLine();
        } while (!Validation.isAddressValid(createAddress));

        String createPassword;
        String createPasswordAgain;
        while (true) {
            createPassword = MenuTextUI.enterPassword(Constants.colorWrapper("PASSWORD: ", Constants.WHITE_BOLD));
            if (Validation.isPasswordValid(createPassword)) {
                createPasswordAgain = MenuTextUI.enterPassword(Constants.colorWrapper("PASSWORD AGAIN: ",
                        Constants.WHITE_BOLD));
                if (!createPasswordAgain.equals(createPassword)) {
                    System.out.println(Constants.colorWrapper("Password not matched!", Constants.RED));
                } else {
                    break;
                }
            }
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

        // commit to the database
        Database.usernameToUuid.put(createUsername, createUuid);
        Database.accounts.put(createUuid, createUser);
        Database.createAccountCommit(createUser);

        System.out.println();
        System.out.println(Database.accounts.get(createUuid));
        System.out.println();
        System.out.println(Constants.colorWrapper("New account successfully created!", Constants.GREEN_BOLD));
        System.out.println();

        // log
        Log.addAnAccount(new Timestamp(System.currentTimeMillis()), MenuTextUI.uuid, createUuid);
        Constants.pressEnterToContinue();
        System.out.println();
    }

    /**
     * The method allows for the user to create a course.
     * If all aspects of the course are valid then the course would be created.
     *
     * @date 2019/04/11
     */
    private void createACourse() {
        // prompt user to input the info, iterate until the input is valid.
        String createCourseId;
        do {
            System.out.print(Constants.colorWrapper("COURSE ID: ", Constants.WHITE_BOLD));
            createCourseId = Constants.IN.nextLine();
        } while (!Validation.isCourseIdValid(createCourseId));
        String createCourseName;
        do {
            System.out.print(Constants.colorWrapper("COURSE NAME: ", Constants.WHITE_BOLD));
            createCourseName = Constants.IN.nextLine();
        } while (!Validation.isCourseNameValid(createCourseName));
        String createCourseDescription;
        do {
            System.out.print(Constants.colorWrapper(
                    "COURSE DESCRIPTION: ", Constants.WHITE_BOLD));
            createCourseDescription = Constants.IN.nextLine();
        } while (!Validation.isCourseDescriptionValid(createCourseDescription));
        String createCourseUnits;
        do {
            System.out.print(Constants.colorWrapper("COURSE UNITS: ", Constants.WHITE_BOLD));
            createCourseUnits = Constants.IN.nextLine();
        } while (!Validation.isCourseUnitsValid(createCourseUnits));


        String createPrerequisites;
        String createAntirequisites;
        while (true) {
            boolean containCommonCourses = false;
            do {
                System.out.print(Constants
                        .colorWrapper("COURSE PREREQUISITES  (Format: A B C): ", Constants.WHITE_BOLD));
                createPrerequisites = Constants.IN.nextLine();
            } while (!Validation.isPrerequisitesValid(createPrerequisites));
            do {
                System.out.print(Constants
                        .colorWrapper("COURSE ANTIREQUISITES  (Format: A B C): ", Constants.WHITE_BOLD));
                createAntirequisites = Constants.IN.nextLine();
            } while (!Validation.isPrerequisitesValid(createAntirequisites));

            // make sure prerequisites and anti-requisites do not contain common courses
            HashSet<String> prerequisitesSet  = Course.parser(createPrerequisites);
            HashSet<String> antirequisitesSet = Course.parser(createAntirequisites);
            for (String i : prerequisitesSet) {
                if (antirequisitesSet.contains(i)) {
                    System.out.print(Constants.colorWrapper("Both prerequisites and anti-requisites contain a same " +
                                    "course id!",
                            Constants.RED));
                    containCommonCourses = true;
                    break;
                }
            }
            if (!containCommonCourses) {
                break;
            }
        }

        String canBeRepeatedForGPA;
        do {
            System.out.print(Constants.colorWrapper("Can be repeated for GPA? ", Constants.WHITE_BOLD) + "(YES/NO) ");
            canBeRepeatedForGPA = Constants.IN.nextLine();
        } while (!Validation.isCanBeRepeatedValid(canBeRepeatedForGPA));

        // create the course object
        Course createCourse = new Course();
        createCourse.setCourseId(createCourseId);
        createCourse.setCourseName(createCourseName);
        createCourse.setCourseDescription(createCourseDescription);
        createCourse.setCourseUnits(createCourseUnits);
        createCourse.setPrerequisitesAsNew(createPrerequisites);
        createCourse.setAntirequisitesAsNew(createAntirequisites);
        createCourse.setCanBeRepeated(canBeRepeatedForGPA);
        Database.courses.put(createCourseId, createCourse);
        Database.createCourseCommit(createCourse);

        // commit to the database
        StringBuilder extraInformation = new StringBuilder(" + It is ");
        String        mandatoryOrOptional;
        // ask the admin it is mandatory or optional for academic requirements
        while (true) {
            System.out.print("Is the course mandatory or optional for academic requirements? " +
                    "(MANDATORY/OPTIONAL) ");
            mandatoryOrOptional = Constants.IN.nextLine();
            if (mandatoryOrOptional.equals(Constants.COURSES_TYPE[0])) {
                Database.mandatoryCourses.add(createCourseId);
                Database.createCourseCommitToRequirements(createCourseId, true, true);
                extraInformation.append("MANDATORY for academic requirements.");
                break;

            } else if (mandatoryOrOptional.equals(Constants.COURSES_TYPE[1])) {
                Database.optionalCourses.add(createCourseId);
                Database.createCourseCommitToRequirements(createCourseId, true, false);
                extraInformation.append("OPTIONAL for academic requirements.");
                break;
            } else {
                System.out.println(Constants.INVALID_INPUT);
            }
        }
        extraInformation.append("\n");
        extraInformation.append(" + It is ");

        // ask the admin it is mandatory or optional for internship requirements
        boolean exitSubOption = false;
        while (!exitSubOption) {
            System.out.print("Is the course mandatory or optional for internship requirements? " +
                    "(MANDATORY/OPTIONAL/NONE) ");
            mandatoryOrOptional = Constants.IN.nextLine();
            switch (mandatoryOrOptional) {
                case "MANDATORY":
                    Database.mandatoryCoursesForInternship.add(createCourseId);
                    Database.createCourseCommitToRequirements(createCourseId, false, true);
                    extraInformation.append("MANDATORY for internship requirements.");
                    exitSubOption = true;
                    break;
                case "OPTIONAL":
                    Database.optionalCourses.add(createCourseId);
                    Database.createCourseCommitToRequirements(createCourseId, false, false);
                    extraInformation.append("OPTIONAL for internship requirements.\n");
                    exitSubOption = true;
                    break;
                case "NONE":
                    extraInformation.append("NOT a part of internship requirements.\n");
                    exitSubOption = true;
                    break;
                default:
                    System.out.println(Constants.INVALID_INPUT);
                    break;
            }
        }

        System.out.println();
        System.out.println(Database.courses.get(createCourseId));
        System.out.println();
        System.out.println(extraInformation);
        System.out.println(Constants.colorWrapper("New course successfully created!", Constants.GREEN_BOLD));
        System.out.println();

        // log
        Log.addACourse(new Timestamp(System.currentTimeMillis()), MenuTextUI.uuid, createCourseId);

        Constants.pressEnterToContinue();
        System.out.println();
    }

    /**
     * The method allows for user to remove an account.
     * The account must exist in the database for the user to do so.
     * The account that the user is deleting cannot be themselves.
     *
     * @param adminUsername a String containing the username of the admin user.
     * @date 2019/04/11
     */
    private void removeAnAccount(String adminUsername) {
        System.out.print(Constants.colorWrapper("Account username: ", Constants.WHITE_BOLD));
        String username = Constants.IN.nextLine();
        if (username.equals(adminUsername)) {
            System.out.println(Constants.colorWrapper("Cannot delete the user itself!", Constants.RED));
            Constants.pressEnterToContinue();
        } else if (Database.usernameToUuid.containsKey(username)) {
            String uuid;
            uuid = Database.usernameToUuid.get(username);

            System.out.println();
            System.out.println(Database.accounts.get(uuid));
            System.out.println();

            // update the database and containers
            Database.accountRemovalCommit(uuid);
            Database.accounts.remove(uuid);
            Database.usernameToUuid.remove(username);

            System.out.println(Constants.colorWrapper("Account successfully removed!", Constants.GREEN_BOLD));

            // log
            Log.removeAnAccount(new Timestamp(System.currentTimeMillis()), MenuTextUI.uuid, uuid);

        } else {
            System.out.println(Constants.colorWrapper("Account does not exist!", Constants.RED));
            Constants.pressEnterToContinue();
        }
        System.out.println();
    }

    /**
     * The method allows for user to remove a course. The course must exist in the database for the user to do so.
     *
     * @date 2019/04/11
     */
    private void removeACourse() {
        System.out.print("Course id: ");
        String courseId = Constants.IN.nextLine();
        // the admin cannot remove a course id which does not exist
        if (Database.courses.containsKey(courseId)) {

            // the admin can only remove a course which is not taken by student or taught by any faculty
            if (Database.courseCurrentlyTaught.contains(courseId)) {
                System.out.println(Constants.colorWrapper("Cannot remove a course which is currently being taught!",
                        Constants.RED));
                System.out.println();
                return;
            }

            // the admin cannot remove a course which is a prerequisite or anti-requisite of other courses
            for (Course i : Database.courses.values()) {
                // pass itself in the iteration
                if (i.getCourseId().equals(courseId)) {
                    continue;
                }
                if (i.getPrerequisites().contains(courseId)) {
                    System.out.println(Constants.colorWrapper("Cannot remove a course since it is a prerequisite of " + i.getCourseId() + "!",
                            Constants.RED));
                    System.out.println();
                    return;
                }
                if (i.getAntirequisites().contains(courseId)) {
                    System.out.println(Constants.colorWrapper("Cannot remove a course since it is a anti-requisite of" +
                                    " " + i.getCourseId() + "!",
                            Constants.RED));
                    System.out.println();
                    return;
                }
            }

            // print the course info before removing it
            System.out.println();
            System.out.println(Database.courses.get(courseId));

            // update the database and containers
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
            System.out.println(Constants.colorWrapper("Course successfully removed!", Constants.GREEN_BOLD));

            // log
            Log.removeACourse(new Timestamp(System.currentTimeMillis()), MenuTextUI.uuid, courseId);

        } else {
            System.out.println(Constants.colorWrapper("Course id does not exist!", Constants.RED));
            Constants.pressEnterToContinue();
        }
        System.out.println();
    }
}

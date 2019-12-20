import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * The {@code AdminTextUI} class contains static methods used specifically for Administrators in the the TextUI.
 */

public class AdminTextUI extends TextUI {

    /**
     * Constructor that initializes the admin user in the database and displays the main admin account page in
     * text-mode.
     *
     * @param uuid a string containing the uuid of the admin user.
     */
    public AdminTextUI(String uuid) {

        super(uuid);
        Admin user = (Admin) Database.accounts.get(uuid);

        String select;

        while (true) {

            System.out.println("    +------------------------------------+");
            System.out.println("    |          " + Constants.COLOR_WRAPPER("CONTROL  PANEL", Constants.WHITE_BOLD) +
                    "            |");
            System.out.println("    |------------------------------------|");
            System.out.println("    |   1. List all available accounts   |");
            System.out.println("    |   2. List all available courses    |");
            System.out.println("    |   3. Search a course               |");
            System.out.println("    |   4. Search an account             |");
            System.out.println("    |   5. Modify a course               |");
            System.out.println("    |   6. Modify an account             |");
            System.out.println("    |   7. Enrollment Management         |");
            System.out.println("    |   8. Create an account             |");
            System.out.println("    |   9. Create a course               |");
            System.out.println("    |  10. Remove an account             |");
            System.out.println("    |  11. Remove a course               |");
            System.out.println("    |  12. Personal information          |");
            System.out.println("    |  13. History                       |");
            System.out.println("    |  14. Logout                        |");
            System.out.println("    +------------------------------------+");

            System.out.println();
            System.out.print(Constants.COLOR_WRAPPER("Select number: ", Constants.WHITE_BOLD));
            select = Constants.IN.nextLine();

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
                    System.out.println();
                    showAccountInformation(user);
                    break;

                // Check history log
                case "13":
                    break;

                // Return to the main menu
                case "14":
                    return;
                default:
                    System.out.println(Constants.INVALID_INPUT);
                    System.out.println();
            }
        }
    }

    /**
     * Allows for the admin user to search accounts by role.
     *
     * @param keyword a String containing the keyword that the user inputs/searches.
     * @return an arrayList containing the results of the search.
     */
    public static ArrayList<User> searchRolesAndSort(String keyword) {

        ArrayList<User> result = new ArrayList<>();

        if (Validation.isRoleValid(keyword)) {

            // search
            for (Map.Entry<String, User> i : Database.accounts.entrySet()) {
                if (i.getValue().getRole().equals(keyword)) {
                    result.add(i.getValue());
                }
            }

            // sort
            Collections.sort(result);
        }
        return result;
    }

    /**
     * Allows for the admin to seach accounts by gender.
     *
     * @param keyword a String containing the keyword that the user inputs/searches.
     * @return an arrayList containing the results of the search.
     */
    public static ArrayList<User> searchGenderAndSort(String keyword) {

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
     * Allows for the admin user to search accounts by username.
     *
     * @param keyword a String containing the keyword that the user inputs/searches.
     * @return an arrayList containing the results of the search.
     */
    public static ArrayList<User> searchUsernameAndSort(String keyword) {

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
     * Allows for the admin user to search accounts by full names.
     *
     * @param keyword a String containing the keyword that the user inputs/searches.
     * @return an arraylist containing the results of the search.
     */
    public static ArrayList<User> searchFullNameAndSort(String keyword) {

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
     * Allows for the admin user to search accounts by uuid.
     *
     * @param keyword a String containing the keyword that the user inputs/searches.
     * @return an arrayList containing the results of the search.
     */
    public static ArrayList<User> searchUuidAndSort(String keyword) {

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
     * Allows for the admin user to search accounts by email.
     *
     * @param keyword a String containing the keyword that the user inputs/searches.
     * @return an arrayList containing the results of the search.
     */
    public static ArrayList<User> searchEmailAndSort(String keyword) {

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
     * Allows for the admin user to search accounts by address.
     *
     * @param keyword a String containing the keyword that the user inputs/searches.
     * @return an arrayList containing the results of the search.
     */
    public static ArrayList<User> searchAddressAndSort(String keyword) {

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
     * Allows for the admin user to search accounts by phone number.
     *
     * @param keyword a String containing the keyword that the user inputs/searches.
     * @return an arrayList containing the results of the search.
     */
    public static ArrayList<User> searchPhoneNumberAndSort(String keyword) {

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
     * Allows for the admin user to search accounts by date of birth.
     *
     * @param keyword a String containing the keyword that the user inputs/searches.
     * @return an arrayList containing the results of the search.
     */
    public static ArrayList<User> searchDateOfBirthAndSort(String keyword) {

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
     * The menu that allows the admin user to search for accounts through various different ways.
     * The different options to do so are displayed and depending on what the user selects, corresponding results would
     * be displayed.
     */
    public static void searchAccounts() {

        boolean exitSubMenu = false;
        while (!exitSubMenu) {

            System.out.println("    +-----------------------------+");
            System.out.println("    |       " + Constants.COLOR_WRAPPER("SEARCH SETTING", Constants.WHITE_BOLD) + "        |");
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
            System.out.print(Constants.COLOR_WRAPPER("Select Number: ", Constants.WHITE_BOLD));
            String          select = Constants.IN.nextLine();
            ArrayList<User> result;
            String          keyword;
            int             count  = 0;

            switch (select) {
                case "1":
                    System.out.print(Constants.COLOR_WRAPPER("Find username: ", Constants.WHITE_BOLD));
                    keyword = Constants.IN.nextLine();
                    result = searchUsernameAndSort(keyword);

                    // output
                    if (!result.isEmpty()) {
                        for (User i : result) {
                            System.out.println();
                            System.out.println(i.toStringWithHighLight(Constants.USER_FIELD.USERNAME, keyword));
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
                        System.out.println(Constants.COLOR_WRAPPER("Account does not exist!", Constants.RED));
                    }
                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    System.out.println();
                    break;
                case "2":
                    System.out.print(Constants.COLOR_WRAPPER("Find role: ", Constants.WHITE_BOLD) + "(ADMIN/FACULTY" +
                            "/STUDENT) ");
                    keyword = Constants.IN.nextLine();
                    result = searchRolesAndSort(keyword);

                    // output
                    if (!result.isEmpty()) {
                        for (User i : result) {
                            System.out.println();
                            System.out.println(i.toStringWithHighLight(Constants.USER_FIELD.ROLE, keyword));
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
                        System.out.println(Constants.COLOR_WRAPPER("Account does not exist!", Constants.RED));
                    }
                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    System.out.println();
                    break;
                case "3":
                    System.out.print(Constants.COLOR_WRAPPER("Find gender: ", Constants.WHITE_BOLD) + "(M/F) ");
                    keyword = Constants.IN.nextLine();
                    result = searchGenderAndSort(keyword);

                    // output
                    if (!result.isEmpty()) {
                        for (User i : result) {
                            System.out.println();
                            System.out.println(i.toStringWithHighLight(Constants.USER_FIELD.GENDER, keyword));
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
                        System.out.println(Constants.COLOR_WRAPPER("Account does not exist!", Constants.RED));
                    }
                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    System.out.println();
                    break;
                case "4":
                    // Find by full name
                    System.out.print(Constants.COLOR_WRAPPER("Find full name: ", Constants.WHITE_BOLD));
                    keyword = Constants.IN.nextLine();
                    result = searchFullNameAndSort(keyword);

                    // output
                    if (!result.isEmpty()) {
                        for (User i : result) {
                            System.out.println();
                            System.out.println(i.toStringWithHighLight(Constants.USER_FIELD.FULLNAME, keyword));
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
                        System.out.println(Constants.COLOR_WRAPPER("Account does not exist!", Constants.RED));
                    }
                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    System.out.println();
                    break;
                case "5":
                    // Find by uuid
                    System.out.print(Constants.COLOR_WRAPPER("Find UUID: ", Constants.WHITE_BOLD));
                    keyword = Constants.IN.nextLine();
                    result = searchUuidAndSort(keyword);

                    // output
                    if (!result.isEmpty()) {
                        for (User i : result) {
                            System.out.println();
                            System.out.println(i.toStringWithHighLight(Constants.USER_FIELD.UUID, keyword));
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
                        System.out.println(Constants.COLOR_WRAPPER("Account does not exist!", Constants.RED));
                    }
                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    System.out.println();
                    break;
                case "6":
                    // Find by email
                    System.out.print(Constants.COLOR_WRAPPER("Find email address: ", Constants.WHITE_BOLD));
                    keyword = Constants.IN.nextLine();
                    result = searchEmailAndSort(keyword);

                    // output
                    if (!result.isEmpty()) {
                        for (User i : result) {
                            System.out.println();
                            System.out.println(i.toStringWithHighLight(Constants.USER_FIELD.EMAIL, keyword));
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
                        System.out.println(Constants.COLOR_WRAPPER("Account does not exist!", Constants.RED));
                    }
                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    System.out.println();
                    break;
                case "7":
                    // Find by address
                    System.out.print(Constants.COLOR_WRAPPER("Find address: ", Constants.WHITE_BOLD));
                    keyword = Constants.IN.nextLine();
                    result = searchAddressAndSort(keyword);

                    // output
                    if (!result.isEmpty()) {
                        for (User i : result) {
                            System.out.println();
                            System.out.println(i.toStringWithHighLight(Constants.USER_FIELD.ADDRESS, keyword));
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
                        System.out.println(Constants.COLOR_WRAPPER("Account does not exist!", Constants.RED));
                    }
                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    System.out.println();
                    break;
                case "8":
                    // Find by phone number
                    System.out.print(Constants.COLOR_WRAPPER("Find phone number: ", Constants.WHITE_BOLD));
                    keyword = Constants.IN.nextLine();
                    result = searchPhoneNumberAndSort(keyword);

                    // output
                    if (!result.isEmpty()) {
                        for (User i : result) {
                            System.out.println();
                            System.out.println(i.toStringWithHighLight(Constants.USER_FIELD.PHONE, keyword));
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
                        System.out.println(Constants.COLOR_WRAPPER("Account does not exist!", Constants.RED));
                    }
                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    System.out.println();
                    break;
                case "9":
                    // Find by date of birth
                    System.out.print(Constants.COLOR_WRAPPER("Find date of birth: ", Constants.WHITE_BOLD));
                    keyword = Constants.IN.nextLine();
                    result = searchDateOfBirthAndSort(keyword);

                    // output
                    if (!result.isEmpty()) {
                        for (User i : result) {
                            System.out.println();
                            System.out.println(i.toStringWithHighLight(Constants.USER_FIELD.DATE_OF_BIRTH, keyword));
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
                        System.out.println(Constants.COLOR_WRAPPER("Account does not exist!", Constants.RED));
                    }
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
    }

    /**
     * Allows for the user to modify an account.
     * Pre-cond: The account must exist in the database for the user to do so. If the account does exist, the user
     * can choose which property of the account to modify.
     */
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
            System.out.println("    +----------------------------+");
            System.out.println("    |      " + Constants.COLOR_WRAPPER("ACCOUNT SETTING", Constants.WHITE_BOLD) + "  " +
                    "     |");
            System.out.println("    +----------------------------+");
            System.out.println("    |   1. Modify username       |");
            System.out.println("    |   2. Modify full name      |");
            System.out.println("    |   3. Modify role           |");
            System.out.println("    |   4. Modify gender         |");
            System.out.println("    |   5. Modify address        |");
            System.out.println("    |   6. Modify phone number   |");
            System.out.println("    |   7. Modify date of birth  |");
            System.out.println("    |   8. Modify email address  |");
            System.out.println("    |   9. Modify password       |");
            System.out.println("    |  10. Return                |");
            System.out.println("    +----------------------------+");
            System.out.println();
            System.out.println(Constants.COLOR_WRAPPER(" Note : Invalid value will remain the original data " +
                    "unchanged!", Constants.YELLOW_BOLD));
            System.out.println();

            System.out.print(Constants.COLOR_WRAPPER("Select number: ", Constants.WHITE_BOLD));
            fieldToModify = Constants.IN.nextLine();

            switch (fieldToModify) {
                case "1":
                    modifyUsername(user);
                    break;
                case "2":
                    modifyFullName(user);
                    break;
                case "3":
                    modifyRole(user);
                    break;
                case "4":
                    modifyGender(user);
                    break;
                case "5":
                    modifyAddress(user);
                    break;
                case "6":
                    modifyPhoneNumber(user);
                    break;
                case "7":
                    modifyDateOfBirth(user);
                    break;
                case "8":
                    modifyEmailAddress(user);
                    break;
                case "9":
                    modifyPassword(user);
                    break;
                case "10":
                    exitMenu = true;
                    break;
                default:
                    System.out.println(Constants.INVALID_INPUT);
                    Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                    System.out.println();
            }

            // print the new user information if 1 <= selected number <= 9
            for (int i = 1; i <= 9; ++i) {
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

    /**
     * Allows for the user to create a course schedule. A schedule assigns the course id and the username of the
     * faculty who is going to teach the course, for the enrollment.
     * The course id, and faculty username that the user inputs must exist in the database for the user to do so.
     */
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

            System.out.print("Faculty username: ");
            facultyUsername = Constants.IN.nextLine();

            if (!Database.usernameToUuid.containsKey(facultyUsername)) {
                System.out.println(Constants.COLOR_WRAPPER("User does not exist!", Constants.RED));
                continue;
            }

            User tempUser = Database.accounts.get(Database.usernameToUuid.get(facultyUsername));
            if (!tempUser.getRole().equals(Constants.ROLE[1])) {
                System.out.println(Constants.COLOR_WRAPPER("User is not a faculty!", Constants.RED));
                continue;
            }

            if (!Database.courses.get(courseId).getStudentsWhoAreTaking().isEmpty()) {
                System.out.println(Constants.COLOR_WRAPPER("The course is currently being taught!", Constants.RED));
                continue;
            }


            if (Database.coursesOpen.containsKey(courseId)) {
                System.out.println(Constants.COLOR_WRAPPER("The course is already scheduled!", Constants.RED));
                continue;
            }

            break;
        }

        // update the corresponding user's COURSES_TEACHING in FACULTY.db
        Faculty faculty = (Faculty) Database.accounts.get(Database.usernameToUuid.get(facultyUsername));
        faculty.addCoursesTeaching(courseId);
        String newCoursesTeaching = "";

        ArrayList<String> temp = faculty.getCoursesTeaching();
        for (String i : temp) {
            newCoursesTeaching = newCoursesTeaching + i + " ";
        }

        newCoursesTeaching = newCoursesTeaching.trim();
        Database.facultyAddCoursesTeachingCommit(faculty.getUuid(), newCoursesTeaching);

        // update Database.courseOpen
        Database.coursesOpen.put(courseId, faculty.getFullName());
        Database.coursesOpenCommit(courseId, faculty.getUuid());

        // show a message indicate the schedule is successfully created
        System.out.println(Constants.COLOR_WRAPPER("Schedule successfully created!", Constants.GREEN_BOLD));
        System.out.println();
        Constants.PRESS_ENTER_KEY_TO_CONTINUE();

    }

    /**
     * ALlows for the user to manage enrollment.
     * Three options are displayed, and depending on what the user chooses, the corresponding methods are called.
     */
    public static void enrollmentManagement() {

        String  select;
        boolean exitMenu = false;

        while (!exitMenu) {

            System.out.println();
            System.out.println("    +-----------------------------------+");
            System.out.println("    |       " + Constants.COLOR_WRAPPER("ENROLLMENT MANAGEMENT", Constants.WHITE_BOLD) + "       |");
            System.out.println("    +-----------------------------------+");
            System.out.println("    |   1. Create a course schedule     |");
            System.out.println("    |   2. Modify status of enrollment  |");
            System.out.println("    |   3. Return                       |");
            System.out.println("    +-----------------------------------+");
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

    /**
     * Allows for the user to modify enrollment status (Open or Close).
     */
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

                    // clean all items in COURSES_OPEN.db when the enrollment is closed
                    Database.cleanCoursesOpen();
                    Database.coursesOpen.clear();

                    System.out.println(Constants.COLOR_WRAPPER("Updated successfully!",
                            Constants.GREEN_BOLD));
                    System.out.println("Current enrollment status: " + Constants.COLOR_WRAPPER(Admin.getEnrollmentStatus(),
                            Constants.WHITE_BOLD));
                    System.out.println();
                    exitMenu = true;

                    break;
                default:
                    System.out.println(Constants.INVALID_INPUT);
            }
        }
    }

    /**
     * Displays all the accounts in the database.
     */
    public void listAllAccounts() {
        int count = 0;
        for (Map.Entry<String, User> i : Database.accounts.entrySet()) {
            System.out.println(i.getValue());
            System.out.println();
            if (count == Constants.MAX_ITEMS_SCROLL_FORWARD) {
                Constants.MORE();
                count = 1;
            } else {
                ++count;
            }
        }
        System.out.println("Total accounts: " + Database.accounts.size());
        System.out.println();
        Constants.PRESS_ENTER_KEY_TO_CONTINUE();
        System.out.println();
    }

    /**
     * Allows for the user to modify different aspects of a course.
     * The course must exist in the database for the user to do so.
     */
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
                System.out.println("    +------------------------------------+");
                System.out.println("    |           " + Constants.COLOR_WRAPPER("COURSE SETTING", Constants.WHITE_BOLD) + "           |");
                System.out.println("    +------------------------------------+");
                System.out.println("    |   1. Modify course name            |");
                System.out.println("    |   2. Modify course description     |");
                System.out.println("    |   3. Modify course units           |");
                System.out.println("    |   4. Modify course prerequisites   |");
                System.out.println("    |   5. Modify course antirequisites  |");
                System.out.println("    |   6. Modify course repeats         |");
                System.out.println("    |   7. Return                        |");
                System.out.println("    +------------------------------------+");
                System.out.println();
                System.out.println(Constants.COLOR_WRAPPER(" Note : Invalid value will remain the original data " +
                        "unchanged!", Constants.YELLOW_BOLD));
                System.out.println();
                System.out.print(Constants.COLOR_WRAPPER("Select number: ", Constants.WHITE_BOLD));
                fieldToModify = Constants.IN.nextLine();

                switch (fieldToModify) {
                    case "1":
                        System.out.print(Constants.COLOR_WRAPPER("New COURSE NAME (must be not empty): ", Constants.WHITE_BOLD));
                        String newCourseName = Constants.IN.nextLine();
                        Database.courses.get(courseId).setCourseName(newCourseName);
                        Database.courseUpdateCommit(courseId, Constants.COURSE_FIELD.COURSE_NAME);
                        break;
                    case "2":
                        System.out.print(Constants.COLOR_WRAPPER("New COURSE DESCRIPTION (must be not empty): ", Constants.WHITE_BOLD));
                        String newCourseDescription = Constants.IN.nextLine();
                        Database.courses.get(courseId).setCourseDescription(newCourseDescription);
                        Database.courseUpdateCommit(courseId, Constants.COURSE_FIELD.COURSE_DESCRIPTION);
                        break;
                    case "3":
                        System.out.print(Constants.COLOR_WRAPPER("New COURSE UNITS (must > 0 and <= " + Constants.MAX_UNITS_AS_DOUBLE + "): ", Constants.WHITE_BOLD));
                        String newCourseUnits = Constants.IN.nextLine();
                        Database.courses.get(courseId).setCourseUnits(newCourseUnits);
                        Database.courseUpdateCommit(courseId, Constants.COURSE_FIELD.COURSE_UNITS);
                        break;
                    case "4":
                        System.out.print(Constants.COLOR_WRAPPER("New PREREQUISITES (Format: A B C): ",
                                Constants.WHITE_BOLD));
                        String newPrerequisites = Constants.IN.nextLine();
                        Database.courses.get(courseId).setPrerequisitesAsNew(newPrerequisites);
                        Database.courseUpdateCommit(courseId, Constants.COURSE_FIELD.PREREQUISITES);
                        break;
                    case "5":
                        System.out.print(Constants.COLOR_WRAPPER("New ANTIREQUISITES (Format: A B C): ", Constants.WHITE_BOLD));
                        String newAntirequisites = Constants.IN.nextLine();
                        Database.courses.get(courseId).setAntirequisitesAsNew(newAntirequisites);
                        Database.courseUpdateCommit(courseId, Constants.COURSE_FIELD.ANTIREQUISITES);
                        break;
                    case "6":
                        System.out.print(Constants.COLOR_WRAPPER("Can the course be repeated for GPA? ",
                                Constants.WHITE_BOLD) + " (YES/NO) ");
                        String newCanBeRepeated = Constants.IN.nextLine();
                        Database.courses.get(courseId).setCanBeRepeated(newCanBeRepeated);
                        Database.courseUpdateCommit(courseId, Constants.COURSE_FIELD.CAN_BE_REPEATED);
                        break;
                    case "7":
                        exitMenu = true;
                        break;
                    default:
                        System.out.println(Constants.INVALID_INPUT);
                        Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                }

                // print course info after the modification
                for (int i = 1; i <= 6; ++i) {
                    if (fieldToModify.equals(String.valueOf(i))) {
                        System.out.println();
                        System.out.println(Database.courses.get(courseId));
                        System.out.println();
                        Constants.PRESS_ENTER_KEY_TO_CONTINUE();
                        break;
                    }
                }
            }
        } else {
            System.out.println(Constants.COLOR_WRAPPER("Course id does not exist!", Constants.RED));
            Constants.PRESS_ENTER_KEY_TO_CONTINUE();
            System.out.println();
        }
    }

    /**
     * Allows for the user to create an account.
     * If all aspects of the new account is valid, then the account would be created.
     */
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
            if (Validation.isYearValid(createYear)) {
                break;
            }
            System.out.println(Constants.INVALID_INPUT);
        }
        String createMonth;
        while (true) {
            System.out.print("MONTH : ");
            createMonth = Constants.IN.nextLine();
            if (Validation.isMonthValid(createMonth)) {
                break;
            }
            System.out.println(Constants.INVALID_INPUT);
        }
        String createDay;
        while (true) {
            System.out.print("DAY : ");
            createDay = Constants.IN.nextLine();
            if (Validation.isDayValid(createYear, createMonth,
                    createDay)) {
                break;
            }
            System.out.println(Constants.INVALID_INPUT);
        }
        DateOfBirth newDateOfBirth = new DateOfBirth(createYear, createMonth, createDay);

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

                // if the user created is a faculty, update FACULTY.db concurrently
                Database.createFacultyCommit(createUuid);
                break;

            default:
                createUser = new Student();
                // if the user created is a student, update STUDENT.db concurrently
                Database.createStudentCommit(createUuid);
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

    /**
     * Allows for the user to create a course.
     * If all aspects of the course are valid then the course would be created.
     */
    public void createACourse() {

        // prompt user to input the info, iterate until the input is valid.
        String createCourseId;
        while (true) {
            System.out.print(Constants.COLOR_WRAPPER("COURSE ID", Constants.WHITE_BOLD) + " (must contain letters " +
                    "and digits only, must be not " +
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
                    .COLOR_WRAPPER("COURSE UNITS", Constants.WHITE_BOLD) + " (must > 0 and <= " + Constants.MAX_UNITS_AS_DOUBLE + "): ");
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
        createCourse.setPrerequisitesAsNew(createPrerequisites);
        createCourse.setAntirequisitesAsNew(createAntirequisites);
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
            if (mandatoryOrOptional.equals(Constants.COURSES_TYPE[0])) {
                Database.mandatoryCourses.add(createCourseId);
                Database.courseUpdateCommitToRequirements(createCourseId, true, true);
                extraInformation += "MANDATORY for academic requirements.";
                break;

            } else if (mandatoryOrOptional.equals(Constants.COURSES_TYPE[1])) {
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
            if (mandatoryOrOptional.equals(Constants.COURSES_TYPE[0])) {
                Database.mandatoryCoursesForInternship.add(createCourseId);
                Database.courseUpdateCommitToRequirements(createCourseId, false, true);
                extraInformation += "MANDATORY for internship requirements.";
                break;
            } else if (mandatoryOrOptional.equals(Constants.COURSES_TYPE[1])) {
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

    /**
     * Allows for user to remove an account.
     * The account must exist in the database for the user to do so.
     * The account that the user is deleting cannot be themselves.
     *
     * @param adminUsername a String containing the username of the admin user.
     */
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

    /**
     * Allows for user to remove a course.
     * The course must exist in the database for the user to do so.
     */
    public void removeACourse() {

        System.out.print("Course id: ");
        String courseId = Constants.IN.nextLine();

        // the admin can only remove a course which is not taken by student or taught by any faculty
        if (Database.coursesCurrentlyTaught.contains(courseId)) {

            System.out.println(Constants.COLOR_WRAPPER("Cannot remove a course which is currently being taught!",
                    Constants.RED));
            return;
        }

        // the admin cannot remove a course id which is scheduled already
        if (Database.coursesOpen.containsKey(courseId)) {

            System.out.println(Constants.COLOR_WRAPPER("Cannot remove a course which is scheduled!",
                    Constants.RED));
            return;
        }

        // the admin cannot remove a course id which does not exist, otherwise the removal is allowed to happen
        if (Database.courses.containsKey(courseId)) {

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
}

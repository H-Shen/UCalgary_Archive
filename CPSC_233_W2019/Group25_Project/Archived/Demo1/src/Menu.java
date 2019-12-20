import java.io.Console;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

/**
 * The part of textUI for the main menu. The GUI design will refer its logic.
 */
public class Menu {

    private static String BANNER_FILEPATH;

    /**
     * Clean the terminal screen in *nix system.
     */
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
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
     * A function make the user's password invisible during the input.
     *
     * @param prompt
     * @return
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
     * The textUI for login
     */
    public static void Login(boolean loginWithPasswordInvisible) {

        System.out.print("USERNAME: ");
        String username = Constants.IN.nextLine();
        String password;

        if (loginWithPasswordInvisible) {
            password = enterPassword("PASSWORD: ");
        } else {
            System.out.print("PASSWORD: ");
            password = Constants.IN.nextLine();
        }

        User user = Database.userAuthentication(username, password);

        if (user != null) {

            System.out.println(Constants.COLOR_WRAPPER("Successfully logined!", Constants.GREEN_BOLD));
            System.out.println();
            System.out.println("Good day, " + user.getFullName());
            System.out.println("Current login time is : " + getCurrentTime());

            Database.initializeAccounts();
            Database.initializeCourses();

            System.out.println();
            switch (user.getRole()) {
                case "ADMIN":
                    new AdminUI(user.getUuid());
                    break;
                case "FACULTY":
                    new FacultyUI(user.getUuid());
                    break;
                case "STUDENT":
                    new StudentUI(user.getUuid());
                    break;
                default:
                    break;
            }
        } else {
            System.out.println(Constants.COLOR_WRAPPER("Wrong username or password!", Constants.RED));
            System.out.println();
        }
    }

    /**
     * Get the banner filepath of the app
     *
     * @return
     */
    public static String getBannerFilePath(boolean testInIDE) {
        if (testInIDE) {
            return "/Users/hshen/IdeaProjects/Project/";
        }
        return new File(System.getProperty("user.dir")).getParent() + "/res/";
    }

    public static void main(String[] args) {

        // Define the filepath of banner.txt
        BANNER_FILEPATH = getBannerFilePath(false) + "banner.txt";

        clearScreen();

        // Print banner
        System.out.println(Constants.COLOR_WRAPPER("CPSC 233 Winter 2019 Group 25 Demo 1", Constants.WHITE_BOLD));
        System.out.println();
        try (Scanner in = new Scanner(new FileInputStream(BANNER_FILEPATH))) {
            while (in.hasNextLine()) {
                System.out.println(in.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Main menu
        String select;
        System.out.println();
        while (true) {

            System.out.println(" 1. Login");
            System.out.println(" 2. Version");
            System.out.println(" 3. Exit");
            System.out.println();

            System.out.print(Constants.COLOR_WRAPPER("Select number: ", Constants.WHITE_BOLD));

            select = Constants.IN.nextLine();
            System.out.println();
            switch (select) {
                case "1":
                    Login(false);
                    Database.releaseAllResources();
                    break;
                case "2":
                    System.out.println("Current version: " + Constants.VERSION);
                    System.out.println();
                    break;
                case "3":
                    Constants.IN.close();
                    System.out.println("Good bye.");
                    System.exit(0);
                default:
                    System.out.println(Constants.INVALID_INPUT);
                    System.out.println();
                    break;
            }
        }
    }
}

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * The part of TextUI for the main menu. The GUI design will refer its logic.
 */
public class MenuTextUI extends TextUI {

    /**
     * The TextUI for login
     */
    public static void Login(boolean loginWithPasswordInvisible) {

        Log.initialization();
        System.out.print("USERNAME: ");
        String username = Constants.IN.nextLine();
        String password;

        password = enterPassword("PASSWORD: ");
        User user = Database.userAuthentication(username, password);

        if (user != null) {
            switch (user.getRole()) {
                case "ADMIN":
                    new AdminTextUI(user.getUuid());
                    break;
                case "FACULTY":
                    new FacultyTextUI(user.getUuid());
                    break;
                case "STUDENT":
                    new StudentTextUI(user.getUuid());
                    break;
                default:
                    break;
            }
        } else {
            Log.userLoginFailed(username);
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
            return "/Users/hshen/IdeaProjects/Project/res/";
        }
        return new File(System.getProperty("user.dir")).getParent() + "/res/";
    }

    public static void main(String[] args) {

        // Define the filepath of banner.txt
        String BANNER_FILEPATH = getBannerFilePath(Constants.TEST_IN_IDE) + "banner.txt";

        clearScreen();

        // Print banner
        System.out.println(Constants.COLOR_WRAPPER("CPSC 233 Winter 2019 Group 25 Demo 2", Constants.WHITE_BOLD));
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
            System.out.println(" 2. Exit");
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

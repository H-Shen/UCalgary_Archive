import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * The {@code MenuTextUI} class inherits class TextUI and contains static methods that creates the main menu page.
 */
public class MenuTextUI extends TextUI {

    /**
     * Login for the TextUI.
     * Depending on the username and password that the user inputs, the corresponding TextUI role with all its
     * components will be initialized.
     */
    public static void login() {

        System.out.print("USERNAME: ");
        String username = Constants.IN.nextLine();
        String password;

        password = enterPassword("PASSWORD: ");
        User user = Database.userAuthentication(username, password);

        if (user != null) {
            // initialization
            Database.releaseAllResources();
            Database.initializationPublicInformation();

            switch (user.getRole()) {
                case "ADMIN":
                    new AdminTextUI(user.getUuid());
                    break;
                case "FACULTY":
                    new FacultyTextUI(user.getUuid());
                    break;
                default:
                    new StudentTextUI(user.getUuid());
                    break;
            }
        } else {
            System.out.println(Constants.COLOR_WRAPPER("Wrong username or password!", Constants.RED));
        }
    }

    /**
     * Displays the full main page (before and after the user logs in).
     * The user can choose to login to their account of to exit the program.
     *
     * @param args
     */
    public static void main(String[] args) {

        clearScreen();

        // Print banner
        System.out.println(Constants.COLOR_WRAPPER("CPSC 233 Winter 2019 Group 25 Demo 3", Constants.WHITE_BOLD));
        System.out.println();
        try (Scanner in = new Scanner(new FileInputStream(Constants.BANNER_FILEPATH))) {
            while (in.hasNextLine()) {
                System.out.println(in.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // Main menu
        String select;
        while (true) {

            System.out.println();
            System.out.println("    +--------------+");
            System.out.println("    |   " + Constants.COLOR_WRAPPER("MAIN MENU", Constants.WHITE_BOLD) + "  |");
            System.out.println("    |--------------|");
            System.out.println("    |   1. Login   |");
            System.out.println("    |   2. Exit    |");
            System.out.println("    +--------------+");
            System.out.println();
            System.out.print(Constants.COLOR_WRAPPER("Select number: ", Constants.WHITE_BOLD));
            select = Constants.IN.nextLine();
            System.out.println();

            switch (select) {
                case "1":
                    login();
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

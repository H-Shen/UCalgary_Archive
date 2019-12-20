import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.Timestamp;
import java.util.Scanner;

/**
 * The {@code MenuTextUI} class inherits class TextUI and contains static methods that creates the main menu page.
 *
 * @author Group 25
 * @date 2019/04/08
 */
public class MenuTextUI extends TextUI {

    public static String uuid;

    /**
     * The main logic of login for the TextUI, which depends on the username and password that the user inputs, the
     * corresponding TextUI role with all its components will be initialized.
     *
     * @date 2019/04/08
     */
    private static void login() {

        System.out.print("USERNAME: ");
        String username = Constants.IN.nextLine();
        String password;
        password = enterPassword("PASSWORD: ");
        User user = Database.userAuthentication(username, password);

        if (user != null) {

            // initialization
            Database.releaseAllResources();
            Database.initialization();
            uuid = user.getUuid();

            // log if the user successfully logged in
            Log.login(new Timestamp(System.currentTimeMillis()), Log.getIpAddress(), username, true);
            Log.userLogin(new Timestamp(System.currentTimeMillis()), uuid);

            switch (user.getRole()) {
                case "ADMIN":
                    new AdminTextUI(uuid);
                    break;
                case "FACULTY":
                    new FacultyTextUI(uuid);
                    break;
                default:
                    new StudentTextUI(uuid);
                    break;
            }

            // log if the user logs out
            Log.userLogout(new Timestamp(System.currentTimeMillis()), uuid);
        } else {
            System.out.println(Constants.colorWrapper("Wrong username or password!", Constants.RED));
            // log if the user failed to login
            Log.login(new Timestamp(System.currentTimeMillis()), Log.getIpAddress(), username, false);
        }
    }

    /**
     * Displays the full main page (before and after the user logs in).
     * The user can choose to login to their account of to exit the program.
     *
     * @param args arguments received from standard input
     * @date 2019/04/08
     */
    public static void main(String[] args) {

        // initialization
        clearScreen();
        Log.readLogFromFile();

        // print banner
        System.out.println(Constants.colorWrapper("CPSC 233 Winter 2019 Group 25", Constants.WHITE_BOLD));
        System.out.println();
        try (Scanner in = new Scanner(new FileInputStream(Constants.BANNER_FILEPATH))) {
            while (in.hasNextLine()) {
                System.out.println(in.nextLine());
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        // main menu
        String select;
        while (true) {

            System.out.println();
            System.out.println("    +--------------+");
            System.out.println("    |   " + Constants.colorWrapper("MAIN MENU", Constants.WHITE_BOLD) + "  |");
            System.out.println("    |--------------|");
            System.out.println("    |   1. Login   |");
            System.out.println("    |   2. Exit    |");
            System.out.println("    +--------------+");
            System.out.println();
            System.out.print(Constants.colorWrapper("Select number: ", Constants.WHITE_BOLD));
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

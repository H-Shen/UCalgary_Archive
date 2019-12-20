import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.sql.Timestamp;

/**
 * The {@code MenuGUI} class defines the layout and logic of Main Menu in GUI.
 *
 * @author Group 25
 * @date 2019/04/08
 */
public class MenuGUI extends Application {

    /**
     * @Fields Properties and components used in GUI
     */
    public static String uuid;
    public static Stage  subStage;

    private Stage                loginStage;
    private Parent               root;
    private SimpleStringProperty username;
    private SimpleStringProperty password;

    private RadioButton studentRadioButton;
    private RadioButton facultyRadioButton;
    private RadioButton adminRadioButton;


    private String role;
    private Label  login;

    public static void main(String[] args) {
        launch(args);
    }

    /**
     * The 'start' method of JavaFX, which creates the log in menu and calls login method.
     *
     * @date 2019/04/08
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        // make "STUDENT" as the default login option
        role = "STUDENT";
        loginStage = primaryStage;
        root = FXMLLoader.load(getClass().getResource("MenuGUI.fxml"));

        // initialization
        Log.readLogFromFile();
        initialize();
        login();

        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.sizeToScene();
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    /**
     * The method that initializes all fields and declares variables that will be used in login method.
     *
     * @date 2019/04/08
     */
    private void initialize() {

        // set the default value in username text-field
        TextField usernameTextField = (TextField) root.lookup("#userAccount");
        this.username = new SimpleStringProperty("");
        this.username.bind(usernameTextField.textProperty());

        // set the default value in password field
        PasswordField passwordField = (PasswordField) root.lookup("#password");
        this.password = new SimpleStringProperty("");
        this.password.bind(passwordField.textProperty());

        // binding fx:id to fields
        ToggleGroup group = new ToggleGroup();

        studentRadioButton = (RadioButton) root.lookup("#studentRadioButton");
        facultyRadioButton = (RadioButton) root.lookup("#facultyRadioButton");
        adminRadioButton = (RadioButton) root.lookup("#adminRadioButton");

        studentRadioButton.setToggleGroup(group);
        facultyRadioButton.setToggleGroup(group);
        adminRadioButton.setToggleGroup(group);

        login = (Label) root.lookup("#loginLabel");

        // define the default option to check
        studentRadioButton.setSelected(true);
    }

    /**
     * The method that gets text from password and login text fields and when mouse is clicked, searches for a user with
     * those password and username within the database shows error popup window if login has failed, adds to log
     * History if user is found, gets role and loads the proper homepage window.
     *
     * @date 2019/04/08
     */
    private void login() {

        // bind role with the corresponding window manager
        studentRadioButton.setOnAction(e -> role = "STUDENT");
        facultyRadioButton.setOnAction(e -> role = "FACULTY");
        adminRadioButton.setOnAction(e -> role = "ADMIN");

        // define the event after clicking
        login.setOnMouseClicked((MouseEvent event) -> {
            User user = Database.userAuthentication(username.getValue(), password.getValue());
            if (user == null || !user.getRole().equals(role)) {
                Utility.showError("ERROR", "Invalid input!", "Username or password is incorrect!");
                // log of login failed
                Log.login(new Timestamp(System.currentTimeMillis()), Log.getIpAddress(), username.getValue(), false);
                return;
            }

            // initialization must be executed after the user passed the authentication
            // to make sure the initialization is pure, release all resources once before it
            Database.releaseAllResources();
            Database.initialization();

            // log of login successfully
            Log.login(new Timestamp(System.currentTimeMillis()), Log.getIpAddress(), username.getValue(), true);
            Log.userLogin(new Timestamp(System.currentTimeMillis()), user.getUuid());

            uuid = user.getUuid();
            switch (role) {
                case "STUDENT":
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("StudentGUI.fxml"));
                        Parent     root       = fxmlLoader.load();
                        subStage = new Stage();
                        subStage.setTitle("Student Panel");
                        subStage.setScene(new Scene(root));
                        subStage.sizeToScene();
                        subStage.setResizable(false);
                        loginStage.hide();
                        subStage.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "FACULTY":
                    Database.initializeFacultyInformation(MenuGUI.uuid);
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("FacultyGUI.fxml"));
                        Parent     root       = fxmlLoader.load();
                        subStage = new Stage();
                        subStage.setTitle("Faculty Panel");
                        subStage.setScene(new Scene(root));
                        subStage.sizeToScene();
                        subStage.setResizable(false);
                        loginStage.hide();
                        subStage.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                default:
                    try {
                        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("AdminGUI.fxml"));
                        Parent     root       = fxmlLoader.load();
                        subStage = new Stage();
                        subStage.setTitle("Administrator Panel");
                        subStage.setScene(new Scene(root));
                        subStage.sizeToScene();
                        subStage.setResizable(false);
                        loginStage.hide();
                        subStage.show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
            }
            // log if the user closes the window
            subStage.setOnCloseRequest(windowEvent -> Log.userLogout(
                    new Timestamp(System.currentTimeMillis()), user.getUuid()));
        });
    }
}

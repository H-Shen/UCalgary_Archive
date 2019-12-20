import javafx.application.Application;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

/**
 * @author Group25
 * @date 2019-3-28
 */
public class MenuGUI extends Application {

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

    @Override
    public void start(Stage primaryStage) throws Exception {

        // make "STUDENT" as default login option
        role = "STUDENT";
        loginStage = primaryStage;
        root = FXMLLoader.load(getClass().getResource("MenuGUI.fxml"));

        // initialization
        initialize();
        login();

        primaryStage.setTitle("Login");
        primaryStage.setScene(new Scene(root));
        primaryStage.sizeToScene();
        primaryStage.setResizable(false);
        primaryStage.show();
    }

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

    private void login() {

        // bind role with the corresponding window manager
        studentRadioButton.setOnAction(e -> role = "STUDENT");
        facultyRadioButton.setOnAction(e -> role = "FACULTY");
        adminRadioButton.setOnAction(e -> role = "ADMIN");

        // define the event after clicking
        login.setOnMouseClicked((MouseEvent event) -> {

            User user = Database.userAuthentication(username.getValue(), password.getValue());
            if (user == null || !user.getRole().equals(role)) {
                Util.showError("ERROR", "Invalid input!", "Username or password is incorrect!");
                return;
            }

            // initialization must be after the user passed the authentication
            // to make sure the initialization is pure, release all resources once before it
            Database.releaseAllResources();
            Database.initializationPublicInformation();

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
        });
    }
}
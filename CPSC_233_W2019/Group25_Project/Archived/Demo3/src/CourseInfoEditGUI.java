import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class CourseInfoEditGUI implements Initializable {

    @FXML
    private Label                   courseIdLabel;
    @FXML
    private TextArea                courseNameTextArea;
    @FXML
    private TextArea                courseDescriptionTextArea;
    @FXML
    private TextField               courseUnitsTextField;
    @FXML
    private MenuButton              canBeRepeatedMenuButton;
    @FXML
    private TableView<CourseForGUI> prerequisitesTable;
    @FXML
    private TableView<CourseForGUI> antirequisitesTable;
    @FXML
    private TextField               courseIDTextField;

    public void yesMenuItemOnAction() {
        canBeRepeatedMenuButton.setText("Yes");
    }

    public void noMenuItemOnAction() {
        canBeRepeatedMenuButton.setText("No");
    }

    public void updateOnAction() {
        System.out.println(1);
    }

    public void removeOnAction() {
        System.out.println(2);
    }

    public void addToPrerequisitesOnAction() {
        System.out.println(3);
    }

    public void addToAntirerequisitesOnAction() {
        System.out.println(4);
    }

    public void courseNameKeyReleased() {

    }

    public void courseDescriptionKeyReleased() {

    }

    public void courseUnitsKeyReleased() {

    }

    public void courseIDKeyReleased() {

    }

    /**
     * Initialize the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        courseIdLabel.setText(AdminGUI.courseIdToEdit);


    }

}

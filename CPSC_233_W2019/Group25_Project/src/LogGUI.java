import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * The {@code LogGUI} class defines the layout and logic of Log History's Panel in GUI.
 *
 * @author Group 25
 * @date 2019/04/08
 */
public class LogGUI implements Initializable {

    /**
     * @Fields Properties and components used in GUI
     */
    @FXML
    private TextField keywordTextField;
    @FXML
    private TextArea  historyLogTextArea;

    private String keyword;

    /**
     * The method gets text from the keyword textfield
     *
     * @date 2019/04/08
     */
    public void keywordKeyReleased() {
        keyword = keywordTextField.getText();
    }

    /**
     * The method turns log array into a string and displays it as text in the GUI
     *
     * @date 2019/04/08
     */
    public void searchOnAction() {
        StringBuilder sb = new StringBuilder();
        for (String i : Log.logArray) {
            if (i.contains(keyword)) {
                sb.append(i).append("\n");
            }
        }
        historyLogTextArea.setText(sb.toString());
    }

    /**
     * The method resets the keyword search text fields.
     *
     * @date 2019/04/08
     */
    public void resetOnAction() {
        StringBuilder sb = new StringBuilder();
        for (String i : Log.logArray) {
            sb.append(i).append("\n");
        }
        historyLogTextArea.setText(sb.toString());
        keyword = "";
        keywordTextField.setText(keyword);
    }

    /**
     * Initialize the controller class.
     *
     * @param url Uniform Resource Locator
     * @param rb  Resource Bundle
     * @date 2019/04/08
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        resetOnAction();
    }

}

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.util.Optional;

/**
 * A collection of miscellaneous functions
 *
 * @author Group 25
 * @date 2019/04/08
 */

public class Utility {

    /**
     * shows an alert window with the content of why the alert has been shown
     *
     * @param title   title of the window
     * @param header  header of the window
     * @param content content of the window
     * @date 2019/04/08
     */
    public static void showAlert(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * shows an error popup window with the content being the error
     *
     * @param title   title of the window
     * @param header  header of the window
     * @param content content of the window
     * @date 2019/04/08
     */
    public static void showError(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    /**
     * shows s confirmation popup with content being the confirmation
     *
     * @param title   title of the window
     * @param header  header of the window
     * @param content content of the window
     * @date 2019/04/08
     */
    public static boolean showConfirmation(String title, String header, String content) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(content);
        Optional<ButtonType> result = alert.showAndWait();
        return (result.isPresent() && result.get() == ButtonType.OK);
    }

    /******************************************************************************
     * Calculate the intensity of password by a set of rules:
     * <p>
     * 1. The length of password: < 6 characters: 0 mark
     * >= 6 and <= 8 characters: 10 marks
     * > 8 characters: 25 marks
     * <p>
     * 2. Letters: No letters: 0 mark
     * All lowercase/uppercase letters: 10 marks
     * Mixed: 25 marks
     * <p>
     * 3. Digits: No digits: 0 mark
     * < 3 digit: 10 marks
     * >= 3 digits: 20 marks
     * <p>
     * 4. special characters: No special characters: 0 mark
     * 1 special character: 10 marks
     * >1 special character: 24 marks
     * <p>
     * 5. Awards: Contains >= 1 letter and >= 1 digit: 2 marks
     * Contains >= 1 letter and >= 1 digit and >= 1 special characters: 3 marks
     * Contains >= 1 uppercase letter, >= 1 lowercase letter and >= 1 digit and >= 1 special characters: 5
     * marks
     * <p>
     * Rules: == 100 Very secure
     * >= 80 Secure
     * >= 70 Very strong
     * >= 60 Strong
     * >= 50 Average
     * >= 25 Weak
     * > 0 Very weak
     *
     * @param password passphrase to check
     * @return a mark depends on the intensity of the password
     * @date 2019/04/08
     *
     ******************************************************************************/
    public static int passwordIntensity(String password) {

        int lowercase        = 0;
        int uppercase        = 0;
        int digit            = 0;
        int specialCharacter = 0;
        int len              = password.length();

        for (int i = 0; i < len; ++i) {
            char ch = password.charAt(i);
            if (ch >= '0' && ch <= '9') {
                ++digit;
            } else if (ch >= 'A' && ch <= 'Z') {
                ++uppercase;
            } else if (ch >= 'a' && ch <= 'z') {
                ++lowercase;
            } else if (Constants.SPECIAL_CHARSET.indexOf(ch) != -1) {
                ++specialCharacter;
            }
        }

        int totalMarks = 0;
        // The length of password
        if (len >= 6 && len <= 8) {
            totalMarks += 10;
        } else if (len > 8) {
            totalMarks += 25;
        }

        // Letters
        if (lowercase == 0 && uppercase != 0) {
            totalMarks += 10;
        } else if (lowercase != 0 && uppercase == 0) {
            totalMarks += 10;
        } else if (lowercase + uppercase != 0) {
            totalMarks += 25;
        }

        // Digits
        if (digit > 0 && digit < 3) {
            totalMarks += 10;
        } else if (digit >= 3) {
            totalMarks += 20;
        }

        // Special characters
        if (specialCharacter == 1) {
            totalMarks += 10;
        } else if (specialCharacter > 1) {
            totalMarks += 25;
        }

        // Rewards
        if (uppercase >= 1 && lowercase >= 1 && digit >= 1 && specialCharacter >= 1) {
            totalMarks += 5;
        } else if (uppercase + lowercase >= 1 && digit >= 1 && specialCharacter >= 1) {
            totalMarks += 3;
        } else if (uppercase + lowercase >= 1 && digit >= 1) {
            totalMarks += 2;
        }
        return totalMarks;
    }
}

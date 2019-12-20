import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

/**
 * Demonstration of TextField and TextArea controls.
 */
public class TextControlDemo extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FlowPane root = new FlowPane();
        root.setVgap(5);
        root.setHgap(5);

        // Label and textfield for name
        root.getChildren().add(new Label("Name"));
        TextField txtName = new TextField("Enter name.");
        txtName.setPrefWidth(100);
        root.getChildren().add(txtName);

        // Label and textarea for info
        root.getChildren().add(new Label("Your Info"));
        TextArea txtInfo = new TextArea(
                "Enter some information\nabout yourself.");
        txtInfo.setPrefWidth(200);
        txtInfo.setPrefRowCount(4);
        txtInfo.setPrefColumnCount(40);
        root.getChildren().add(txtInfo);

        Scene scene = new Scene(root, 450, 150);
        primaryStage.setTitle("Text Control Demo");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

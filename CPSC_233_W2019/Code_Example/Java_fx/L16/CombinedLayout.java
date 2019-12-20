import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * Embedding an HBox and FlowPane into a BorderPane.
 */
public class CombinedLayout extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();

        // Create a FlowPane with a TextField and TextArea
        FlowPane centerPane = new FlowPane();
        centerPane.setVgap(5);
        centerPane.setHgap(5);
        // Label and textfield for name
        centerPane.getChildren().add(new Label("Name"));
        TextField txtName = new TextField("Enter name.");
        txtName.setPrefWidth(100);
        centerPane.getChildren().add(txtName);
        // Label and textarea for info
        centerPane.getChildren().add(new Label("Your Info"));
        TextArea txtInfo = new TextArea(
                "Enter some information\nabout yourself.");
        txtInfo.setPrefWidth(200);
        txtInfo.setPrefRowCount(8);
        txtInfo.setPrefColumnCount(40);
        centerPane.getChildren().add(txtInfo);

        // Create an HBox with four buttons
        HBox topPane = new HBox();
        topPane.getChildren().add(new Button("This is Button 1"));
        topPane.getChildren().add(new Button("This is Button 2"));
        topPane.getChildren().add(new Button("This is Button 3"));
        topPane.getChildren().add(new Button("This is Button 4"));

        // Add the FlowPane to the center
        root.setCenter(centerPane);
        // Add the HBox to the top
        root.setTop(topPane);

        Scene scene = new Scene(root, 450, 250);
        primaryStage.setTitle("Text Control Demo");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

/**
 * Simple demonstration of adding buttons to the FlowPane layout.
 */
public class FlowPaneDemo extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FlowPane root = new FlowPane();

        // Set a gap of 5 pixels vertically and horizontally
        // between buttons
        root.setVgap(5);
        root.setHgap(5);

        root.getChildren().add(new Button("This is Button 1"));
        root.getChildren().add(new Button("This is Button 2"));
        root.getChildren().add(new Button("This is Button 3"));


        Scene scene = new Scene(root, 500, 200);
        primaryStage.setTitle("FlowPane Demo");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

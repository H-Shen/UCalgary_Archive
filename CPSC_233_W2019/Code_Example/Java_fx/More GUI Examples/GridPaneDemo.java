import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

/**
 * Simple demonstration of adding buttons and labels
 * to the GridPane layout.
 */
public class GridPaneDemo extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        GridPane root = new GridPane();

        // Set a gap of 5 pixels vertically and horizontally
        // between elements
        root.setVgap(5);
        root.setHgap(5);
        // Margins around the top, right, bottom, and left
        root.setPadding(new Insets(10, 10, 10, 10));

        // Add three nodes, by default horizontally left-aligned
        root.add(new Label("Option 1"), 0, 0);
        root.add(new Button("This is Button 1"), 1, 0);
        root.add(new Label("Option 2"), 0, 1);

        // Add a button that is horizontally right-aligned
        Button btn2 = new Button("Button 2");
        GridPane.setHalignment(btn2, HPos.RIGHT);
        root.add(btn2, 1, 1);

        // Add a label to the bottom right of the buttons
        root.add(new Label("Out there"), 2, 2);

        Scene scene = new Scene(root, 500, 200);
        primaryStage.setTitle("GridPane Demo");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

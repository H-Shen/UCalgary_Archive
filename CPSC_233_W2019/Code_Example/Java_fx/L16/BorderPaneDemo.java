import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 * Simple demonstration of adding buttons to the BorderPane layout.
 */
public class BorderPaneDemo extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();

        root.setTop(new Button("Top Button"));
        root.setLeft(new Button("Left Button"));
        root.setCenter(new Button("Center Button"));
        root.setRight(new Button("Right Button"));
        root.setBottom(new Button("Bottom Button"));

        Scene scene = new Scene(root, 500, 200);
        primaryStage.setTitle("BorderPane Demo");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

/**
 * Simple demonstration of adding buttons using the HBox layout.
 * These buttons do not do anything. That comes in a later version.
 */
public class HBoxDemo extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        HBox root = new HBox();
        root.getChildren().add(new Button("This is Button 1"));
        root.getChildren().add(new Button("This is Button 2"));
        root.getChildren().add(new Button("This is Button 3"));

        Scene scene = new Scene(root, 250, 100);
        primaryStage.setTitle("HBox Demo");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

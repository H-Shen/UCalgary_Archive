import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Simple demonstration of adding buttons to a JavaFX application.
 * This version displays an icon for the Sunny Button.
 * Nothing happens when the buttons are clicked.
 */
public class ButtonImageDemo extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox   root = new VBox();
        Button btnSunny;
        Button btnCloudy;

        btnSunny = new Button("Sunny");
        btnCloudy = new Button("Cloudy");

        Image imgSmiley = new Image("smiley.gif");
        btnSunny.setGraphic(new ImageView(imgSmiley));

        root.getChildren().add(btnSunny);
        root.getChildren().add(btnCloudy);

        Scene scene = new Scene(root, 300, 100);
        primaryStage.setTitle("Button Demo");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

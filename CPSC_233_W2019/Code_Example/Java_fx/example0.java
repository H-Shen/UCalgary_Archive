import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;


public class example0 extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {


        // System.out.println(Font.getFontNames());

        Canvas          canvas = new Canvas(400, 300);
        GraphicsContext a      = canvas.getGraphicsContext2D();

        a.setFill(Color.AZURE);
        a.fillOval(100, 50, 200, 200);
        a.setFill(Color.DARKBLUE);
        a.strokeOval(100, 50, 200, 200);
        a.strokeLine(0, 0, 10, 10);
        a.strokeRect(10, 10, 100, 5);
        a.fillText("Hello", 50, 50);
        a.setFont(Font.font("Monaco"));
        a.fillText("abcd", 200, 100);
        a.drawImage(new Image("https://avatars0.githubusercontent.com/u/22427365?s=400&v=4"), 200, 200);
        // a.setEffect();

        Group root  = new Group();
        Scene scene = new Scene(root);
        root.getChildren().add(canvas);
        primaryStage.setScene(scene);

        primaryStage.setTitle("Draw a circle");
        primaryStage.show();
    }
}

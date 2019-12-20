import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.stage.Stage;


public class example1 extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {


        // System.out.println(Font.getFontNames());

        Canvas          canvas = new Canvas(800, 600);
        GraphicsContext a      = canvas.getGraphicsContext2D();

        Image img = new Image("file:/Users/hshen/Desktop/500px-Baidu.svg.png");
        //a.drawImage(img, 0, 0);
        a.drawImage(img, 10, 10, img.getWidth() * 2, img.getHeight() * 2);
        a.setEffect(new Reflection());
        // then when you draw a new stuff, the effect will work
        a.drawImage(img, 1, img.getHeight() * 2);

        Group root  = new Group();
        Scene scene = new Scene(root);
        root.getChildren().add(canvas);
        primaryStage.setScene(scene);

        primaryStage.setTitle("GraphicsContext_example1");
        primaryStage.show();
    }
}

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class LabelDemo extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        VBox  root = new VBox();
        Label label1, label2;
        label1 = new Label("Hello");
        label1.setFont(Font.font("Times New Roman", 24));

        label2 = new Label("Out there!");
        label2.setFont(Font.font("Courier New", 36));

        root.getChildren().add(label1);
        root.getChildren().add(label2);

        Scene scene = new Scene(root, 300, 100);
        primaryStage.setTitle("Label Demo");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
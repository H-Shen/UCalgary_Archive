/**
 * The {@code IA5} class creates a graphical user interface using JavaFX for a very simple banking
 * app. The app would allow the user to control their own account. When either of the buttons is
 * pressed, the app does nothing.
 * <p>
 *
 * @author Haohu Shen
 * @version 2019-02-21
 */

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * The {@code BankAccount} class can be used to simulate a dummy account holder which contains a
 * balance and a holder name only.
 */
class BankAccount {

    private double balance;
    private String holder;

    /**
     * Constructor with balance and holder's name
     *
     * @param balance
     * @param holder
     */
    public BankAccount(double balance, String holder) {
        setBalance(balance);
        setHolder(holder);
    }

    /**
     * Getter of balance
     *
     * @return a formatted string of balance
     */
    public String getBalance() {
        return String.format("%.2f", balance);
    }

    /**
     * Setter of balance
     *
     * @param balance
     */
    private void setBalance(double balance) {
        this.balance = balance;
    }

    /**
     * Getter of holder
     *
     * @return holder
     */
    public String getHolder() {
        return holder;
    }

    /**
     * Setter of holder
     *
     * @param holder
     */
    private void setHolder(String holder) {
        this.holder = holder;
    }
}

/**
 * The {@code Window} class extends AnchorPane and simulates the main part of the UI.
 */
class Window extends AnchorPane {

    private static final double PREF_HEIGHT = 250;
    private static final double PREF_WIDTH  = 400.0;
    private static final double FONT_SIZE   = 16.0;

    public Window(BankAccount account) {

        // Initialization
        Button    button    = new Button();
        Button    button0   = new Button();
        Label     label     = new Label();
        Label     label0    = new Label();
        Label     label1    = new Label();
        TextField textField = new TextField();

        // Setting the layout of the anchorPane.
        setMaxHeight(PREF_HEIGHT);
        setMaxWidth(PREF_WIDTH);
        setMinHeight(PREF_HEIGHT);
        setMinWidth(PREF_WIDTH);
        setPrefHeight(PREF_HEIGHT);
        setPrefWidth(PREF_WIDTH);

        // Setting the layout and contents of the 'Deposit' button.
        button.setLayoutX(120.0);
        button.setLayoutY(190.0);
        button.setMnemonicParsing(false);
        button.setText("Deposit");
        button.setFont(new Font(FONT_SIZE));
        button.setOnAction((event) -> {
            System.out.println("1");
        });

        // Setting the layout and contents of the 'Withdraw' button.
        button0.setLayoutX(200.0);
        button0.setLayoutY(190.0);
        button0.setMnemonicParsing(false);
        button0.setText("Withdraw");
        button0.setFont(new Font(FONT_SIZE));
        button0.setOnAction((event) -> {
            System.out.println("2");
        });

        // Setting the layout and contents of the 'Account Holder' label.
        label.setAlignment(javafx.geometry.Pos.CENTER);
        label.setContentDisplay(javafx.scene.control.ContentDisplay.CENTER);
        label.setLayoutX(90.0);
        label.setLayoutY(20.0);
        label.setPrefHeight(20.0);
        label.setPrefWidth(220.0);
        label.setText("Account Holder: " + account.getHolder());
        label.setFont(new Font(FONT_SIZE));

        // Setting the layout and contents of the 'Balance' label.
        label0.setAlignment(javafx.geometry.Pos.CENTER);
        label0.setContentDisplay(javafx.scene.control.ContentDisplay.CENTER);
        label0.setLayoutX(90.0);
        label0.setLayoutY(50.0);
        label0.setPrefHeight(20.0);
        label0.setPrefWidth(220.0);
        label0.setText("Balance: " + account.getBalance());
        label0.setFont(new Font(FONT_SIZE));

        // Setting the layout and contents of the 'Enter amount' label.
        label1.setLayoutX(25.0);
        label1.setLayoutY(105.0);
        label1.setPrefHeight(20.0);
        label1.setPrefWidth(130.0);
        label1.setText("Enter amount:");
        label1.setFont(new Font(FONT_SIZE));

        // Setting the layout of the textfield.
        textField.setLayoutX(150.0);
        textField.setLayoutY(100.0);
        textField.setPrefHeight(35.0);
        textField.setPrefWidth(210.0);

        getChildren().add(button);
        getChildren().add(button0);
        getChildren().add(label);
        getChildren().add(label0);
        getChildren().add(label1);
        getChildren().add(textField);
    }
}

/**
 * Demonstration of IA5.
 */
public class IA5 extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        BankAccount account = new BankAccount(0.00, "John Doe");
        AnchorPane  root    = new AnchorPane(new Window(account));
        Scene       scene   = new Scene(root);

        primaryStage.setResizable(false);
        primaryStage.setTitle("My Bank");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

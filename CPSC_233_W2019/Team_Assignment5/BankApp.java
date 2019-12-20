import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * The {@code BankApp} class creates a graphical user interface using JavaFX for showing banking information of a
 * customer.The app would allow the user to check their name and the amount, withdraw and deposit by input and pressing
 * the corresponding buttons. Invalid input will be ignored.
 * <p>
 *
 * @author Group25
 * @version 2019-03-01
 */

/**
 * The {@code Widget} class is the widget where the user enters the amount to deposit/withdraw
 */
class Widget extends AnchorPane {

    /**
     * Constant fields
     */
    public static final double PREF_HEIGHT = 250;
    public static final double PREF_WIDTH  = 400.0;
    public static final double FONT_SIZE   = 16.0;

    public Widget(BankAccount account, Label accountBalanceLabel) {

        // Initialization of components except accountBalanceLabel
        Button    depositButton      = new Button();
        Button    withdrawButton     = new Button();
        Label     accountHolderLabel = new Label();
        Label     enterAmountLabel   = new Label();
        TextField textField          = new TextField();

        // Setting the layout of the anchorPane.
        setMaxHeight(PREF_HEIGHT);
        setMaxWidth(PREF_WIDTH);
        setMinHeight(PREF_HEIGHT);
        setMinWidth(PREF_WIDTH);
        setPrefHeight(PREF_HEIGHT);
        setPrefWidth(PREF_WIDTH);

        // Setting the layout and contents of the 'Deposit' button.
        depositButton.setLayoutX(120.0);
        depositButton.setLayoutY(190.0);
        depositButton.setMnemonicParsing(false);
        depositButton.setText("Deposit");
        depositButton.setFont(new Font(FONT_SIZE));

        // Create an anonymous inner class to handle the 'Deposit' button
        depositButton.setOnAction(new EventHandler<ActionEvent>() {
            private int a = 1;

            @Override
            public void handle(ActionEvent event) {
                ++a;
                System.out.println(a);
                // Any invalid input will be ignored.
                String depositValue = textField.getText();
                double deposit      = 0.0;
                try {
                    deposit = Double.parseDouble(depositValue);
                } catch (Exception e) {
                    return;
                }

                account.deposit(deposit);
                accountBalanceLabel.setText("Balance: " + String.format("%.2f", account.getBalance()));
            }
        });

        // Setting the layout and contents of the 'Withdraw' button.
        withdrawButton.setLayoutX(200.0);
        withdrawButton.setLayoutY(190.0);
        withdrawButton.setMnemonicParsing(false);
        withdrawButton.setText("Withdraw");
        withdrawButton.setFont(new Font(FONT_SIZE));

        // Create an anonymous inner class to handle 'Withdraw' button
        withdrawButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                // Any invalid input will be ignored.
                String withdrawValue = textField.getText();
                double withdraw      = 0.0;
                try {
                    withdraw = Double.parseDouble(withdrawValue);
                } catch (Exception e) {
                    return;
                }

                account.withdraw(withdraw);
                accountBalanceLabel.setText("Balance: " + String.format("%.2f", account.getBalance()));
            }
        });


        // Setting the layout and contents of the 'Account Holder' label.
        accountHolderLabel.setAlignment(Pos.CENTER);
        accountHolderLabel.setContentDisplay(ContentDisplay.CENTER);
        accountHolderLabel.setLayoutX(90.0);
        accountHolderLabel.setLayoutY(20.0);
        accountHolderLabel.setPrefHeight(20.0);
        accountHolderLabel.setPrefWidth(220.0);
        accountHolderLabel.setText("Account Holder: " + account.getAccountHolder().getName());
        accountHolderLabel.setFont(new Font(FONT_SIZE));

        // Setting the layout and contents of the 'Balance' label.
        accountBalanceLabel.setAlignment(Pos.CENTER);
        accountBalanceLabel.setContentDisplay(ContentDisplay.CENTER);
        accountBalanceLabel.setLayoutX(90.0);
        accountBalanceLabel.setLayoutY(50.0);
        accountBalanceLabel.setPrefHeight(20.0);
        accountBalanceLabel.setPrefWidth(220.0);
        accountBalanceLabel.setText("Balance: " + String.format("%.2f", account.getBalance()));
        accountBalanceLabel.setFont(new Font(FONT_SIZE));

        // Setting the layout and contents of the 'Enter amount' label.
        enterAmountLabel.setLayoutX(25.0);
        enterAmountLabel.setLayoutY(105.0);
        enterAmountLabel.setPrefHeight(20.0);
        enterAmountLabel.setPrefWidth(130.0);
        enterAmountLabel.setText("Enter amount:");
        enterAmountLabel.setFont(new Font(FONT_SIZE));

        // Setting the layout of the textfield.
        textField.setLayoutX(150.0);
        textField.setLayoutY(100.0);
        textField.setPrefHeight(35.0);
        textField.setPrefWidth(210.0);

        getChildren().add(depositButton);
        getChildren().add(withdrawButton);
        getChildren().add(accountHolderLabel);
        getChildren().add(accountBalanceLabel);
        getChildren().add(enterAmountLabel);
        getChildren().add(textField);
    }
}

/**
 * The {@code BankApp} class is the main part of the app.
 */
public class BankApp extends Application {

    private BankAccount bankAccount;
    private Widget      widget;
    private Label       accountBalanceLabel;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        Customer customer = new Customer("John Doe", 1000);
        bankAccount = new BankAccount(customer, 0.00);
        accountBalanceLabel = new Label();

        // add the copy of reference of instance variables of bankAccount and accountBalanceLabel to the widget
        widget = new Widget(bankAccount, accountBalanceLabel);
        AnchorPane root  = new AnchorPane(widget);
        Scene      scene = new Scene(root);

        primaryStage.setResizable(false);
        primaryStage.setTitle("My Bank");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}

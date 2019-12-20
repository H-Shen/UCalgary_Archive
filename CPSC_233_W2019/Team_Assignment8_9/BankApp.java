import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * The {@code Widget} class is the widget where the user enters the amount to deposit/withdraw
 */
class Widget extends AnchorPane {

    /**
     * Constant fields
     */
    private static final double PREF_HEIGHT = 250;
    private static final double PREF_WIDTH  = 400.0;
    private static final double FONT_SIZE   = 16.0;

    public Widget(SavingsAccount account, Label accountBalanceLabel) {

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

                // Any invalid input will be ignored.
                String depositValue = textField.getText();
                double deposit;

                try {
                    deposit = Double.parseDouble(depositValue);
                } catch (Exception e) {
                    BankApp.showError();
                    return;
                }

                account.deposit(deposit);
                BankApp.balance = account.getBalance();
                accountBalanceLabel.setText("Balance: " + String.format("%.2f", account.getBalance()));

                // update
                BankApp.balance = account.getBalance();
            }
        });

        // Setting the layout and contents of the 'Withdraw' button.
        withdrawButton.setLayoutX(200.0);
        withdrawButton.setLayoutY(190.0);
        withdrawButton.setMnemonicParsing(false);
        withdrawButton.setText("Withdraw");
        withdrawButton.setFont(new Font(FONT_SIZE));

        // Create an anonymous inner class to handle 'Withdraw' button
        withdrawButton.setOnAction(event -> {

            // Any invalid input will be ignored.
            String withdrawValue = textField.getText();
            double withdraw;

            try {
                withdraw = Double.parseDouble(withdrawValue);
            } catch (Exception e) {
                BankApp.showError();
                return;
            }

            account.withdraw(withdraw);
            BankApp.balance = account.getBalance();
            accountBalanceLabel.setText("Balance: " + String.format("%.2f", account.getBalance()));
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
 * The {@code BankApp} class creates a graphical user interface using JavaFX for showing banking information of a
 * customer.The app would allow the user to check their name and the amount, withdraw and deposit by input and pressing
 * the corresponding buttons. Invalid input will be ignored.
 * <p>
 *
 * @author Group25
 * @version 2019-03-29
 */
public class BankApp extends Application {

    public static double         balance;
    private       SavingsAccount savingsAccountAccount;

    /**
     * Show a descriptive error message if the input is invalid
     */
    public static void showError() {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Invalid input!");
        alert.setHeaderText(null);
        alert.setContentText("Input is not a number or a valid number!");
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        // use the current filepath as a default value since initial filepath is not given in the requirement of the
        // assignment
        String filePath = System.getProperty("user.dir") + "/account_info.txt";

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            savingsAccountAccount = new SavingsAccount(br);

        } catch (Exception e) {

            // Create a text input dialog and prompt the user to input the customer name
            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("New Customer Register");
            dialog.setHeaderText(null);
            dialog.setContentText("Please enter the customer name:");

            Optional<String> customerInput = dialog.showAndWait();
            if (customerInput.isPresent()) {
                String customerName   = customerInput.get();
                double initialBalance = 0.0;
                int    customerID     = ThreadLocalRandom.current().nextInt(1000, 9999 + 1);
                savingsAccountAccount = new SavingsAccount(new Customer(customerName, customerID), initialBalance);
            } else {
                // if the user abandons to input the customer name, exits the app without doing anything
                System.exit(0);
            }
        }

        Label accountBalanceLabel = new Label();
        // add the copy of reference of instance variables of bankAccount and accountBalanceLabel to the widget
        Widget     widget = new Widget(savingsAccountAccount, accountBalanceLabel);
        AnchorPane root   = new AnchorPane(widget);
        Scene      scene  = new Scene(root);
        primaryStage.setTitle("My Bank");
        primaryStage.setScene(scene);
        primaryStage.sizeToScene();
        primaryStage.setResizable(false);
        primaryStage.show();

        // save all info before closing the window
        primaryStage.setOnCloseRequest((WindowEvent event) -> {
            try {
                // the filePath is valid and the information of the new user will be stored
                // if the file exists then the contents will be updated
                // if the file does not exist then the file will be created and the contents will be appended to it
                savingsAccountAccount.saveToTextFile(filePath);
            } catch (Exception e) {
                // the filePath is invalid so the information of the new user will not be stored
            }
        });
    }
}

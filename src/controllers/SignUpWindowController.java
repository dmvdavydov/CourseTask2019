package controllers;

import java.net.URL;
import java.util.ResourceBundle;

import helpers.User;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import sql.DatabaseHandler;


public class SignUpWindowController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField nameField;

    @FXML
    private TextField secondNameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button nextButton;

    @FXML
    private TextField loginField;

    @FXML
    void initialize() {

        nextButton.setOnAction(event -> {

            signUpNewUser();

        });

    }

    private void signUpNewUser() {
        DatabaseHandler dbHandler = new DatabaseHandler();

        String firstname = nameField.getText();
        String secondname = secondNameField.getText();
        String email = emailField.getText();
        String login = loginField.getText();
        String password = passwordField.getText();

        User user = new User(firstname, secondname, email, login, password);
        dbHandler.signUpUser(user);
    }
}

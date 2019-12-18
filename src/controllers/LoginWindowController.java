package controllers;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import helpers.User;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import sql.DatabaseHandler;

public class LoginWindowController {
    private static String currentUserLogin;

    public static String getCurrentUserLogin() {
        return currentUserLogin;
    }

    private static String currentUserPassword;

    public static String getCurrentUserPassword() {
        return currentUserPassword;
    }


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField loginField;

    @FXML
    private Button signUpButton;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button nextButton;

    @FXML
    void initialize() {
        nextButton.setOnAction(event -> {
            String loginText = loginField.getText().trim();
            String loginPassword = passwordField.getText().trim();

            if (!loginText.equals("") && !loginPassword.equals(""))
                loginUser(loginText, loginPassword);
            else
                System.out.println("Login or password is empty!");

        });

        signUpButton.setOnAction(event -> {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/views/SignUpWindow.fxml"));
            try {
                loader.load();
            } catch (IOException e) {
                e.printStackTrace();
            }

            Parent root = loader.getRoot();
            Stage stage = new Stage();
            stage.setTitle("Окно регистрации ");
            stage.setScene(new Scene(root));
            stage.showAndWait();
        });
    }

    private void loginUser(String loginText, String loginPassword) {
        DatabaseHandler dbHandler = new DatabaseHandler();
        User user = new User();
        user.setLogin(loginText);
        user.setPassword(loginPassword);
        ResultSet result = dbHandler.getUser(user);

        int counter = 0;

        try {
            while (result.next()) {
                counter++;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        if (counter >= 1) {
            System.out.println("Success!");
            try {
                currentUserLogin = loginText;
                currentUserPassword = loginPassword;

                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/views/MainWindow.fxml"));
                Parent root = loader.load();
                Stage stage = new Stage();
                stage.setTitle("Рабочее окно ");
                stage.setScene(new Scene(root, 910, 690));
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

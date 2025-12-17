package org.example.ui;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.db.DB;

import java.sql.Connection;

public class LoginController {

    @FXML private TextField userField;
    @FXML private PasswordField passField;
    @FXML private Label statusLabel;

    // URL FIXE (tu peux changer le nom de la DB ici)
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/job_db";

    @FXML
    public void initialize() {
        statusLabel.setText("");
        // tu peux aussi pré-remplir userField si tu veux :
        // userField.setText("admin");
    }

    @FXML
    public void onLogin() {
        String user = userField.getText().trim();
        String pass = passField.getText();

        if (user.isEmpty()) {
            statusLabel.setText("User obligatoire.");
            return;
        }

        try {
            Connection cn = DB.connect(JDBC_URL, user, pass);

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
            Scene scene = new Scene(loader.load(), 1100, 700);

            MainController mc = loader.getController();
            mc.setSession(cn, user); // role = user (simple pour l’instant)

            Stage stage = (Stage) userField.getScene().getWindow();
            stage.setTitle("Job UI - Main");
            stage.setScene(scene);

        } catch (Exception e) {
            statusLabel.setText("Erreur login: " + e.getMessage());
            new Alert(Alert.AlertType.ERROR, "Login error: " + e.getMessage()).showAndWait();
        }
    }
}

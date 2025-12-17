package org.example.ui;

import org.example.db.DB;
import org.example.db.DB;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;

public class LoginController {

    @FXML private TextField urlField;
    @FXML private TextField userField;
    @FXML private PasswordField passField;
    @FXML private Label statusLabel;

    @FXML
    public void initialize() {
        // Exemple MySQL local : adapte NOM_BDD
        urlField.setText("jdbc:mysql://localhost:3306/job-desc?useSSL=false&serverTimezone=UTC");
    }

    @FXML
    public void onLogin() {
        String url = urlField.getText().trim();
        String user = userField.getText().trim();
        String pass = passField.getText();

        if (url.isEmpty() || user.isEmpty()) {
            statusLabel.setText("URL et user obligatoires.");
            return;
        }

        try {
            Connection cn = DB.connect(url, user, pass);

            // Rôle simple basé sur username (adapte selon vos comptes DB)
            // ex: administrateur / gestionnaire / utilisateur_standard / recruteur
            String role = user.toLowerCase();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/main.fxml"));
            Scene scene = new Scene(loader.load(), 980, 620);

            MainController controller = loader.getController();
            controller.setSession(cn, role);

            Stage stage = (Stage) statusLabel.getScene().getWindow();
            stage.setTitle("Job UI");
            stage.setScene(scene);

        } catch (Exception e) {
            statusLabel.setText("Erreur : " + e.getMessage());
        }
    }
}

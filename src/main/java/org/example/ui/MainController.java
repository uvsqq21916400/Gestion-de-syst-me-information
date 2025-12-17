package org.example.ui;

import org.example.dao.JobDao;
import org.example.model.Job;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.util.List;

public class MainController {

    @FXML private Label roleLabel;
    @FXML private Button addBtn;
    @FXML private Button editBtn;
    @FXML private Button deleteBtn;

    @FXML private TableView<Job> table;
    @FXML private TableColumn<Job, Integer> idCol;
    @FXML private TableColumn<Job, String> titleCol;
    @FXML private TableColumn<Job, String> typeCol;
    @FXML private TableColumn<Job, Object> dateCol;
    @FXML private TableColumn<Job, String> companyCol;

    private Connection cn;
    private String role;
    private JobDao jobDao;

    public void setSession(Connection cn, String role) {
        this.cn = cn;
        this.role = role;
        this.jobDao = new JobDao(cn);

        roleLabel.setText("Role: " + role);
        applyPrivileges(role);
        onRefresh();
    }

    @FXML
    public void initialize() {
        idCol.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getJobId()));
        titleCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTitle()));
        typeCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getWorkType()));
        dateCol.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getPostingDate()));
        companyCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCompanyName()));
    }

    private void applyPrivileges(String role) {
        // Exemple (à adapter à vos comptes):
        // admin/gestionnaire: CRUD
        // recruteur: update seulement
        // standard: read-only
        boolean canInsert = role.contains("admin") || role.contains("gestionnaire");
        boolean canUpdate = canInsert || role.contains("recruteur");
        boolean canDelete = role.contains("admin") || role.contains("gestionnaire");

        addBtn.setDisable(!canInsert);
        editBtn.setDisable(!canUpdate);
        deleteBtn.setDisable(!canDelete);
    }

    @FXML
    public void onRefresh() {
        try {
            List<Job> jobs = jobDao.findAll();
            table.setItems(FXCollections.observableArrayList(jobs));
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Refresh error: " + e.getMessage()).showAndWait();
        }
    }

    // Bonus: on laisse ces actions en place (pas encore implémentées)
    @FXML
    public void onAdd() {
        new Alert(Alert.AlertType.INFORMATION, "Add: à implémenter (INSERT)").showAndWait();
    }

    @FXML
    public void onEdit() {
        new Alert(Alert.AlertType.INFORMATION, "Edit: à implémenter (UPDATE)").showAndWait();
    }

    @FXML
    public void onDelete() {
        new Alert(Alert.AlertType.INFORMATION, "Delete: à implémenter (DELETE)").showAndWait();
    }

    @FXML
    public void onLogout() {
        try {
            if (cn != null) cn.close();
        } catch (Exception ignored) {}

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
            Scene scene = new Scene(loader.load(), 560, 280);

            Stage stage = (Stage) roleLabel.getScene().getWindow();
            stage.setTitle("Job UI - Login");
            stage.setScene(scene);
        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Logout error: " + e.getMessage()).showAndWait();
        }
    }
}


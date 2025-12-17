package org.example.ui;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.example.dao.CompanyDao;
import org.example.dao.JobDao;
import org.example.dao.LocationDao;
import org.example.model.Company;
import org.example.model.Job;
import org.example.model.JobDetails;
import org.example.model.Location;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

public class MainController {

    // Top bar
    @FXML private Label roleLabel;
    @FXML private Button addBtn;
    @FXML private Button editBtn;
    @FXML private Button deleteBtn;
    @FXML private TextField searchField;


    // Table (liste)
    @FXML private TableView<Job> table;
    @FXML private TableColumn<Job, Integer> idCol;
    @FXML private TableColumn<Job, String> titleCol;
    @FXML private TableColumn<Job, String> typeCol;
    @FXML private TableColumn<Job, Object> dateCol;
    @FXML private TableColumn<Job, String> companyCol;

    // Panneau détails (droite)
    @FXML private Label dTitle, dRole, dWorkType, dSalary, dDate, dCompany, dLocation, dPortal;
    @FXML private TextArea dDesc, dResp, dBenefits;

    private Connection cn;
    private String role;

    private JobDao jobDao;
    private CompanyDao companyDao;
    private LocationDao locationDao;

    // cache locations pour afficher un libellé propre
    private List<Location> cachedLocations = List.of();

    /**
     * Appelé depuis LoginController après login OK.
     */
    public void setSession(Connection cn, String role) {
        this.cn = cn;
        this.role = role;

        this.jobDao = new JobDao(cn);
        this.companyDao = new CompanyDao(cn);
        this.locationDao = new LocationDao(cn);

        roleLabel.setText("Role: " + role);
        applyPrivileges(role);

        // charger cache locations (pour afficher dLocation)
        try {
            cachedLocations = locationDao.findAll();
        } catch (Exception ignored) {
            cachedLocations = List.of();
        }

        onRefresh();

    }

    @FXML
    public void initialize() {
        // 1) Bind des colonnes -> comment afficher les propriétés de Job
        idCol.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getJobId()));
        titleCol.setCellValueFactory(c -> new SimpleStringProperty(nz(c.getValue().getJobTitle())));
        typeCol.setCellValueFactory(c -> new SimpleStringProperty(nz(c.getValue().getWorkType())));
        dateCol.setCellValueFactory(c -> new SimpleObjectProperty<>(c.getValue().getPostingDate()));
        companyCol.setCellValueFactory(c -> new SimpleStringProperty(nz(c.getValue().getCompanyName())));

        // 2) Filtrage (search)
        filteredData = new FilteredList<>(masterData, p -> true);
        table.setItems(filteredData);

        // 3) Affichage détails quand on sélectionne une ligne
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldV, job) -> {
            if (job == null) clearDetails();
            else loadDetails(job.getJobId());
        });

        // 4) Entrée = lancer la recherche
        searchField.setOnAction(e -> onSearch());
    }



    private void applyPrivileges(String role) {
        String r = (role == null) ? "" : role.toLowerCase();

        boolean canInsert = r.contains("admin") || r.contains("gestionnaire");
        boolean canUpdate = canInsert || r.contains("recruteur");
        boolean canDelete = r.contains("admin") || r.contains("gestionnaire");

        addBtn.setDisable(!canInsert);
        editBtn.setDisable(!canUpdate);
        deleteBtn.setDisable(!canDelete);
    }

    @FXML
    public void onRefresh() {
        try {
            masterData.setAll(jobDao.findAll()); // ou ta méthode existante
            // si un filtre est déjà dans le champ, on le réapplique
            onSearch();
        } catch (Exception e) {
            showDbError("Refresh error", e);
        }
    }


    private void loadDetails(int jobId) {
        try {
            JobDetails d = jobDao.findDetailsById(jobId);
            if (d == null) {
                clearDetails();
                return;
            }

            dTitle.setText(nz(d.getJobTitle()));
            dRole.setText(nz(d.getRole()));
            dWorkType.setText(nz(d.getWorkType()));
            dSalary.setText(nz(d.getSalaryRange()));
            dDate.setText(d.getPostingDate() == null ? "" : d.getPostingDate().toString());
            dCompany.setText(nz(d.getCompanyName()));
            dPortal.setText(nz(d.getJobPortal()));

            dLocation.setText(locationLabel(d.getLocationId()));

            dDesc.setText(nz(d.getJobDescription()));
            dResp.setText(nz(d.getResponsibilities()));
            dBenefits.setText(nz(d.getBenefits()));

        } catch (Exception e) {
            new Alert(Alert.AlertType.ERROR, "Load details error: " + e.getMessage()).showAndWait();
        }
    }

    @FXML
    public void onAdd() {
        try {
            JobDetails created = openJobDialog(null);
            if (created == null) return;

            jobDao.insert(created);
            onRefresh();
        } catch (Exception e) {
            showDbError("Insert", e);
        }
    }

    @FXML
    public void onEdit() {
        Job selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Sélectionne un job d'abord.").showAndWait();
            return;
        }

        try {
            JobDetails existing = jobDao.findDetailsById(selected.getJobId());
            if (existing == null) {
                new Alert(Alert.AlertType.WARNING, "Impossible de charger les détails pour ce job.").showAndWait();
                return;
            }

            JobDetails updated = openJobDialog(existing);
            if (updated == null) return;

            jobDao.update(updated);
            onRefresh();
        } catch (Exception e) {
            showDbError("Update", e);
        }
    }

    @FXML
    public void onDelete() {
        Job selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            new Alert(Alert.AlertType.WARNING, "Sélectionne un job d'abord.").showAndWait();
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm delete");
        confirm.setHeaderText("Supprimer job #" + selected.getJobId() + " ?");
        confirm.setContentText(selected.getJobTitle());

        Optional<ButtonType> res = confirm.showAndWait();
        if (res.isEmpty() || res.get() != ButtonType.OK) return;

        try {
            jobDao.delete(selected.getJobId());
            onRefresh();
        } catch (Exception e) {
            showDbError("Delete", e);
        }
    }

    @FXML
    public void onSearch() {
        String q = (searchField.getText() == null) ? "" : searchField.getText().trim().toLowerCase();

        if (q.isEmpty()) {
            filteredData.setPredicate(j -> true);
            return;
        }

        // si l’utilisateur tape un nombre, on tente une recherche par ID
        Integer idQuery = null;
        try {
            idQuery = Integer.parseInt(q);
        } catch (NumberFormatException ignored) {
        }

        Integer finalIdQuery = idQuery;

        filteredData.setPredicate(job -> {
            if (job == null) return false;

            // recherche par ID
            if (finalIdQuery != null && job.getJobId() == finalIdQuery) {
                return true;
            }

            String title = job.getJobTitle() == null ? "" : job.getJobTitle().toLowerCase();
            String company = job.getCompanyName() == null ? "" : job.getCompanyName().toLowerCase();

            return title.contains(q) || company.contains(q);
        });
    }


    private final ObservableList<Job> masterData = FXCollections.observableArrayList();
    private FilteredList<Job> filteredData;



    /**
     * Ouvre le dialog job_form.fxml.
     * @param editing null = Add, sinon Edit (pré-rempli)
     * @return JobDetails à sauvegarder (ou null si cancel)
     */
    private JobDetails openJobDialog(JobDetails editing) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/job_form.fxml"));
        Scene formScene = new Scene(loader.load(), 780, 640);

        JobFormController fc = loader.getController();

        // Lookups FK
        List<Company> companies = companyDao.findAll();
        List<Location> locations = locationDao.findAll();
        cachedLocations = locations; // refresh cache pour dLocation
        fc.setLookups(companies, locations);

        if (editing != null) {
            fc.setEditing(editing);
            fc.selectCompanyByName(editing.getCompanyName());
            fc.selectLocationById(editing.getLocationId());
        }

        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(editing == null ? "Add Job" : "Edit Job");
        dialog.getDialogPane().setContent(formScene.getRoot());
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.initModality(Modality.APPLICATION_MODAL);

        // Validation avant OK
        Button okBtn = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
        okBtn.addEventFilter(javafx.event.ActionEvent.ACTION, ev -> {
            String err = fc.validateForm();
            if (err != null) {
                ev.consume();
                new Alert(Alert.AlertType.WARNING, err).showAndWait();
            }
        });

        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isEmpty() || result.get() != ButtonType.OK) return null;

        JobDetails toSave = fc.buildDetailsToSave();

        // Si edit : forcer l'ID original
        if (editing != null) {
            toSave = new JobDetails(
                    editing.getJobId(),
                    toSave.getRole(),
                    toSave.getWorkType(),
                    toSave.getSalaryRange(),
                    toSave.getPostingDate(),
                    toSave.getJobTitle(),
                    toSave.getJobPortal(),
                    toSave.getJobDescription(),
                    toSave.getResponsibilities(),
                    toSave.getBenefits(),
                    toSave.getCompanyName(),
                    toSave.getLocationId()
            );
        }

        return toSave;
    }

    private void showDbError(String action, Exception e) {
        new Alert(Alert.AlertType.ERROR, action + " error: " + e.getMessage()).showAndWait();
    }

    private String nz(String s) { return (s == null) ? "" : s; }

    private void clearDetails() {
        dTitle.setText("");
        dRole.setText("");
        dWorkType.setText("");
        dSalary.setText("");
        dDate.setText("");
        dCompany.setText("");
        dLocation.setText("");
        dPortal.setText("");
        dDesc.setText("");
        dResp.setText("");
        dBenefits.setText("");
    }

    private String locationLabel(int locationId) {
        for (Location l : cachedLocations) {
            if (l.getLocationId() == locationId) return l.toString() + " (id=" + locationId + ")";
        }
        return "id=" + locationId;
    }

    @FXML
    public void onLogout() {
        try { if (cn != null) cn.close(); } catch (Exception ignored) {}

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

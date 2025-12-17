package org.example.ui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import org.example.model.Company;
import org.example.model.JobDetails;
import org.example.model.Location;

import java.time.LocalDate;
import java.util.List;

public class JobFormController {

    @FXML private TextField titleField;
    @FXML private TextField roleField;
    @FXML private ComboBox<String> workTypeBox;
    @FXML private TextField salaryField;
    @FXML private DatePicker datePicker;
    @FXML private ComboBox<Company> companyBox;
    @FXML private ComboBox<Location> locationBox;
    @FXML private TextField portalField;
    @FXML private TextArea descArea;
    @FXML private TextArea respArea;
    @FXML private TextArea benefitsArea;

    private JobDetails editing; // null si Add

    @FXML
    public void initialize() {
        workTypeBox.getItems().setAll(
                "On-site", "Remote", "Hybrid", "Full-time", "Part-time", "Contract"
        );
        datePicker.setValue(LocalDate.now());
    }

    public void setLookups(List<Company> companies, List<Location> locations) {
        companyBox.getItems().setAll(companies);
        locationBox.getItems().setAll(locations);
    }

    public void setEditing(JobDetails d) {
        this.editing = d;

        titleField.setText(d.getJobTitle());
        roleField.setText(d.getRole());
        workTypeBox.setValue(d.getWorkType());
        salaryField.setText(nz(d.getSalaryRange()));
        datePicker.setValue(d.getPostingDate());
        portalField.setText(nz(d.getJobPortal()));

        descArea.setText(nz(d.getJobDescription()));
        respArea.setText(nz(d.getResponsibilities()));
        benefitsArea.setText(nz(d.getBenefits()));
    }

    public void selectCompanyByName(String companyName) {
        for (Company c : companyBox.getItems()) {
            if (c.getCompanyName().equals(companyName)) {
                companyBox.getSelectionModel().select(c);
                return;
            }
        }
    }

    public void selectLocationById(int locationId) {
        for (Location l : locationBox.getItems()) {
            if (l.getLocationId() == locationId) {
                locationBox.getSelectionModel().select(l);
                return;
            }
        }
    }

    public String validateForm() {
        if (titleField.getText() == null || titleField.getText().trim().isEmpty()) return "Job title obligatoire.";
        if (roleField.getText() == null || roleField.getText().trim().isEmpty()) return "Role obligatoire.";
        if (workTypeBox.getValue() == null || workTypeBox.getValue().trim().isEmpty()) return "Work type obligatoire.";
        if (datePicker.getValue() == null) return "Posting date obligatoire.";
        if (companyBox.getValue() == null) return "Company obligatoire.";
        if (locationBox.getValue() == null) return "Location obligatoire.";
        return null;
    }

    public JobDetails buildDetailsToSave() {
        int id = (editing == null) ? 0 : editing.getJobId(); // si AUTO_INCREMENT: 0 ignoré à l’insert

        return new JobDetails(
                id,
                roleField.getText().trim(),
                workTypeBox.getValue(),
                emptyToNull(salaryField.getText()),
                datePicker.getValue(),
                titleField.getText().trim(),
                emptyToNull(portalField.getText()),
                emptyToNull(descArea.getText()),
                emptyToNull(respArea.getText()),
                emptyToNull(benefitsArea.getText()),
                companyBox.getValue().getCompanyName(),
                locationBox.getValue().getLocationId()
        );
    }

    private String emptyToNull(String s) {
        if (s == null) return null;
        String t = s.trim();
        return t.isEmpty() ? null : t;
    }

    private String nz(String s) {
        return (s == null) ? "" : s;
    }
}


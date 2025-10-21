package com.group1.ui;

import com.group1.dao.AcademicPeriodDao;
import com.group1.dao.AcademicPeriodDaoImpl;
import com.group1.model.AcademicPeriod;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import java.util.List;
import java.util.Optional;

public class ManageAcademicPeriodsController {

    @FXML private TextField yearField;
    @FXML private TextField semesterField;
    @FXML private Label messageLabel;
    @FXML private TableView<AcademicPeriod> periodTableView;
    @FXML private TableColumn<AcademicPeriod, String> yearColumn;
    @FXML private TableColumn<AcademicPeriod, String> semesterColumn;

    private final AcademicPeriodDao academicPeriodDao = new AcademicPeriodDaoImpl();
    private final ObservableList<AcademicPeriod> periodList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("academic_year"));
        semesterColumn.setCellValueFactory(new PropertyValueFactory<>("semester"));
        periodTableView.setItems(periodList);
        loadAllPeriods();

        // Listener to populate the form when a table row is selected
        periodTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                yearField.setText(newSelection.getAcademic_year());
                semesterField.setText(newSelection.getSemester());
            }
        });
    }

    @FXML
    private void handleSavePeriod() {
        String year = yearField.getText();
        String semester = semesterField.getText();

        if (year.isEmpty() || semester.isEmpty()) {
            setMessage("Year and Semester cannot be empty.", Color.RED);
            return;
        }

        AcademicPeriod selectedPeriod = periodTableView.getSelectionModel().getSelectedItem();

        try {
            if (selectedPeriod == null) { // Creating new
                AcademicPeriod newPeriod = new AcademicPeriod();
                newPeriod.setAcademic_year(year);
                newPeriod.setSemester(semester);
                academicPeriodDao.savePeriod(newPeriod);
                setMessage("Academic Period added successfully!", Color.GREEN);
            } else { // Updating existing
                selectedPeriod.setAcademic_year(year);
                selectedPeriod.setSemester(semester);
                academicPeriodDao.updatePeriod(selectedPeriod);
                setMessage("Academic Period updated successfully!", Color.GREEN);
            }
            loadAllPeriods();
            clearForm();
        } catch (Exception e) {
            setMessage("Error: Could not save academic period.", Color.RED);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleDeletePeriod() {
        AcademicPeriod selectedPeriod = periodTableView.getSelectionModel().getSelectedItem();
        if (selectedPeriod == null) {
            setMessage("Please select a period from the table to delete.", Color.RED);
            return;
        }

        if (academicPeriodDao.isUsedInCourseSection(selectedPeriod.getPeriod_id())) {
            showAlert(Alert.AlertType.ERROR, "Deletion Failed", "Cannot delete this period because it is currently used by one or more course sections.");
            return;
        }

        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Confirm Deletion");
        confirmation.setHeaderText("Delete '" + selectedPeriod.getAcademic_year() + " - " + selectedPeriod.getSemester() + "'?");
        Optional<ButtonType> result = confirmation.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                academicPeriodDao.deletePeriod(selectedPeriod.getPeriod_id());
                setMessage("Period deleted successfully.", Color.GREEN);
                loadAllPeriods();
                clearForm();
            } catch (Exception e) {
                setMessage("Error deleting period.", Color.RED);
            }
        }
    }

    @FXML
    private void handleClearForm() {
        clearForm();
    }
    
    private void clearForm() {
        periodTableView.getSelectionModel().clearSelection();
        yearField.clear();
        semesterField.clear();
        messageLabel.setText("");
    }

    private void loadAllPeriods() {
        periodList.setAll(academicPeriodDao.getAllPeriods());
    }
    
    private void setMessage(String text, Color color) {
        messageLabel.setText(text);
        messageLabel.setTextFill(color);
    }
    
    private void showAlert(Alert.AlertType type, String title, String content) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
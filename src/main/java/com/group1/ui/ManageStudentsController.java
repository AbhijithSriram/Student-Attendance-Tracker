package com.group1.ui;

import com.group1.dao.StudentDao;
import com.group1.dao.StudentDaoImpl;
import com.group1.model.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import java.util.List;
import java.util.Optional;

public class ManageStudentsController {

    @FXML private TextField regNumberField;
    @FXML private TextField nameField;
    @FXML private Label messageLabel;
    @FXML private TableView<Student> studentTableView;
    @FXML private TableColumn<Student, String> regNumberColumn;
    @FXML private TableColumn<Student, String> nameColumn;

    private final StudentDao studentDao = new StudentDaoImpl();
    private final ObservableList<Student> studentList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        regNumberColumn.setCellValueFactory(new PropertyValueFactory<>("reg_number"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        studentTableView.setItems(studentList);
        loadAllStudents();

        studentTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) {
                regNumberField.setText(newV.getReg_number());
                nameField.setText(newV.getName());
                regNumberField.setDisable(true);
            } else {
                clearForm();
            }
        });
    }

    @FXML
    private void handleSaveStudent() {
        String regNumber = regNumberField.getText();
        String name = nameField.getText();

        if (regNumber.isEmpty() || name.isEmpty()) {
            setMessage("Registration number and name are required.", Color.RED);
            return;
        }

        Student student = new Student();
        student.setReg_number(regNumber);
        student.setName(name);

        try {
            if (regNumberField.isDisabled()) {
                studentDao.updateStudent(student);
                setMessage("Student updated successfully.", Color.GREEN);
            } else {
                studentDao.saveStudent(student);
                setMessage("Student added successfully.", Color.GREEN);
            }
            loadAllStudents();
            clearForm();
        } catch (Exception e) {
            setMessage("Error saving student. Registration number may already exist.", Color.RED);
        }
    }
    
    @FXML
    private void handleClearForm() {
        clearForm();
    }
    
    private void clearForm() {
        studentTableView.getSelectionModel().clearSelection();
        regNumberField.clear();
        nameField.clear();
        regNumberField.setDisable(false);
        setMessage("", Color.BLACK);
    }

    @FXML
    private void handleDeleteStudent() {
        Student selected = studentTableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            setMessage("Please select a student to delete.", Color.RED);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Delete Student: " + selected.getName());
        alert.setContentText("Are you sure? This will also delete all of their attendance records and enrollments.");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                studentDao.deleteStudent(selected.getReg_number());
                setMessage("Student deleted successfully.", Color.GREEN);
                loadAllStudents();
            } catch (Exception e) {
                setMessage("Error deleting student.", Color.RED);
            }
        }
    }

    private void loadAllStudents() {
        studentList.setAll(studentDao.getAllStudents());
    }
    
    private void setMessage(String text, Color color) {
        messageLabel.setText(text);
        messageLabel.setTextFill(color);
    }
}
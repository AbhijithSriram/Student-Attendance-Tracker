package com.group1.ui;

import com.group1.dao.ProfessorDao;
import com.group1.dao.ProfessorDaoImpl;
import com.group1.model.Professor;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.mindrot.jbcrypt.BCrypt;

import java.io.IOException;
import java.util.Objects;

public class LoginController {
    private AttendanceTrackerApp app;
    @FXML private TextField employeeIdField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;
    @FXML private Button loginButton;

    private final ProfessorDao professorDao = new ProfessorDaoImpl();

    public void setApp(AttendanceTrackerApp app) {
        this.app = app;
    }

    @FXML
    private void handleLogin() {
        String employeeId = employeeIdField.getText();
        String password = passwordField.getText();

        if (employeeId.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Employee ID and Password cannot be empty.");
            return;
        }

        Professor professor = professorDao.getProfessorById(employeeId);

        if (professor != null && BCrypt.checkpw(password, professor.getPassword_hash())) {
            app.showDashboard(professor); // Call main app to switch scenes
        } else {
            messageLabel.setText("Invalid Employee ID or Password.");
        }
    }


    @FXML
    private void openRegisterWindow() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/RegisterView.fxml")));
            Stage registerStage = new Stage();
            registerStage.setTitle("Professor Registration");
            registerStage.setScene(new Scene(root));
            registerStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
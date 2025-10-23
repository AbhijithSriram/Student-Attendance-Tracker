package com.group1.ui;

import com.group1.model.Professor;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class DashboardController {
    private AttendanceTrackerApp app;
    @FXML private Label welcomeLabel;
    private Professor loggedInProfessor;

    public void setApp(AttendanceTrackerApp app) {
        this.app = app;
    }

    @FXML
    private void handleLogout() {
        app.showLoginScreen();
    }
    
    public void initData(Professor professor) {
        this.loggedInProfessor = professor;
        welcomeLabel.setText("Welcome, " + professor.getName());
    }

    @FXML
    private void openMarkAttendanceWindow() {
        openWindow("/MarkAttendanceView.fxml", "Mark Attendance", controller -> {
            if (controller instanceof MarkAttendanceController) {
                ((MarkAttendanceController) controller).initData(loggedInProfessor);
            }
        });
    }

    @FXML
    private void openManageSessionsWindow() {
        openWindow("/ManageSessionsView.fxml", "Manage Sessions", controller -> {
            if (controller instanceof ManageSessionsController) {
                ((ManageSessionsController) controller).initData(loggedInProfessor);
            }
        });
    }

    @FXML
    private void openManageCoursesWindow() {
         openWindow("/ManageCoursesView.fxml", "Manage Courses", controller -> {
            if (controller instanceof ManageCoursesController) {
                ((ManageCoursesController) controller).initData(loggedInProfessor);
            }
        });
    }

    @FXML
    private void openManageStudentsWindow() {
        openWindow("/ManageStudentsView.fxml", "Manage Students", null);
    }

    @FXML
    private void openReportsWindow() {
        openWindow("/ReportsView.fxml", "View Reports", controller -> {
            if (controller instanceof ReportsController) {
                ((ReportsController) controller).initData(loggedInProfessor);
            }
        });
    }

    private void openWindow(String fxmlFile, String title, ControllerInitializer initializer) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            
            if (initializer != null) {
                initializer.init(loader.getController());
            }

            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            showAlert("Error", "Could not open the window: " + title);
        }
    }

    @FunctionalInterface
    interface ControllerInitializer {
        void init(Object controller);
    }
    
    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
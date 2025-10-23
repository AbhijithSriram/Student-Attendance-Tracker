package com.group1.ui;

import com.group1.model.Professor;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AttendanceTrackerApp extends Application {

    private Stage primaryStage;

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showLoginScreen();
    }

    public void showLoginScreen() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/LoginView.fxml"));
            Parent root = loader.load();
            
            LoginController controller = loader.getController();
            controller.setApp(this);

            primaryStage.setTitle("Attendance Tracker - Login");
            primaryStage.setScene(new Scene(root));
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void showDashboard(Professor professor) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/DashboardView.fxml"));
            Parent root = loader.load();
            
            DashboardController controller = loader.getController();
            controller.setApp(this);
            controller.initData(professor);

            primaryStage.setTitle("Attendance Dashboard");
            primaryStage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
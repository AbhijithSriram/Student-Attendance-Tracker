package com.group1.ui;

import com.group1.dao.ProfessorDao;
import com.group1.dao.ProfessorDaoImpl;
import com.group1.model.Professor;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import org.mindrot.jbcrypt.BCrypt;

public class RegisterController {

    @FXML private TextField nameField;
    @FXML private TextField employeeIdField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Label messageLabel;

    private final ProfessorDao professorDao = new ProfessorDaoImpl();

    @FXML
    private void handleRegister() {
        String name = nameField.getText();
        String employeeId = employeeIdField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();

        if (name.isEmpty() || employeeId.isEmpty() || email.isEmpty() || password.isEmpty()) {
            messageLabel.setTextFill(Color.RED);
            messageLabel.setText("All fields are required.");
            return;
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());
        Professor newProfessor = new Professor();
        newProfessor.setName(name);
        newProfessor.setEmployee_id(employeeId);
        newProfessor.setEmail(email);
        newProfessor.setPassword_hash(hashedPassword);

        try {
            professorDao.saveProfessor(newProfessor);
            messageLabel.setTextFill(Color.GREEN);
            messageLabel.setText("Registration Successful! You can now close this window and log in.");
        } catch (Exception e) {
            messageLabel.setTextFill(Color.RED);
            messageLabel.setText("Registration failed. Employee ID or Email may already exist.");
            e.printStackTrace();
        }
    }
}
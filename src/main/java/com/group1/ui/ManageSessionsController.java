package com.group1.ui;

import com.group1.dao.*;
import com.group1.model.*;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import java.util.List;
import java.util.Optional;

public class ManageSessionsController {

    @FXML private ComboBox<CourseSection> courseSectionComboBox;
    @FXML private ListView<ClassSession> sessionsListView;
    @FXML private TableView<AttendanceEntry> attendanceTableView;
    @FXML private TableColumn<AttendanceEntry, String> regNumberColumn;
    @FXML private TableColumn<AttendanceEntry, String> studentNameColumn;
    @FXML private TableColumn<AttendanceEntry, ComboBox<String>> statusColumn;
    @FXML private Label messageLabel;

    private Professor loggedInProfessor;
    private final CourseSectionDao courseSectionDao = new CourseSectionDaoImpl();
    private final ClassSessionDao classSessionDao = new ClassSessionDaoImpl();
    private final AttendanceDao attendanceDao = new AttendanceDaoImpl();
    private final ObservableList<AttendanceEntry> attendanceEntries = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        regNumberColumn.setCellValueFactory(cellData -> cellData.getValue().studentRegNumberProperty());
        studentNameColumn.setCellValueFactory(cellData -> cellData.getValue().studentNameProperty());
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusComboBoxProperty());
        attendanceTableView.setItems(attendanceEntries);

        // Listen for course section selection
        courseSectionComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> loadSessionsForSection(newV));
        
        // Listen for session selection
        sessionsListView.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> loadAttendanceForSession(newV));
    }

    public void initData(Professor professor) {
        this.loggedInProfessor = professor;
        loadProfessorSections();
    }

    private void loadProfessorSections() {
        List<CourseSection> sections = courseSectionDao.getSectionsByProfessor(loggedInProfessor.getEmployee_id());
        courseSectionComboBox.getItems().addAll(sections);
        courseSectionComboBox.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(CourseSection section) {
                if (section == null) return null;
                return String.format("%s - %s (%s - %s)", section.getCourse().getCourse_name(), section.getSection_name(),
                        section.getAcademicPeriod().getAcademic_year(), section.getAcademicPeriod().getSemester());
            }
            @Override
            public CourseSection fromString(String string) { return null; }
        });
    }

    private void loadSessionsForSection(CourseSection section) {
        sessionsListView.getItems().clear();
        attendanceEntries.clear();
        if (section != null) {
            sessionsListView.getItems().setAll(classSessionDao.getSessionsBySection(section.getSection_id()));
            sessionsListView.setCellFactory(lv -> new ListCell<>() {
                @Override
                protected void updateItem(ClassSession session, boolean empty) {
                    super.updateItem(session, empty);
                    setText(empty ? null : session.getSession_date().toString() + " - " + session.getTopic_covered());
                }
            });
        }
    }

    private void loadAttendanceForSession(ClassSession session) {
        attendanceEntries.clear();
        if (session != null) {
            List<Attendance> records = attendanceDao.getAttendanceBySession(session.getSession_id());
            for (Attendance record : records) {
                attendanceEntries.add(new AttendanceEntry(record.getStudent(), record.getStatus()));
            }
        }
    }

    @FXML
    private void handleSaveChanges() {
        ClassSession selectedSession = sessionsListView.getSelectionModel().getSelectedItem();
        if (selectedSession == null || attendanceEntries.isEmpty()) {
            setMessage("Please select a session and ensure attendance is loaded.", Color.RED);
            return;
        }

        try {
            for (AttendanceEntry entry : attendanceEntries) {
                Optional<Attendance> existingRecordOpt = attendanceDao.getAttendanceForStudentInSession(selectedSession.getSession_id(), entry.getStudent().getReg_number());
                if (existingRecordOpt.isPresent()) {
                    Attendance recordToUpdate = existingRecordOpt.get();
                    recordToUpdate.setStatus(entry.getStatusComboBox().getValue());
                    attendanceDao.updateAttendance(recordToUpdate);
                }
            }
            setMessage("Attendance changes saved successfully.", Color.GREEN);
        } catch (Exception e) {
            setMessage("Error saving changes: " + e.getMessage(), Color.RED);
        }
    }

    private void setMessage(String text, Color color) {
        messageLabel.setText(text);
        messageLabel.setTextFill(color);
    }

    // Inner class for TableView
    public static class AttendanceEntry {
        private final Student student;
        private final ComboBox<String> statusComboBox;
        
        public AttendanceEntry(Student student, String status) {
            this.student = student;
            this.statusComboBox = new ComboBox<>();
            this.statusComboBox.getItems().addAll("Present", "Absent");
            this.statusComboBox.setValue(status);
        }
        
        public Student getStudent() { return student; }
        public ComboBox<String> getStatusComboBox() { return statusComboBox; }
        
        public javafx.beans.property.StringProperty studentRegNumberProperty() { return new javafx.beans.property.SimpleStringProperty(student.getReg_number()); }
        public javafx.beans.property.StringProperty studentNameProperty() { return new javafx.beans.property.SimpleStringProperty(student.getName()); }
        public javafx.beans.property.ObjectProperty<ComboBox<String>> statusComboBoxProperty() { return new javafx.beans.property.SimpleObjectProperty<>(statusComboBox); }
    }
}
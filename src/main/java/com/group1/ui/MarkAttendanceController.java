package com.group1.ui;

import com.group1.dao.*;
import com.group1.model.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

public class MarkAttendanceController {

    @FXML private ComboBox<CourseSection> courseSectionComboBox;
    @FXML private DatePicker sessionDatePicker;
    @FXML private TextField startTimeField;
    @FXML private TextField endTimeField;
    @FXML private TextField topicField;
    @FXML private Label messageLabel;
    @FXML private TableView<AttendanceEntry> attendanceTableView;
    @FXML private TableColumn<AttendanceEntry, String> regNumberColumn;
    @FXML private TableColumn<AttendanceEntry, String> studentNameColumn;
    @FXML private TableColumn<AttendanceEntry, ComboBox<String>> statusColumn;
    @FXML private Button saveButton;

    private Professor loggedInProfessor;
    private ClassSession currentSession;
    private final CourseSectionDao courseSectionDao = new CourseSectionDaoImpl();
    private final EnrollmentDao enrollmentDao = new EnrollmentDaoImpl();
    private final ClassSessionDao classSessionDao = new ClassSessionDaoImpl();
    private final AttendanceDao attendanceDao = new AttendanceDaoImpl();
    private final ObservableList<AttendanceEntry> attendanceEntries = FXCollections.observableArrayList();
    
    @FXML
    public void initialize() {
        sessionDatePicker.setValue(LocalDate.now());
        regNumberColumn.setCellValueFactory(cellData -> cellData.getValue().studentRegNumberProperty());
        studentNameColumn.setCellValueFactory(cellData -> cellData.getValue().studentNameProperty());
        statusColumn.setCellValueFactory(cellData -> cellData.getValue().statusComboBoxProperty());
        attendanceTableView.setItems(attendanceEntries);
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

    @FXML
    private void handleStartSession() {
        CourseSection section = courseSectionComboBox.getValue();
        LocalDate date = sessionDatePicker.getValue();
        String startStr = startTimeField.getText();
        String endStr = endTimeField.getText();
        String topic = topicField.getText();

        if (section == null || date == null || startStr.isEmpty() || endStr.isEmpty() || topic.isEmpty()) {
            setMessage("All session fields are required.", Color.RED);
            return;
        }

        try {
            ClassSession session = new ClassSession();
            session.setCourseSection(section);
            session.setSession_date(Date.valueOf(date));
            session.setStart_time(Time.valueOf(LocalTime.parse(startStr)));
            session.setEnd_time(Time.valueOf(LocalTime.parse(endStr)));
            session.setTopic_covered(topic);

            this.currentSession = classSessionDao.saveSession(session);
            loadStudentsForSession(currentSession);
            
            setMessage("Session created. Please mark attendance.", Color.GREEN);
            attendanceTableView.setDisable(false);
            saveButton.setDisable(false);

        } catch (DateTimeParseException e) {
            setMessage("Invalid time format. Use HH:MM.", Color.RED);
        } catch (Exception e) {
            setMessage("Error creating session: " + e.getMessage(), Color.RED);
            e.printStackTrace();
        }
    }
    
    private void loadStudentsForSession(ClassSession session) {
        attendanceEntries.clear();
        List<Enrollment> enrollments = enrollmentDao.getEnrollmentsBySection(session.getCourseSection().getSection_id());
        for(Enrollment e : enrollments) {
            attendanceEntries.add(new AttendanceEntry(e.getStudent()));
        }
    }

    @FXML
    private void handleSaveAttendance() {
        if (currentSession == null || attendanceEntries.isEmpty()) {
            setMessage("No session started or no students to mark.", Color.RED);
            return;
        }

        try {
            for (AttendanceEntry entry : attendanceEntries) {
                // Check if attendance already exists for this student in this session
                Optional<Attendance> existingRecord = attendanceDao.getAttendanceForStudentInSession(currentSession.getSession_id(), entry.getStudent().getReg_number());
                
                Attendance record;
                if(existingRecord.isPresent()) {
                    record = existingRecord.get(); // We will update this record
                } else {
                    record = new Attendance(); // This is a new record
                    record.setClassSession(currentSession);
                    record.setStudent(entry.getStudent());
                }
                
                record.setStatus(entry.getStatusComboBox().getValue());

                if(existingRecord.isPresent()) {
                    attendanceDao.updateAttendance(record);
                } else {
                    attendanceDao.saveAttendance(record);
                }
            }
            setMessage("Attendance saved successfully for session " + currentSession.getSession_id(), Color.GREEN);
        } catch (Exception e) {
            setMessage("Error saving attendance: " + e.getMessage(), Color.RED);
            e.printStackTrace();
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
        
        public AttendanceEntry(Student student) {
            this.student = student;
            this.statusComboBox = new ComboBox<>();
            this.statusComboBox.getItems().addAll("Present", "Absent");
            this.statusComboBox.setValue("Present");
        }
        
        public Student getStudent() { return student; }
        public ComboBox<String> getStatusComboBox() { return statusComboBox; }
        
        public javafx.beans.property.StringProperty studentRegNumberProperty() { return new javafx.beans.property.SimpleStringProperty(student.getReg_number()); }
        public javafx.beans.property.StringProperty studentNameProperty() { return new javafx.beans.property.SimpleStringProperty(student.getName()); }
        public javafx.beans.property.ObjectProperty<ComboBox<String>> statusComboBoxProperty() { return new javafx.beans.property.SimpleObjectProperty<>(statusComboBox); }
    }
}
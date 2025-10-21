package com.group1.ui;

import com.group1.dao.*;
import com.group1.model.*;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class ReportsController {

    @FXML private ComboBox<CourseSection> courseSectionComboBox;
    @FXML private DatePicker startDatePicker;
    @FXML private DatePicker endDatePicker;
    @FXML private TableView<AttendanceReportEntry> reportTableView;
    @FXML private TableColumn<AttendanceReportEntry, String> regNumberColumn;
    @FXML private TableColumn<AttendanceReportEntry, String> studentNameColumn;
    @FXML private TableColumn<AttendanceReportEntry, Number> totalClassesColumn;
    @FXML private TableColumn<AttendanceReportEntry, Number> classesAttendedColumn;
    @FXML private TableColumn<AttendanceReportEntry, Number> percentageColumn;

    private Professor currentProfessor;
    private final CourseSectionDao courseSectionDao = new CourseSectionDaoImpl();
    private final ClassSessionDao classSessionDao = new ClassSessionDaoImpl();
    private final AttendanceDao attendanceDao = new AttendanceDaoImpl();
    private final EnrollmentDao enrollmentDao = new EnrollmentDaoImpl();

    private final ObservableList<AttendanceReportEntry> reportData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        regNumberColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStudent().getReg_number()));
        studentNameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getStudent().getName()));
        totalClassesColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getTotalClasses()));
        classesAttendedColumn.setCellValueFactory(cellData -> new SimpleIntegerProperty(cellData.getValue().getClassesAttended()));
        percentageColumn.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPercentage()));
        reportTableView.setItems(reportData);
        
        startDatePicker.setValue(LocalDate.now().minusMonths(1));
        endDatePicker.setValue(LocalDate.now());
    }

    public void initData(Professor professor) {
        this.currentProfessor = professor;
        loadProfessorSections();
    }

    private void loadProfessorSections() {
        List<CourseSection> sections = courseSectionDao.getSectionsByProfessor(currentProfessor.getEmployee_id());
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
    private void handleGenerateReport() {
        CourseSection section = courseSectionComboBox.getValue();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        reportData.clear();
        if (section == null || startDate == null || endDate == null) {
            showAlert(Alert.AlertType.WARNING, "Input Needed", "Please select a course section and both start/end dates.");
            return;
        }

        List<ClassSession> sessionsInRange = classSessionDao.getSessionsBySection(section.getSection_id()).stream()
                .filter(s -> !s.getSession_date().toLocalDate().isBefore(startDate) && !s.getSession_date().toLocalDate().isAfter(endDate))
                .collect(Collectors.toList());

        int totalClasses = sessionsInRange.size();
        if (totalClasses == 0) {
            showAlert(Alert.AlertType.INFORMATION, "No Data", "No class sessions found in the selected date range.");
            return;
        }

        List<Enrollment> enrollments = enrollmentDao.getEnrollmentsBySection(section.getSection_id());
        for (Enrollment enrollment : enrollments) {
            Student student = enrollment.getStudent();
            List<Attendance> studentAttendance = attendanceDao.getAttendanceByStudent(student.getReg_number());

            int attendedCount = 0;
            for (Attendance record : studentAttendance) {
                boolean sessionInRange = sessionsInRange.stream().anyMatch(s -> s.getSession_id() == record.getClassSession().getSession_id());
                if (sessionInRange && "Present".equalsIgnoreCase(record.getStatus())) {
                    attendedCount++;
                }
            }
            reportData.add(new AttendanceReportEntry(student, totalClasses, attendedCount));
        }
    }
    
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public static class AttendanceReportEntry {
        private final Student student;
        private final int totalClasses;
        private final int classesAttended;

        public AttendanceReportEntry(Student student, int totalClasses, int classesAttended) {
            this.student = student;
            this.totalClasses = totalClasses;
            this.classesAttended = classesAttended;
        }

        public Student getStudent() { return student; }
        public int getTotalClasses() { return totalClasses; }
        public int getClassesAttended() { return classesAttended; }
        public double getPercentage() {
            return totalClasses == 0 ? 0.0 : ((double) classesAttended / totalClasses) * 100.0;
        }
    }
}
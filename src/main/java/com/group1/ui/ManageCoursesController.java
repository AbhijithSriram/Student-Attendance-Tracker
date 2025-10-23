package com.group1.ui;

import com.group1.dao.*;
import com.group1.model.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ManageCoursesController {
    
    @FXML private TextField courseCodeField;
    @FXML private TextField courseNameField;
    @FXML private TextField creditsField;
    @FXML private TableView<Course> courseTableView;
    @FXML private TableColumn<Course, String> courseCodeCol;
    @FXML private TableColumn<Course, String> courseNameCol;
    @FXML private TableColumn<Course, Integer> creditsCol;
    
    @FXML private ComboBox<Course> sectionCourseComboBox;
    @FXML private ComboBox<AcademicPeriod> sectionPeriodComboBox;
    @FXML private TextField sectionNameField;
    @FXML private TableView<CourseSection> sectionTableView;
    @FXML private TableColumn<CourseSection, String> sectionCourseCol;
    @FXML private TableColumn<CourseSection, String> sectionNameCol;
    @FXML private TableColumn<CourseSection, String> sectionPeriodCol;

    @FXML private Label messageLabel;
    
    private Professor loggedInProfessor;
    private final CourseDao courseDao = new CourseDaoImpl();
    private final AcademicPeriodDao academicPeriodDao = new AcademicPeriodDaoImpl();
    private final CourseSectionDao courseSectionDao = new CourseSectionDaoImpl();

    private final ObservableList<Course> courseList = FXCollections.observableArrayList();
    private final ObservableList<CourseSection> sectionList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        courseCodeCol.setCellValueFactory(new PropertyValueFactory<>("course_code"));
        courseNameCol.setCellValueFactory(new PropertyValueFactory<>("course_name"));
        creditsCol.setCellValueFactory(new PropertyValueFactory<>("credits"));
        courseTableView.setItems(courseList);
        
        sectionCourseCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCourse().getCourse_name()));
        sectionNameCol.setCellValueFactory(new PropertyValueFactory<>("section_name"));
        sectionPeriodCol.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getAcademicPeriod().getAcademic_year() + " - " + cellData.getValue().getAcademicPeriod().getSemester()));
        sectionTableView.setItems(sectionList);
        
        loadAllCourses();
        loadAllPeriods();
        
        courseTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV != null) {
                courseCodeField.setText(newV.getCourse_code());
                courseNameField.setText(newV.getCourse_name());
                creditsField.setText(String.valueOf(newV.getCredits()));
                courseCodeField.setDisable(true);
            } else {
                clearCourseForm();
            }
        });

        sectionCourseComboBox.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(Course course) {
                return course == null ? "" : course.getCourse_code() + " - " + course.getCourse_name();
            }
            @Override
            public Course fromString(String string) { return null; }
        });
        
        sectionPeriodComboBox.setConverter(new javafx.util.StringConverter<>() {
            @Override
            public String toString(AcademicPeriod period) {
                return period == null ? "" : period.getAcademic_year() + " - " + period.getSemester();
            }
            @Override
            public AcademicPeriod fromString(String string) { return null; }
        });
    }
    
    
    public void initData(Professor professor) {
        this.loggedInProfessor = professor;
        loadMySections();
    }
    
    private void loadAllCourses() {
        List<Course> courses = courseDao.getAllCourses();
        courseList.setAll(courses);
        sectionCourseComboBox.getItems().setAll(courses);
    }
    
    private void loadAllPeriods() {
        List<AcademicPeriod> periods = academicPeriodDao.getAllPeriods();
        sectionPeriodComboBox.getItems().setAll(periods);
    }

    private void loadMySections() {
        sectionList.setAll(courseSectionDao.getSectionsByProfessor(loggedInProfessor.getEmployee_id()));
    }

    @FXML
    private void handleSaveCourse() {
        String code = courseCodeField.getText();
        String name = courseNameField.getText();
        if (code.isEmpty() || name.isEmpty() || creditsField.getText().isEmpty()) {
            setMessage("Course code, name, and credits are required.", Color.RED);
            return;
        }
        
        try {
            int credits = Integer.parseInt(creditsField.getText());
            Course course = new Course();
            course.setCourse_code(code);
            course.setCourse_name(name);
            course.setCredits(credits);
            
            if (courseCodeField.isDisabled()) {
                courseDao.updateCourse(course);
                setMessage("Course updated successfully.", Color.GREEN);
            } else {
                courseDao.saveCourse(course);
                setMessage("Course saved successfully.", Color.GREEN);
            }
            loadAllCourses();
            clearCourseForm();
        } catch (NumberFormatException e) {
            setMessage("Credits must be a number.", Color.RED);
        } catch (Exception e) {
            setMessage("Error saving course. Code may already exist.", Color.RED);
        }
    }
    
    @FXML
    private void handleClearCourseForm() {
        clearCourseForm();
    }

    private void clearCourseForm() {
        courseTableView.getSelectionModel().clearSelection();
        courseCodeField.clear();
        courseNameField.clear();
        creditsField.clear();
        courseCodeField.setDisable(false);
        setMessage("", Color.BLACK);
    }

    @FXML
    private void handleDeleteCourse() {
        Course selected = courseTableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            setMessage("Please select a course to delete.", Color.RED);
            return;
        }
        
        if (courseDao.hasAttendanceRecords(selected.getCourse_code())) {
            setMessage("Cannot delete course: it has existing attendance records.", Color.RED);
            return;
        }

        try {
            courseDao.deleteCourse(selected.getCourse_code());
            setMessage("Course deleted successfully.", Color.GREEN);
            loadAllCourses();
        } catch (Exception e) {
            setMessage("Error deleting course. It may be used in a section.", Color.RED);
        }
    }

    @FXML
    private void handleSaveSection() {
        Course course = sectionCourseComboBox.getValue();
        AcademicPeriod period = sectionPeriodComboBox.getValue();
        String name = sectionNameField.getText();

        if (course == null || period == null || name.isEmpty()) {
            setMessage("Please select a course, period, and provide a section name.", Color.RED);
            return;
        }

        CourseSection section = new CourseSection();
        section.setCourse(course);
        section.setAcademicPeriod(period);
        section.setSection_name(name);
        section.setProfessor(loggedInProfessor);
        
        try {
            courseSectionDao.saveSection(section);
            setMessage("Course section created successfully.", Color.GREEN);
            loadMySections();
            sectionNameField.clear();
        } catch (Exception e) {
            setMessage("Error creating course section.", Color.RED);
        }
    }
    
    @FXML
    private void handleClearSectionForm() {
        sectionCourseComboBox.getSelectionModel().clearSelection();
        sectionPeriodComboBox.getSelectionModel().clearSelection();
        sectionNameField.clear();
        setMessage("", Color.BLACK);
    }

    @FXML
    private void handleDeleteSection() {
        CourseSection selected = sectionTableView.getSelectionModel().getSelectedItem();
        if (selected == null) {
            setMessage("Please select a section from 'My Course Sections' to delete.", Color.RED);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirm Deletion");
        alert.setHeaderText("Delete Section: " + selected.getCourse().getCourse_name() + " - " + selected.getSection_name());
        alert.setContentText("Are you sure? This will permanently delete the section and ALL associated attendance records.");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                courseSectionDao.deleteSection(selected.getSection_id());
                setMessage("Section deleted successfully.", Color.GREEN);
                loadMySections();
            } catch (Exception e) {
                setMessage("Error deleting section.", Color.RED);
                e.printStackTrace();
            }
        }
    }

    @FXML
    private void openManagePeriodsWindow() {
        try {
            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/ManageAcademicPeriodsView.fxml")));
            Stage periodStage = new Stage();
            periodStage.setTitle("Manage Academic Periods");
            periodStage.setScene(new Scene(root));
            periodStage.initModality(Modality.APPLICATION_MODAL);
            periodStage.showAndWait();
            loadAllPeriods();
        } catch (IOException e) {
            setMessage("Could not open the periods window.", Color.RED);
            e.printStackTrace();
        }
    }
    
    @FXML
    private void handleManageRoster() {
        CourseSection selectedSection = sectionTableView.getSelectionModel().getSelectedItem();
        if (selectedSection == null) {
            setMessage("Please select a section from 'My Course Sections' to manage its roster.", Color.RED);
            return;
        }
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/EnrollmentView.fxml"));
            Parent root = loader.load();
            EnrollmentController controller = loader.getController();
            controller.initData(selectedSection);
            Stage rosterStage = new Stage();
            rosterStage.setTitle("Manage Student Roster");
            rosterStage.setScene(new Scene(root));
            rosterStage.initModality(Modality.APPLICATION_MODAL);
            rosterStage.showAndWait();
        } catch (IOException e) {
            setMessage("Could not open the roster management window.", Color.RED);
            e.printStackTrace();
        }
    }
    
    private void setMessage(String text, Color color) {
        messageLabel.setText(text);
        messageLabel.setTextFill(color);
    }
}
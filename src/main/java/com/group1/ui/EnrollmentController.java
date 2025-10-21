package com.group1.ui;

import com.group1.dao.EnrollmentDao;
import com.group1.dao.EnrollmentDaoImpl;
import com.group1.dao.StudentDao;
import com.group1.dao.StudentDaoImpl;
import com.group1.model.CourseSection;
import com.group1.model.Enrollment;
import com.group1.model.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.util.List;
import java.util.stream.Collectors;

public class EnrollmentController {

    @FXML private Label sectionLabel;
    @FXML private TextField searchField; // New UI component
    @FXML private ListView<Student> allStudentsListView;
    @FXML private ListView<Student> enrolledStudentsListView;
    @FXML private Label messageLabel;

    private CourseSection currentSection;
    private final StudentDao studentDao = new StudentDaoImpl();
    private final EnrollmentDao enrollmentDao = new EnrollmentDaoImpl();

    // Master list for unenrolled students
    private final ObservableList<Student> unenrolledStudentsMaster = FXCollections.observableArrayList();
    // Filtered list that will be displayed to the user
    private FilteredList<Student> filteredUnenrolledStudents;
    // List for enrolled students
    private final ObservableList<Student> enrolledStudents = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Wrap the master list in a filtered list
        filteredUnenrolledStudents = new FilteredList<>(unenrolledStudentsMaster, p -> true);
        
        // Bind the list views to their respective lists
        allStudentsListView.setItems(filteredUnenrolledStudents);
        enrolledStudentsListView.setItems(enrolledStudents);

        // Add a listener to the search field to filter the list as the user types
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredUnenrolledStudents.setPredicate(student -> {
                if (newValue == null || newValue.isEmpty()) {
                    return true; // If search text is empty, display all students
                }
                String lowerCaseFilter = newValue.toLowerCase();
                // Match against student name or registration number
                if (student.getName().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                } else if (student.getReg_number().toLowerCase().contains(lowerCaseFilter)) {
                    return true;
                }
                return false; // Does not match
            });
        });

        // Customize how students are displayed in the lists
        javafx.util.Callback<ListView<Student>, ListCell<Student>> cellFactory = lv -> new ListCell<>() {
            @Override
            protected void updateItem(Student student, boolean empty) {
                super.updateItem(student, empty);
                setText(empty ? "" : student.getReg_number() + " - " + student.getName());
            }
        };
        allStudentsListView.setCellFactory(cellFactory);
        enrolledStudentsListView.setCellFactory(cellFactory);
    }

    public void initData(CourseSection section) {
        this.currentSection = section;
        sectionLabel.setText("Managing Roster for: " + section.getCourse().getCourse_name() + " (" + section.getSection_name() + ")");
        loadStudents();
    }

    private void loadStudents() {
        // Get all students in the system
        List<Student> allStudentsInDb = studentDao.getAllStudents();

        // Get students already enrolled in this section
        List<Enrollment> enrollments = enrollmentDao.getEnrollmentsBySection(currentSection.getSection_id());
        List<Student> enrolledStudentsInDb = enrollments.stream().map(Enrollment::getStudent).collect(Collectors.toList());

        // Populate the "enrolled" list
        enrolledStudents.setAll(enrolledStudentsInDb);

        // Populate the master "unenrolled" list (this now works correctly because of equals/hashCode in Student.java)
        allStudentsInDb.removeAll(enrolledStudentsInDb);
        unenrolledStudentsMaster.setAll(allStudentsInDb);
    }

    @FXML
    private void handleEnrollStudent() {
        Student selectedStudent = allStudentsListView.getSelectionModel().getSelectedItem();
        if (selectedStudent == null) {
            setMessage("Please select a student from the 'All Students' list to enroll.", Color.RED);
            return;
        }

        Enrollment newEnrollment = new Enrollment();
        newEnrollment.setStudent(selectedStudent);
        newEnrollment.setCourseSection(currentSection);

        try {
            enrollmentDao.saveEnrollment(newEnrollment);
            // Move the student from the master unenrolled list to the enrolled list
            unenrolledStudentsMaster.remove(selectedStudent);
            enrolledStudents.add(selectedStudent);
            setMessage("Student enrolled successfully.", Color.GREEN);
        } catch (Exception e) {
            setMessage("Error enrolling student.", Color.RED);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleUnenrollStudent() {
        Student selectedStudent = enrolledStudentsListView.getSelectionModel().getSelectedItem();
        if (selectedStudent == null) {
            setMessage("Please select a student from the 'Enrolled Students' list to unenroll.", Color.RED);
            return;
        }

        // Find the specific enrollment record to delete it
        List<Enrollment> enrollments = enrollmentDao.getEnrollmentsBySection(currentSection.getSection_id());
        Enrollment toDelete = enrollments.stream()
                .filter(e -> e.getStudent().getReg_number().equals(selectedStudent.getReg_number()))
                .findFirst().orElse(null);

        if (toDelete != null) {
            try {
                enrollmentDao.deleteEnrollment(toDelete.getEnrollment_id());
                // Move student from the enrolled list back to the master unenrolled list
                enrolledStudents.remove(selectedStudent);
                unenrolledStudentsMaster.add(selectedStudent);
                setMessage("Student unenrolled successfully.", Color.GREEN);
            } catch (Exception e) {
                setMessage("Error unenrolling student.", Color.RED);
                e.printStackTrace();
            }
        }
    }
    
    private void setMessage(String text, Color color) {
        messageLabel.setText(text);
        messageLabel.setTextFill(color);
    }
}
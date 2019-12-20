import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class CourseHistoryGUI implements Initializable {

    @FXML
    private Label                                                totalCoursesTakenLabel;
    @FXML
    private Label                                                overallGPALabel;
    @FXML
    private TableView<CourseAndGradeGUI>                         tableView;
    @FXML
    private TableColumn<CourseAndGradeGUI, SimpleStringProperty> courseIdColumn;
    @FXML
    private TableColumn<CourseAndGradeGUI, SimpleStringProperty> gradeColumn;
    @FXML
    private TableColumn<CourseAndGradeGUI, SimpleStringProperty> statusColumn;

    private double overallGpa        = 0.0;
    private int    totalCoursesTaken = 0;

    public ObservableList<CourseAndGradeGUI> getData() {

        ObservableList<CourseAndGradeGUI> result  = FXCollections.observableArrayList();
        Student                           student = (Student) Database.accounts.get(MenuGUI.uuid);

        // add all courses that the student has taken
        ArrayList<CourseAndGrade> coursesTaken = student.getTakenCoursesList();
        for (CourseAndGrade i : coursesTaken) {
            result.add(new CourseAndGradeGUI(i.getCourseId(), i.getGrade(), "Closed"));
            overallGpa += Double.parseDouble(i.getGrade());
        }

        // calculate overallGpa and totalCoursesTaken
        if (!coursesTaken.isEmpty()) {
            overallGpa = overallGpa / coursesTaken.size();
        }
        totalCoursesTaken = coursesTaken.size();

        // add all courses that the student is taking
        ArrayList<String> coursesTaking = student.getCurrentCoursesList();
        for (String i : coursesTaking) {
            result.add(new CourseAndGradeGUI(i, "Grading", "Ongoing"));
        }
        return result;
    }

    /**
     * Initialize the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // set up the columns in the table
        courseIdColumn.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        // import data
        tableView.setItems(getData());

        // update labels
        totalCoursesTakenLabel.setText(String.valueOf(totalCoursesTaken));
        overallGPALabel.setText(String.format("%.2f", overallGpa));

        // no drag and drop
        tableView.getColumns().addListener(new ListChangeListener<TableColumn<CourseAndGradeGUI, ?>>() {
            @Override
            public void onChanged(Change<? extends TableColumn<CourseAndGradeGUI, ?>> c) {
                c.next();
                if (c.wasReplaced()) {
                    tableView.getColumns().clear();
                    tableView.getColumns().addAll(Arrays.asList(courseIdColumn, gradeColumn, statusColumn));
                }
            }
        });
    }

}

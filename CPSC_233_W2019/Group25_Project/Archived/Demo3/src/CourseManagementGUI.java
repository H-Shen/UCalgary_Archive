import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.ResourceBundle;

public class CourseManagementGUI implements Initializable {

    @FXML
    private TableView<CourseForGUI>                                   coursesCurrentlyTeachingTable;
    @FXML
    private TableColumn<CourseForGUI, SimpleStringProperty>           courseIdColumn;
    @FXML
    private TableColumn<CourseForGUI, SimpleStringProperty>           countOfStudentsColumn;
    @FXML
    private TableView<StudentWithGradeForGUI>                         coursesGraderTable;
    @FXML
    private TableColumn<StudentWithGradeForGUI, SimpleStringProperty> uuidColumn;
    @FXML
    private TableColumn<StudentWithGradeForGUI, SimpleStringProperty> gradeColumn;
    @FXML
    private TextArea                                                  fullNameTextArea;
    @FXML
    private TextArea                                                  emailAddressTextArea;

    public void gradeTextFieldKeyReleased() {
        // TODO
    }

    public void updateOnAction() {
        // TODO
    }

    public ObservableList<CourseForGUI> getDataForCoursesCurrentlyTeachingTable() {
        ObservableList<CourseForGUI> result  = FXCollections.observableArrayList();
        Faculty                      faculty = (Faculty) Database.accounts.get(MenuGUI.uuid);
        ArrayList<String>            temp    = faculty.getCoursesTeaching();
        for (String i : temp) {
            result.add(new CourseForGUI(Database.courses.get(i)));
        }
        return result;
    }

    public ObservableList<StudentWithGradeForGUI> getDataForCoursesGraderTable(String courseId) {
        ObservableList<StudentWithGradeForGUI> result = FXCollections.observableArrayList();
        HashSet<String>                        temp   = Database.courses.get(courseId).getStudentsWhoAreTaking();
        for (String i : temp) {
            result.add(new StudentWithGradeForGUI(i));
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

        // set up the columns in coursesCurrentlyTeachingTable
        courseIdColumn.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        countOfStudentsColumn.setCellValueFactory(new PropertyValueFactory<>("studentsWhoAreTakingCount"));

        // set up the columns in coursesGraderTable
        uuidColumn.setCellValueFactory(new PropertyValueFactory<>("uuid"));
        gradeColumn.setCellValueFactory(new PropertyValueFactory<>("grade"));

        // import data to coursesCurrentlyTeachingTable
        coursesCurrentlyTeachingTable.setItems(getDataForCoursesCurrentlyTeachingTable());

        // no drag and drop for coursesCurrentlyTeachingTable
        coursesCurrentlyTeachingTable.getColumns().addListener(new ListChangeListener<TableColumn<CourseForGUI, ?>>() {
            @Override
            public void onChanged(Change<? extends TableColumn<CourseForGUI, ?>> c) {
                c.next();
                if (c.wasReplaced()) {
                    coursesCurrentlyTeachingTable.getColumns().clear();
                    coursesCurrentlyTeachingTable.getColumns().addAll(
                            courseIdColumn,
                            countOfStudentsColumn
                    );
                }
            }
        });


        // when an item in coursesCurrentlyTeachingTable is selected, coursesGraderTable will be
        // concurrently updated
        coursesCurrentlyTeachingTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
                //Check whether item is selected
                CourseForGUI temp = coursesCurrentlyTeachingTable.getSelectionModel().getSelectedItem();
                if (temp != null) {
                    // update coursesGraderTable
                    coursesGraderTable.setItems(getDataForCoursesGraderTable(temp.getCourseId()));
                }
            }
        });

        // no drag and drop for coursesGraderTable
        coursesGraderTable.getColumns().addListener(new ListChangeListener<TableColumn<StudentWithGradeForGUI, ?>>() {
            @Override
            public void onChanged(Change<? extends TableColumn<StudentWithGradeForGUI, ?>> c) {
                c.next();
                if (c.wasReplaced()) {
                    coursesGraderTable.getColumns().clear();
                    coursesGraderTable.getColumns().add(uuidColumn);
                    coursesGraderTable.getColumns().add(gradeColumn);
                }
            }
        });

        // when an item in coursesGraderTable is selected, fullNameTextArea and emailAddressTextArea will be
        // concurrently updated
        coursesGraderTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object oldValue, Object newValue) {
                //Check whether item is selected
                StudentWithGradeForGUI temp = coursesGraderTable.getSelectionModel().getSelectedItem();
                if (temp != null) {
                    // update fullNameTextArea and emailAddressTextArea
                    User tempUser = Database.accounts.get(temp.getUuid());
                    fullNameTextArea.setText(tempUser.getFullName());
                    emailAddressTextArea.setText(tempUser.getEmailAddress());
                }
            }
        });
    }
}
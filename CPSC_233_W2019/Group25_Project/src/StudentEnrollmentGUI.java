import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.sql.Timestamp;

/**
 * The {@code StudentEnrollmentGUI} class defines the layout and logic of the window that the student can enroll
 * courses in it.
 *
 * @author Group 25
 * @date 2019/04/11
 */
public class StudentEnrollmentGUI implements Initializable {

    /**
     * @Fields Properties and components used in GUI
     */
    @FXML
    private TableView<CourseEnrollmentGUI>                         tableOfOpenCourses;
    @FXML
    private TableColumn<CourseEnrollmentGUI, SimpleStringProperty> courseIdColumn;
    @FXML
    private TableColumn<CourseEnrollmentGUI, SimpleStringProperty> facultyFullNameColumn;
    @FXML
    private ObservableList<CourseEnrollmentGUI>                    coursesList;

    public static void main0(String[] args) {
        Database.initialization();
        int counter = 0;
        for (User i : Database.accounts.values()) {
            if ("STUDENT".equals(i.getRole())) {
                ++counter;
                if (counter == 100) {
                    return;
                }
                Student p = (Student) i;

                if (!p.getCurrentCoursesListAsString().contains("STAT213") && !p.getTakenCoursesListAsString().contains(
                        "STAT213")) {
                    Random r      = new Random();
                    double random = 2.9 + r.nextDouble() * (4 - 2.9);
                    String m      = String.format("%.1f", random);
                    if ("3.0".equals(m)) {
                        m = "3";
                    }
                    if ("4.0".equals(m)) {
                        m = "4";
                    }
                    p.addTakenCourses(new CourseAndGrade("STAT213", m));
                    Database.updateStudentTakenCourses(p.getUuid(), p.getTakenCoursesListAsString());
                    System.out.println(p.getUuid() + " " + m);
                }
            }
        }
    }

    /**
     * The method defines the event when the user presses "Enroll" button.
     *
     * @date 2019/04/12
     */
    public void enrollOnClicked() {
        CourseEnrollmentGUI obj = tableOfOpenCourses.getSelectionModel().getSelectedItem();
        if (obj != null) {
            boolean ifConfirm = Utility.showConfirmation("Confirmation", "Are you sure to enroll the course?",
                    "Warning: Once you enrolled the course, contact the administrator if you want to drop it.");
            if (ifConfirm) {

                Student student = (Student) Database.accounts.get(MenuGUI.uuid);

                // update the student in Database.account
                student.addCurrentCourses(obj.getCourseId());

                // update the course in Database.courses
                Database.courses.get(obj.getCourseId()).addStudentsWhoAreTaking(student.getUuid());

                // commit to STUDENT.db
                Database.updateStudentCurrentCourses(student.getUuid(), student.getCurrentCoursesListAsString());

                // delete the item in courseList
                initializeCoursesList();

                // update tableOfOpenCourses
                tableOfOpenCourses.setItems(coursesList);

                // log
                Log.enrollACourse(new Timestamp(System.currentTimeMillis()), MenuGUI.uuid, obj.getCourseId(),
                        obj.getFacultyUuid());

                // pop-out a window to show the student has enrolled successfully
                Utility.showAlert("Notification", null, "You successfully enrolled the course!");
            }
        }
    }

    /**
     * Initialize coursesList
     *
     * @date 2019/04/08
     */
    public void initializeCoursesList() {

        // initialization of courses taken, courses taking, courses remained
        Student         student                 = (Student) Database.accounts.get(MenuGUI.uuid);
        HashSet<String> coursesTakenOrTakingSet = new HashSet<>();
        HashSet<String> coursesTaken            = new HashSet<>();
        HashSet<String> remainingCourses        = new HashSet<>();
        // filter
        ArrayList<CourseAndGrade> tempArr0 = student.getTakenCoursesList();
        for (CourseAndGrade i : tempArr0) {
            coursesTakenOrTakingSet.add(i.getCourseId());
            coursesTaken.add(i.getCourseId());
        }
        coursesTakenOrTakingSet.addAll(student.getCurrentCoursesList());
        // add to remainingCourses
        for (String i : Database.courses.keySet()) {
            if (!coursesTakenOrTakingSet.contains(i)) {
                remainingCourses.add(i);
            }
        }

        // initialization of all open courses which are available to the student
        HashSet<String> temp = new HashSet<>();
        for (User i : Database.accounts.values()) {
            if (i.getRole().equals(Constants.ROLE[1])) {
                Faculty tempFaculty = (Faculty) i;
                temp.addAll(tempFaculty.getCoursesTeaching());
            }
        }

        // obtain a set contains all anti-req courses
        HashSet<String> antireqSet = new HashSet<>();
        for (String i : coursesTakenOrTakingSet) {
            antireqSet.addAll(Database.courses.get(i).getAntirequisites());
        }

        // obtain an array list contains all open courses for the student
        ArrayList<String> openCoursesList = new ArrayList<>();
        boolean           isCourseValid;
        for (String i : temp) {
            isCourseValid = true;
            if (remainingCourses.contains(i)) {
                // make sure no violations of anti-requisites
                if (!antireqSet.contains(i)) {
                    // make sure the student satisfied all pre-requisites
                    HashSet<String> tempHashSet = Database.courses.get(i).getPrerequisites();
                    for (String j : tempHashSet) {
                        if (!coursesTaken.contains(j)) {
                            isCourseValid = false;
                            break;
                        }
                    }
                    if (isCourseValid) {
                        openCoursesList.add(i);
                    }
                }
            }
        }
        // sort openCoursesList
        Collections.sort(openCoursesList);
        // wrap all items in openCoursesList to coursesList
        coursesList = FXCollections.observableArrayList();
        for (String i : openCoursesList) {
            for (User user : Database.accounts.values()) {
                if (user.getRole().equals(Constants.ROLE[1])) {
                    Faculty faculty = (Faculty) user;
                    if (faculty.getCoursesTeaching().contains(i)) {
                        coursesList.add(new CourseEnrollmentGUI(i, faculty.getFullName(), faculty.getUuid()));
                    }
                }
            }
        }
    }

    /**
     * Initialize the controller class.
     *
     * @param url Uniform Resource Locator
     * @param rb  Resource Bundle
     * @date 2019/04/08
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // set up the columns in the table
        courseIdColumn.setCellValueFactory(new PropertyValueFactory<>("courseId"));
        facultyFullNameColumn.setCellValueFactory(new PropertyValueFactory<>("facultyFullName"));

        // no drag and drop of the table
        tableOfOpenCourses.getColumns().addListener((ListChangeListener<TableColumn<CourseEnrollmentGUI, ?>>) c -> {
            c.next();
            if (c.wasReplaced()) {
                tableOfOpenCourses.getColumns().clear();
                tableOfOpenCourses.getColumns().add(courseIdColumn);
                tableOfOpenCourses.getColumns().add(facultyFullNameColumn);
            }
        });

        // initialize coursesList
        initializeCoursesList();

        // refresh the table
        tableOfOpenCourses.setItems(coursesList);
    }
}

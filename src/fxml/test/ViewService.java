/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 *
 * @author sunny
 */
public class ViewService {

    private Stage window;
    private FileChooser fileChooser;
    List<Student> studentList;
    List<Course> courseList;
    Map<String, Student> studentMap;
    List<String> inputs;
    BorderPane mainLayout;
    private ComboBox<String> depts;
    private ComboBox<String> semesters;
    private TextField session;
    private TextField year;
    private TextField chairman;
    private TextField controller;
    private TextField member1;
    private TextField member2;
    private TextField member3;
    private TextField member4;
    private TextField heldIn;
    MenuBar menuBar;
    MenuItem exportAsPDF;

    public void configureFileChooser(final FileChooser fileChooser) {

        fileChooser.setTitle("Open Student List");
//        fileChooser.setInitialDirectory(
//                new File(System.getProperty("/Users/sunny/NetBeansProjects/Tabulation"))
//        );
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("CSV", "*.csv")
        );
    }

    public void openFileForCourse(File file, BorderPane mainLayout, MenuItem exportAsPDF) {

        this.mainLayout = mainLayout;
        this.exportAsPDF = exportAsPDF;

        courseList = new ArrayList<>();
        studentList = new ArrayList<>();
        studentMap = new HashMap<String, Student>();

        BufferedReader br = null;
        String csvSplitBy = ",";
        String courseCode = null;
        String semester = null;
        String credit = null;
        String line = null;

        try {

            br = new BufferedReader(new FileReader(file));

            while ((line = br.readLine()) != null) {

                if (line == null || line.length() == 0) {
                    continue;
                }

                Course course = new Course();

                String array[] = line.split(csvSplitBy);
                courseCode = array[0].trim();
                semester = array[1].trim();
                credit = array[2].trim();

                course.setCourseCode(courseCode);
                course.setSemester(semester);
                course.setCredit(Double.valueOf(credit));

                this.courseList.add(course);

            }

//            CSVReader reader = new CSVReader(new FileReader(file), ',');
//            ColumnPositionMappingStrategy strat = new ColumnPositionMappingStrategy();
//            strat.setType(Course.class);
//            String[] columns = new String[]{"courseCode", "semester", "credit"};
//            strat.setColumnMapping(columns);
//            CsvToBean csv = new CsvToBean();
//            List courseList = csv.parse(strat, reader);
            if (this.courseList != null && !this.courseList.isEmpty()) {
                showCourseList();
            }

        } catch (FileNotFoundException e) {

            AlertMessage.showAlertMessage(Alert.AlertType.ERROR, "Course list not found!");
            e.printStackTrace();
        } catch (IOException ex) {

            AlertMessage.showAlertMessage(Alert.AlertType.ERROR, "Problem in opening course list!");
            Logger.getLogger(ViewService.class.getName()).log(
                    Level.SEVERE, null, ex
            );
        } catch (Exception e) {

            AlertMessage.showAlertMessage(Alert.AlertType.ERROR, e.getMessage());

        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void showCourseList() {

        fileChooser = new FileChooser();

        GridPane gridPane = new GridPane();

        gridPane.setPadding(new Insets(30, 30, 30, 30));
        gridPane.setVgap(10);
        gridPane.setHgap(30);

        int column = 0, row = 0;
        int rowCount = 0;

        Collections.sort(courseList);

        for (Course course : courseList) {

            if (rowCount == 5) {

                column += 2;
                row = row - 5;
                rowCount = 0;
            }

            Label label = new Label(course.getCourseCode());
            GridPane.setConstraints(label, column, row);

            String s = "";
            s += course.getCourseCode() + " " + course.getCredit();
            Button button = new Button("Browse");
            button.setId(s);

            button.setOnAction(e -> {

                System.out.print(button.getId());
                configureFileChooser(fileChooser);
                File file = fileChooser.showOpenDialog(window);

                if (file != null) {
                    openFileForStudent(file, button.getId().split(" "));
                    if (studentList.size() != 0) {
                        button.setStyle("-fx-background-color:green");
                        button.setText("Selected");
                    }

                }
            });
            button.setPrefSize(100, 20);
            button.setCursor(Cursor.HAND);
            GridPane.setConstraints(button, (column + 1), row);

            gridPane.getChildren().addAll(label, button);

            row++;
            rowCount++;

        }

        GridPane inputGrid = new GridPane();
        inputGrid.setPadding(new Insets(30, 30, 30, 30));
        inputGrid.setVgap(5);
        inputGrid.setHgap(20);

        inputGrid.add(new Label("Department"), 0, 0);
        inputGrid.add(getDepts(), 1, 0);

        inputGrid.add(new Label("Semester"), 2, 0);
        inputGrid.add(getSemesters(), 3, 0);

        inputGrid.add(new Label("Session"), 4, 0);
        session = new TextField();
        session.setPromptText("2011-12");
        inputGrid.add(session, 5, 0);

        inputGrid.add(new Label("Year"), 6, 0);
        year = new TextField();
        year.setPromptText("2016");
        inputGrid.add(year, 7, 0);

        inputGrid.add(new Label(" "), 0, 1);

        inputGrid.add(new Label("Chairman"), 0, 2);
        chairman = new TextField();
        inputGrid.add(chairman, 1, 2);
        inputGrid.add(new Label("Controller"), 2, 2);
        controller = new TextField();
        inputGrid.add(controller, 3, 2);
        inputGrid.add(new Label("Held In"), 6, 2);
        heldIn = new TextField();
        heldIn.setPromptText("October 2016");
        inputGrid.add(heldIn, 7, 2);

        inputGrid.add(new Label(" "), 0, 3);

        inputGrid.add(new Label("Members"), 0, 4);
        member1 = new TextField();
        member2 = new TextField();
        member3 = new TextField();
        member4 = new TextField();
        inputGrid.add(member1, 1, 4);
        inputGrid.add(member2, 3, 4);
        inputGrid.add(member3, 5, 4);
        inputGrid.add(member4, 7, 4);

        Button button = new Button("Generate");
        button.setOnAction(e -> {

            if (studentList.size() == 0 || !isValadidInputs()) {

                AlertMessage.showAlertMessage(Alert.AlertType.WARNING, "Student List or inputs are empty");

            } else {

                createTabulationView();

                PDFService pDFService = new PDFService(studentList, courseList, inputs);

                exportAsPDF.setVisible(true);

                exportAsPDF.setOnAction(et -> {

                    if (studentList.size() != 0 && isValadidInputs()) {

                        pDFService.generatePdf();
                        AlertMessage.showAlertMessage(Alert.AlertType.CONFIRMATION, "PDF Created Successfully!");
                    } else {
                        AlertMessage.showAlertMessage(Alert.AlertType.WARNING, "No Student List Selected");

                    }

                });
            }
        });
        button.setMinSize(100, 100);
        mainLayout.setRight(button);

        VBox vBox = new VBox();
        vBox.setPadding(new Insets(10, 10, 10, 10));
        vBox.setSpacing(30);

        ScrollPane sp = new ScrollPane();
        sp.setContent(gridPane);
        ScrollPane sp2 = new ScrollPane();
        sp2.setContent(inputGrid);

        Region region = new Region();
        vBox.getChildren().addAll(sp, region, sp2);

        mainLayout.setCenter(vBox);

    }

    private ComboBox<String> getDepts() {

        depts = new ComboBox<>();
        depts.getItems().addAll(
                "Forestry & Environmental Science",
                "Architecture",
                "Chemical Engineering & Polymer Science",
                "Civil & Environmental Engineering",
                "Computer Science & Engineering",
                "Electrical & Electronic Engineering",
                "Food Engineering & Tea Technology",
                "Industrial & Production Engineering",
                "Mechanical Engineering",
                "Petroleum and Mining Engineering",
                "Biochemistry and Molecular Biology",
                "Genetic Engineering & Biotechnology",
                "Business Administration",
                "Chemistry",
                "Geography and Environment",
                "Mathematics",
                "Physics",
                "Statistics",
                "Anthropology",
                "Bangla",
                "Economics",
                "English",
                "Political Studies",
                "Public Administration",
                "Social Work",
                "Sociology"
        );

        depts.setPromptText("Computer Science & Engineering");
        depts.setEditable(true);

        return depts;
    }

    private ComboBox<String> getSemesters() {

        semesters = new ComboBox<>();
        semesters.getItems().addAll(
                "1st SEMESTER",
                "2nd SEMESTER",
                "3rd SEMESTER",
                "4th SEMESTER",
                "5th SEMESTER",
                "6th SEMESTER",
                "7th SEMESTER",
                "8th SEMESTER",
                "9th SEMESTER"
        );

        semesters.setEditable(true);

        return semesters;
    }

    private void openFileForStudent(File file, String[] courseCredit) {

        BufferedReader br = null;
        String line = "";
        String csvSplitBy = ",";
        String regNo = null;
        String name = null;
        String gpa = null;
        String courseCode = null;
        String credit = null;

        try {

            br = new BufferedReader(new FileReader(file));

            while ((line = br.readLine()) != null) {

                if (line == null || line.length() == 0) {
                    continue;
                }

                String[] studentGpa = line.split(csvSplitBy);

                regNo = studentGpa[0].trim();
                name = studentGpa[1].trim();
                gpa = studentGpa[2].trim();
                courseCode = courseCredit[0].trim();
                credit = courseCredit[1].trim();

                Student student = checkForDuplicate(regNo);
                if (student == null) {

                    student = new Student(regNo);
                    student.setName(name);
                    this.studentList.add(student);
                    studentMap.put(regNo,student);

                }

                student
                        .getRegesteredCourse()
                        .add(new CourseReg(courseCode, Double.valueOf(credit), Double.valueOf(gpa), CgpaCalculator.getLetterGrade(Double.parseDouble(gpa))));

            }

        } catch (FileNotFoundException e) {

            AlertMessage.showAlertMessage(Alert.AlertType.ERROR, "Student list not found!");
            e.printStackTrace();
        } catch (IOException ex) {

            AlertMessage.showAlertMessage(Alert.AlertType.ERROR, "Problem in opening student list!");
            Logger.getLogger(ViewService.class.getName()).log(
                    Level.SEVERE, null, ex
            );

        } catch (Exception e) {

            studentList.clear();
            AlertMessage.showAlertMessage(Alert.AlertType.ERROR, e.getMessage());

        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public Student checkForDuplicate(String regNo) {

        if(studentMap.containsKey(regNo)){
            return studentMap.get(regNo);
        }
        return null;

    }

    private void createTabulationView() {

        //creating the main layout
        BorderPane borderPane = new BorderPane();
        borderPane.setPrefHeight(588.0);
        borderPane.setPrefWidth(900.0);
        borderPane.setPadding(new Insets(20, 10, 20, 10));

        //creating the top section
        VBox vBox = new VBox();
        vBox.setPadding(new Insets(20, 20, 20, 20));
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(2.0);
        Label university = new Label("SHAHJALAL UNIVERSITY OF SCIENCE & TECHNOLOGY SYLHET, BANGLADESH");
        Label tabulation = new Label("TABULATION SHEET");
        Label department = new Label(inputs.get(0));
        Label semester = new Label("B.Sc (Engg.) " + inputs.get(1) + " EXAMINATION " + inputs.get(2));
        Label session = new Label("SESSION:" + inputs.get(3) + " EXAMINATION HELD IN: " + inputs.get(4));
        Label date = new Label("Result Published On.....................");

        vBox.getChildren().addAll(university, tabulation, department, semester, session, date);

        borderPane.setTop(vBox);

        ScrollPane sp = new ScrollPane();
        GridPane grid = new GridPane();
        grid.setGridLinesVisible(true);
        sp.setPrefViewportWidth(860.0);
        sp.setPrefHeight(448.0);
        sp.setContent(grid);

        Label regNo = new Label("Reg No.");
        regNo.setPadding(new Insets(10, 10, 10, 10));
        grid.add(regNo, 0, 0);

        //start semester info
        HBox hBox2 = new HBox();
        hBox2.setSpacing(100);

        VBox nameBox = new VBox();
        nameBox.setAlignment(Pos.CENTER_RIGHT);
        nameBox.setSpacing(3);
        nameBox.getChildren().addAll(new Label(" "), new Label("Name"), new Label(""));

        VBox semesterBox = new VBox();
        semesterBox.setAlignment(Pos.CENTER_RIGHT);
        semesterBox.setSpacing(3);
        semesterBox.getChildren().addAll(new Label("Semester="), new Label("Course No="), new Label("Credit="));

        hBox2.getChildren().addAll(nameBox, semesterBox);
        grid.add(hBox2, 1, 0);

        //end semester info
        int row = 0;
        int col = 2;

        Collections.sort(courseList);
        Collections.sort(studentList);

        for (Course course : courseList) {

            VBox courseBox = new VBox();
            courseBox.setSpacing(4);
            courseBox.setPadding(new Insets(5, 5, 5, 5));

            Label semesterLabel = new Label(course.getSemester());
            semesterLabel.setStyle("-fx-font-size:10;");
            Label courseCode = new Label(course.getCourseCode());
            Label credit = new Label(String.format("%.02f", course.getCredit()));

            courseBox.getChildren().addAll(semesterLabel, courseCode, credit);

            courseBox.setAlignment(Pos.CENTER);
            grid.add(courseBox, col++, row);

        }
        Label totalCreditLabel = new Label("Total Credit");
        totalCreditLabel.setPadding(new Insets(5, 5, 5, 5));
        grid.add(totalCreditLabel, col++, row);

        Label totalGPALabel = new Label("Total GPA");
        totalGPALabel.setPadding(new Insets(5, 5, 5, 5));
        grid.add(totalGPALabel, col++, row);

        Label letterGradeLabel = new Label("Letter Grade");
        letterGradeLabel.setPadding(new Insets(5, 5, 5, 5));
        grid.add(letterGradeLabel, col++, row);

        row = 1;
        boolean flag = false;

        for (Student student : studentList) {

            Label regLabel = new Label(student.getRegNo());
            regLabel.setTextAlignment(TextAlignment.CENTER);
            regLabel.setPadding(new Insets(5, 5, 5, 5));
            grid.add(regLabel, 0, row);

            Label nameLabel = new Label(student.getName());
            nameLabel.setMaxWidth(250);
            nameLabel.setWrapText(true);
            nameLabel.setTextAlignment(TextAlignment.CENTER);
            nameLabel.setPadding(new Insets(5, 5, 5, 5));
            grid.add(nameLabel, 1, row);

            col = 2;

            double totalCredit = 0;
            double totalGpa = 0;
            for (Course course : courseList) {

                flag = false;
                for (CourseReg courseReg : student.getRegesteredCourse()) {

                    if (course.getCourseCode().equalsIgnoreCase(courseReg.getCourseCode())) {

                        HBox gpaLetterGrade = new HBox();
                        gpaLetterGrade.setSpacing(20);
                        gpaLetterGrade.setPadding(new Insets(5, 5, 5, 5));
                        gpaLetterGrade.setAlignment(Pos.CENTER_LEFT);
                        gpaLetterGrade.getChildren().addAll(new Label(String.format("%.02f", courseReg.getGpa())), new Label(courseReg.getLetterGrade()));

                        grid.add(gpaLetterGrade, col++, row);
                        flag = true;

                        if (courseReg.getGpa() != 0) {
                            totalCredit += courseReg.getCredit();
                            totalGpa += (courseReg.getGpa() * courseReg.getCredit());
                        }

                        break;

                    }
                }

                if (!flag) {
                    Label emptyLabel = new Label(" ");
                    grid.add(emptyLabel, col++, row);
                }

            }

            if (totalCredit != 0) {

                Label creditLabel = new Label(String.format("%.02f", totalCredit));
                creditLabel.setPadding(new Insets(5, 5, 5, 5));
                creditLabel.setTextAlignment(TextAlignment.CENTER);
                grid.add(creditLabel, col++, row);

                Label gpaLabel = new Label(String.format("%.02f", (totalGpa / totalCredit)));
                gpaLabel.setPadding(new Insets(5, 5, 5, 5));
                gpaLabel.setTextAlignment(TextAlignment.CENTER);
                grid.add(gpaLabel, col++, row);

                Label letterLabel = new Label(CgpaCalculator.getLetterGrade(totalGpa / totalCredit));
                letterLabel.setPadding(new Insets(5, 5, 5, 5));
                letterLabel.setTextAlignment(TextAlignment.CENTER);
                grid.add(letterLabel, col++, row);

                student.setTotalCredit(totalCredit);
                student.setTotalGpa(totalGpa / totalCredit);
                student.setLetterGrade(CgpaCalculator.getLetterGrade(totalGpa / totalCredit));

            } else {

                grid.add(new Label(" "), col++, row);
                grid.add(new Label(" "), col++, row);
                grid.add(new Label(" "), col++, row);

            }

            row++;

        }

        borderPane.setCenter(sp);

        StackPane pane = new StackPane();
        GridPane footergrid = new GridPane();

        footergrid.setAlignment(Pos.CENTER);
        footergrid.setVgap(5);
        footergrid.setHgap(20);
        pane.getChildren().add(footergrid);

        footergrid.add(new Label(""), 0, 0);
        footergrid.add(new Label("Chairman: "), 0, 1);

        footergrid.add(new Label(inputs.get(5)), 1, 1);

        footergrid.add(new Label("Controller: "), 3, 1);

        footergrid.add(new Label(inputs.get(6)), 4, 1);

        footergrid.add(new Label(" "), 0, 2);

        footergrid.add(new Label("Members: "), 0, 3);
        footergrid.add(new Label(inputs.get(7)), 1, 3);
        footergrid.add(new Label(inputs.get(8)), 2, 3);
        footergrid.add(new Label(inputs.get(9)), 3, 3);
        footergrid.add(new Label(inputs.get(10)), 4, 3);

        borderPane.setBottom(pane);

        mainLayout.setCenter(borderPane);
        mainLayout.setRight(new Label(""));

    }

    private boolean isValadidInputs() {

        inputs = new ArrayList<>();
        inputs.add("Department of Computer Science & Engineering");
        inputs.add("5th SEMESTER");
        inputs.add("2014");
        inputs.add("2011-12");
        inputs.add("October 2014");
        inputs.add("Md. Eamin Rahman");
        inputs.add("Md. Mujibur Rahman");
        inputs.add("Md Masum");
        inputs.add("Md. Saiful Islam");
        inputs.add("Husne Ara Chowdhury");
        inputs.add("Sabir Ismail");

        /*if (!chairman.getText().isEmpty()
         && !controller.getText().isEmpty()
         && !session.getText().isEmpty()
         && !year.getText().isEmpty()
         && !member1.getText().isEmpty()
         && !member2.getText().isEmpty()
         && !member3.getText().isEmpty()
         && !member4.getText().isEmpty()
         && !heldIn.getText().isEmpty()) {
            
           
            
         inputs = new ArrayList<>();
            
         inputs.add(depts.getValue());
         inputs.add(semesters.getValue());
         inputs.add(year.getText());
         inputs.add(session.getText());
         inputs.add(heldIn.getText());
         inputs.add(chairman.getText());
         inputs.add(controller.getText());
         inputs.add(member1.getText());
         inputs.add(member2.getText());
         inputs.add(member3.getText());
         inputs.add(member4.getText());
            
         System.err.println(depts.getValue());

         return true;
         }*/
        return true;

    }

}

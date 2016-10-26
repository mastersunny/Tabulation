/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml.test;

import com.opencsv.CSVWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javafx.scene.control.Alert;

/**
 *
 * @author sunny
 */
public class CSVService {

    List<Student> studentList;
    List<Course> courseList;

    public CSVService(List<Student> studentList, List<Course> courseList) {

        this.studentList = studentList;
        this.courseList = courseList;
    }

    void generateCSV() {

        try {

            CSVWriter writer = new CSVWriter(new FileWriter("table.csv"), ',');

            List<String[]> list = new ArrayList<>();
            String courses[] = new String[courseList.size() + 8];
            int i = 2;
            courses[0] = "Reg No";
            courses[1] = "Name";
            for (Course course : courseList) {

                courses[i++] = "(" + course.getCourseCode() + "_" + course.getSemester() + "_" + course.getCredit() + ")";

            }
            courses[i++] = "Total Credit";
            courses[i++] = "Total GPA";
            courses[i++] = "Letter Grade";
            courses[i++] = "Cumulative Credit";
            courses[i++] = "Cumulative GPA";
            courses[i++] = "Cumulative Grade";
            list.add(courses);

            for (Student student : studentList) {

                i = 0;
                String students[] = new String[courseList.size()+8];
                students[i++] = student.getRegNo();
                students[i++] = student.getName();

                double totalCredit = 0;
                double totalGpa = 0;

                Map<String, CourseReg> regesteredCourse = student.getRegesteredCourse();

                for (Course course : courseList) {

                    if (regesteredCourse.containsKey(course.getCourseCode())) {

                        CourseReg courseReg = regesteredCourse.get(course.getCourseCode());
                        students[i++] = String.format("%.02f", courseReg.getGpa())+"  "+ courseReg.getLetterGrade();

                    } else {
                        
                        students[i++] = " ";
                        
                    }
                }
                if (student.getTotalCredit() != 0) {

                    totalCredit = student.getTotalCredit();
                    totalGpa = student.getTotalGpa();
                    student.setLetterGrade(CgpaCalculator.getLetterGrade(totalGpa / totalCredit));
                    student.setGpa(totalGpa / totalCredit);

                    students[i++] = String.format("%.02f", totalCredit);
                    students[i++] = String.format("%.02f", (totalGpa / totalCredit));
                    students[i++] = CgpaCalculator.getLetterGrade(totalGpa / totalCredit);

                } else {

                    students[i++] = "0";
                    students[i++] = "0.00";
                    students[i++] = "F";

                }
                if (student.getCumulativeCredit() != 0) {

                    students[i++] = String.format("%.02f", student.getCumulativeCredit());
                    students[i++] = String.format("%.02f", student.getCumulativeGpa());
                    students[i++] = student.getCumulativeLetterGrade();

                } else {

                    students[i++] = "0";
                    students[i++] = "0.00";
                    students[i++] = "F";

                }
                list.add(students);

            }
            writer.writeAll(list);

            writer.close();

        } catch (IOException e) {
            AlertMessage.showAlertMessage(Alert.AlertType.ERROR, "Could not create CSV file");
        }

    }

}

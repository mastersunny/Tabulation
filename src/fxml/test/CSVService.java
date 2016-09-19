/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml.test;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sunny
 */
public class CSVService {

    List<Student> studentList;
    List<Course> courseList;

    public CSVService(List<Student> studentList, List<Course> courseList, List<String> inputs) {

        this.studentList = studentList;
        this.courseList = courseList;
    }

}

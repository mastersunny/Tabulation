/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml.test;

/**
 *
 * @author sunny
 */
public class CgpaCalculator {

    public static String getLetterGrade(double grade) {

        String s = null;

        if (grade >= 3.76 && grade <= 4.00) {
            s = "A+";
        } else if (grade >= 3.51 && grade <= 3.75) {
            s = "A-";
        } else if (grade >= 3.25 && grade <= 3.49) {
            s = "B+";
        } else if (grade >= 3.00 && grade <= 3.24) {
            s = "B";
        } else if (grade >= 2.75 && grade <= 2.99) {
            s = "B-";
        } else if (grade >= 2.50 && grade <= 2.74) {
            s = "C+";
        } else if (grade >= 2.25 && grade <= 2.49) {
            s = "C";
        } else if (grade >= 2.00 && grade <= 2.24) {
            s = "C-";
        } else {
            s = "F";
        }
        return s;

    }

}

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
public class CourseReg {

    private String courseCode;
    private double credit;
    private double gpa;
    private String letterGrade;

    public String getLetterGrade() {
        return letterGrade;
    }

    public void setLetterGrade(String letterGrade) {
        this.letterGrade = letterGrade;
    }

    public CourseReg() {

    }

    public CourseReg(String courseCode, double credit, double gpa, String letterGrade) {
        this.courseCode = courseCode;
        this.credit = credit;
        this.gpa = gpa;
        this.letterGrade = letterGrade;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    public double getGpa() {
        return gpa;
    }

    public void setGpa(double gpa) {
        this.gpa = gpa;
    }

    @Override
    public String toString() {
        return "CourseReg{" + "courseCode=" + courseCode + ", credit=" + credit + ", gpa=" + gpa + ", letterGrade=" + letterGrade + '}';
    }

}

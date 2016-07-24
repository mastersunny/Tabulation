package fxml.test;

import fxml.test.CourseReg;
import java.util.ArrayList;
import java.util.List;

public class Student implements Comparable<Student>{

    private String name;
    private String regNo;
    private List<CourseReg> regesteredCourse = new ArrayList<>();
    private double totalCredit;
    private double totalGpa;
    private String letterGrade;

    public double getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(double totalCredit) {
        this.totalCredit = totalCredit;
    }

    public double getTotalGpa() {
        return totalGpa;
    }

    public void setTotalGpa(double totalGpa) {
        this.totalGpa = totalGpa;
    }

    public String getLetterGrade() {
        return letterGrade;
    }

    public void setLetterGrade(String letterGrade) {
        this.letterGrade = letterGrade;
    }

    public Student(String regNo) {
        this.regNo = regNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }

    public List<CourseReg> getRegesteredCourse() {
        return regesteredCourse;
    }

    public void setRegesteredCourse(List<CourseReg> regesteredCourse) {
        this.regesteredCourse = regesteredCourse;
    }
    
    @Override
    public int compareTo(Student student) {
     
         return this.regNo.compareTo(student.getRegNo());
         
    }

    @Override
    public String toString() {
        return "Student{" + "name=" + name + ", regNo=" + regNo + ", regesteredCourse=" + regesteredCourse + '}';
    }

}

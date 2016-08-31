package fxml.test;

import java.util.HashMap;
import java.util.Map;

public class Student implements Comparable<Student> {

    private String name;
    private String regNo;
    private Map<String, CourseReg> regesteredCourse = new HashMap<>();

    //curerent semester
    private double totalCredit;
    private double Gpa;
    private double totalGpa;
    private String LetterGrade="F";
    //end current semester
    //cumulative
    private double cumulativeCredit;
    private double cumulativeGpa;
    private String cumulativeLetterGrade="F";
    //end cumulative

    public double getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(double totalCredit) {
        this.totalCredit = totalCredit;
    }

    public double getGpa() {
        return Gpa;
    }

    public void setGpa(double Gpa) {
        this.Gpa = Gpa;
    }

    public double getTotalGpa() {
        return totalGpa;
    }

    public void setTotalGpa(double totalGpa) {
        this.totalGpa = totalGpa;
    }

    public String getLetterGrade() {
        return LetterGrade;
    }

    public void setLetterGrade(String LetterGrade) {
        this.LetterGrade = LetterGrade;
    }

    public double getCumulativeCredit() {
        return cumulativeCredit;
    }

    public void setCumulativeCredit(double cumulativeCredit) {
        this.cumulativeCredit = cumulativeCredit;
    }

    public double getCumulativeGpa() {
        return cumulativeGpa;
    }

    public void setCumulativeGpa(double cumulativeGpa) {
        this.cumulativeGpa = cumulativeGpa;
    }

    public String getCumulativeLetterGrade() {
        return cumulativeLetterGrade;
    }

    public void setCumulativeLetterGrade(String cumulativeLetterGrade) {
        this.cumulativeLetterGrade = cumulativeLetterGrade;
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

    public Map<String, CourseReg> getRegesteredCourse() {
        return regesteredCourse;
    }

    @Override
    public int compareTo(Student student) {

        return this.regNo.compareTo(student.getRegNo());

    }

    @Override
    public String toString() {
        return "Student{" + "name=" + name + ", regNo=" + regNo + ", regesteredCourse=" + regesteredCourse + ", totalCredit=" + totalCredit + ", Gpa=" + Gpa + ", totalGpa=" + totalGpa + ", LetterGrade=" + LetterGrade + ", cumulativeCredit=" + cumulativeCredit + ", cumulativeGpa=" + cumulativeGpa + ", cumulativeLetterGrade=" + cumulativeLetterGrade + '}';
    }

}

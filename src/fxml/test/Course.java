package fxml.test;

import com.opencsv.bean.CsvBind;

public class Course implements Comparable<Course> {

    @CsvBind
    private String courseCode;
    @CsvBind
    private String semester;
    @CsvBind
    private double credit;

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getSemester() {
        return semester;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public double getCredit() {
        return credit;
    }

    public void setCredit(double credit) {
        this.credit = credit;
    }

    @Override
    public String toString() {
        return "Course{" + "courseCode=" + courseCode + ", semester=" + semester + ", credit=" + credit + '}';
    }

    @Override
    public int compareTo(Course course) {
     
         return this.courseCode.compareTo(course.getCourseCode());
         
    }

}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxml.test;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Font;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;

/**
 *
 * @author sunny
 */
public class PDFService {

    Font font4 = new Font(FontFamily.TIMES_ROMAN, 4);
    Font font8 = new Font(Font.FontFamily.TIMES_ROMAN, 8);
    Font font9 = new Font(Font.FontFamily.TIMES_ROMAN, 9);
    Font font10 = new Font(Font.FontFamily.TIMES_ROMAN, 10.5f);
    Font font7 = new Font(Font.FontFamily.TIMES_ROMAN, 7);

    List<Student> studentList;
    List<Object> list;
    List<String> inputs;

    public PDFService(List<Student> studentList, List<Course> courseList, List<String> inputs) {

        list = new ArrayList<>();
        this.studentList = studentList;
        this.list.addAll(courseList);
        this.inputs = inputs;
    }

    public void generatePdf() {
        //   Document document = new Document(PageSize.A4.rotate());
        Document document = new Document(new Rectangle(1008, 612));

        try {
            PdfWriter.getInstance(document,
                    new FileOutputStream("table.pdf"));

            document.setMargins(90, 80, 35, 40);
            document.open();

            if (!list.isEmpty()) {

                list.add("Total Credit");
                list.add("Total GPA");
                list.add("Letter Grade");
                list.add("Cumulative");
                list.add("Remarks");
                list.add("GC");

                if (inputs.get(1).contains("8th SEMESTER")) {

                    list.add("PC. No");
                    list.add("OC. No");
                    list.add("D/AF. No");
                    list.add("Others");
                }

                int totalCoureseSize = list.size();
                int totalCourseLoop = totalCoureseSize / 12;
                int courseLoopVariable = 0;

                if (totalCoureseSize % 12 > 0) {
                    totalCourseLoop += 1;
                }

                int totalStudentSize = studentList.size();
                int totalStudentLoop = (totalStudentSize / 15);
                int studentLoopVariable = 0;

                if (totalStudentSize % 15 > 0) {
                    totalStudentLoop += 1;
                }

                if (totalStudentLoop > 0) {

                    for (studentLoopVariable = 0; studentLoopVariable < totalStudentLoop; studentLoopVariable++) {

                        //start print the courses when it is multiple of 12
                        if (totalCourseLoop > 0) {

                            for (courseLoopVariable = 0; courseLoopVariable < totalCourseLoop; courseLoopVariable++) {

                                int courseStart = courseLoopVariable * 12;
                                int studentStart = studentLoopVariable * 15;
                                
                                //start document header
                                document.add(createDocumentHeader());
                                //end document header

                                //start table header
                                PdfPTable table = createTableHeader(courseStart);
                                //end table header

                                //start table body
                                table = createTableBody(studentStart, courseStart,table);
                                //end table body

                                //adding table and footer
                                table.setSpacingAfter(27);
                                document.add(table);
                                document.add(createFooter1());
                                document.add(createFooter2());
                                //end adding table and footer
                                //go to new page..
                                document.newPage();
                            }
                        }
                    }
                }
            }
            
            document.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            AlertMessage.showAlertMessage(Alert.AlertType.ERROR, "Error creating Pdf document.Please try again");
            e.printStackTrace();
        }
    }

    private PdfPTable createDocumentHeader() throws IOException, BadElementException {

        //start creating header for the document......
        PdfPTable headerTable = new PdfPTable(3);
        headerTable.setHorizontalAlignment(Element.ALIGN_LEFT);
        try {
            headerTable.setTotalWidth(new float[]{57.5f, 531.5f, 183f});
            headerTable.setLockedWidth(true);

        } catch (DocumentException ex) {
            Logger.getLogger(PDFService.class.getName()).log(Level.SEVERE, null, ex);
        }

        Image image = Image.getInstance(getClass().getClassLoader().getResource("img/sust.jpg"));
        PdfPCell imageCell = new PdfPCell(image, true);
        imageCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        imageCell.setBorder(Rectangle.NO_BORDER);
        headerTable.addCell(imageCell);

        //start info table.....
        PdfPTable infoTable = new PdfPTable(1);
        infoTable.getDefaultCell().setBorder(Rectangle.NO_BORDER);

        String universityText = "SHAHJALAL UNIVERSITY OF SCIENCE & TECHNOLOGY SYLHET, BANGLADESH";
        String tabulationText = "TABULATION SHEET";
        String deptText = inputs.get(0).trim();

        String s1 = inputs.get(1).trim();
        String s2 = inputs.get(2).trim();
        String semesterText = ("B.Sc (Engg.) " + s1 + " EXAMINATION " + s2);

        String session = inputs.get(3).trim();
        String date = inputs.get(4).trim();

        String sessionDateText = ("SESSION:" + session + " EXAMINATION HELD IN: " + date);

        infoTable.addCell(getCellForHeaderString(universityText, 0, false, 0, Element.ALIGN_CENTER, font10, true));
        infoTable.addCell(getCellForHeaderString(tabulationText, 0, false, 0, Element.ALIGN_CENTER, font10, false));
        infoTable.addCell(getCellForHeaderString(deptText, 0, false, 0, Element.ALIGN_CENTER, font10, false));
        infoTable.addCell(getCellForHeaderString(semesterText, 0, false, 0, Element.ALIGN_CENTER, font10, false));
        infoTable.addCell(getCellForHeaderString(sessionDateText, 0, false, 0, Element.ALIGN_CENTER, font10, false));
        //end info table.....

        PdfPCell infoCell = new PdfPCell(infoTable);
        infoCell.setBorder(Rectangle.NO_BORDER);
        headerTable.addCell(infoCell);

        PdfPCell resultPublishDateCell = new PdfPCell(new Paragraph("Result Published On............................", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
        resultPublishDateCell.setBorder(Rectangle.NO_BORDER);
        resultPublishDateCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        resultPublishDateCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        headerTable.addCell(resultPublishDateCell);
        headerTable.setSpacingAfter(18);
        // System.err.println("completed header table");
        return headerTable;
        //end creating header for the document......
    }

    public PdfPTable createFooter1() {

        //String[] names = new String[]{"Md. Eamin Rahman", "Md. Mujibur Rahman", "Md Masum", "Md. Saiful Islam", "Husne Ara Chowdhury", "Sabir Ismail"};
        PdfPTable table = new PdfPTable(5);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);

        try {
            table.setTotalWidth(new float[]{161f, 161f, 133f, 167f, 161f});
            table.setLockedWidth(true);

        } catch (DocumentException ex) {
            Logger.getLogger(PDFService.class.getName()).log(Level.SEVERE, null, ex);
        }
        //table.setWidthPercentage(100);
        PdfPCell chairmanSIgnature = new PdfPCell(new Paragraph("Signature of the Chairman:", font9));
        chairmanSIgnature.setBorder(Rectangle.NO_BORDER);
        chairmanSIgnature.setPaddingLeft(0f);
        chairmanSIgnature.setPaddingTop(5);
        table.addCell(chairmanSIgnature);

        PdfPCell underLine = new PdfPCell(new Paragraph("_______________________"));
        underLine.setBorder(Rectangle.NO_BORDER);
        table.addCell(underLine);

        PdfPCell blankColumn = new PdfPCell(new Paragraph(" "));
        blankColumn.setBorder(Rectangle.NO_BORDER);
        table.addCell(blankColumn);

        Paragraph p = new Paragraph("Signature of The Controller of Examinations:", font9);
        p.setLeading(0, 1.3f);
        PdfPCell controllerSignature = new PdfPCell();
        controllerSignature.addElement(p);
        controllerSignature.setBorder(Rectangle.NO_BORDER);
        table.addCell(controllerSignature);
        table.addCell(underLine);

        PdfPCell cell1 = new PdfPCell(new Paragraph(inputs.get(5).trim(), font9));
        cell1.setPaddingTop(0f);
        cell1.setBorder(Rectangle.NO_BORDER);

        PdfPCell cell2 = new PdfPCell(new Paragraph(inputs.get(6).trim(), font9));
        cell2.setPaddingTop(0f);
        cell2.setBorder(Rectangle.NO_BORDER);

        PdfPCell nameColumn = new PdfPCell(new Paragraph("Name :", font9));
        nameColumn.setBorder(Rectangle.NO_BORDER);
        nameColumn.setPaddingLeft(0f);
        nameColumn.setPaddingTop(0f);

        PdfPCell nameColumn2 = new PdfPCell(new Paragraph("Name :", font9));
        nameColumn2.setBorder(Rectangle.NO_BORDER);
        nameColumn2.setPaddingTop(0f);

        table.addCell(nameColumn);
        table.addCell(cell1);
        table.addCell(blankColumn);
        table.addCell(nameColumn2);
        table.addCell(cell2);

        table.setSpacingAfter(24);
        return table;
    }

    public PdfPTable createFooter2() {

        PdfPTable table = new PdfPTable(8);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);

        try {
            table.setTotalWidth(new float[]{192f, 144f, 5f, 144f, 5f, 144f, 5f, 144f});
            table.setLockedWidth(true);

        } catch (DocumentException ex) {
            Logger.getLogger(PDFService.class.getName()).log(Level.SEVERE, null, ex);
        }

        PdfPCell underLine = new PdfPCell(new Paragraph("_____________________"));
        //underLine.setPaddingLeft(0f);
        underLine.setBorder(Rectangle.NO_BORDER);

        PdfPCell blankColumn = new PdfPCell(new Paragraph(" "));
        blankColumn.setBorder(Rectangle.NO_BORDER);

        PdfPCell blankColumn2 = new PdfPCell(new Paragraph(" "));
        blankColumn2.setBorder(Rectangle.BOTTOM);

        PdfPCell nameColumn = new PdfPCell(new Paragraph("Name :", font9));
        nameColumn.setBorder(Rectangle.NO_BORDER);
        nameColumn.setPaddingLeft(0f);

        PdfPCell cell3 = new PdfPCell(new Paragraph(inputs.get(7).trim(), font9));
        cell3.setPaddingRight(2);
        cell3.setBorder(Rectangle.TOP);

        PdfPCell cell4 = new PdfPCell(new Paragraph(inputs.get(8).trim(), font9));
        cell4.setBorder(Rectangle.TOP);

        PdfPCell cell5 = new PdfPCell(new Paragraph(inputs.get(9).trim(), font9));
        cell5.setBorder(Rectangle.TOP);

        PdfPCell cell6 = new PdfPCell(new Paragraph(inputs.get(10).trim(), font9));
        cell6.setBorder(Rectangle.TOP);

        PdfPCell memberSIgnature = new PdfPCell(new Paragraph("Signature of the Members:", font9));
        memberSIgnature.setPaddingLeft(0f);
        memberSIgnature.setBorder(Rectangle.NO_BORDER);

        table.addCell(memberSIgnature);
        table.addCell(blankColumn);
        table.addCell(blankColumn);
        table.addCell(blankColumn);
        table.addCell(blankColumn);
        table.addCell(blankColumn);
        table.addCell(blankColumn);
        table.addCell(blankColumn);

        table.addCell(nameColumn);
        table.addCell(cell3);
        table.addCell(blankColumn);
        table.addCell(cell4);
        table.addCell(blankColumn);
        table.addCell(cell5);
        table.addCell(blankColumn);
        table.addCell(cell6);

        table.addCell(blankColumn);
        table.addCell(blankColumn);
        table.addCell(blankColumn);
        table.addCell(blankColumn);
        table.addCell(blankColumn);
        table.addCell(blankColumn);
        table.addCell(blankColumn);
        table.addCell(blankColumn);

        PdfPCell tabulatorSIgnature = new PdfPCell(new Paragraph("Signature of the Tabulators:", font9));
        tabulatorSIgnature.setPaddingLeft(0f);
        tabulatorSIgnature.setBorder(Rectangle.NO_BORDER);

        table.addCell(tabulatorSIgnature);
        table.addCell(blankColumn2);
        table.addCell(blankColumn);
        table.addCell(blankColumn2);
        table.addCell(blankColumn);
        table.addCell(blankColumn2);
        table.addCell(blankColumn);
        table.addCell(blankColumn2);

        return table;

    }
    
    public PdfPCell getCellForString(String args, int colSpan, boolean border, int vertical, int horizontal, Font font, boolean wrap) {

        PdfPCell cell = new PdfPCell(new Paragraph(args, font));
        if (colSpan != 0) {
            cell.setColspan(colSpan);
        }
        cell.setVerticalAlignment(vertical);
        cell.setHorizontalAlignment(horizontal);
        if (!border) {
            cell.setBorder(Rectangle.NO_BORDER);
        }
        if (wrap) {
            cell.setNoWrap(true);
        }
        return cell;
    }

    public PdfPCell getCellForHeaderString(String args, int colSpan, boolean flag, int vertical, int horizontal, Font font, boolean wrap) {

        PdfPCell cell = new PdfPCell(new Paragraph(args, font));
        if (colSpan != 0) {
            cell.setColspan(colSpan);
        }
        cell.setVerticalAlignment(vertical);
        cell.setHorizontalAlignment(horizontal);
        cell.setPaddingTop(1f);
        if (!flag) {
            cell.setBorder(Rectangle.NO_BORDER);
        }

        if (wrap) {
            cell.setNoWrap(true);
        }
        return cell;

    }

    private PdfPCell getNameCell() {

        PdfPTable nameTable = new PdfPTable(2);

        PdfPCell nameCell = new PdfPCell(new Paragraph("Name", font9));
        nameCell.setBorder(Rectangle.NO_BORDER);
        nameCell.setVerticalAlignment(Element.ALIGN_MIDDLE);

        PdfPTable semesterInfo = new PdfPTable(1);
        semesterInfo.addCell(nameCellHelper("Semester="));
        semesterInfo.addCell(nameCellHelper("Course No="));
        semesterInfo.addCell(nameCellHelper("Credit="));

        PdfPCell semesterCell = new PdfPCell(semesterInfo);
        semesterCell.setBorder(Rectangle.NO_BORDER);

        nameTable.addCell(nameCell);
        nameTable.addCell(semesterCell);

        PdfPCell cell = new PdfPCell(nameTable);

        return cell;
    }

    private PdfPCell nameCellHelper(String args) {

        PdfPCell cell = new PdfPCell(new Paragraph(args, font9));
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPaddingBottom(2.5f);
        return cell;

    }

    public PdfPTable createCourseInfo(Course course) {

        PdfPTable courseInfo = new PdfPTable(1);
        courseInfo.setWidthPercentage(100);

        courseInfo.addCell(getCellForString(course.getSemester(), Element.ALIGN_MIDDLE, false, 0, Element.ALIGN_CENTER, font7, false));

        PdfPCell cell2 = getCellForString(course.getCourseCode(), Element.ALIGN_MIDDLE, false, 0, Element.ALIGN_CENTER, font9, true);
        cell2.setPaddingBottom(2.5f);
        cell2.setPaddingTop(3.4f);
        courseInfo.addCell(cell2);
        courseInfo.addCell(getCellForString(String.format("%.02f", course.getCredit()), 0, false, Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, font9, false));

        return courseInfo;

    }

    public PdfPTable createTableHeader(int start) {

        PdfPTable table = null;

        try {

            int colCount = 1;
            List<Float> columnsList = new ArrayList<>();

            columnsList.add(57.5f);
            columnsList.add(159.4f);

            for (int i = start; i < list.size(); i++) {

                if (list.get(i) instanceof Course) {

                    columnsList.add(44f);

                } else if (list.get(i).equals("Total Credit")) {

                    columnsList.add(30f);

                } else if (list.get(i).equals("Total GPA")) {

                    columnsList.add(30f);

                } else if (list.get(i).equals("Letter Grade")) {

                    columnsList.add(30f);

                } else if (list.get(i).equals("Cumulative")) {

                    columnsList.add(88f);

                } else {

                    columnsList.add(66f);

                }

                if (colCount == 12) {
                    break;
                }
                colCount++;

            }

            float[] columns = new float[columnsList.size()];

            for (int i = 0; i < columnsList.size(); i++) {
                columns[i] = columnsList.get(i);
            }

            table = new PdfPTable(columns.length);

            System.err.println("table size :" + table.getNumberOfColumns());

            table.setHorizontalAlignment(Element.ALIGN_LEFT);
            table.setTotalWidth(columns);
            table.setLockedWidth(true);

        } catch (DocumentException ex) {
            Logger.getLogger(PDFService.class.getName()).log(Level.SEVERE, null, ex);
        }

        PdfPCell regCell = getCellForString("Reg No.", 0, true, Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, font9, false);
        regCell.setPaddingTop(0f);
        table.addCell(regCell);

        PdfPCell nameCell = getNameCell();
        nameCell.setPaddingBottom(2f);
        table.addCell(nameCell);

        int colCount = 1;

        for (int i = start; i < list.size(); i++) {

            if (list.get(i) instanceof Course) {

                Course course = (Course) list.get(i);

                PdfPCell cell3 = new PdfPCell(createCourseInfo(course));
                cell3.setPaddingTop(1f);
                cell3.setPaddingBottom(2f);
                //cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
                table.addCell(cell3);

            } else if (list.get(i).equals("Total Credit") || list.get(i).equals("Total GPA") || list.get(i).equals("Letter Grade")) {

                String str = (String) list.get(i);
                String s[] = str.split(" ");

                PdfPTable totalCredit = new PdfPTable(1);
                totalCredit.setSpacingBefore(12.5f);
                totalCredit.setWidthPercentage(100);
                totalCredit.getDefaultCell().setBorder(Rectangle.NO_BORDER);

                PdfPCell cell1 = getCellForString(s[0], 0, false, 0, Element.ALIGN_CENTER, new Font(Font.FontFamily.TIMES_ROMAN, 10f), false);
                PdfPCell cell2 = getCellForString(s[1], 0, false, 0, Element.ALIGN_CENTER, new Font(Font.FontFamily.TIMES_ROMAN, 10f), false);

                totalCredit.addCell(cell1);
                totalCredit.addCell(cell2);

                PdfPCell grade = new PdfPCell(totalCredit);
                table.addCell(grade);

            } else if (list.get(i).equals("Cumulative")) {

                PdfPTable cumulative = new PdfPTable(1);
                // cumulative.setPaddingTop(count);
                cumulative.addCell(getCellForString("Cumulative", 0, false, Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, font10, true));

                PdfPTable creditGpaGrade = new PdfPTable(3);

                creditGpaGrade.setTotalWidth(88f);
                creditGpaGrade.setLockedWidth(true);
                creditGpaGrade.addCell(getCellForString("Credit", 0, false, Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, font10, true));
                creditGpaGrade.addCell(getCellForString("GPA", 0, false, Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, font10, true));
                creditGpaGrade.addCell(getCellForString("Grade", 0, false, Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, font10, true));

                PdfPTable p = new PdfPTable(1);
                PdfPCell cell1 = new PdfPCell(cumulative);
                cell1.setPaddingBottom(3f);
                cell1.setBorder(Rectangle.NO_BORDER);
                PdfPCell cell2 = new PdfPCell(creditGpaGrade);
                cell2.setBorder(Rectangle.NO_BORDER);
                p.addCell(cell1);
                p.addCell(cell2);
                table.addCell(p);

            } else {

                table.addCell(getCellForString((String) list.get(i), 0, true, Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, font9, true));

            }

            if (colCount == 12) {
                break;
            }
            colCount++;
        }

        return table;

    }

    private PdfPTable createTableBody(int studentStart, int start, PdfPTable table) {

        int studentCount = 1;

        for (int j = studentStart; j < studentList.size(); j++) {

            Student student = (Student) studentList.get(j);

            PdfPCell regCell = getCellForString(student.getRegNo(), 0, true, Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, font9, false);
            regCell.setPaddingTop(1f);
            regCell.setPaddingBottom(4f);

            PdfPCell nameCell = getCellForString(student.getName(), 0, true, Element.ALIGN_MIDDLE, 0, font9, false);
            nameCell.setPaddingTop(1f);
            nameCell.setPaddingBottom(4f);
            nameCell.setPaddingLeft(5f);

            table.addCell(regCell);
            table.addCell(nameCell);

            int colCount = 1;

            //Getting student regestered courses for current semester.
            Map<String, CourseReg> regesteredCourse = student.getRegesteredCourse();

            for (int k = start; k < list.size(); k++) {

                //checking if its a instance of Course then we will print the Course details
                if (list.get(k) instanceof Course) {

                    Course course = (Course) list.get(k);

                    if (regesteredCourse.containsKey(course.getCourseCode())) {

                        CourseReg courseReg = regesteredCourse.get(course.getCourseCode());

                        PdfPTable table1 = new PdfPTable(2);
                        table1.setTotalWidth(44f);
                        table1.setLockedWidth(true);

                        PdfPCell cell1 = getCellForString(String.format("%.02f", courseReg.getGpa()), 0, false, Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, font9, false);
                        cell1.setPaddingTop(1f);
                        cell1.setPaddingBottom(4f);

                        PdfPCell cell2 = getCellForString(courseReg.getLetterGrade(), 0, false, Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, font9, false);
                        cell2.setPaddingTop(1f);
                        cell2.setPaddingBottom(4f);

                        table1.addCell(cell1);
                        table1.addCell(cell2);

                        PdfPCell cell3 = new PdfPCell(table1);
                        cell3.setPaddingTop(0f);
                        cell3.setPaddingBottom(0f);
                        table.addCell(cell3);

                    } else {
                        table.addCell(new PdfPCell());
                    }
                } //End checking if its a instance of Course then we will print the Course details
                else if (list.get(k).equals("Total Credit")) {

                    PdfPCell creditCell = getCellForString(String.valueOf(student.getTotalCredit()), 0, true, Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, font9, false);
                    creditCell.setPaddingTop(1f);
                    creditCell.setPaddingBottom(4f);
                    table.addCell(creditCell);

                } else if (list.get(k).equals("Total GPA")) {

                    PdfPCell gpaCell = getCellForString(String.format("%.02f", student.getGpa()), 0, true, Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, font9, false);
                    gpaCell.setPaddingTop(1f);
                    gpaCell.setPaddingBottom(4f);
                    table.addCell(gpaCell);

                } else if (list.get(k).equals("Letter Grade")) {

                    PdfPCell gradeCell = getCellForString(student.getLetterGrade(), 0, true, Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, font9, false);
                    gradeCell.setPaddingTop(1f);
                    gradeCell.setPaddingBottom(4f);
                    table.addCell(gradeCell);

                } else if (list.get(k).equals("Cumulative")) {

                    PdfPTable table1 = new PdfPTable(3);
                    table1.setTotalWidth(88f);
                    table1.setLockedWidth(true);

                    PdfPCell cell1 = getCellForString(String.format("%.02f", student.getCumulativeCredit()), 0, true, Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, font9, false);
                    cell1.setPaddingTop(1f);
                    cell1.setPaddingBottom(4f);

                    PdfPCell cell2 = getCellForString(String.format("%.02f", student.getCumulativeGpa()), 0, true, Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, font9, false);
                    cell2.setPaddingTop(1f);
                    cell2.setPaddingBottom(4f);

                    PdfPCell cell3 = getCellForString(student.getCumulativeLetterGrade(), 0, true, Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, font9, false);
                    cell3.setPaddingTop(1f);
                    cell3.setPaddingBottom(4f);

                    table1.addCell(cell1);
                    table1.addCell(cell2);
                    table1.addCell(cell3);

                    PdfPCell cell4 = new PdfPCell(table1);
                    cell4.setPaddingTop(0f);
                    cell4.setPaddingBottom(0f);
                    table.addCell(cell4);

                } else {

                    table.addCell(new PdfPCell());

                }

                if (colCount == 12) {
                    break;
                }
                colCount++;

            }

            if (studentCount == 15) {
                break;
            }
            studentCount++;

        }

        return table;
    }

}

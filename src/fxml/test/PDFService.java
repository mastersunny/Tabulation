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
import com.itextpdf.tool.xml.html.pdfelement.HtmlCell;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

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
    List<Course> courseList;
    List<String> inputs;

    public PDFService(List<Student> studentList, List<Course> courseList, List<String> inputs) {

        this.studentList = studentList;
        this.courseList = courseList;
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

            if (!courseList.isEmpty()) {

                Collections.sort(courseList);

                int courseCount = 0;
                int totalCoureseSize = courseList.size();
                int totalCourseLoop = (totalCoureseSize / 12);
                int courseLoopVariable = 0;

                int studentCount = 0;
                int totalStudentSize = studentList.size();
                int totalStudentLoop = (totalStudentSize / 15);
                int studentLoopVariable = 0;

                if (totalStudentSize % 15 > 0) {
                    totalStudentLoop += 1;
                }

                if (totalStudentLoop > 0) {

                    for (studentLoopVariable = 0; studentLoopVariable < totalStudentLoop; studentLoopVariable++) {

                        if (totalCourseLoop > 0) {

                            for (courseLoopVariable = 0; courseLoopVariable < totalCourseLoop; courseLoopVariable++) {

                                //start document header
                                document.add(createDocumentHeader());
                                //end document header

                                //Start main table header
                                int courseStart = courseLoopVariable * 12;
                                PdfPTable table = createMainTableHeader(courseStart);
                                //end main table header

                                //start main table body    
                                int studentStart = studentLoopVariable * 15;
                                table = createMainTableBody(studentStart, courseStart, table);
                                //end main table body

                                //start document footer
                                table.setSpacingAfter(20);
                                document.add(table);
                                document.add(createFooter1());
                                document.add(createFooter2());
                                document.newPage();
                                //end document footer
                            }
                        }

                        if (totalCoureseSize % 12 > 0) {

                            boolean flag = false;
                            int tableSize = 0;

                            int courseStart = courseLoopVariable * 12;
                            int studentStart = studentLoopVariable * 15;
                            int courseMod = totalCoureseSize % 12;

                            if ((courseMod + 3 + 5) <= 12) {

                                document.add(createDocumentHeader());
                                PdfPTable table = createExtensiontable1(courseStart, studentStart, totalCoureseSize);
                                table.setSpacingAfter(20);
                                document.add(table);
                                document.add(createFooter1());
                                document.add(createFooter2());

                            } else {

                                createExtensiontable2(courseStart, studentStart, totalCoureseSize);

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

        table.setSpacingAfter(25);
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

    public PdfPTable createMainTableHeader(int courseStart) {

        PdfPTable table = new PdfPTable(14);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);

        try {
            table.setTotalWidth(new float[]{57.5f, 159.4f, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44});
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

        int courseCount = 1;

        for (int j = courseStart; j < courseList.size(); j++) {

            Course course = (Course) courseList.get(j);

            PdfPCell cell3 = new PdfPCell(createCourseInfo(course));
            cell3.setPaddingTop(1f);
            cell3.setPaddingBottom(2f);
            //cell3.setVerticalAlignment(Element.ALIGN_MIDDLE);
            table.addCell(cell3);

            if (courseCount == 12) {
                break;
            }
            courseCount++;
        }

        return table;

    }

    public PdfPTable createMainTableBody(int studentStart, int courseStart, PdfPTable table) throws DocumentException {

        int studentCount = 1;
        boolean flag = false;

        for (int j = studentStart; j < studentList.size(); j++) {

            Student student = (Student) studentList.get(j);

            //column 1
            table.addCell(getCellForString(student.getRegNo(), 0, true, 0, Element.ALIGN_CENTER, font9, false));

            //column 2
            PdfPCell name = new PdfPCell(new Paragraph(student.getName(), font9));
            table.addCell(name);

            int courseCount = 1;
            double totalCredit = 0;
            double totalGpa = 0;
            //rest of the columns
            for (int k = courseStart; k < courseList.size(); k++) {

                Course course = (Course) courseList.get(k);

                flag = false;
                for (CourseReg courseReg : student.getRegesteredCourse()) {

                    if (course.getCourseCode().equalsIgnoreCase(courseReg.getCourseCode())) {

                        //for showing each subject grade and GPA
                        PdfPTable gpaLetterGrade = new PdfPTable(2);

                        PdfPCell cell1 = new PdfPCell(new Paragraph(String.format("%.02f", courseReg.getGpa()), font9));
                        cell1.setBorder(Rectangle.NO_BORDER);
                        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                        PdfPCell cell2 = new PdfPCell(new Paragraph(courseReg.getLetterGrade(), font9));
                        cell2.setBorder(Rectangle.NO_BORDER);
                        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                        gpaLetterGrade.addCell(cell1);
                        gpaLetterGrade.addCell(cell2);

                        PdfPCell cell3 = new PdfPCell(gpaLetterGrade);
                        cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell3);
                        flag = true;
                        //totalCredit += courseReg.getCredit();
                        // totalGpa += (courseReg.getGpa() * courseReg.getCredit());

                        break;

                    }
                }

                if (!flag) {
                    PdfPCell cell3 = new PdfPCell(new Paragraph(" "));
                    table.addCell(cell3);
                }

                if (courseCount == 12) {
                    break;
                }
                courseCount++;
            }

            //student.setTotalCredit(totalCredit);
            //student.setTotalGpa(totalGpa);
            if (studentCount == 15) {
                break;
            }
            studentCount++;

        }

        return table;
    }

    public PdfPCell getCellForString(String args, int colSpan, boolean flag) {

        PdfPCell cell = new PdfPCell(new Paragraph(args, font9));
        if (colSpan != 0) {
            cell.setColspan(colSpan);
        }
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        if (!flag) {
            cell.setBorder(Rectangle.NO_BORDER);
        }

        return cell;

    }

    //this method will return cell where elements will be horizontally center aligned 
    public PdfPCell getCellForTable(PdfPTable table) {

        PdfPCell cell = new PdfPCell(table);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setBorder(Rectangle.NO_BORDER);

        return cell;

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

        PdfPCell cell2 = new PdfPCell(nameTable);

        return cell2;
    }

    private PdfPCell nameCellHelper(String args) {

        PdfPCell cell = new PdfPCell(new Paragraph(args, font9));

        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        cell.setBorder(Rectangle.NO_BORDER);

        return cell;

    }

    public PdfPTable createCourseInfo(Course course) {

        PdfPTable courseInfo = new PdfPTable(1);
        courseInfo.setWidthPercentage(100);

        courseInfo.addCell(getCellForString(course.getSemester(), 0, false, Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, font7, false));
        PdfPCell cell2 = getCellForString(course.getCourseCode(), 0, false, Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, font9, true);
        cell2.setPaddingTop(3f);
        courseInfo.addCell(cell2);
        courseInfo.addCell(getCellForString(String.valueOf(course.getCredit()), 0, false, Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, font9, false));

        return courseInfo;

    }

    private PdfPTable createExtensiontable1(int courseStart, int studentStart, int totalCoureseSize) {

        PdfPTable table = new PdfPTable(totalCoureseSize + 3 + 3 + 2);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);

        try {

            System.out.println("table size" + table.getNumberOfColumns());
            float[] columns = new float[table.getNumberOfColumns()];
            float[] mainColumns = new float[]{57.5f, 159.4f, 44, 44, 44, 44, 44, 44, 44, 44, 44};

            columns[0] = mainColumns[0];
            columns[1] = mainColumns[1];
            int i = 0;
            for (i = 2; i < totalCoureseSize + 2; i++) {
                columns[i] = mainColumns[i];
            }
            columns[i++] = 32;
            columns[i++] = 32;
            columns[i++] = 32;
            columns[i++] = 88;
            columns[i++] = 66;
            columns[i++] = 66;

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

        for (int j = courseStart; j < courseList.size(); j++) {

            Course course = (Course) courseList.get(j);

            PdfPCell courseCell = new PdfPCell(createCourseInfo(course));
            courseCell.setPaddingTop(1f);
            courseCell.setPaddingBottom(2f);
            table.addCell(courseCell);

        }

        table.addCell(getCellForString("Total Credit", 0, true, Element.ALIGN_BOTTOM, Element.ALIGN_CENTER, font10, false));
        table.addCell(getCellForString("Total GPA", 0, true, Element.ALIGN_BOTTOM, Element.ALIGN_CENTER, font10, false));
        table.addCell(getCellForString("Letter Grade", 0, true, Element.ALIGN_BOTTOM, Element.ALIGN_CENTER, font10, false));

        //start
        PdfPTable cumulative = new PdfPTable(1);
        cumulative.addCell(getCellForString("Cumulative", 0, false, Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, font10, true));

        PdfPTable creditGpaGrade = new PdfPTable(3);
        creditGpaGrade.addCell(getCellForString("Credit", 0, false, Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, font10, true));
        creditGpaGrade.addCell(getCellForString("GPA", 0, false, Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, font10, true));
        creditGpaGrade.addCell(getCellForString("Grade", 0, false, Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, font10, true));

        PdfPTable p = new PdfPTable(1);
        PdfPCell cell1 = new PdfPCell(cumulative);
        cell1.setBorder(Rectangle.NO_BORDER);
        PdfPCell cell2 = new PdfPCell(creditGpaGrade);
        cell2.setBorder(Rectangle.NO_BORDER);
        p.addCell(cell1);
        p.addCell(cell2);
        table.addCell(p);
        //end

        table.addCell(getCellForString("Remarks", 0, true, Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, font9, true));
        table.addCell(getCellForString("GC", 0, true, Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, font9, true));

        createExtensiontable1Body(studentStart, courseStart, table);
        return table;
    }

    private void createExtensiontable1Body(int studentStart, int courseStart, PdfPTable table) {

        int studentCount = 1;
        boolean flag = false;

        for (int j = studentStart; j < studentList.size(); j++) {

            Student student = (Student) studentList.get(j);

            PdfPCell regCell = getCellForString(student.getRegNo(), 0, true, Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, font9, false);
            regCell.setPaddingTop(-0.5f);

            PdfPCell nameCell = getCellForString(student.getName(), 0, true, Element.ALIGN_MIDDLE, 0, font9, false);
            nameCell.setPaddingLeft(5f);
            nameCell.setPaddingTop(-0.5f);

            table.addCell(regCell);
            table.addCell(nameCell);

            int courseCount = 1;
            double totalCredit = 0;
            double totalGpa = 0;

            for (int k = courseStart; k < courseList.size(); k++) {

                Course course = (Course) courseList.get(k);

                flag = false;
                for (CourseReg courseReg : student.getRegesteredCourse()) {

                    if (course.getCourseCode().equalsIgnoreCase(courseReg.getCourseCode())) {

                        //for showing each subject grade and GPA
                        PdfPTable gpaLetterGrade = new PdfPTable(2);

                        PdfPCell cell1 = new PdfPCell(new Paragraph(String.format("%.02f", courseReg.getGpa()), font9));
                        cell1.setBorder(Rectangle.NO_BORDER);
                        cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
                        PdfPCell cell2 = new PdfPCell(new Paragraph(courseReg.getLetterGrade(), font9));
                        cell2.setBorder(Rectangle.NO_BORDER);
                        cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
                        gpaLetterGrade.addCell(cell1);
                        gpaLetterGrade.addCell(cell2);

                        PdfPCell cell3 = new PdfPCell(gpaLetterGrade);
                        cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
                        table.addCell(cell3);
                        flag = true;
                        break;

                    }
                }

                if (!flag) {
                    PdfPCell cell3 = new PdfPCell(new Paragraph(" "));
                    table.addCell(cell3);
                }

            }

            PdfPCell creditCell = new PdfPCell(new Paragraph(String.format("%.02f", student.getTotalCredit()), font9));
            creditCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            creditCell.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell gpaCell = new PdfPCell(new Paragraph(String.format("%.02f", student.getTotalGpa()), font9));
            gpaCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            gpaCell.setHorizontalAlignment(Element.ALIGN_CENTER);

            PdfPCell gradeCell = new PdfPCell(new Paragraph(student.getLetterGrade(), font9));
            gradeCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            gradeCell.setHorizontalAlignment(Element.ALIGN_CENTER);

            table.addCell(creditCell);
            table.addCell(gpaCell);
            table.addCell(gradeCell);

            PdfPTable table1 = new PdfPTable(3);
            table1.setTotalWidth(88f);
            table1.setLockedWidth(true);

            PdfPCell cell1 = new PdfPCell();

            table1.addCell(cell1);
            table1.addCell(cell1);
            table1.addCell(cell1);

            PdfPCell cell2 = new PdfPCell(table1);
            cell2.setPaddingTop(0f);
            cell2.setPaddingBottom(0f);
            table.addCell(cell2);
            table.addCell(new Paragraph(""));
            table.addCell(new Paragraph(""));

            if (studentCount == 15) {
                break;
            }
            studentCount++;
        }
    }

    private void createExtensiontable2(int courseStart, int studentStart, int totalCoureseSize) {

        PdfPTable table = null;
        List<String> list = new ArrayList<>();
        list.add("Total Credit");
        list.add("Total GPA");
        list.add("Letter Grade");

        int colCount = 0;
        try {

            List<Float> columnsList = new ArrayList<>();

            int i = 0, j = 0;
            columnsList.add(57.5f);
            columnsList.add(159.4f);

            for (i = 2; i < (totalCoureseSize - courseStart) + 2; i++) {
        
                colCount++;
                columnsList.add(44f);
            }
            for (String s : list) {
               
                if(colCount==12)
                    break;
                columnsList.add(32f);
                colCount++;
            }

            float[] columns = new float[columnsList.size()];

            for (i = 0; i < columnsList.size(); i++) {
                columns[i] = columnsList.get(i);
            }

            table = new PdfPTable(columns.length);
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

        int j =0;
        for (j = courseStart; j < courseList.size(); j++) {

            Course course = (Course) courseList.get(j);

            PdfPCell courseCell = new PdfPCell(createCourseInfo(course));
            courseCell.setPaddingTop(1f);
            courseCell.setPaddingBottom(2f);
            table.addCell(courseCell);

        }

        int k =0;
        for (k = 0; k < 3; k++) {
            
            PdfPCell grade = getCellForString(list.get(k), 0, false, Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, font10, true);
            grade.setPaddingTop(1f);
            grade.setPaddingBottom(2f);
            table.addCell(grade);
            
            if((j+k)==colCount)
                break;

        }
        
        
        System.err.println(table.getNumberOfColumns());

        
    }

}

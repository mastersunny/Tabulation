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

            document.setMargins(90,80,35,40);
           
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

                            if (totalCoureseSize % 12 <= 9) {

                                flag = true;
                                tableSize = (totalCoureseSize % 12) + 3 + 2;

                            } else {

                                flag = false;
                                tableSize = (totalCoureseSize % 12) + 2;

                            }

                            //start document header
                            document.add(createDocumentHeader());
                            //end document header

                            //start extension table header
                            int courseStart = courseLoopVariable * 12;
                            PdfPTable table = createExtensionTableHeader(tableSize, courseStart, flag);
                            //end extension table header

                            //start extension table body
                            int studentStart = studentLoopVariable * 15;
                            table = createExtensionTableBody(studentStart, courseStart, table, flag);
                            //end extension table body

                            //start document footer
                            table.setSpacingAfter(20);
                            document.add(table);
                            document.add(createFooter1());
                            document.add(createFooter2());
                            document.newPage();
                            //end document footer

                            //start cumulative table
                            //start document header
                            document.add(createDocumentHeader());
                                //end document header

                            //start table body
                            PdfPTable cumulativeTable = createCumulativeTable(studentStart, flag);
                            cumulativeTable.setSpacingAfter(20);
                            document.add(cumulativeTable);
                                //end table body

                            //start document footer
                            document.add(createFooter1());
                            document.add(createFooter2());
                            document.newPage();
                                //end document footer

                            //end cumulative table
                        }

                    }
                }

                if (totalStudentSize % 15 > 0) {

                    if (totalCourseLoop > 0) {

                        for (courseLoopVariable = 0; courseLoopVariable < totalCourseLoop; courseLoopVariable++) {

                            //start document header
                            document.add(createDocumentHeader());
                            //end document header

                            //Start  main table header
                            int courseStart = courseLoopVariable * 12;
                            PdfPTable table = createMainTableHeader(courseStart);
                            //End  main table header

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

                        if (totalCoureseSize % 12 <= 9) {

                            flag = true;
                            tableSize = (totalCoureseSize % 12) + 3 + 2;

                        } else {

                            flag = false;
                            tableSize = (totalCoureseSize % 12) + 2;

                        }

                        //start document header
                        document.add(createDocumentHeader());
                        //end document header

                        //start extension table header
                        int courseStart = courseLoopVariable * 12;
                        PdfPTable table = createExtensionTableHeader(tableSize, courseStart, flag);
                        //end extension table header

                        //start extension table body
                        int studentStart = studentLoopVariable * 15;
                        table = createExtensionTableBody(studentStart, courseStart, table, flag);
                        //end extension table body

                        //start document footer
                        table.setSpacingAfter(20);
                        document.add(table);
                        document.add(createFooter1());
                        document.add(createFooter2());
                        document.newPage();
                        //end document footer

                        //start cumulative table
                        //start document header
                        document.add(createDocumentHeader());
                            //end document header

                        //start table body
                        PdfPTable cumulativeTable = createCumulativeTable(studentStart, flag);
                        cumulativeTable.setSpacingAfter(20);
                        document.add(cumulativeTable);
                            //end table body

                        //start document footer
                        document.add(createFooter1());
                        document.add(createFooter2());
                            //end document footer

                        //end cumulative table
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
            headerTable.setTotalWidth(new float[]{57.5f, 545, 176});
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

        infoTable.addCell(getCellForString(universityText, 0, false, 0, Element.ALIGN_CENTER, font10, true));
        infoTable.addCell(getCellForString(tabulationText, 0, false, 0, Element.ALIGN_CENTER, font10, false));
        infoTable.addCell(getCellForString(deptText, 0, false,0, Element.ALIGN_CENTER, font10, false));
        infoTable.addCell(getCellForString(semesterText, 0, false,0, Element.ALIGN_CENTER, font10, false));
        infoTable.addCell(getCellForString(sessionDateText, 0, false,0, Element.ALIGN_CENTER, font10, false));
        //end info table.....

        PdfPCell infoCell = new PdfPCell(infoTable);
        infoCell.setBorder(Rectangle.NO_BORDER);
        headerTable.addCell(infoCell);

        PdfPCell resultPublishDateCell = new PdfPCell(new Paragraph("Result Published On............................", new Font(Font.FontFamily.TIMES_ROMAN, 10, Font.BOLD)));
        resultPublishDateCell.setBorder(Rectangle.NO_BORDER);
        resultPublishDateCell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        resultPublishDateCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerTable.addCell(resultPublishDateCell);
        headerTable.setSpacingAfter(20); 

        // System.err.println("completed header table");
        return headerTable;

        //end creating header for the document......
    }

    public PdfPTable createCourseInfo(Course course) {

        PdfPTable courseInfo = new PdfPTable(1);
        courseInfo.setWidthPercentage(100);

        courseInfo.addCell(getCellForString(course.getSemester(), 0, false, Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, font7, false));
        courseInfo.addCell(getCellForString(course.getCourseCode(), 0, false, Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, font9, true));
        courseInfo.addCell(getCellForString(String.valueOf(course.getCredit()), 0, false, Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, font9, false));

        return courseInfo;

    }

    public PdfPTable createFooter1() {

        //String[] names = new String[]{"Md. Eamin Rahman", "Md. Mujibur Rahman", "Md Masum", "Md. Saiful Islam", "Husne Ara Chowdhury", "Sabir Ismail"};
        PdfPTable table = new PdfPTable(5);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        
        try {
            table.setTotalWidth(new float[]{161f, 161f, 133f,167f,161f});
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
        p.setLeading(0,1.3f);
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
    
    public PdfPTable createFooter2(){
        
        PdfPTable table = new PdfPTable(8);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);
        
        try {
            table.setTotalWidth(new float[]{192f, 144f,5f, 144f,5f,144f,5f,144f});
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
        regCell.setPaddingBottom(2.5f);
        table.addCell(regCell);
        PdfPCell nameCell = getNameCell();
        nameCell.setPaddingBottom(2.5f);
        table.addCell(nameCell);

        int courseCount = 1;

        for (int j = courseStart; j < courseList.size(); j++) {

            Course course = (Course) courseList.get(j);

            PdfPCell cell3 = new PdfPCell(createCourseInfo(course));         
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

    private PdfPTable createExtensionTableHeader(int tableSize, int courseStart, boolean flag) {

        PdfPTable table = new PdfPTable(tableSize);
        table.setHorizontalAlignment(Element.ALIGN_LEFT);

        try {
            
            float[] columns=new float[tableSize];
            float[] mainColumns = new float[]{57.5f, 159.4f, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44, 44};
            
            
            for(int i=0;i<tableSize;i++){
                columns[i] = mainColumns[i];
            }
            table.setTotalWidth(columns);
            table.setLockedWidth(true);

        } catch (DocumentException ex) {
            Logger.getLogger(PDFService.class.getName()).log(Level.SEVERE, null, ex);
        }

        PdfPCell regCell = getCellForString("Reg No.", 0, true, Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, font9, false);
        regCell.setPaddingBottom(2.5f);
        table.addCell(regCell);
        PdfPCell nameCell = getNameCell();
        nameCell.setPaddingBottom(2.5f);
        table.addCell(nameCell);

        for (int j = courseStart; j < courseList.size(); j++) {

            Course course = (Course) courseList.get(j);

            PdfPCell cell3 = new PdfPCell(createCourseInfo(course));
            table.addCell(cell3);

        }
        if (flag) {

            table.addCell(getCellForString("Total Credit", 0, true));
            table.addCell(getCellForString("Total GPA", 0, true));
            table.addCell(getCellForString("Letter Grade", 0, true));
        }

        return table;

    }

    private PdfPTable createExtensionTableBody(int studentStart, int courseStart, PdfPTable table, boolean flag1) {

        int studentCount = 1;
        boolean flag = false;

        for (int j = studentStart; j < studentList.size(); j++) {

            Student student = (Student) studentList.get(j);

            //column 1
            table.addCell(getCellForString(student.getRegNo(), 0, true,0, Element.ALIGN_CENTER, font9, false));

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
                        // totalCredit += courseReg.getCredit();
                        //totalGpa += (courseReg.getGpa() * courseReg.getCredit());
                        break;

                    }
                }

                if (!flag) {
                    PdfPCell cell3 = new PdfPCell(new Paragraph(" "));
                    table.addCell(cell3);
                }

            }

            //student.setTotalCredit(totalCredit);
            //student.setTotalGpa(totalGpa);
            if (flag1) {

                table.addCell(getCellForString(String.valueOf(student.getTotalCredit()), 0, true));
                table.addCell(getCellForString(String.valueOf(student.getTotalGpa()), 0, true));
                table.addCell(getCellForString(student.getLetterGrade(), 0, true));

            }

            if (studentCount == 15) {
                break;
            }
            studentCount++;

        }

        return table;
    }

    private PdfPTable createSemesterInfo() {

        PdfPTable semesterInfo = new PdfPTable(1);
        semesterInfo.setWidthPercentage(100);

        semesterInfo.addCell(getCellForString2("Semester=", 0, false));
        semesterInfo.addCell(getCellForString2("Course No=", 0, false));
        semesterInfo.addCell(getCellForString2("Credit=", 0, false));

        return semesterInfo;
    }

    private PdfPTable createCumulativeTable(int studentStart, boolean flag) {

        PdfPTable table = new PdfPTable(18);

        //column 1
        table.addCell(getCellForString("Reg No.", 2, true, Element.ALIGN_MIDDLE, Element.ALIGN_CENTER, font9, false));
        //column 2
        table.addCell(getNameCell());

        int spaceCounter = 5;
        if (!flag) {
            //if course size is >9 then this will print
            table.addCell(getCellForString("Total Credit", 0, true));
            table.addCell(getCellForString("Total GPA", 0, true));
            table.addCell(getCellForString("Letter Grade", 0, true));
            spaceCounter = 2;

        }

        //start cumulative cell
        PdfPTable cumulativeTable = new PdfPTable(1);

        PdfPTable creditGpaGrade = new PdfPTable(3);

        creditGpaGrade.addCell(getCellForString("Credit", 0, false));
        creditGpaGrade.addCell(getCellForString("GPA", 0, false));
        creditGpaGrade.addCell(getCellForString("Grade", 0, false));

        cumulativeTable.addCell(getCellForString("Cumulative", 0, false));
        cumulativeTable.addCell(getCellForTable(creditGpaGrade));

        PdfPCell cumulativeCell = new PdfPCell(cumulativeTable);
        cumulativeCell.setColspan(3);
        table.addCell(cumulativeCell);
        //end cumulative cell

        table.addCell(getCellForString("Remarks", 2, true));
        table.addCell(getCellForString("GC", 2, true));

        for (int i = 0; i < spaceCounter; i++) {
            table.addCell(getCellForString(" ", 0, false));
        }

        int studentCount = 1;
        for (int j = studentStart; j < studentList.size(); j++) {

            Student student = (Student) studentList.get(j);

            PdfPCell regNo = new PdfPCell(new Paragraph(student.getRegNo(), font9));
            regNo.setVerticalAlignment(Element.ALIGN_MIDDLE);
            regNo.setHorizontalAlignment(Element.ALIGN_CENTER);
            regNo.setPaddingBottom(4);
            regNo.setPaddingTop(3);
            regNo.setColspan(2);
            table.addCell(regNo);

            PdfPCell name = new PdfPCell(new Paragraph(student.getName(), font9));
            name.setVerticalAlignment(Element.ALIGN_MIDDLE);
            name.setColspan(4);
            table.addCell(name);

            if (!flag) {

                table.addCell(getCellForString(String.valueOf(student.getTotalCredit()), 0, true));
                table.addCell(getCellForString(String.valueOf(student.getTotalGpa()), 0, true));
                table.addCell(getCellForString(student.getLetterGrade(), 0, true));
            }

            table.addCell(getCellForString(String.valueOf(student.getTotalCredit()), 0, true));
            table.addCell(getCellForString(String.valueOf(student.getTotalGpa()), 0, true));
            table.addCell(getCellForString(student.getLetterGrade(), 0, true));

            table.addCell(getCellForString(" ", 2, true));
            table.addCell(getCellForString(" ", 2, true));

            for (int i = 0; i < spaceCounter; i++) {
                table.addCell(getCellForString(" ", 0, false));
            }

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

    //this method will return cell where elements will be horizontally right aligned 
    private PdfPCell getCellForString2(String args, int colSpan, boolean flag) {

        PdfPCell cell = new PdfPCell(new Paragraph(args, font9));
        if (colSpan != 0) {
            cell.setColspan(colSpan);
        }
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        if (!flag) {
            cell.setBorder(Rectangle.NO_BORDER);
        }

        return cell;

    }

    public PdfPCell getCellForString(String args, int colSpan, boolean flag, int vertical, int horizontal, Font font, boolean wrap) {

        PdfPCell cell = new PdfPCell(new Paragraph(args, font));
        if (colSpan != 0) {
            cell.setColspan(colSpan);
        }
        cell.setVerticalAlignment(vertical);
        cell.setHorizontalAlignment(horizontal);
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

        PdfPTable semesterTable = createSemesterInfo();

        PdfPCell semesterCell = new PdfPCell(semesterTable);
        semesterCell.setBorder(Rectangle.NO_BORDER);

        nameTable.addCell(nameCell);
        nameTable.addCell(semesterCell);

        PdfPCell cell2 = new PdfPCell(nameTable);

        return cell2;
    }

}

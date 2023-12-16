package com.nitap.attende;

import static org.apache.poi.ss.util.CellUtil.createCell;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nitap.attende.models.Attendance;
import com.nitap.attende.models.SectionInfo;
import com.nitap.attende.models.Teacher;
import com.opencsv.CSVWriter;


import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);


        Attendance attendance = new Attendance();
        attendance.sectionInfo = new SectionInfo();
        attendance.attendance = new ArrayList<>();

        attendance.courseCode = "CS251";
        attendance.dateInfo = "23-04-2023";
        attendance.sectionInfo.sectionId = "4212";
        attendance.sectionInfo.max = "75";
        attendance.sectionInfo.startRollno = "421201";
        attendance.sectionInfo.endRollno = "421275";
        attendance.sectionInfo.sectionName = "B";
        attendance.sectionInfo.classId = "BTECHCSE22";



        for (int i=0; i<75; i++) {
            if (i%2==0) {
                attendance.attendance.add("0");
            } else {
                attendance.attendance.add("1");
            }
        }














        XSSFWorkbook ourWorkBook = new XSSFWorkbook();
        Sheet sheet = ourWorkBook.createSheet("statSheet");
        Row row1 = sheet.createRow(0);
        Row row2 = sheet.createRow(1);

       /* row1.createCell(0).setCellValue("hdj");
        row1.createCell(1).setCellValue("bfkjsk");
        row2.createCell(0).setCellValue("jffs");
        row2.createCell(1).setCellValue("nfew");*/

        createCell(row1, 0, "Mike");
        createCell(row1, 1, "470");

        createCell(row2, 0, "Montessori");
        createCell(row2, 1, "460");


        File mydir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        //File myfile = new File(mydir.getAbsolutePath()+"test.xlsx");
        File myfile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "test.xlsx");
        try {
           // myfile.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
            //throw new RuntimeException(e);
        }
        try {
            FileOutputStream fileOut = new FileOutputStream(myfile);
            ourWorkBook.write(fileOut);
            fileOut.close();
            Toast.makeText(this, "written", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            //throw new RuntimeException(e);
        } catch (IOException e) {
            e.printStackTrace();
            //throw new RuntimeException(e);
        }

        //writeAttendance1(this,new Attendance());
        //writeAttendance(this,attendance);
        try {
            writeAttendance(this,attendance);
           // writeDataLineByLine(this,attendance);
        } catch (IOException e) {
            e.printStackTrace();
        }
       /*writeAttendance(this,attendance);
        writeAttendance(this,attendance);
        writeAttendance(this,attendance);
        writeAttendance(this,attendance);*/

    }

/*
    public static void writeAttendance1(Context context, Attendance attendance7)  {
        Attendance myattendance = new Attendance();
        Attendance attendance = new Attendance();
        attendance.attendance = new ArrayList<>();
        attendance.attendance.add("bgiu");
        attendance.attendance.add("nioi");


        try {
            Workbook workbook = null;
            // workbook = new HSSFWorkbook();
            File myfile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Attendance.xlsx");
            //myfile.createNewFile();
            //workbook = new XSSFWorkbook(myfile.getPath());
            try {
                workbook = new XSSFWorkbook(new FileInputStream(myfile));
            } catch (Throwable e ) {
                workbook = new XSSFWorkbook();
            }
            SectionInfo sectionInfo = attendance.sectionInfo;
            //String sheetname = sectionInfo.degree+sectionInfo.branch+sectionInfo.year+sectionInfo.sem+sectionInfo.sectionName;
            String sheetname = "pojugug";


            Sheet sheet = workbook.getSheet(sheetname);
            if (sheet==null) {
                sheet = workbook.createSheet(sheetname);
            }

            // Sheet sheet = workbook.createSheet("Countries");
            Row row = sheet.createRow(0);
            int noOfColumns = sheet.getRow(0).getPhysicalNumberOfCells();
            //noOfColumns++;
            int rowIndex = 0;
            Iterator<String> iterator = attendance.attendance.iterator();
            while(iterator.hasNext()){
                String attendance1 = iterator.next();
                row = sheet.createRow(rowIndex++);
                Cell cell0 = row.createCell(noOfColumns);
                cell0.setCellValue(attendance1);
                //Cell cell1 = row.createCell(1);
                //cell1.setCellValue(country.getShortCode());
            }





            FileOutputStream fos = new FileOutputStream(myfile);
            workbook.write(fos);
            fos.close();
            workbook.close();
        }catch (Throwable e) {
            e.printStackTrace();

            Toast.makeText(context, "Failed to update Excel sheet", Toast.LENGTH_SHORT).show();
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }
*/


    public void writeAttendance(Context context, Attendance attendance) throws IOException {
       /* Attendance myattendance = new Attendance();
        Attendance attendance = new Attendance();
        attendance.attendance = new ArrayList<>();
        attendance.attendance.add("bgiu");
        attendance.attendance.add("nioi");*/

/*
        InputStream myInput;
// initialize asset manager
        AssetManager assetManager = getAssets();
//  open excel file name as myexcelsheet.xls
        myInput = assetManager.open("myexcelsheet.xls");
// Create a POI File System object
        POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
// Create a workbook using the File System
        HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
// Get the first sheet from workbook
        HSSFSheet mySheet = myWorkBook.getSheetAt(0);*/


        try {
            Workbook workbook = null;
            // workbook = new HSSFWorkbook();
            File myfile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"Attendance.xlsx");
            //myfile.createNewFile();
            FileInputStream fileInputStream = null;


            Log.e("TAG", "Reading from Excel" + myfile);

            // Create instance having reference to .xls file

            if (!myfile.exists()) {
                //myfile.createNewFile();
                //fileInputStream = new FileInputStream(myfile);
                workbook = new XSSFWorkbook();
            } else {
                fileInputStream = new FileInputStream(myfile);
                workbook = WorkbookFactory.create(fileInputStream);
                // workbook = new XSSFWorkbook(fileInputStream);
                fileInputStream.close();
            }


            //new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Attendance.xlsx");
            //myfile.createNewFile();
            //workbook = new XSSFWorkbook(myfile.getPath());
            try {
                //workbook = new XSSFWorkbook(new FileInputStream(myfile));

                //workbook = WorkbookFactory.create(new FileInputStream(myfile));
            } catch (Throwable e ) {
                e.printStackTrace();
                // workbook = new XSSFWorkbook();
            }
            SectionInfo sectionInfo = attendance.sectionInfo;
            //String sheetname = sectionInfo.degree+sectionInfo.branch+sectionInfo.year+sectionInfo.sem+sectionInfo.sectionName;
            String sheetname = attendance.sectionInfo.classId + attendance.courseCode;  //"pojugug";


            Sheet sheet = workbook.getSheet(sheetname);
            if (sheet==null) {
                sheet = workbook.createSheet(sheetname);
            }

            // Sheet sheet = workbook.createSheet("Countries");
            //int currentColumn = sheet.createRow(0).getLastCellNum();
            Row firstRow = sheet.createRow(0);
            int i = sheet.getLastRowNum()+1;
            //int i=1;
            /*
            while (true) {
                Cell currentCell = firstRow.createCell(i);
                if (firstRow.getCell(i).getCellTypeEnum()!= CellType.BLANK) {
                    Toast.makeText(context, i + " is filled", Toast.LENGTH_SHORT).show();
                        i++;
                       // continue;
                    } else {
                    Toast.makeText(context, i + " is blank", Toast.LENGTH_SHORT).show();
                    break;
                }
            }
*/





            int noOfColumns = 0 ;//= sheet.getRow(0).getPhysicalNumberOfCells();
            //noOfColumns++;
            //noOfColumns = currentColumn+1;
            createCell(sheet.createRow(i),noOfColumns++,attendance.dateInfo);
            int rowIndex = i;
            Row row = sheet.createRow(rowIndex++);
            Iterator<String> iterator = attendance.attendance.iterator();
            while(iterator.hasNext()){
                String attendance1 = iterator.next();

                Cell cell0 = row.createCell(noOfColumns++);
                cell0.setCellValue(attendance1);
                //Cell cell1 = row.createCell(1);
                //cell1.setCellValue(country.getShortCode());
            }





            FileOutputStream fos = new FileOutputStream(myfile);
            workbook.write(fos);
            fos.close();
            workbook.close();
        }catch (Throwable e) {
            e.printStackTrace();

            Toast.makeText(context, "Failed to update Excel sheet", Toast.LENGTH_SHORT).show();
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }

    public static void writeDataLineByLine(Context c, Attendance attendance) throws IOException {
        // first create file object for file placed at location
        // specified by filepath
        String filename = attendance.sectionInfo.classId+attendance.courseCode+attendance.dateInfo;
        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Attendance.csv");
       // File file = new File(filePath);
        if (file.createNewFile()  ) {
            Toast.makeText(c,"Created CSV File",Toast.LENGTH_SHORT).show();
        }
        try {
            // create FileWriter object with file as parameter
            FileWriter outputfile = new FileWriter(file,true);

            // create CSVWriter object filewriter object as parameter
            CSVWriter writer = new CSVWriter(new FileWriter(file,true),',',CSVWriter.NO_QUOTE_CHARACTER,CSVWriter.DEFAULT_ESCAPE_CHARACTER,"\r\n");
            //new CSVWriter(outputfile);

            // adding header to csv
           // String[] header = { "Name", "Class", "Marks" };
            String[] header = new String[100];
            for (int i=0;i<attendance.attendance.size();i++) {
                header[i] = attendance.attendance.get(i);
            }

            writer.writeNext(header);


            // add data to csv
            String[] data1 = { "Aman", "10", "620" };
            writer.writeNext(data1);
            String[] data2 = { "Suraj", "10", "630" };
            writer.writeNext(data2);

            // closing writer connection
            writer.close();
        }
        catch (IOException e) {
            //  Auto-generated catch block
            e.printStackTrace();
        }
    }



    // ORIGINAL COPY DO NOT TOUCH

    /*
    public void writeAttendance(Context context, Attendance attendance) throws IOException {
       /* Attendance myattendance = new Attendance();
        Attendance attendance = new Attendance();
        attendance.attendance = new ArrayList<>();
        attendance.attendance.add("bgiu");
        attendance.attendance.add("nioi");

/*
        InputStream myInput;
// initialize asset manager
        AssetManager assetManager = getAssets();
//  open excel file name as myexcelsheet.xls
        myInput = assetManager.open("myexcelsheet.xls");
// Create a POI File System object
        POIFSFileSystem myFileSystem = new POIFSFileSystem(myInput);
// Create a workbook using the File System
        HSSFWorkbook myWorkBook = new HSSFWorkbook(myFileSystem);
// Get the first sheet from workbook
        HSSFSheet mySheet = myWorkBook.getSheetAt(0);   * /


        try {
            Workbook workbook = null;
            // workbook = new HSSFWorkbook();
            File myfile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),"Attendance.xlsx");
            //myfile.createNewFile();
            FileInputStream fileInputStream = null;
            fileInputStream = new FileInputStream(myfile);
            Log.e("TAG", "Reading from Excel" + myfile);

            // Create instance having reference to .xls file

            if (!myfile.exists()) {
                workbook = new XSSFWorkbook(fileInputStream);
            } else {
                workbook = WorkbookFactory.create(fileInputStream);
                // workbook = new XSSFWorkbook(fileInputStream);
            }
            fileInputStream.close();

            //new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Attendance.xlsx");
            //myfile.createNewFile();
            //workbook = new XSSFWorkbook(myfile.getPath());
            try {
                //workbook = new XSSFWorkbook(new FileInputStream(myfile));

                //workbook = WorkbookFactory.create(new FileInputStream(myfile));
            } catch (Throwable e ) {
                e.printStackTrace();
                // workbook = new XSSFWorkbook();
            }
            SectionInfo sectionInfo = attendance.sectionInfo;
            //String sheetname = sectionInfo.degree+sectionInfo.branch+sectionInfo.year+sectionInfo.sem+sectionInfo.sectionName;
            String sheetname = attendance.sectionInfo.classId + attendance.courseCode;  //"pojugug";


            Sheet sheet = workbook.getSheet(sheetname);
            if (sheet==null) {
                sheet = workbook.createSheet(sheetname);
            }

            // Sheet sheet = workbook.createSheet("Countries");
            //int currentColumn = sheet.createRow(0).getLastCellNum();
            Row firstRow = sheet.createRow(1);
            int i = sheet.getLastRowNum()+1;
            //int i=1;
            /*
            while (true) {
                Cell currentCell = firstRow.createCell(i);
                if (firstRow.getCell(i).getCellTypeEnum()!= CellType.BLANK) {
                    Toast.makeText(context, i + " is filled", Toast.LENGTH_SHORT).show();
                        i++;
                       // continue;
                    } else {
                    Toast.makeText(context, i + " is blank", Toast.LENGTH_SHORT).show();
                    break;
                }
            }

/*




            int noOfColumns = 1 ;//= sheet.getRow(0).getPhysicalNumberOfCells();
            //noOfColumns++;
            //noOfColumns = currentColumn+1;
            createCell(firstRow,i,attendance.dateInfo);
            int rowIndex = i;
            Row row = sheet.createRow(rowIndex++);
            Iterator<String> iterator = attendance.attendance.iterator();
            while(iterator.hasNext()){
                String attendance1 = iterator.next();

                Cell cell0 = row.createCell(noOfColumns++);
                cell0.setCellValue(attendance1);
                //Cell cell1 = row.createCell(1);
                //cell1.setCellValue(country.getShortCode());
            }





            FileOutputStream fos = new FileOutputStream(myfile);
            workbook.write(fos);
            fos.close();
            workbook.close();
        }catch (Throwable e) {
            e.printStackTrace();

            Toast.makeText(context, "Failed to update Excel sheet", Toast.LENGTH_SHORT).show();
            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
        }

    }
  */

}
package com.example.covid_19shoppingcentre;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.covid_19shoppingcentre.Common.ReportCommonJava;
import com.example.covid_19shoppingcentre.models.PdfDocumentAdapterJava;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.itextpdf.text.pdf.draw.VerticalPositionMark;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class GenerateDailyReportJava extends AppCompatActivity {

    Button btn_create_pdf;
    DatabaseReference reff;
    int count = 0;
    int countCheckIn = 0;
    int countCheckOut = 0;
    int countAbnormal = 0;
    int totalCust = 0;

    int verifyBtn = 0;
    String key;
    String a;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.generate_daily_report_java);

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime currentDateTime  = LocalDateTime.now();
        DateTimeFormatter dateFormat  = DateTimeFormatter.ofPattern("yyyyMMdd");
        String dateText = currentDateTime.format(dateFormat);

        btn_create_pdf = (Button)findViewById(R.id.btn_create_pdf);

        Dexter.withActivity(this)
                .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse response) {
                        btn_create_pdf.setOnClickListener(new View.OnClickListener(){
                            @Override
                            public void onClick(View view){
                                verifyBtn = 1;
                                reff = FirebaseDatabase.getInstance().getReference().child("ShoppingCentre").child(dateText);

                                reff.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        count = 1;
                                        totalCust = 0;
                                        count = 0;
                                        countCheckIn = 0;
                                        countCheckOut = 0;
                                        countAbnormal = 0;

                                        for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                                            count = 2;
                                            if (postSnapshot.exists()) {
                                                count = 3;
                                                key = postSnapshot.getKey();
                                                a = dataSnapshot.child(key).child("status").getValue().toString();
                                                if (dataSnapshot.child(key).child("status").getValue().toString().equals("checkIn")) {
                                                    count = 4;
                                                    countCheckIn = countCheckIn + 1;
                                                } else if (dataSnapshot.child(key).child("status").getValue().toString().equals("checkOut")) {
                                                    count = 4;
                                                    countCheckOut = countCheckOut + 1;
                                                } else {
                                                    count = 4;
                                                    countAbnormal = countAbnormal + 1;
                                                }
                                            }
                                        }
                                        if(count == 4) {
                                            createPDFFile(ReportCommonJava.getAppPath(GenerateDailyReportJava.this) + "test_pdf.pdf");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError error) {
                                        Toast.makeText(getApplicationContext(),"Error Access Database",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse response) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                    }
                })
                .check();
    }

    private void createPDFFile(String path){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime currentDateTime  = LocalDateTime.now();
        DateTimeFormatter dateFormat  = DateTimeFormatter.ofPattern("yyyyMMdd");
        String dateText = currentDateTime.format(dateFormat);
        String name = getIntent().getStringExtra("Count");
        String printer = getIntent().getStringExtra("PrinterName");


        if(new File(path).exists())
            new File(path).delete();
        try{

            totalCust = countAbnormal + countCheckIn + countCheckOut;

            Document document = new Document();
            //save
            PdfWriter.getInstance(document, new FileOutputStream(path));

            //open document
            document.open();

            //setting
            document.setPageSize(PageSize.A4);
            document.addCreationDate();
            document.addAuthor("Enter Checking System");
            document.addCreator("Choo Yao Song");

            //font Setting
            BaseColor colorAccent = new BaseColor(0,153,204,255);
            float fontSize = 20.0f;
            float valueFontSize = 26.0f;

            //custom font
            BaseFont fontName = BaseFont.createFont("assets/fonts/SourceSansPro-Regular.otf", "UTF-8", BaseFont.EMBEDDED);

            //Create titles of Document
            Font titleFont = new Font(fontName, 36f, Font.NORMAL,BaseColor.BLACK);
            addNewItem(document, "Daily Reports", Element.ALIGN_CENTER, titleFont);

            //add more
            Font orderNumberFont = new Font(fontName, fontSize,Font.NORMAL, colorAccent);
            //addNewItem(document, "Order No:", Element.ALIGN_LEFT, orderNumberFont);

            Font orderNumberValueFont = new Font(fontName, fontSize,Font.NORMAL, BaseColor.BLACK);
            //addNewItem(document, "#717171", Element.ALIGN_LEFT, orderNumberValueFont);

            addLineSeperator(document);

            addNewItem(document, "Generate Date", Element.ALIGN_LEFT, orderNumberFont);
            addNewItem(document, dtf.format(now), Element.ALIGN_LEFT, orderNumberValueFont);

            addLineSeperator(document);

            addNewItem(document, "Staff Name:", Element.ALIGN_LEFT, orderNumberFont);
            addNewItem(document, printer, Element.ALIGN_LEFT, orderNumberValueFont);

            addLineSeperator(document);

            //Add Product order detail
            addLineSpace(document);
            addNewItem(document,"Reports Detail", Element.ALIGN_CENTER, titleFont);
            addLineSeperator(document);

            //Item
            addNewItem(document, "Total Customer : " + totalCust, Element.ALIGN_LEFT, orderNumberValueFont);
            /* addNewItemWithLeftAndRight(document, "12.0*1000", "12000.0", titleFont, orderNumberValueFont); */

            addLineSpace(document);

            addNewItem(document, "Total Customer CheckIn : " + countCheckIn, Element.ALIGN_LEFT, orderNumberValueFont);

            addLineSpace(document);

            addNewItem(document, "Total Customer CheckOut : " + countCheckOut, Element.ALIGN_LEFT, orderNumberValueFont);

            addLineSpace(document);

            addNewItem(document, "Total Abnormal Record : " + countAbnormal, Element.ALIGN_LEFT, orderNumberValueFont);

            //addNewItemWithLeftAndRight(document, "Pizza 25", "(0.0%)", titleFont, orderNumberValueFont);
            //addNewItemWithLeftAndRight(document, "12.0*1000", "12000.0", titleFont, orderNumberValueFont);

            addLineSeperator(document);

            //Total
            //addLineSpace(document);
            //addLineSpace(document);

            document.close();

            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();


            printPDF();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }
    }

    private void printPDF() {
        totalCust = 0;
        count = 0;
        countCheckIn = 0;
        countCheckOut = 0;
        countAbnormal = 0;

        PrintManager printManager = (PrintManager)getSystemService(Context.PRINT_SERVICE);
        try{
            PrintDocumentAdapter printDocumentAdapter = new PdfDocumentAdapterJava(GenerateDailyReportJava.this, ReportCommonJava.getAppPath(GenerateDailyReportJava.this) + "test_pdf.pdf");
            printManager.print("Document", printDocumentAdapter, new PrintAttributes.Builder().build());
        } catch (Exception e) {
            Log.e("Error", ""+e.getMessage());
        }
    }

    private void addNewItemWithLeftAndRight(Document document, String textLeft, String textRight, Font textLeftFont, Font textRightFont) throws DocumentException {
        Chunk chunkTextLeft = new Chunk(textLeft, textLeftFont);
        Chunk chunkTextRight = new Chunk(textLeft, textRightFont);
        Paragraph p = new Paragraph(chunkTextLeft);
        p.add(new Chunk(new VerticalPositionMark()));
        p.add(chunkTextRight);
        document.add(p);
    }

    private void addLineSeperator(Document document) throws DocumentException {
        LineSeparator lineSeparator = new LineSeparator();
        lineSeparator.setLineColor(new BaseColor(0,0,0,68));
        addLineSpace(document);
        document.add(new Chunk(lineSeparator));
        addLineSpace(document);
    }

    private void addLineSpace(Document document) throws DocumentException {
        document.add(new Paragraph(""));
    }

    private void addNewItem(Document document, String text, int align, Font font) throws DocumentException {
        Chunk chunk = new Chunk(text,font);
        Paragraph paragraph = new Paragraph(chunk);
        paragraph.setAlignment(align);
        document.add(paragraph);
    }
}
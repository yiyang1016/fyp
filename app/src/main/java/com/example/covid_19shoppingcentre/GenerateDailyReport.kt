package com.example.covid_19shoppingcentre

import android.Manifest
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.print.PrintAttributes
import android.print.PrintManager
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.example.covid_19shoppingcentre.Common.ReportCommon
import com.example.covid_19shoppingcentre.models.PdfDocumentAdapter
import com.itextpdf.text.*
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfWriter
import com.itextpdf.text.pdf.draw.LineSeparator
import com.itextpdf.text.pdf.draw.VerticalPositionMark
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.generate_daily_report.*
import java.io.File
import java.io.*

class GenerateDailyReport : AppCompatActivity() {

    val file_name : String = "Daily Report.pdf"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.generate_daily_report)

        setActionBar()

        Dexter.withActivity(this@GenerateDailyReport)
            .withPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object:PermissionListener{
                override fun onPermissionGranted(response: PermissionGrantedResponse?) {
                    Toast.makeText(
                        applicationContext,
                        "Test2",
                        Toast.LENGTH_SHORT
                    ).show()
                    btnGenDailyRe.setOnClickListener {
                        Toast.makeText(
                            applicationContext,
                            "Test1",
                            Toast.LENGTH_SHORT
                        ).show()
                        createPDFFile(ReportCommon.getAppPath(this@GenerateDailyReport)+file_name)
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permission: PermissionRequest?,
                    token: PermissionToken?
                ) {
                    Toast.makeText(
                        applicationContext,
                        "Test3",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse?) {
                    Toast.makeText(
                        applicationContext,
                        "Test4",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })

    }

    @Throws(DocumentException::class)
    private fun createPDFFile(path: String) {
        if (File(path).exists())
            File(path).delete()
        try {
            val document = Document()
            Toast.makeText(
                applicationContext,
                "Test4",
                Toast.LENGTH_SHORT
            ).show()

            PdfWriter.getInstance(document,  FileOutputStream(path))
            Toast.makeText(
                applicationContext,
                "Test3",
                Toast.LENGTH_SHORT
            ).show()
            document.open()
            Toast.makeText(
                applicationContext,
                "Test2",
                Toast.LENGTH_SHORT
            ).show()

            document.pageSize = PageSize.A4
            document.addCreationDate()
            document.addAuthor("FYP Project")
            document.addCreator("Choo Yao Song")

            val colorAccent = BaseColor(0,153,204,255)
            val headingFontSize = 20.0f
            val valueFontSize = 26.0f

            val fontName = BaseFont.createFont("assets/fonts/SourceSansPro-Regular.otf","UTF-8",BaseFont.EMBEDDED)

            val titleStyle = Font(fontName, 36.0f,Font.NORMAL, BaseColor.BLACK)
            addNewItem(document, "Daily Report", Element.ALIGN_CENTER,titleStyle)

            val headingStyle = Font(fontName, headingFontSize,Font.NORMAL,colorAccent)
            addNewItem(document, "Order No:", Element.ALIGN_LEFT, headingStyle)

            val valueStyle = Font(fontName, headingFontSize,Font.NORMAL,colorAccent)
            addNewItem(document, "#123123", Element.ALIGN_LEFT, valueStyle)

            addLineSpace(document)
            addLineSeperator(document)
            addNewItem(document, "Order Date:", Element.ALIGN_LEFT, valueStyle)
            addNewItem(document, "03/08/2019", Element.ALIGN_LEFT, valueStyle)

            addLineSeperator(document)
            addNewItem(document, "Account Name", Element.ALIGN_LEFT, valueStyle)
            addNewItem(document, "Yao Song", Element.ALIGN_LEFT, valueStyle)

            addLineSeperator(document)

            addLineSpace(document)
            addNewItem(document, "Product Details", Element.ALIGN_CENTER,titleStyle)

            addLineSeperator(document)
            addNewItemWithLeftAndRight(document,"Pizza 25","(0.0%)", titleStyle,valueStyle)
            addNewItemWithLeftAndRight(document,"12.0*1000","12000.0", titleStyle,valueStyle)

            addLineSeperator(document)
            addNewItemWithLeftAndRight(document,"Pizza 25","(0.0%)", titleStyle,valueStyle)
            addNewItemWithLeftAndRight(document,"12.0*1000","12000.0", titleStyle,valueStyle)

            addLineSeperator(document)
            addLineSpace(document)
            addLineSpace(document)

            addNewItemWithLeftAndRight(document,"Total","24000.0", titleStyle,valueStyle)

            document.close()

            Toast.makeText(
                applicationContext,
                "Report has Generated",
                Toast.LENGTH_SHORT
            ).show()

        } catch (e:Exception){
            Log.e("Error", ""+e.message)
        }
    }

    private fun printPDF(){
        val printManager = getSystemService(Context.PRINT_SERVICE) as PrintManager
        try{
            val printAdapter = PdfDocumentAdapter(this@GenerateDailyReport, ReportCommon.getAppPath(this@GenerateDailyReport)+ file_name)
            printManager.print("Document",printAdapter,PrintAttributes.Builder().build())
        }catch (e:Exception){
            Log.e("Error", ""+e.message)
        }
    }

    @Throws(DocumentException::class)
    private fun addNewItemWithLeftAndRight(document: Document, textLeft: String, textRight: String,leftStyle: Font, rightStyle: Font) {
        val chuckTextLeft = Chunk(textLeft,leftStyle)
        val chuckTextRight = Chunk(textRight,rightStyle)
        val p = Paragraph(chuckTextLeft)
        p.add(Chunk(VerticalPositionMark()))
        p.add(chuckTextRight)
        document.add(p)
    }

    @Throws(DocumentException::class)
    private fun addLineSeperator(document: Document){
       val lineSeperator = LineSeparator()
        lineSeperator.lineColor = BaseColor(0,0,0,68)
        addLineSpace(document)
        document.add(Chunk(lineSeperator))
        addLineSpace(document)
    }

    @Throws(DocumentException::class)
    private fun addLineSpace(document: Document){
        document.add(Paragraph(""))
    }

    @Throws(DocumentException::class)
    private fun addNewItem(document: Document, text:String, align:Int, style: Font){
        val chunk = Chunk(text,style)
        val p = Paragraph(chunk)
        p.alignment = align
        document.add(p)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent1 = Intent(this, ReserveStore_List::class.java).apply {
            putExtra("EXTRA_MESSAGE", "message")
        }
        startActivity(intent1)
        return false
    }

    private fun setActionBar(){
        val actionBar: ActionBar? = supportActionBar
        actionBar!!.title = "Report"
        actionBar!!.setDisplayHomeAsUpEnabled(true)
    }
}
package com.example.covid_19shoppingcentre.models

import android.content.Context
import android.graphics.pdf.PdfDocument
import android.os.Bundle
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.print.PageRange
import android.print.PageRange.ALL_PAGES
import android.print.PrintAttributes
import android.print.PrintDocumentAdapter
import android.print.PrintDocumentInfo
import android.util.Log
import java.io.*

class PdfDocumentAdapter(context: Context, path:String): PrintDocumentAdapter() {
    internal var context:Context?=null
    internal var path = ""

    init {
        this.context = context
        this.path = path
    }

    override fun onLayout(
        oldAttributes: PrintAttributes?,
        newAttributes: PrintAttributes?,
        cancellationSignal: CancellationSignal?,
        callback: LayoutResultCallback?,
        extras: Bundle?
    ) {
        if(cancellationSignal!!.isCanceled)
            callback!!.onLayoutCancelled()
        else{
            val builder = PrintDocumentInfo.Builder("Report")
            builder.setContentType(PrintDocumentInfo.CONTENT_TYPE_DOCUMENT)
                .setPageCount(PrintDocumentInfo.PAGE_COUNT_UNKNOWN)
                .build()
            callback!!.onLayoutFinished(builder.build(), newAttributes != oldAttributes)
        }
    }

    override fun onWrite(
        pageRanges: Array<out PageRange>?,
        parcelFileDescriptor: ParcelFileDescriptor?,
        cancellationSignal: CancellationSignal?,
        writeResultCallback: WriteResultCallback?
    ) {
        var inputS : InputStream?=null
        var out : OutputStream?=null

        try{
            val file = File(path)
            inputS = FileInputStream(file)
            out = FileOutputStream(parcelFileDescriptor!!.fileDescriptor)

            if(!cancellationSignal!!.isCanceled) {
                inputS.copyTo(out)
                writeResultCallback!!.onWriteFinished(arrayOf(ALL_PAGES))
            }
            else
                writeResultCallback!!.onWriteCancelled()
        }catch(e:Exception){
            writeResultCallback!!.onWriteFailed(e.message)
            Log.e("Error", e.message)
        }finally{
            try{
                inputS!!.close()
                out!!.close()
            }catch (e:IOException){
                Log.e("Error", e.message)
            }
        }
    }
}
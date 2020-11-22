package com.example.covid_19shoppingcentre

import android.R.attr
import android.app.Activity
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.zxing.integration.android.IntentIntegrator
import kotlinx.android.synthetic.main.member_registration.*
import kotlinx.android.synthetic.main.qr_scanner.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class QRCodeScanner : AppCompatActivity(){
    private var checkInDate: String?=null
    private var checkInTime: String?=null
    private var checkOutTime: String? =null
    /////////////////////////////////////////
    private var customerId: String?=null
    ////////////////////////////////////////
    private var status: String? =null
    private var storeId: String? =null
    private var checkoutStoreID: String? =null
    private var bodyTemp: String? =null
    private var userDatabase =  FirebaseDatabase.getInstance().getReference("NewCheckInStore")
    private var mDatabase = FirebaseDatabase.getInstance().getReference("ShoppingCentre")
    private var Database = FirebaseDatabase.getInstance().getReference()
    private lateinit var checkInStoreId:String
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.qr_scanner)
        /////////////////////////////////////////////////////
        val intent1: Intent = intent
        customerId= intent1.getStringExtra("MemberID")
        /////////////////////////////////////////////////////
        userDatabase.addListenerForSingleValueEvent( object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                var num: Int = 1
                var num1: Int = 0
                if (snapshot.exists()){
                    num += snapshot.childrenCount.toInt()
                    num1 += snapshot.childrenCount.toInt()
                }
                if(num<10){
                    checkInStoreId = "CS0000$num"
                    checkoutStoreID = "CS0000$num1"
                }else if(num<100){
                    checkInStoreId = "CS000$num"
                    checkoutStoreID = "CS000$num1"
                }else if(num<1000){
                    checkInStoreId = "CS00$num"
                    checkoutStoreID = "CS00$num1"
                }else if(num<10000){
                    checkInStoreId = "CS0$num"
                    checkoutStoreID = "CS0$num1"
                }else{
                    checkInStoreId = "CS$num"
                    checkoutStoreID = "CS$num1"
                }
            }
        })

        btnCamActivate.setOnClickListener{
            val scanner = IntentIntegrator(this)
            scanner.initiateScan()
        }
    }

    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            val result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
            if(result != null) {
                if(result.contents == null) {
                    Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
                } else {
                    var results:String = result.contents.substring(0,1)
                    if (results == "I"){

                        storeId = result.contents.substring(1)
                        val date = getCurrentDateTime()
                    val currentDateTime = date.toString("yyyy/MM/dd")
                    val hourFormat = date.toString("hh:mm aa")


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        val currentDateTime1  = LocalDateTime.now()
                        val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
                        val dateText = currentDateTime1.format(dateFormat)
                        val hourMinuteFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a")
                        val hourFormat1: DateTimeFormatter = DateTimeFormatter.ofPattern("H")
                        val hourMinuteText = currentDateTime1.format(hourMinuteFormat)
                        val hourFormatText = currentDateTime1.format(hourFormat1)
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////



                        checkInDate = currentDateTime
                    checkInTime = hourFormat
                        mDatabase.addListenerForSingleValueEvent(object: ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for(i in snapshot.children){
                                    val comparedate = currentDateTime.replace("/","")
                                    if(i.hasChild(customerId!!)&&i.key.toString()== comparedate&&i.child(customerId!!).child("status").value.toString() == "checkIn"){
                                        var tempString = i.child(customerId!!).child("bodyTemperature").value.toString()
                                        bodyTemp = tempString
                                        var temp = tempString.substring(0,4).toFloat()
                                        if(temp> 37.5){
                                            status = "Fever"
                                        }else{
                                            status="Normal"
                                        }
                                        userDatabase.child(checkInStoreId).setValue(MemberCheckInStore(checkInDate,checkInTime,customerId,status,
                                            storeId!!,bodyTemp))

                                        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                        Database.child("CheckInStore").child(dateText.toString()).child(storeId!!).child(hourFormatText.toString()).child(customerId.toString()).child("checkInTime").setValue(hourMinuteText.toString())
                                        Database.child("CheckInStore").child(dateText.toString()).child(storeId!!).child(hourFormatText.toString()).child(customerId.toString()).child("checkOutTime").setValue("pending")
                                        Database.child("CheckInStore").child(dateText.toString()).child(storeId!!).child(hourFormatText.toString()).child(customerId.toString()).child("customerId").setValue(customerId.toString())
                                        Database.child("CheckInStore").child(dateText.toString()).child(storeId!!).child(hourFormatText.toString()).child(customerId.toString()).child("status").setValue("active")
                                        ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                                    }else{
                                        Toast.makeText(this@QRCodeScanner, "Please checked in at the Shopping Mall Main entrance",Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })

                 }else if(results == "O"){
                        val date = getCurrentDateTime()
                        val hourFormat = date.toString("hh:mm aa")
                        checkOutTime  = hourFormat
                        userDatabase.child(checkInStoreId!!).child("checkOutTime").setValue(checkOutTime)

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
                        val currentDateTime2  = LocalDateTime.now()
                        val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
                        val dateText = currentDateTime2.format(dateFormat)
                        val hourFormat1: DateTimeFormatter = DateTimeFormatter.ofPattern("H")
                        val hourFormatText = currentDateTime2.format(hourFormat1)

                        Database.child("CheckInStore").child(dateText.toString()).child(storeId!!).child(hourFormatText.toString()).child(customerId.toString()).child("checkOutTime").setValue(checkOutTime.toString())
                        Database.child("CheckInStore").child(dateText.toString()).child(storeId!!).child(hourFormatText.toString()).child(customerId.toString()).child("status").setValue("pass")
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

                    }else{
                        Toast.makeText(this, "Please Scan the correct store QR code", Toast.LENGTH_LONG).show()
                    }
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

}
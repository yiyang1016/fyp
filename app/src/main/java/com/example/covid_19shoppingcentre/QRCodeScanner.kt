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
    val intent1: Intent = intent
    private var customerId: String = intent1.getStringExtra("MemberID")
    ////////////////////////////////////////
    private var status: String? =null
    private var storeId: String? =null
    private var checkoutStoreID: String? =null
    private var bodyTemp: String? =null
    private var userDatabase =  FirebaseDatabase.getInstance().getReference("NewCheckInStore")
    private var mDatabase = FirebaseDatabase.getInstance().getReference("ShoppingCentre")
    private lateinit var checkInStoreId:String
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.qr_scanner)

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
                        checkInDate = currentDateTime
                    checkInTime = hourFormat
                        mDatabase.addListenerForSingleValueEvent(object: ValueEventListener{
                            override fun onDataChange(snapshot: DataSnapshot) {
                                for(i in snapshot.children){
                                    if(i.hasChild(customerId!!)){
                                        var tempString = i.child(customerId!!).child("bodyTemperature").value.toString()
                                        bodyTemp = tempString
                                        var temp = tempString.substring(0,4).toFloat()
                                        if(temp> 37.5){
                                            status = "Fever"
                                        }else{
                                            status="Normal"
                                        }
                                    }
                                }
                                userDatabase.child(checkInStoreId).setValue(MemberCheckInStore(checkInDate,checkInTime,customerId,status,
                                    storeId!!,bodyTemp))
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })

                 }else if(results == "O"){
                        customerId = "M00001"
                        val date = getCurrentDateTime()
                        val hourFormat = date.toString("hh:mm aa")
                        checkOutTime  = hourFormat
                        Log.d("PRINTOUTRESULT","====================================$checkOutTime==============================")
                        userDatabase.child(checkInStoreId!!).child("checkOutTime").setValue(checkOutTime)
                    }else{
                        Toast.makeText(this, "Please Scan the correct store QR code", Toast.LENGTH_LONG).show();
                    }
                }
            } else {
                super.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

}
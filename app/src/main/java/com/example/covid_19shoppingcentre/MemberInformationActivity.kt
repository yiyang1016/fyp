package com.example.covid_19shoppingcentre

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.covid_19shoppingcentre.models.addShoppingCentreCheckIn
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import github.nisrulz.qreader.QRDataListener
import github.nisrulz.qreader.QREader
import kotlinx.android.synthetic.main.sc_member_check_in_information.*
import kotlinx.android.synthetic.main.sc_member_check_in_scanner.*
import java.text.DateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class MemberInformationActivity : AppCompatActivity() {

    private var userDatabase = FirebaseDatabase.getInstance().getReference()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sc_member_check_in_information)

        getMemberInformation()
        setActionBar()

        val checkInButton = findViewById<Button>(R.id.checkInBtn)
        val backButton = findViewById<Button>(R.id.backBtn)

        checkInButton.setOnClickListener {
            checkInMember()
        }

        backButton.setOnClickListener {
            back()
        }

        bodyTemperature.addTextChangedListener(object: TextWatcher{
            override fun afterTextChanged(p0: Editable?) {
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (TextUtils.isEmpty(bodyTemperature.text)){
                    return
                    bodyTemperatureResult.text = "Please enter the customer's temperature"
                    checkInButton.isEnabled = false
                }
                val getTemperature: Double = bodyTemperatureResult.text.toString().toDouble()
                if (getTemperature in 0.0..35.9){
                    bodyTemperatureResult.text = "Temperature too low, are you OK?"
                }
                else if (getTemperature in 36.0..37.5){
                    bodyTemperatureResult.text = "Body Temperature is fine. customer are allowed to enter the Shopping Centre"
                }
                else{
                    bodyTemperatureResult.text = "Body Temperature is too high. customer are not allowed to enter the Shopping Centre"
                    checkInButton.isEnabled = false
                }
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getMemberInformation() {
        val calendar1 = Calendar.getInstance()
        val currentDay = DateFormat.getDateInstance(DateFormat.FULL).format(calendar1.time)
        val currentDateTime  = LocalDateTime.now()
        val hourMinuteFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a")
        val hourMinuteText = currentDateTime.format(hourMinuteFormat)

        val dataSent = intent.getStringExtra("EXTRA_MESSAGE")

        val query = userDatabase.child("Member").orderByChild("Id").equalTo(dataSent)
        query.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(s0: DataSnapshot) {
                try {
                    for (s0 in s0.children) {
                        val ref= userDatabase.child("Member").child(s0.key.toString())
                        ref.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(p0: DataSnapshot) {
                                try {
                                    if (p0.exists()) {
                                        memberName.text = (p0.child("Name").value.toString())
                                        memberIC.text = (p0.child("IC_Number").value.toString())
                                        memberPhone.text = (p0.child("PhoneNumber").value.toString())
                                        CheckInTime.text = "$hourMinuteText"
                                        CheckInDate.text = currentDay.toString()
                                    }
                                } catch (e: Exception) {
                                    Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                } catch (e: Exception) {
                    Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkInMember() {

        val intent = Intent(this, StaffMainActivity::class.java).apply {
            putExtra("EXTRA_MESSAGE", "message")
        }

        val custID = memberName.text.toString()
        val currentDateTime  = LocalDateTime.now()
        val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val hourMinuteFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a")
        val dateText = currentDateTime.format(dateFormat)
        val hourMinuteText = currentDateTime.format(hourMinuteFormat)

        val query = userDatabase.child("Member").orderByChild("Id").equalTo(custID)
        query.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(s0: DataSnapshot) {
                for (s0 in s0.children) {
                    val checkInTime = hourMinuteText.toString().trim()
                    val phone = (s0.child("PhoneNumber").value.toString())
                    val temperature = bodyTemperature.text.toString() + "°C"
                    val statusNow = "checkIn"
                    val checkOutTime = "pending"
                    val name = (s0.child("Name").value.toString())

                    val addQuery = FirebaseDatabase.getInstance().getReference("ShoppingCentre").child(dateText.toString())
                    //val customerID = s0.key.toString()


                    if(custID != null && checkInTime != null && temperature != null && temperature != ""){
                        val writeNewCheckIn = addShoppingCentreCheckIn(checkInTime, name, phone, custID, temperature, statusNow, checkOutTime)

                        addQuery.child(custID).setValue(writeNewCheckIn).addOnCompleteListener {
                            Toast.makeText(
                                applicationContext,
                                "Check In Successful",
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(intent)
                        }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun back() {
        val intent1 = Intent(this, QRScannerActivity::class.java).apply {
            putExtra("EXTRA_MESSAGE", "message")
        }
        startActivity(intent1)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent1 = Intent(this, QRScannerActivity::class.java).apply {
            putExtra("EXTRA_MESSAGE", "message")
        }
        startActivity(intent1)
        return false
    }

    private fun setActionBar(){
        val actionBar: ActionBar? = supportActionBar
        actionBar!!.title = "Member Information"
        actionBar!!.setDisplayHomeAsUpEnabled(true)
    }
}
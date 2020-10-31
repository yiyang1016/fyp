package com.example.covid_19shoppingcentre

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.icu.text.Transliterator
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.widget.*
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

        val temperatures = resources.getStringArray(R.array.Temperature)

        val checkInButton = findViewById<Button>(R.id.checkInBtn)
        val backButton = findViewById<Button>(R.id.backBtn)
        val testBtn = findViewById<Button>(R.id.testing)
        val spinner = findViewById<Spinner>(R.id.temperatureSpinner)

        if (spinner != null){
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, temperatures)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                @SuppressLint("ResourceAsColor")
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    when {
                        temperatures[position].toString() == "below 36.0°C" -> {
                            bodyTemperatureResult.text = "Temperature too low, are you OK?"
                            checkInButton.isEnabled = false
                            checkInButton.setBackgroundColor(R.color.greyColor)
                        }
                        temperatures[position].toString() == "above 37.5°C" -> {
                            bodyTemperatureResult.text = "Body Temperature is too high. customer are not allowed to enter the Shopping Centre"
                            checkInButton.isEnabled = false
                            checkInButton.setBackgroundColor(R.color.greyColor)
                        }
                        temperatures[position].toString() == "Please Select a Temperature" -> {
                            bodyTemperatureResult.text = "Please Select a Temperature"
                            checkInButton.isEnabled = false
                            checkInButton.setBackgroundColor(R.color.greyColor)
                        }
                        else -> {
                            bodyTemperatureResult.text = "Body Temperature is fine. customer are allowed to enter the Shopping Centre"
                            checkInButton.isEnabled = true
                            checkInButton.setBackgroundResource(R.drawable.button_corner2)
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    bodyTemperatureResult.text = "Please select a temperature"
                    checkInButton.isEnabled = false
                }
            }
        }

        checkInButton.setOnClickListener {
            checkInMember()
        }

        backButton.setOnClickListener {
            back()
        }

        testBtn.setOnClickListener {
            checkInMember()
        }
    }

    //@RequiresApi(Build.VERSION_CODES.O)
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getMemberInformation() {
        val calendar1 = Calendar.getInstance()
        val currentDay = DateFormat.getDateInstance(DateFormat.SHORT).format(calendar1.time)
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

        val dataSent = intent.getStringExtra("EXTRA_MESSAGE")

        val intent1 = Intent(this, StaffMainActivity::class.java).apply {
            putExtra("whatMessage", "message")
        }

        val currentDateTime  = LocalDateTime.now()
        val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val hourMinuteFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a")
        val dateText = currentDateTime.format(dateFormat)
        val hourMinuteText = currentDateTime.format(hourMinuteFormat)

        val query = userDatabase.child("Member").orderByChild("Id").equalTo(dataSent)
        query.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(s0: DataSnapshot) {
                for (s0 in s0.children) {
                    val checkInTime = hourMinuteText.toString().trim()
                    val phone = (s0.child("PhoneNumber").value.toString())
                    val temperature = temperatureSpinner.selectedItem.toString().trim()
                    val statusNow = "checkIn"
                    val checkOutTime = "pending"
                    val name = (s0.child("Name").value.toString())

                    val addQuery = FirebaseDatabase.getInstance().getReference("ShoppingCentre").child(dateText.toString())
                    //val customerID = s0.key.toString()

                    if(dataSent != null && checkInTime != null){
                        val writeNewCheckIn = addShoppingCentreCheckIn(checkInTime, name, phone, dataSent, temperature, statusNow, checkOutTime)

                        addQuery.child(dataSent).setValue(writeNewCheckIn).addOnCompleteListener {
                            Toast.makeText(
                                applicationContext,
                                "Check In Successful",
                                Toast.LENGTH_SHORT
                            ).show()
                            startActivity(intent1)
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
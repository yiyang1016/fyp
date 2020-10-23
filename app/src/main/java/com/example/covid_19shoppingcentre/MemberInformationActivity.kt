package com.example.covid_19shoppingcentre

import android.Manifest
import android.content.Intent
import android.os.Build
import android.os.Bundle
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
    val myRef = FirebaseDatabase.getInstance().getReference("Member")

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sc_member_check_in_information)

        val dataSent = intent.getStringExtra("EXTRA_MESSAGE")

        memberName.text = dataSent

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
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getMemberInformation() {
        val name = memberName.text.toString()

        val currentDateTime  = LocalDateTime.now()
        val hourMinuteFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a")
        val hourMinuteText = currentDateTime.format(hourMinuteFormat)

        val query = userDatabase.child("Member").orderByChild("Name").equalTo(name)
        query.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(s0: DataSnapshot) {
                try {
                    for (s0 in s0.children) {
                        val ref= userDatabase.child("Member").child(s0.key.toString())
                        ref.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(p0: DataSnapshot) {
                                try {
                                    if (p0.exists()) {
                                        memberIC.text = (p0.child("IC_Number").value.toString())
                                        memberPhone.text = (p0.child("PhoneNumber").value.toString())
                                        CheckInTime.text = "$hourMinuteText"
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

        val name = memberName.text.toString()

        val currentDateTime  = LocalDateTime.now()
        val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val hourMinuteFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a")
        val dateText = currentDateTime.format(dateFormat)
        val hourMinuteText = currentDateTime.format(hourMinuteFormat)

        val query = userDatabase.child("Member").orderByChild("Name").equalTo(name)
        query.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(s0: DataSnapshot) {
                for (s0 in s0.children) {
                    val checkIntime = hourMinuteText.toString().trim()
                    val cusPhone = (s0.child("PhoneNumber").value.toString())

                    val addQuery = FirebaseDatabase.getInstance().getReference("ShoppingCentre").child(dateText.toString())
                    val memberID = s0.key.toString()

                    if(memberID != null && checkIntime != null){
                        val writeNewCheckIn = addShoppingCentreCheckIn(checkIntime, name, cusPhone)

                        addQuery.child(memberID).setValue(writeNewCheckIn).addOnCompleteListener {
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
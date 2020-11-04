package com.example.covid_19shoppingcentre

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class StaffMainActivity : AppCompatActivity() {
    private var Database = FirebaseDatabase.getInstance().getReference()
    private val EVERY_EIGHT_SECOND: Long = 1000

    // Run on parallel
    private var handler: Handler? = null
    private var runnable: Runnable? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.staff_main)

        val SC_Customer_CheckIn = findViewById<Button>(R.id.SCCheckInBtn)
        val SC_CheckedIn_Customer = findViewById<Button>(R.id.SCCheckInCustomerBtn)
        val SC_View_Store = findViewById<Button>(R.id.viewStoreBtn)

        checkTime()
        executeHandler()

        SC_Customer_CheckIn.setOnClickListener{
            val intent = Intent(this, QRScannerActivity::class.java).apply {
                putExtra("EXTRA_MESSAGE", "message")
            }
            startActivity(intent)
        }

        SC_CheckedIn_Customer.setOnClickListener{
            val intent = Intent(this, CheckInScCustomer_List::class.java).apply {
                putExtra("EXTRA_MESSAGE", "message")
            }
            startActivity(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkTime() {
        val currentDateTime  = LocalDateTime.now()
        val hourFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("HH")
        val hourText = currentDateTime.format(hourFormat)
        val hourNow = hourText.toInt()
        val minuteFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("m")
        val minuteText = currentDateTime.format(minuteFormat)
        val minuteNow = minuteText.toInt()

        if (hourNow >= 10 && minuteNow >= 5) {
            updateStatus("10:00 AM")
        }
        if (hourNow >= 12 && minuteNow >= 5) {
            updateStatus("12:00 AM")
        }
        if (hourNow >= 14 && minuteNow >= 5) {
            updateStatus("2:00 PM")
        }
        if (hourNow >= 16 && minuteNow >= 5) {
            updateStatus("4:00 PM")
        }
        if (hourNow >= 18 && minuteNow >= 5) {
            updateStatus("6:00 PM")
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateStatus(m: String) {
        val currentDateTime  = LocalDateTime.now()
        val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val dateText = currentDateTime.format(dateFormat)

        for(x in 1..9){
            val query = Database.child("Reservation").child(dateText.toString()).child("ST0000$x").child(m).orderByChild("status").equalTo("active")
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(s0: DataSnapshot) {
                    try{
                        for (s0 in s0.children){
                            val updateQuery = Database.child("Reservation").child(dateText.toString()).child("ST0000$x").child(m).child(s0.key.toString())
                            updateQuery.child("status").setValue("cancelled").addOnCompleteListener {
                            }
                        }
                    } catch (e: Exception) {
                        Toast.makeText(applicationContext, "ERROR: $e", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
                }
            })
        }

        for(x in 10..20){
            val query = Database.child("Reservation").child(dateText.toString()).child("ST000$x").child(m).orderByChild("status").equalTo("active")
            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(s0: DataSnapshot) {
                    try{
                        for (s0 in s0.children){
                            val updateQuery = Database.child("Reservation").child(dateText.toString()).child("ST000$x").child(m).child(s0.key.toString())
                            updateQuery.child("status").setValue("cancelled").addOnCompleteListener {
                            }
                        }
                    } catch (e: Exception) {
                        Toast.makeText(applicationContext, "ERROR$e", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
                }
            })
        }

        val anotherQuery = Database.child("ReservationList").orderByChild("date").equalTo(dateText.toString())
        anotherQuery.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(s0: DataSnapshot) {
                try{
                    for (s0 in s0.children){
                        if(s0.child("time").value == m){
                            val updateQuery1 = Database.child("ReservationList").child(s0.key.toString())
                            updateQuery1.child("status").setValue("cancelled").addOnCompleteListener {
                            }
                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(applicationContext, "ERROR: $e", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun executeHandler() {
        //If the handler and runnable are null we create it the first time.
        if (handler == null && runnable == null) {
            handler = Handler()
            runnable = object : Runnable {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun run() {
                    //Updating firebase store/getting
                    checkTime()
                    //And we execute it again
                    handler!!.postDelayed(this, EVERY_EIGHT_SECOND)
                }
            }
        } else {
            handler?.postDelayed(runnable, EVERY_EIGHT_SECOND)

        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        //execute the handler again.
        executeHandler()
    }

    override fun onPause() {
        super.onPause()
        //we remove the callback
        handler?.removeCallbacks(runnable)
        //and we set the status to offline.
    }
}
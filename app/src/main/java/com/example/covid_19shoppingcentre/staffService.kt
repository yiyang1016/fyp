package com.example.covid_19shoppingcentre

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class staffService : Service() {
    private var Database = FirebaseDatabase.getInstance().getReference()

    private lateinit var mHandler: Handler
    private lateinit var mRunnable: Runnable

    private val channelId = "com.example.covid_19shoppingcentre"

    private var handler: Handler? = null
    private var runnable: Runnable? = null

    override fun onBind(intent: Intent?): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        Toast.makeText(applicationContext, "staff service started", Toast.LENGTH_SHORT).show()

        // Do a periodic task
        mHandler = Handler()
        mRunnable = Runnable {
            checkTime()
        }
        mHandler.postDelayed(mRunnable, 5000)

        return Service.START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(applicationContext, "Kill Running", Toast.LENGTH_SHORT).show()
        handler?.removeCallbacks(runnable)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkTime() {
        mHandler.postDelayed(mRunnable, 5000)
        val currentDateTime = LocalDateTime.now()
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
                        if(s0.child("time").value.toString() == m){
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

}
package com.example.covid_19shoppingcentre

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class checkService : Service() {
    private var Database = FirebaseDatabase.getInstance().getReference()

    lateinit var notificationManager: NotificationManager
    lateinit var builder : NotificationCompat.Builder

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
        val memberId = intent.getStringExtra("MemberId")

        // Do a periodic task
        mHandler = Handler()
        mRunnable = Runnable {
            checkTime()
                notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
                checkNotification(memberId)
        }
        mHandler.postDelayed(mRunnable, 35000)

        return Service.START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(applicationContext, "Kill Running", Toast.LENGTH_SHORT).show()
        handler?.removeCallbacks(runnable)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkTime() {
        mHandler.postDelayed(mRunnable, 35000)
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
    private fun checkNotification(member: String){
        mHandler.postDelayed(mRunnable, 35000)
        val currentDateTime = LocalDateTime.now()
        val hourFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("HH")
        val hourText = currentDateTime.format(hourFormat)
        val hourNow = hourText.toInt()
        val minuteFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("m")
        val minuteText = currentDateTime.format(minuteFormat)
        val minuteNow = minuteText.toInt()
        val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val dateText = currentDateTime.format(dateFormat)

        val query = Database.child("ReservationList").orderByChild("memberId").equalTo(member)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(s0: DataSnapshot) {
                try{
                    for (s0 in s0.children){
                        if (s0.child("status").value.toString() == "active" && s0.child("date").value.toString() == dateText.toString()){
                            if(s0.child("hour").value.toString().toInt() == hourNow && minuteNow == 50){
                                val time = s0.child("time").value.toString()
                                val name = s0.child("storeName").value.toString()
                                notification(time, name)
                            }
                        }
                    }
                } catch(e: Exception){
                    Toast.makeText(applicationContext, "ERROR: $e", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
            }
        })
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun notification(d: String, N: String){
        val alarmSound =
            RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION)

        try{
            val intent = Intent(this, Reservation_List::class.java)
            val pendingIntent =
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            builder = NotificationCompat.Builder(this, channelId)
                .setContentTitle("Reservation")
                .setContentText("You had a Reservation in $N at $d")
                .setSmallIcon(R.drawable.hand_sanitizer)
                .setAutoCancel(true)
                .setPriority(2)
                .setSound(alarmSound)
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setLargeIcon(
                    BitmapFactory.decodeResource(
                        this.resources,
                        R.drawable.hand_sanitizer
                    )
                )
                .setContentIntent(pendingIntent)
            notificationManager.notify(1234, builder.build())
        } catch(e: Exception){
            Toast.makeText(applicationContext, "this is ERROR: $e", Toast.LENGTH_SHORT).show()
        }
    }
}
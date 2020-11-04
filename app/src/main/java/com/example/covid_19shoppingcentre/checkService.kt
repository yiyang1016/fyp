package com.example.covid_19shoppingcentre

import android.app.*
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
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
import kotlin.concurrent.timer

class checkService : Service() {
    private var Database = FirebaseDatabase.getInstance().getReference()

    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder : Notification.Builder

    private lateinit var mHandler: Handler
    private lateinit var mRunnable: Runnable

    private val channelId = "com.e.radiobutton"
    private val description = "Test notification"

    private val EVERY_EIGHT_SECOND: Long = 5000
    private var handler: Handler? = null
    private var runnable: Runnable? = null

    override fun onBind(intent: Intent?): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        //val memberId = intent.getStringExtra("memberId").toString()

        Toast.makeText(applicationContext, "notification testing", Toast.LENGTH_SHORT).show()

        // Do a periodic task
        mHandler = Handler()
        mRunnable = Runnable {
            notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            checkTime()
            checkNotification("M00001")
        }
        mHandler.postDelayed(mRunnable, 20000)

        return Service.START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(applicationContext, "Kill Running", Toast.LENGTH_SHORT).show()
        handler?.removeCallbacks(runnable)
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkTime() {
        mHandler.postDelayed(mRunnable, 20000)
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun notification(d: String, N: String){
        try{
            val intent = Intent(this, Reservation_List::class.java)
            val pendingIntent =
                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

            notificationChannel = NotificationChannel(
                channelId,
                description,
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.enableLights(true)
            notificationChannel.lightColor = Color.GREEN
            notificationChannel.enableVibration(false)
            notificationManager.createNotificationChannel(notificationChannel)

            builder = Notification.Builder(this, channelId)
                .setContentTitle("Reservation")
                .setContentText("You had a Reservation in $N at $d")
                .setSmallIcon(R.drawable.hand_sanitizer)
                .setAutoCancel(true)
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

//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun executeHandler() {
//        //If the handler and runnable are null we create it the first time.
//        if (handler == null && runnable == null) {
//            handler = Handler()
//            runnable = object : Runnable {
//                @RequiresApi(Build.VERSION_CODES.O)
//                override fun run() {
//                    //Updating firebase store/getting
//                    Toast.makeText(applicationContext, "got run run", Toast.LENGTH_SHORT).show()
//                    checkTime()
//                    notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//                    checkNotification(memberId)
//                    //And we execute it again
//                    handler!!.postDelayed(this, EVERY_EIGHT_SECOND)
//                }
//            }
//        } else {
//            handler?.postDelayed(runnable, EVERY_EIGHT_SECOND)
//        }
//    }
}
package com.example.covid_19shoppingcentre

//import com.firebase.ui.database.FirebaseRecyclerOptions
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.reservation_list.*
import kotlinx.android.synthetic.main.reservation_list_layout.view.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class Reservation_List :AppCompatActivity() {

    lateinit var  mRecyclerView: RecyclerView
    lateinit var reserveDatabase : DatabaseReference
    lateinit var FirebaseRecyclerAdapter : FirebaseRecyclerAdapter<Reserve, ReserveViewHolder>
    private var Database = FirebaseDatabase.getInstance().getReference()

    lateinit var notificationManager: NotificationManager
    lateinit var notificationChannel: NotificationChannel
    lateinit var builder : Notification.Builder

    private val channelId = "com.e.radiobutton"
    private val description = "Test notification"

    private val EVERY_EIGHT_SECOND: Long = 35000
    private var handler: Handler? = null
    private var runnable: Runnable? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reservation_list)

        setActionBar()

        mRecyclerView = findViewById(R.id.reservationList)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.setLayoutManager(LinearLayoutManager(this))
        reserveDatabase = FirebaseDatabase.getInstance().getReference("ReservationList")
        logRecyclerView()

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

//        checkTime()
//        checkNotification()
//        executeHandler()
        val left = findViewById<RadioButton>(R.id.current)
        val right = findViewById<RadioButton>(R.id.pass)

        left.setOnClickListener{
            left.setTextColor(Color.BLACK)
            right.setTextColor(Color.WHITE)
            logRecyclerView()
        }
        right.setOnClickListener{
            right.setTextColor(Color.BLACK)
            left.setTextColor(Color.WHITE)
            logRecyclerView1()
        }
    }

    private fun logRecyclerView(){
        val memberI = "M00001"

        FirebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<Reserve, ReserveViewHolder>(
            Reserve::class.java,
            R.layout.reservation_list_layout,
            ReserveViewHolder::class.java,
            reserveDatabase
        ) {
            override fun populateViewHolder(p0: ReserveViewHolder, p1: Reserve, p2: Int) {
                if (p1.memberId.toString() == memberI){
                    if(p1.status.toString() == "active"){
                        p0.mView.reserveTime.setText(p1.time)
                        p0.mView.storeName.setText(p1.storeName)

                        p0.mView.setOnClickListener {
                            val i = Intent(
                                this@Reservation_List,
                                ReserveDetailsActivity::class.java
                            )
                            i.putExtra("name", p1.storeName.toString())
                            i.putExtra("time", p1.time.toString())
                            i.putExtra("date", p1.date.toString())
                            i.putExtra("status", p1.status.toString())
                            startActivity(i)
                        }

                    } else {
                        p0.mView.visibility = View.GONE
                        p0.mView.layoutParams = RecyclerView.LayoutParams(0, 0)
                    }
                } else{
                    p0.mView.visibility = View.GONE
                    p0.mView.layoutParams = RecyclerView.LayoutParams(0, 0)
                }
            }
        }
        mRecyclerView.adapter = FirebaseRecyclerAdapter
    }

    private fun logRecyclerView1(){
        val memberI = "M00001"

        FirebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<Reserve, ReserveViewHolder>(
            Reserve::class.java,
            R.layout.reservation_list_layout,
            ReserveViewHolder::class.java,
            reserveDatabase
        ) {
            override fun populateViewHolder(p0: ReserveViewHolder, p1: Reserve, p2: Int) {
                if (p1.memberId.toString() == memberI){
                    if(p1.status.toString() != "active"){
                        p0.mView.reserveTime.setText(p1.time)
                        p0.mView.storeName.setText(p1.storeName)

                        p0.mView.setOnClickListener {
                            val i = Intent(
                                this@Reservation_List,
                                ReserveDetailsPassActivity::class.java
                            )
                            i.putExtra("name", p1.storeName.toString())
                            i.putExtra("time", p1.time.toString())
                            i.putExtra("date", p1.date.toString())
                            i.putExtra("status", p1.status.toString())
                            startActivity(i)
                        }

                    } else {
                        p0.mView.visibility = View.GONE
                        p0.mView.layoutParams = RecyclerView.LayoutParams(0, 0)
                    }
                } else{
                    p0.mView.visibility = View.GONE
                    p0.mView.layoutParams = RecyclerView.LayoutParams(0, 0)
                }
            }
        }
        mRecyclerView.adapter = FirebaseRecyclerAdapter
    }

    class ReserveViewHolder( var mView: View) : RecyclerView.ViewHolder(mView) {
    }

//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun checkTime() {
//        val currentDateTime = LocalDateTime.now()
//        val hourFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("HH")
//        val hourText = currentDateTime.format(hourFormat)
//        val hourNow = hourText.toInt()
//        val minuteFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("m")
//        val minuteText = currentDateTime.format(minuteFormat)
//        val minuteNow = minuteText.toInt()
//        val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
//        val dateText = currentDateTime.format(dateFormat)
//        val memberI = "M00001"
//
//        if (hourNow >= 10 && minuteNow >= 5) {
//            updateStatus("10:00 AM")
//        }
//        if (hourNow >= 12 && minuteNow >= 5) {
//            updateStatus("12:00 AM")
//        }
//        if (hourNow >= 14 && minuteNow >= 5) {
//            updateStatus("2:00 PM")
//        }
//        if (hourNow >= 16 && minuteNow >= 5) {
//            updateStatus("4:00 PM")
//        }
//        if (hourNow >= 18 && minuteNow >= 5) {
//            updateStatus("6:00 PM")
//        }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun checkNotification(){
//        val currentDateTime = LocalDateTime.now()
//        val hourFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("HH")
//        val hourText = currentDateTime.format(hourFormat)
//        val hourNow = hourText.toInt()
//        val minuteFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("m")
//        val minuteText = currentDateTime.format(minuteFormat)
//        val minuteNow = minuteText.toInt()
//        val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
//        val dateText = currentDateTime.format(dateFormat)
//        val memberI = "M00001"
//
//        val query = Database.child("ReservationList").orderByChild("memberId").equalTo(memberI)
//        query.addListenerForSingleValueEvent(object : ValueEventListener{
//            override fun onDataChange(s0: DataSnapshot) {
//                try{
//                    for (s0 in s0.children){
//                        if (s0.child("status").value.toString() == "active" && s0.child("date").value.toString() == "20201106"){
////                            if(s0.child("hour").value.toString().toInt() == hourNow && minuteNow == 55){
//                            if(s0.child("hour").value.toString() == "0" && minuteNow >= 20){
//                                val time = s0.child("time").value.toString()
//                                val name = s0.child("storeName").value.toString()
//                                notification(time, name)
//                            }
//                        }
//                    }
//                } catch(e: Exception){
//                    Toast.makeText(applicationContext, "ERROR: $e", Toast.LENGTH_SHORT).show()
//                }
//            }
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }

//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun updateStatus(m: String) {
//        val currentDateTime  = LocalDateTime.now()
//        val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
//        val dateText = currentDateTime.format(dateFormat)
//
//        for(x in 1..9){
//            val query = Database.child("Reservation").child(dateText.toString()).child("ST0000$x").child(m).orderByChild("status").equalTo("active")
//            query.addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(s0: DataSnapshot) {
//                    try{
//                        for (s0 in s0.children){
//                            val updateQuery = Database.child("Reservation").child(dateText.toString()).child("ST0000$x").child(m).child(s0.key.toString())
//                            updateQuery.child("status").setValue("cancelled").addOnCompleteListener {
//                            }
//                        }
//                    } catch (e: Exception) {
//                        Toast.makeText(applicationContext, "ERROR: $e", Toast.LENGTH_SHORT).show()
//                    }
//                }
//                override fun onCancelled(error: DatabaseError) {
//                    Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
//                }
//            })
//        }
//
//        for(x in 10..20){
//            val query = Database.child("Reservation").child(dateText.toString()).child("ST000$x").child(m).orderByChild("status").equalTo("active")
//            query.addListenerForSingleValueEvent(object : ValueEventListener {
//                override fun onDataChange(s0: DataSnapshot) {
//                    try{
//                        for (s0 in s0.children){
//                            val updateQuery = Database.child("Reservation").child(dateText.toString()).child("ST000$x").child(m).child(s0.key.toString())
//                            updateQuery.child("status").setValue("cancelled").addOnCompleteListener {
//                            }
//                        }
//                    } catch (e: Exception) {
//                        Toast.makeText(applicationContext, "ERROR$e", Toast.LENGTH_SHORT).show()
//                    }
//                }
//                override fun onCancelled(error: DatabaseError) {
//                    Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
//                }
//            })
//        }
//
//        val anotherQuery = Database.child("ReservationList").orderByChild("date").equalTo(dateText.toString())
//        anotherQuery.addListenerForSingleValueEvent(object : ValueEventListener {
//            override fun onDataChange(s0: DataSnapshot) {
//                try{
//                    for (s0 in s0.children){
//                        if(s0.child("time").value == m){
//                            val updateQuery1 = Database.child("ReservationList").child(s0.key.toString())
//                            updateQuery1.child("status").setValue("cancelled").addOnCompleteListener {
//                            }
//                        }
//                    }
//                } catch (e: Exception) {
//                    Toast.makeText(applicationContext, "ERROR: $e", Toast.LENGTH_SHORT).show()
//                }
//            }
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
//
//    @RequiresApi(Build.VERSION_CODES.O)
//    private fun notification(d: String, N: String){
//        try{
//            val intent = Intent(this, Reservation_List::class.java)
//            val pendingIntent =
//                PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
//
//            notificationChannel = NotificationChannel(
//                channelId,
//                description,
//                NotificationManager.IMPORTANCE_HIGH
//            )
//            notificationChannel.enableLights(true)
//            notificationChannel.lightColor = Color.GREEN
//            notificationChannel.enableVibration(false)
//            notificationManager.createNotificationChannel(notificationChannel)
//
//            builder = Notification.Builder(this, channelId)
//                .setContentTitle("Reservation")
//                .setContentText("You had a Reservation in $N at $d")
//                .setSmallIcon(R.drawable.hand_sanitizer)
//                .setAutoCancel(true)
//                .setLargeIcon(
//                    BitmapFactory.decodeResource(
//                        this.resources,
//                        R.drawable.hand_sanitizer
//                    )
//                )
//                .setContentIntent(pendingIntent)
//            notificationManager.notify(1234, builder.build())
//        } catch(e: Exception){
//            Toast.makeText(applicationContext, "ERROR: $e", Toast.LENGTH_SHORT).show()
//        }
//    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent1 = Intent(this, MainActivity::class.java).apply {
            putExtra("EXTRA_MESSAGE", "message")
        }
        startActivity(intent1)
        return false
    }

    private fun setActionBar(){
        val actionBar: ActionBar? = supportActionBar
        actionBar!!.title = "Reservation List"
        actionBar!!.setDisplayHomeAsUpEnabled(true)
    }

//    private fun executeHandler() {
//        //If the handler and runnable are null we create it the first time.
//        if (handler == null && runnable == null) {
//            handler = Handler()
//            runnable = object : Runnable {
//                @RequiresApi(Build.VERSION_CODES.O)
//                override fun run() {
//                    //Updating firebase store/getting
//                    checkTime()
//                    notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//                    checkNotification()
//                    //And we execute it again
//                    handler!!.postDelayed(this, EVERY_EIGHT_SECOND)
//                }
//            }
//        } else {
//            handler?.postDelayed(runnable, EVERY_EIGHT_SECOND)
//        }
//    }

//    @RequiresApi(Build.VERSION_CODES.O)
//    override fun onResume() {
//        super.onResume()
//        //execute the handler again.
//        executeHandler()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        //we remove the callback
//        //handler?.removeCallbacks(runnable)
//        //and we set the status to offline.
//    }
//
//    override fun onStop() {
//        super.onStop()
//        //we remove the callback
//        executeHandler()
//        //and we set the status to offline.
//    }
}
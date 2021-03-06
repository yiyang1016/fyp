package com.example.covid_19shoppingcentre


import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.list_layout.view.*
import kotlinx.android.synthetic.main.reserve_store_date.storeName
import kotlinx.android.synthetic.main.reserve_store_time.*
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.collections.ArrayList

class ReserveTime : AppCompatActivity() {
    private var Database = FirebaseDatabase.getInstance().getReference()

    // CONSTANT
    private val EVERY_EIGHT_SECOND: Long = 500

    // Run on parallel
    private var handler: Handler? = null
    private var runnable: Runnable? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reserve_store_time)

        val storeN = intent.getStringExtra("storeName")
        val reserveDate = intent.getStringExtra("ReserveDate")
        val pic = intent.getStringExtra("storePic")
        val idd = intent.getStringExtra("memberid2")

        Picasso.with(this@ReserveTime).load(pic).into(imageView)

        storeName.text = storeN

        val chooseTime = listOf("10:00 AM", "12:00 AM", "2:00 PM", "4:00 PM", "6:00 PM")
        timeList.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, chooseTime)

        setActionBar()
        compareTime()
        alreadyBook()
        NoBookingSameTime()
        executeHandler()

        val searchStore = Database.child("Store").orderByChild("Store_Name").equalTo(storeN)
        searchStore.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                try{
                    for (snapshot in snapshot.children){
                        val storeId = snapshot.key.toString()

                        timeList.setOnItemClickListener { parent, view, position, id ->
                            val intent = Intent(this@ReserveTime, ReserveConfirmationActivity::class.java)
                            intent.putExtra("reserveTime", chooseTime[position])
                            intent.putExtra("reserveDate", reserveDate)
                            intent.putExtra("storeName", storeN)
                            intent.putExtra("storeId", storeId)
                            intent.putExtra("StorePic", pic)
                            intent.putExtra("memberid3", idd)
                            startActivity(intent)
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

    private fun alreadyBook() {
        val reserveDate = intent.getStringExtra("ReserveDate")
        val storeI = intent.getStringExtra("storeId")
        val storeN = intent.getStringExtra("storeName")
        val memberI = intent.getStringExtra("memberid2")

        val member = Database.child("Reservation").child(reserveDate).child(storeI).child("10:00 AM").child(memberI)
        val member1 = Database.child("Reservation").child(reserveDate).child(storeI).child("12:00 AM").child(memberI)
        val member2 = Database.child("Reservation").child(reserveDate).child(storeI).child("2:00 PM").child(memberI)
        val member3 = Database.child("Reservation").child(reserveDate).child(storeI).child("4:00 PM").child(memberI)
        val member4 = Database.child("Reservation").child(reserveDate).child(storeI).child("6:00 PM").child(memberI)

        member.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(s0: DataSnapshot) {
                val view = timeList.getChildAt(0)
                val trying = view as TextView
                try {
                    if (s0.exists()) {
                        view.setOnClickListener {
                            Toast.makeText(applicationContext, "You have either booking or already cancelled this time", Toast.LENGTH_SHORT).show() }
                        trying.setTextColor(Color.GRAY)
                        //trying.text = "Hello"
                    } else{
                        limitation()
                    }
                } catch (e: Exception){
                    Toast.makeText(applicationContext, "ERROR$e", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
            }
        })

        member1.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(s0: DataSnapshot) {
                val view1 = timeList.getChildAt(1)
                val trying = view1 as TextView
                try {
                    if (s0.exists()) {
                        view1.setOnClickListener {
                            Toast.makeText(applicationContext, "You have either booking or already cancelled this time", Toast.LENGTH_SHORT).show() }
                        trying.setTextColor(Color.GRAY)
                    } else {
                        limitation1()
                    }
                }catch (e: Exception){
                    Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
            }
        })

        member2.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(s0: DataSnapshot) {
                val view = timeList.getChildAt(2)
                val trying = view as TextView
                try {
                    if (s0.exists()) {
                        view.setOnClickListener {
                            Toast.makeText(applicationContext, "You have either booking or already cancelled this time", Toast.LENGTH_SHORT).show() }
                        trying.setTextColor(Color.GRAY)
                    } else {
                        limitation2()
                    }
                } catch (e: Exception){
                    Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
            }
        })

        member3.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(s0: DataSnapshot) {
                val view = timeList.getChildAt(3)
                val trying = view as TextView
                try {
                    if (s0.exists()) {
                        view.setOnClickListener {
                            Toast.makeText(applicationContext, "You have either booking or already cancelled this time", Toast.LENGTH_SHORT).show() }
                        trying.setTextColor(Color.GRAY)
                    } else {
                        limitation3()
                    }
                } catch (e: Exception){
                    Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
            }
        })

        member4.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(s0: DataSnapshot) {
                val view = timeList.getChildAt(4)
                val trying = view as TextView
                try {
                    if (s0.exists()) {
                        view.setOnClickListener {
                            Toast.makeText(applicationContext, "You have either booking or already cancelled this time", Toast.LENGTH_SHORT).show() }
                        trying.setTextColor(Color.GRAY)
                    } else {
                        limitation4()
                    }
                } catch (e: Exception){
                    Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun limitation() {
        val reserveDate = intent.getStringExtra("ReserveDate")
        val storeI = intent.getStringExtra("storeId")
        val storeN = intent.getStringExtra("storeName")
        val pic = intent.getStringExtra("storePic")
        val memberI = intent.getStringExtra("memberid2")

        val query = Database.child("Reservation").child(reserveDate).child(storeI).child("10:00 AM").orderByChild("status").equalTo("active")
        val storeLimitation = Database.child("Store").orderByChild("Store_Name").equalTo(storeN)

        storeLimitation.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snapshot in snapshot.children) {
                    if (snapshot.exists()) {
                        val limitCount = snapshot.child("Store_Limitation").value.toString()

                        query.addListenerForSingleValueEvent(object : ValueEventListener{
                            override fun onDataChange(s0: DataSnapshot) {
                                val view = timeList.getChildAt(0)
                                val trying = view as TextView
                                try {
                                    if (s0.exists()) {
                                        if (s0.childrenCount >= limitCount.toInt()) {
                                            view.setOnClickListener {
                                                Toast.makeText(applicationContext, "Its already exceed store check in limitation", Toast.LENGTH_SHORT).show()
                                            }
                                            trying.setTextColor(Color.GRAY)
                                        } else {
                                            if (s0.childrenCount >= 5) {
                                                view.setOnClickListener {
                                                    Toast.makeText(
                                                        applicationContext,
                                                        "10:00 AM reservation is full",
                                                        Toast.LENGTH_SHORT
                                                    ).show()
                                                }
                                                trying.setTextColor(Color.GRAY)
                                            } else {
                                                trying.setTextColor(Color.BLACK)
                                                view.setOnClickListener {
                                                    val i = Intent(
                                                        this@ReserveTime,
                                                        ReserveConfirmationActivity::class.java
                                                    )
                                                    i.putExtra("reserveTime", "10:00 AM")
                                                    i.putExtra("reserveDate", reserveDate)
                                                    i.putExtra("storeName", storeN)
                                                    i.putExtra("storeId", storeI)
                                                    i.putExtra("StorePic", pic)
                                                    i.putExtra("memberid3", memberI)
                                                    startActivity(i)
                                                }
                                            }
                                        }
                                    }
                                } catch (e: Exception){
                                    Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun limitation1() {
        val reserveDate = intent.getStringExtra("ReserveDate")
        val storeI = intent.getStringExtra("storeId")
        val storeN = intent.getStringExtra("storeName")
        val pic = intent.getStringExtra("storePic")
        val memberI = intent.getStringExtra("memberid2")

        val query1 = Database.child("Reservation").child(reserveDate).child(storeI).child("12:00 AM").orderByChild("status").equalTo("active")
        val storeLimitation = Database.child("Store").orderByChild("Store_Name").equalTo(storeN)

        storeLimitation.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snapshot in snapshot.children) {
                    if (snapshot.exists()) {
                        val limitCount = snapshot.child("Store_Limitation").value.toString()

                        query1.addListenerForSingleValueEvent(object : ValueEventListener{
                            override fun onDataChange(s0: DataSnapshot) {
                                val view = timeList.getChildAt(1)
                                val trying = view as TextView
                                try {
                                    if (s0.exists()) {
                                        if (s0.childrenCount >= limitCount.toInt()) {
                                            view.setOnClickListener {
                                                Toast.makeText(applicationContext, "Its already exceed store check in limitation", Toast.LENGTH_SHORT).show()
                                            }
                                            trying.setTextColor(Color.GRAY)
                                        } else {
                                            if (s0.childrenCount >= 5){
                                                view.setOnClickListener {
                                                    Toast.makeText(applicationContext, "12:00 AM reservation is full", Toast.LENGTH_SHORT).show()
                                                }
                                                trying.setTextColor(Color.GRAY)
                                            } else{
                                                view.setOnClickListener{
                                                    val i = Intent(
                                                        this@ReserveTime,
                                                        ReserveConfirmationActivity::class.java
                                                    )
                                                    i.putExtra("reserveTime", "12:00 AM")
                                                    i.putExtra("reserveDate", reserveDate)
                                                    i.putExtra("storeName", storeN)
                                                    i.putExtra("storeId", storeI)
                                                    i.putExtra("StorePic", pic)
                                                    i.putExtra("memberid3", memberI)
                                                    startActivity(i)
                                                }
                                            }
                                        }
                                    }
                                } catch (e: Exception){
                                    Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun limitation2() {
        val reserveDate = intent.getStringExtra("ReserveDate")
        val storeI = intent.getStringExtra("storeId")
        val storeN = intent.getStringExtra("storeName")
        val pic = intent.getStringExtra("storePic")
        val memberI = intent.getStringExtra("memberid2")

        val query2 = Database.child("Reservation").child(reserveDate).child(storeI).child("2:00 PM").orderByChild("status").equalTo("active")
        val storeLimitation = Database.child("Store").orderByChild("Store_Name").equalTo(storeN)

        storeLimitation.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snapshot in snapshot.children) {
                    if (snapshot.exists()) {
                        val limitCount = snapshot.child("Store_Limitation").value.toString()

                        query2.addListenerForSingleValueEvent(object : ValueEventListener{
                            override fun onDataChange(s0: DataSnapshot) {
                                val view = timeList.getChildAt(2)
                                val trying = view as TextView
                                try {
                                    if (s0.exists()) {
                                        if (s0.childrenCount >= limitCount.toInt()) {
                                            view.setOnClickListener {
                                                Toast.makeText(applicationContext, "Its already exceed store check in limitation", Toast.LENGTH_SHORT).show()
                                            }
                                            trying.setTextColor(Color.GRAY)
                                        } else {
                                            if (s0.childrenCount >= 5){
                                                view.setOnClickListener {
                                                    Toast.makeText(applicationContext, "2:00 PM reservation is full", Toast.LENGTH_SHORT).show()
                                                }
                                                trying.setTextColor(Color.GRAY)
                                            } else{
                                                trying.setTextColor(Color.BLACK)
                                                view.setOnClickListener{
                                                    val i = Intent(
                                                        this@ReserveTime,
                                                        ReserveConfirmationActivity::class.java
                                                    )
                                                    i.putExtra("reserveTime", "2:00 PM")
                                                    i.putExtra("reserveDate", reserveDate)
                                                    i.putExtra("storeName", storeN)
                                                    i.putExtra("storeId", storeI)
                                                    i.putExtra("StorePic", pic)
                                                    i.putExtra("memberid3", memberI)
                                                    startActivity(i)
                                                }
                                            }
                                        }
                                    }
                                } catch (e: Exception){
                                    Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun limitation3() {
        val reserveDate = intent.getStringExtra("ReserveDate")
        val storeI = intent.getStringExtra("storeId")
        val storeN = intent.getStringExtra("storeName")
        val pic = intent.getStringExtra("storePic")
        val memberI = intent.getStringExtra("memberid2")

        val query3 = Database.child("Reservation").child(reserveDate).child(storeI).child("4:00 PM").orderByChild("status").equalTo("active")
        val storeLimitation = Database.child("Store").orderByChild("Store_Name").equalTo(storeN)

        storeLimitation.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snapshot in snapshot.children) {
                    if (snapshot.exists()) {
                        val limitCount = snapshot.child("Store_Limitation").value.toString()

                        query3.addListenerForSingleValueEvent(object : ValueEventListener{
                            override fun onDataChange(s0: DataSnapshot) {
                                val view = timeList.getChildAt(3)
                                val trying = view as TextView
                                try {
                                    if (s0.exists()) {
                                        if (s0.childrenCount >= limitCount.toInt()) {
                                            view.setOnClickListener {
                                                Toast.makeText(applicationContext, "Its already exceed store check in limitation", Toast.LENGTH_SHORT).show()
                                            }
                                            trying.setTextColor(Color.GRAY)
                                        } else {
                                            if (s0.childrenCount >= 5){
                                                view.setOnClickListener {
                                                    Toast.makeText(applicationContext, "4:00 PM reservation is full", Toast.LENGTH_SHORT).show()
                                                }
                                                trying.setTextColor(Color.GRAY)
                                            } else{
                                                trying.setTextColor(Color.BLACK)
                                                view.setOnClickListener{
                                                    val i = Intent(
                                                        this@ReserveTime,
                                                        ReserveConfirmationActivity::class.java
                                                    )
                                                    i.putExtra("reserveTime", "4:00 PM")
                                                    i.putExtra("reserveDate", reserveDate)
                                                    i.putExtra("storeName", storeN)
                                                    i.putExtra("storeId", storeI)
                                                    i.putExtra("StorePic", pic)
                                                    i.putExtra("memberid3", memberI)
                                                    startActivity(i)
                                                }
                                            }
                                        }
                                    }
                                } catch (e: Exception){
                                    Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun limitation4() {
        val reserveDate = intent.getStringExtra("ReserveDate")
        val storeI = intent.getStringExtra("storeId")
        val storeN = intent.getStringExtra("storeName")
        val pic = intent.getStringExtra("storePic")
        val memberI = intent.getStringExtra("memberid2")

        val query4 = Database.child("Reservation").child(reserveDate).child(storeI).child("6:00 PM").orderByChild("status").equalTo("active")
        val storeLimitation = Database.child("Store").orderByChild("Store_Name").equalTo(storeN)

        storeLimitation.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (snapshot in snapshot.children) {
                    if (snapshot.exists()) {
                        val limitCount = snapshot.child("Store_Limitation").value.toString()

                        query4.addListenerForSingleValueEvent(object : ValueEventListener{
                            override fun onDataChange(s0: DataSnapshot) {
                                val view = timeList.getChildAt(4)
                                val trying = view as TextView
                                try {
                                    if (s0.exists()) {
                                        if (s0.childrenCount >= limitCount.toInt()) {
                                            view.setOnClickListener {
                                                Toast.makeText(applicationContext, "Its already exceed store check in limitation", Toast.LENGTH_SHORT).show()
                                            }
                                            trying.setTextColor(Color.GRAY)
                                        } else {
                                            if (s0.childrenCount >= 5){
                                                view.setOnClickListener {
                                                    Toast.makeText(applicationContext, "6:00 PM reservation is full", Toast.LENGTH_SHORT).show()
                                                }
                                                trying.setTextColor(Color.GRAY)
                                            } else{
                                                trying.setTextColor(Color.BLACK)
                                                view.setOnClickListener{
                                                    val i = Intent(
                                                        this@ReserveTime,
                                                        ReserveConfirmationActivity::class.java
                                                    )
                                                    i.putExtra("reserveTime", "6:00 PM")
                                                    i.putExtra("reserveDate", reserveDate)
                                                    i.putExtra("storeName", storeN)
                                                    i.putExtra("storeId", storeI)
                                                    i.putExtra("StorePic", pic)
                                                    i.putExtra("memberid3", memberI)
                                                    startActivity(i)
                                                }
                                            }
                                        }
                                    }
                                } catch (e: Exception){
                                    Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {
                                Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
                            }
                        })
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun compareTime() {
        val currentDateTime  = LocalDateTime.now()
        val hourMinuteFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("HH")
        val hourMinuteText = currentDateTime.format(hourMinuteFormat)
        val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val dateText = currentDateTime.format(dateFormat)
        val timeNow = hourMinuteText.toInt()

        val reserveDate = intent.getStringExtra("ReserveDate")
        val member = Database.child("Reservation")

        member.addListenerForSingleValueEvent(object : ValueEventListener{
            @SuppressLint("ResourceAsColor")
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()){
                    try{
                        if (reserveDate == dateText.toString()){
                            if (timeNow >= 10){
                                val view = timeList.getChildAt(0)
                                    view.setOnClickListener{
                                        Toast.makeText(applicationContext, "Already Passed 10:00 AM", Toast.LENGTH_SHORT).show()
                                    }
                                val trying = view as TextView
                                trying.setTextColor(Color.GRAY)
                            }
                            if (timeNow >= 12){
                                val view = timeList.getChildAt(1)
                                view.setOnClickListener{
                                    Toast.makeText(applicationContext, "Already Passed 12:00 AM", Toast.LENGTH_SHORT).show()
                                }
                                val trying = view as TextView
                                trying.setTextColor(Color.GRAY)
                            }
                            if (timeNow >= 14){
                                val view = timeList.getChildAt(2)
                                view.setOnClickListener{
                                    Toast.makeText(applicationContext, "Already Passed 2:00 PM", Toast.LENGTH_SHORT).show()
                                }
                                val trying = view as TextView
                                trying.setTextColor(Color.GRAY)
                            }
                            if (timeNow >= 16){
                                val view = timeList.getChildAt(3)
                                view.setOnClickListener{
                                    Toast.makeText(applicationContext, "Already Passed 4:00 PM", Toast.LENGTH_SHORT).show()
                                }
                                val trying = view as TextView
                                trying.setTextColor(Color.GRAY)
                            }
                            if (timeNow >= 18){
                                val view = timeList.getChildAt(4)
                                view.setOnClickListener{
                                    Toast.makeText(applicationContext, "Already Passed 6:00 PM", Toast.LENGTH_SHORT).show()
                                }
                                val trying = view as TextView
                                trying.setTextColor(Color.GRAY)
                            }
                        }
                    } catch (e: Exception){
                        Toast.makeText(applicationContext, "Error$e", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun NoBookingSameTime(){
        val reserveDate = intent.getStringExtra("ReserveDate")
        val memberI = intent.getStringExtra("memberid2")

        val query = Database.child("ReservationList").orderByChild("memberId").equalTo(memberI)
        query.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(s0: DataSnapshot) {
                for (s0 in s0.children){
                    if (s0.exists()){
                        if(s0.child("status").value.toString() == "active") {
                            val date = s0.child("date").value.toString()
                            val time = s0.child("time").value.toString()
                            val name = s0.child("storeName").value.toString()

                            if (reserveDate == date){
                                if(time == "10:00 AM"){
                                    try{
                                        val view = timeList.getChildAt(0)
                                        view.setOnClickListener{
                                            Toast.makeText(applicationContext,
                                                "Already booked at this time on this day in $name", Toast.LENGTH_SHORT).show()
                                        }
                                        val trying = view as TextView
                                        trying.setTextColor(Color.GRAY)
                                    } catch (e: Exception){
                                    }
                                }
                                if(time == "12:00 AM"){
                                    try{
                                        val view = timeList.getChildAt(1)
                                        view.setOnClickListener{
                                            Toast.makeText(applicationContext,
                                                "Already booked at this time on this day in $name", Toast.LENGTH_SHORT).show()
                                        }
                                        val trying = view as TextView
                                        trying.setTextColor(Color.GRAY)
                                    } catch (e: Exception){
                                    }
                                }
                                if(time == "2:00 PM"){
                                    try{
                                        val view = timeList.getChildAt(2)
                                        view.setOnClickListener{
                                            Toast.makeText(applicationContext,
                                                "Already booked at this time on this day in $name", Toast.LENGTH_SHORT).show()
                                        }
                                        val trying = view as TextView
                                        trying.setTextColor(Color.GRAY)
                                    } catch (e: Exception){
                                    }
                                }
                                if(time == "4:00 PM"){
                                    try{
                                        val view = timeList.getChildAt(3)
                                        view.setOnClickListener{
                                            Toast.makeText(applicationContext,
                                                "Already booked at this time on this day in $name", Toast.LENGTH_SHORT).show()
                                        }
                                        val trying = view as TextView
                                        trying.setTextColor(Color.GRAY)
                                    } catch (e: Exception){
                                    }
                                }
                                if(time == "6:00 PM"){
                                    try{
                                        val view = timeList.getChildAt(4)
                                        view.setOnClickListener{
                                            Toast.makeText(applicationContext,
                                                "Already booked at this time on this day in $name", Toast.LENGTH_SHORT).show()
                                        }
                                        val trying = view as TextView
                                        trying.setTextColor(Color.GRAY)
                                    } catch (e: Exception){
                                    }
                                }
                            }
                        }
                    }
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
                    compareTime()
                    alreadyBook()
                    NoBookingSameTime()
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val pic = intent.getStringExtra("storePic")
        val storeN = intent.getStringExtra("storeName")
        val idd = intent.getStringExtra("memberid2")

        val intent1 = Intent(this, ReserveDate::class.java).apply {
            putExtra("StorePic", pic)
            putExtra("StoreName", storeN)
            putExtra("memberid1", idd)
        }
        startActivity(intent1)
        return false
    }

    private fun setActionBar(){
        val actionBar: ActionBar? = supportActionBar
        actionBar!!.title = "Reserve Time"
        actionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onBackPressed() {
        val pic = intent.getStringExtra("storePic")
        val storeN = intent.getStringExtra("storeName")
        val idd = intent.getStringExtra("memberid2")

        val intent1 = Intent(this, ReserveDate::class.java).apply {
            putExtra("StorePic", pic)
            putExtra("StoreName", storeN)
            putExtra("memberid1", idd)
        }
        startActivity(intent1)
    }

}
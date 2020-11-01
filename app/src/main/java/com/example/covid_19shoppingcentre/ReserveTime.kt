package com.example.covid_19shoppingcentre

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.reserve_store_date.storeName
import kotlinx.android.synthetic.main.reserve_store_time.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


class ReserveTime : AppCompatActivity() {
    private var Database = FirebaseDatabase.getInstance().getReference()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reserve_store_time)

        val storeN = intent.getStringExtra("storeName")
        val reserveDate = intent.getStringExtra("ReserveDate")
        val storeI = intent.getStringExtra("storeId")

        storeName.text = storeN

        val chooseTime = listOf("10:00 AM", "12:00 AM", "2:00 PM", "4:00 PM", "6:00 PM")

        timeList.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, chooseTime)

        compareTime()
        limitation()
        alreadyBook()

        timeList.setOnItemClickListener { parent, view, position, id ->
            val i = Intent(
                this@ReserveTime,
                reserveConfirmationActivity::class.java
            )
            i.putExtra("reserveTime", chooseTime[position])
            i.putExtra("reserveDate", reserveDate)
            i.putExtra("storeName", storeN)
            i.putExtra("storeId", storeI)
            startActivity(i)
        }
    }

    private fun limitation() {
        val reserveDate = intent.getStringExtra("ReserveDate")
        val storeI = intent.getStringExtra("storeId")

        val query = Database.child("Reservation").child(reserveDate).child(storeI).child("10:00 AM").orderByChild("status").equalTo("active")
        val query1 = Database.child("Reservation").child(reserveDate).child(storeI).child("12:00 AM").orderByChild("status").equalTo("active")
        val query2 = Database.child("Reservation").child(reserveDate).child(storeI).child("2:00 PM").orderByChild("status").equalTo("active")
        val query3 = Database.child("Reservation").child(reserveDate).child(storeI).child("4:00 PM").orderByChild("status").equalTo("active")
        val query4 = Database.child("Reservation").child(reserveDate).child(storeI).child("6:00 PM").orderByChild("status").equalTo("active")
        //val query5 = Database.child("Reservation").child(reserveDate).child(storeI).child("8:00 PM").orderByChild("status").equalTo("active")

        query.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(s0: DataSnapshot) {
                try {
                    if (s0.exists()) {
                        if (s0.childrenCount >= 5){
                            val view = timeList.getChildAt(0)
                            view.setOnClickListener {
                                Toast.makeText(applicationContext, "10:00 AM reservation is full", Toast.LENGTH_SHORT).show() }
                            val trying = view as TextView
                            trying.setTextColor(Color.GRAY)
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

        query1.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(s0: DataSnapshot) {
                try {
                    if (s0.exists()) {
                        if (s0.childrenCount >= 5){
                            val view = timeList.getChildAt(1)
                            view.setOnClickListener {
                                Toast.makeText(applicationContext, "12:00 AM reservation is full", Toast.LENGTH_SHORT).show()
                            }
                            val trying = view as TextView
                            trying.setTextColor(Color.GRAY)
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

        query2.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(s0: DataSnapshot) {
                try {
                    if (s0.exists()) {
                        if (s0.childrenCount >= 5){
                            val view = timeList.getChildAt(2)
                            view.setOnClickListener {
                                Toast.makeText(applicationContext, "2:00 PM reservation is full", Toast.LENGTH_SHORT).show()
                            }
                            val trying = view as TextView
                            trying.setTextColor(Color.GRAY)
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

        query3.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(s0: DataSnapshot) {
                try {
                    if (s0.exists()) {
                        if (s0.childrenCount >= 5){
                            val view = timeList.getChildAt(3)
                            view.setOnClickListener {
                                Toast.makeText(applicationContext, "4:00 PM reservation is full", Toast.LENGTH_SHORT).show()
                            }
                            val trying = view as TextView
                            trying.setTextColor(Color.GRAY)
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

        query4.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(s0: DataSnapshot) {
                try {
                    if (s0.exists()) {
                        if (s0.childrenCount >= 5){
                            val view = timeList.getChildAt(4)
                            view.setOnClickListener {
                                Toast.makeText(applicationContext, "6:00 PM reservation is full", Toast.LENGTH_SHORT).show()
                            }
                            val trying = view as TextView
                            trying.setTextColor(Color.GRAY)
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

//        query5.addListenerForSingleValueEvent(object : ValueEventListener{
//            override fun onDataChange(s0: DataSnapshot) {
//                try {
//                    if (s0.exists()) {
//                        if (s0.childrenCount >= 5){
//                            val view = timeList.getChildAt(5)
//                            view.setOnClickListener {
//                                Toast.makeText(applicationContext, "8:00 PM reservation is full", Toast.LENGTH_SHORT).show()
//                            }
//                            //view.isEnabled = false
//                        }
//                    }
//                } catch (e: Exception){
//                    Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
//                }
//            }
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
//            }
//        })
    }

    private fun alreadyBook() {
        val reserveDate = intent.getStringExtra("ReserveDate")
        val storeI = intent.getStringExtra("storeId")
        val memberI = "M00001"

        val member = Database.child("Reservation").child(reserveDate).child(storeI).child("10:00 AM").child(memberI)
        val member1 = Database.child("Reservation").child(reserveDate).child(storeI).child("12:00 AM").child(memberI)
        val member2 = Database.child("Reservation").child(reserveDate).child(storeI).child("2:00 PM").child(memberI)
        val member3 = Database.child("Reservation").child(reserveDate).child(storeI).child("4:00 PM").child(memberI)
        val member4 = Database.child("Reservation").child(reserveDate).child(storeI).child("6:00 PM").child(memberI)
        //val member5 = Database.child("Reservation").child(reserveDate).child(storeI).child("8:00 PM").child(memberI)

        member.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(s0: DataSnapshot) {
                try {
                    if (s0.exists()) {
                        val view = timeList.getChildAt(0)
                        view.setOnClickListener {
                            Toast.makeText(applicationContext, "Already booked this time", Toast.LENGTH_SHORT).show() }
                        val trying = view as TextView
                        trying.setTextColor(Color.GRAY)
                        //trying.text = "Hello"

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
                try {
                    if (s0.exists()) {
                        val view = timeList.getChildAt(1)
                        view.setOnClickListener {
                            Toast.makeText(applicationContext, "Already booked this time", Toast.LENGTH_SHORT).show() }
                        val trying = view as TextView
                        trying.setTextColor(Color.GRAY)
                    }
                } catch (e: Exception){
                    Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
            }
        })

        member2.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(s0: DataSnapshot) {
                try {
                    if (s0.exists()) {
                        val view = timeList.getChildAt(2)
                        view.setOnClickListener {
                            Toast.makeText(applicationContext, "Already booked this time", Toast.LENGTH_SHORT).show() }
                        val trying = view as TextView
                        trying.setTextColor(Color.GRAY)
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
                try {
                    if (s0.exists()) {
                        val view = timeList.getChildAt(3)
                        view.setOnClickListener {
                            Toast.makeText(applicationContext, "Already booked this time", Toast.LENGTH_SHORT).show() }
                        val trying = view as TextView
                        trying.setTextColor(Color.GRAY)
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
                try {
                    if (s0.exists()) {
                        val view = timeList.getChildAt(4)
                        view.setOnClickListener {
                            Toast.makeText(applicationContext, "Already booked this time", Toast.LENGTH_SHORT).show() }
                        val trying = view as TextView
                        trying.setTextColor(Color.GRAY)
                    }
                } catch (e: Exception){
                    Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
            }
        })

//        member5.addListenerForSingleValueEvent(object : ValueEventListener{
//            override fun onDataChange(s0: DataSnapshot) {
//                try {
//                    if (s0.exists()) {
//                        val view = timeList.getChildAt(5)
//                        //view.isEnabled = false
//                        view.setOnClickListener {
//                            Toast.makeText(applicationContext, "Already booked this time", Toast.LENGTH_SHORT).show() }
//
//                    }
//                } catch (e: Exception){
//                    Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
//                }
//            }
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
//            }
//        })
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
                            if (timeNow > 10){
                                val view = timeList.getChildAt(0)
                                    view.setOnClickListener{
                                        Toast.makeText(applicationContext, "Already Passed 10:00 AM", Toast.LENGTH_SHORT).show()
                                    }
                                val trying = view as TextView
                                trying.setTextColor(Color.GRAY)
                            }
                            if (timeNow > 12){
                                val view = timeList.getChildAt(1)
                                view.setOnClickListener{
                                    Toast.makeText(applicationContext, "Already Passed 12:00 AM", Toast.LENGTH_SHORT).show()
                                }
                                val trying = view as TextView
                                trying.setTextColor(Color.GRAY)
                            }
                            if (timeNow > 14){
                                val view = timeList.getChildAt(2)
                                view.setOnClickListener{
                                    Toast.makeText(applicationContext, "Already Passed 2:00 PM", Toast.LENGTH_SHORT).show()
                                }
                                val trying = view as TextView
                                trying.setTextColor(Color.GRAY)
                            }
                            if (timeNow > 16){
                                val view = timeList.getChildAt(3)
                                view.setOnClickListener{
                                    Toast.makeText(applicationContext, "Already Passed 4:00 PM", Toast.LENGTH_SHORT).show()
                                }
                                val trying = view as TextView
                                trying.setTextColor(Color.GRAY)
                            }
                            if (timeNow > 18){
                                val view = timeList.getChildAt(4)
                                view.setOnClickListener{
                                    Toast.makeText(applicationContext, "Already Passed 6:00 PM", Toast.LENGTH_SHORT).show()
                                }
                                val trying = view as TextView
                                trying.setTextColor(Color.GRAY)
                            }
//                            if (timeNow > 20){
//                                val view = timeList.getChildAt(5)
//                                view.setOnClickListener{
//                                    Toast.makeText(applicationContext, "Already Passed 8:00 PM", Toast.LENGTH_SHORT).show()
//                                }
//                                //view.isEnabled = false
//                            }
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

}
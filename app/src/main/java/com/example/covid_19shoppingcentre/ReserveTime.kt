package com.example.covid_19shoppingcentre

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.reserve_store_date.*
import kotlinx.android.synthetic.main.reserve_store_date.storeName
import kotlinx.android.synthetic.main.reserve_store_time.*
import java.lang.Exception
import java.util.*

class ReserveTime : AppCompatActivity() {
    private var Database = FirebaseDatabase.getInstance().getReference()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reserve_store_time)

        val storeN = intent.getStringExtra("storeName")
        val reserveDate = intent.getStringExtra("ReserveDate")
        val storeI = intent.getStringExtra("storeId")

        storeName.text = storeN

        val chooseTime = listOf("10:00 AM", "12:00 AM", "2:00 PM", "4:00 PM", "6:00 PM", "8:00 PM")

        timeList.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, chooseTime)

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

        val query = Database.child("Reservation").child(reserveDate).child(storeI).child("10:00 AM").orderByChild("status").equalTo("active")
        query.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(s0: DataSnapshot) {
                try {
                    if (s0.exists()) {
                        if (s0.childrenCount >= 2){
                            timeText.text = "full"
                            val view = timeList.getChildAt(0)
                            view.setOnClickListener { }
                            view.isEnabled = false
                        } else{
                            timeText.text = "not full"
                        }
                    } else{
                        timeText.text = "Nothing"
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
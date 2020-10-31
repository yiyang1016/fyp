package com.example.covid_19shoppingcentre

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.reserve_store_confirmation.*
import java.text.SimpleDateFormat
import java.util.*

class reserveConfirmationActivity : AppCompatActivity() {
    private var Database = FirebaseDatabase.getInstance().getReference()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reserve_store_confirmation)

        val storeN = intent.getStringExtra("storeName")
        val reserveDate = intent.getStringExtra("reserveDate")
        val storeI = intent.getStringExtra("storeId")
        val reserveTime = intent.getStringExtra("reserveTime")

        val simpleDateFormat = SimpleDateFormat("yyyyMMdd")
        val displayDateFormat = SimpleDateFormat("dd/MM/yyyy")

        val parseDate: Date = simpleDateFormat.parse(reserveDate)
        val displayDate = displayDateFormat.format(parseDate)

        storeName.text = storeN
        time.text = reserveTime
        date.text = displayDate.toString()

        reserveBtn.setOnClickListener{
            reserve()
        }

        val query = Database.child("Store").child(storeI)
        query.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(s0: DataSnapshot) {
                if(s0.exists()){
                    floor.text = (s0.child("Store_Floor").value.toString())
                    slot.text = (s0.child("Store_Slot").value.toString())
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun reserve(){

    }
}
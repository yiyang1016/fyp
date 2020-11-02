package com.example.covid_19shoppingcentre

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.covid_19shoppingcentre.models.addReservation
import com.example.covid_19shoppingcentre.models.addReservationList
import com.example.covid_19shoppingcentre.models.addShoppingCentreCheckIn
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
            reserveList()
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
        val reserveDate = intent.getStringExtra("reserveDate")
        val storeI = intent.getStringExtra("storeId")
        val reserveTime = intent.getStringExtra("reserveTime")

        val statusNow = "active"
        val memberI = "M00001"

        val intent1 = Intent(this, Reservation_List::class.java).apply {
            putExtra("whatMessage", "message")
        }

        val query = Database.child("Reservation")

        if(storeI != null && reserveTime != null){
            val writeNewCheckIn = addReservation(statusNow)

            query.child(reserveDate).child(storeI).child(reserveTime).child(memberI).setValue(writeNewCheckIn).addOnCompleteListener {
                Toast.makeText(
                    applicationContext,
                    "Reserve Successful",
                    Toast.LENGTH_SHORT
                ).show()
//                intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent1)
                finish()
            }
        }
    }

    private fun reserveList() {
        val storeN = intent.getStringExtra("storeName")
        val reserveDate = intent.getStringExtra("reserveDate")
        val reserveTime = intent.getStringExtra("reserveTime")

        val statusNow = "active"
        val memberI = "M00001"

        val query = Database.child("ReservationList")
        val writeNewReserveList = addReservationList(reserveDate, memberI, statusNow, storeN, reserveTime)

        query.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    query.child(snapshot.childrenCount.toString()).setValue(writeNewReserveList)
                        .addOnCompleteListener {
                            //Toast.makeText(applicationContext, "ERROR" + snapshot.childrenCount, Toast.LENGTH_SHORT).show()
                            reserve()
                        }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
            }

        })
        }
    }
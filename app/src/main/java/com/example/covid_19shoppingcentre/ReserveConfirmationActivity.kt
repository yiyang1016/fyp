package com.example.covid_19shoppingcentre

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.example.covid_19shoppingcentre.models.addReservation
import com.example.covid_19shoppingcentre.models.addReservationList
import com.example.covid_19shoppingcentre.models.addShoppingCentreCheckIn
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.reserve_store_confirmation.*
import kotlinx.android.synthetic.main.reserve_store_confirmation.imageView
import kotlinx.android.synthetic.main.reserve_store_confirmation.storeName
import kotlinx.android.synthetic.main.reserve_store_time.*
import java.text.SimpleDateFormat
import java.util.*

class ReserveConfirmationActivity : AppCompatActivity() {
    private var Database = FirebaseDatabase.getInstance().getReference()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reserve_store_confirmation)

        val storeN = intent.getStringExtra("storeName")
        val reserveDate = intent.getStringExtra("reserveDate")
        val storeI = intent.getStringExtra("storeId")
        val reserveTime = intent.getStringExtra("reserveTime")
        val pic = intent.getStringExtra("StorePic")

        val simpleDateFormat = SimpleDateFormat("yyyyMMdd")
        val displayDateFormat = SimpleDateFormat("dd/MM/yyyy")

        val parseDate: Date = simpleDateFormat.parse(reserveDate)
        val displayDate = displayDateFormat.format(parseDate)

        Picasso.with(this@ReserveConfirmationActivity).load(pic).into(imageView)
        storeName.text = storeN
        time.text = reserveTime
        date.text = displayDate.toString()
        setActionBar()

        reserveBtn.setOnClickListener {
            reserveList()
        }

        val query = Database.child("Store").child(storeI)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(s0: DataSnapshot) {
                if (s0.exists()) {
                    floor.text = (s0.child("Store_Floor").value.toString())
                    slot.text = (s0.child("Store_Slot").value.toString())
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun reserve() {
        val reserveDate = intent.getStringExtra("reserveDate")
        val storeI = intent.getStringExtra("storeId")
        val reserveTime = intent.getStringExtra("reserveTime")

        val statusNow = "active"
        val memberI = "M00001"

        val intent1 = Intent(this, Reservation_List::class.java).apply {
            putExtra("whatMessage", "message")
        }

        val query = Database.child("Reservation")

        if (storeI != null && reserveTime != null) {
            val writeNewCheckIn = addReservation(statusNow)

            query.child(reserveDate).child(storeI).child(reserveTime).child(memberI)
                .setValue(writeNewCheckIn).addOnCompleteListener {
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

        val query = Database.child("ReservationList")

        val statusNow = "active"
        val memberI = "M00001"
        val needTime: String

        if (reserveTime.toString() == "10:00 AM") {
            needTime = "9"
            val writeNewReserveList =
                addReservationList(reserveDate, needTime, memberI, statusNow, storeN, reserveTime)

            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        query.child(snapshot.childrenCount.toString()).setValue(writeNewReserveList)
                            .addOnCompleteListener {
                                reserve()
                            }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
                }
            })
        } else if (reserveTime.toString() == "12:00 AM") {
            needTime = "11"
            val writeNewReserveList =
                addReservationList(reserveDate, needTime, memberI, statusNow, storeN, reserveTime)

            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        query.child(snapshot.childrenCount.toString()).setValue(writeNewReserveList)
                            .addOnCompleteListener {
                                reserve()
                            }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
                }
            })
        } else if (reserveTime.toString() == "2:00 PM") {
            needTime = "13"
            val writeNewReserveList =
                addReservationList(reserveDate, needTime, memberI, statusNow, storeN, reserveTime)

            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        query.child(snapshot.childrenCount.toString()).setValue(writeNewReserveList)
                            .addOnCompleteListener {
                                reserve()
                            }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
                }
            })
        } else if (reserveTime.toString() == "4:00 PM") {
            needTime = "15"
            val writeNewReserveList =
                addReservationList(reserveDate, needTime, memberI, statusNow, storeN, reserveTime)

            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        query.child(snapshot.childrenCount.toString()).setValue(writeNewReserveList)
                            .addOnCompleteListener {
                                reserve()
                            }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
                }
            })
        } else if (reserveTime.toString() == "6:00 PM") {
            needTime = "17"
            val writeNewReserveList =
                addReservationList(reserveDate, needTime, memberI, statusNow, storeN, reserveTime)

            query.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()) {
                        query.child(snapshot.childrenCount.toString()).setValue(writeNewReserveList)
                            .addOnCompleteListener {
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val pic = intent.getStringExtra("StorePic")
        val storeN = intent.getStringExtra("storeName")
        val reserveD = intent.getStringExtra("reserveDate")
        val storeI = intent.getStringExtra("storeId")

        val intent1 = Intent(this, ReserveTime::class.java).apply {
            putExtra("storeName", storeN)
            putExtra("storePic", pic)
            putExtra("storeId", storeI)
            putExtra("ReserveDate", reserveD)
        }
        startActivity(intent1)
        return false
    }

    private fun setActionBar() {
        val actionBar: ActionBar? = supportActionBar
        actionBar!!.title = "Reserve Confirmation"
        actionBar!!.setDisplayHomeAsUpEnabled(true)
    }
}
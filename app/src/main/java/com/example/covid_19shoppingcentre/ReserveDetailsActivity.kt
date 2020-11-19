package com.example.covid_19shoppingcentre

import android.content.Intent
import android.media.Image
import android.os.Bundle
import android.provider.ContactsContract
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.reserve_store_confirmation.*
import kotlinx.android.synthetic.main.reserve_store_date.*
import kotlinx.android.synthetic.main.reserve_store_details.*
import kotlinx.android.synthetic.main.reserve_store_details.date
import kotlinx.android.synthetic.main.reserve_store_details.status
import kotlinx.android.synthetic.main.reserve_store_details.slot
import kotlinx.android.synthetic.main.reserve_store_details.storeName
import kotlinx.android.synthetic.main.reserve_store_details.time
import java.text.SimpleDateFormat
import java.util.*

class ReserveDetailsActivity : AppCompatActivity() {
    private var Database = FirebaseDatabase.getInstance().getReference()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reserve_store_details)

        setActionBar()

        val storeN = intent.getStringExtra("name")
        val reserveT = intent.getStringExtra("time")
        val reserveD = intent.getStringExtra("date")
        val statu = intent.getStringExtra("status")

        storeName.text = storeN
        time.text = reserveT
        status.text = statu

        val simpleDateFormat = SimpleDateFormat("yyyyMMdd")
        val displayDateFormat = SimpleDateFormat("dd/MM/yyyy")

        val parseDate: Date = simpleDateFormat.parse(reserveD)
        val displayDate = displayDateFormat.format(parseDate)

        date.text = displayDate.toString()

        val query = Database.child("Store").orderByChild("Store_Name").equalTo(storeN)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(s0: DataSnapshot) {
                for (s0 in s0.children) {
                    slot.text = (s0.child("Store_Slot").value.toString())
                    val pic = (s0.child("Store_Image").value.toString())
                    Picasso.with(this@ReserveDetailsActivity).load(pic).into(storePicture)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
            }
        })


        cancelReserveBtn.setOnClickListener{
            cancelList()
        }
    }

    private fun cancelReservation() {
        val memberI = "M00001"
        val storeN = intent.getStringExtra("name")
        val reserveT = intent.getStringExtra("time")
        val reserveD = intent.getStringExtra("date")

        val intent1 = Intent(this, Reservation_List::class.java).apply {
            putExtra("nothing", "message")
        }

        val getId = Database.child("Store").orderByChild("Store_Name").equalTo(storeN)
        getId.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(snapshot in snapshot.children){
                    val update = Database.child("Reservation").child(reserveD).child(snapshot.key.toString()).child(reserveT).child(memberI)
                    update.child("status").setValue("cancelled").addOnCompleteListener {
                        Toast.makeText(
                            applicationContext,
                            "Reserve Cancelled Successful",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(intent1)
                        finish()
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun cancelList() {
        val memberI = "M00001"
        val storeN = intent.getStringExtra("name")
        val reserveT = intent.getStringExtra("time")
        val reserveD = intent.getStringExtra("date")

        val update = Database.child("ReservationList").orderByChild("memberId").equalTo(memberI)
        update.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(s0: DataSnapshot) {
                for(s0 in s0.children){
                    if(s0.child("date").value == reserveD && s0.child("time").value == reserveT && s0.child("storeName").value == storeN) {
                                val updateQuery = Database.child("ReservationList").child(s0.key.toString())
                                updateQuery.child("status").setValue("cancelled").addOnCompleteListener {
                                    cancelReservation()
                                }
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent1 = Intent(this, Reservation_List::class.java).apply {
            putExtra("EXTRA_MESSAGE", "message")
        }
        startActivity(intent1)
        return false
    }

    private fun setActionBar(){
        val actionBar: ActionBar? = supportActionBar
        actionBar!!.title = "Reservation Details"
        actionBar!!.setDisplayHomeAsUpEnabled(true)
    }
}


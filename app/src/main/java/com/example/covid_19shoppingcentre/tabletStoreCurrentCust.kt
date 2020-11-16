package com.example.covid_19shoppingcentre

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.firebase.ui.database.FirebaseRecyclerAdapter
import kotlinx.android.synthetic.main.question_mobile.*
import android.widget.Button
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.list_layout.view.*
import kotlinx.android.synthetic.main.tablet_set_limitation.*
import kotlinx.android.synthetic.main.tablet_store_current_cust.*
import org.threeten.bp.format.DateTimeFormatter.ofPattern
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlinx.android.synthetic.main.tablet_set_limitation.tvCurrentCust as tvCurrentCust1

class tabletStoreCurrentCust : AppCompatActivity() {

    private var Database = FirebaseDatabase.getInstance().getReference()
    lateinit var nDatabase: DatabaseReference
    lateinit var FirebaseRecyclerAdapter: FirebaseRecyclerAdapter<Store, Store_List.StoreViewHolder>

    lateinit var storeID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tablet_store_current_cust)

        storeID = "ST00001"

        var customerCountInt = 0
        val currentDateTime = LocalDateTime.now()
        val hourFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("H")
        val hourText = currentDateTime.format(hourFormat)
        val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val dateText = currentDateTime.format(dateFormat)
        val query = Database.child("CheckInStore").child(dateText.toString()).child(storeID).child(hourText.toString())
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snap: DataSnapshot) {
                for (snap in snap.children) {
                    if (snap.child("status").value.toString() == "active") {
                        customerCountInt++
                    }
                }
                tvCurrentCust.setText(customerCountInt.toString())
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
            }
        })

        nDatabase = FirebaseDatabase.getInstance().getReference("Store")

        val ref = nDatabase.child(storeID)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    if(!p0.child("Store_Limitation").value.toString().isNullOrEmpty()){
                        tvLimitationCust.setText(p0.child("Store_Limitation").value.toString())
                    }else{
                        tvLimitationCust.setText("0")
                    }
                } else {
                    Toast.makeText(applicationContext, "Error: Can Not Access Data", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "00", Toast.LENGTH_SHORT).show()
            }
        })

        btnSetLimit.setOnClickListener{


            val intent = Intent(
                this@tabletStoreCurrentCust,
                tabletSetLimitation::class.java
            )
            startActivity(intent)
        }
    }
}
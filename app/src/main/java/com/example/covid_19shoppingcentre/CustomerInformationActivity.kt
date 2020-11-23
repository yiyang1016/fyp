package com.example.covid_19shoppingcentre

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.sc_customer_check_out_information.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CustomerInformationActivity : AppCompatActivity() {

    private var userDatabase = FirebaseDatabase.getInstance().getReference()
    private  var database = FirebaseDatabase.getInstance().getReference("Member")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sc_customer_check_out_information)

        val name = intent.getStringExtra("name")
        val phone = intent.getStringExtra("phone")
        val temperature = intent.getStringExtra("temperature")
        val date = intent.getStringExtra("date")
        val time = intent.getStringExtra("time")

        customerName.text = name
        customerBodyTemperature.text = temperature
        customerPhone.text = phone
        CheckInDate.text = date
        CheckInTime.text = time

        setActionBar()

        val checkOutButton = findViewById<Button>(R.id.checkOutBtn)

        checkOutButton.setOnClickListener {
            checkOutCustomer()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkOutCustomer() {
        val currentDateTime  = LocalDateTime.now()
        val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val dateText = currentDateTime.format(dateFormat)
        val hourMinuteFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a")
        val hourMinuteText = currentDateTime.format(hourMinuteFormat)

        val id = intent.getStringExtra("id")

        val intent = Intent(this, CheckInScCustomer_List::class.java).apply {
            putExtra("EXTRA_MESSAGE", "message")
        }

        val query = userDatabase.child("ShoppingCentre").child(dateText.toString()).orderByChild("customerId").equalTo(id)
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(s0: DataSnapshot) {
                for (s0 in s0.children) {
                    val statusNow = "checkOut"
                    val checkOutTime = hourMinuteText.toString().trim()

                    val updateQuery = FirebaseDatabase.getInstance().getReference("ShoppingCentre").child(dateText.toString())
                    val customerID = s0.key.toString()

                    updateQuery.child(customerID).child("status").setValue(statusNow)
                    updateQuery.child(customerID).child("checkOutTime").setValue(checkOutTime).addOnCompleteListener {
                        val refSearch = FirebaseDatabase.getInstance().getReference().child("Member")
                            .orderByChild("Id").equalTo(id)
                        refSearch.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(error: DatabaseError) {
                                val text = "Connection Failed"
                                Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                if (p0.exists()) {
                                    for (p0 in p0.children) {
                                        database.child(id).child("DistanceScoreStatus").setValue(0)
                                    }
                                } else {
                                    Toast.makeText(applicationContext, "Member Missing", Toast.LENGTH_SHORT)
                                        .show()
                                }
                            }
                        })
                        Toast.makeText(
                            applicationContext,
                            "Check Out Successful",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(intent)
                        finish()
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent1 = Intent(this, CheckInScCustomer_List::class.java).apply {
            putExtra("EXTRA_MESSAGE", "message")
        }
        startActivity(intent1)
        return false
    }

    private fun setActionBar(){
        val actionBar: ActionBar? = supportActionBar
        actionBar!!.title = "Customer Information"
        actionBar!!.setDisplayHomeAsUpEnabled(true)
    }
}
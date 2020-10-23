package com.example.covid_19shoppingcentre

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class StaffMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.staff_main)

        val SC_Customer_CheckIn = findViewById<Button>(R.id.SCCheckInBtn)
        val SC_CheckedIn_Customer = findViewById<Button>(R.id.SCCheckInCustomerBtn)

        SC_Customer_CheckIn.setOnClickListener{
            val intent = Intent(this, QRScannerActivity::class.java).apply {
                putExtra("EXTRA_MESSAGE", "message")
            }
            startActivity(intent)
        }

        SC_CheckedIn_Customer.setOnClickListener{
            val intent = Intent(this, CheckInScCustomer_List::class.java).apply {
                putExtra("EXTRA_MESSAGE", "message")
            }
            startActivity(intent)
        }
    }
}
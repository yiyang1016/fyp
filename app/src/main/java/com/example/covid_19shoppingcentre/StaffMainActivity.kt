package com.example.covid_19shoppingcentre

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class StaffMainActivity : AppCompatActivity() {
//    private var Database = FirebaseDatabase.getInstance().getReference()
//    private val EVERY_EIGHT_SECOND: Long = 1000
//
//    // Run on parallel
//    private var handler: Handler? = null
//    private var runnable: Runnable? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.staff_main)

        val serviceClass = staffService::class.java
        val intent1 = Intent(applicationContext, serviceClass)

        if (!isServiceRunning(serviceClass)) {
            // Start the service
            startService(intent1)
        } else {
            Toast.makeText(applicationContext,"service already running",Toast.LENGTH_SHORT).show()
        }

        val SC_Customer_CheckIn = findViewById<Button>(R.id.SCCheckInBtn)
        val SC_CheckedIn_Customer = findViewById<Button>(R.id.SCCheckInCustomerBtn)
        val SC_View_Store = findViewById<Button>(R.id.viewStoreBtn)

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

        SC_View_Store.setOnClickListener{
            val intent = Intent(this, Store_List::class.java).apply {
                putExtra("EXTRA_MESSAGE", "message")
            }
            startActivity(intent)
        }
    }

    private fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        // Loop through the running services
        for (service in activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                // If the service is running then return true
                return true
            }
        }
        return false
    }
}
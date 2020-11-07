package com.example.covid_19shoppingcentre

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.customer_in_store_layout.view.*
import kotlinx.android.synthetic.main.list_layout.view.*
import kotlinx.android.synthetic.main.reserve_store_details.*
import kotlinx.android.synthetic.main.staff_store_details.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class StaffStoreDetailsActivity : AppCompatActivity() {

    private var Database = FirebaseDatabase.getInstance().getReference()
    lateinit var  mRecyclerView: RecyclerView
    lateinit var mDatabase : DatabaseReference
    lateinit var FirebaseRecyclerAdapter : FirebaseRecyclerAdapter<CheckInScCustomer, CustomerViewHolder>

    // CONSTANT
    private val EVERY_ONE_SECOND: Long = 1000

    // Run on parallel
    private var handler: Handler? = null
    private var runnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.staff_store_details)

        val storeName = intent.getStringExtra("StoreName")
        val storeId = intent.getStringExtra("StoreId")

        val currentDateTime = LocalDateTime.now()
        val hourFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("H")
        val hourText = currentDateTime.format(hourFormat)
        val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val dateText = currentDateTime.format(dateFormat)

        mRecyclerView = findViewById(R.id.customerInStoreList)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.setLayoutManager(LinearLayoutManager(this))

        mDatabase = FirebaseDatabase.getInstance().getReference("CheckInStore").child(dateText.toString()).child(storeId).child(hourText.toString())

        StoreName.text = storeName

        customerNumber()
        logRecyclerView()
        executeHandler()
    }

    private fun logRecyclerView(){
        val currentDateTime = LocalDateTime.now()
        val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val dateText = currentDateTime.format(dateFormat)

        FirebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<CheckInScCustomer, CustomerViewHolder>(
            CheckInScCustomer::class.java,
            R.layout.customer_in_store_layout,
            CustomerViewHolder::class.java,
            mDatabase
        ) {
            override fun populateViewHolder(p0: CustomerViewHolder, p1: CheckInScCustomer, p2: Int) {
                p0.mView.CheckInTime.text = p1.checkInTime

                val query = Database.child("ShoppingCentre").child(dateText.toString()).orderByChild("customerId").equalTo(p1.customerId)
                query.addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(s0: DataSnapshot) {
                        for (s0 in s0.children){
                            p0.mView.CustomerName.text = s0.child("name").value.toString()
                            p0.mView.PhoneNumber.text = s0.child("phone").value.toString()
                            p0.mView.Temperature.text = s0.child("bodyTemperature").value.toString()
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(applicationContext, "notification", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
        mRecyclerView.adapter = FirebaseRecyclerAdapter
    }

    private fun customerNumber() {
        val storeId = intent.getStringExtra("StoreId")
        var customerCountInt = 0
        val currentDateTime = LocalDateTime.now()
        val hourFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("H")
        val hourText = currentDateTime.format(hourFormat)
        val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val dateText = currentDateTime.format(dateFormat)

        val query = Database.child("CheckInStore").child(dateText.toString()).child(storeId).child(hourText.toString())
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (p0 in p0.children) {
                    customerCountInt++
                }
                customerCount.text = customerCountInt.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
            }
        })
    }

    class CustomerViewHolder( var mView: View) : RecyclerView.ViewHolder(mView) {
    }

    private fun executeHandler() {
        //If the handler and runnable are null we create it the first time.
        if (handler == null && runnable == null) {
            handler = Handler()
            runnable = object : Runnable {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun run() {
                    //Updating firebase store/getting
                    customerNumber()
                    //And we execute it again
                    handler!!.postDelayed(this, EVERY_ONE_SECOND)
                }
            }
        } else {
            handler?.postDelayed(runnable, EVERY_ONE_SECOND)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        //execute the handler again.
        executeHandler()
    }

    override fun onPause() {
        super.onPause()
        //we remove the callback
        handler?.removeCallbacks(runnable)
        //and we set the status to offline.
    }

}
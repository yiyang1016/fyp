package com.example.covid_19shoppingcentre

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.sc_checked_in_list.*
import kotlinx.android.synthetic.main.sc_checked_in_list_layout.view.*
import java.text.DateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class CheckInScCustomer_List : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var customerDatabase: DatabaseReference
    lateinit var FirebaseRecyclerAdapter : FirebaseRecyclerAdapter<CheckInScCustomer, CustomerViewHolder>

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sc_checked_in_list)

        val currentDateTime  = LocalDateTime.now()
        val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val dateText = currentDateTime.format(dateFormat)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.setLayoutManager(LinearLayoutManager(this))
        customerDatabase = FirebaseDatabase.getInstance().getReference("ShoppingCentre").child(dateText.toString())

        setActionBar()
        logRecyclerView()

        searchCustomer.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val searchText = searchCustomer.text.toString().trim()
                logRecyclerView(searchText)
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun logRecyclerView(){
        FirebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<CheckInScCustomer, CustomerViewHolder>(
            CheckInScCustomer::class.java,
            R.layout.sc_checked_in_list_layout,
            CustomerViewHolder::class.java,
            customerDatabase
        ) {
            override fun populateViewHolder(p0: CustomerViewHolder, p1: CheckInScCustomer, p2: Int) {
                if (p1.status.toString() == "checkIn") {
                    p0.mView.SCCustomerName.text = p1.name.toString()
                    p0.mView.SCCustomerPhone.text = p1.phone.toString()

                    val calendar1 = Calendar.getInstance()
                    val currentDay = DateFormat.getDateInstance(DateFormat.LONG).format(calendar1.time)

                    p0.mView.setOnClickListener {
                        val i = Intent(
                            this@CheckInScCustomer_List,
                            CustomerInformationActivity::class.java
                        )
                        i.putExtra("name", p1.name.toString())
                        i.putExtra("phone", p1.phone.toString())
                        i.putExtra("temperature", p1.bodyTemperature.toString())
                        i.putExtra("time", p1.checkInTime.toString())
                        i.putExtra("date", currentDay.toString())
                        i.putExtra("id",p1.customerId.toString())
                        startActivity(i)
                    }
                } else{
                    p0.mView.visibility = View.GONE
                    p0.mView.layoutParams = RecyclerView.LayoutParams(0, 0)
                }
            }
        }
        recyclerView.adapter = FirebaseRecyclerAdapter
    }

    private fun logRecyclerView(searchCustomer: String){
        if(searchCustomer.isEmpty()){

            FirebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<CheckInScCustomer, CustomerViewHolder>(
                CheckInScCustomer::class.java,
                R.layout.sc_checked_in_list_layout,
                CustomerViewHolder::class.java,
                customerDatabase
            ) {
                override fun populateViewHolder(p0: CustomerViewHolder, p1: CheckInScCustomer, p2: Int) {
                    if (p1.status.toString() == "checkIn") {
                        p0.mView.SCCustomerName.text = p1.name.toString()
                        p0.mView.SCCustomerPhone.text = p1.phone.toString()

                        val calendar1 = Calendar.getInstance()
                        val currentDay =
                            DateFormat.getDateInstance(DateFormat.LONG).format(calendar1.time)

                        p0.mView.setOnClickListener {
                            val i = Intent(
                                this@CheckInScCustomer_List,
                                CustomerInformationActivity::class.java
                            )
                            i.putExtra("name", p1.name.toString())
                            i.putExtra("phone", p1.phone.toString())
                            i.putExtra("temperature", p1.bodyTemperature.toString())
                            i.putExtra("time", p1.checkInTime.toString())
                            i.putExtra("date", currentDay.toString())
                            i.putExtra("id",p1.customerId.toString())
                            startActivity(i)
                        }
                    } else{
                        p0.mView.visibility = View.GONE
                        p0.mView.layoutParams = RecyclerView.LayoutParams(0, 0)
                    }
                }
            }
            recyclerView.adapter = FirebaseRecyclerAdapter
        }else {
            val firebaseSearchQuery = customerDatabase.orderByChild("name").startAt(searchCustomer).endAt(searchCustomer+"\uf8ff")

            FirebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<CheckInScCustomer, CustomerViewHolder>(
                CheckInScCustomer::class.java,
                R.layout.sc_checked_in_list_layout,
                CustomerViewHolder::class.java,

                firebaseSearchQuery
            ) {
                override fun populateViewHolder(p0: CustomerViewHolder, p1: CheckInScCustomer, p2: Int) {
                    if (p1.status.toString() == "checkIn") {
                        p0.mView.SCCustomerName.text = p1.name.toString()
                        p0.mView.SCCustomerPhone.text = p1.phone.toString()

                        val calendar1 = Calendar.getInstance()
                        val currentDay =
                            DateFormat.getDateInstance(DateFormat.LONG).format(calendar1.time)

                        p0.mView.setOnClickListener {
                            val i = Intent(
                                this@CheckInScCustomer_List,
                                CustomerInformationActivity::class.java
                            )
                            i.putExtra("name", p1.name.toString())
                            i.putExtra("phone", p1.phone.toString())
                            i.putExtra("temperature", p1.bodyTemperature.toString())
                            i.putExtra("time", p1.checkInTime.toString())
                            i.putExtra("date", currentDay.toString())
                            i.putExtra("id",p1.customerId.toString())
                            startActivity(i)
                        }
                    } else{
                        p0.mView.visibility = View.GONE
                        p0.mView.layoutParams = RecyclerView.LayoutParams(0, 0)
                    }
                }
            }
            recyclerView.adapter = FirebaseRecyclerAdapter
        }
    }
    class CustomerViewHolder( var mView: View) : RecyclerView.ViewHolder(mView)

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent1 = Intent(this, StaffMainActivity::class.java).apply {
            putExtra("EXTRA_MESSAGE", "message")
        }
        startActivity(intent1)
        return false
    }

    private fun setActionBar(){
        val actionBar: ActionBar? = supportActionBar
        actionBar!!.title = "Customer List"
        actionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    override fun onBackPressed() {
        val intent1 = Intent(this, StaffMainActivity::class.java).apply {
            putExtra("EXTRA_MESSAGE", "message")
        }
        startActivity(intent1)
    }
}

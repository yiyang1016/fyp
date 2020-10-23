package com.example.covid_19shoppingcentre

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_store__list.*
import kotlinx.android.synthetic.main.list_layout.view.*
import kotlinx.android.synthetic.main.sc_checked_in_list.*
import kotlinx.android.synthetic.main.sc_checked_in_list_layout.view.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
        val hi = 1

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.setHasFixedSize(true)
        recyclerView.setLayoutManager(LinearLayoutManager(this))
        customerDatabase = FirebaseDatabase.getInstance().getReference("ShoppingCentre").child(dateText.toString())
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

    private  fun logRecyclerView(){
        FirebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<CheckInScCustomer, CustomerViewHolder>(
            CheckInScCustomer::class.java,
            R.layout.sc_checked_in_list_layout,
            CustomerViewHolder::class.java,
            customerDatabase
        ) {
            override fun populateViewHolder(p0: CustomerViewHolder, p1: CheckInScCustomer, p2: Int) {
                    p0.mView.SCCustomerName.text = p1.name.toString()
                    p0.mView.SCCustomerPhone.text = p1.phone.toString()
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
                        p0.mView.SCCustomerName.text = p1.name.toString()
                        p0.mView.SCCustomerPhone.text = p1.phone.toString()
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
                    p0.mView.SCCustomerName.text = p1.name.toString()
                    p0.mView.SCCustomerPhone.text = p1.phone.toString()
                    p0.mView.nameText.text = p1.status.toString()
                }
            }
            recyclerView.adapter = FirebaseRecyclerAdapter
        }
    }
    class CustomerViewHolder( var mView: View) : RecyclerView.ViewHolder(mView) {
    }

}
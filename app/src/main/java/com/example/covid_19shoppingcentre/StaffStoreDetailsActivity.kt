package com.example.covid_19shoppingcentre

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.customer_in_store_layout.view.*
import kotlinx.android.synthetic.main.staff_store_details.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class StaffStoreDetailsActivity : AppCompatActivity() {

    private var Database = FirebaseDatabase.getInstance().getReference()
    lateinit var mRecyclerView: RecyclerView
    lateinit var mDatabase : DatabaseReference
    lateinit var FirebaseRecyclerAdapter : FirebaseRecyclerAdapter<CheckInScCustomer, CustomerViewHolder>

    // CONSTANT
    private val EVERY_ONE_SECOND: Long = 10000

    // Run on parallel
    private var handler: Handler? = null
    private var runnable: Runnable? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.staff_store_details)

        setActionBar()
        chart()

        val storeName = intent.getStringExtra("StoreName")
        val storeId = intent.getStringExtra("StoreId")
        val currentDateTime = LocalDateTime.now()
        val hourFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("H")
        val hourText = currentDateTime.format(hourFormat)
        val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val dateText = currentDateTime.format(dateFormat)

        val storeTime =  resources.getStringArray(R.array.inStoreTime)
        val spinner = findViewById<Spinner>(R.id.time_spinner)
        spinner.background.setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);

        if (spinner != null){
            val adapter = ArrayAdapter(this,
                R.layout.time_spinner, storeTime)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                @SuppressLint("ResourceAsColor")
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {
                    when {
                        storeTime[position].toString() == "8am" -> {
                            mDatabase = FirebaseDatabase.getInstance().getReference("CheckInStore").child(dateText.toString()).child(storeId).child("8")
                            logRecyclerView()
                        }
                        storeTime[position].toString() == "9am" -> {
                            mDatabase = FirebaseDatabase.getInstance().getReference("CheckInStore").child(dateText.toString()).child(storeId).child("9")
                            logRecyclerView()
                        }
                        storeTime[position].toString() == "10am" -> {
                            mDatabase = FirebaseDatabase.getInstance().getReference("CheckInStore").child(dateText.toString()).child(storeId).child("10")
                            logRecyclerView()
                        }
                        storeTime[position].toString() == "11am" -> {
                            mDatabase = FirebaseDatabase.getInstance().getReference("CheckInStore").child(dateText.toString()).child(storeId).child("11")
                            logRecyclerView()
                            chart()
                        }
                        storeTime[position].toString() == "12am" -> {
                            mDatabase = FirebaseDatabase.getInstance().getReference("CheckInStore").child(dateText.toString()).child(storeId).child("12")
                            logRecyclerView()
                        }
                        storeTime[position].toString() == "1pm" -> {
                            mDatabase = FirebaseDatabase.getInstance().getReference("CheckInStore").child(dateText.toString()).child(storeId).child("13")
                            logRecyclerView()
                        }
                        storeTime[position].toString() == "2pm" -> {
                            mDatabase = FirebaseDatabase.getInstance().getReference("CheckInStore").child(dateText.toString()).child(storeId).child("14")
                            logRecyclerView()
                        }
                        storeTime[position].toString() == "3pm" -> {
                            mDatabase = FirebaseDatabase.getInstance().getReference("CheckInStore").child(dateText.toString()).child(storeId).child("15")
                            logRecyclerView()
                        }
                        storeTime[position].toString() == "4pm" -> {
                            mDatabase = FirebaseDatabase.getInstance().getReference("CheckInStore").child(dateText.toString()).child(storeId).child("16")
                            logRecyclerView()
                        }
                        storeTime[position].toString() == "5pm" -> {
                            mDatabase = FirebaseDatabase.getInstance().getReference("CheckInStore").child(dateText.toString()).child(storeId).child("17")
                            logRecyclerView()
                        }
                        storeTime[position].toString() == "6pm" -> {
                            mDatabase = FirebaseDatabase.getInstance().getReference("CheckInStore").child(dateText.toString()).child(storeId).child("18")
                            logRecyclerView()
                        }
                        storeTime[position].toString() == "7pm" -> {
                            mDatabase = FirebaseDatabase.getInstance().getReference("CheckInStore").child(dateText.toString()).child(storeId).child("19")
                            logRecyclerView()
                        }
                        storeTime[position].toString() == "8pm" -> {
                            mDatabase = FirebaseDatabase.getInstance().getReference("CheckInStore").child(dateText.toString()).child(storeId).child("20")
                            logRecyclerView()
                        }
                        storeTime[position].toString() == "9pmm" -> {
                            mDatabase = FirebaseDatabase.getInstance().getReference("CheckInStore").child(dateText.toString()).child(storeId).child("21")
                            logRecyclerView()
                        }
                        storeTime[position].toString() == "10pm" -> {
                            mDatabase = FirebaseDatabase.getInstance().getReference("CheckInStore").child(dateText.toString()).child(storeId).child("22")
                            logRecyclerView()
                        }
                        else -> {
                            mDatabase = FirebaseDatabase.getInstance().getReference("CheckInStore").child(dateText.toString()).child(storeId).child(hourText.toString())
                            logRecyclerView()
                        }
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                }
            }
        }

        mRecyclerView = findViewById(R.id.customerInStoreList)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.setLayoutManager(LinearLayoutManager(this))

        StoreName.text = storeName

        customerNumber()
        executeHandler()
    }

    private fun chart(){
        val currentDateTime = LocalDateTime.now()
        val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val dateText = currentDateTime.format(dateFormat)
        val storeId = intent.getStringExtra("StoreId")

        val linechart = findViewById<LineChart>(R.id.storeLinechart)

        val entries1 = ArrayList<Entry>()

        for (x in 8..22) {
            val data1 = Database.child("CheckInStore").child(dateText.toString()).child(storeId).child(x.toString())
            data1.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(q0: DataSnapshot) {
                    if (!q0.exists()) {
                        var floaat = x.toFloat()
                        entries1.add(Entry(floaat, 0f))
                    } else {
                        var floaat1 = q0.childrenCount.toFloat()
                        var floaat = x.toFloat()
                        entries1.add(Entry(floaat, floaat1))

                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
                }
            })
        }

        entries1.add(Entry(7f, 0f))

        val v2 = LineDataSet(entries1, "Number of Customer")

        v2.setDrawValues(true)
        v2.setDrawFilled(true)
        v2.lineWidth = 2f
        v2.fillColor = R.color.white
        v2.fillAlpha = R.color.white

        linechart.xAxis.position = XAxis.XAxisPosition.BOTTOM

        linechart.axisLeft.setDrawGridLines(false);
        linechart.xAxis.setDrawGridLines(false);

        linechart.data = LineData(v2)

        linechart.xAxis.labelRotationAngle = 0f

        linechart.axisRight.isEnabled = false
        linechart.xAxis.axisMaximum = 22f
        linechart.xAxis.axisMinimum = 8f

        linechart.axisLeft.axisMinimum = 0f
        linechart.axisLeft.axisMaximum = 30f

        linechart.setTouchEnabled(true)
        linechart.setPinchZoom(true)

        linechart.description.text = "Hours"
        linechart.setNoDataText("No customer yet!")

        linechart.animateX(1000, Easing.EaseInExpo)
        linechart.getXAxis().setAvoidFirstLastClipping(true)
        linechart.extraLeftOffset = 15f
        linechart.extraRightOffset = 15f
        linechart.axisLeft.setStartAtZero(true)
    }

    private fun logRecyclerView(){
        val currentDateTime = LocalDateTime.now()
        val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val dateText = currentDateTime.format(dateFormat)
        val hourFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("H")
        val hourText = currentDateTime.format(hourFormat)
        val hourtext2 = hourText.toInt() - 1

        FirebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<CheckInScCustomer, CustomerViewHolder>(
            CheckInScCustomer::class.java,
            R.layout.customer_in_store_layout,
            CustomerViewHolder::class.java,
            mDatabase
        ) {
            override fun populateViewHolder(p0: CustomerViewHolder, p1: CheckInScCustomer, p2: Int) {
//                if (p1.status.toString() == "active"){

                    p0.mView.CheckInTime.text = p1.checkInTime
                if (p1.status == "pass") {
                    p0.mView.CustomerStatus.text = "Checked Out"
                } else if (p1.status == "active"){
                    p0.mView.CustomerStatus.text = "Checking In"
                }

                val query = Database.child("ShoppingCentre").child(dateText.toString())
                    .orderByChild("customerId").equalTo(p1.customerId)
                query.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(s0: DataSnapshot) {
                        for (s0 in s0.children) {
                            p0.mView.CustomerName.text = s0.child("name").value.toString()
                            p0.mView.PhoneNumber.text = s0.child("phone").value.toString()
                            p0.mView.Temperature.text = s0.child("bodyTemperature").value.toString()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(applicationContext, "notification", Toast.LENGTH_SHORT)
                            .show()
                    }
                })
//            } else{
//                    p0.mView.visibility = View.GONE
//                    p0.mView.layoutParams = RecyclerView.LayoutParams(0, 0)
//                }
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
        val hourtext1 = hourText.toInt() - 1

        val query = Database.child("CheckInStore").child(dateText.toString()).child(storeId).child(hourText.toString())
        val query1 = Database.child("CheckInStore").child(dateText.toString()).child(storeId).child(hourtext1.toString())
        query.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (p0 in p0.children) {
                    if(p0.child("status").value.toString() == "active") {
                        customerCountInt++
                    }
                }
                query1.addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (snapshot in snapshot.children) {
                            if(snapshot.child("status").value.toString() == "active") {
                                customerCountInt++
                            }
                        }
                        customerCount.text = customerCountInt.toString()
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
                    }
                })
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
            }
        })
    }

    class CustomerViewHolder( var mView: View) : RecyclerView.ViewHolder(mView) {
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent1 = Intent(this, Store_List::class.java).apply {
            putExtra("EXTRA_MESSAGE", "message")
        }
        startActivity(intent1)
        return false
    }

    private fun setActionBar(){
        val actionBar: ActionBar? = supportActionBar
        actionBar!!.title = "Store Details"
        actionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun executeHandler() {
        //If the handler and runnable are null we create it the first time.
        if (handler == null && runnable == null) {
            handler = Handler()
            runnable = object : Runnable {
                @RequiresApi(Build.VERSION_CODES.O)
                override fun run() {
                    //Updating firebase store/getting
                    chart()
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
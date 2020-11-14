package com.example.covid_19shoppingcentre

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
    lateinit var  mRecyclerView: RecyclerView
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

        val storeName = intent.getStringExtra("StoreName")
        val storeId = intent.getStringExtra("StoreId")

        val currentDateTime = LocalDateTime.now()
        val hourFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("H")
        val hourText = currentDateTime.format(hourFormat)
        val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val dateText = currentDateTime.format(dateFormat)

        val linechart = findViewById<LineChart>(R.id.storeLinechart)

        val entries = ArrayList<Entry>()

        for (x in 8..22) {
            val data1 = Database.child("CheckInStore").child(dateText.toString()).child(storeId).child(x.toString())

            data1.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(q0: DataSnapshot) {

                    if (q0.exists()) {
                        var floaat1 = q0.childrenCount.toFloat()
                        var floaat = x.toFloat()
                        entries.add(Entry(floaat, floaat1))
                    } else {
                        var floaat = x.toFloat()
                        entries.add(Entry(floaat, 0f))
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
                }
            })
        }

        entries.add(Entry(7f, 0f))

        val v1 = LineDataSet(entries, "Number of Customer")

        v1.setDrawValues(true)
        v1.setDrawFilled(true)
        v1.lineWidth = 2f
        v1.fillColor = R.color.white
        v1.fillAlpha = R.color.white

        linechart.xAxis.position = XAxis.XAxisPosition.BOTTOM

        linechart.axisLeft.setDrawGridLines(false);
        linechart.xAxis.setDrawGridLines(false);

        linechart.data = LineData(v1)

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

        linechart.animateX(1800, Easing.EaseInExpo)



        mRecyclerView = findViewById(R.id.customerInStoreList)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.setLayoutManager(LinearLayoutManager(this))

        mDatabase = FirebaseDatabase.getInstance().getReference("CheckInStore").child(dateText.toString()).child(storeId).child(hourText.toString())

        StoreName.text = storeName

        customerNumber()
        logRecyclerView()
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
            val data1 = Database.child("CheckInStore").child("20201114").child(storeId).child(x.toString())

            data1.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(q0: DataSnapshot) {

                    if (q0.exists()) {
                        var floaat1 = q0.childrenCount.toFloat()
                        var floaat = x.toFloat()
                        entries1.add(Entry(floaat, floaat1))
                        Toast.makeText(applicationContext, floaat1.toString() + "ERROR", Toast.LENGTH_SHORT).show()
                    } else {
                        var floaat = x.toFloat()
                        entries1.add(Entry(floaat, 0f))
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

        //linechart.animateY(1000, Easing.EaseInExpo)
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
                if (p1.status.toString() == "active"){
                    p0.mView.CheckInTime.text = p1.checkInTime

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
            } else{
                    p0.mView.visibility = View.GONE
                    p0.mView.layoutParams = RecyclerView.LayoutParams(0, 0)
                }
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
                    if(p0.child("status").value.toString() == "active") {

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
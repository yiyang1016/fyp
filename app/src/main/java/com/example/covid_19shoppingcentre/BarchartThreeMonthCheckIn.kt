package com.example.covid_19shoppingcentre

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.barchart_three_month_check_in.*
import kotlinx.android.synthetic.main.tablet_set_limitation.*
import kotlinx.android.synthetic.main.tablet_store_current_cust.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class BarchartThreeMonthCheckIn : AppCompatActivity() {
    lateinit var thisMonth:String
    lateinit var lastMonth:String
    lateinit var last2Month:String

    lateinit var thisMonthNumber:String
    lateinit var lastMonthNumber:String
    lateinit var last2MonthNumber:String

    lateinit var thisYear:String
    lateinit var lastMonthYear:String
    lateinit var last2MonthYear:String

    var thisNumber:Int = 0
    var lastNumber:Int = 0
    var last2Number:Int = 0

    lateinit var key : String
    lateinit var yearAndMonth : String

    var verify: Int = 0

    var entries = ArrayList<BarEntry>()

    var count:Int = 0

    lateinit var mDatabase:  DatabaseReference
    lateinit var firebaseRecyclerAdapter: FirebaseRecyclerAdapter<Store, Store_List.StoreViewHolder>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.barchart_three_month_check_in)

        val dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")
        val now = LocalDateTime.now()
        val currentDateTime = LocalDateTime.now()
        val dateFormat = DateTimeFormatter.ofPattern("yyyyMMdd")
        val monthFormat = DateTimeFormatter.ofPattern("MM")
        val dateText = currentDateTime.format(dateFormat)

        val monthDateTime = LocalDateTime.now()
        val monthDateTime1 = LocalDateTime.now().minusMonths(1)
        val monthDateTime2 = LocalDateTime.now().minusMonths(2)

        var xString: String = ""


        thisMonth = currentDateTime.month.toString()
        lastMonth = currentDateTime.month.minus(1).toString()
        last2Month = currentDateTime.month.minus(2).toString()

        thisMonthNumber = monthDateTime.format(monthFormat).toString()
        lastMonthNumber = monthDateTime1.format(monthFormat).toString()
        last2MonthNumber = monthDateTime2.format(monthFormat).toString()

        thisYear = currentDateTime.year.toString()
        lastMonthYear = currentDateTime.minusMonths(1).year.toString()
        last2MonthYear = currentDateTime.minusMonths(2).year.toString()

        mDatabase = FirebaseDatabase.getInstance().getReference()


        val ref = mDatabase.child("ShoppingCentre")
        val ref2 = mDatabase.child("ShoppingCentre")
        val ref3 = mDatabase.child("ShoppingCentre")

        /////////////////////////////////////////

        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snap: DataSnapshot) {
                if(snap.exists()) {
                    for (x in 1..31) {
                        if (x < 10) {
                            xString = "0" + x.toString()
                        } else {
                            xString = x.toString()
                        }

                        if (snap.child(thisYear + thisMonthNumber + xString).exists()){
                            thisNumber += snap.child(thisYear + thisMonthNumber + xString).childrenCount.toInt()

                        }
                    }
                    entries.add(BarEntry(2f, thisNumber.toFloat()))
                }
                verify += 1;
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
            }
        })

        /////////////////////////////////////////////////

        ref2.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snap: DataSnapshot) {
                if(snap.exists()) {
                    for (x in 1..31) {
                        if (x < 10) {
                            xString = "0" + x.toString()
                        } else {
                            xString = x.toString()
                        }

                        if (snap.child(lastMonthYear + lastMonthNumber + xString).exists()){
                            lastNumber += snap.child(lastMonthYear + lastMonthNumber + xString).childrenCount.toInt()

                        }
                    }
                    entries.add(BarEntry(1f, lastNumber.toFloat()))
                }
                verify += 1;
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
            }
        })

        ///////////////////////////////////

        ref3.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snap: DataSnapshot) {
                if(snap.exists()) {
                    for (x in 1..31) {
                        if (x < 10) {
                            xString = "0" + x.toString()
                        } else {
                            xString = x.toString()
                        }

                        if (snap.child(last2MonthYear + last2MonthNumber + xString).exists()){
                            last2Number += snap.child(last2MonthYear + last2MonthNumber + xString).childrenCount.toInt()

                        }
                    }
                    entries.add(BarEntry(0f, last2Number.toFloat()))
                }
                verify += 1;
                if(verify == 3) {
                    setBarChart()
                }else{
                    Toast.makeText(applicationContext, "Retrieving Data", Toast.LENGTH_SHORT)
                        .show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
            }
        })

        ///////////////////////////////////////////////////////////////

    }

    private fun setBarChart() {

        Toast.makeText(applicationContext, thisNumber.toString(), Toast.LENGTH_SHORT).show()

        entries.add(BarEntry(0f, last2Number.toFloat()))

        var barDataSet = BarDataSet(entries, "Number of Customer")

        var labels = ArrayList<String>()
        labels.add(last2Month)
        labels.add(lastMonth)
        labels.add(thisMonth)


        var data = BarData(barDataSet)

        barChartMonthCheckIn.data = data // set the data and list of lables into chart
        barChartMonthCheckIn.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        barChartMonthCheckIn.axisLeft.textColor = Color.WHITE
        barChartMonthCheckIn.axisRight.textColor = Color.WHITE
        barChartMonthCheckIn.xAxis.textColor = Color.WHITE
        barChartMonthCheckIn.xAxis.textSize = 15f

        barChartMonthCheckIn.axisLeft.textSize = 15f
        barChartMonthCheckIn.axisRight.textSize = 15f

        barChartMonthCheckIn.data.setValueTextColor(Color.WHITE)
        barChartMonthCheckIn.data.setValueTextSize(15f)
        barChartMonthCheckIn.legend.textSize = 15f
        barChartMonthCheckIn.legend.textColor = Color.WHITE

        barChartMonthCheckIn.setPinchZoom(false)
        barChartMonthCheckIn.setDrawGridBackground(false)
        barChartMonthCheckIn.setDrawBarShadow(false)

        val description: Description = Description()
        description.textColor = Color.BLACK
        description.text = "Three Months CheckIn Record"
        description.textSize = 15f

        barChartMonthCheckIn.setDescription(description)  // set the description

        //barDataSet.setColors(ColorTemplate.COLORFUL_COLORS)
        barDataSet.color = resources.getColor(R.color.colorAccent)

        barChartMonthCheckIn.animateY(3000)

        }

}
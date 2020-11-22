package com.example.covid_19shoppingcentre

import android.graphics.Color
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class PieChartMaleFemale : AppCompatActivity() {

    lateinit var mDatabase: DatabaseReference
    lateinit var firebaseRecyclerAdapter: FirebaseRecyclerAdapter<Store, Store_List.StoreViewHolder>

    private  var male : Int = 0
    private var female : Int = 0
    private var count : Int = 0

    private var IC: String = ""
    private var a = 0f
    private var b = 0f
    private var booleanStatus : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pie_chart_male_female)

        val pieChart = findViewById<PieChart>(R.id.pieChart)
        pieChart.setUsePercentValues(true)

        val value = ArrayList<PieEntry>()
        val listColors = ArrayList<Int>()


        var pds : PieDataSet

        mDatabase = FirebaseDatabase.getInstance().getReference("Member")

        val firebaseSearchQuery = mDatabase.orderByChild("Id").startAt("M00001").endAt("M00001"+"\uf8ff")

        val ref = Firebase.database.reference.child("Member")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                for (p0 in p0.children) {
                    IC = p0.child("IC_Number").value.toString()
                    if(IC != null) {
                        IC = p0.child("IC_Number").value.toString()
                        IC = IC.takeLast(1)
                        count = IC.toInt()
                        if(count % 2 > 0){
                            a += 1f
                        }else{
                            b += 1f
                        }

                    }
                }

                listColors.add(Color.YELLOW)
                //value.add(PieEntry(male.toFloat(),"Male"))
                listColors.add(Color.MAGENTA)
                //value.add(PieEntry(female.toFloat(),"Female"))

                value.add(PieEntry(a.toFloat(),"Male"))
                value.add(PieEntry(b.toFloat(),"Female"))


                pds = PieDataSet(value,"Percentage of Male and Female")
                pds.valueTextSize = 35f
                pds.colors = listColors

                pieChart.data = PieData(pds)

                pieChart.setCenterTextColor(Color.WHITE)
                pieChart.setUsePercentValues(true)
                pieChart.isDrawHoleEnabled = false
                pieChart.description.isEnabled = false
                pieChart.setEntryLabelColor(Color.BLUE)
                pieChart.animateY(1400, Easing.EaseInOutQuad)
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
            }

        })

    }

}
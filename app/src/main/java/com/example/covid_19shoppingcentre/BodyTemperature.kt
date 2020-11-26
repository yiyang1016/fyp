package com.example.covid_19shoppingcentre

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.FirebaseError
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_store__list.*
import kotlinx.android.synthetic.main.temperature_rv_item_layout.view.*
import kotlinx.android.synthetic.main.temperature_history.*
import java.text.SimpleDateFormat
import java.util.*

class BodyTemperature :AppCompatActivity() {
    lateinit var  mSearchDate: String
    lateinit var mDatabase : DatabaseReference
    lateinit var  nDatabase: DatabaseReference
    lateinit var  mRecycleView: RecyclerView
    lateinit var mMemberId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        /////////////////////////////////////////
        val intent1: Intent = intent
       mMemberId = intent1.getStringExtra("MemberID")
        ////////////////////////////////////////
        super.onCreate(savedInstanceState)
        setContentView(R.layout.temperature_history)

        mSearchDate = txtTemperatureDate.text.toString()
        mRecycleView = findViewById(R.id.recTemperature)
        mDatabase = FirebaseDatabase.getInstance().getReference("ShoppingCentre")
        nDatabase = FirebaseDatabase.getInstance().getReference("NewCheckInStore")
        mRecycleView.setHasFixedSize(true)
        mRecycleView.layoutManager = LinearLayoutManager(this)
        //moveFirebaseRecord(mDatabase, nDatabase)
        loadFirebaseData()

        btnTemperatureOK.setOnClickListener{
            loadFirebaseData(mSearchDate)
        }

       txtTemperatureDate.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val searchText = txtTemperatureDate.text.toString()
                loadFirebaseData(searchText)
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })

    }

    fun Date.toString(format: String, locale: Locale = Locale.getDefault()): String {
        val formatter = SimpleDateFormat(format, locale)
        return formatter.format(this)
    }

    fun getCurrentDateTime(): Date {
        return Calendar.getInstance().time
    }

    private fun loadFirebaseData() {
        val date = getCurrentDateTime()
        val currentDateTime = date.toString("yyyyMMdd")
        val firebaseSearchQuery = mDatabase.child(currentDateTime).orderByChild("customerId").equalTo(mMemberId)
        var FirebaseRecyclerAdapter = object: FirebaseRecyclerAdapter<BodyTemperatureDetails, BodyTemperatureDetailsViewHolder>(
            BodyTemperatureDetails::class.java,
            R.layout.temperature_rv_item_layout,
            BodyTemperatureDetailsViewHolder::class.java,
            firebaseSearchQuery
        ){
            override fun populateViewHolder(
                p0: BodyTemperatureDetailsViewHolder?,
                p1: BodyTemperatureDetails?,
                p2: Int
            ) {
                if(p1!=null){
                        p0?.nview?.lblTempNumb?.setText(p1.bodyTemperature)
                        var bodytemp = p1.bodyTemperature?.substring(0, 4)?.toFloat()
                        if (bodytemp != null) {
                            if (bodytemp > 37.5) {
                                p0?.nview?.lblTempStatus?.setText("Fever")
                            } else if (bodytemp < 36.5) {
                                p0?.nview?.lblTempStatus?.setText("Cold")
                            } else {
                                p0?.nview?.lblTempStatus?.setText("Normal")
                            }
                        }
                }else{
                    Toast.makeText(
                        applicationContext,
                        "There is no data to show",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
        mRecycleView.adapter=FirebaseRecyclerAdapter

    }

    private fun loadFirebaseData(searchDate:String) {
        if(searchDate.isEmpty()){
            val date = getCurrentDateTime()
             val currentDateTime = date.toString("yyyyMMdd")
            val firebaseSearchQuery = mDatabase.child(currentDateTime).orderByChild("customerId").equalTo(mMemberId)
            var FirebaseRecyclerAdapter = object: FirebaseRecyclerAdapter<BodyTemperatureDetails, BodyTemperatureDetailsViewHolder>(
                BodyTemperatureDetails::class.java,
                R.layout.temperature_rv_item_layout,
                BodyTemperatureDetailsViewHolder::class.java,
                firebaseSearchQuery
            ){
                override fun populateViewHolder(
                    p0: BodyTemperatureDetailsViewHolder?,
                    p1: BodyTemperatureDetails?,
                    p2: Int
                ) {
                    if(p1!=null){
                        p0?.nview?.lblTempNumb?.setText(p1.bodyTemperature)
                        var bodytemp = p1.bodyTemperature?.substring(0, 4)?.toFloat()
                        if (bodytemp != null) {
                            if (bodytemp > 37.5) {
                                p0?.nview?.lblTempStatus?.setText("Fever")
                            } else if (bodytemp < 36.5) {
                                p0?.nview?.lblTempStatus?.setText("Cold")
                            } else {
                                p0?.nview?.lblTempStatus?.setText("Normal")
                            }
                        }
                    }else{
                        Toast.makeText(
                            applicationContext,
                            "There is no data to show",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
            mRecycleView.adapter=FirebaseRecyclerAdapter
        }else{
            val firebaseSearchQuery = mDatabase.child(searchDate)
            var FirebaseRecyclerAdapter = object: FirebaseRecyclerAdapter<BodyTemperatureDetails, BodyTemperatureDetailsViewHolder>(
                BodyTemperatureDetails::class.java,
                R.layout.temperature_rv_item_layout,
                BodyTemperatureDetailsViewHolder::class.java,
                firebaseSearchQuery
            ){
                override fun populateViewHolder(
                    p0: BodyTemperatureDetailsViewHolder?,
                    p1: BodyTemperatureDetails?,
                    p2: Int
                ) {
                    if(p1!=null){
                        if(p1.customerId.toString() == mMemberId){
                            p0?.nview?.lblTempNumb?.setText(p1.bodyTemperature)
                            p0?.nview?.lblTempStatus?.setText(p1.status)
                        }
                    }else{
                        Toast.makeText(
                            applicationContext,
                            "There is no data to show",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
            mRecycleView.adapter=FirebaseRecyclerAdapter
        }
    }

    private fun moveFirebaseRecord(fromPath: DatabaseReference, toPath: DatabaseReference){
        fromPath.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                System.out.println("Copy Failed")
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                toPath.setValue(snapshot.value)
                @Override
                fun onComplete(firebaseError: FirebaseError, databaseReference: DatabaseReference){
                    if(firebaseError!=null){
                        System.out.println("Copy Failed")
                    }else{
                        System.out.println("Success")
                    }
                }

            }
        })
    }

    class BodyTemperatureDetailsViewHolder(var nview: View): RecyclerView.ViewHolder(nview){
    }
}
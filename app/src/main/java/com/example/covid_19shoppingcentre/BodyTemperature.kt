package com.example.covid_19shoppingcentre

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.FirebaseError
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.temperature_rv_item_layout.view.*
import kotlinx.android.synthetic.main.temperature_history.*

class BodyTemperature :AppCompatActivity() {
    lateinit var  mSearchDate: String
    lateinit var mDatabase : DatabaseReference
    lateinit var  nDatabase: DatabaseReference
    lateinit var  mRecycleView: RecyclerView
    lateinit var mMemberId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        /////////////////////////////////////////
        val intent1: Intent = intent
        var memberId = intent1.getStringExtra("MemberID")
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
    }

    private fun loadFirebaseData() {
        var FirebaseRecyclerAdapter = object: FirebaseRecyclerAdapter<BodyTemperatureDetails, BodyTemperatureDetailsViewHolder>(
            BodyTemperatureDetails::class.java,
            R.layout.temperature_rv_item_layout,
            BodyTemperatureDetailsViewHolder::class.java,
            nDatabase
        ){
            override fun populateViewHolder(
                p0: BodyTemperatureDetailsViewHolder?,
                p1: BodyTemperatureDetails?,
                p2: Int
            ) {
                if(p1!=null){
                    if(p1.customerId.toString() == mMemberId){
                        p0?.nview?.lblTempNumb?.setText(p1.bodyTemp)
                        p0?.nview?.lblTempStatus?.setText(p1.status)
                    }
                }
            }
        }
        mRecycleView.adapter=FirebaseRecyclerAdapter

    }

    private fun loadFirebaseData(searchDate:String) {
        val firebaseSearchQuery = nDatabase.orderByChild("checkInDate").startAt(searchDate).endAt(searchDate+"\uf8ff")
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
                        p0?.nview?.lblTempNumb?.setText(p1.bodyTemp)
                        p0?.nview?.lblTempStatus?.setText(p1.status)
                    }
                }
            }
        }
        mRecycleView.adapter=FirebaseRecyclerAdapter
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
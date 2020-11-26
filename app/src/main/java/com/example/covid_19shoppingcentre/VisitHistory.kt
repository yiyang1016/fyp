package com.example.covid_19shoppingcentre

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.dynamic_rv_item_layout.view.*
import kotlinx.android.synthetic.main.visitation_history.*

class VisitHistory : AppCompatActivity(){

    lateinit var mSearchText:String
    lateinit var  mSearchDate: String
    lateinit var mMemberId: String
    lateinit var  mRecycleView: RecyclerView
    //firebase
    lateinit var mDatabase : DatabaseReference
    lateinit var storeDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        /////////////////////////////////////////
        val intent1: Intent = intent
        mMemberId = intent1.getStringExtra("MemberID")
        ////////////////////////////////////////
        super.onCreate(savedInstanceState)
        setContentView(R.layout.visitation_history)

       // mSearchText = txtStoreName.text.toString()
        mSearchDate = txtVisitorDate.text.toString()
        mRecycleView = findViewById(R.id.recviewVisitorHistory)

        mDatabase = FirebaseDatabase.getInstance().getReference("NewCheckInStore")
        storeDatabase = FirebaseDatabase.getInstance().getReference("Store")

        mRecycleView.setHasFixedSize(true)
        mRecycleView.layoutManager = LinearLayoutManager(this)
        loadFirebaseData()


        btnVisitorOK.setOnClickListener{
            loadFirebase(mSearchDate)
        }

    }



    private fun loadFirebaseData() {
        var FirebaseRecyclerAdapter = object: FirebaseRecyclerAdapter<MemberVisitDetails, MemberVisitDetailsViewHolder>(
            MemberVisitDetails::class.java,
            R.layout.dynamic_rv_item_layout,
            MemberVisitDetailsViewHolder::class.java,
            mDatabase
        ){
            override fun populateViewHolder(p0: MemberVisitDetailsViewHolder?, p1: MemberVisitDetails?, p2: Int) {
                if(p1!=null) {
                    if (p1.customerId.toString() == mMemberId) {
                            p0?.nview?.lblCheckInDate?.setText(p1.checkInDate)
                            var searchDatabase: Query = FirebaseDatabase.getInstance().getReference("Store").orderByKey().equalTo(p1.storeId)
                            searchDatabase.addListenerForSingleValueEvent(object: ValueEventListener {
                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                  }

                                 override fun onDataChange(snapshot: DataSnapshot) {
                                     var storename: String
                                     var storeurl:String
                                    if(snapshot.exists()){
                                        storename = snapshot.child(p1.storeId.toString()).child("Store_Name").value.toString()
                                        storeurl = snapshot.child(p1.storeId.toString()).child("Store_Image").value.toString()
                                        p0?.nview?.lblCheckInStore?.setText(snapshot.child(p1.storeId.toString()).child("Store_Name").value.toString())
                                        Picasso.with(this@VisitHistory).load(snapshot.child(p1.storeId.toString()).child("Store_Image").value.toString()).into(p0?.nview?.imgStoreView)
                                 }
                        }
                        })
                    }
                }

            }

        }
        mRecycleView.adapter =FirebaseRecyclerAdapter
    }

    private fun loadFirebase(searchDate: String) {

        val firebaseSearchQuery =
            mDatabase.orderByChild("checkInDate").startAt(searchDate)
                .endAt(searchDate + "\uf8ff")
        var FirebaseRecyclerAdapter =
            object : FirebaseRecyclerAdapter<MemberVisitDetails, MemberVisitDetailsViewHolder>(
                MemberVisitDetails::class.java,
                R.layout.dynamic_rv_item_layout,
                MemberVisitDetailsViewHolder::class.java,
                //mDatabase
                firebaseSearchQuery
            ) {
                override fun populateViewHolder(
                    p0: MemberVisitDetailsViewHolder?,
                    p1: MemberVisitDetails?,
                    p2: Int
                ) {
                    if (p1 != null) {
                        if (p1.customerId.toString() == mMemberId) {
                            p0?.nview?.lblCheckInDate?.setText(p1.checkInDate)
                            var searchDatabase: Query =
                                FirebaseDatabase.getInstance().getReference("Store").orderByKey()
                                    .equalTo(p1.storeId)
                            searchDatabase.addListenerForSingleValueEvent(object :
                                ValueEventListener {
                                override fun onCancelled(error: DatabaseError) {
                                    TODO("Not yet implemented")
                                }

                                override fun onDataChange(snapshot: DataSnapshot) {
                                    if (snapshot.exists()) {
                                        p0?.nview?.lblCheckInStore?.setText(
                                            snapshot.child(p1.storeId.toString())
                                                .child("Store_Name").value.toString()
                                        )
                                        Picasso.with(this@VisitHistory).load(
                                            snapshot.child(p1.storeId.toString())
                                                .child("Store_Image").value.toString()
                                        ).into(p0?.nview?.imgStoreView)
                                    }
                                }
                            })
                        }
                    }

                }

            }
            mRecycleView.adapter =FirebaseRecyclerAdapter
    }


    // // View Holder Class


    class MemberVisitDetailsViewHolder(var nview: View): RecyclerView.ViewHolder(nview){
    }
}
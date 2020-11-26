package com.example.covid_19shoppingcentre

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
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_store__list.*
import kotlinx.android.synthetic.main.customer_record_rv_item_layout.view.*
import kotlinx.android.synthetic.main.customer_record_selecting.*
import kotlinx.android.synthetic.main.dynamic_rv_item_layout.view.*

class CustomerRecordSelecting : AppCompatActivity() {
    lateinit var mSearchText: String
    lateinit var mSearchIC: String
    lateinit var mRecycleView: RecyclerView

    //firebase
    lateinit var mDatabase: DatabaseReference
    lateinit var storeDatabase: DatabaseReference
    lateinit var memberDatabase: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.customer_record_selecting)

        mSearchText = txtRecordDate.text.toString()
        mSearchIC = editTextNumber.text.toString()
        mRecycleView = findViewById(R.id.recMemRecord)
        mDatabase = FirebaseDatabase.getInstance().getReference("NewShoppingCentre")
        storeDatabase = FirebaseDatabase.getInstance().getReference("NewCheckInStore")
        memberDatabase = FirebaseDatabase.getInstance().getReference("Member")

        mRecycleView.setHasFixedSize(true)
        mRecycleView.layoutManager = LinearLayoutManager(this)
        loadFirebaseData()

        btnMemberRecord.setOnClickListener{
            loadFirebaseData(mSearchText,mSearchIC)
        }

        txtRecordDate.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val searchText = txtRecordDate.text.toString().toUpperCase().trim()
                val searchid = editTextNumber.text.toString()
                searchText.toUpperCase()
                loadFirebaseData(searchText, searchid)
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })

        editTextNumber.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val searchText = txtRecordDate.text.toString().toUpperCase().trim()
                val searchid = editTextNumber.text.toString()
                searchText.toUpperCase()
                loadFirebaseData(searchText,searchid)
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })

    }


    private fun loadFirebaseData() {
        var FirebaseRecyclerAdapter = object: FirebaseRecyclerAdapter<MemberVisitDetails, CustomerRecordDetailsViewHolder>(
            MemberVisitDetails::class.java,
            R.layout.customer_record_rv_item_layout,
            CustomerRecordDetailsViewHolder::class.java,
            storeDatabase
        ){
            override fun populateViewHolder(
                p0: CustomerRecordDetailsViewHolder?,
                p1: MemberVisitDetails?,
                p2: Int
            ) {
                var settext1:String
                var settext2:String
                var settext3:String
                p0?.nview?.lblCustomerRecordCheckInDate?.setText(p1?.checkInDate)
                var searchMemberName: Query = FirebaseDatabase.getInstance().getReference("Member").orderByKey().equalTo(p1?.customerId)
                searchMemberName.addListenerForSingleValueEvent(object: ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        if(snapshot.exists()){
                            p0?.nview?.txtCustRecdName?.setText(snapshot.child(p1?.customerId.toString()).child("Name").value.toString())
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        TODO("Not yet implemented")
                    }
                })

                                    settext1 = p1?.storeId.toString()
                                    var searchDatabase: Query = FirebaseDatabase.getInstance().getReference("Store").orderByKey().equalTo(settext1)
                                    searchDatabase.addListenerForSingleValueEvent(object: ValueEventListener {
                                        override fun onCancelled(error: DatabaseError) {
                                            TODO("Not yet implemented")
                                        }
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            var storename: String
                                            var storeurl:String
                                            if(snapshot.exists()){
                                                storename = snapshot.child(p1?.storeId.toString()).child("Store_Name").value.toString()
                                                storeurl = snapshot.child(p1?.storeId.toString()).child("Store_Image").value.toString()
                                                p0?.nview?.lblCustomerRecordCheckInStore?.setText(snapshot.child(p1?.storeId.toString()).child("Store_Name").value.toString())
                                                Picasso.with(this@CustomerRecordSelecting).load(snapshot.child(p1?.storeId.toString()).child("Store_Image").value.toString()).into(p0?.nview?.imgStoreView1)
                                            }
                                            else{
                                                Toast.makeText(applicationContext, "Store Not Found", Toast.LENGTH_SHORT).show()
                                            }
                                        }
                                    })
            }
        }

        mRecycleView.adapter = FirebaseRecyclerAdapter
    }


    private fun loadFirebaseData(searchDate:String, searchId:String) {
        if(searchDate.isEmpty() && searchId.isEmpty()){
            var FirebaseRecyclerAdapter = object: FirebaseRecyclerAdapter<MemberVisitDetails, CustomerRecordDetailsViewHolder>(
                MemberVisitDetails::class.java,
                R.layout.customer_record_rv_item_layout,
                CustomerRecordDetailsViewHolder::class.java,
                storeDatabase
            ){
                override fun populateViewHolder(
                    p0: CustomerRecordDetailsViewHolder?,
                    p1: MemberVisitDetails?,
                    p2: Int
                ) {
                    var settext1:String
                    var settext2:String
                    var settext3:String
                    p0?.nview?.lblCustomerRecordCheckInDate?.setText(p1?.checkInDate)
                    var searchMemberName: Query = FirebaseDatabase.getInstance().getReference("Member").orderByKey().equalTo(p1?.customerId)
                    searchMemberName.addListenerForSingleValueEvent(object: ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if(snapshot.exists()){
                                p0?.nview?.txtCustRecdName?.setText(snapshot.child(p1?.customerId.toString()).child("Name").value.toString())
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })

                    settext1 = p1?.storeId.toString()
                    var searchDatabase: Query = FirebaseDatabase.getInstance().getReference("Store").orderByKey().equalTo(settext1)
                    searchDatabase.addListenerForSingleValueEvent(object: ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                        override fun onDataChange(snapshot: DataSnapshot) {
                            var storename: String
                            var storeurl:String
                            if(snapshot.exists()){
                                storename = snapshot.child(p1?.storeId.toString()).child("Store_Name").value.toString()
                                storeurl = snapshot.child(p1?.storeId.toString()).child("Store_Image").value.toString()
                                p0?.nview?.lblCustomerRecordCheckInStore?.setText(snapshot.child(p1?.storeId.toString()).child("Store_Name").value.toString())
                                Picasso.with(this@CustomerRecordSelecting).load(snapshot.child(p1?.storeId.toString()).child("Store_Image").value.toString()).into(p0?.nview?.imgStoreView1)
                            }
                            else{
                                Toast.makeText(applicationContext, "Store Not Found", Toast.LENGTH_SHORT).show()
                            }
                        }
                    })
                }
            }

            mRecycleView.adapter = FirebaseRecyclerAdapter
        }else if(searchDate.isNotEmpty()&&searchId.isEmpty()) {
            val query = storeDatabase.orderByChild("checkInDate").startAt(searchDate).endAt(searchDate+"\uf8ff")
            var FirebaseRecyclerAdapter = object :
                FirebaseRecyclerAdapter<MemberVisitDetails, CustomerRecordDetailsViewHolder>(
                    MemberVisitDetails::class.java,
                    R.layout.customer_record_rv_item_layout,
                    CustomerRecordDetailsViewHolder::class.java,
                    query
                ) {
                override fun populateViewHolder(
                    p0: CustomerRecordDetailsViewHolder?,
                    p1: MemberVisitDetails?,
                    p2: Int
                ) {
                    var settext1: String
                    var settext2: String
                    var settext3: String
                        p0?.nview?.lblCustomerRecordCheckInDate?.setText(p1?.checkInDate)
                        var searchMemberName: Query =
                            FirebaseDatabase.getInstance().getReference("Member").orderByKey()
                                .equalTo(p1?.customerId)
                        searchMemberName.addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                if (snapshot.exists()) {
                                    p0?.nview?.txtCustRecdName?.setText(
                                        snapshot.child(p1?.customerId.toString())
                                            .child("Name").value.toString()
                                    )
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }
                        })

                        settext1 = p1?.storeId.toString()
                        var searchDatabase: Query =
                            FirebaseDatabase.getInstance().getReference("Store").orderByKey()
                                .equalTo(settext1)
                        searchDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(error: DatabaseError) {
                                TODO("Not yet implemented")
                            }

                            override fun onDataChange(snapshot: DataSnapshot) {
                                var storename: String
                                var storeurl: String
                                if (snapshot.exists()) {
                                    storename = snapshot.child(p1?.storeId.toString())
                                        .child("Store_Name").value.toString()
                                    storeurl = snapshot.child(p1?.storeId.toString())
                                        .child("Store_Image").value.toString()
                                    p0?.nview?.lblCustomerRecordCheckInStore?.setText(
                                        snapshot.child(p1?.storeId.toString())
                                            .child("Store_Name").value.toString()
                                    )
                                    Picasso.with(this@CustomerRecordSelecting).load(
                                        snapshot.child(p1?.storeId.toString())
                                            .child("Store_Image").value.toString()
                                    ).into(p0?.nview?.imgStoreView1)
                                } else {
                                    Toast.makeText(
                                        applicationContext,
                                        "Store Not Found",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            }
                        })
                }
            }

            mRecycleView.adapter = FirebaseRecyclerAdapter
        }else if(searchDate.isEmpty()&&searchId.isNotEmpty()) {
            val query = storeDatabase.orderByChild("customerId").equalTo(searchId)
            var FirebaseRecyclerAdapter = object :
                FirebaseRecyclerAdapter<MemberVisitDetails, CustomerRecordDetailsViewHolder>(
                    MemberVisitDetails::class.java,
                    R.layout.customer_record_rv_item_layout,
                    CustomerRecordDetailsViewHolder::class.java,
                    query
                ) {
                override fun populateViewHolder(
                    p0: CustomerRecordDetailsViewHolder?,
                    p1: MemberVisitDetails?,
                    p2: Int
                ) {
                    var settext1: String
                    var settext2: String
                    var settext3: String
                    p0?.nview?.lblCustomerRecordCheckInDate?.setText(p1?.checkInDate)
                    var searchMemberName: Query =
                        FirebaseDatabase.getInstance().getReference("Member").orderByKey()
                            .equalTo(p1?.customerId)
                    searchMemberName.addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                p0?.nview?.txtCustRecdName?.setText(
                                    snapshot.child(p1?.customerId.toString())
                                        .child("Name").value.toString()
                                )
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })

                    settext1 = p1?.storeId.toString()
                    var searchDatabase: Query =
                        FirebaseDatabase.getInstance().getReference("Store").orderByKey()
                            .equalTo(settext1)
                    searchDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            var storename: String
                            var storeurl: String
                            if (snapshot.exists()) {
                                storename = snapshot.child(p1?.storeId.toString())
                                    .child("Store_Name").value.toString()
                                storeurl = snapshot.child(p1?.storeId.toString())
                                    .child("Store_Image").value.toString()
                                p0?.nview?.lblCustomerRecordCheckInStore?.setText(
                                    snapshot.child(p1?.storeId.toString())
                                        .child("Store_Name").value.toString()
                                )
                                Picasso.with(this@CustomerRecordSelecting).load(
                                    snapshot.child(p1?.storeId.toString())
                                        .child("Store_Image").value.toString()
                                ).into(p0?.nview?.imgStoreView1)
                            } else {
                                Toast.makeText(
                                    applicationContext,
                                    "Store Not Found",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    })
                }
            }

            mRecycleView.adapter = FirebaseRecyclerAdapter
        }else{
            val query = storeDatabase.orderByChild("checkInDate").equalTo(searchDate)
            var FirebaseRecyclerAdapter = object :
                FirebaseRecyclerAdapter<MemberVisitDetails, CustomerRecordDetailsViewHolder>(
                    MemberVisitDetails::class.java,
                    R.layout.customer_record_rv_item_layout,
                    CustomerRecordDetailsViewHolder::class.java,
                    query
                ) {
                override fun populateViewHolder(
                    p0: CustomerRecordDetailsViewHolder?,
                    p1: MemberVisitDetails?,
                    p2: Int
                ) {
                    var settext1: String
                    var settext2: String
                    var settext3: String
                    if(p1?.customerId.equals(searchId)){
                    p0?.nview?.lblCustomerRecordCheckInDate?.setText(p1?.checkInDate)
                    var searchMemberName: Query =
                        FirebaseDatabase.getInstance().getReference("Member").orderByKey()
                            .equalTo(p1?.customerId)
                    searchMemberName.addListenerForSingleValueEvent(object :
                        ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                p0?.nview?.txtCustRecdName?.setText(
                                    snapshot.child(p1?.customerId.toString())
                                        .child("Name").value.toString()
                                )
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })

                    settext1 = p1?.storeId.toString()
                    var searchDatabase: Query =
                        FirebaseDatabase.getInstance().getReference("Store").orderByKey()
                            .equalTo(settext1)
                    searchDatabase.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }

                        override fun onDataChange(snapshot: DataSnapshot) {
                            var storename: String
                            var storeurl: String
                            if (snapshot.exists()) {
                                storename = snapshot.child(p1?.storeId.toString())
                                    .child("Store_Name").value.toString()
                                storeurl = snapshot.child(p1?.storeId.toString())
                                    .child("Store_Image").value.toString()
                                p0?.nview?.lblCustomerRecordCheckInStore?.setText(
                                    snapshot.child(p1?.storeId.toString())
                                        .child("Store_Name").value.toString()
                                )
                                Picasso.with(this@CustomerRecordSelecting).load(
                                    snapshot.child(p1?.storeId.toString())
                                        .child("Store_Image").value.toString()
                                ).into(p0?.nview?.imgStoreView1)
                            } else {
                                Toast.makeText(
                                    applicationContext,
                                    "Store Not Found",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    })
                }
                }
            }

            mRecycleView.adapter = FirebaseRecyclerAdapter
        }
    }

      class CustomerRecordDetailsViewHolder(var nview: View): RecyclerView.ViewHolder(nview){
      }
    }








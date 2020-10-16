package com.example.covid_19shoppingcentre

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.*
//import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_store__list.*
import kotlinx.android.synthetic.main.list_layout.view.*


class Store_List :AppCompatActivity() {

    lateinit var  mRecyclerView: RecyclerView
    lateinit var mDatabase : DatabaseReference
    lateinit var FirebaseRecyclerAdapter : FirebaseRecyclerAdapter<Store, StoreViewHolder>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store__list)


        mRecyclerView = findViewById(R.id.listView)
        mRecyclerView.setHasFixedSize(true )
        mRecyclerView.setLayoutManager(LinearLayoutManager(this))
        mDatabase = FirebaseDatabase.getInstance().getReference("Store")
        logRecyclerView()


        searchStore.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
               val searchText = searchStore.text.toString().trim()
                logRecyclerView(searchText)
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }
    private  fun logRecyclerView(){
        FirebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<Store, StoreViewHolder>(
            Store::class.java,
            R.layout.list_layout,
            StoreViewHolder::class.java,
            mDatabase
        ) {
            override fun populateViewHolder(p0: StoreViewHolder, p1: Store, p2: Int) {
                p0.mView.name.setText(p1.Store_Name)
                p0.mView.floor.setText(p1.Store_Floor + "," + p1.Store_Slot)
                p0.mView.limit.setText("/" + p1.Store_Limitation)
                Picasso.with(this@Store_List).load(p1.Store_Image).into(p0.mView.image)
            }
        }
        mRecyclerView.adapter = FirebaseRecyclerAdapter
    }
    private  fun logRecyclerView(searchText: String){
        if(searchText.isEmpty()){
           // FirebaseRecyclerAdapter.cleanup()
            //mRecyclerView.adapter=FirebaseRecyclerAdapter

            FirebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<Store, StoreViewHolder>(
                Store::class.java,
                R.layout.list_layout,
                StoreViewHolder::class.java,
                mDatabase
            ) {
                override fun populateViewHolder(p0: StoreViewHolder, p1: Store, p2: Int) {
                    p0.mView.name.setText(p1.Store_Name)
                    p0.mView.floor.setText(p1.Store_Floor + "," + p1.Store_Slot)
                    p0.mView.limit.setText("/" + p1.Store_Limitation)
                    Picasso.with(this@Store_List).load(p1.Store_Image).into(p0.mView.image)
                }
            }
            mRecyclerView.adapter = FirebaseRecyclerAdapter
        }else {
            val firebaseSearchQuery = mDatabase.orderByChild("Store_Name").startAt(searchText).endAt(searchText+"\uf8ff")
            //mDatabase = FirebaseDatabase.getInstance().getReference("Store")
            //val options: FirebaseRecyclerOptions<Store> = FirebaseRecyclerOptions.Builder<Store>().setQuery(mDatabase, Store::class.java).build()
            FirebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<Store, StoreViewHolder>(
                Store::class.java,
                R.layout.list_layout,
                StoreViewHolder::class.java,
               // mDatabase
                firebaseSearchQuery
            ) {
                override fun populateViewHolder(p0: StoreViewHolder, p1: Store, p2: Int) {

                    p0.mView.name.setText(p1.Store_Name)
                    p0.mView.floor.setText(p1.Store_Floor + "," + p1.Store_Slot)
                    p0.mView.limit.setText("/" + p1.Store_Limitation)
                    //p0.mView.textView4.setText(p1.Store_Slot)

                    // Get a non-default Storage bucket
                    //val storageRef = storage.reference

                    // storageRef.child("Store").downloadUrl.addOnSuccessListener {
                    //    Picasso.with(this@Store_List).load(storageRef).into(p0.mView.image)
                    //}.addOnFailureListener {
                    // Handle any errors
                    // }

                    Picasso.with(this@Store_List).load(p1.Store_Image).into(p0.mView.image)
                    //searchStore.setText(p1.Store_Name)
                }
            }
            mRecyclerView.adapter = FirebaseRecyclerAdapter
        }
    }
    // View Holder
    class StoreViewHolder( var mView: View) : RecyclerView.ViewHolder(mView) {

    }


}
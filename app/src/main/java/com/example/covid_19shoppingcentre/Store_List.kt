package com.example.covid_19shoppingcentre

import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
//import com.firebase.ui.database.FirebaseRecyclerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.list_layout.view.*


class Store_List :AppCompatActivity() {

    lateinit var  mRecyclerView: RecyclerView
    lateinit var mDatabase : DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_store__list)

        mDatabase = FirebaseDatabase.getInstance().getReference("Store")
        mRecyclerView = findViewById(R.id.listView)
        mRecyclerView.setHasFixedSize(true )
        mRecyclerView.setLayoutManager(LinearLayoutManager(this))
        logRecyclerView()
    }

    private  fun logRecyclerView(){


        //val options: FirebaseRecyclerOptions<Store> = FirebaseRecyclerOptions.Builder<Store>().setQuery(mDatabase, Store::class.java).build()
        var FirebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<Store, StoreViewHolder>(
            Store::class.java,
            R.layout.list_layout,
            StoreViewHolder::class.java,
            mDatabase
        ){
            override fun populateViewHolder(p0: StoreViewHolder, p1: Store, p2: Int) {
                p0.mView.name.setText(p1.Store_Name)
                p0.mView.floor.setText(p1.Store_Floor+ ","+ p1.Store_Slot)
                p0.mView.limit.setText(""+p1.Store_Limitation)
                //p0.mView.textView4.setText(p1.Store_Slot)

            }

        }

        mRecyclerView.adapter = FirebaseRecyclerAdapter
    }
    // View Holder
    class StoreViewHolder( var mView: View) : RecyclerView.ViewHolder(mView) {

    }


}
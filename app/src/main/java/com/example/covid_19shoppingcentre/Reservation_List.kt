package com.example.covid_19shoppingcentre

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.*
//import com.firebase.ui.database.FirebaseRecyclerOptions
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_store__list.*
import kotlinx.android.synthetic.main.list_layout.view.*
import kotlinx.android.synthetic.main.list_layout.view.image
import kotlinx.android.synthetic.main.list_layout.view.name
import kotlinx.android.synthetic.main.reservation_list_layout.view.*
import kotlinx.android.synthetic.main.reserve_store_date.*
import kotlinx.android.synthetic.main.reserve_store_list_layout.view.*
import java.util.*


class Reservation_List :AppCompatActivity() {

    lateinit var  mRecyclerView: RecyclerView
    lateinit var reserveDatabase : DatabaseReference
    lateinit var FirebaseRecyclerAdapter : FirebaseRecyclerAdapter<Reserve, ReserveViewHolder>
    private var Database = FirebaseDatabase.getInstance().getReference()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reservation_list)

        mRecyclerView = findViewById(R.id.reservationList)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.setLayoutManager(LinearLayoutManager(this))
        reserveDatabase = FirebaseDatabase.getInstance().getReference("Reservation")
        logRecyclerView()
    }

    private  fun logRecyclerView(){
        val memberI = "M00001"

        FirebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<Reserve, ReserveViewHolder>(
            Reserve::class.java,
            R.layout.reservation_list_layout,
            ReserveViewHolder::class.java,
            reserveDatabase
        ) {
            override fun populateViewHolder(p0: ReserveViewHolder, p1: Reserve, p2: Int) {
                val query = reserveDatabase.child("member").equalTo(memberI)
                query.addListenerForSingleValueEvent(object : ValueEventListener{
                    override fun onDataChange(s0: DataSnapshot) {
                        for (s0 in s0.children) {
                            p0.mView.storeName.setText(s0.key.toString())
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
                    }

                })
//                p0.mView.name.setText(p1.Store_Name)
//                Picasso.with(this@Reservation_List).load(p1.Store_Image).into(p0.mView.image)
//                p0.mView.available.setText(p1.Store_Floor + "," + p1.Store_Slot)

//                p0.mView.setOnClickListener {
//                    val i = Intent(
//                        this@Reservation_List,
//                        ReserveDate::class.java
//                    )
//                    i.putExtra("StoreName", p1.Store_Name.toString())
//                    startActivity(i)
//                }
            }
        }
        mRecyclerView.adapter = FirebaseRecyclerAdapter
    }

    class ReserveViewHolder( var mView: View) : RecyclerView.ViewHolder(mView) {
    }
}
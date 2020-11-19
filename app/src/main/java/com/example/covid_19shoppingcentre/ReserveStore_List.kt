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
import kotlinx.android.synthetic.main.reserve_store_date.*
//import kotlinx.android.synthetic.main.reserve_store_list_layout.view.*
import kotlinx.android.synthetic.main.list_layout.view.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class ReserveStore_List :AppCompatActivity() {

    lateinit var  mRecyclerView: RecyclerView
    lateinit var mDatabase : DatabaseReference
    lateinit var FirebaseRecyclerAdapter : FirebaseRecyclerAdapter<Store, StoreViewHolder>
    private var Database = FirebaseDatabase.getInstance().getReference()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reserve_store_list)
        overridePendingTransition(R.anim.slide_up, R.anim.slide_down)
        mRecyclerView = findViewById(R.id.listView)
        mRecyclerView.setHasFixedSize(true)
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
                Picasso.with(this@ReserveStore_List).load(p1.Store_Image).into(p0.mView.image)
                p0.mView.floor.setText(p1.Store_Floor + "," + p1.Store_Slot)


                val findId = Database.child("Store").orderByChild("Store_Name").equalTo(p1.Store_Name)
                findId.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (snapshot in snapshot.children) {
                            p0.mView.setOnClickListener {
                                val i = Intent(
                                    this@ReserveStore_List,
                                    ReserveDate::class.java
                                )
                                i.putExtra("StoreName", p1.Store_Name.toString())
                                startActivity(i)

                            }
                            val storeId = snapshot.key.toString()
                            var customerCountInt = 0
                            val currentDateTime = LocalDateTime.now()
                            val hourFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("H")
                            val hourText = currentDateTime.format(hourFormat)
                            val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
                            val dateText = currentDateTime.format(dateFormat)
                            val hourtext1 = hourText.toInt() - 1

                            val query = Database.child("CheckInStore").child(dateText.toString()).child(storeId).child(hourText.toString())
                            val query1 = Database.child("CheckInStore").child(dateText.toString()).child(storeId).child(hourtext1.toString())
                            query.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snap: DataSnapshot) {
                                    for (snap in snap.children) {
                                        if(snap.child("status").value.toString() == "active") {
                                            customerCountInt++
                                        }
                                    }
                                    query1.addListenerForSingleValueEvent(object : ValueEventListener{
                                        override fun onDataChange(snapshot: DataSnapshot) {
                                            for (snapshot in snapshot.children) {
                                                if(snapshot.child("status").value.toString() == "active") {
                                                    customerCountInt++
                                                }
                                            }
                                            p0.mView.limit.setText(customerCountInt.toString() + "/" + p1.Store_Limitation)
                                        }
                                        override fun onCancelled(error: DatabaseError) {
                                            Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
                                        }
                                    })
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
                                }
                            })
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(applicationContext, "notification", Toast.LENGTH_SHORT).show()
                    }
                })
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
                    Picasso.with(this@ReserveStore_List).load(p1.Store_Image).into(p0.mView.image)

                    val findId = Database.child("Store").orderByChild("Store_Name").equalTo(p1.Store_Name)
                    findId.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (snapshot in snapshot.children) {
                                p0.mView.setOnClickListener {
                                    val i = Intent(
                                        this@ReserveStore_List,
                                        ReserveDate::class.java
                                    )
                                    i.putExtra("StoreName", p1.Store_Name.toString())
                                    startActivity(i)

                                }
                                val storeId = snapshot.key.toString()
                                var customerCountInt = 0
                                val currentDateTime = LocalDateTime.now()
                                val hourFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("H")
                                val hourText = currentDateTime.format(hourFormat)
                                val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
                                val dateText = currentDateTime.format(dateFormat)
                                val hourtext1 = hourText.toInt() - 1

                                val query = Database.child("CheckInStore").child(dateText.toString()).child(storeId).child(hourText.toString())
                                val query1 = Database.child("CheckInStore").child(dateText.toString()).child(storeId).child(hourtext1.toString())
                                query.addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snap: DataSnapshot) {
                                        for (snap in snap.children) {
                                            if(snap.child("status").value.toString() == "active") {
                                                customerCountInt++
                                            }
                                        }
                                        query1.addListenerForSingleValueEvent(object : ValueEventListener{
                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                for (snapshot in snapshot.children) {
                                                    if(snapshot.child("status").value.toString() == "active") {
                                                        customerCountInt++
                                                    }
                                                }
                                                p0.mView.limit.setText(customerCountInt.toString() + "/" + p1.Store_Limitation)
                                            }
                                            override fun onCancelled(error: DatabaseError) {
                                                Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
                                            }
                                        })
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
                                    }
                                })
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(applicationContext, "notification", Toast.LENGTH_SHORT).show()
                        }
                    })
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
                    Picasso.with(this@ReserveStore_List).load(p1.Store_Image).into(p0.mView.image)

                    val findId = Database.child("Store").orderByChild("Store_Name").equalTo(p1.Store_Name)
                    findId.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for (snapshot in snapshot.children) {
                                p0.mView.setOnClickListener {
                                    val i = Intent(
                                        this@ReserveStore_List,
                                        ReserveDate::class.java
                                    )
                                    i.putExtra("StoreName", p1.Store_Name.toString())
                                    startActivity(i)

                                }
                                val storeId = snapshot.key.toString()
                                var customerCountInt = 0
                                val currentDateTime = LocalDateTime.now()
                                val hourFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("H")
                                val hourText = currentDateTime.format(hourFormat)
                                val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
                                val dateText = currentDateTime.format(dateFormat)
                                val hourtext1 = hourText.toInt() - 1

                                val query = Database.child("CheckInStore").child(dateText.toString()).child(storeId).child(hourText.toString())
                                val query1 = Database.child("CheckInStore").child(dateText.toString()).child(storeId).child(hourtext1.toString())
                                query.addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onDataChange(snap: DataSnapshot) {
                                        for (snap in snap.children) {
                                            if(snap.child("status").value.toString() == "active") {
                                                customerCountInt++
                                            }
                                        }
                                        query1.addListenerForSingleValueEvent(object : ValueEventListener{
                                            override fun onDataChange(snapshot: DataSnapshot) {
                                                for (snapshot in snapshot.children) {
                                                    if(snapshot.child("status").value.toString() == "active") {
                                                        customerCountInt++
                                                    }
                                                }
                                                p0.mView.limit.setText(customerCountInt.toString() + "/" + p1.Store_Limitation)
                                            }
                                            override fun onCancelled(error: DatabaseError) {
                                                Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
                                            }
                                        })
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
                                    }
                                })
                            }
                        }
                        override fun onCancelled(error: DatabaseError) {
                            Toast.makeText(applicationContext, "notification", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            }
            mRecyclerView.adapter = FirebaseRecyclerAdapter
        }
    }

    class StoreViewHolder( var mView: View) : RecyclerView.ViewHolder(mView) {
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.slide_down_reverse, R.anim.slide_up_reverse)
    }
}
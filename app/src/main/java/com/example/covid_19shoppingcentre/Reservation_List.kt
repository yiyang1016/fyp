package com.example.covid_19shoppingcentre

//import com.firebase.ui.database.FirebaseRecyclerOptions
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.reservation_list_layout.view.*


class Reservation_List :AppCompatActivity() {

    lateinit var  mRecyclerView: RecyclerView
    lateinit var reserveDatabase : DatabaseReference
    lateinit var FirebaseRecyclerAdapter : FirebaseRecyclerAdapter<Reserve, ReserveViewHolder>
    private var Database = FirebaseDatabase.getInstance().getReference()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reservation_list)

        setActionBar()

        mRecyclerView = findViewById(R.id.reservationList)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.setLayoutManager(LinearLayoutManager(this))
        reserveDatabase = FirebaseDatabase.getInstance().getReference("ReservationList")
        logRecyclerView()

        val left = findViewById<RadioButton>(R.id.current)
        val right = findViewById<RadioButton>(R.id.pass)

        left.setOnClickListener{
            left.setTextColor(Color.BLACK)
            right.setTextColor(Color.WHITE)
            logRecyclerView()
        }
        right.setOnClickListener{
            right.setTextColor(Color.BLACK)
            left.setTextColor(Color.WHITE)
            logRecyclerView1()
        }
    }

    private fun logRecyclerView(){
        val memberI = "M00001"

        FirebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<Reserve, ReserveViewHolder>(
            Reserve::class.java,
            R.layout.reservation_list_layout,
            ReserveViewHolder::class.java,
            reserveDatabase
        ) {
            override fun populateViewHolder(p0: ReserveViewHolder, p1: Reserve, p2: Int) {
                if (p1.memberId.toString() == memberI){
                    if(p1.status.toString() == "active"){
                        p0.mView.reserveTime.setText(p1.time)
                        p0.mView.storeName.setText(p1.storeName)

                        p0.mView.setOnClickListener {
                            val i = Intent(
                                this@Reservation_List,
                                ReserveDetailsActivity::class.java
                            )
                            i.putExtra("name", p1.storeName.toString())
                            i.putExtra("time", p1.time.toString())
                            i.putExtra("date", p1.date.toString())
                            i.putExtra("status", p1.status.toString())
                            startActivity(i)
                        }

                    } else {
                        p0.mView.visibility = View.GONE
                        p0.mView.layoutParams = RecyclerView.LayoutParams(0, 0)
                    }
                } else{
                    p0.mView.visibility = View.GONE
                    p0.mView.layoutParams = RecyclerView.LayoutParams(0, 0)
                }
            }
        }
        mRecyclerView.adapter = FirebaseRecyclerAdapter
    }

    private fun logRecyclerView1(){
        val memberI = "M00001"

        FirebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<Reserve, ReserveViewHolder>(
            Reserve::class.java,
            R.layout.reservation_list_layout,
            ReserveViewHolder::class.java,
            reserveDatabase
        ) {
            override fun populateViewHolder(p0: ReserveViewHolder, p1: Reserve, p2: Int) {
                if (p1.memberId.toString() == memberI){
                    if(p1.status.toString() != "active"){
                        p0.mView.reserveTime.setText(p1.time)
                        p0.mView.storeName.setText(p1.storeName)

                        p0.mView.setOnClickListener {
                            val i = Intent(
                                this@Reservation_List,
                                ReserveDetailsPassActivity::class.java
                            )
                            i.putExtra("name", p1.storeName.toString())
                            i.putExtra("time", p1.time.toString())
                            i.putExtra("date", p1.date.toString())
                            i.putExtra("status", p1.status.toString())
                            startActivity(i)
                        }

                    } else {
                        p0.mView.visibility = View.GONE
                        p0.mView.layoutParams = RecyclerView.LayoutParams(0, 0)
                    }
                } else{
                    p0.mView.visibility = View.GONE
                    p0.mView.layoutParams = RecyclerView.LayoutParams(0, 0)
                }
            }
        }
        mRecyclerView.adapter = FirebaseRecyclerAdapter
    }

    class ReserveViewHolder( var mView: View) : RecyclerView.ViewHolder(mView) {
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val intent1 = Intent(this, MainActivity::class.java).apply {
            putExtra("EXTRA_MESSAGE", "message")
        }
        startActivity(intent1)
        return false
    }

    private fun setActionBar(){
        val actionBar: ActionBar? = supportActionBar
        actionBar!!.title = "Reservation List"
        actionBar!!.setDisplayHomeAsUpEnabled(true)
    }
}
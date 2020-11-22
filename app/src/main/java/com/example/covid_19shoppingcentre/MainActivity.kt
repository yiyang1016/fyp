package com.example.covid_19shoppingcentre

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.customer_review.view.*
import kotlinx.android.synthetic.main.customer_review_submission.view.*
import kotlinx.android.synthetic.main.list_layout.view.*
import kotlinx.android.synthetic.main.reservation_list.*
import kotlinx.android.synthetic.main.staff_store_details.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class MainActivity : AppCompatActivity() {
    private var Database = FirebaseDatabase.getInstance().getReference()
    lateinit var  mRecyclerView: RecyclerView
    lateinit var mDatabase : DatabaseReference
    lateinit var FirebaseRecyclerAdapter : FirebaseRecyclerAdapter<Store, Store_List.StoreViewHolder>
    lateinit var toggle: ActionBarDrawerToggle

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val memberID = intent.getStringExtra("MemberID")
        //Toast.makeText(this,memberID,Toast.LENGTH_SHORT).show()
        toggle = ActionBarDrawerToggle(this, drawer_layout, R.string.drawer_open, R.string.drawer_close)
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        navigationView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.nav_reserve_store -> {
                val i = Intent(
                    this@MainActivity,
                    ReserveStore_List::class.java
                )
                    i.putExtra("memberid", memberID)
                startActivity(i)
            }
                R.id.nav_reservation_list -> {
                    val i = Intent(
                        this@MainActivity,
                        Reservation_List::class.java
                    )
                    i.putExtra("memberid", memberID)
                    startActivity(i)
                }
                R.id.nav_storeList -> {
                    val i = Intent(
                        this@MainActivity,
                        StaffMainActivity::class.java
                    )
                    startActivity(i)
                }
                R.id.nav_generateQR->{
                    val i = Intent(
                        this@MainActivity,
                        QRCodeGenerator::class.java
                    )
                    i.putExtra("MemberID", memberID)
                    startActivity(i)
                }
                R.id.nav_distanceTrackingBtn ->{
                    val intent = Intent(this, distance_tracking::class.java)
                    intent.putExtra("MemberID", memberID)
                    startActivity(intent)
                }

                 R.id.nav_nearestHostpitalBtn -> {
                    val intent = Intent(this, nearby_hospital::class.java)
                     intent.putExtra("MemberID", memberID)
                    startActivity(intent)
                }

                R.id.nav_history -> {
                    val intent = Intent(this, social_distance_score_history::class.java)
                    intent.putExtra("MemberID", memberID)
                    startActivity(intent)
                }

                R.id.nav_scanQR->{
                    val intent = Intent(this, QRCodeScanner::class.java)
                    intent.putExtra("MemberID", memberID)
                    startActivity(intent)
                }

                R.id.nav_memberMenu->{
                    val intent = Intent(this, QRCodeScanner::class.java)
                    intent.putExtra("MemberID", memberID)
                    startActivity(intent)
                }

                R.id.nav_review ->{
                    val dialogBuilder = AlertDialog.Builder(this@MainActivity, R.style.CustomAlertDialog)

                    val view = layoutInflater.inflate(R.layout.customer_review, null)
                    dialogBuilder.setView(view)
                    val alertDialog = dialogBuilder.create()
                    alertDialog.show()

                    val submitButton = view.btnSubmit
                    submitButton.setOnClickListener {
                        if (view.review1.rating.toInt() == 0 || view.review2.rating.toInt() == 0 ||
                            view.review3.rating.toInt() == 0 || view.review4.rating.toInt() == 0
                        ) {
                            val errordialogBuilder =
                                AlertDialog.Builder(this@MainActivity, R.style.CustomAlertDialog)
                            val view1 = layoutInflater.inflate(R.layout.customer_review_empty_field, null)
                            errordialogBuilder.setView(view1)
                            val alertDialog1 = errordialogBuilder.create()
                            alertDialog1.show()
                            view1.okbtn.setOnClickListener {
                                alertDialog1.cancel()
                            }
                        } else {
                            val ref = FirebaseDatabase.getInstance().getReference("Review")
                            var reviewId = ""
                            val refSearch =
                                FirebaseDatabase.getInstance().getReference("Review").orderByKey()
                                    .limitToLast(1)

                            refSearch.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onCancelled(error: DatabaseError) {
                                    val text = "Connection Failed"
                                    Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
                                }

                                override fun onDataChange(p0: DataSnapshot) {
                                    if (p0.exists()) {
                                        for (p0 in p0.children) {
                                            val reviewsId = p0.getValue(ReviewIdClass::class.java)
                                            reviewId = reviewsId.toString()
                                        }

                                        val cal = ((reviewId.substring(2, 7)).toInt()) + 1
                                        val num = 100000 + cal
                                        val newId = "RV" + num.toString().substring(1,6)

                                        val editText = view.review5
                                        if (editText.text.toString() != null) {
                                            val showString = editText.text.toString()

                                            var feedback = ""
                                            feedback = view.review5.text.toString()

                                            val data = Review(
                                                newId,
                                                view.review1.rating.toInt(),
                                                view.review2.rating.toInt(),
                                                view.review3.rating.toInt(),
                                                view.review4.rating.toInt(),
                                                feedback
                                            )

                                            ref.child(newId).setValue(data).addOnCompleteListener {
                                                val submissiondialogBuilder = AlertDialog.Builder(
                                                    this@MainActivity,
                                                    R.style.CustomAlertDialog
                                                )
                                                alertDialog.cancel()
                                                val view2 = layoutInflater.inflate(
                                                    R.layout.customer_review_submission,
                                                    null
                                                )
                                                submissiondialogBuilder.setView(view2)
                                                val alertDialog = submissiondialogBuilder.create()
                                                alertDialog.show()
                                                view2.okbtn.setOnClickListener {
                                                    alertDialog.cancel()
                                                }
                                            }

                                        }
                                    }

                                }
                            })

                        }
                    }
                }
            }
            true
        }

        val serviceClass = checkService::class.java
        val intent1 = Intent(applicationContext, serviceClass)
        intent1.putExtra("MemberId", memberID)

        if (!isServiceRunning(serviceClass)) {
            // Start the service
            startService(intent1)
        } else {
        }

        //Display First Two Store
        mRecyclerView = findViewById(R.id.listView)
        mRecyclerView.setHasFixedSize(true )
        mRecyclerView.setLayoutManager(LinearLayoutManager(this))
        mDatabase = Firebase.database.reference
            //FirebaseDatabase.getInstance().getReference("Store").limitToFirst(2)
        logRecyclerView()

        storeList_btn.setOnClickListener {
            val i = Intent(
                this@MainActivity,
                ReserveStore_List::class.java
            )
            i.putExtra("memberid", memberID)
            startActivity(i)
        }

        button.setOnClickListener {

        }

        //Get Today Score
        val refSearch = FirebaseDatabase.getInstance().getReference().child("Member").orderByChild("Id").equalTo(memberID)
        refSearch.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                val text = "Connection Failed"
                Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
            }

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    for (p0 in p0.children) {
                        displayScoreText.setText(p0.child("CurrentScore").value.toString())
                    }
                }else{
                    Toast.makeText(applicationContext, "Haiyaa", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    private fun isServiceRunning(serviceClass: Class<*>): Boolean {
        val activityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

        // Loop through the running services
        for (service in activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                // If the service is running then return true
                return true
            }
        }
        return false
    }
    private fun logRecyclerView(){
        val memberID:String = intent.getStringExtra("MemberID")

        FirebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<Store, Store_List.StoreViewHolder>(
            Store::class.java,
            R.layout.list_layout,
            Store_List.StoreViewHolder::class.java,
            mDatabase.child("Store").limitToFirst(2)
        ) {
            override fun populateViewHolder(p0: Store_List.StoreViewHolder, p1: Store, p2: Int) {
                p0.mView.name.setText(p1.Store_Name)
                p0.mView.floor.setText(p1.Store_Floor + "," + p1.Store_Slot)

                Picasso.with(this@MainActivity).load(p1.Store_Image).into(p0.mView.image)

                //val findId = Database.child("Store").orderByChild("Store_Name").limitToFirst(2)
                val findId = Database.child("Store").orderByChild("Store_Name").equalTo(p1.Store_Name)
                findId.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for (snapshot in snapshot.children) {
                            p0.mView.setOnClickListener {
                                val i = Intent(
                                    this@MainActivity,
                                    ReserveStore_List::class.java
                                )
                                i.putExtra("memberid", memberID)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }
}


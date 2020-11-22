package com.example.covid_19shoppingcentre

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.reserve_store_date.*
import kotlinx.android.synthetic.main.reserve_store_date.imageView
import kotlinx.android.synthetic.main.reserve_store_date.storeName
import kotlinx.android.synthetic.main.reserve_store_time.*
import kotlinx.android.synthetic.main.sc_member_check_in_information.*
import java.text.SimpleDateFormat
import java.util.*

class ReserveDate : AppCompatActivity() {
    private var Database = FirebaseDatabase.getInstance().getReference()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.reserve_store_date)

        setActionBar()

        var idd = intent.getStringExtra("memberid1")

        val storeN = intent.getStringExtra("StoreName")

        val simpleDateFormat = SimpleDateFormat("dd/MM/yy")
        val databaseDate = SimpleDateFormat("yyyyMMdd")
        val c = Calendar.getInstance()
        val d = Calendar.getInstance()
        val today = simpleDateFormat.format(c.time)
        c.add(Calendar.DATE, 2)
        d.add(Calendar.DATE, 1)
        val future2 = simpleDateFormat.format(c.time)
        val future1 = simpleDateFormat.format(d.time)

        val pic = intent.getStringExtra("StorePic")
        Picasso.with(this@ReserveDate).load(pic).into(imageView)

        storeName.text = storeN

        val futureDate = listOf(today, future1.toString(), future2.toString())

        dateList.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, futureDate)

        val query = Database.child("Store").orderByChild("Store_Name").equalTo(storeN)
        query.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(s0: DataSnapshot) {
                var idd = intent.getStringExtra("memberid1")
                try {
                    for (s0 in s0.children) {
                        val storeId = s0.key.toString()

                        dateList.setOnItemClickListener { parent, view, position, id ->
                            val pls: Date = simpleDateFormat.parse(futureDate[position])
                            val output = databaseDate.format(pls)

                            val i = Intent(
                                this@ReserveDate,
                                ReserveTime::class.java
                            )
                            i.putExtra("ReserveDate", output)
                            i.putExtra("storeName", storeN)
                            i.putExtra("storeId", storeId)
                            i.putExtra("storePic", pic)
                            i.putExtra("memberid2", idd)
                            startActivity(i)
                        }
                    }
                } catch (e: Exception) {
                    Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var idd = intent.getStringExtra("memberid1")

        val intent1 = Intent(this, ReserveStore_List::class.java).apply {
            putExtra("memberid", idd)
        }
        startActivity(intent1)
        return false
    }

    private fun setActionBar(){
        val actionBar: ActionBar? = supportActionBar
        actionBar!!.title = "Reserve Date"
        actionBar!!.setDisplayHomeAsUpEnabled(true)
    }
}

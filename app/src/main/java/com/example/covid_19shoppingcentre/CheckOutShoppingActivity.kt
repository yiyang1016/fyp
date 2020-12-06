package com.example.covid_19shoppingcentre

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.check_out_shopping_page.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CheckOutShoppingActivity: AppCompatActivity() {
    private var userDatabase = FirebaseDatabase.getInstance().getReference()
    private  var database = FirebaseDatabase.getInstance().getReference("Member")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.check_out_shopping_page)

        val currentDateTime  = LocalDateTime.now()
        val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd")
        val dateText = currentDateTime.format(dateFormat)
        val hourMinuteFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a")
        val hourMinuteText = currentDateTime.format(hourMinuteFormat)

        val dataSent = intent.getStringExtra("EXTRA_MESSAGE")
        val memberI = intent.getStringExtra("memberID")

        var query1 = userDatabase.child("ShoppingCentre").child(dateText.toString()).child(memberI.toString())
        query1.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(s0: DataSnapshot) {
                if (s0.exists()){
                    var query = userDatabase.child("ShoppingCentre").child(dateText.toString()).child(memberI.toString())
                    query.child("checkOutTime").setValue(hourMinuteText.toString())
                    query.child("status").setValue(dataSent.toString()).addOnCompleteListener {

                        val refSearch = FirebaseDatabase.getInstance().getReference().child("Member")
                            .orderByChild("Id").equalTo(dataSent)
                        refSearch.addListenerForSingleValueEvent(object : ValueEventListener {
                            override fun onCancelled(error: DatabaseError) {
                                val text = "Connection Failed"
                                Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
                            }

                            override fun onDataChange(p0: DataSnapshot) {
                                if (p0.exists()) {
                                    for (p0 in p0.children) {
                                        database.child(dataSent).child("DistanceScoreStatus").setValue(0)
                                    }
                                } else {
//                                    Toast.makeText(applicationContext, "Member Missing", Toast.LENGTH_SHORT)
//                                        .show()
                                }
                            }
                        })
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "ERROR", Toast.LENGTH_SHORT).show()
            }
        })

        okBtn.setOnClickListener{
            val intent1 = Intent(this, MainActivity::class.java).apply {
                putExtra("MemberID", memberI)
            }
            startActivity(intent1)
        }
    }

    override fun onBackPressed() {
        val memberI = intent.getStringExtra("memberID")

        val intent1 = Intent(this, MainActivity::class.java).apply {
            putExtra("MemberID", memberI)
        }
        startActivity(intent1)
    }
}
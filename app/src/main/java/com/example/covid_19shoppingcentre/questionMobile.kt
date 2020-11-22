package com.example.covid_19shoppingcentre

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.firebase.ui.database.FirebaseRecyclerAdapter
import kotlinx.android.synthetic.main.question_mobile.*
import android.widget.Button
import android.widget.Toast
import com.google.firebase.database.*

class questionMobile : AppCompatActivity() {

    private var userDatabase = FirebaseDatabase.getInstance().getReference("Member")
    private var Database = FirebaseDatabase.getInstance().getReference()
    lateinit var mDatabase: DatabaseReference
    lateinit var FirebaseRecyclerAdapter: FirebaseRecyclerAdapter<Store, Store_List.StoreViewHolder>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.question_mobile)

        mDatabase = FirebaseDatabase.getInstance().getReference();

        var score : Int = 0
        var q1Status : Boolean = false
        var q2Status : Boolean = false
        var q3Status : Boolean = false

        questionNextButton.setOnClickListener{
            if(cbCough.isChecked){
                score += 3
                q1Status = true
            }
            if(cbSoreThroat.isChecked){
                score += 3
                q1Status = true
            }
            if(cbFever.isChecked){
                score += 3
                q1Status = true
            }
            if(cbHead.isChecked){
                score += 3
                q1Status = true
            }
            if(cbBreath.isChecked){
                score += 3
                q1Status = true
            }
            if(cbNo.isChecked){
                q1Status = true
            }
            if(rbQ2Yes.isChecked){
                score += 2
                q2Status = true
            }
            if(rbQ2No.isChecked){
                q2Status = true
            }
            if(rbQ3Yes.isChecked){
                score += 4
                q3Status = true
            }
            if(rbQ3No.isChecked){
                q3Status = true
            }

            var getMemberId = intent.getStringExtra("MemberID")
            var checkUser: Query = userDatabase.orderByKey().equalTo(getMemberId)

                if(q1Status){
                    if(q2Status){
                        if(q3Status){
                            if(score >= 5){
                                mDatabase.child("Member").child(getMemberId).child("HealthyStatus").setValue("Danger")
                                Toast.makeText(applicationContext, "Your are Dangerous, Please went to Hospital for a Check", Toast.LENGTH_SHORT)
                                    .show()

                                checkUser.addListenerForSingleValueEvent(object : ValueEventListener {
                                    override fun onCancelled(error: DatabaseError) {
                                        Toast.makeText(applicationContext, "Error",Toast.LENGTH_SHORT).show()
                                    }

                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if(snapshot.exists()){
                                            var role = snapshot.child(getMemberId).child("Role").value.toString()

                                            if(role == "staff"){
                                                val i = Intent(this@questionMobile, StaffMainActivity::class.java)
                                                i.putExtra("MemberID", getMemberId )
                                                startActivity(i)
                                            } else if (role == "member") {
                                                val i = Intent(this@questionMobile, MainActivity::class.java)
                                                i.putExtra("MemberID", getMemberId)
                                                startActivity(i)
                                            }
                                        }else
                                            Toast.makeText(applicationContext, "Member ID doesn't exists $getMemberId",Toast.LENGTH_SHORT).show()
                                    }
                                })
                            } else {
                                mDatabase.child("Member").child(getMemberId).child("HealthyStatus")
                                    .setValue("Safety")
                                Toast.makeText(
                                    applicationContext,
                                    "Your are Safety",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()

                                checkUser.addListenerForSingleValueEvent(object :
                                    ValueEventListener {
                                    override fun onCancelled(error: DatabaseError) {
                                        Toast.makeText(
                                            applicationContext,
                                            "Error",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        if (snapshot.exists()) {
                                            var role = snapshot.child(getMemberId)
                                                .child("Role").value.toString()

                                            if (role == "staff") {
                                                val i = Intent(
                                                    this@questionMobile,
                                                    StaffMainActivity::class.java
                                                )
                                                i.putExtra("MemberID", getMemberId)
                                                startActivity(i)
                                            } else if (role == "member") {
                                                val i = Intent(
                                                    this@questionMobile,
                                                    MainActivity::class.java
                                                )
                                                i.putExtra("MemberID", getMemberId)
                                                startActivity(i)
                                            }
                                        } else
                                            Toast.makeText(
                                                applicationContext,
                                                "Member ID doesn't exists $getMemberId",
                                                Toast.LENGTH_SHORT
                                            ).show()
                                    }
                                })
                            }
                        }else{
                            Toast.makeText(applicationContext, "Please Answer Q3", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }else{
                        Toast.makeText(applicationContext, "Please Answer Q2", Toast.LENGTH_SHORT)
                            .show()
                    }
                }else{
                    Toast.makeText(applicationContext, "Please Answer Q1", Toast.LENGTH_SHORT)
                        .show()
                }

        }
    }
}
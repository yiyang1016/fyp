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

        questionNextButton.setOnClickListener{
            if(cbCough.isChecked){
                score += 3
            }
            if(cbSoreThroat.isChecked){
                score += 3
            }
            if(cbFever.isChecked){
                score += 3
            }
            if(cbHead.isChecked){
                score += 3
            }
            if(cbBreath.isChecked){
                score += 3
            }
            if(rbQ2Yes.isChecked){
                score += 2
            }
            if(rbQ3Yes.isChecked){
                score += 4
            }

            var getMemberId = intent.getStringExtra("MemberID")

            if(score >= 5){
                mDatabase.child("Member").child(getMemberId).child("HealthyStatus").setValue("Danger")
                Toast.makeText(applicationContext, "Your are Dangerous, Please went to Hospital for a Check", Toast.LENGTH_SHORT)
                    .show()
            }else{
                mDatabase.child("Member").child(getMemberId).child("HealthyStatus").setValue("Safety")
                Toast.makeText(applicationContext, "Your are Safety", Toast.LENGTH_SHORT)
                    .show()
            }

            var checkUser: Query = userDatabase.orderByKey().equalTo(getMemberId)

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
        }
    }
}
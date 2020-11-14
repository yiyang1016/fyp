package com.example.covid_19shoppingcentre

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.question_mobile.*
import android.widget.Button
import android.widget.Toast

class questionMobile : AppCompatActivity() {

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

            var memberID : String = "M00001"

            if(score >= 5){
                mDatabase.child("Member").child(memberID).child("HealthyStatus").setValue("Danger")
                Toast.makeText(applicationContext, "Your are Dangerous, Please went to Hospital for a Check", Toast.LENGTH_SHORT)
                    .show()
            }else{
                mDatabase.child("Member").child(memberID).child("HealthyStatus").setValue("Safety")
                Toast.makeText(applicationContext, "Your are Safety", Toast.LENGTH_SHORT)
                    .show()
            }

            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }
}
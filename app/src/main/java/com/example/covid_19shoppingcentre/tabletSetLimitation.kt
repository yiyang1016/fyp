package com.example.covid_19shoppingcentre

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.firebase.ui.database.FirebaseRecyclerAdapter
import kotlinx.android.synthetic.main.question_mobile.*
import android.widget.Button
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.tablet_set_limitation.*

class tabletSetLimitation : AppCompatActivity() {

    private var Database = FirebaseDatabase.getInstance().getReference()
    lateinit var mDatabase: DatabaseReference
    lateinit var nDatabase: DatabaseReference
    lateinit var FirebaseRecyclerAdapter: FirebaseRecyclerAdapter<Store, Store_List.StoreViewHolder>

    lateinit var storeID: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tablet_set_limitation)

        mDatabase = FirebaseDatabase.getInstance().getReference();
        nDatabase = FirebaseDatabase.getInstance().getReference("Store");

        storeID = "ST00001"

        val ref = nDatabase.child(storeID)
        ref.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    if(!p0.child("Store_Limitation").value.toString().isNullOrEmpty()){
                        tvCurrentNumerLimit.setText(p0.child("Store_Limitation").value.toString())
                    }else{
                        tvCurrentNumerLimit.setText("Empty")
                    }
                } else {
                    Toast.makeText(applicationContext, "Error: Can Not Access Data", Toast.LENGTH_SHORT)
                        .show()
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(applicationContext, "00", Toast.LENGTH_SHORT).show()
            }
        })

        btnComfirmChangeLimit.setOnClickListener{
            mDatabase.child("Store").child(storeID).child("Store_Limitation").setValue(etNewNumber.text.toString())
            Toast.makeText(applicationContext, "Successful Edit", Toast.LENGTH_SHORT)
                .show()

            val intent = Intent(
                this@tabletSetLimitation,
                tabletStoreCurrentCust::class.java
            )
            startActivity(intent)
        }



    }
}
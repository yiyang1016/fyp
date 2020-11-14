package com.example.covid_19shoppingcentre

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.tablet_store_login_varification.*

abstract class tabletStoreLoginVarification : AppCompatActivity() {

    private var Database = FirebaseDatabase.getInstance().getReference()
    lateinit var mDatabase : DatabaseReference
    lateinit var FirebaseRecyclerAdapter : FirebaseRecyclerAdapter<Store, Store_List.StoreViewHolder>

    lateinit var storeID : String
    lateinit var password : String

    private var booleanStatus : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tablet_store_login_varification)

        mDatabase = FirebaseDatabase.getInstance().getReference("Store")

        storeID = tfStoreID.text.toString()
        password = tfPasswordStore.text.toString()

        val btnStoreLogin = findViewById<Button>(R.id.btnStoreLogin)

        fun readData() {
            val findDate = "Store"

            //Find Firebase's file location
            val ref = mDatabase.child(findDate).equalTo(storeID)

            ref.addValueEventListener(object : ValueEventListener {
                override fun onCancelled(error: DatabaseError) {
                    val text = "Connection Failed"
                    Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
                }

                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.exists()) {
                        if (password.equals(p0.child("Store_Password").value.toString())) {
                            booleanStatus = true
                        }
                    }else{
                        val text = "Incorect Store ID"
                        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
                    }
                }
            })
        }

        btnStoreLogin.setOnClickListener{
            readData()
            if(booleanStatus){
                val intent = Intent(this, tabletStoreCurrentCust::class.java)
                startActivity(intent)
            }else{
                val text = "Incorect Password"
                Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
            }
        }

    }
}
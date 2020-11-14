package com.example.covid_19shoppingcentre

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.tablet_store_login_varification.*

class tabletStoreLoginVarification : AppCompatActivity() {

    private var Database = FirebaseDatabase.getInstance().getReference()
    lateinit var mDatabase: DatabaseReference
    lateinit var FirebaseRecyclerAdapter: FirebaseRecyclerAdapter<Store, Store_List.StoreViewHolder>

    lateinit var storeID: String
    lateinit var password: String

    private var booleanStatus: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tablet_store_login_varification)

        mDatabase = FirebaseDatabase.getInstance().getReference("Store")

        val btnStoreLogin = findViewById<Button>(R.id.btnStoreLogin)

        btnStoreLogin.setOnClickListener {
            storeID = tfStoreID.text.toString()
            password = tfPasswordStore.text.toString()

            val ref = mDatabase.child(storeID)
            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(p0: DataSnapshot) {
                    if (p0.child("Store_Password").value.toString() == password) {
                        Toast.makeText(applicationContext, "win", Toast.LENGTH_SHORT).show()
                        val intent = Intent(
                            this@tabletStoreLoginVarification,
                            tabletStoreCurrentCust::class.java
                        )
                        startActivity(intent)
                    } else {
                        Toast.makeText(applicationContext, "wrong password", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(applicationContext, "00", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}
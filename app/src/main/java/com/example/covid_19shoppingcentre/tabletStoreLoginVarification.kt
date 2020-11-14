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
    lateinit var mDatabase : DatabaseReference
    lateinit var FirebaseRecyclerAdapter : FirebaseRecyclerAdapter<Store, Store_List.StoreViewHolder>

    lateinit var storeID : String
    lateinit var password : String

    private var booleanStatus : Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tablet_store_login_varification)

        mDatabase = FirebaseDatabase.getInstance().getReference("Store")

        val btnStoreLogin = findViewById<Button>(R.id.btnStoreLogin)

        btnStoreLogin.setOnClickListener{
            storeID = tfStoreID.text.toString()
            password = tfPasswordStore.text.toString()
            readData()
            if(booleanStatus){
                //val intent = Intent(this, tabletStoreCurrentCust::class.java)
                //startActivity(intent)
                booleanStatus = false
            }else{
                val text = "Incorect Store ID or Password"
                Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun readData() {
        val findDate = "Store"
        storeID = "ST00001"
        //Find Firebase's file location
        val ref = mDatabase.child(storeID).child("Store_Password").equalTo(password)
        val text = "4"
        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(p0: DataSnapshot) {
                Toast.makeText(applicationContext, "00", Toast.LENGTH_SHORT).show()
                if (p0.exists()) {
                    //val text = "3"
                    //Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
                    //if (password.equals(p0.value.toString())) {
                    //    booleanStatus = true
                        val text = "1"
                        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
                }else{
                        Toast.makeText(applicationContext, "Error", Toast.LENGTH_SHORT).show()
                    }
                }
            override fun onCancelled(error: DatabaseError) {
                val text = "Connection Failed"
                Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
            }
        })
    }
}
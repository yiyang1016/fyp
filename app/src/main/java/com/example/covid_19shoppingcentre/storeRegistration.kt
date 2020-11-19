package com.example.covid_19shoppingcentre

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import android.widget.Button
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.covid_19shoppingcentre.models.AddStoreClass
import com.example.covid_19shoppingcentre.models.addShoppingCentreCheckIn
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_store__list.*
import kotlinx.android.synthetic.main.store_registration.*

class storeRegistration : AppCompatActivity() {

    private var Database = FirebaseDatabase.getInstance().getReference()
    lateinit var nDatabase: DatabaseReference
    lateinit var FirebaseRecyclerAdapter: FirebaseRecyclerAdapter<Store, Store_List.StoreViewHolder>
    private var userDatabase = FirebaseDatabase.getInstance().getReference()
    private lateinit var database: DatabaseReference

    lateinit var storeID: String
    lateinit var name : String
    lateinit var des : String
    lateinit var pass : String
    lateinit var floor : String
    lateinit var slot : String
    lateinit var limit : String
    private var count : Long = 0
    lateinit private var numberIndentity : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.store_registration)

        nDatabase = FirebaseDatabase.getInstance().getReference("Store");

        txtStoreName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                btnUpPic.visibility = View.VISIBLE
            }
            override fun afterTextChanged(p0: Editable?) {
            }
        })


        btnUpPic.setOnClickListener{
            name = txtStoreName.getText().toString()

            val i = Intent(
                this@storeRegistration,
                uploadStorePicture::class.java
            )
            i.putExtra("regisStoreName", name)
            startActivity(i)
        }

        btnAddStoreComfirm.setOnClickListener{

            val query = userDatabase.child("Store")
            query.addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onDataChange(s0: DataSnapshot) {
                    count = s0.childrenCount
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        applicationContext,
                        "Data Access Error",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })

            Toast.makeText(
                applicationContext,
                count.toString(),
                Toast.LENGTH_SHORT
            ).show()

            count = count + 1
            if(count/10 >= 1){
                numberIndentity = "000"
            }else if(count/ 100 >= 1){
                numberIndentity = "00"
            }else if(count/ 1000 >= 1){
                numberIndentity = ""
            }else{
                numberIndentity = "0000"
            }

            val picurl = intent.getStringExtra("PictureURL")
            storeID = "ST"+numberIndentity.toString()+count.toString()
            name = txtStoreName.getText().toString()
            des = txtStoreDes.getText().toString()
            floor = txtStoreFloor.getText().toString()
            pass = txtStorePass.getText().toString()
            slot = txtStoreSlot.getText().toString()
            limit = txtStoreLimit.getText().toString()

            Toast.makeText(
                applicationContext,
                storeID.toString(),
                Toast.LENGTH_SHORT
            ).show()

            //val writeStoreIn = AddStoreClass(name, des, pass, floor, slot, limit, picurl)
            /*val intent2 = Intent(this, MainActivity::class.java).apply {
                putExtra("EXTRA_MESSAGE", "message")
            }*/

            if(count >1) {

                database.child("Store").child(storeID).child("Store_Name").setValue(name)
                database.child("Store").child(storeID).child("Store_Description").setValue(des)
                database.child("Store").child(storeID).child("Store_Password").setValue(pass)
                database.child("Store").child(storeID).child("Store_Floor").setValue(floor)
                database.child("Store").child(storeID).child("Store_Slot").setValue(slot)
                database.child("Store").child(storeID).child("Store_Limitation").setValue(limit)
                database.child("Store").child(storeID).child("Store_Image").setValue(picurl)

                /*Toast.makeText(
                    applicationContext,
                    "New Store is Added",
                    Toast.LENGTH_SHORT
                ).show()
                startActivity(intent2)*/
            }
        }
    }
}
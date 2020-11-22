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
import kotlinx.android.synthetic.main.member_login.*
import kotlinx.android.synthetic.main.store_registration.*

class storeRegistration : AppCompatActivity() {

    private var Database = FirebaseDatabase.getInstance().getReference("Store")
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
            pass = txtPassword.getText().toString()
            des = txtStoreDes.getText().toString()
            floor = txtStoreFloor.getText().toString()
            slot = txtStoreSlot.getText().toString()
            limit = txtStoreLimit.getText().toString()


            val i = Intent(
                this@storeRegistration,
                uploadStorePicture::class.java
            )
            i.putExtra("regisStoreName", name)
            i.putExtra("regisPass", pass)
            i.putExtra("regisDes", des)
            i.putExtra("regisFloor", floor)
            i.putExtra("regisSlot", slot)
            i.putExtra("regisLimit", limit)

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
            /*name = txtStoreName.getText().toString()
            des = txtStoreDes.getText().toString()
            floor = txtStoreFloor.getText().toString()
            pass = txtStorePass.getText().toString()
            slot = txtStoreSlot.getText().toString()
            limit = txtStoreLimit.getText().toString()*/

            Toast.makeText(
                applicationContext,
                storeID.toString(),
                Toast.LENGTH_SHORT
            ).show()

            //val writeStoreIn = AddStoreClass(name, des, pass, floor, slot, limit, picurl)
            val intent2 = Intent(this, MainActivity::class.java).apply {
                putExtra("EXTRA_MESSAGE", "message")
            }

            if(count >1) {
                if (txtStoreName.getText().toString() == "") {
                    Toast.makeText(applicationContext, "Plaese Enter Store Name", Toast.LENGTH_SHORT).show()
                }
                else if (txtStoreDes.getText().toString() == ""){
                    Toast.makeText(applicationContext, "Plaese Enter Store Description", Toast.LENGTH_SHORT).show()
                }
                else if (txtStorePass.getText().toString() == ""){
                    Toast.makeText(applicationContext, "Plaese Enter Store Password", Toast.LENGTH_SHORT).show()
                }
                else if (txtStoreFloor.getText().toString() == ""){
                    Toast.makeText(applicationContext, "Plaese Enter Store Floor", Toast.LENGTH_SHORT).show()
                }
                else if (txtStoreSlot.getText().toString() == ""){
                    Toast.makeText(applicationContext, "Plaese Enter Store Slot", Toast.LENGTH_SHORT).show()
                }
                else if (txtStoreLimit.getText().toString() == ""){
                    Toast.makeText(applicationContext, "Plaese Enter Store Name", Toast.LENGTH_SHORT).show()
                }
                else if (picurl == "") {
                    Toast.makeText(applicationContext, "Plaese Upload Store Picture", Toast.LENGTH_SHORT).show()
                }
                else if (txtStorePass.getText().toString().length < 5) {
                    Toast.makeText(applicationContext, "Password Should more then 5 digit", Toast.LENGTH_SHORT).show()
                }
                else if (txtStoreSlot.getText().toString().length != 5){
                    Toast.makeText(applicationContext, "Please Enter corrent Slot ID", Toast.LENGTH_SHORT).show()
                }
                else if (txtStoreSlot.getText().toString().length != 5){
                    Toast.makeText(applicationContext, "Please Enter corrent Slot ID", Toast.LENGTH_SHORT).show()
                }
                else if (txtStoreFloor.getText().toString() != "G" && txtStoreFloor.getText().toString() != "F1" && txtStoreFloor.getText().toString() != "F2"){
                    Toast.makeText(applicationContext, "Please Enter corrent Floor", Toast.LENGTH_SHORT).show()
                }
                else {
                    Database.child(storeID).child("Store_Name")
                        .setValue(txtStoreName.getText().toString())
                    Database.child(storeID).child("Store_Description")
                        .setValue(txtStoreDes.getText().toString())
                    Database.child(storeID).child("Store_Password")
                        .setValue(txtStorePass.getText().toString())
                    Database.child(storeID).child("Store_Floor")
                        .setValue(txtStoreFloor.getText().toString())
                    Database.child(storeID).child("Store_Slot")
                        .setValue(txtStoreSlot.getText().toString())
                    Database.child(storeID).child("Store_Limitation")
                        .setValue(txtStoreLimit.getText().toString())
                    Database.child(storeID).child("Store_Image").setValue(picurl)
                    Toast.makeText(
                        applicationContext,
                        "New Store is Added",
                        Toast.LENGTH_SHORT
                    ).show()
                    startActivity(intent2)
                }
            }

        }
    }
}
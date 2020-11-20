package com.example.covid_19shoppingcentre

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.PersistableBundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.android.gms.common.util.Strings
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.member_login.*
import kotlinx.android.synthetic.main.member_registration.*
import kotlinx.android.synthetic.main.sc_checked_in_list.*
import kotlinx.android.synthetic.main.sc_checked_in_list_layout.view.*
import java.text.DateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class MemberRegister : AppCompatActivity() {
    //firebase
    private var userDatabase = FirebaseDatabase.getInstance().getReference("Member")
    private lateinit var  userID: String
    private var count=0
    private lateinit var  userPassword: String
    private lateinit var confirmPass: String
    private lateinit var  userName: String
    private lateinit var icNum: String
    private lateinit var email:String
    private lateinit var phone: String

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.member_registration)

        userDatabase.addListenerForSingleValueEvent( object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

            override fun onDataChange(snapshot: DataSnapshot) {

                var num: Int = 1
                println("First Processing the number $num")
                if (snapshot.exists()){
                    num += snapshot.childrenCount.toInt()
                    println("Processing the number $num")
                }
                println("Last Processing the number $num")
                if(num<10){
                    userID = "M0000$num"
                }else if(num<100){
                    userID = "M000$num"
                }else if(num<1000){
                    userID = "M00$num"
                }else if(count<10000){
                    userID = "M0$num"
                }else{
                    userID = "M$num"
                }
            }
        })
        btnCreate.setOnClickListener {
            userName = txtName.text.toString()
            icNum = txtICNumber.text.toString()
            email = txtEmailAddress.text.toString()
            userPassword = txtRegisPassword.text.toString()
            confirmPass = txtRegisConPassword.text.toString()
            phone = txtPhone.text.toString()
            var role = "staff"

            val intent1 = Intent(this@MemberRegister, MemberLogin::class.java).apply {
            }

            if (userName.isNotEmpty() && email.isNotEmpty() && userPassword.isNotEmpty() && confirmPass.isNotEmpty() && phone.isNotEmpty()){
                if (userPassword == confirmPass) {
                    if (icNum.length == 12) {
                        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            userDatabase.child(userID)
                                .setValue(Member(icNum, userID, userName, phone, email, confirmPass, role))
                            Toast.makeText(
                                applicationContext,
                                "YOUR USER ID IS $userID",
                                Toast.LENGTH_LONG
                            ).show()
                            startActivity(intent1)
                        } else {
                            txtEmailAddress.error = "Enter correct Email"
                        }
                    }else{
                        txtICNumber.error = "Enter correct Password"
                    }
                } else
                    Toast.makeText(
                        applicationContext,
                        "Please Enter the same password in both password field",
                        Toast.LENGTH_SHORT
                    ).show()
        }else
                Toast.makeText(
                    applicationContext,
                    "Please Fill Up all the field",
                    Toast.LENGTH_SHORT
                ).show()
        }

    }
}
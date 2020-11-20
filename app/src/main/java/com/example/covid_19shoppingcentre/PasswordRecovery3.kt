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
import kotlinx.android.synthetic.main.password_recovery_1.*
import kotlinx.android.synthetic.main.password_recovery_3.*
import kotlinx.android.synthetic.main.sc_checked_in_list.*
import kotlinx.android.synthetic.main.sc_checked_in_list_layout.view.*
import java.text.DateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class PasswordRecovery3 :AppCompatActivity(){
    //firebase
    private var userDatabase = FirebaseDatabase.getInstance().getReference("Member")
    private lateinit var email: String
    private lateinit var pass:String
    private lateinit var confirmPass:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.password_recovery_3)
        val intent1: Intent = intent
        val inten2 = Intent(this@PasswordRecovery3,MemberLogin::class.java)
        email = intent1.getStringExtra("MemberEmail")
        btnResetPassword.setOnClickListener{
            pass = txtResetPassword.text.toString()
            confirmPass = txtResetConfirmPassword.text.toString()
            userDatabase.addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    var returnAddress: String
                    Loop@for(i in snapshot.children){
                        returnAddress = i.child("Email").value.toString()
                        if(returnAddress == email){
                            if(pass == confirmPass){
                                userDatabase.child(i.toString()).child("Password").setValue(pass)
                                Toast.makeText(applicationContext, "Password Reset Successfully!!!",Toast.LENGTH_SHORT).show()

                            }else{
                                Toast.makeText(applicationContext, "Please Enter the same password in both password field",Toast.LENGTH_SHORT).show()
                            }
                            break@Loop
                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
            startActivity(inten2)
        }

    }
}
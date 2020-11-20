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
import kotlinx.android.synthetic.main.sc_checked_in_list.*
import kotlinx.android.synthetic.main.sc_checked_in_list_layout.view.*
import java.text.DateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class PasswordRecovery1 :AppCompatActivity(){
    //firebase
    private var userDatabase = FirebaseDatabase.getInstance().getReference("Member")
    private lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.password_recovery_1)
        var foundEmail = 0
        btnRecover1.setOnClickListener{
            email = recoverEmail.text.toString()
            userDatabase.addValueEventListener(object: ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    var returnAddress: String
                    Loop@for(i in snapshot.children){
                        returnAddress = i.child("Email").value.toString()
                        if(returnAddress == email){
                            Toast.makeText(applicationContext, "Success",Toast.LENGTH_SHORT).show()
                            foundEmail+1;
                            break@Loop
                        }else{

                        }
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
            if(foundEmail>0) {
                //GO TO SECOND PAGE
                val i = Intent(this@PasswordRecovery1, PasswordRecovery2::class.java)
                i.putExtra("MemberEmail", email)
                startActivity(i)
            }else{
                Toast.makeText(applicationContext, "Please Enter Correct Email Address",Toast.LENGTH_SHORT).show()
            }
        }
    }

}
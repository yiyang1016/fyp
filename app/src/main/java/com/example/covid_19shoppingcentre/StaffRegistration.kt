package com.example.covid_19shoppingcentre

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.staff_registration.*

class StaffRegistration : AppCompatActivity() {

    private var userDatabase = FirebaseDatabase.getInstance().getReference("Member")
    private lateinit var  userID: String
    private var count=0
    private lateinit var  userPassword: String
    private lateinit var confirmPass: String
    private lateinit var  userName: String
    private lateinit var icNum: String
    private lateinit var email:String
    private lateinit var phone: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.staff_registration)

        userDatabase.addListenerForSingleValueEvent( object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    applicationContext,
                    "Data Access Error",
                    Toast.LENGTH_SHORT
                ).show()
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
        btnStaffCreate.setOnClickListener {
            userName = txtStaffName.text.toString()
            icNum = txtStaffICNumber.text.toString()
            email = txtStaffEmailAddress.text.toString()
            userPassword = txtStaffRegisPassword.text.toString()
            confirmPass = txtStaffRegisConPassword.text.toString()
            phone = txtStaffPhone.text.toString()
            var role = "staff"

            val intent1 = Intent(this@StaffRegistration, StaffMainActivity::class.java).apply {
            }

            if (userName.isNotEmpty() && email.isNotEmpty() && userPassword.isNotEmpty() && confirmPass.isNotEmpty() && phone.isNotEmpty()){
                if (userPassword == confirmPass) {
                    if (icNum.length == 14) {
                        if (android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                            userDatabase.child(userID).child("IC_Number").setValue(icNum)
                            userDatabase.child(userID).child("Id").setValue(userID)
                            userDatabase.child(userID).child("Name").setValue(userName)
                            userDatabase.child(userID).child("PhoneNumber").setValue(phone)
                            userDatabase.child(userID).child("Email").setValue(email)
                            userDatabase.child(userID).child("Password").setValue(confirmPass)
                            userDatabase.child(userID).child("Role").setValue(role)
                            Toast.makeText(
                                applicationContext,
                                "YOUR USER ID IS $userID",
                                Toast.LENGTH_LONG
                            ).show()
                            startActivity(intent1)
                        } else {
                            txtStaffEmailAddress.error = "Enter correct Email"
                        }
                    }else{
                        txtStaffICNumber.error = "Enter correct Password"
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
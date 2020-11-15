package com.example.covid_19shoppingcentre

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.firebase.ui.database.FirebaseRecyclerAdapter
import kotlinx.android.synthetic.main.question_mobile.*
import android.widget.Button
import android.widget.Toast
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.tablet_store_current_cust.*

class tabletStoreCurrentCust : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tablet_store_current_cust)



        btnSetLimit.setOnClickListener{

            val intent = Intent(
                this@tabletStoreCurrentCust,
                tabletSetLimitation::class.java
            )
            startActivity(intent)
        }
    }
}
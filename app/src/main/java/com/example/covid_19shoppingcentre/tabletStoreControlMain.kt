package com.example.covid_19shoppingcentre

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.staff_main.*
import kotlinx.android.synthetic.main.tablet_store_control_main.*

class tabletStoreControlMain : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.tablet_store_control_main)

        logoImgStoreControl.setImageDrawable(resources.getDrawable(R.drawable.logo))

        btnRegisterNewStore.setOnClickListener{
            val intent = Intent(this, storeRegistration::class.java).apply {
                putExtra("EXTRA_MESSAGE", "message")
            }
            startActivity(intent)
        }

        btnSetStoreLimit.setOnClickListener{
            val intent = Intent(this, tabletStoreLoginVarification::class.java).apply {
                putExtra("EXTRA_MESSAGE", "message")
            }

            startActivity(intent)
        }
    }
}
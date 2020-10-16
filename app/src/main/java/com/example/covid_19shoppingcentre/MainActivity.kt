package com.example.covid_19shoppingcentre

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.customer_review.view.*
import kotlinx.android.synthetic.main.customer_review_submission.view.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    button.setOnClickListener{
        val intent = Intent(this, Store_List::class.java)
        startActivity(intent)
    }
        review.setOnClickListener{
            val dialogBuilder = AlertDialog.Builder(this@MainActivity, R.style.CustomAlertDialog)

            val view = layoutInflater.inflate(R.layout.customer_review, null)
            dialogBuilder.setView(view)

            val alertDialog = dialogBuilder.create()
            dialogBuilder.show()

            view.submitBtn.setOnClickListener{
                //val dialogBuilder = AlertDialog.Builder(this@MainActivity, R.style.CustomAlertDialog)

                //val view = layoutInflater.inflate(R.layout.customer_review_submission, null)
                //dialogBuilder.setView(view)

                //val alertDialog = dialogBuilder.create()
                //dialogBuilder.show()

                //view.okBtn.setOnClickListener{
                //    alertDialog.dismiss()
                    alertDialog.dismiss()
                }
            }
        }
    }

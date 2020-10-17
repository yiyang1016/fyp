package com.example.covid_19shoppingcentre

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.customer_review.view.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        button.setOnClickListener {
            val intent = Intent(this, Store_List::class.java)
            startActivity(intent)
        }
        review.setOnClickListener {
            val dialogBuilder = AlertDialog.Builder(this@MainActivity, R.style.CustomAlertDialog)

            val view = layoutInflater.inflate(R.layout.customer_review, null)
            dialogBuilder.setView(view)

            val alertDialog = dialogBuilder.create()

            dialogBuilder.setPositiveButton("Submit"){ dialogInterface, i ->
                val ref = FirebaseDatabase.getInstance().getReference("Review")
                var reviewId = ""
                val refSearch = FirebaseDatabase.getInstance().getReference("Review").orderByKey().limitToLast(1)

                refSearch.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(error: DatabaseError) {
                        val text = "Connection Failed"
                        Toast.makeText(applicationContext, text, Toast.LENGTH_SHORT).show()
                    }

                    override fun onDataChange(p0: DataSnapshot) {
                        if (p0.exists()) {
                            for (p0 in p0.children) {
                                val reviewsId = p0.getValue(ReviewIdClass::class.java)
                                reviewId = reviewsId.toString()
                            }

                            val cal = ((reviewId.substring(2, 7)).toInt()) + 1
                            val newId = "RV0000" + cal.toString()
                            Toast.makeText(
                                this@MainActivity, newId, Toast.LENGTH_SHORT
                            ).show().toString()

                            val data = Review(
                                newId,
                                view.review1.rating.toInt(),
                                view.review2.rating.toInt(),
                                view.review3.rating.toInt(),
                                view.review4.rating.toInt(), ""
                            )

                            ref.child(newId).setValue(data).addOnCompleteListener {
                                Toast.makeText(
                                    this@MainActivity,
                                    "Thanks for your submission!", Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                })
            }
            dialogBuilder.show()

        }

    }
}

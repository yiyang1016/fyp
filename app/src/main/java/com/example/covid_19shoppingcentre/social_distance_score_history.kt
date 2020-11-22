package com.example.covid_19shoppingcentre

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.database.FirebaseRecyclerAdapter
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.social_distance_history_layout.view.*
import android.graphics.Color
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBar

class social_distance_score_history : AppCompatActivity() {
    lateinit var mDatabase : DatabaseReference
    lateinit var  mRecyclerView: RecyclerView
    lateinit var FirebaseRecyclerAdapter : FirebaseRecyclerAdapter<Score, ScoreViewHolder>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_social_distance_score_history)
        setActionBar()
        val id = intent.getStringExtra("MemberID")
        val dialogBuilder = AlertDialog.Builder(this@social_distance_score_history, R.style.CustomAlertDialog)

        val view = layoutInflater.inflate(R.layout.distance_rule, null)
        dialogBuilder.setView(view)
        dialogBuilder.setPositiveButton(
            "OK"
        ) { _, _ ->}
        val alertDialog = dialogBuilder.create()
        alertDialog.show()

        mRecyclerView = findViewById(R.id.listView)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.setLayoutManager(LinearLayoutManager(this))
        mRecyclerView.addItemDecoration(DividerItemDecoration(this.applicationContext, DividerItemDecoration.VERTICAL))

        mDatabase = FirebaseDatabase.getInstance().getReference("SocialDistanceScore")
        //Pass in member id
        logRecycleView(id)
    }

    private fun logRecycleView(memberId : String){
        val firebaseSearchQuery = mDatabase.orderByChild("member_Id").startAt(memberId).endAt(memberId+"\uf8ff")
        FirebaseRecyclerAdapter = object : FirebaseRecyclerAdapter<Score, ScoreViewHolder>(
            Score::class.java,
            R.layout.social_distance_history_layout,
            ScoreViewHolder::class.java,
            firebaseSearchQuery
        ){
            override fun populateViewHolder(p0: ScoreViewHolder, p1: Score, p2: Int) {

                p0.mView.title.setText(p1.Title)
                if(Integer.parseInt(p1.Score.toString())!= 100){
                    p0.mView.score.setText("-"+p1.Score.toString())
                    p0.mView.score.setTextColor(Color.RED)
                }else{
                    p0.mView.score.setText(p1.Score.toString())
                    p0.mView.score.setTextColor(Color.GREEN)
                }
                p0.mView.date.setText(p1.Score_Date)
            }
        }
        mRecyclerView.adapter = FirebaseRecyclerAdapter
    }

    class ScoreViewHolder( var mView: View) : RecyclerView.ViewHolder(mView) {
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        //Inflate the menu; this adds items to the action bar if it is present
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //Handle action bar item clicks here.
        //The action bar will automatically handle clicks on the Home/Up button, so long
        //as you specify a parent activity in AndroidManfest.xml
        val id = intent.getStringExtra("MemberID")

        val intent1 = Intent(this, MainActivity::class.java).apply {
            putExtra("MemberID", id)
        }
        startActivity(intent1)
         when (item.itemId){
            R.id.rule ->{
                val dialogBuilder = AlertDialog.Builder(this@social_distance_score_history, R.style.CustomAlertDialog)

                val view = layoutInflater.inflate(R.layout.distance_rule, null)
                dialogBuilder.setView(view)
                dialogBuilder.setPositiveButton(
                    "OK"
                ) { _, _ ->}
                val alertDialog = dialogBuilder.create()
                alertDialog.show()
                return true
            }
            else -> super.onOptionsItemSelected(item)
        }
        return false
    }
    private fun setActionBar(){
        val actionBar: ActionBar? = supportActionBar
        actionBar!!.title = "Score History"
        actionBar!!.setDisplayHomeAsUpEnabled(true)
    }
}
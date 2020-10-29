package com.example.covid_19shoppingcentre

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.example.covid_19shoppingcentre.Common.Common
import com.example.covid_19shoppingcentre.Remote.IGoogleAPIService
import com.example.covid_19shoppingcentre.models.PlaceDetail
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_view_place.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.StringBuilder

class ViewPlace : AppCompatActivity() {

    internal lateinit var mService:IGoogleAPIService

    var mPlace:PlaceDetail?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_place)

        //Init Service
        mService = Common.googleApiService

        //Set empty for all text view
        place_name.text=""
        place_address.text=""
        place_open_hour.text=""

        btn_show_map.setOnClickListener{
            val mapIntent = Intent(Intent.ACTION_VIEW, Uri.parse(mPlace!!.result!!.url!!))
            startActivity(mapIntent)
        }

        btn_view_direction.setOnClickListener{
            val viewDirections = Intent(this@ViewPlace,ViewDirections::class.java)
            startActivity(viewDirections)
        }
        //Load photo of place
        if(Common.currentResult!!.photos!=null&&Common.currentResult!!.photos!!.size>0)
            Picasso.with(this)
                .load(getPhotoOfPlace(Common.currentResult!!.photos!![0].photo_reference!!,1000))
                .into(photo)

        //Load Rating
        if(Common.currentResult!!.rating !=null)
            rating_bar.rating = Common.currentResult!!.rating.toFloat()
        else
            rating_bar.visibility = View.GONE

        //Load open hours
        if(Common.currentResult!!.opening_hours!=null)
            place_open_hour.text="Open Now : "+ Common.currentResult!!.opening_hours!!.open_now
        else
            place_open_hour.visibility = View.GONE

        //Use Service to fetch address and Name
        mService.getDetailPlace(getPlaceDetailUrl(Common.currentResult!!.place_id!!))
            .enqueue(object : retrofit2.Callback<PlaceDetail>{
                override fun onFailure(call: Call<PlaceDetail>?, t: Throwable?) {
                   Toast.makeText(baseContext,""+t!!.message,Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<PlaceDetail>, response: Response<PlaceDetail>) {
                    mPlace = response.body()

                    place_address.text= mPlace!!.result!!.formatted_address
                    place_name.text = mPlace!!.result!!.name

                }

            })

    }

    private fun getPlaceDetailUrl(placeId: String): String {
        val url=StringBuilder("https://maps.googleapis.com/maps/api/place/details/json")
        url.append("?placeid=$placeId")
        url.append("&key=AIzaSyDQFRH8H5JA_Tw6Rnwne4CVB2VaNagqvsM")
        return url.toString()
    }

    private fun getPhotoOfPlace(photo_reference: String, maxWidth: Int): String {
        val url=StringBuilder("https://maps.googleapis.com/maps/api/place/photo")
        url.append("?maxwidth=$maxWidth")
        url.append("&photoreference=$photo_reference")
        url.append("&key=AIzaSyDQFRH8H5JA_Tw6Rnwne4CVB2VaNagqvsM")
        return url.toString()
    }
}
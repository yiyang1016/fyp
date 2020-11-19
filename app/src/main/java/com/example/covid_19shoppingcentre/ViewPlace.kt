package com.example.covid_19shoppingcentre

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
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
import android.widget.ListView
import com.example.covid_19shoppingcentre.nearby_hospital
import java.math.RoundingMode
import java.text.DecimalFormat

class ViewPlace : AppCompatActivity() {

    internal lateinit var mService:IGoogleAPIService

    var mPlace:PlaceDetail?=null
    internal  var number:String?=""
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
        if(Common.currentResult!!.opening_hours!=null) {
            place_open_hour.text = "Open Now : " + Common.currentResult!!.opening_hours!!.open_now
            if (Common.currentResult!!.opening_hours!!.open_now == true)
                place_open_hour.text = "Open Now : Yes"
            else
                place_open_hour.text = "Open Now : No"
        }
        else
                place_open_hour.visibility = View.GONE

        fun roundOffDecimal(number: Double): Double? {
            val df = DecimalFormat("#.##")
            df.roundingMode = RoundingMode.FLOOR
            return df.format(number).toDouble()
        }

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
                    //place_name.text = mPlace!!.result!!.geometry!!.location!!.lat.toString()+ " , "+mPlace!!.result!!.geometry!!.location!!.lng.toString()
                    phone_number.text = "Phone No : "+ mPlace!!.result!!.formatted_phone_number
                    val currentLat = intent.getStringExtra("lat").toDouble()
                    val currentLng = intent.getStringExtra("lng").toDouble()
                    val latDistance = Math.toRadians(currentLat - mPlace!!.result!!.geometry!!.location!!.lat)
                    val lngDistance = Math.toRadians(currentLng - mPlace!!.result!!.geometry!!.location!!.lng)
                    val sinLat = Math.sin(latDistance /2)
                    val sinLng = Math.sin(lngDistance /2)
                    val a = sinLat * sinLat + (Math.cos(Math.toRadians(currentLat))*Math.cos(Math.toRadians(mPlace!!.result!!.geometry!!.location!!.lat)) *sinLng*sinLng)
                    val c = 2 * Math.atan2(Math.sqrt(a),Math.sqrt(1-a))
                    val distance = (6371 * c)

                    place_distance.text = "Distance : " + roundOffDecimal(distance).toString()+" KM"
                }
            })
        phone_number.setOnClickListener{
            //get input from edit text
            number = phone_number.text.substring(11).toString().trim()

            //Dialer intent
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + Uri.encode(number)))
            startActivity(intent)
        }

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
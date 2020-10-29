package com.example.covid_19shoppingcentre.Remote

import retrofit2.Call
import com.example.covid_19shoppingcentre.models.MyPlaces
import com.example.covid_19shoppingcentre.models.PlaceDetail
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.Url

interface IGoogleAPIService {
    @GET
    fun getNearbyPlaces (@Url url:String): Call<MyPlaces>

    @GET
    fun getDetailPlace(@Url url:String):Call<PlaceDetail>

    @GET("maps/api/directions/json")
    fun getDirections(@Query("origin")origin:String,@Query("destination")destination:String):Call<String>
}
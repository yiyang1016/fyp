package com.example.covid_19shoppingcentre.Common

import com.example.covid_19shoppingcentre.Remote.IGoogleAPIService
import com.example.covid_19shoppingcentre.Remote.RetrofitClient
import com.example.covid_19shoppingcentre.Remote.RetrofitScalarClient
import com.example.covid_19shoppingcentre.models.Results

object Common {

    private val GOOGLE_API_URL="https://maps.googleapis.com/"

    var currentResult:Results?=null

    val googleApiService:IGoogleAPIService
    get()=RetrofitClient.getClient(GOOGLE_API_URL).create(IGoogleAPIService::class.java)

    val googleApiServiceScalars:IGoogleAPIService
        get()=RetrofitScalarClient.getClient(GOOGLE_API_URL).create(IGoogleAPIService::class.java)

}
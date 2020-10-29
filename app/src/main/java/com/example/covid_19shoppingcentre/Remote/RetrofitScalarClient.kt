package com.example.covid_19shoppingcentre.Remote

import retrofit2.Retrofit

import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitScalarClient {
    private var retrofit: Retrofit?=null

    fun getClient(baseUrll:String): Retrofit {
        if(retrofit==null)
        {
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrll)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
        }
        return retrofit!!
    }
}

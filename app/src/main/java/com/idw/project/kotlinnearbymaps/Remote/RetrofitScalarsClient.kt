package com.idw.project.kotlinnearbymaps.Remote

import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitScalarsClient {
    private var retrofit: Retrofit?= null

    fun getClient(baseUrl1:String): Retrofit {
        if (retrofit== null){
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl1)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()

        }

        return retrofit!!
    }
}
package com.idw.project.kotlinnearbymaps.Common

import com.idw.project.kotlinnearbymaps.Model.Result
import com.idw.project.kotlinnearbymaps.Remote.IGoogleAPIService
import com.idw.project.kotlinnearbymaps.Remote.RetrofitClient
import com.idw.project.kotlinnearbymaps.Remote.RetrofitScalarsClient

object Common {
    private val GOOGLE_API_URL = "https://maps.googleapis.com/"

    var currentResult:Result? = null

    val googleApiService:IGoogleAPIService
            get()=RetrofitClient.getClient(GOOGLE_API_URL).create(IGoogleAPIService::class.java)

    val googleApiServiceScalars:IGoogleAPIService
        get()=RetrofitScalarsClient.getClient(GOOGLE_API_URL).create(IGoogleAPIService::class.java)
}
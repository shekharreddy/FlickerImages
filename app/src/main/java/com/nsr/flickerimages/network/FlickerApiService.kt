package com.nsr.flickerimages.network

import com.nsr.flickerimages.model.FlickerSearchResponse
import retrofit2.http.GET
import retrofit2.http.Url

interface FlickerApiService {
    @GET
    suspend fun getFlickerImages(@Url url:String): FlickerSearchResponse

}
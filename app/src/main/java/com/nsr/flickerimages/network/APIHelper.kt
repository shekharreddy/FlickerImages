package com.nsr.flickerimages.network

class ApiHelper(private val apiService: FlickerApiService) {
    private val flickerImagesUrl  = "/services/feeds/photos_public.gne?format=json&nojsoncallback=1&tags=%s"

    suspend fun getFlickerImages(searchKey: String) = apiService.getFlickerImages(String.format(flickerImagesUrl, searchKey))
}
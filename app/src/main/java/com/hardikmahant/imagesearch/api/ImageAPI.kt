package com.hardikmahant.imagesearch.api

import com.hardikmahant.imagesearch.models.ImageSearchData
import com.hardikmahant.imagesearch.util.Constants
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface ImageAPI {

    @Headers("Authorization:Client-ID ${Constants.AUTH_ID}")
    @GET("/3/gallery/search/1")
    suspend fun searchForImages(
        @Query("q") queryString: String,
        @Query("page") pageCount: Int
    ): Response<ImageSearchData>

}
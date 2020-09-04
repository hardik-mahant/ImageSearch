package com.hardikmahant.imagesearch.repository

import com.hardikmahant.imagesearch.api.RetrofitInstance
import com.hardikmahant.imagesearch.db.ImageDatabase
import com.hardikmahant.imagesearch.models.Data
import com.hardikmahant.imagesearch.models.ImageSearchData
import retrofit2.Response

//REPOSITORY component for MVVM architecture

class ImagesRepository(
    val db: ImageDatabase
) {

    suspend fun searchForImages(query: String, searchPageCount: Int): Response<ImageSearchData> =
        RetrofitInstance.api.searchForImages(query, searchPageCount)

    suspend fun insertOrUpdate(data: Data) = db.getArticleDao().insertOrUpdate(data)

    fun getAllImages() = db.getArticleDao().getAllImages()

    fun getCommentForImage(id: String) = db.getArticleDao().getCommentForImage(id)
}
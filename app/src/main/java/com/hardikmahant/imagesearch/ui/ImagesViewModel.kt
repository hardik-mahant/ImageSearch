package com.hardikmahant.imagesearch.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.hardikmahant.imagesearch.ImageSearchApplication
import com.hardikmahant.imagesearch.models.Data
import com.hardikmahant.imagesearch.models.ImageSearchData
import com.hardikmahant.imagesearch.repository.ImagesRepository
import com.hardikmahant.imagesearch.util.Resource
import kotlinx.coroutines.launch
import retrofit2.Response
import java.io.IOException

/**
 * ViewModel component for MVVM architecture
 * */
class ImagesViewModel(
    app: Application,
    private val repository: ImagesRepository
) : AndroidViewModel(app) {

    var imageSearchResponse: ImageSearchData? = null

    //used for handling pagination
    var searchPageCount = 1
    var lastQuery = ""

    val imageSearchData: MutableLiveData<Resource<ImageSearchData>> = MutableLiveData()


    fun searchForImages(query: String) = viewModelScope.launch {
        if (lastQuery == query) {
            searchPageCount++
        } else {
            resetPageCount()
            lastQuery = query
        }
        safeSearchImageCall(query, searchPageCount)
    }

    private fun resetPageCount() {
        searchPageCount = 1
    }

    private suspend fun safeSearchImageCall(query: String, searchPageCount: Int) {
        imageSearchData.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()) {
                val response = repository.searchForImages(query, searchPageCount)
                imageSearchData.postValue(handleImageSearchResponse(response))
            } else {
                imageSearchData.postValue(Resource.Error("No internet connection!"))
            }
        } catch (t: Throwable) {
            when (t) {
                is IOException -> imageSearchData.postValue(Resource.Error("Network failure"))
                else -> imageSearchData.postValue(Resource.Error("Conversion error"))
            }
        }
    }

    private fun handleImageSearchResponse(response: Response<ImageSearchData>): Resource<ImageSearchData> {
        if (response.isSuccessful) {
            response.body()?.let { searchData ->
                if (imageSearchResponse == null || searchPageCount == 1) {
                    imageSearchResponse = searchData
                } else {
                    val oldResults = imageSearchResponse?.data
                    val newResults = searchData.data
                    oldResults?.addAll(newResults)
                }
                return Resource.Success(imageSearchResponse ?: searchData)
            }
        }
        return Resource.Error(response.message())
    }

    fun getSavedImages() = repository.getAllImages()

    /** Launches the Coroutine with ViewModelScope
     * Insert or updates the provided data to the database
     */
    fun saveImage(data: Data) = viewModelScope.launch {
        repository.insertOrUpdate(data)
    }

    fun getCommentForImage(id: String) = repository.getCommentForImage(id)

    /**
     * Checks for the internet connection
     * @return true if internet connection is available, false otherwise
     * */
    private fun hasInternetConnection(): Boolean {
        val connectivityManager =
            getApplication<ImageSearchApplication>().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}
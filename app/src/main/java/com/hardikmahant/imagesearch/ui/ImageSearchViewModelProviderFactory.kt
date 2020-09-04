package com.hardikmahant.imagesearch.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.hardikmahant.imagesearch.repository.ImagesRepository

class ImageSearchViewModelProviderFactory(
    val app: Application,
    val repository: ImagesRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ImagesViewModel(app, repository) as T
    }
}
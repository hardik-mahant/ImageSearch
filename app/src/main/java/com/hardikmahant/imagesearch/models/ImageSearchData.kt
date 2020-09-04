package com.hardikmahant.imagesearch.models

data class ImageSearchData(
    val `data`: MutableList<Data>,
    val status: Int,
    val success: Boolean
)
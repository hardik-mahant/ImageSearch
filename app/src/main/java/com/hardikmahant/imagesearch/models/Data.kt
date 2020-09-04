package com.hardikmahant.imagesearch.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(
    tableName = "ImageData"
)
data class Data(
    @PrimaryKey
    val id: String = "",
    val images: List<Image>,
    val images_count: Int,
    val title: String,
    var usr_comment: String? = ""
): Serializable
package com.hardikmahant.imagesearch.db

import androidx.room.TypeConverter
import com.hardikmahant.imagesearch.models.Image

class Converters {

    @TypeConverter
    fun fromSource(images: List<Image>): String {
        return images[0].link!!
    }

    @TypeConverter
    fun toSource(link: String): List<Image> {
        val images = ArrayList<Image>()
        images.add(Image(link, link))
        return images
    }
}
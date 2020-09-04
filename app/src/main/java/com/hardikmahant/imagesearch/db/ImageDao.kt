package com.hardikmahant.imagesearch.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hardikmahant.imagesearch.models.Data

@Dao
interface ImageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(image: Data): Long

    @Query("SELECT * FROM ImageData")
    fun getAllImages(): LiveData<List<Data>>

    @Query("SELECT usr_comment FROM ImageData WHERE id = :id")
    fun getCommentForImage(id: String): LiveData<String>
}
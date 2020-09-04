package com.hardikmahant.imagesearch.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.hardikmahant.imagesearch.models.Data

/**
 * Room Database,
 * use ImageDao for using the features of the database
 * */
@Database(
    entities = [Data::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class ImageDatabase: RoomDatabase() {

    abstract fun getArticleDao(): ImageDao

    companion object {
        @Volatile
        private var instance: ImageDatabase? = null
        private val LOCK = Any()

        operator fun invoke(context: Context) = instance ?: synchronized(LOCK) {
            instance ?: createDatabase(context).also {
                instance = it
            }
        }

        /**
         * @param context Context of an activity or application level context
         * Creates a database
         * */
        private fun createDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                ImageDatabase::class.java,
                "image_database.db"
            ).build()
    }
}
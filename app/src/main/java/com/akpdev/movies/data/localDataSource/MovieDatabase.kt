package com.akpdev.movies.data.localDataSource

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.akpdev.movies.data.localDataSource.dao.MovieDao
import com.akpdev.movies.data.localDataSource.entity.MovieEntity
import com.akpdev.movies.data.localDataSource.entity.PopularPagingMetaDataEntity
import com.akpdev.movies.data.localDataSource.entity.UpcomingPagingMetaDataEntity

@Database(
    entities = [MovieEntity::class, UpcomingPagingMetaDataEntity::class , PopularPagingMetaDataEntity::class],
    version = 1,
    exportSchema = false
)
abstract class MovieDatabase :RoomDatabase(){
    abstract fun movieDao():MovieDao

    companion object {
        fun create(applicationContext: Context): MovieDatabase {
            return Room.databaseBuilder(
                applicationContext,
                MovieDatabase::class.java, "AppDatabase"
            ).allowMainThreadQueries()
                .build()
        }
    }
}
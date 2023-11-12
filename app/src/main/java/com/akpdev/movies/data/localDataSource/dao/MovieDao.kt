package com.akpdev.movies.data.localDataSource.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.akpdev.movies.data.localDataSource.entity.MovieEntity
import com.akpdev.movies.data.localDataSource.entity.PopularPagingMetaDataEntity
import com.akpdev.movies.data.localDataSource.entity.UpcomingPagingMetaDataEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(movies:List<MovieEntity>)

    @Query("SELECT * FROM movies")
    fun pagingSource():PagingSource<Int,MovieEntity>

    @Transaction
    @Query("SELECT * FROM movies where movieType=:movieType ORDER by itemOrder ASC")
    fun getMovies(movieType:String):Flow<List<MovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUpcomingPagingMetaData(pageData: UpcomingPagingMetaDataEntity)

    @Transaction
    @Query("SELECT * FROM UpcomingPagingMetaDataEntity")
    fun getUpcomingPagingMetaDataEntity(): UpcomingPagingMetaDataEntity?

    @Delete
    suspend fun deletePagingMetaData(pagingMetaDataEntity: UpcomingPagingMetaDataEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPopularPagingMetaData(pageData: PopularPagingMetaDataEntity)

    @Transaction
    @Query("SELECT * FROM PopularPagingMetaDataEntity")
    fun getPopularPagingMetaDataEntity(): PopularPagingMetaDataEntity?

    @Delete
    suspend fun deletePagingMetaData(pagingMetaDataEntity: PopularPagingMetaDataEntity)

    @Delete
    suspend fun deleteMovie(movie: MovieEntity)

    @Transaction
    @Query("SELECT * FROM movies where isFav=:favorite")
    fun getFavoriteMovies(favorite: Boolean = true ):List<MovieEntity>

    @Query("update movies set isFav = :favorite where  id = :id")
    fun toggleFavorite(id: String, favorite: Boolean)
}
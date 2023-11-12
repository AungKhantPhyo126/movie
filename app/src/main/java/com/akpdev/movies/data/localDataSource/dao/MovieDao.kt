package com.akpdev.movies.data.localDataSource.dao

import androidx.paging.PagingData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.akpdev.movies.data.localDataSource.entity.MovieEntity
import com.akpdev.movies.data.localDataSource.entity.PagingMetaDataEntity
import com.akpdev.movies.domain.model.PagingMetaData
import kotlinx.coroutines.flow.Flow

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAll(movies:List<MovieEntity>)

    @Query("SELECT * FROM movies")
    fun pagingSource():PagingSource<Int,MovieEntity>

    @Transaction
    @Query("SELECT * FROM movies ORDER by itemOrder ASC")
    fun getAllMovies():Flow<List<MovieEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPagingMetaData(pageData:PagingMetaDataEntity)

    @Transaction
    @Query("SELECT * FROM PagingMetaDataEntity")
    fun getPagingMetaData(): PagingMetaDataEntity?

    @Delete
    suspend fun deletePagingMetaData(pagingMetaDataEntity: PagingMetaDataEntity)

    @Delete
    suspend fun deleteMovie(movie: MovieEntity)

    @Transaction
    @Query("SELECT * FROM movies where isFav=:favorite")
    fun getFavoriteMovies(favorite: Boolean = true ):PagingSource<Int,MovieEntity>

    @Query("update movies set isFav = :favorite where  id = :id")
    fun toggleFavorite(id: String, favorite: Boolean)
}
package com.akpdev.movies.di

import android.content.Context
import com.akpdev.movies.common.ConnectionObserver
import com.akpdev.movies.common.ConnectionObserverImpl
import com.akpdev.movies.common.Constants
import com.akpdev.movies.data.localDataSource.MovieDatabase
import com.akpdev.movies.data.localDataSource.dao.MovieDao
import com.akpdev.movies.data.remote.api.MoviedbApi
import com.akpdev.movies.data.repository.MovieRepositoryImpl
import com.akpdev.movies.di.inteceptors.CommonQueryInterceptor
import com.akpdev.movies.domain.repository.MovieRepository
import com.google.gson.Gson
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    fun provideRetrofit(moshi: Moshi,okHttpClient: OkHttpClient) : Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .baseUrl(Constants.BASE_URL)
            .client(okHttpClient)
            .build()
    }

    @Provides
    fun provideMoshi() : Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideMovieApiService(retrofit: Retrofit) = retrofit.create<MoviedbApi>()

    @Provides
    @Singleton
    fun provideMovieRepository(moviedbApi:MoviedbApi,movieDao: MovieDao):MovieRepository{
        return MovieRepositoryImpl(moviedbApi,movieDao)
    }

    @Singleton
    @Provides
    fun provideRoomDatabase(@ApplicationContext context: Context)  : MovieDatabase {
        return MovieDatabase.create(context)
    }

    @Provides
    fun provideMovieDao(movieDatabase: MovieDatabase) : MovieDao {
        return movieDatabase.movieDao()
    }

    @Provides
    @Singleton
    fun provideCommonQueryInterceptor():CommonQueryInterceptor{
        return CommonQueryInterceptor()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        @ApplicationContext context: Context,
        commonQueryInterceptor: CommonQueryInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor(commonQueryInterceptor)
        }.build()
    }

    @Provides
    @Singleton
    fun provideConnectionObserver(
        connectionObserver: ConnectionObserverImpl
    ): ConnectionObserver{
        return connectionObserver
    }
}
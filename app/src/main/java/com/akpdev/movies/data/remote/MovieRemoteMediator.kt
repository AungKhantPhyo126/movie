//package com.akpdev.movies.data.remote
//
//import androidx.paging.ExperimentalPagingApi
//import androidx.paging.LoadType
//import androidx.paging.PagingState
//import androidx.paging.RemoteMediator
//import com.akpdev.movies.data.localDataSource.MovieDatabase
//import com.akpdev.movies.data.localDataSource.entity.MovieEntity
//import com.akpdev.movies.data.remote.api.MoviedbApi
//import retrofit2.HttpException
//import java.io.IOException
//
//@OptIn(ExperimentalPagingApi::class)
//class MovieRemoteMediator(
//    private val movieDb:MovieDatabase,
//    private val moviedbApi: MoviedbApi
//):RemoteMediator<Int,MovieEntity>() {
//    override suspend fun load(
//        loadType: LoadType,
//        state: PagingState<Int, MovieEntity>
//    ): MediatorResult {
//        return try {
//            val loadKey = when(loadType){
//                LoadType.REFRESH-> 1
//                LoadType.PREPEND-> return MediatorResult.Success(
//                    endOfPaginationReached = true
//                )
//                LoadType.APPEND->{
//                    val lastItem =state.lastItemOrNull()
//                    if (lastItem == null){
//                        1
//                    }else{
//                        (lastItem.id/state.config.pageSize)+1
//                    }
//                }
//            }
//        }catch (e:IOException){
//            MediatorResult.Error(e)
//        }catch (e:HttpException){
//            MediatorResult.Error(e)
//        }
//    }
//}
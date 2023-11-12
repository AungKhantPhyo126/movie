package com.akpdev.movies.presentation.upcomingMovieList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.akpdev.movies.common.ConnectionObserver
import com.akpdev.movies.common.ConnectivityLiveData
import com.akpdev.movies.common.DefaultPaginator
import com.akpdev.movies.domain.useCase.fetchFromNetworkUseCase.FetchUpcomingMoviesUseCase
import com.akpdev.movies.domain.useCase.fetchFromRoom.GeUpcomingMoviesUseCase
import com.akpdev.movies.domain.useCase.GetCachedUpcomingPagingMetadataUseCase
import com.akpdev.movies.domain.useCase.ToggleFavoriteMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UpcomingMovieListViewModel @Inject constructor(
    private val getUpcomingMoviesUseCase: GeUpcomingMoviesUseCase,
    private val fetchUpcomingMoviesUseCase: FetchUpcomingMoviesUseCase,
    private val getCachedUpcomingPagingMetadataUseCase: GetCachedUpcomingPagingMetadataUseCase,
    private val toggleFavoriteMovieUseCase: ToggleFavoriteMovieUseCase,
    private val connectionObserver: ConnectionObserver,
    application: Application
) : AndroidViewModel(application) {

    private val _event = Channel<String>(capacity = Channel.CONFLATED)
    val event = _event.receiveAsFlow()

    private val connectivityLiveData = ConnectivityLiveData(application)

    fun getConnectivityLiveData(): LiveData<Boolean> {
        return connectivityLiveData
    }


    private val _upcomingMovieListState = MutableStateFlow(UpcomingMovieListState())
    val movieListState = _upcomingMovieListState.asStateFlow()

    private val paginator = DefaultPaginator(
        initialKey = cachedPageNumber(),
        onLoadUpdated = {
            _upcomingMovieListState.update { currentState ->
                currentState.copy(isLoading = it)
            }
        },
        onRequest = { nextPage ->
            fetchUpcomingMoviesUseCase(nextPage)
        },
        getNextKey = { paginatedList ->
            paginatedList.page + 1
        },
        onError = {
            _event.send(it?.localizedMessage ?: "Unknown Error Occurred")
//            _upcomingMovieListState.update { currentUiState ->
//                currentUiState.copy(error = it?.localizedMessage ?: "Unknown Error Occurred")
//            }
        },
        onSuccess = { items, newKey, isEndReached ->
            getUpcomingMovies()
        }
    )

    init {
        getUpcomingMovies()
    }

    fun loadNextUpcomingMovies() {
        //time to load next items
        with(connectionObserver) {
            onConnected = {
                viewModelScope.launch {
                    paginator.loadNextItems()
                }
            }
            onDisconnected = {
                _upcomingMovieListState.value = _upcomingMovieListState.value.copy(
                    isLoading = false,
                    error = "No Internet!",
                    hasInternet = false
                )
            }
            register()
        }
    }

    fun getUpcomingMovies() {
        cachedPageNumber()
        getUpcomingMoviesUseCase.invoke()
            .onEach { result ->
                if (result.isNotEmpty()) {
                    _upcomingMovieListState.value = UpcomingMovieListState(upcomingMovieList = result)
                } else {
                    loadNextUpcomingMovies()
                }
            }
            .launchIn(viewModelScope)
    }


    fun cachedPageNumber(): Int = getCachedUpcomingPagingMetadataUseCase()?.page ?: 0

    

    fun toggleFavorite(id:String,isFavorite:Boolean){
        toggleFavoriteMovieUseCase(id,isFavorite)
    }

    override fun onCleared() {
        super.onCleared()
        connectionObserver.unregister()
    }

}
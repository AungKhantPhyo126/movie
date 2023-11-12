package com.akpdev.movies.presentation.popularMovieList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.akpdev.movies.common.ConnectionObserver
import com.akpdev.movies.common.ConnectivityLiveData
import com.akpdev.movies.common.DefaultPaginator
import com.akpdev.movies.domain.useCase.fetchFromNetworkUseCase.FetchPopularMovieUseCase
import com.akpdev.movies.domain.useCase.GetCachedPopularPagingMetadataUseCase
import com.akpdev.movies.domain.useCase.fetchFromRoom.GetPopularMoviesUseCase
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
class  PopularMovieListViewModel @Inject constructor(
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val fetchPopularMovieUseCase: FetchPopularMovieUseCase,
    private val getCachedPopularPagingMetadataUseCase: GetCachedPopularPagingMetadataUseCase,
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


    private val _popularMovieListState = MutableStateFlow(PopularMovieListState())
    val movieListState = _popularMovieListState.asStateFlow()

    private val paginator = DefaultPaginator(
        initialKey = cachedPageNumber(),
        onLoadUpdated = {
            _popularMovieListState.update { currentState ->
                currentState.copy(isLoading = it)
            }
        },
        onRequest = { nextPage ->
            fetchPopularMovieUseCase(nextPage)
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
            getPopularMovies()
        }
    )

    init {
        getPopularMovies()
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
                _popularMovieListState.value = _popularMovieListState.value.copy(
                    isLoading = false,
                    error = "No Internet!",
                    hasInternet = false
                )
            }
            register()
        }
    }

    fun getPopularMovies() {
        cachedPageNumber()
        getPopularMoviesUseCase.invoke()
            .onEach { result ->
                if (result.isNotEmpty()) {
                    _popularMovieListState.value = PopularMovieListState(popularMovieList = result)
                } else {
                    loadNextUpcomingMovies()
                }
            }
            .launchIn(viewModelScope)
    }


    fun cachedPageNumber(): Int = getCachedPopularPagingMetadataUseCase()?.page ?: 0



    fun toggleFavorite(id:String,isFavorite:Boolean){
        toggleFavoriteMovieUseCase(id,isFavorite)
    }

    override fun onCleared() {
        super.onCleared()
        connectionObserver.unregister()
    }

}
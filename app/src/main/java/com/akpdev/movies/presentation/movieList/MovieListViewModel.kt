package com.akpdev.movies.presentation.movieList

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.akpdev.movies.common.ConnectionObserver
import com.akpdev.movies.common.ConnectivityLiveData
import com.akpdev.movies.common.DefaultPaginator
import com.akpdev.movies.common.Resource
import com.akpdev.movies.domain.useCase.FetchUpcomingMoviesUseCase
import com.akpdev.movies.domain.useCase.GeUpcomingMoviesUseCase
import com.akpdev.movies.domain.useCase.GetCachedPagingMetadataUseCase
import com.akpdev.movies.domain.useCase.GetPopularMoviesUseCase
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
class MovieListViewModel @Inject constructor(
    private val getUpcomingMoviesUseCase: GeUpcomingMoviesUseCase,
    private val fetchUpcomingMoviesUseCase: FetchUpcomingMoviesUseCase,
    private val getCachedPagingMetadataUseCase: GetCachedPagingMetadataUseCase,
    private val getPopularMoviesUseCase: GetPopularMoviesUseCase,
    private val connectionObserver: ConnectionObserver,
    application: Application
) : AndroidViewModel(application) {

    private val _event = Channel<String>(capacity = Channel.CONFLATED)
    val event = _event.receiveAsFlow()

    private val connectivityLiveData = ConnectivityLiveData(application)

    fun getConnectivityLiveData(): LiveData<Boolean> {
        return connectivityLiveData
    }


    private val _upcomingMovieListState = MutableStateFlow(MovieListState())
    val upcomingMovieListState = _upcomingMovieListState.asStateFlow()

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
//            _upcomingMovieListState.update { currentState ->
//                currentState.copy(
//                    upcomingMovieList = currentState.upcomingMovieList + items as List<Movie>,
//                    page = newKey,
//                    endReached = isEndReached
//                )
//            }
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
                    _upcomingMovieListState.value = MovieListState(upcomingMovieList = result)
                } else {
                    loadNextUpcomingMovies()
                }
            }
            .launchIn(viewModelScope)
    }


    fun cachedPageNumber(): Int = getCachedPagingMetadataUseCase()?.page ?: 0


    private val _popularMovieListState = MutableStateFlow(MovieListState())
    val popularMovieListState = _popularMovieListState.asStateFlow()

    fun getPopularMovies() {
        getPopularMoviesUseCase(0).onEach { result ->
            when (result) {
                is Resource.Loading -> {
                    _popularMovieListState.value = MovieListState(isLoading = true)
                }

                is Resource.Success -> {
                    _popularMovieListState.value =
                        MovieListState(popularMovieList = result.data ?: emptyList())
                }

                is Resource.Error -> {
                    _popularMovieListState.update { currentUiState ->
                        currentUiState.copy(error = result.message ?: "Unknown Error Occurred")
                    }
                }
            }
        }.launchIn(viewModelScope)
    }

    override fun onCleared() {
        super.onCleared()
        connectionObserver.unregister()
    }

}
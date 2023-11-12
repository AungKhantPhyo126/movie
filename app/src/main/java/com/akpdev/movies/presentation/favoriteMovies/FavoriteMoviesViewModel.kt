package com.akpdev.movies.presentation.favoriteMovies

import androidx.lifecycle.ViewModel
import com.akpdev.movies.domain.useCase.GetFavoriteMoviesUseCase
import com.akpdev.movies.domain.useCase.ToggleFavoriteMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import javax.inject.Inject

@HiltViewModel
class FavoriteMoviesViewModel @Inject constructor(
    private val getFavoriteMoviesUseCase: GetFavoriteMoviesUseCase,
    private val toggleFavoriteMovieUseCase: ToggleFavoriteMovieUseCase
) : ViewModel() {

    private val _event = Channel<String>(capacity = Channel.CONFLATED)
    val event = _event.receiveAsFlow()

    private val _movieListState = MutableStateFlow(FavoriteMovieState())
    val movieListState = _movieListState.asStateFlow()

    fun getFavoriteMovies(){
        _movieListState.value = _movieListState.value.copy(favoriteMovies = getFavoriteMoviesUseCase())
    }
    fun toggleFavorite(id:String,isFavorite:Boolean){
        toggleFavoriteMovieUseCase(id,isFavorite)
    }
    init {
        getFavoriteMovies()
    }

}
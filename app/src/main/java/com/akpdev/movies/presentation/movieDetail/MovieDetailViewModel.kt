package com.akpdev.movies.presentation.movieDetail

import androidx.lifecycle.ViewModel
import com.akpdev.movies.domain.useCase.GetMovieDetailUseCase
import com.akpdev.movies.domain.useCase.ToggleFavoriteMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
// Inject dependencies here if needed
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    private val toggleFavoriteMovieUseCase: ToggleFavoriteMovieUseCase
) : ViewModel() {

       private val _movieDetailState = MutableStateFlow(MovieDetailState())
           val movieDetailState = _movieDetailState.asStateFlow()
    fun getMovieDetail(id:Int){
        _movieDetailState.value = _movieDetailState.value.copy(movieDetail = getMovieDetailUseCase(id))
    }

    fun toggleFavorite(id:String,isFavorite:Boolean){
        toggleFavoriteMovieUseCase(id,isFavorite)
    }
}
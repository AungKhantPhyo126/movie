package com.akpdev.movies.presentation.movieDetail

import androidx.lifecycle.ViewModel
import com.akpdev.movies.domain.useCase.ToggleFavoriteMovieUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
// Inject dependencies here if needed
    private val toggleFavoriteMovieUseCase: ToggleFavoriteMovieUseCase
) : ViewModel() {
    fun toggleFavorite(id:String,isFavorite:Boolean){
        toggleFavoriteMovieUseCase(id,isFavorite)
    }
}
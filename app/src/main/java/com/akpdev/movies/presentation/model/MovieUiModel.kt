package com.akpdev.movies.presentation.model

import android.os.Parcelable
import com.akpdev.movies.domain.model.Movie
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MovieUiModel(
    val id:String,
    val title:String,
    val posterPath:String,
    val overview:String,
    val itemOrder: Int,
    val isFavorite:Boolean
):Parcelable

fun Movie.asUiModel():MovieUiModel{
    return MovieUiModel(
        id = id,
        title = title,
        posterPath = posterPath,
        overview = overview,
        itemOrder = itemOrder,
        isFavorite = isFavorite
    )
}


package com.akpdev.movies.presentation.favoriteMovies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.akpdev.movies.databinding.FragmentFavoriteMoviesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteMoviesFragment:Fragment() {
    private var _binding:FragmentFavoriteMoviesBinding?=null
    val binding:FragmentFavoriteMoviesBinding
        get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentFavoriteMoviesBinding.inflate(inflater).also {
            _binding = it
        }.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
}
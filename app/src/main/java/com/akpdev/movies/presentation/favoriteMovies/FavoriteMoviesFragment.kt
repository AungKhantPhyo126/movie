package com.akpdev.movies.presentation.favoriteMovies

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.akpdev.movies.databinding.FragmentFavoriteMoviesBinding
import com.akpdev.movies.presentation.model.asUiModel
import com.akpdev.movies.presentation.upcomingMovieList.MoviesListRecyclerAdapter
import com.akpdev.movies.presentation.upcomingMovieList.UpcomingMovieListFragment
import com.akpdev.movies.presentation.upcomingMovieList.UpcomingMovieListFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FavoriteMoviesFragment : Fragment() {
    private var _binding: FragmentFavoriteMoviesBinding? = null
    val binding: FragmentFavoriteMoviesBinding
        get() = _binding!!

    private val viewModel by viewModels<FavoriteMoviesViewModel>()

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
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = MoviesListRecyclerAdapter({ movieId ->
            findNavController().navigate(
                FavoriteMoviesFragmentDirections
                    .actionFavoriteMoviesFragmentToMovieDetailFragment(movieId)
            )
        }, { movieId, isFavorite ->
            viewModel.toggleFavorite(movieId, isFavorite)
            viewModel.getFavoriteMovies()
        })
        binding.rvFavoriteMovies.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.movieListState.collectLatest {
                    adapter.submitList(it.favoriteMovies)
                }
            }
        }
    }
}
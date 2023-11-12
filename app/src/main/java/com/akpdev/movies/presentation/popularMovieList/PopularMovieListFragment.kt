package com.akpdev.movies.presentation.popularMovieList

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akpdev.movies.databinding.FragmentPopularMovieListBinding
import com.akpdev.movies.presentation.model.asUiModel
import com.akpdev.movies.presentation.upcomingMovieList.MoviesListRecyclerAdapter
import com.akpdev.movies.presentation.upcomingMovieList.UpcomingMovieListFragmentDirections
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PopularMovieListFragment:Fragment() {
    private var _binding:FragmentPopularMovieListBinding?=null
    val binding:FragmentPopularMovieListBinding
        get() = _binding!!
    private val viewModel by viewModels<PopularMovieListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentPopularMovieListBinding.inflate(inflater).also {
            _binding = it
        }.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val adapter = MoviesListRecyclerAdapter({ movie ->
            findNavController().navigate(
                PopularMovieListFragmentDirections.actionPopularMovieListFragmentToMovieDetailFragment(
                    movie.asUiModel()
                )
            )
        }, { movieId, isFavorite ->
            viewModel.toggleFavorite(movieId, isFavorite)
        })
        binding.rvMovie.adapter = adapter
        binding.rvMovie.addOnScrollListener(this@PopularMovieListFragment.scrollListener)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.movieListState.collectLatest {
                    if (it.isLoading) {
                        showLoadingBar()
                    } else if (it.popularMovieList.isNotEmpty()) {
                        hideLoadingBar()
                        adapter.submitList(it.popularMovieList)
                    }else if (it.error.isNotEmpty()) {
                        hideLoadingBar()
                        Toast.makeText(requireContext(), it.error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.event.collect { event ->
                // Handle the one-time event
                Snackbar.make(view, event, Snackbar.LENGTH_SHORT).show()
            }
        }

        viewModel.getConnectivityLiveData().observe(viewLifecycleOwner) { isConnected ->
            if (isConnected) {
                // Internet connection is available
                // Update your UI or perform actions accordingly
                binding.tvInternetState.setBackgroundColor(Color.GREEN)
                binding.tvInternetState.text = "Connected to internet"
                binding.tvInternetState.isVisible = false
            } else {
                // No internet connection
                // Update your UI or perform actions accordingly
                binding.tvInternetState.isVisible = true
                binding.tvInternetState.text = "Disconnected from internet"
                binding.tvInternetState.setBackgroundColor(Color.RED)
            }
        }
    }

    fun hideLoadingBar() {
        isLoading = false
        binding.loadingProgressBar.isVisible = false
    }

    fun showLoadingBar() {
        isLoading = true
        binding.loadingProgressBar.isVisible = true
    }


    var isScrolling = false
    var isLoading = false

    val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val totalItemCount = layoutManager.itemCount
            val lastVisibleItem = layoutManager.findLastVisibleItemPosition()
            val visibleThreshold = 5

            if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                // End has been reached, load more data
                viewModel.loadNextUpcomingMovies()
                isLoading = true
            }
        }
    }
}
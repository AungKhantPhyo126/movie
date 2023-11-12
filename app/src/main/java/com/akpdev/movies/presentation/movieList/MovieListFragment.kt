package com.akpdev.movies.presentation.movieList

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.view.setPadding
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.LayoutManager
import androidx.recyclerview.widget.RecyclerView.Recycler
import com.akpdev.movies.R
import com.akpdev.movies.common.Constants.QUERY_PAGE_SIZE
import com.akpdev.movies.databinding.FragmentMovieListBinding
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class MovieListFragment : Fragment() {
    private var _binding: FragmentMovieListBinding? = null
    val binding: FragmentMovieListBinding
        get() = _binding!!

    private val viewModel by viewModels<MovieListViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentMovieListBinding.inflate(inflater).also {
            _binding = it
        }.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = MoviesListRecyclerAdapter {

        }
        binding.rvMovie.adapter = adapter
        binding.rvMovie.addOnScrollListener(this@MovieListFragment.scrollListener)
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.upcomingMovieListState.collectLatest {
                    if (it.page>0){
                        cachedPage = it.page
                    }
                    if (it.isLoading) {
                        showLoadingBar()
                    } else if (it.upcomingMovieList.isNotEmpty()) {
                        hideLoadingBar()
                        adapter.submitList(it.upcomingMovieList)
                    }else if (it.error.isNotEmpty()) {
                        hideLoadingBar()
                        Toast.makeText(requireContext(), it.error, Toast.LENGTH_LONG).show()
                    }else if (it.endReached) {
                        hideLoadingBar()
                        isLastPage = true
                        binding.rvMovie.setPadding(0, 0, 0, 0)
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

    var cachedPage = 1

    var isScrolling = false
    var isLoading = false
    var isLastPage = false

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
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= cachedPage * QUERY_PAGE_SIZE
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning &&
                    isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                viewModel.loadNextUpcomingMovies()
                isScrolling = false
            }

        }
    }

    fun setUpRecyclerView() {

    }
}
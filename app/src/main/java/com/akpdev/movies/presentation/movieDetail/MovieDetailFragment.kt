package com.akpdev.movies.presentation.movieDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.navArgs
import com.akpdev.movies.R
import com.akpdev.movies.common.Constants
import com.akpdev.movies.common.loadImageWithGlide
import com.akpdev.movies.databinding.FragmentMovieDetailBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieDetailFragment : Fragment() {
    private var _binding: FragmentMovieDetailBinding? = null
    val binding: FragmentMovieDetailBinding
        get() = _binding!!

    private val args by navArgs<MovieDetailFragmentArgs>()
    private val viewModel by viewModels<MovieDetailViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return FragmentMovieDetailBinding.inflate(inflater).also {
            _binding = it
        }.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getMovieDetail(args.movieId)
        var isFavorite = false
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.movieDetailState.collectLatest { result ->
                    isFavorite = result.movieDetail?.isFavorite ?: false
                    updateFavoriteState(isFavorite)
                    binding.ivMoviePoster.loadImageWithGlide(Constants.BASE_IMG_URL + result.movieDetail?.posterPath)
                    binding.movieTitle.text = result.movieDetail?.title
                    binding.tvReview.text = result.movieDetail?.overview
                }
            }
        }


        binding.btnFavorite.setOnClickListener {
            isFavorite = !isFavorite
            viewModel.toggleFavorite(args.movieId.toString(), isFavorite)
            updateFavoriteState(isFavorite)
        }
    }

    fun updateFavoriteState(isFavorite: Boolean) {
        if (isFavorite) {
            binding.btnFavorite.text = "Remove favorite"
            binding.btnFavorite.icon = binding.root.context.getDrawable(R.drawable.ic_favorite)
            binding.btnFavorite.iconTint = binding.root.context.getColorStateList(R.color.red)
        } else {
            binding.btnFavorite.text = "Add favorite"
            binding.btnFavorite.icon =
                binding.root.context.getDrawable(R.drawable.ic_not_favorite)
            binding.btnFavorite.iconTint = binding.root.context.getColorStateList(R.color.white)

        }
    }
}
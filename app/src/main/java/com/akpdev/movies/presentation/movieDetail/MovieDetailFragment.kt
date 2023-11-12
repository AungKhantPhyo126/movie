package com.akpdev.movies.presentation.movieDetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.akpdev.movies.R
import com.akpdev.movies.common.Constants
import com.akpdev.movies.common.loadImageWithGlide
import com.akpdev.movies.databinding.FragmentMovieDetailBinding
import dagger.hilt.android.AndroidEntryPoint

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
        binding.ivMoviePoster.loadImageWithGlide(Constants.BASE_IMG_URL + args.detailData.posterPath)
        binding.movieTitle.text = args.detailData.title
        binding.tvReview.text = args.detailData.overview

        var isFavorite = args.detailData.isFavorite
        updateFavoriteState(isFavorite)


        binding.btnFavorite.setOnClickListener {
            isFavorite = !isFavorite
            viewModel.toggleFavorite(args.detailData.id, isFavorite)
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
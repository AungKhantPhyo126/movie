package com.akpdev.movies.presentation.upcomingMovieList

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.akpdev.movies.R
import com.akpdev.movies.common.Constants.BASE_IMG_URL
import com.akpdev.movies.common.loadImageWithGlide
import com.akpdev.movies.databinding.ItemRecyclerUpcomingMovieBinding
import com.akpdev.movies.domain.model.Movie

class MoviesListRecyclerAdapter(
    private val navigateToDetail: (movieId:Int) -> Unit,
    private val toggleFavorite: (movieId: String, isFavorite: Boolean) -> Unit,
) :
    ListAdapter<Movie, MoviesListRecyclerAdapter.UpcomingMoviesViewHolder>(MoviesDiffCallback) {
    override fun onBindViewHolder(holder: UpcomingMoviesViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item!!)
    }

    override fun onBindViewHolder(
        holder: UpcomingMoviesViewHolder,
        position: Int,
        payloads: MutableList<Any>
    ) {
        holder.bind(getItem(position), payloads)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UpcomingMoviesViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemRecyclerUpcomingMovieBinding.inflate(layoutInflater, parent, false)
        return UpcomingMoviesViewHolder(binding)
    }

    inner class UpcomingMoviesViewHolder(
        private val binding: ItemRecyclerUpcomingMovieBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        var _data: Movie? = null
        val data: Movie
            get() = _data!!

        init {
            binding.mcvMovie.setOnClickListener {
                navigateToDetail(data.id.toInt())
            }
            binding.btnFavorite.setOnClickListener {
                toggleFavorite(data.id, !data.isFavorite)
            }
        }

        fun bind(movie: Movie, payloads: MutableList<Any>) {
            _data = movie
            when (payloads.firstOrNull()) {
                "favoriteChanged" -> updateFavorite(data.isFavorite)
                else ->bind(data)
            }
        }

        fun bind(movie: Movie) {
            _data = movie
            binding.ivMoviePoster.loadImageWithGlide(BASE_IMG_URL + movie.posterPath)
            binding.movieTitle.text = movie.title
            updateFavorite(movie.isFavorite)
        }

        fun updateFavorite(isFavorite: Boolean){
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
}

object MoviesDiffCallback : DiffUtil.ItemCallback<Movie>() {
    override fun getChangePayload(oldItem: Movie, newItem: Movie): Any? {
        return when {
            oldItem.isFavorite != newItem.isFavorite -> "favoriteChanged"
            else -> super.getChangePayload(oldItem, newItem)
        }
    }

    override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem.itemOrder == newItem.itemOrder
    }

    override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
        return oldItem == newItem
    }
}


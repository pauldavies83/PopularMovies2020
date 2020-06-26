package dev.pauldavies.popularmovies2020.movielist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.api.load
import dev.pauldavies.popularmovies2020.R
import kotlinx.android.synthetic.main.item_movie_list.view.*

val itemCallback = object : DiffUtil.ItemCallback<MovieListItem>() {
    override fun areItemsTheSame(old: MovieListItem, new: MovieListItem) = old.id == new.id
    override fun areContentsTheSame(old: MovieListItem, new: MovieListItem) = old == new
}

const val TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/w500"

internal class MovieListAdapter :
    PagingDataAdapter<MovieListItem, MovieListAdapter.MovieViewHolder>(itemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_movie_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    class MovieViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(movie: MovieListItem) {
            itemView.apply {
                with (movie) {
                    /** this ext fun is a shortcut to the singleton image loader using coil
                        ideally this should be configured and make use of a shared okHttp instance
                        https://coil-kt.github.io/coil/
                     */
                    moviePoster.apply {
                        load(TMDB_IMAGE_BASE_URL + posterPath) {
                            crossfade(true)
                            // an easier on the eye placeholder could/should go here
                            placeholder(R.drawable.ic_launcher_foreground)
                        }
                        contentDescription = title
                        clipToOutline = true
                    }
                    movieTitle.text = title
                    movieReleaseDate.text = releaseDate
                    movieRatingPercentage.text = resources.getString(R.string.voteAvergaePercentage, voteAverage)
                    movieFavouriteButton.isChecked = isFavourite
                    movieFavouriteButton.setOnCheckedChangeListener { _, nowChecked ->
                        onClickFavourite(movie.id, nowChecked)
                    }
                }
            }
        }
    }

}

data class MovieListItem(
    val id: String,
    val title: String,
    val posterPath: String,
    val voteAverage: Int,
    val releaseDate: String,
    val isFavourite: Boolean,
    val onClickFavourite: (String, Boolean) -> Unit
)
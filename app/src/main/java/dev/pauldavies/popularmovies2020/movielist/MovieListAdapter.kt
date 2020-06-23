package dev.pauldavies.popularmovies2020.movielist

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.pauldavies.popularmovies2020.R
import kotlinx.android.synthetic.main.item_movie_list.view.*

val itemCallback = object : DiffUtil.ItemCallback<MovieListItem>() {
    override fun areItemsTheSame(old: MovieListItem, new: MovieListItem) = old.id == new.id
    override fun areContentsTheSame(old: MovieListItem, new: MovieListItem) = old == new
}

internal class MovieListAdapter :
    ListAdapter<MovieListItem, MovieListAdapter.MovieViewHolder>(itemCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        return MovieViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_movie_list, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    class MovieViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        fun bind(movie: MovieListItem) {
            itemView.movieTitle.text = movie.title
        }
    }

}

data class MovieListItem(val id: String, val title: String)
package dev.pauldavies.popularmovies2020.movielist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.pauldavies.popularmovies2020.R
import kotlinx.android.synthetic.main.fragment_movie_list.*

@AndroidEntryPoint
class MovieListFragment : Fragment(R.layout.fragment_movie_list) {

    private val viewModel: MovieListViewModel by viewModels()
    private val movieListAdapter = MovieListAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieListRecyclerView.apply {
            adapter = movieListAdapter
            layoutManager = LinearLayoutManager(context)
        }

        viewModel.state.observe(viewLifecycleOwner, Observer { state ->
            when (state) {
                is MovieListViewModel.State.Loading -> {
                    movieListLoadingProgress.visible()
                    movieListRecyclerView.gone()
                }
                is MovieListViewModel.State.Loaded -> {
                    movieListLoadingProgress.gone()
                    movieListRecyclerView.visible()
                    movieListAdapter.submitList(state.movieItems)
                }
            }
        })
    }
}

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun View.gone() {
    this.visibility = View.GONE
}
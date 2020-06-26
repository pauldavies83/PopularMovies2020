package dev.pauldavies.popularmovies2020.movielist

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.GridLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import dev.pauldavies.popularmovies2020.R
import kotlinx.android.synthetic.main.fragment_movie_list.*
import kotlinx.coroutines.launch

const val NUMBER_OF_GRID_COLUMNS = 2

@AndroidEntryPoint
class MovieListFragment : Fragment(R.layout.fragment_movie_list) {

    private val viewModel: MovieListViewModel by viewModels()
    private val movieListAdapter = MovieListAdapter()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieListRecyclerView.apply {
            adapter = movieListAdapter
            layoutManager = GridLayoutManager(context, NUMBER_OF_GRID_COLUMNS)
        }

        movieListAdapter.addLoadStateListener { loadState ->
            movieListRecyclerView.isVisible = loadState.refresh is LoadState.NotLoading
            movieListLoadingProgress.isVisible = loadState.refresh is LoadState.Loading
        }

        viewModel.movieListItems.observe(viewLifecycleOwner, Observer { movieListItems ->
            lifecycleScope.launch {
                movieListAdapter.submitData(movieListItems)
            }
        })
    }
}
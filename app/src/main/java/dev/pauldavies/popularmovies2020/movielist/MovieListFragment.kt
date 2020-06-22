package dev.pauldavies.popularmovies2020.movielist

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import dagger.hilt.android.AndroidEntryPoint
import dev.pauldavies.popularmovies2020.R
import kotlinx.android.synthetic.main.fragment_movie_list.*

@AndroidEntryPoint class MovieListFragment : Fragment(R.layout.fragment_movie_list) {

    private val viewModel: MovieListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.state.observe(viewLifecycleOwner, Observer { state ->
            movieTitle.text = state.movieItems.lastOrNull()
        })
    }
}
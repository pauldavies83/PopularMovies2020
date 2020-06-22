package dev.pauldavies.popularmovies2020.movielist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.pauldavies.popularmovies2020.data.MovieRepository

internal class MovieListViewModel @ViewModelInject constructor(
    movieRepository: MovieRepository
) : ViewModel() {

    val state: MutableLiveData<State> = MutableLiveData(State())

    init {
        state.postValue(State(movieItems = movieRepository.movies().map { it.title }))
    }

    data class State(val movieItems: List<String> = emptyList())
}
package dev.pauldavies.popularmovies2020.movielist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import dev.pauldavies.popularmovies2020.repository.MovieRepository

internal class MovieListViewModel @ViewModelInject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    val state: MutableLiveData<State> = MutableLiveData(State())

    init {
        viewModelScope.launch {
            state.postValue(
                State(
                    movieItems = movieRepository.popularMovies().map {
                        MovieListItem(it.title, it.title)
                    }
                )
            )
        }
    }

    data class State(val movieItems: List<MovieListItem> = emptyList())
}
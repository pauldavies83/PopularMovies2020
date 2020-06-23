package dev.pauldavies.popularmovies2020.movielist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import dev.pauldavies.popularmovies2020.repository.MovieRepository
import kotlinx.coroutines.CoroutineDispatcher

internal class MovieListViewModel @ViewModelInject constructor(
    private val movieRepository: MovieRepository,
    defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    val state: MutableLiveData<State> = MutableLiveData(State.Loading)

    init {
        viewModelScope.launch(defaultDispatcher) {
            state.postValue(
                State.Loaded(
                    movieItems = movieRepository.popularMovies().map {
                        MovieListItem(it.title, it.title)
                    }
                )
            )
        }
    }

    sealed class State {
        object Loading: State()
        data class Loaded(val movieItems: List<MovieListItem> = emptyList()): State()
    }
}
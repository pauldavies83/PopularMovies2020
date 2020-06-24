package dev.pauldavies.popularmovies2020.movielist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.pauldavies.popularmovies2020.repository.Movie
import kotlinx.coroutines.launch
import dev.pauldavies.popularmovies2020.repository.MovieRepository
import kotlinx.coroutines.CoroutineDispatcher
import java.time.format.DateTimeFormatter
import java.util.*

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
                        it.toMovieListItem()
                    }
                )
            )
        }
    }

    private fun Movie.toMovieListItem(): MovieListItem = MovieListItem(
        id = id,
        title = title,
        voteAverage = voteAverage,
        releaseDate = releaseDate.format(
            DateTimeFormatter.ofPattern("dd-MMM-yyyy", Locale.ENGLISH)
        )
    )

    sealed class State {
        object Loading: State()
        data class Loaded(val movieItems: List<MovieListItem> = emptyList()): State()
    }
}
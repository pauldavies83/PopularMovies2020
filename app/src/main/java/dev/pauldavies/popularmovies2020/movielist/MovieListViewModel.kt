package dev.pauldavies.popularmovies2020.movielist

import androidx.annotation.VisibleForTesting
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.PagingData
import dev.pauldavies.popularmovies2020.repository.Movie
import dev.pauldavies.popularmovies2020.repository.MovieRepository
import kotlinx.coroutines.CoroutineDispatcher
import java.time.format.DateTimeFormatter
import java.util.*

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
internal val DATE_FORMAT_SHORT_MONTH_SPACE_SEPARATOR =
    DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH)

internal class MovieListViewModel @ViewModelInject constructor(
    private val movieRepository: MovieRepository,
    defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    val state: LiveData<State>

    init {
        state = movieRepository.popularMovies().map { pagingData ->
            State.Loaded(
                pagingData.map { it.toMovieListItem() }
            )
        }
    }

    private fun Movie.toMovieListItem(): MovieListItem = MovieListItem(
        id = id,
        title = title,
        posterPath = posterPath,
        voteAverage = voteAverage,
        releaseDate = releaseDate.format(DATE_FORMAT_SHORT_MONTH_SPACE_SEPARATOR)
    )

    sealed class State {
        object Loading: State()
        data class Loaded(val movieItems: PagingData<MovieListItem>): State()
    }
}
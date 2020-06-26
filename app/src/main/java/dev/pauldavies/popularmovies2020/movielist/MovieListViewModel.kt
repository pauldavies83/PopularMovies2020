package dev.pauldavies.popularmovies2020.movielist

import androidx.annotation.VisibleForTesting
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.*
import dev.pauldavies.popularmovies2020.repository.Movie
import dev.pauldavies.popularmovies2020.repository.MovieRepository
import dev.pauldavies.popularmovies2020.repository.MoviesDataSource
import java.time.format.DateTimeFormatter
import java.util.*

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
internal val DATE_FORMAT_SHORT_MONTH_SPACE_SEPARATOR =
    DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.ENGLISH)

@VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
internal const val TMBD_PAGE_SIZE = 20 // this should be configurable in production-ready code

internal class MovieListViewModel @ViewModelInject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    val movieListItems = Pager(
        config = PagingConfig(pageSize = TMBD_PAGE_SIZE),
        pagingSourceFactory = { MoviesDataSource(movieRepository) }
    ).liveData.map { movies ->
        movies.map {
            it.toMovieListItem(onClick = ::onFavouriteClicked)
        }
    }

    @VisibleForTesting(otherwise = VisibleForTesting.PRIVATE)
    internal fun onFavouriteClicked(movieId: String, addAsFavourite: Boolean) {
        if (addAsFavourite) {
            movieRepository.setMovieAsFavourite(movieId)
        } else {
            movieRepository.removeMovieAsFavourite(movieId)
        }
    }
}

internal fun Movie.toMovieListItem(onClick: (String, Boolean) -> Unit) = MovieListItem(
    id = id,
    title = title,
    posterPath = posterPath,
    voteAverage = voteAverage,
    releaseDate = releaseDate.format(DATE_FORMAT_SHORT_MONTH_SPACE_SEPARATOR),
    isFavourite = isFavourite,
    onClickFavourite = onClick
)
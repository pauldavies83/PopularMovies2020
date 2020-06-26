package dev.pauldavies.popularmovies2020.repository

import dev.pauldavies.popularmovies2020.api.ApiMovie
import dev.pauldavies.popularmovies2020.api.TmdbApi
import dev.pauldavies.popularmovies2020.persistence.FavouriteMovieStorage
import java.time.LocalDate
import javax.inject.Inject
import kotlin.math.roundToInt

internal class MovieRepository @Inject constructor(
    private val tmdbApi: TmdbApi,
    private val favouriteMovieStorage: FavouriteMovieStorage
) {

    suspend fun popularMoviesPage(page: Int): List<Movie> {
        return tmdbApi.getPopularMovies(page).results.map { it.toMovie() }
    }

    fun isMovieFavourite(movieId: String): Boolean = favouriteMovieStorage.isMovieFavourite(movieId)

    fun setMovieAsFavourite(movieId: String) {
        favouriteMovieStorage.setMovieAsFavourite(movieId)
    }

    fun removeMovieAsFavourite(movieId: String) {
        favouriteMovieStorage.removeMovieAsFavourite(movieId)
    }
}

private fun ApiMovie.toMovie(isFavourite: Boolean = false): Movie {
    return Movie(
        id = id,
        title = title,
        posterPath = poster_path,
        voteAverage = (vote_average * 10).roundToInt(),
        releaseDate = LocalDate.parse(release_date),
        isFavourite = isFavourite
    )
}

data class Movie(
    val id: String,
    val title: String,
    val posterPath: String,
    val voteAverage: Int,
    val releaseDate: LocalDate,
    val isFavourite: Boolean
)
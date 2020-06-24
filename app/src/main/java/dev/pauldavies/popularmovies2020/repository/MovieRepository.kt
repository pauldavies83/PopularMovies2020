package dev.pauldavies.popularmovies2020.repository

import dev.pauldavies.popularmovies2020.api.ApiMovie
import dev.pauldavies.popularmovies2020.api.TmdbApi
import java.time.LocalDate
import javax.inject.Inject
import kotlin.math.roundToInt

internal class MovieRepository @Inject constructor(
    private val tmdbApi: TmdbApi
) {

    suspend fun popularMovies(): List<Movie> {
        return tmdbApi
            .getPopularMovies()
            .results.map { it.toMovie() }
    }
}

private fun ApiMovie.toMovie(): Movie {
    return Movie(
        id = id,
        title = title,
        posterPath = poster_path,
        voteAverage = (vote_average * 10).roundToInt(),
        releaseDate =  LocalDate.parse(release_date)
    )
}

data class Movie(
    val id: String,
    val title: String,
    val posterPath: String,
    val voteAverage: Int,
    val releaseDate: LocalDate
)
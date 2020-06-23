package dev.pauldavies.popularmovies2020.repository

import dev.pauldavies.popularmovies2020.api.TmdbApi
import javax.inject.Inject

internal class MovieRepository @Inject constructor(
    private val tmdbApi: TmdbApi
) {

    suspend fun popularMovies(): List<Movie> {
        return tmdbApi.getPopularMovies().results.map { Movie(it.title) }
    }
}

data class Movie(val title: String)
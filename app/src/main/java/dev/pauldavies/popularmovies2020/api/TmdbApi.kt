package dev.pauldavies.popularmovies2020.api

import kotlinx.serialization.Serializable
import retrofit2.http.GET

interface TmdbApi {

    @GET("popular")
    suspend fun getPopularMovies(): ApiMovieResponse
}

@Serializable
data class ApiMovieResponse(val results: List<ApiMovie>)

@Serializable
data class ApiMovie(
    val id: String,
    val title: String,
    val vote_average: Double,
    val release_date: String
)
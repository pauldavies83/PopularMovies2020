package dev.pauldavies.popularmovies2020.api

import kotlinx.serialization.Serializable
import retrofit2.http.GET
import retrofit2.http.Query

interface TmdbApi {

    @GET("popular")
    suspend fun getPopularMovies(@Query("page") page: Int): ApiMovieResponse
}

@Serializable
data class ApiMovieResponse(val results: List<ApiMovie>)

@Serializable
data class ApiMovie(
    val id: String,
    val title: String,
    val poster_path: String,
    val vote_average: Double,
    val release_date: String
)
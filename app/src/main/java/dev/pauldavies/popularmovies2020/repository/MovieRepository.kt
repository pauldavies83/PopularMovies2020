package dev.pauldavies.popularmovies2020.repository

import androidx.lifecycle.LiveData
import androidx.paging.*
import dev.pauldavies.popularmovies2020.api.ApiMovie
import dev.pauldavies.popularmovies2020.api.TmdbApi
import retrofit2.HttpException
import java.io.IOException
import java.time.LocalDate
import javax.inject.Inject
import kotlin.math.roundToInt

internal class MovieRepository @Inject constructor(
    private val tmdbApi: TmdbApi
) {

    fun popularMovies(): LiveData<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20),
            pagingSourceFactory = { MoviesDataSource(tmdbApi) }
        ).liveData
    }
}

internal class MoviesDataSource (private val tmdbApi: TmdbApi) : PagingSource<Int, Movie>() {
    private val STARTING_PAGE_INDEX = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = tmdbApi
                .getPopularMovies(page)
                .results.map { it.toMovie() }

            LoadResult.Page(
                data = response,
                prevKey = if (page == STARTING_PAGE_INDEX) null else page - 1,
                nextKey = if (response.isEmpty()) null else page + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
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
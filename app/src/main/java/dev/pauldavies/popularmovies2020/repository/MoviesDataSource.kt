    package dev.pauldavies.popularmovies2020.repository

import androidx.paging.PagingSource
import retrofit2.HttpException
import java.io.IOException

internal class MoviesDataSource(
    private val moviesRepository: MovieRepository
) : PagingSource<Int, Movie>() {
    private val STARTING_PAGE_INDEX = 1

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        val page = params.key ?: STARTING_PAGE_INDEX
        return try {
            val response = moviesRepository.popularMoviesPage(page).map { movie ->
                /** Querying the repo for each movie in turn could become expensive in production, but will work for now
                    A better way overall would be to cache every movie in a database after pulling it
                    from the network and load our pages from the database
                */
                movie.copy(isFavourite = moviesRepository.isMovieFavourite(movie.id))
            }
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
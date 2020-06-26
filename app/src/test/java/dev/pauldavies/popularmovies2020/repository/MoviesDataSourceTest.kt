package dev.pauldavies.popularmovies2020.repository

import androidx.paging.PagingSource
import com.nhaarman.mockitokotlin2.*
import dev.pauldavies.popularmovies2020.TestCoroutineRule
import dev.pauldavies.popularmovies2020.movielist.TMBD_PAGE_SIZE
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test

class MoviesDataSourceTest {

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val firstPage = 1
    private val movieId1 = "movieId1"
    private val movieId2 = "movieId2"

    private val movie1 = Movie(
        id = movieId1,
        title = "movieTitle",
        posterPath = "moviePosterPath",
        voteAverage = 57,
        releaseDate = mock(),
        isFavourite = false
    )
    private val movie2 = Movie(
        id = movieId2,
        title = "movieTitle",
        posterPath = "moviePosterPath",
        voteAverage = 57,
        releaseDate = mock(),
        isFavourite = false
    )

    private val movieRepository = mock<MovieRepository>()

    private val dataSource by lazy {
        MoviesDataSource(moviesRepository = movieRepository)
    }

    @Test
    fun `for valid page, movie list wrapped into paging data source`() = testCoroutineRule.runBlockingTest {
        movieRepository.stub {
            onBlocking { it.popularMoviesPage(any()) } doReturn(listOf(movie1))
            on { it.isMovieFavourite(movieId1) } doReturn(false)
        }

        val loadResult = dataSourceLoadResult()

        require(loadResult is PagingSource.LoadResult.Page)
        assertEquals(listOf(movie1), loadResult.data)
    }

    @Test
    fun `when movie is favourite, mark favourite in paging data source`() = testCoroutineRule.runBlockingTest {
        movieRepository.stub {
            onBlocking { it.popularMoviesPage(any()) } doReturn(listOf(movie2))
            on { it.isMovieFavourite(movieId2) } doReturn(true)
        }

        val loadResult = dataSourceLoadResult()

        require(loadResult is PagingSource.LoadResult.Page)
        assertEquals(listOf(movie2.copy(isFavourite = true)), loadResult.data)
    }

    private suspend fun dataSourceLoadResult(): PagingSource.LoadResult<Int, Movie> {
        return dataSource.load(
            PagingSource.LoadParams.Refresh(
                key = firstPage,
                pageSize = TMBD_PAGE_SIZE,
                placeholdersEnabled = false,
                loadSize = TMBD_PAGE_SIZE
            )
        )
    }
}
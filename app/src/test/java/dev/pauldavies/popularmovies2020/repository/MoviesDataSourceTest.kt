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
    private val movie = Movie(
        id = "movieId",
        title = "movieTitle",
        posterPath = "moviePosterPath",
        voteAverage = 57,
        releaseDate = mock()
    )

    private val movieRepository = mock<MovieRepository> {
        onBlocking { it.popularMoviesPage(any()) } doReturn(listOf(movie))
    }

    private val dataSource by lazy {
        MoviesDataSource(moviesRepository = movieRepository)
    }

    @Test
    fun `for valid page, movie list wrapped into paging data source`() = testCoroutineRule.runBlockingTest {
        val loadResult = dataSource.load(PagingSource.LoadParams.Refresh(
            key = firstPage,
            pageSize = TMBD_PAGE_SIZE,
            placeholdersEnabled = false,
            loadSize = TMBD_PAGE_SIZE
        ))

        require(loadResult is PagingSource.LoadResult.Page)
        assertEquals(listOf(movie), loadResult.data)
    }
}
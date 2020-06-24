package dev.pauldavies.popularmovies2020.movielist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import dev.pauldavies.popularmovies2020.TestCoroutineRule
import dev.pauldavies.popularmovies2020.repository.Movie
import dev.pauldavies.popularmovies2020.repository.MovieRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

class MovieListViewModelTest {

    @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule val testCoroutineRule = TestCoroutineRule()

    private val movieId = "movieId"
    private val movieTitle = "movieTitle"
    private val movieVoteAverage = 59
    private val movieReleaseDateApi = "2019-11-01"
    private val movieReleaseDateFormatted = "01-Nov-2019"

    private val movie = Movie(
        id = movieId,
        title = movieTitle,
        voteAverage = movieVoteAverage,
        releaseDate = LocalDate.parse(movieReleaseDateApi)
    )
    private val expectedMovieListItem = MovieListItem(
        id = movieId,
        title = movieTitle,
        voteAverage = movieVoteAverage,
        releaseDate = movieReleaseDateFormatted
    )

    private val movieRepository = mock<MovieRepository> {
        onBlocking { it.popularMovies() } doReturn(listOf(movie))
    }

    private val viewModel by lazy {
        MovieListViewModel(movieRepository, testCoroutineRule.testDispatcher)
    }

    @Test
    fun `loading state shown initially`() = testCoroutineRule.runBlockingTest {
        testCoroutineRule.pauseDispatcher()
        viewModel.apply {
            assertTrue(state.requireValue() is MovieListViewModel.State.Loading)
        }
    }

    @Test
    fun `loaded movies bound into emitted state`() = testCoroutineRule.runBlockingTest {
        viewModel.apply {
            val loaded = state.requireValue() as MovieListViewModel.State.Loaded
            assertEquals(expectedMovieListItem, loaded.movieItems.first())
        }
    }
}

fun <T : Any> LiveData<T>.requireValue(): T = value!!
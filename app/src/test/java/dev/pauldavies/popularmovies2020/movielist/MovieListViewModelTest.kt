package dev.pauldavies.popularmovies2020.movielist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.PagingData
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import dev.pauldavies.popularmovies2020.TestCoroutineRule
import dev.pauldavies.popularmovies2020.api.ApiMovie
import dev.pauldavies.popularmovies2020.api.ApiMovieResponse
import dev.pauldavies.popularmovies2020.api.TmdbApi
import dev.pauldavies.popularmovies2020.repository.Movie
import dev.pauldavies.popularmovies2020.repository.MovieRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

class MovieListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val movieId = "movieId"
    private val movieTitle = "movieTitle"
    private val moviePosterPath = "/urlto.jpg"
    private val movieVoteAverage = 59
    private val movieReleaseDate = LocalDate.parse("2019-11-01")
    private val movieReleaseDateFormatted =
        movieReleaseDate.format(DATE_FORMAT_SHORT_MONTH_SPACE_SEPARATOR)

    private val movie = Movie(
        id = movieId,
        title = movieTitle,
        posterPath = moviePosterPath,
        voteAverage = movieVoteAverage,
        releaseDate = movieReleaseDate
    )
    private val expectedMovieListItem = MovieListItem(
        id = movieId,
        title = movieTitle,
        posterPath = moviePosterPath,
        voteAverage = movieVoteAverage,
        releaseDate = movieReleaseDateFormatted
    )
    private val movieLiveData = MutableLiveData<PagingData<Movie>>()

    private val tmdbApi: TmdbApi = mock {
        onBlocking { getPopularMovies(any()) } doReturn (ApiMovieResponse(
            listOf(
                ApiMovie(movieId, movieTitle, moviePosterPath, 5.7, "2019-01-11")
            )
        ))
    }

    private val movieRepository = MovieRepository(tmdbApi)
//        onBlocking { it.popularMovies() } doReturn(movieLiveData)
//    }

    private val viewModel by lazy {
        MovieListViewModel(movieRepository, testCoroutineRule.testDispatcher)
    }

//    @Test
//    fun `loading state shown initially`() = testCoroutineRule.runBlockingTest {
//        testCoroutineRule.pauseDispatcher()
//        viewModel.apply {
//            assertTrue(state.requireValue() is MovieListViewModel.State.Loading)
//        }
//    }

    @Test
    fun `loaded movies bound into emitted state`() = testCoroutineRule.runBlockingTest {
            viewModel.apply {
                val loaded = state.requireValue() as MovieListViewModel.State.Loaded
                assertEquals(expectedMovieListItem, loaded.movieItems)
            }
    }
}

fun <T : Any> LiveData<T>.requireValue(): T = value!!
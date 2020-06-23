package dev.pauldavies.popularmovies2020.repository

import com.nhaarman.mockitokotlin2.*
import dev.pauldavies.popularmovies2020.api.ApiMovie
import dev.pauldavies.popularmovies2020.api.ApiMovieResponse
import dev.pauldavies.popularmovies2020.api.TmdbApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieRepositoryTest {

    private val testDispatcher = TestCoroutineDispatcher()

    @Before fun setup() { Dispatchers.setMain(testDispatcher) }
    @After fun tearDown() { Dispatchers.resetMain(); testDispatcher.cleanupTestCoroutines() }

    private val movieTitle = "movieTitle"
    private val successResponse = ApiMovieResponse(listOf(ApiMovie(movieTitle)))

    private val tmdbApi = mock<TmdbApi> {
        onBlocking { it.getPopularMovies() } doReturn(successResponse)
    }

    private val repository by lazy { MovieRepository(tmdbApi) }

    @Test fun successfulApiCall() = runBlockingTest {
        val result = repository.popularMovies()

        verify(tmdbApi).getPopularMovies()
        assertEquals(result, listOf(Movie(movieTitle)))
    }

    /**
     * This test just confirms the presence of a thrown exception.
     * Robust error handling from the API should be added later.
     */
    @Test(expected = Exception::class) fun errorApiCall() = runBlockingTest {
        tmdbApi.stub {
            onBlocking { it.getPopularMovies() } doAnswer { throw Exception() }
        }

        repository.popularMovies()

        verify(tmdbApi).getPopularMovies()
    }

}

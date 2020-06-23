package dev.pauldavies.popularmovies2020.repository

import com.nhaarman.mockitokotlin2.*
import dev.pauldavies.popularmovies2020.TestCoroutineRule
import dev.pauldavies.popularmovies2020.api.ApiMovie
import dev.pauldavies.popularmovies2020.api.ApiMovieResponse
import dev.pauldavies.popularmovies2020.api.TmdbApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class MovieRepositoryTest {

    @get:Rule val testCoroutineRule = TestCoroutineRule()

    private val movieTitle = "movieTitle"
    private val successResponse = ApiMovieResponse(listOf(ApiMovie(movieTitle)))

    private val tmdbApi = mock<TmdbApi> {
        onBlocking { it.getPopularMovies() } doReturn(successResponse)
    }

    private val repository by lazy { MovieRepository(tmdbApi) }

    @Test
    fun `successful Api Call`() = testCoroutineRule.runBlockingTest {
        val result = repository.popularMovies()

        verify(tmdbApi).getPopularMovies()
        assertEquals(result, listOf(Movie(movieTitle)))
    }

    /**
     * This test just confirms the presence of a thrown exception.
     * Robust error handling from the API should be added later.
     */
    @Test(expected = Exception::class)
    fun `error Api Call`() = testCoroutineRule.runBlockingTest {
        tmdbApi.stub {
            onBlocking { it.getPopularMovies() } doAnswer { throw Exception() }
        }

        repository.popularMovies()

        verify(tmdbApi).getPopularMovies()
    }

}

package dev.pauldavies.popularmovies2020.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.*
import dev.pauldavies.popularmovies2020.TestCoroutineRule
import dev.pauldavies.popularmovies2020.api.ApiMovie
import dev.pauldavies.popularmovies2020.api.ApiMovieResponse
import dev.pauldavies.popularmovies2020.api.TmdbApi
import dev.pauldavies.popularmovies2020.persistence.FavouriteMovieStorage
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate
import kotlin.math.roundToInt

class MovieRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val id = "id"
    private val title = "movieTitle"
    private val posterPath = "/urlto.jpg"
    private val voteAverageDouble = 5.9
    private val releaseDateString = "2019-01-01"
    private val releaseDateTime = LocalDate.parse(
        releaseDateString
    )

    private val successResponse = ApiMovieResponse(
        listOf(ApiMovie(id, title, posterPath, voteAverageDouble, releaseDateString))
    )

    private val tmdbApi = mock<TmdbApi> {
        onBlocking { it.getPopularMovies(1) } doReturn (successResponse)
    }

    private val favouriteMovieStorage = mock<FavouriteMovieStorage>()

    private val repository by lazy { MovieRepository(tmdbApi, favouriteMovieStorage) }

    @Test
    fun `successful Api Call`() = testCoroutineRule.runBlockingTest {
        val result = repository.popularMoviesPage(1)

        verify(tmdbApi).getPopularMovies(1)
        assertEquals(
            listOf(
                Movie(
                    id = id,
                    title = title,
                    posterPath = posterPath,
                    voteAverage = (voteAverageDouble * 10).roundToInt(),
                    releaseDate = releaseDateTime,
                    isFavourite = false
                )
            ),
            result
        )
    }

    /**
     * This test just confirms the presence of a thrown exception.
     * Robust error handling from the API should be added later.
     */
    @Test(expected = Exception::class)
    fun `error Api Call`() = testCoroutineRule.runBlockingTest {
        tmdbApi.stub {
            onBlocking { it.getPopularMovies(1) } doAnswer { throw Exception() }
        }

        repository.popularMoviesPage(1)

        verify(tmdbApi).getPopularMovies(1)
    }

    @Test
    fun `add favourite stored`() {
        repository.setMovieAsFavourite(id)

        verify(favouriteMovieStorage, times(1)).setMovieAsFavourite(id)
    }

    @Test
    fun `remove favourite stored`() {
        repository.removeMovieAsFavourite(id)

        verify(favouriteMovieStorage, times(1)).removeMovieAsFavourite(id)
    }
}

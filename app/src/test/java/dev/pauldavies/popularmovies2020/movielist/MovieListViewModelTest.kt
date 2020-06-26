package dev.pauldavies.popularmovies2020.movielist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import dev.pauldavies.popularmovies2020.TestCoroutineRule
import dev.pauldavies.popularmovies2020.repository.Movie
import org.junit.Assert.assertEquals
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

    @Test
    fun `loaded movies bound into emitted state`() {
        assertEquals(expectedMovieListItem, movie.toMovieListItem())
    }
}
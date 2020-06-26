package dev.pauldavies.popularmovies2020.movielist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import dev.pauldavies.popularmovies2020.TestCoroutineRule
import dev.pauldavies.popularmovies2020.repository.Movie
import dev.pauldavies.popularmovies2020.repository.MovieRepository
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate

class MovieListViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    private val noOpClickListener = { _: String, _:Boolean -> Unit }

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
        releaseDate = movieReleaseDate,
        isFavourite = false
    )
    private val expectedMovieListItem = MovieListItem(
        id = movieId,
        title = movieTitle,
        posterPath = moviePosterPath,
        voteAverage = movieVoteAverage,
        releaseDate = movieReleaseDateFormatted,
        isFavourite = false,
        onClickFavourite = noOpClickListener
    )

    private val movieRepository = mock<MovieRepository>()

    private val viewModel by lazy { MovieListViewModel(movieRepository) }

    @Test
    fun `loaded movies bound into emitted state`() {
        assertEquals(expectedMovieListItem, movie.toMovieListItem(noOpClickListener) )
    }

    @Test
    fun `when movie added as favourite, on favourite clicked, sent to repository`() {
        viewModel.onFavouriteClicked(movieId, true)

        verify(movieRepository, times(1)).setMovieAsFavourite(movieId)
    }

    @Test
    fun `when movie removed as favourite, on favourite clicked, sent to repository`() {
        viewModel.onFavouriteClicked(movieId, false)

        verify(movieRepository, times(1)).removeMovieAsFavourite(movieId)
    }
}
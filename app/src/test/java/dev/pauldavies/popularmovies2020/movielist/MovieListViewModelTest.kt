package dev.pauldavies.popularmovies2020.movielist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import dev.pauldavies.popularmovies2020.repository.Movie
import dev.pauldavies.popularmovies2020.repository.MovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MovieListViewModelTest {

    @Rule @JvmField var rule = InstantTaskExecutorRule()
    private val testDispatcher = TestCoroutineDispatcher()

    @Before fun setup() { Dispatchers.setMain(testDispatcher) }
    @After fun tearDown() { Dispatchers.resetMain(); testDispatcher.cleanupTestCoroutines() }

    private val movieTitle = "movieTitle"
    private val movieRepository = mock<MovieRepository> {
        onBlocking { it.popularMovies() } doReturn(listOf(Movie(movieTitle)))
    }

    private val viewModel by lazy {
        MovieListViewModel(movieRepository)
    }

    @Test
    fun `title is set correctly in first emitted state`() = runBlockingTest {
        viewModel.apply {
            assertEquals(state.requireValue().movieItems.first().title, movieTitle)
        }
    }
}

fun <T : Any> LiveData<T>.requireValue(): T = value!!
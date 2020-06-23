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

class MovieListViewModelTest {

    @get:Rule val instantTaskExecutorRule = InstantTaskExecutorRule()
    @get:Rule val testCoroutineRule = TestCoroutineRule()

    private val movieTitle = "movieTitle"
    private val movieRepository = mock<MovieRepository> {
        onBlocking { it.popularMovies() } doReturn(listOf(Movie(movieTitle)))
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
    fun `title is set correctly loaded emitted state`() = testCoroutineRule.runBlockingTest {
        viewModel.apply {
            val loaded = state.requireValue() as MovieListViewModel.State.Loaded
            assertEquals(loaded.movieItems.first().title, movieTitle)
        }
    }
}

fun <T : Any> LiveData<T>.requireValue(): T = value!!
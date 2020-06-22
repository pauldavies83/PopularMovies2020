package dev.pauldavies.popularmovies2020

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.whenever
import dev.pauldavies.popularmovies2020.data.Movie
import dev.pauldavies.popularmovies2020.data.MovieRepository
import dev.pauldavies.popularmovies2020.movielist.MovieListViewModel
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class MovieListViewModelTest {

    @Rule @JvmField var rule = InstantTaskExecutorRule()

    private val movieTitle = "movieTitle"
    private val movieRepository = mock<MovieRepository> {
        whenever(it.movies()).thenReturn(listOf(Movie(movieTitle)))
    }

    private val viewModel by lazy {
        MovieListViewModel(movieRepository)
    }

    @Test
    fun `title is set correctly in first emitted state`() {
        viewModel.apply {
            assertEquals(state.requireValue().movieItems.first(), movieTitle)
        }
    }
}

fun <T : Any> LiveData<T>.requireValue(): T = value!!
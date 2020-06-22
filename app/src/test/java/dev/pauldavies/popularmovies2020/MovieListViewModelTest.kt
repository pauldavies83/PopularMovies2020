package dev.pauldavies.popularmovies2020

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import dev.pauldavies.popularmovies2020.movielist.MovieListViewModel
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class MovieListViewModelTest {

    @Rule @JvmField var rule = InstantTaskExecutorRule()

    private val viewModel by lazy {
        MovieListViewModel(DependencyInjectedColours())
    }

    @Test
    fun `color is set correctly in first emitted state`() {
        viewModel.apply {
            assertEquals(state.requireValue().colorRes, R.color.colorPrimary)
        }
    }
}

fun <T : Any> LiveData<T>.requireValue(): T = value!!
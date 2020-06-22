package dev.pauldavies.popularmovies2020.movielist

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dev.pauldavies.popularmovies2020.DependencyInjectedColours

class MovieListViewModel @ViewModelInject constructor(
    colors: DependencyInjectedColours
) : ViewModel() {

    val state: MutableLiveData<State> = MutableLiveData(State())

    init {
        state.postValue(State(colorRes = colors.primary))
    }

    data class State(val colorRes: Int? = null)
}
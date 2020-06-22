package dev.pauldavies.popularmovies2020.data

import javax.inject.Inject

internal class MovieRepository @Inject constructor() {

    fun movies(): List<Movie> {
        return listOf(Movie("Toy Story"))
    }

}
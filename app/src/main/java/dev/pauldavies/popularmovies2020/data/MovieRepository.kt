package dev.pauldavies.popularmovies2020.data

import javax.inject.Inject

internal class MovieRepository @Inject constructor() {

    fun movies(): List<Movie> {
        return listOf(
            Movie("Toy Story"),
            Movie("Toy Story 2"),
            Movie("Toy Story 3"),
            Movie("Toy Story 4"),
            Movie("Finding Nemo"),
            Movie("Finding Dory")
        )
    }

}
package dev.pauldavies.popularmovies2020.persistence

import javax.inject.Inject
import javax.inject.Singleton

// quick in-memory storage of favourites - should be replaced by persistence in production
@Singleton class FavouriteMovieStorage @Inject constructor() {

    private val inMemoryStorage = mutableSetOf<String>()

    fun favouriteMovieIds(): Set<String> = inMemoryStorage

    fun isMovieFavourite(id: String) = inMemoryStorage.contains(id)

    fun setMovieAsFavourite(id: String) {
        inMemoryStorage.add(id)
    }

    fun removeMovieAsFavourite(id: String) {
        inMemoryStorage.remove(id)
    }
}
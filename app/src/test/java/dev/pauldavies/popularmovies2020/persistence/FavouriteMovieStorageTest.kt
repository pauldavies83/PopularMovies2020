package dev.pauldavies.popularmovies2020.persistence

import org.junit.Assert.*
import org.junit.Test

class FavouriteMovieStorageTest {

    private val idOne = "id_one"

    private val storage by lazy { FavouriteMovieStorage() }

    @Test
    fun `storage starts with no favourites`() {
        assertEquals(0, storage.favouriteMovieIds().size)
    }

    @Test
    fun `set as favourite stores movie id`() {
        storage.setMovieAsFavourite(idOne)
        assertTrue(storage.isMovieFavourite(idOne))
    }

    @Test
    fun `set as favourite more than once only stores movie id once`() {
        storage.setMovieAsFavourite(idOne)
        storage.setMovieAsFavourite(idOne)
        assertEquals(1, storage.favouriteMovieIds().count { it == idOne})
    }

    @Test
    fun `remove as favourite removes movie id`() {
        storage.setMovieAsFavourite(idOne)
        storage.removeMovieAsFavourite(idOne)
        assertFalse(storage.favouriteMovieIds().contains(idOne))
    }
}
package dev.pauldavies.popularmovies2020

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import dagger.hilt.android.AndroidEntryPoint
import dev.pauldavies.popularmovies2020.movielist.MovieListFragment

@AndroidEntryPoint class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportFragmentManager.commit { add(android.R.id.content, MovieListFragment()) }
    }
}
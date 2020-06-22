package dev.pauldavies.popularmovies2020

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

@AndroidEntryPoint class MainActivity : AppCompatActivity() {

    @Inject lateinit var colors: DependencyInjectedColours

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainActivityContent.setBackgroundColor(ContextCompat.getColor(this, colors.primary))
    }
}
package dev.pauldavies.popularmovies2020

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.rule.ActivityTestRule
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dev.pauldavies.popularmovies2020.di.NetworkModule
import dev.pauldavies.popularmovies2020.movielist.MovieListAdapter
import kotlinx.android.synthetic.main.fragment_movie_list.*
import org.hamcrest.Matchers.not
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
@UninstallModules(NetworkModule::class)
class MovieListActivityTest: MockWebServerTestCase() {

    @Module
    @InstallIn(ApplicationComponent::class)
    class TestNetworkModule : NetworkModule() {
        override fun baseUrl() = "http://localhost:8080/"
    }

    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    var testRule = ActivityTestRule(
        MainActivity::class.java, false, false
    )

    @Test
    fun listOfResultsShouldBeDisplayedWhenRetrieved() {
        testRule.launchActivity(null)

        onView(withId(R.id.movieListRecyclerView))
            .check(matches(isDisplayed()))
    }

    @Test
    fun whenScrollToBottomLoadMoreData() {
        testRule.launchActivity(null)

        mockWebServer.assertRequestParamValue("page", "1")

        onView(withId(R.id.movieListRecyclerView))
            .perform(RecyclerViewActions.scrollToPosition<MovieListAdapter.MovieViewHolder>(
                testRule.activity.movieListRecyclerView.adapter!!.itemCount - 1)
            )

        mockWebServer.assertRequestParamValue("page", "2")
    }

    @Test
    fun retryButtonShownWhenNetworkErrorOccurs() {
        mockWebServer.dispatcher = FourOhFourDispatcher

        testRule.launchActivity(null)

        onView(withId(R.id.movieListRecyclerView))
            .check(matches(not(isDisplayed())))

        onView(withId(R.id.movieListRetryButton))
            .check(matches(isDisplayed()))
    }

    @Test
    fun clickRetryButtonRetriesRequestForPageAndDisplaysWhenSuccessful() {
        mockWebServer.dispatcher = FourOhFourDispatcher

        testRule.launchActivity(null)

        mockWebServer.assertRequestParamValue("page", "1")

        mockWebServer.dispatcher = MovieListDispatcher

        onView(withId(R.id.movieListRetryButton)).perform(click())

        mockWebServer.assertRequestParamValue("page", "1")

        onView(withId(R.id.movieListRetryButton))
            .check(matches(not(isDisplayed())))

        onView(withId(R.id.movieListRecyclerView))
            .check(matches(isDisplayed()))
    }
}
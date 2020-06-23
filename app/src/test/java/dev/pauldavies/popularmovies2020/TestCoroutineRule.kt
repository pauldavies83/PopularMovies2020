package dev.pauldavies.popularmovies2020

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement

@OptIn(ExperimentalCoroutinesApi::class)
class TestCoroutineRule : TestRule {

    val testDispatcher = TestCoroutineDispatcher()

    override fun apply(base: Statement, description: Description) = object : Statement() {
        override fun evaluate() {
            Dispatchers.setMain(testDispatcher)

            base.evaluate()

            Dispatchers.resetMain()
            testDispatcher.cleanupTestCoroutines()
        }
    }

    fun pauseDispatcher() = testDispatcher.pauseDispatcher()
    fun resumeDispatcher() = testDispatcher.resumeDispatcher()

    fun runBlockingTest(block: suspend () -> Unit) {
        testDispatcher.runBlockingTest { block() }
    }
}
package dev.pauldavies.popularmovies2020.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

@Module
@InstallIn(ApplicationComponent::class)
class DispatchersModule {

    @Provides
    fun providesDefaultDispatcher(): CoroutineDispatcher = Dispatchers.Default

}
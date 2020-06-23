package dev.pauldavies.popularmovies2020.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dev.pauldavies.popularmovies2020.api.TmdbApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonConfiguration
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
class NetworkModule {

    /** the api key and baseUrl should be somewhere in external configuration (such as gradle properties)
        ive added this okHttpClient override to allow me to add the api_key query param
        to each request for speed of development, this is not production ready! :coneofshame:
     **/
    private val okHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request()
                val url = request.url
                    .newBuilder()
                    .addQueryParameter("api_key", "284ae5a1b4931c711e9ca264a38ae2e3")
                    .build()
                chain.proceed(
                    request.newBuilder().url(url).build()
                )
            }
            .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
            .build()
    }

    @Provides
    @Singleton
    fun providesTmdbService(): TmdbApi {
        return Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/movie/")
            .client(okHttpClient)
            .addConverterFactory(
                /** enable ignoreUnknownKeys to save having to parse every json key from the API,
                    this should not be considered production ready **/
                Json(JsonConfiguration.Stable.copy(ignoreUnknownKeys = true))
                    .asConverterFactory("application/json".toMediaType())
            )
            .build()
            .create(TmdbApi::class.java)
    }


}
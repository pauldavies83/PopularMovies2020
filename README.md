# PopularMovies2020

My goal here was to build a functional, performant infinite list of movies (pulled from the network), in a short amount of time. I've detailed in comments some less-than ideal trade-offs I've made in the interest of speed.

I've also treated this as a learning exercise, to see how quickly I can pull together a new app with "modern" Android libraries and techniques such as Coroutines, LiveData, PagingV3.

All dependencies are defined in build.gradle. A TMDB API key needs to be provided - add it to the placeholder in gradle.properties, and clean+build as normal.

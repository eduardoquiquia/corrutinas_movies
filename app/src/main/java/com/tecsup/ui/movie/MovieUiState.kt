package com.tecsup.ui.movie

import com.tecsup.data.model.Movie

data class MovieUiState(
    val isLoading: Boolean = false,
    val loadingMessage: String = "",
    val movies: List<Movie> = emptyList(),
    val searchQuery: String = "",
    val threadInfo: String = "Main Thread"
) {
    val filteredMovies: List<Movie>
        get() = if (searchQuery.isEmpty()) {
            movies
        } else {
            movies.filter { it.title.contains(searchQuery, ignoreCase = true) }
        }
}

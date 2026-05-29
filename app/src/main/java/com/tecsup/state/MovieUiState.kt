package com.tecsup.state

import androidx.compose.runtime.Immutable
import com.tecsup.model.Movie

@Immutable
data class MovieUiState(
    val isLoading: Boolean = false,
    val loadingMessage: String = "",
    val movies: List<Movie> = emptyList(),
    val searchQuery: String = "",
    val threadInfo: String = "Hilo Principal: Esperando...",
    val errorMessage: String? = null
) {
    val filteredMovies: List<Movie>
        get() = movies.filter { it.title.contains(searchQuery, ignoreCase = true) }
}

package com.tecsup.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.model.Movie
import com.tecsup.state.MovieUiState
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class MovieViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MovieUiState())
    val uiState: StateFlow<MovieUiState> = _uiState.asStateFlow()

    fun onSearchQueryChange(newQuery: String) {
        _uiState.update { it.copy(searchQuery = newQuery) }
    }

    fun toggleLike(movieId: Int) {
        _uiState.update { state ->
            val updatedMovies = state.movies.map {
                if (it.id == movieId) it.copy(isLiked = !it.isLiked) else it
            }
            state.copy(movies = updatedMovies)
        }
    }

    fun getMovieById(movieId: Int): Movie? {
        return _uiState.value.movies.find { it.id == movieId }
    }

    fun loadMovies() {
        if (_uiState.value.isLoading) return

        viewModelScope.launch {
            try {
                _uiState.update { 
                    it.copy(
                        isLoading = true, 
                        errorMessage = null,
                        movies = emptyList(),
                        threadInfo = "Hilo: ${Thread.currentThread().name} (Main)" 
                    ) 
                }

                withContext(Dispatchers.IO) {
                    simulateLoadingStep("📡 Conectando con servidor...", 1000)
                    simulateLoadingStep("⏳ Descargando catálogo...", 1500)
                    simulateLoadingStep("🎬 Procesando datos...", 1000)
                    simulateLoadingStep("🖼️ Preparando imágenes...", 1000)
                }

                val mockMovies = listOf(
                    Movie(1, "Inception", "Sci-Fi", 8.8, "https://i.pinimg.com/1200x/b0/ae/a4/b0aea49646879a043ad9f6ec3002e99f.jpg"),
                    Movie(2, "The Dark Knight", "Action", 9.0, "https://i.pinimg.com/1200x/5f/fe/79/5ffe79003530da912a82acac80be1426.jpg"),
                    Movie(3, "Interstellar", "Sci-Fi", 8.6, "https://i.pinimg.com/736x/1f/c9/33/1fc9333403a78e182b3921db0974f070.jpg"),
                    Movie(4, "Spider-Man: Across the Spider-Verse", "Animation", 8.9, "https://i.pinimg.com/736x/46/7d/94/467d9422c34a27a813e84d4da80a0cb1.jpg"),
                    Movie(6, "The Matrix", "Action", 8.7, "https://i.pinimg.com/736x/b3/73/ed/b373ed2c47e9c8ce59459871b9ac1808.jpg")
                )

                _uiState.update { 
                    it.copy(
                        movies = mockMovies,
                        loadingMessage = "✅ Catálogo cargado",
                        threadInfo = "Hilo: ${Thread.currentThread().name} (Main)"
                    ) 
                }

            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Error: ${e.localizedMessage}") }
            } finally {
                _uiState.update { it.copy(isLoading = false) }
            }
        }
    }

    private suspend fun simulateLoadingStep(message: String, time: Long) {
        _uiState.update { it.copy(loadingMessage = message, threadInfo = "Hilo: ${Thread.currentThread().name} (IO)") }
        delay(time)
    }
}

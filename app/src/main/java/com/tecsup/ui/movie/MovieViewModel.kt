package com.tecsup.ui.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.data.model.Movie
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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

    fun loadMovies() {
        // viewModelScope utiliza Dispatchers.Main por defecto.
        // launch inicia una nueva corrutina sin bloquear el hilo actual.
        viewModelScope.launch {
            _uiState.update { 
                it.copy(
                    isLoading = true, 
                    movies = emptyList(),
                    threadInfo = "Hilo: ${Thread.currentThread().name}" 
                ) 
            }

            // Simulamos tareas pesadas cambiando a Dispatchers.IO para no saturar el Main Thread
            // aunque delay() no es bloqueante, withContext es buena práctica para tareas de E/S.
            withContext(Dispatchers.IO) {
                simulateLoadingStep("📡 Conectando con servidor...", 1500)
                simulateLoadingStep("⏳ Descargando películas...", 2000)
                simulateLoadingStep("🎬 Procesando catálogo...", 1500)
                simulateLoadingStep("🖼️ Cargando imágenes...", 1000)
            }

            val mockMovies = listOf(
                Movie(1, "Inception", "Sci-Fi", 8.8, "https://i.pinimg.com/736x/31/5e/09/315e09d46f6c3784ed889938fc7b2d8e.jpg"),
                Movie(2, "The Dark Knight", "Action", 9.0, "https://i.pinimg.com/1200x/e4/01/27/e40127c71b4a07bb4eb02cd62f711dbb.jpg"),
                Movie(3, "Interstellar", "Sci-Fi", 8.6, "https://i.pinimg.com/474x/e0/10/1f/e0101f780e6ac931538cb06762e22c31.jpg"),
                Movie(4, "Spider-Man: Across the Spider-Verse", "Animation", 8.9, "https://i.pinimg.com/736x/8f/bb/d5/8fbbd577324cd69c588cdccab1d8d5bf.jpg"),
                Movie(5, "Pulp Fiction", "Crime", 8.9, "https://i.pinimg.com/1200x/c8/54/cc/c854ccce48c3dbbeb16a834046bce01c.jpg"),
                Movie(6, "The Matrix", "Action", 8.7, "https://i.pinimg.com/736x/f2/83/7b/f2837b8c138760e9eda426f539b2caac.jpg")
            )

            _uiState.update { 
                it.copy(
                    isLoading = false,
                    loadingMessage = "✅ Películas cargadas",
                    movies = mockMovies,
                    threadInfo = "Hilo Final: ${Thread.currentThread().name}"
                ) 
            }
        }
    }

    private suspend fun simulateLoadingStep(message: String, time: Long) {
        // delay() suspende la corrutina pero libera el hilo, permitiendo que la UI siga fluida.
        _uiState.update { it.copy(loadingMessage = message, threadInfo = "Hilo: ${Thread.currentThread().name}") }
        delay(time)
    }
}

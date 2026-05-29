package com.tecsup.ui.movie

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage

// Colores Cinematográficos
val DeepPurple = Color(0xFF120E21)
val SoftViolet = Color(0xFF332A54)
val NeonPurple = Color(0xFFBB86FC)
val DarkGrey = Color(0xFF1E1E1E)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieScreen(viewModel: MovieViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Movie Demo Coroutines", fontWeight = FontWeight.Bold) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DeepPurple,
                    titleContentColor = Color.White
                )
            )
        },
        containerColor = DeepPurple
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            // Buscador Moderno
            SearchBar(
                query = uiState.searchQuery,
                onQueryChange = { viewModel.onSearchQueryChange(it) }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Info de Corrutina e Hilos
            ThreadInfoCard(threadInfo = uiState.threadInfo)

            Spacer(modifier = Modifier.height(16.dp))

            // Botón de Carga
            Button(
                onClick = { viewModel.loadMovies() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = NeonPurple),
                shape = RoundedCornerShape(12.dp),
                enabled = !uiState.isLoading
            ) {
                Text(if (uiState.isLoading) "Cargando..." else "Cargar Películas", color = DeepPurple)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Estado de Carga
            AnimatedVisibility(visible = uiState.isLoading || uiState.loadingMessage.isNotEmpty()) {
                LoadingStatus(message = uiState.loadingMessage, isLoading = uiState.isLoading)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Lista de Películas
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(uiState.filteredMovies, key = { it.id }) { movie ->
                    MovieCard(
                        movie = movie,
                        onLikeClick = { viewModel.toggleLike(movie.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    TextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp)),
        placeholder = { Text("Buscar película...", color = Color.Gray) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = NeonPurple) },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = SoftViolet,
            unfocusedContainerColor = SoftViolet,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White
        ),
        singleLine = true
    )
}

@Composable
fun ThreadInfoCard(threadInfo: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = DarkGrey),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Text("Estado de Corrutina:", color = NeonPurple, fontSize = 12.sp, fontWeight = FontWeight.Bold)
            Text(threadInfo, color = Color.White, fontSize = 14.sp)
        }
    }
}

@Composable
fun LoadingStatus(message: String, isLoading: Boolean) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(24.dp),
                color = NeonPurple,
                strokeWidth = 2.dp
            )
            Spacer(modifier = Modifier.width(12.dp))
        }
        Text(message, color = Color.White, fontSize = 16.sp)
    }
}

@Composable
fun MovieCard(movie: com.tecsup.data.model.Movie, onLikeClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SoftViolet)
    ) {
        Row(
            modifier = Modifier
                .height(150.dp)
                .fillMaxWidth()
        ) {
            // Imagen con Coil
            AsyncImage(
                model = movie.imageUrl,
                contentDescription = movie.title,
                modifier = Modifier
                    .width(100.dp)
                    .fillMaxHeight(),
                contentScale = ContentScale.Crop
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(movie.title, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                    Text(movie.category, color = Color.LightGray, fontSize = 14.sp)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Star, contentDescription = null, tint = Color.Yellow, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(movie.rating.toString(), color = Color.White, fontSize = 14.sp)
                    }

                    IconButton(onClick = onLikeClick) {
                        Icon(
                            imageVector = if (movie.isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Like",
                            tint = if (movie.isLiked) Color.Red else Color.White
                        )
                    }
                }
            }
        }
    }
}

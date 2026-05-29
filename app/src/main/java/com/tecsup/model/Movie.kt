package com.tecsup.model

data class Movie(
    val id: Int,
    val title: String,
    val category: String,
    val rating: Double,
    val imageUrl: String,
    val description: String = "Esta es una descripción detallada de la película simulada para fines de demostración de navegación y corrutinas.",
    val isLiked: Boolean = false
)

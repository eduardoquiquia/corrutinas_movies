package com.tecsup.data.model

data class Movie(
    val id: Int,
    val title: String,
    val category: String,
    val rating: Double,
    val imageUrl: String,
    val isLiked: Boolean = false
)

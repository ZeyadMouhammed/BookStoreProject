package com.example.bookstoreproject

data class Book(
    val title: String,
    val author: String,
    val rating: Double = 0.0,
    val pages: Int = 0,
    val imageRes: Int, // Resource ID for the book cover
    val id: Int = 0,
    var isFavorite: Boolean = false // Mutable property to track favorite status
)
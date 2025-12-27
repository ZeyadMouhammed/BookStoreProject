package com.example.bookstoreproject

data class Book(
    val title: String,
    val author: String,
    val rating: Double,
    val pages: Int,
    val imageRes: Int,
    var id: Int = 0  // Added id field, default to 0 for new books
)

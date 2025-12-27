package com.example.bookstoreproject

data class Book(
    val title: String,
    val author: String,
    val rating: Double = 0.0,
    val pages: Int = 0,
    val imageRes: Int, // Resource ID for the book cover
    val id: Int = 0,
    var isFavorite: Boolean = false // Mutable property to track favorite status
) {
    // Override equals and hashCode to only compare by id
    // This helps DiffUtil detect changes properly
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as Book
        return id == other.id && isFavorite == other.isFavorite
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + isFavorite.hashCode()
        return result
    }
}
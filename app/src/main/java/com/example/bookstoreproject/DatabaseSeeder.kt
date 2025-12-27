package com.example.bookstoreproject

import android.content.Context

class DatabaseSeeder(private val context: Context) {

    private val dbHelper = MyDatabaseHelper(context)

    fun seedDatabase() {
        // Check if database is already seeded
        if (dbHelper.getAllBooks().isNotEmpty()) {
            return // Already seeded
        }

        seedBooks()
        seedAuthors()
    }

    private fun seedBooks() {
        val books = listOf(
            Book("Sparrow's Nest", "Edith Vincent", 5.0, 270, R.drawable.book_placeholder),
            Book("The Silent Lake", "John Hayes", 4.8, 320, R.drawable.book_placeholder),
            Book("Wild Forest", "Mila Rowan", 4.9, 190, R.drawable.book_placeholder),
            Book("Midnight Echo", "Sarah Collins", 4.7, 280, R.drawable.book_placeholder),
            Book("Desert Dreams", "Marcus Allen", 4.9, 310, R.drawable.book_placeholder),
            Book("Ocean's Call", "Luna Brooks", 5.0, 245, R.drawable.book_placeholder),
            Book("Mountain Path", "Kevin Turner", 4.6, 290, R.drawable.book_placeholder),
            Book("City Lights", "Emma Stone", 4.8, 260, R.drawable.book_placeholder),
            Book("Ancient Ruins", "David Chen", 4.9, 335, R.drawable.book_placeholder),
            Book("Starlight Journey", "Olivia Martinez", 5.0, 300, R.drawable.book_placeholder)
        )

        books.forEach { book ->
            dbHelper.insertBook(book)
        }
    }

    private fun seedAuthors() {
        val authors = listOf(
            Author("J.K. Rowling", R.drawable.book_placeholder),
            Author("George R.R. Martin", R.drawable.book_placeholder),
            Author("Agatha Christie", R.drawable.book_placeholder),
            Author("Mark Twain", R.drawable.book_placeholder),
            Author("Jane Austen", R.drawable.book_placeholder),
            Author("Ernest Hemingway", R.drawable.book_placeholder)
        )

        authors.forEach { author ->
            dbHelper.insertAuthor(author)
        }
    }

    fun clearDatabase() {
        dbHelper.clearCart()
        // Note: You might want to add methods to clear other tables if needed
    }
}
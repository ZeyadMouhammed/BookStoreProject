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
            Book(
                title = "Sparrow's Nest",
                author = "Edith Vincent",
                rating = 5.0,
                pages = 270,
                imageRes = R.drawable.book_placeholder,
                id = 0,
                isFavorite = false
            ),
            Book(
                title = "The Silent Lake",
                author = "John Hayes",
                rating = 4.8,
                pages = 320,
                imageRes = R.drawable.book_placeholder,
                id = 0,
                isFavorite = false
            ),
            Book(
                title = "Wild Forest",
                author = "Mila Rowan",
                rating = 4.9,
                pages = 190,
                imageRes = R.drawable.book_placeholder,
                id = 0,
                isFavorite = false
            ),
            Book(
                title = "Midnight Echo",
                author = "Sarah Collins",
                rating = 4.7,
                pages = 280,
                imageRes = R.drawable.book_placeholder,
                id = 0,
                isFavorite = false
            ),
            Book(
                title = "Desert Dreams",
                author = "Marcus Allen",
                rating = 4.9,
                pages = 310,
                imageRes = R.drawable.book_placeholder,
                id = 0,
                isFavorite = false
            ),
            Book(
                title = "Ocean's Call",
                author = "Luna Brooks",
                rating = 5.0,
                pages = 245,
                imageRes = R.drawable.book_placeholder,
                id = 0,
                isFavorite = false
            ),
            Book(
                title = "Mountain Path",
                author = "Kevin Turner",
                rating = 4.6,
                pages = 290,
                imageRes = R.drawable.book_placeholder,
                id = 0,
                isFavorite = false
            ),
            Book(
                title = "City Lights",
                author = "Emma Stone",
                rating = 4.8,
                pages = 260,
                imageRes = R.drawable.book_placeholder,
                id = 0,
                isFavorite = false
            ),
            Book(
                title = "Ancient Ruins",
                author = "David Chen",
                rating = 4.9,
                pages = 335,
                imageRes = R.drawable.book_placeholder,
                id = 0,
                isFavorite = false
            ),
            Book(
                title = "Starlight Journey",
                author = "Olivia Martinez",
                rating = 5.0,
                pages = 300,
                imageRes = R.drawable.book_placeholder,
                id = 0,
                isFavorite = false
            ),
            Book(
                title = "Harry Potter and the Sorcerer's Stone",
                author = "J.K. Rowling",
                rating = 4.9,
                pages = 309,
                imageRes = R.drawable.book_placeholder,
                id = 0,
                isFavorite = false
            ),
            Book(
                title = "A Game of Thrones",
                author = "George R.R. Martin",
                rating = 4.7,
                pages = 694,
                imageRes = R.drawable.book_placeholder,
                id = 0,
                isFavorite = false
            ),
            Book(
                title = "Murder on the Orient Express",
                author = "Agatha Christie",
                rating = 4.5,
                pages = 256,
                imageRes = R.drawable.book_placeholder,
                id = 0,
                isFavorite = false
            ),
            Book(
                title = "The Adventures of Tom Sawyer",
                author = "Mark Twain",
                rating = 4.2,
                pages = 274,
                imageRes = R.drawable.book_placeholder,
                id = 0,
                isFavorite = false
            ),
            Book(
                title = "Pride and Prejudice",
                author = "Jane Austen",
                rating = 4.6,
                pages = 432,
                imageRes = R.drawable.book_placeholder,
                id = 0,
                isFavorite = false
            ),
            Book(
                title = "The Old Man and the Sea",
                author = "Ernest Hemingway",
                rating = 4.4,
                pages = 127,
                imageRes = R.drawable.book_placeholder,
                id = 0,
                isFavorite = false
            )
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
            Author("Ernest Hemingway", R.drawable.book_placeholder),
            Author("Edith Vincent", R.drawable.book_placeholder),
            Author("John Hayes", R.drawable.book_placeholder),
            Author("Mila Rowan", R.drawable.book_placeholder),
            Author("Sarah Collins", R.drawable.book_placeholder),
            Author("Marcus Allen", R.drawable.book_placeholder),
            Author("Luna Brooks", R.drawable.book_placeholder),
            Author("Kevin Turner", R.drawable.book_placeholder),
            Author("Emma Stone", R.drawable.book_placeholder),
            Author("David Chen", R.drawable.book_placeholder),
            Author("Olivia Martinez", R.drawable.book_placeholder)
        )

        authors.forEach { author ->
            dbHelper.insertAuthor(author)
        }
    }

    fun clearDatabase() {
        dbHelper.clearCart()
        // Clear other tables
        val db = dbHelper.writableDatabase
        db.execSQL("DELETE FROM Favorites")
        db.execSQL("DELETE FROM Books")
        db.execSQL("DELETE FROM Authors")
    }

    fun reseedDatabase() {
        clearDatabase()
        seedDatabase()
    }

    // Add some books to "My Books" for testing
    fun seedMyBooks() {
        val allBooks = dbHelper.getAllBooks()
        if (allBooks.size >= 3) {
            // Add first 3 books to My Books (simulate purchase)
            dbHelper.addToMyBooks(allBooks[0].id)
            dbHelper.addToMyBooks(allBooks[1].id)
            dbHelper.addToMyBooks(allBooks[2].id)
        }
    }
}
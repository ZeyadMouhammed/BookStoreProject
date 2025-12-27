package com.example.bookstoreproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AuthorDetailsActivity : AppCompatActivity() {

    private lateinit var authorImage: ImageView
    private lateinit var authorName: TextView
    private lateinit var authorBio: TextView
    private lateinit var btnBack: ImageView
    private lateinit var rvAuthorBooks: RecyclerView
    private lateinit var dbHelper: MyDatabaseHelper
    private lateinit var booksAdapter: BooksAdapter
    private val authorBooks = mutableListOf<Book>()
    private var currentUserId: Int = -1  // Add this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_author_details)

        // Initialize database helper
        dbHelper = MyDatabaseHelper(this)

        // Get current user ID (retrieve from SharedPreferences or intent)
        currentUserId = getCurrentUserId()

        // Initialize views
        authorImage = findViewById(R.id.authorImage)
        authorName = findViewById(R.id.authorName)
        authorBio = findViewById(R.id.authorBio)
        btnBack = findViewById(R.id.btnBack)
        rvAuthorBooks = findViewById(R.id.rvAuthorBooks)

        btnBack.setOnClickListener { finish() }

        // Receive author data from intent
        val name = intent.getStringExtra("name")
        val bio = intent.getStringExtra("bio")
        val imageRes = intent.getIntExtra("imageRes", R.drawable.book_placeholder)

        // Get books by this author from database
        val allBooks = dbHelper.getAllBooks()
        authorBooks.addAll(allBooks.filter { it.author == name })

        // Check favorite status for each book WITH USER ID
        authorBooks.forEach { book ->
            book.isFavorite = dbHelper.isBookFavorited(book.id, currentUserId)
        }

        // Set author info
        authorName.text = name
        authorBio.text = bio ?: "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                "Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."
        authorImage.setImageResource(imageRes)

        // Setup RecyclerView for author's books
        if (authorBooks.isEmpty()) {
            rvAuthorBooks.visibility = View.GONE
            authorBio.append("\n\nThis author currently has no books in our catalog.")
        } else {
            rvAuthorBooks.visibility = View.VISIBLE

            rvAuthorBooks.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

            booksAdapter = BooksAdapter(
                onBookClick = { book ->
                    openBookDetails(book)
                },
                onFavoriteClick = { book ->
                    toggleFavorite(book)
                }
            )
            rvAuthorBooks.adapter = booksAdapter
            booksAdapter.submitList(authorBooks.toList())
        }
    }

    private fun getCurrentUserId(): Int {
        return UserSessionManager.getCurrentUserId(this)
    }

    private fun openBookDetails(book: Book) {
        val intent = Intent(this, BookDetailsActivity::class.java)
        intent.putExtra("bookId", book.id)
        intent.putExtra("title", book.title)
        intent.putExtra("author", book.author)
        intent.putExtra("rating", book.rating)
        intent.putExtra("pages", book.pages)
        intent.putExtra("coverRes", book.imageRes)
        startActivity(intent)
    }

    private fun toggleFavorite(book: Book) {
        if (book.id == 0) {
            Toast.makeText(this, "Cannot favorite sample books", Toast.LENGTH_SHORT).show()
            return
        }

        // Toggle in database WITH USER ID
        if (book.isFavorite) {
            dbHelper.removeFromFavorites(book.id, currentUserId)
            book.isFavorite = false
            Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show()
        } else {
            dbHelper.addToFavorites(book.id, currentUserId)
            book.isFavorite = true
            Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show()
        }

        // Find the book in the list and update it
        val index = authorBooks.indexOfFirst { it.id == book.id }
        if (index != -1) {
            authorBooks[index] = book.copy(isFavorite = book.isFavorite)

            booksAdapter.submitList(null)
            booksAdapter.submitList(authorBooks.toList())
        }
    }

    override fun onResume() {
        super.onResume()

        // Refresh user ID
        currentUserId = UserSessionManager.getCurrentUserId(this)

        // Refresh favorite status when returning to this activity
        if (::booksAdapter.isInitialized && authorBooks.isNotEmpty()) {
            authorBooks.forEach { book ->
                book.isFavorite = dbHelper.isBookFavorited(book.id, currentUserId)
            }
            booksAdapter.submitList(null)
            booksAdapter.submitList(authorBooks.toList())
        }
    }
}
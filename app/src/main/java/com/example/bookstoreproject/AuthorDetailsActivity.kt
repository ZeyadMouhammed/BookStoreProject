package com.example.bookstoreproject

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AuthorDetailsActivity : AppCompatActivity() {

    private lateinit var authorImage: ImageView
    private lateinit var authorName: TextView
    private lateinit var authorBio: TextView
    private lateinit var btnBack: ImageView
    private lateinit var rvAuthorBooks: RecyclerView
    private lateinit var bestBooksTitle: TextView
    private lateinit var dbHelper: MyDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_author_details)

        // Initialize database helper
        dbHelper = MyDatabaseHelper(this)

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
        val authorBooks = allBooks.filter { it.author == name }.toMutableList()

        // Check favorite status for each book
        authorBooks.forEach { book ->
            book.isFavorite = dbHelper.isBookFavorited(book.id)
        }

        // Set author info
        authorName.text = name
        authorBio.text = bio ?: "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                "Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."
        authorImage.setImageResource(imageRes)

        // Setup RecyclerView for author's books
        if (authorBooks.isEmpty()) {
            // Hide RecyclerView if no books
            rvAuthorBooks.visibility = View.GONE
            // You can optionally show a message
            authorBio.append("\n\nThis author currently has no books in our catalog.")
        } else {
            rvAuthorBooks.visibility = View.VISIBLE

            rvAuthorBooks.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

            val adapter = BooksAdapter(
                onBookClick = { book ->
                    openBookDetails(book)
                },
                onFavoriteClick = { book ->
                    toggleFavorite(book, authorBooks)
                }
            )
            rvAuthorBooks.adapter = adapter
            adapter.submitList(authorBooks.toList())
        }
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

    private fun toggleFavorite(book: Book, booksList: MutableList<Book>) {
        if (book.id == 0) {
            // This is a hardcoded book, can't favorite it
            return
        }

        if (book.isFavorite) {
            dbHelper.removeFromFavorites(book.id)
            book.isFavorite = false
        } else {
            dbHelper.addToFavorites(book.id)
            book.isFavorite = true
        }

        // Find the book in the list and update it
        val index = booksList.indexOfFirst { it.id == book.id }
        if (index != -1) {
            booksList[index] = book
            // Refresh the adapter
            (rvAuthorBooks.adapter as? BooksAdapter)?.submitList(booksList.toList())
        }
    }
}
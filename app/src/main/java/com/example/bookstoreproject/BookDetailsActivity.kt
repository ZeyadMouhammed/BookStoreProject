package com.example.bookstoreproject

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.button.MaterialButton

class BookDetailsActivity : AppCompatActivity() {

    private lateinit var bookCover: ImageView
    private lateinit var bookTitle: TextView
    private lateinit var bookAuthor: TextView
    private lateinit var bookRating: TextView
    private lateinit var bookPages: TextView
    private lateinit var bookPrice: TextView
    private lateinit var btnAddToBag: MaterialButton
    private lateinit var btnSample: MaterialButton

    private lateinit var dbHelper: MyDatabaseHelper
    private var currentUserId: Int = -1
    private var bookId: Int = -1
    private var isFree: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_details)

        // Initialize database
        dbHelper = MyDatabaseHelper(this)
        currentUserId = UserSessionManager.getCurrentUserId(this)

        // Initialize views
        bookCover = findViewById(R.id.bookCover)
        bookTitle = findViewById(R.id.bookTitle)
        bookAuthor = findViewById(R.id.bookAuthor)
        bookRating = findViewById(R.id.bookRating)
        bookPages = findViewById(R.id.bookPages)
        bookPrice = findViewById(R.id.bookPrice)
        btnAddToBag = findViewById(R.id.btnAddToBag)
        btnSample = findViewById(R.id.btnSample)

        // Get data from intent
        bookId = intent.getIntExtra("bookId", -1)
        val title = intent.getStringExtra("title")
        val author = intent.getStringExtra("author")
        val rating = intent.getDoubleExtra("rating", 0.0)
        val pages = intent.getIntExtra("pages", 0)
        val coverRes = intent.getIntExtra("coverRes", R.drawable.book_placeholder)

        // Display book info
        bookTitle.text = title
        bookAuthor.text = "By $author"
        bookRating.text = "⭐ $rating"
        bookPages.text = "$pages Pages"
        bookCover.setImageResource(coverRes)

        // Determine if book is free (books with rating 5.0 are free for demo)
        isFree = rating == 5.0

        // Update price text
        if (isFree) {
            bookPrice.text = "FREE"
            btnAddToBag.text = "Get Free Book"
        } else {
            bookPrice.text = "$9.99"
            btnAddToBag.text = "Add to Bag"
        }

        // Check if book is already owned
        if (bookId != -1 && dbHelper.isBookInMyBooks(bookId, currentUserId)) {
            btnAddToBag.isEnabled = false
            btnAddToBag.text = "Already Owned"
        }

        // Back button
        val btnBack: ImageView = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        // Sample button
        btnSample.setOnClickListener {
            Toast.makeText(this, "Sample feature coming soon!", Toast.LENGTH_SHORT).show()
        }

        // Add to Bag / Get Free Book button
        btnAddToBag.setOnClickListener {
            handleAddToBag()
        }
    }

    private fun handleAddToBag() {
        if (bookId == -1) {
            Toast.makeText(this, "Cannot add sample book", Toast.LENGTH_SHORT).show()
            return
        }

        if (isFree) {
            // Free book - add directly to My Books
            val result = dbHelper.addToMyBooks(bookId, currentUserId)

            if (result > 0) {
                Toast.makeText(this, "Free book added to your library!", Toast.LENGTH_SHORT).show()

                // Disable button
                btnAddToBag.isEnabled = false
                btnAddToBag.text = "Already Owned"
            } else if (result == -1L) {
                Toast.makeText(this, "You already own this book", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to add book", Toast.LENGTH_SHORT).show()
            }
        } else {
            // Paid book - add to cart (bag)
            val cartItems = dbHelper.getCartItems(currentUserId)
            val alreadyInCart = cartItems.any { it.first.id == bookId }

            if (alreadyInCart) {
                Toast.makeText(this, "Book already in your bag", Toast.LENGTH_SHORT).show()
                return
            }

            val result = dbHelper.addToCart(currentUserId, bookId, 1)

            if (result > 0) {
                Toast.makeText(this, "Added to bag successfully!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to add to bag", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
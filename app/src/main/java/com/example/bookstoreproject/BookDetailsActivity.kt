package com.example.bookstoreproject

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class BookDetailsActivity : AppCompatActivity() {

    private lateinit var bookCover: ImageView
    private lateinit var bookTitle: TextView
    private lateinit var bookAuthor: TextView
    private lateinit var bookRating: TextView
    private lateinit var bookPages: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_details)

        bookCover = findViewById(R.id.bookCover)
        bookTitle = findViewById(R.id.bookTitle)
        bookAuthor = findViewById(R.id.bookAuthor)
        bookRating = findViewById(R.id.bookRating)
        bookPages = findViewById(R.id.bookPages)

        val title = intent.getStringExtra("title")
        val author = intent.getStringExtra("author")
        val rating = intent.getDoubleExtra("rating", 0.0)
        val pages = intent.getIntExtra("pages", 0)
        val coverRes = intent.getIntExtra("coverRes", R.drawable.book_placeholder)

        bookTitle.text = title
        bookAuthor.text = "By $author"
        bookRating.text = "⭐ $rating"
        bookPages.text = "$pages Pages"
        bookCover.setImageResource(coverRes)

        val btnBack: ImageView = findViewById(R.id.btnBack)
        btnBack.setOnClickListener {
            finish() // This closes the activity and goes back
        }


    }
}

package com.example.bookstoreproject

import android.os.Bundle
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_author_details)

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
        // Hardcoded list of best books
        val books = listOf(
            Book(
                "Harry Potter and the Sorcerer's Stone",
                "J.K. Rowling",
                5.0,
                309,
                R.drawable.book_placeholder
            ),
            Book(
                "Harry Potter and the Chamber of Secrets",
                "J.K. Rowling",
                4.9,
                341,
                R.drawable.book_placeholder
            ),
            Book(
                "Harry Potter and the Prisoner of Azkaban",
                "J.K. Rowling",
                4.8,
                435,
                R.drawable.book_placeholder
            )
        )

        authorName.text = name
        authorBio.text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                "Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. " +
                "Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat."
        authorImage.setImageResource(imageRes)

        // Setup RecyclerView for author's best books
        rvAuthorBooks.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rvAuthorBooks.adapter = BookAdapter(books)
    }
}

package com.example.bookstoreproject

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HomeFragment : Fragment() {

    private lateinit var fictionView: View
    private lateinit var scienceView: View
    private lateinit var historyView: View

    private lateinit var rvAuthor: RecyclerView
    private lateinit var authorAdapter: AuthorAdapter
    private val authorList = mutableListOf<Author>()

    private lateinit var dbHelper: MyDatabaseHelper
    private lateinit var rvBestsellers: RecyclerView
    private lateinit var rvTopRated: RecyclerView
    private lateinit var bestsellerAdapter: BooksAdapter
    private lateinit var topRatedAdapter: BooksAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize database helper
        dbHelper = MyDatabaseHelper(requireContext())

        setupDrawer(view)
        setupCategories(view)
        setupBooksRecycler(view)
        setupTopRatedRecycler(view)
        setupAuthorsRecycler(view)
        setupTabBar(view)
    }

    // ---------------------------------------------------------
    // Drawer
    // ---------------------------------------------------------
    private fun setupDrawer(view: View) {
        val filterButton: View = view.findViewById(R.id.myImageButton)
        val drawerLayout: DrawerLayout = view.findViewById(R.id.drawer_layout)

        filterButton.setOnClickListener {
            drawerLayout.openDrawer(GravityCompat.START)
        }
    }

    // ---------------------------------------------------------
    // Categories (All / Science / History)
    // ---------------------------------------------------------
    private fun setupCategories(view: View) {
        fictionView = view.findViewById(R.id.includeFiction)
        scienceView = view.findViewById(R.id.includeScience)
        historyView = view.findViewById(R.id.includeHistory)

        setupCategory(fictionView, "All", R.drawable.ic_cart)
        setupCategory(scienceView, "Science", R.drawable.ic_category)
        setupCategory(historyView, "History", R.drawable.ic_filter)

        fictionView.setOnClickListener { selectCategory(fictionView) }
        scienceView.setOnClickListener { selectCategory(scienceView) }
        historyView.setOnClickListener { selectCategory(historyView) }

        selectCategory(fictionView) // Default
    }

    private fun setupCategory(view: View, text: String, iconRes: Int) {
        view.findViewById<TextView>(R.id.categoryText).text = text
        view.findViewById<ImageView>(R.id.categoryIcon).setImageResource(iconRes)
    }

    private fun selectCategory(selected: View) {
        val categories = listOf(fictionView, scienceView, historyView)

        categories.forEach { view ->
            val selectedState = view == selected
            view.isSelected = selectedState

            val textView = view.findViewById<TextView>(R.id.categoryText)
            val iconView = view.findViewById<ImageView>(R.id.categoryIcon)

            if (selectedState) {
                textView.setTextColor(Color.WHITE)
                iconView.setColorFilter(Color.WHITE)
            } else {
                val color = ContextCompat.getColor(requireContext(), R.color.primaryColor)
                textView.setTextColor(color)
                iconView.setColorFilter(color)
            }
        }
    }

    // ---------------------------------------------------------
    // Books Recycler (Horizontal) - Bestsellers
    // ---------------------------------------------------------
    private fun setupBooksRecycler(view: View) {
        rvBestsellers = view.findViewById(R.id.rvBestsellers)
        rvBestsellers.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

        // Load books from database
        val allBooks = dbHelper.getAllBooks()

        // Check favorite status for each book
        allBooks.forEach { book ->
            book.isFavorite = dbHelper.isBookFavorited(book.id)
        }

        // If no books in database, use sample data
        val booksToDisplay = if (allBooks.isEmpty()) {
            listOf(
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
                    title = "Book C",
                    author = "Author 3",
                    rating = 5.0,
                    pages = 150,
                    imageRes = R.drawable.book_placeholder,
                    id = 0,
                    isFavorite = false
                )
            )
        } else {
            allBooks.take(4) // Show first 4 books
        }

        bestsellerAdapter = BooksAdapter(
            onBookClick = { book ->
                openBookDetails(book)
            },
            onFavoriteClick = { book ->
                toggleFavorite(book)
            }
        )
        rvBestsellers.adapter = bestsellerAdapter
        bestsellerAdapter.submitList(booksToDisplay)
    }

    // ---------------------------------------------------------
    // Top Rated Recycler (Horizontal)
    // ---------------------------------------------------------
    private fun setupTopRatedRecycler(view: View) {
        rvTopRated = view.findViewById(R.id.rvTopRated)
        rvTopRated.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

        // Load books from database and sort by rating
        val allBooks = dbHelper.getAllBooks()
        val topRatedBooks = allBooks.sortedByDescending { it.rating }

        // Check favorite status
        topRatedBooks.forEach { book ->
            book.isFavorite = dbHelper.isBookFavorited(book.id)
        }

        // If no books, use sample data
        val booksToDisplay = if (topRatedBooks.isEmpty()) {
            listOf(
                Book(
                    title = "Book A",
                    author = "Author 1",
                    rating = 4.9,
                    pages = 250,
                    imageRes = R.drawable.book_placeholder,
                    id = 0,
                    isFavorite = false
                ),
                Book(
                    title = "Book B",
                    author = "Author 2",
                    rating = 4.8,
                    pages = 310,
                    imageRes = R.drawable.book_placeholder,
                    id = 0,
                    isFavorite = false
                ),
                Book(
                    title = "Book C",
                    author = "Author 3",
                    rating = 5.0,
                    pages = 150,
                    imageRes = R.drawable.book_placeholder,
                    id = 0,
                    isFavorite = false
                ),
                Book(
                    title = "Book D",
                    author = "Author 2",
                    rating = 5.0,
                    pages = 150,
                    imageRes = R.drawable.book_placeholder,
                    id = 0,
                    isFavorite = false
                )
            )
        } else {
            topRatedBooks.take(4) // Show top 4 rated books
        }

        topRatedAdapter = BooksAdapter(
            onBookClick = { book ->
                openBookDetails(book)
            },
            onFavoriteClick = { book ->
                toggleFavorite(book)
            }
        )
        rvTopRated.adapter = topRatedAdapter
        topRatedAdapter.submitList(booksToDisplay)
    }

    // ---------------------------------------------------------
    // Authors Recycler (Horizontal)
    // ---------------------------------------------------------
    private fun setupAuthorsRecycler(view: View) {
        rvAuthor = view.findViewById(R.id.rvAuthor)
        rvAuthor.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

        // Load authors from database
        val dbAuthors = dbHelper.getAllAuthors()

        // Clear existing list
        authorList.clear()

        // If no authors in database, use sample data
        if (dbAuthors.isEmpty()) {
            authorList.apply {
                add(Author("J.K. Rowling", R.drawable.book_placeholder))
                add(Author("George R.R. Martin", R.drawable.book_placeholder))
                add(Author("Agatha Christie", R.drawable.book_placeholder))
                add(Author("Mark Twain", R.drawable.book_placeholder))
            }
        } else {
            authorList.addAll(dbAuthors)
        }

        // Create adapter with click listener
        authorAdapter = AuthorAdapter(authorList) { author ->
            openAuthorDetails(author)
        }
        rvAuthor.adapter = authorAdapter
    }

    // ---------------------------------------------------------
    // TAB BAR (Free / Top Rated / New Release / Authors)
    // ---------------------------------------------------------
    private fun setupTabBar(view: View) {
        val tabFree: TextView = view.findViewById(R.id.tabFree)
        val tabTopRated: TextView = view.findViewById(R.id.tabTopRated)
        val tabNewRel: TextView = view.findViewById(R.id.tabNewRel)
        val tabAuthors: TextView = view.findViewById(R.id.tabAuthors)

        val tabs = listOf(tabFree, tabTopRated, tabNewRel, tabAuthors)

        fun selectTab(selected: TextView) {
            tabs.forEach { it.isSelected = (it == selected) }
        }

        tabFree.setOnClickListener { selectTab(tabFree) }
        tabTopRated.setOnClickListener { selectTab(tabTopRated) }
        tabNewRel.setOnClickListener { selectTab(tabNewRel) }
        tabAuthors.setOnClickListener { selectTab(tabAuthors) }

        selectTab(tabTopRated) // Default selected
    }

    // ---------------------------------------------------------
    // Helper Methods
    // ---------------------------------------------------------
    private fun toggleFavorite(book: Book) {
        if (book.id == 0) {
            // This is sample data, can't favorite
            Toast.makeText(requireContext(), "Cannot favorite sample books", Toast.LENGTH_SHORT).show()
            return
        }

        if (book.isFavorite) {
            dbHelper.removeFromFavorites(book.id)
            book.isFavorite = false
            Toast.makeText(requireContext(), "Removed from favorites", Toast.LENGTH_SHORT).show()
        } else {
            dbHelper.addToFavorites(book.id)
            book.isFavorite = true
            Toast.makeText(requireContext(), "Added to favorites", Toast.LENGTH_SHORT).show()
        }

        // Refresh both adapters
        bestsellerAdapter.notifyDataSetChanged()
        topRatedAdapter.notifyDataSetChanged()
    }

    private fun openBookDetails(book: Book) {
        val intent = Intent(requireContext(), BookDetailsActivity::class.java)
        intent.putExtra("bookId", book.id)
        intent.putExtra("title", book.title)
        intent.putExtra("author", book.author)
        intent.putExtra("rating", book.rating)
        intent.putExtra("pages", book.pages)
        intent.putExtra("coverRes", book.imageRes) // Changed from imageRes to coverRes
        startActivity(intent)
    }

    private fun openAuthorDetails(author: Author) {
        val intent = Intent(requireContext(), AuthorDetailsActivity::class.java)
        intent.putExtra("name", author.name)
        intent.putExtra("imageRes", author.imageResId)
        // You can add bio if you have it
        startActivity(intent)
    }

    override fun onResume() {
        super.onResume()
        // Reload data when returning to fragment
        view?.let {
            setupBooksRecycler(it)
            setupTopRatedRecycler(it)
            authorList.clear()
            setupAuthorsRecycler(it)
        }
    }
}
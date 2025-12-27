package com.example.bookstoreproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class BooksFragment : Fragment() {

    private lateinit var tabMyBooks: LinearLayout
    private lateinit var tabFavorite: LinearLayout
    private lateinit var myBooksContent: View
    private lateinit var favoriteContent: View

    private lateinit var recyclerViewMyBooks: RecyclerView
    private lateinit var recyclerViewFavorites: RecyclerView
    private lateinit var emptyStateMyBooks: View
    private lateinit var emptyStateFavorites: View

    private lateinit var dbHelper: MyDatabaseHelper
    private lateinit var myBooksAdapter: BooksAdapter
    private lateinit var favoritesAdapter: BooksAdapter

    private var myBooks = mutableListOf<Book>()
    private var favoriteBooks = mutableListOf<Book>()
    private var currentUserId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_books, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize database helper
        dbHelper = MyDatabaseHelper(requireContext())

        // Initialize views
        initializeViews(view)

        // Setup RecyclerViews
        setupRecyclerViews()

        // Setup tabs
        setupTabs()

        // Setup back button
        // Setup back button
        view.findViewById<ImageView>(R.id.backButtonBooks)?.setOnClickListener {
            // Navigate to home fragment
            parentFragmentManager.beginTransaction()
                .replace(R.id.mainContainer, HomeFragment())
                .commit()

            // Update bottom navigation
            (activity as? MainActivity)?.findViewById<com.google.android.material.bottomnavigation.BottomNavigationView>(
                R.id.bottomNav
            )?.selectedItemId = R.id.nav_home
        }

        // Default: show My Books
        setActiveTab(tabMyBooks)
    }

    override fun onResume() {
        super.onResume()

        // CRITICAL: Get fresh user ID every time fragment resumes
        currentUserId = UserSessionManager.getCurrentUserId(requireContext())

        Log.d("BooksFragment", "onResume - Current User ID: $currentUserId")

        // Check if user is logged in
        if (currentUserId == -1 || !UserSessionManager.isLoggedIn(requireContext())) {
            Toast.makeText(requireContext(), "Please login first", Toast.LENGTH_SHORT).show()
            requireActivity().finish()
            return
        }

        // Reload data for current user
        loadBooksFromDatabase()
    }

    private fun initializeViews(view: View) {
        tabMyBooks = view.findViewById(R.id.tabMyBooks)
        tabFavorite = view.findViewById(R.id.tabFavorite)
        myBooksContent = view.findViewById(R.id.myBooksContent)
        favoriteContent = view.findViewById(R.id.favoriteContent)
        recyclerViewMyBooks = view.findViewById(R.id.recyclerViewMyBooks)
        recyclerViewFavorites = view.findViewById(R.id.recyclerViewFavorites)
        emptyStateMyBooks = view.findViewById(R.id.emptyStateMyBooks)
        emptyStateFavorites = view.findViewById(R.id.emptyStateFavorites)
    }

    private fun setupRecyclerViews() {
        myBooksAdapter = BooksAdapter(
            onBookClick = { book ->
                openBookDetails(book)
            },
            onFavoriteClick = { book ->
                toggleFavorite(book)
            }
        )

        recyclerViewMyBooks.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = myBooksAdapter
        }

        favoritesAdapter = BooksAdapter(
            onBookClick = { book ->
                openBookDetails(book)
            },
            onFavoriteClick = { book ->
                toggleFavorite(book)
            }
        )

        recyclerViewFavorites.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = favoritesAdapter
        }
    }

    private fun setupTabs() {
        tabMyBooks.setOnClickListener {
            setActiveTab(tabMyBooks)
            myBooksContent.visibility = View.VISIBLE
            favoriteContent.visibility = View.GONE
        }

        tabFavorite.setOnClickListener {
            setActiveTab(tabFavorite)
            myBooksContent.visibility = View.GONE
            favoriteContent.visibility = View.VISIBLE
        }
    }

    private fun setActiveTab(selectedTab: LinearLayout) {
        tabMyBooks.isSelected = false
        tabFavorite.isSelected = false
        selectedTab.isSelected = true
    }

    private fun loadBooksFromDatabase() {
        Log.d("BooksFragment", "Loading books for user: $currentUserId")

        // Clear existing data FIRST
        myBooks.clear()
        favoriteBooks.clear()

        // Load purchased books (My Books)
        val userBooks = dbHelper.getMyBooks(currentUserId)
        Log.d("BooksFragment", "Found ${userBooks.size} books in My Books")

        userBooks.forEach { book ->
            book.isFavorite = dbHelper.isBookFavorited(book.id, currentUserId)
        }
        myBooks.addAll(userBooks)

        // Load favorite books
        val userFavorites = dbHelper.getFavoriteBooks(currentUserId)
        Log.d("BooksFragment", "Found ${userFavorites.size} favorite books")

        userFavorites.forEach { book ->
            book.isFavorite = true
        }
        favoriteBooks.addAll(userFavorites)

        // Update UI
        updateMyBooksUI()
        updateFavoritesUI()
    }

    private fun updateMyBooksUI() {
        if (myBooks.isEmpty()) {
            recyclerViewMyBooks.visibility = View.GONE
            emptyStateMyBooks.visibility = View.VISIBLE
        } else {
            recyclerViewMyBooks.visibility = View.VISIBLE
            emptyStateMyBooks.visibility = View.GONE
            myBooksAdapter.submitList(null)
            myBooksAdapter.submitList(myBooks.toList())
        }
    }

    private fun updateFavoritesUI() {
        if (favoriteBooks.isEmpty()) {
            recyclerViewFavorites.visibility = View.GONE
            emptyStateFavorites.visibility = View.VISIBLE
        } else {
            recyclerViewFavorites.visibility = View.VISIBLE
            emptyStateFavorites.visibility = View.GONE
            favoritesAdapter.submitList(null)
            favoritesAdapter.submitList(favoriteBooks.toList())
        }
    }

    private fun toggleFavorite(book: Book) {
        if (book.isFavorite) {
            // Remove from favorites
            dbHelper.removeFromFavorites(book.id, currentUserId)
            book.isFavorite = false
            favoriteBooks.removeAll { it.id == book.id }
            Toast.makeText(requireContext(), "Removed from favorites", Toast.LENGTH_SHORT).show()
        } else {
            // Add to favorites
            dbHelper.addToFavorites(book.id, currentUserId)
            book.isFavorite = true

            if (!favoriteBooks.any { it.id == book.id }) {
                favoriteBooks.add(book.copy(isFavorite = true))
            }
            Toast.makeText(requireContext(), "Added to favorites", Toast.LENGTH_SHORT).show()
        }

        // Update the book in myBooks list
        val myBooksIndex = myBooks.indexOfFirst { it.id == book.id }
        if (myBooksIndex != -1) {
            myBooks[myBooksIndex] = book.copy(isFavorite = book.isFavorite)
        }

        // Refresh both adapters
        updateMyBooksUI()
        updateFavoritesUI()

        Log.d("BooksFragment", "Favorite toggled for book ${book.id}, user $currentUserId")
    }

    private fun openBookDetails(book: Book) {
        val intent = Intent(requireContext(), BookDetailsActivity::class.java)
        intent.putExtra("bookId", book.id)
        intent.putExtra("title", book.title)
        intent.putExtra("author", book.author)
        intent.putExtra("rating", book.rating)
        intent.putExtra("pages", book.pages)
        intent.putExtra("coverRes", book.imageRes)
        startActivity(intent)
    }
}
package com.example.bookstoreproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
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

        // Load data from database
        loadBooksFromDatabase()

        // Setup tabs
        setupTabs()

        // Setup back button
        view.findViewById<ImageView>(R.id.backButtonBooks)?.setOnClickListener {
            requireActivity().onBackPressed()
        }

        // Default: show My Books
        setActiveTab(tabMyBooks)
    }

    private fun initializeViews(view: View) {
        // Tabs
        tabMyBooks = view.findViewById(R.id.tabMyBooks)
        tabFavorite = view.findViewById(R.id.tabFavorite)

        // Content containers
        myBooksContent = view.findViewById(R.id.myBooksContent)
        favoriteContent = view.findViewById(R.id.favoriteContent)

        // RecyclerViews
        recyclerViewMyBooks = view.findViewById(R.id.recyclerViewMyBooks)
        recyclerViewFavorites = view.findViewById(R.id.recyclerViewFavorites)

        // Empty states
        emptyStateMyBooks = view.findViewById(R.id.emptyStateMyBooks)
        emptyStateFavorites = view.findViewById(R.id.emptyStateFavorites)
    }

    private fun setupRecyclerViews() {
        // Setup My Books RecyclerView
        myBooksAdapter = BooksAdapter(
            onBookClick = { book ->
                // TODO: Navigate to book details
            },
            onFavoriteClick = { book ->
                toggleFavorite(book)
            }
        )

        recyclerViewMyBooks.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = myBooksAdapter
        }

        // Setup Favorites RecyclerView
        favoritesAdapter = BooksAdapter(
            onBookClick = { book ->
                // TODO: Navigate to book details
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
        // Load purchased books (My Books)
        myBooks = dbHelper.getMyBooks().toMutableList()

        // Check favorite status for each book
        myBooks.forEach { book ->
            book.isFavorite = dbHelper.isBookFavorited(book.id)
        }

        // Load favorite books
        favoriteBooks = dbHelper.getFavoriteBooks().toMutableList()
        favoriteBooks.forEach { it.isFavorite = true }

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
            favoritesAdapter.submitList(favoriteBooks.toList())
        }
    }

    private fun toggleFavorite(book: Book) {
        if (book.isFavorite) {
            // Remove from favorites
            dbHelper.removeFromFavorites(book.id)
            book.isFavorite = false
            favoriteBooks.removeAll { it.id == book.id }
        } else {
            // Add to favorites
            dbHelper.addToFavorites(book.id)
            book.isFavorite = true
            favoriteBooks.add(book)
        }

        // Update both adapters
        myBooksAdapter.notifyDataSetChanged()
        updateFavoritesUI()
    }

    override fun onResume() {
        super.onResume()
        // Reload data when returning to this fragment
        loadBooksFromDatabase()
    }
}
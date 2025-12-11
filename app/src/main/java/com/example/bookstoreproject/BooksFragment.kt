package com.example.bookstoreproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment

class BooksFragment : Fragment() {

    private lateinit var tabMyBooks: LinearLayout
    private lateinit var tabFavorite: LinearLayout
    private lateinit var myBooksContent: View
    private lateinit var favoriteContent: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_my_books, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find Tabs
        tabMyBooks = view.findViewById(R.id.tabMyBooks)
        tabFavorite = view.findViewById(R.id.tabFavorite)

        // Find content placeholders inside this fragment
        myBooksContent = view.findViewById(R.id.myBooksContent)      // e.g., LinearLayout for MyBooks
        favoriteContent = view.findViewById(R.id.favoriteContent)    // e.g., LinearLayout for Favorite

        // Default: show My Books
        setActiveTab(tabMyBooks)

        // Tab clicks
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
}

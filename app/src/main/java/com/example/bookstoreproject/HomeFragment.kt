package com.example.bookstoreproject

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
    // Books Recycler (Horizontal)
    // ---------------------------------------------------------
    private fun setupBooksRecycler(view: View) {
        val rvBooks = view.findViewById<RecyclerView>(R.id.rvBestsellers)
        rvBooks.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val sampleBooks = listOf(
            Book("Sparrow's Nest", "Edith Vincent", 5.0, 270, R.drawable.book_placeholder),
            Book("The Silent Lake", "John Hayes", 4.8, 320, R.drawable.book_placeholder),
            Book("Wild Forest", "Mila Rowan", 4.9, 190, R.drawable.book_placeholder)
        )

        rvBooks.adapter = BookAdapter(sampleBooks)
    }

    private fun setupTopRatedRecycler(view: View) {
        val rvTopRated = view.findViewById<RecyclerView>(R.id.rvTopRated)
        rvTopRated.layoutManager = LinearLayoutManager(
            requireContext(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

        val list = listOf(
            Book("Book A", "Author 1", 4.9, 250, R.drawable.book_placeholder),
            Book("Book B", "Author 2", 4.8, 310, R.drawable.book_placeholder),
            Book("Book C", "Author 3", 5.0, 150, R.drawable.book_placeholder)
        )

        rvTopRated.adapter = BookAdapter(list)
    }



    // ---------------------------------------------------------
    // Authors Recycler (Horizontal)
    // ---------------------------------------------------------
    private fun setupAuthorsRecycler(view: View) {
        rvAuthor = view.findViewById(R.id.rvAuthor)
        rvAuthor.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        authorList.apply {
            add(Author("J.K. Rowling", R.drawable.book_placeholder))
            add(Author("George R.R. Martin", R.drawable.book_placeholder))
            add(Author("Agatha Christie", R.drawable.book_placeholder))
            add(Author("Mark Twain", R.drawable.book_placeholder))
        }

        authorAdapter = AuthorAdapter(authorList)
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
}

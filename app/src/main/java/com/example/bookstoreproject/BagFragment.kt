package com.example.bookstoreproject

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.button.MaterialButton

class BagFragment : Fragment() {

    private lateinit var emptyStateLayout: LinearLayout
    private lateinit var cartContentLayout: LinearLayout
    private lateinit var rvCartItems: RecyclerView
    private lateinit var tvSubtotal: TextView
    private lateinit var tvTotal: TextView
    private lateinit var btnCheckout: MaterialButton
    private lateinit var btnDiscoverProducts: MaterialButton

    private lateinit var dbHelper: MyDatabaseHelper
    private lateinit var cartAdapter: CartAdapter
    private var currentUserId: Int = -1
    private var cartItems = mutableListOf<Pair<Book, Int>>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_bag, container, false)

        // Initialize database
        dbHelper = MyDatabaseHelper(requireContext())
        currentUserId = UserSessionManager.getCurrentUserId(requireContext())

        // Initialize views
        initializeViews(view)

        // Setup RecyclerView
        setupRecyclerView()

        // Back button
        val backButton = view.findViewById<ImageView>(R.id.backButtonBag)
        backButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.mainContainer, HomeFragment())
                .commit()

            (activity as? MainActivity)?.findViewById<BottomNavigationView>(R.id.bottomNav)?.selectedItemId = R.id.nav_home
        }

        // Discover Products button
        btnDiscoverProducts.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.mainContainer, HomeFragment())
                .commit()

            (activity as? MainActivity)?.findViewById<BottomNavigationView>(R.id.bottomNav)?.selectedItemId = R.id.nav_home
        }

        return view
    }

    override fun onResume() {
        super.onResume()

        // Refresh user ID
        currentUserId = UserSessionManager.getCurrentUserId(requireContext())

        // Load cart items
        loadCartItems()
    }

    private fun initializeViews(view: View) {
        emptyStateLayout = view.findViewById(R.id.emptyStateLayout)
        cartContentLayout = view.findViewById(R.id.cartContentLayout)
        rvCartItems = view.findViewById(R.id.rvCartItems)
        tvSubtotal = view.findViewById(R.id.tvSubtotal)
        tvTotal = view.findViewById(R.id.tvTotal)
        btnCheckout = view.findViewById(R.id.btnCheckout)
        btnDiscoverProducts = view.findViewById(R.id.btnDiscoverProducts)

        // Checkout button
        btnCheckout.setOnClickListener {
            handleCheckout()
        }
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(
            onRemove = { book ->
                removeFromCart(book)
            },
            onBookClick = { book ->
                openBookDetails(book)
            }
        )

        rvCartItems.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = cartAdapter
        }
    }

    private fun loadCartItems() {
        // Clear existing items
        cartItems.clear()

        // Load from database
        val items = dbHelper.getCartItems(currentUserId)
        cartItems.addAll(items)

        // Update UI
        if (cartItems.isEmpty()) {
            showEmptyState()
        } else {
            showCartContent()
        }
    }

    private fun showEmptyState() {
        emptyStateLayout.visibility = View.VISIBLE
        cartContentLayout.visibility = View.GONE
    }

    private fun showCartContent() {
        emptyStateLayout.visibility = View.GONE
        cartContentLayout.visibility = View.VISIBLE

        cartAdapter.submitList(cartItems.toList())
        updateTotals()
    }

    private fun updateTotals() {
        val subtotal = cartItems.size * 9.99 // $9.99 per book
        val total = subtotal

        tvSubtotal.text = String.format("$%.2f", subtotal)
        tvTotal.text = String.format("$%.2f", total)
    }

    private fun removeFromCart(book: Book) {
        dbHelper.removeFromCart(currentUserId, book.id)
        Toast.makeText(requireContext(), "Removed from bag", Toast.LENGTH_SHORT).show()
        loadCartItems()
    }

    private fun handleCheckout() {
        if (cartItems.isEmpty()) {
            Toast.makeText(requireContext(), "Your bag is empty", Toast.LENGTH_SHORT).show()
            return
        }

        // Show payment unavailable message
        Toast.makeText(
            requireContext(),
            "Payment system is not available yet. Books added to your library for demo!",
            Toast.LENGTH_LONG
        ).show()

        // Add all cart items to My Books
        cartItems.forEach { (book, _) ->
            dbHelper.addToMyBooks(book.id, currentUserId)
        }

        // Clear cart
        dbHelper.clearCart(currentUserId)

        Toast.makeText(requireContext(), "Books added to your library!", Toast.LENGTH_SHORT).show()

        // Reload
        loadCartItems()
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
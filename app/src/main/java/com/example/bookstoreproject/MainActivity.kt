package com.example.bookstoreproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView
    private var currentFragmentTag: String = "HOME"

    companion object {
        private const val TAG = "MainActivity"
        private const val PREF_DATABASE_SEEDED = "database_seeded"
        private const val STATE_CURRENT_FRAGMENT = "current_fragment"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check authentication first
        if (!checkUserAuthentication()) {
            return
        }

        setContentView(R.layout.activity_main)

        // Initialize views
        initializeViews()

        // Initialize database
        initializeDatabase()

        // Restore fragment state or load default
        if (savedInstanceState != null) {
            currentFragmentTag = savedInstanceState.getString(STATE_CURRENT_FRAGMENT, "HOME")
            restoreFragment()
        } else {
            loadHomeFragment()
        }

        // Setup bottom navigation
        setupBottomNavigation()
    }

    /**
     * Check if user is authenticated
     * Redirect to login if not
     */
    private fun checkUserAuthentication(): Boolean {
        if (!UserSessionManager.isLoggedIn(this)) {
            Log.d(TAG, "User not authenticated, redirecting to login")
            navigateToLogin()
            return false
        }
        return true
    }

    /**
     * Initialize all views
     */
    private fun initializeViews() {
        bottomNav = findViewById(R.id.bottomNav)
    }

    /**
     * Initialize database - seed if needed
     */
    private fun initializeDatabase() {
        val prefs = getSharedPreferences("BookStorePrefs", MODE_PRIVATE)
        val isSeeded = prefs.getBoolean(PREF_DATABASE_SEEDED, false)

        if (!isSeeded) {
            Log.d(TAG, "Seeding database for first time")
            val seeder = DatabaseSeeder(this)
            seeder.seedDatabase()

            // Mark as seeded
            prefs.edit().putBoolean(PREF_DATABASE_SEEDED, true).apply()
            Log.d(TAG, "Database seeded successfully")
        } else {
            Log.d(TAG, "Database already seeded")
        }
    }

    /**
     * Setup bottom navigation listener
     */
    private fun setupBottomNavigation() {
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    loadFragment(HomeFragment(), "HOME")
                    true
                }
                R.id.nav_library -> {
                    loadFragment(BooksFragment(), "LIBRARY")
                    true
                }
                R.id.nav_bag -> {
                    loadFragment(BagFragment(), "BAG")
                    true
                }
                R.id.nav_profile -> {
                    loadFragment(ProfileFragment(), "PROFILE")
                    true
                }
                else -> false
            }
        }
    }

    /**
     * Load home fragment as default
     */
    private fun loadHomeFragment() {
        loadFragment(HomeFragment(), "HOME")
        bottomNav.selectedItemId = R.id.nav_home
    }

    /**
     * Restore fragment after rotation or state change
     */
    private fun restoreFragment() {
        when (currentFragmentTag) {
            "HOME" -> bottomNav.selectedItemId = R.id.nav_home
            "LIBRARY" -> bottomNav.selectedItemId = R.id.nav_library
            "BAG" -> bottomNav.selectedItemId = R.id.nav_bag
            "PROFILE" -> bottomNav.selectedItemId = R.id.nav_profile
        }
    }

    /**
     * Load fragment with proper transaction
     */
    private fun loadFragment(fragment: Fragment, tag: String) {
        currentFragmentTag = tag

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.mainContainer, fragment, tag)
            commit()
        }
    }

    /**
     * Navigate to login screen
     */
    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    /**
     * Handle user logout
     */
    fun handleLogout() {
        Log.d(TAG, "User logging out")
        UserSessionManager.logout(this)
        navigateToLogin()
    }

    /**
     * Save fragment state on rotation
     */
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(STATE_CURRENT_FRAGMENT, currentFragmentTag)
    }

    /**
     * Check authentication on resume
     */
    override fun onResume() {
        super.onResume()

        // Verify user is still logged in
        if (!UserSessionManager.isLoggedIn(this)) {
            Log.d(TAG, "Session expired, redirecting to login")
            navigateToLogin()
        }
    }

    /**
     * Handle back button press
     */
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.mainContainer)

        when {
            // If on home, exit app
            currentFragment is HomeFragment -> {
                super.onBackPressed()
            }
            // Otherwise, go back to home
            else -> {
                loadHomeFragment()
            }
        }
    }

    /**
     * Clean up resources
     */
    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "MainActivity destroyed")
    }
}
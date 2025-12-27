package com.example.bookstoreproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var bottomNav: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Check if user is logged in
        if (!UserSessionManager.isLoggedIn(this)) {
            navigateToLogin()
            return
        }

        setContentView(R.layout.activity_main)

        bottomNav = findViewById(R.id.bottomNav)

        // Seed database ONLY ONCE (check if already seeded)
        val prefs = getSharedPreferences("BookStorePrefs", MODE_PRIVATE)
        val isSeeded = prefs.getBoolean("database_seeded", false)

        if (!isSeeded) {
            val seeder = DatabaseSeeder(this)
            seeder.seedDatabase()

            // Mark as seeded
            prefs.edit().putBoolean("database_seeded", true).apply()
        }

        // Load default screen
        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
            bottomNav.selectedItemId = R.id.nav_home
        }

        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> replaceFragment(HomeFragment())
                R.id.nav_profile -> replaceFragment(ProfileFragment())
                R.id.nav_library -> replaceFragment(BooksFragment())
                R.id.nav_bag -> replaceFragment(BagFragment())
            }
            true
        }
    }

    override fun onResume() {
        super.onResume()

        // Check if user is still logged in
        if (!UserSessionManager.isLoggedIn(this)) {
            navigateToLogin()
        }
    }

    private fun replaceFragment(fragment: androidx.fragment.app.Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.mainContainer, fragment)
            .commit()
    }

    private fun navigateToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    // Call this method when user logs out
    fun handleLogout() {
        UserSessionManager.logout(this)
        navigateToLogin()
    }

    // Handle back button - go to home fragment instead of closing app
    override fun onBackPressed() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.mainContainer)

        if (currentFragment is HomeFragment) {
            // If already on home, exit app
            super.onBackPressed()
        } else {
            // Go back to home fragment
            replaceFragment(HomeFragment())
            bottomNav.selectedItemId = R.id.nav_home
        }
    }
}
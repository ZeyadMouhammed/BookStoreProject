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
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Back button
        view.findViewById<ImageView>(R.id.backButtonBooks)?.setOnClickListener {
            // Navigate to home fragment
            parentFragmentManager.beginTransaction()
                .replace(R.id.mainContainer, HomeFragment())
                .commit()

            // Update bottom navigation
            (activity as? MainActivity)?.findViewById<BottomNavigationView>(R.id.bottomNav)?.selectedItemId = R.id.nav_home
        }

        // 🔹 Welcome Username
        val tvWelcome = view.findViewById<TextView>(R.id.tvWelcomeUsername)
        val username = UserSessionManager.getCurrentUserName(requireContext())
        tvWelcome.text = "Welcome $username!"

        // Logout
        view.findViewById<LinearLayout>(R.id.itemLogOut).setOnClickListener {
            (activity as? MainActivity)?.handleLogout()
        }

        return view
    }
}

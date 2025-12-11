package com.example.bookstoreproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment

class ProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the fragment layout
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        // Top bar back button
        val backButton = view.findViewById<ImageView>(R.id.backButton)
        backButton.setOnClickListener {
            activity?.onBackPressed()
        }

        // Menu item clicks
        val itemMyGoal = view.findViewById<LinearLayout>(R.id.itemMyGoal)
        itemMyGoal.setOnClickListener {
            Toast.makeText(requireContext(), "My Goal clicked", Toast.LENGTH_SHORT).show()
            // Add navigation logic here
        }

        val itemMyNotes = view.findViewById<LinearLayout>(R.id.itemMyNotes)
        itemMyNotes.setOnClickListener {
            Toast.makeText(requireContext(), "My Notes clicked", Toast.LENGTH_SHORT).show()
        }

        val itemMyOrders = view.findViewById<LinearLayout>(R.id.itemMyOrders)
        itemMyOrders.setOnClickListener {
            Toast.makeText(requireContext(), "My Orders clicked", Toast.LENGTH_SHORT).show()
        }

        val itemPaymentCards = view.findViewById<LinearLayout>(R.id.itemPaymentCards)
        itemPaymentCards.setOnClickListener {
            Toast.makeText(requireContext(), "Payment Cards clicked", Toast.LENGTH_SHORT).show()
        }

        val itemMyPoints = view.findViewById<LinearLayout>(R.id.itemMyPoints)
        itemMyPoints.setOnClickListener {
            Toast.makeText(requireContext(), "My Points clicked", Toast.LENGTH_SHORT).show()
        }

        val itemSubscription = view.findViewById<LinearLayout>(R.id.itemSubscription)
        itemSubscription.setOnClickListener {
            Toast.makeText(requireContext(), "Subscription Plans clicked", Toast.LENGTH_SHORT).show()
        }

        val itemSettings = view.findViewById<LinearLayout>(R.id.itemSettings)
        itemSettings.setOnClickListener {
            Toast.makeText(requireContext(), "Settings clicked", Toast.LENGTH_SHORT).show()
        }

        val itemLogOut = view.findViewById<LinearLayout>(R.id.itemLogOut)
        itemLogOut.setOnClickListener {
            Toast.makeText(requireContext(), "Log Out clicked", Toast.LENGTH_SHORT).show()
            // Add logout logic here
        }

        return view
    }
}

package com.example.bookstoreproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.android.material.button.MaterialButton

class BagFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_bag, container, false)

        // Back button click
        val backButton = view.findViewById<ImageView>(R.id.backButtonBag)
        backButton.setOnClickListener {
            // Handle back press, e.g., pop the fragment
            activity?.onBackPressed()
        }

        // Discover Products button click
        val btnDiscover = view.findViewById<MaterialButton>(R.id.btnDiscoverProducts)
        btnDiscover.setOnClickListener {
            Toast.makeText(requireContext(), "Discover Products clicked", Toast.LENGTH_SHORT).show()
            // You can navigate to your products fragment/activity here
        }

        return view
    }
}

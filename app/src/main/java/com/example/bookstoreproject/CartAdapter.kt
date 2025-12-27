package com.example.bookstoreproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class CartAdapter(
    private val onRemove: (Book) -> Unit,
    private val onBookClick: (Book) -> Unit
) : ListAdapter<Pair<Book, Int>, CartAdapter.CartViewHolder>(CartDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val (book, quantity) = getItem(position)
        holder.bind(book, quantity)
    }

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val bookCover: ImageView = itemView.findViewById(R.id.cartBookCover)
        private val bookTitle: TextView = itemView.findViewById(R.id.cartBookTitle)
        private val bookAuthor: TextView = itemView.findViewById(R.id.cartBookAuthor)
        private val bookPrice: TextView = itemView.findViewById(R.id.cartBookPrice)
        private val btnRemove: ImageView = itemView.findViewById(R.id.btnRemove)

        fun bind(book: Book, quantity: Int) {
            bookCover.setImageResource(book.imageRes)
            bookTitle.text = book.title
            bookAuthor.text = book.author
            bookPrice.text = "$9.99"

            itemView.setOnClickListener {
                onBookClick(book)
            }

            btnRemove.setOnClickListener {
                onRemove(book)
            }
        }
    }

    private class CartDiffCallback : DiffUtil.ItemCallback<Pair<Book, Int>>() {
        override fun areItemsTheSame(oldItem: Pair<Book, Int>, newItem: Pair<Book, Int>): Boolean {
            return oldItem.first.id == newItem.first.id
        }

        override fun areContentsTheSame(oldItem: Pair<Book, Int>, newItem: Pair<Book, Int>): Boolean {
            return oldItem == newItem
        }
    }
}
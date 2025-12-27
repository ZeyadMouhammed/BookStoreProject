package com.example.bookstoreproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class BooksAdapter(
    private val onBookClick: (Book) -> Unit,
    private val onFavoriteClick: (Book) -> Unit
) : ListAdapter<Book, BooksAdapter.BookViewHolder>(BookDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view, onBookClick, onFavoriteClick)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class BookViewHolder(
        itemView: View,
        private val onBookClick: (Book) -> Unit,
        private val onFavoriteClick: (Book) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {

        private val ivBookCover: ImageView = itemView.findViewById(R.id.ivBookCover)
        private val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)
        private val tvBookTitle: TextView = itemView.findViewById(R.id.tvBookTitle)
        private val tvAuthor: TextView = itemView.findViewById(R.id.tvAuthor)
        private val tvRating: TextView = itemView.findViewById(R.id.tvRating)
        private val tvPages: TextView = itemView.findViewById(R.id.tvPages)
        private val ivFavorite: ImageView = itemView.findViewById(R.id.ivFavorite)

        fun bind(book: Book) {
            tvBookTitle.text = book.title
            tvAuthor.text = book.author
            tvRating.text = "⭐ ${book.rating}"
            tvPages.text = "${book.pages} Pages"
            tvPrice.text = "Free"

            // Load book cover image
            ivBookCover.setImageResource(book.imageRes)

            // Update favorite icon - FIXED: correct logic
            if (book.isFavorite) {
                // When favorited, show filled heart
                ivFavorite.setImageResource(R.drawable.ic_favorite_filled)
                ivFavorite.alpha = 1.0f
            } else {
                // When not favorited, show border heart
                ivFavorite.setImageResource(R.drawable.ic_favorite_border)
                ivFavorite.alpha = 0.6f
            }

            // Click listeners - Make entire card clickable
            itemView.setOnClickListener {
                onBookClick(book)
            }

            // Favorite button click
            ivFavorite.setOnClickListener {
                onFavoriteClick(book)
            }
        }
    }

    private class BookDiffCallback : DiffUtil.ItemCallback<Book>() {
        override fun areItemsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Book, newItem: Book): Boolean {
            return oldItem == newItem
        }
    }
}
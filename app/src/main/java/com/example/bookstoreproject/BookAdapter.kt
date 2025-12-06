package com.example.bookstoreproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class BookAdapter(private val books: List<Book>) :
    RecyclerView.Adapter<BookAdapter.BookViewHolder>() {

    class BookViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cover: ImageView = itemView.findViewById(R.id.ivBookCover)
        val title: TextView = itemView.findViewById(R.id.tvBookTitle)
        val author: TextView = itemView.findViewById(R.id.tvAuthor)
        val rating: TextView = itemView.findViewById(R.id.tvRating)
        val pages: TextView = itemView.findViewById(R.id.tvPages)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_book, parent, false)
        return BookViewHolder(view)
    }

    override fun onBindViewHolder(holder: BookViewHolder, position: Int) {
        val book = books[position]

        holder.cover.setImageResource(book.imageRes)
        holder.title.text = book.title
        holder.author.text = book.author
        holder.rating.text = "⭐ ${book.rating}"
        holder.pages.text = "${book.pages} pages"
    }

    override fun getItemCount() = books.size
}

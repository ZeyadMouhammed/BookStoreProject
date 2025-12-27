package com.example.bookstoreproject

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AuthorAdapter(
    private val authorList: List<Author>,
    private val onAuthorClick: ((Author) -> Unit)? = null
) : RecyclerView.Adapter<AuthorAdapter.AuthorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuthorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_author, parent, false)
        return AuthorViewHolder(view)
    }

    override fun onBindViewHolder(holder: AuthorViewHolder, position: Int) {
        val author = authorList[position]
        holder.bind(author)
    }

    override fun getItemCount(): Int = authorList.size

    inner class AuthorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Changed to match your layout IDs
        private val authorImage: ImageView = itemView.findViewById(R.id.imgAuthor)
        private val authorName: TextView = itemView.findViewById(R.id.tvAuthorName)

        fun bind(author: Author) {
            authorName.text = author.name
            authorImage.setImageResource(author.imageResId)

            // Set click listener on the entire item
            itemView.setOnClickListener {
                // If lambda is provided, use it
                onAuthorClick?.invoke(author)

                // Otherwise, open AuthorDetailsActivity (backward compatibility)
                if (onAuthorClick == null) {
                    val intent = Intent(itemView.context, AuthorDetailsActivity::class.java)
                    intent.putExtra("name", author.name)
                    intent.putExtra("imageRes", author.imageResId ?: R.drawable.book_placeholder)
                    itemView.context.startActivity(intent)
                }
            }
        }
    }
}
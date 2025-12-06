package com.example.bookstoreproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AuthorAdapter(private val authors: List<Author>) :
    RecyclerView.Adapter<AuthorAdapter.AuthorViewHolder>() {

    inner class AuthorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgAuthor: ImageView = itemView.findViewById(R.id.imgAuthor)
        val tvAuthorName: TextView = itemView.findViewById(R.id.tvAuthorName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AuthorViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_author, parent, false)
        return AuthorViewHolder(view)
    }

    override fun onBindViewHolder(holder: AuthorViewHolder, position: Int) {
        val author = authors[position]
        holder.tvAuthorName.text = author.name
        holder.imgAuthor.setImageResource(author.imageResId)
    }

    override fun getItemCount(): Int = authors.size
}

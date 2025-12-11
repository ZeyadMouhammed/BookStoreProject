package com.example.bookstoreproject

enum class BookCategory(val displayName: String, val iconRes: Int) {
    ALL("All", R.drawable.ic_cart),
    FICTION("Fiction", R.drawable.ic_bag),
    SCIENCE("Science", R.drawable.ic_category),
    HISTORY("History", R.drawable.ic_filter);

    companion object {
        fun fromString(value: String): BookCategory? {
            return BookCategory.entries.find { it.displayName.equals(value, ignoreCase = true) }
        }
    }
}
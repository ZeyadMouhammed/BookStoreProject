package com.example.bookstoreproject

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "BookStore.db"
        private const val DATABASE_VERSION = 3  // ← Changed from 2 to 3

        // Tables
        private const val TABLE_BOOKS = "Books"
        private const val TABLE_AUTHORS = "Authors"
        private const val TABLE_CART = "Cart"
        private const val TABLE_FAVORITES = "Favorites"
        private const val TABLE_MY_BOOKS = "MyBooks"  // ← New table

        // Book columns
        private const val COLUMN_BOOK_ID = "id"
        private const val COLUMN_BOOK_TITLE = "title"
        private const val COLUMN_BOOK_AUTHOR = "author"
        private const val COLUMN_BOOK_RATING = "rating"
        private const val COLUMN_BOOK_PAGES = "pages"
        private const val COLUMN_BOOK_IMAGE_RES = "imageRes"

        // Author columns
        private const val COLUMN_AUTHOR_ID = "id"
        private const val COLUMN_AUTHOR_NAME = "name"
        private const val COLUMN_AUTHOR_IMAGE_RES = "imageResId"

        // Cart columns
        private const val COLUMN_CART_ID = "cartId"
        private const val COLUMN_CART_BOOK_ID = "bookId"
        private const val COLUMN_CART_QUANTITY = "quantity"

        // Favorites columns
        private const val COLUMN_FAV_ID = "favoriteId"
        private const val COLUMN_FAV_BOOK_ID = "bookId"

        // MyBooks columns - New
        private const val COLUMN_MY_BOOK_ID = "myBookId"
        private const val COLUMN_MY_BOOK_BOOK_ID = "bookId"
        private const val COLUMN_MY_BOOK_PURCHASE_DATE = "purchaseDate"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createBooksTable = """
            CREATE TABLE $TABLE_BOOKS (
                $COLUMN_BOOK_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_BOOK_TITLE TEXT NOT NULL,
                $COLUMN_BOOK_AUTHOR TEXT NOT NULL,
                $COLUMN_BOOK_RATING REAL DEFAULT 0.0,
                $COLUMN_BOOK_PAGES INTEGER DEFAULT 0,
                $COLUMN_BOOK_IMAGE_RES INTEGER NOT NULL
            )
        """.trimIndent()

        val createAuthorsTable = """
            CREATE TABLE $TABLE_AUTHORS (
                $COLUMN_AUTHOR_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_AUTHOR_NAME TEXT,
                $COLUMN_AUTHOR_IMAGE_RES INTEGER NOT NULL
            )
        """.trimIndent()

        val createCartTable = """
            CREATE TABLE $TABLE_CART (
                $COLUMN_CART_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_CART_BOOK_ID INTEGER NOT NULL,
                $COLUMN_CART_QUANTITY INTEGER NOT NULL DEFAULT 1,
                FOREIGN KEY($COLUMN_CART_BOOK_ID) REFERENCES $TABLE_BOOKS($COLUMN_BOOK_ID) ON DELETE CASCADE
            )
        """.trimIndent()

        val createFavoritesTable = """
            CREATE TABLE $TABLE_FAVORITES (
                $COLUMN_FAV_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_FAV_BOOK_ID INTEGER NOT NULL UNIQUE,
                FOREIGN KEY($COLUMN_FAV_BOOK_ID) REFERENCES $TABLE_BOOKS($COLUMN_BOOK_ID) ON DELETE CASCADE
            )
        """.trimIndent()

        // New MyBooks table
        val createMyBooksTable = """
            CREATE TABLE $TABLE_MY_BOOKS (
                $COLUMN_MY_BOOK_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_MY_BOOK_BOOK_ID INTEGER NOT NULL UNIQUE,
                $COLUMN_MY_BOOK_PURCHASE_DATE TEXT,
                FOREIGN KEY($COLUMN_MY_BOOK_BOOK_ID) REFERENCES $TABLE_BOOKS($COLUMN_BOOK_ID) ON DELETE CASCADE
            )
        """.trimIndent()

        db?.execSQL(createBooksTable)
        db?.execSQL(createAuthorsTable)
        db?.execSQL(createCartTable)
        db?.execSQL(createFavoritesTable)
        db?.execSQL(createMyBooksTable)  // ← Create new table
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_MY_BOOKS")  // ← Add this
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_FAVORITES")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_CART")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_AUTHORS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_BOOKS")
        onCreate(db)
    }

    // ==================== BOOK OPERATIONS ====================

    fun insertBook(book: Book): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_BOOK_TITLE, book.title)
            put(COLUMN_BOOK_AUTHOR, book.author)
            put(COLUMN_BOOK_RATING, book.rating)
            put(COLUMN_BOOK_PAGES, book.pages)
            put(COLUMN_BOOK_IMAGE_RES, book.imageRes)
        }
        return db.insert(TABLE_BOOKS, null, values)
    }

    fun getAllBooks(): List<Book> {
        val books = mutableListOf<Book>()
        val db = readableDatabase
        val cursor = db.query(TABLE_BOOKS, null, null, null, null, null, null)

        if (cursor.moveToFirst()) {
            do {
                books.add(cursorToBook(cursor))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return books
    }

    fun getBookById(bookId: Int): Book? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_BOOKS,
            null,
            "$COLUMN_BOOK_ID = ?",
            arrayOf(bookId.toString()),
            null, null, null
        )

        var book: Book? = null
        if (cursor.moveToFirst()) {
            book = cursorToBook(cursor)
        }
        cursor.close()
        return book
    }

    fun updateBook(bookId: Int, book: Book): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_BOOK_TITLE, book.title)
            put(COLUMN_BOOK_AUTHOR, book.author)
            put(COLUMN_BOOK_RATING, book.rating)
            put(COLUMN_BOOK_PAGES, book.pages)
            put(COLUMN_BOOK_IMAGE_RES, book.imageRes)
        }
        return db.update(TABLE_BOOKS, values, "$COLUMN_BOOK_ID = ?", arrayOf(bookId.toString()))
    }

    fun deleteBook(bookId: Int): Int {
        val db = writableDatabase
        return db.delete(TABLE_BOOKS, "$COLUMN_BOOK_ID = ?", arrayOf(bookId.toString()))
    }

    fun searchBooks(query: String): List<Book> {
        val books = mutableListOf<Book>()
        val db = readableDatabase
        val cursor = db.query(
            TABLE_BOOKS,
            null,
            "$COLUMN_BOOK_TITLE LIKE ? OR $COLUMN_BOOK_AUTHOR LIKE ?",
            arrayOf("%$query%", "%$query%"),
            null, null, null
        )

        if (cursor.moveToFirst()) {
            do {
                books.add(cursorToBook(cursor))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return books
    }

    // ==================== AUTHOR OPERATIONS ====================

    fun insertAuthor(author: Author): Long {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_AUTHOR_NAME, author.name)
            put(COLUMN_AUTHOR_IMAGE_RES, author.imageResId)
        }
        return db.insert(TABLE_AUTHORS, null, values)
    }

    fun getAllAuthors(): List<Author> {
        val authors = mutableListOf<Author>()
        val db = readableDatabase
        val cursor = db.query(TABLE_AUTHORS, null, null, null, null, null, null)

        if (cursor.moveToFirst()) {
            do {
                val author = Author(
                    cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AUTHOR_NAME)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AUTHOR_IMAGE_RES))
                )
                authors.add(author)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return authors
    }

    fun getAuthorById(authorId: Int): Author? {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_AUTHORS,
            null,
            "$COLUMN_AUTHOR_ID = ?",
            arrayOf(authorId.toString()),
            null, null, null
        )

        var author: Author? = null
        if (cursor.moveToFirst()) {
            author = Author(
                cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_AUTHOR_NAME)),
                cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_AUTHOR_IMAGE_RES))
            )
        }
        cursor.close()
        return author
    }

    fun deleteAuthor(authorId: Int): Int {
        val db = writableDatabase
        return db.delete(TABLE_AUTHORS, "$COLUMN_AUTHOR_ID = ?", arrayOf(authorId.toString()))
    }

    // ==================== CART OPERATIONS ====================

    fun addToCart(bookId: Int, quantity: Int = 1): Long {
        val db = writableDatabase

        // Check if book already exists in cart
        val cursor = db.query(
            TABLE_CART,
            null,
            "$COLUMN_CART_BOOK_ID = ?",
            arrayOf(bookId.toString()),
            null, null, null
        )

        val result = if (cursor.moveToFirst()) {
            // Update quantity
            val currentQty = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_QUANTITY))
            val values = ContentValues().apply {
                put(COLUMN_CART_QUANTITY, currentQty + quantity)
            }
            db.update(
                TABLE_CART,
                values,
                "$COLUMN_CART_BOOK_ID = ?",
                arrayOf(bookId.toString())
            ).toLong()
        } else {
            // Insert new
            val values = ContentValues().apply {
                put(COLUMN_CART_BOOK_ID, bookId)
                put(COLUMN_CART_QUANTITY, quantity)
            }
            db.insert(TABLE_CART, null, values)
        }
        cursor.close()
        return result
    }

    fun getCartItems(): List<Pair<Book, Int>> {
        val items = mutableListOf<Pair<Book, Int>>()
        val db = readableDatabase

        val query = """
            SELECT b.*, c.$COLUMN_CART_QUANTITY
            FROM $TABLE_BOOKS b
            INNER JOIN $TABLE_CART c ON b.$COLUMN_BOOK_ID = c.$COLUMN_CART_BOOK_ID
        """

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                val book = cursorToBook(cursor)
                val quantity = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_CART_QUANTITY))
                items.add(Pair(book, quantity))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return items
    }

    fun updateCartQuantity(bookId: Int, quantity: Int): Int {
        val db = writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_CART_QUANTITY, quantity)
        }
        return db.update(TABLE_CART, values, "$COLUMN_CART_BOOK_ID = ?", arrayOf(bookId.toString()))
    }

    fun removeFromCart(bookId: Int): Int {
        val db = writableDatabase
        return db.delete(TABLE_CART, "$COLUMN_CART_BOOK_ID = ?", arrayOf(bookId.toString()))
    }

    fun clearCart(): Int {
        val db = writableDatabase
        return db.delete(TABLE_CART, null, null)
    }

    // ==================== FAVORITE OPERATIONS ====================

    fun addToFavorites(bookId: Int): Long {
        val db = writableDatabase

        // Check if already favorited
        val cursor = db.query(
            TABLE_FAVORITES,
            null,
            "$COLUMN_FAV_BOOK_ID = ?",
            arrayOf(bookId.toString()),
            null, null, null
        )

        val result = if (cursor.count > 0) {
            -1L // Already exists
        } else {
            val values = ContentValues().apply {
                put(COLUMN_FAV_BOOK_ID, bookId)
            }
            db.insert(TABLE_FAVORITES, null, values)
        }
        cursor.close()
        return result
    }

    fun removeFromFavorites(bookId: Int): Int {
        val db = writableDatabase
        return db.delete(TABLE_FAVORITES, "$COLUMN_FAV_BOOK_ID = ?", arrayOf(bookId.toString()))
    }

    fun getFavoriteBooks(): List<Book> {
        val books = mutableListOf<Book>()
        val db = readableDatabase

        val query = """
            SELECT b.*
            FROM $TABLE_BOOKS b
            INNER JOIN $TABLE_FAVORITES f ON b.$COLUMN_BOOK_ID = f.$COLUMN_FAV_BOOK_ID
        """

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                books.add(cursorToBook(cursor))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return books
    }

    fun isBookFavorited(bookId: Int): Boolean {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_FAVORITES,
            null,
            "$COLUMN_FAV_BOOK_ID = ?",
            arrayOf(bookId.toString()),
            null, null, null
        )
        val isFavorited = cursor.count > 0
        cursor.close()
        return isFavorited
    }

    // ==================== MY BOOKS OPERATIONS (NEW) ====================

    fun addToMyBooks(bookId: Int): Long {
        val db = writableDatabase

        // Check if already in my books
        val cursor = db.query(
            TABLE_MY_BOOKS,
            null,
            "$COLUMN_MY_BOOK_BOOK_ID = ?",
            arrayOf(bookId.toString()),
            null, null, null
        )

        val result = if (cursor.count > 0) {
            -1L // Already exists
        } else {
            val values = ContentValues().apply {
                put(COLUMN_MY_BOOK_BOOK_ID, bookId)
                put(COLUMN_MY_BOOK_PURCHASE_DATE, System.currentTimeMillis().toString())
            }
            db.insert(TABLE_MY_BOOKS, null, values)
        }
        cursor.close()
        return result
    }

    fun removeFromMyBooks(bookId: Int): Int {
        val db = writableDatabase
        return db.delete(TABLE_MY_BOOKS, "$COLUMN_MY_BOOK_BOOK_ID = ?", arrayOf(bookId.toString()))
    }

    fun getMyBooks(): List<Book> {
        val books = mutableListOf<Book>()
        val db = readableDatabase

        val query = """
            SELECT b.*
            FROM $TABLE_BOOKS b
            INNER JOIN $TABLE_MY_BOOKS m ON b.$COLUMN_BOOK_ID = m.$COLUMN_MY_BOOK_BOOK_ID
            ORDER BY m.$COLUMN_MY_BOOK_PURCHASE_DATE DESC
        """

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            do {
                books.add(cursorToBook(cursor))
            } while (cursor.moveToNext())
        }
        cursor.close()
        return books
    }

    fun isBookInMyBooks(bookId: Int): Boolean {
        val db = readableDatabase
        val cursor = db.query(
            TABLE_MY_BOOKS,
            null,
            "$COLUMN_MY_BOOK_BOOK_ID = ?",
            arrayOf(bookId.toString()),
            null, null, null
        )
        val isInMyBooks = cursor.count > 0
        cursor.close()
        return isInMyBooks
    }

    // ==================== HELPER METHODS ====================

    private fun cursorToBook(cursor: Cursor): Book {
        return Book(
            title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_TITLE)),
            author = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_BOOK_AUTHOR)),
            rating = cursor.getDouble(cursor.getColumnIndexOrThrow(COLUMN_BOOK_RATING)),
            pages = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOOK_PAGES)),
            imageRes = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOOK_IMAGE_RES)),
            id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_BOOK_ID))
        )
    }
}
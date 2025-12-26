package com.example.bookstoreproject

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class MyDatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "BookStore.db"
        private const val DATABASE_VERSION = 1

        // Tables
        private const val TABLE_BOOKS = "Books"
        private const val TABLE_AUTHORS = "Authors"
        private const val TABLE_BOOK_AUTHORS = "Book_Authors"
        private const val TABLE_CATEGORY = "Category"
        private const val TABLE_BOOK_CATEGORY = "BookCategory"
        private const val TABLE_ORDER = "Orders"
        private const val TABLE_ORDER_ITEM = "OrderItem"
        private const val TABLE_CART = "Cart"
        private const val TABLE_FAVORITE = "Favorite"

        // Columns for Books
        private const val COLUMN_BOOK_ID = "id"
        private const val COLUMN_BOOK_TITLE = "title"
        private const val COLUMN_BOOK_ISBN = "isbn"
        private const val COLUMN_BOOK_PRICE = "price"
        private const val COLUMN_BOOK_DESCRIPTION = "description"
        private const val COLUMN_BOOK_STOCK_QUANTITY = "stockQuantity"
        private const val COLUMN_BOOK_COVER_IMAGE_PATH = "coverImagePath"

        // Columns for Authors
        private const val COLUMN_AUTHOR_ID = "id"
        private const val COLUMN_AUTHOR_NAME = "name"
        private const val COLUMN_AUTHOR_BIO = "bio"

        // Columns for Book_Authors
        private const val COLUMN_BA_BOOK_ID = "book_id"
        private const val COLUMN_BA_AUTHOR_ID = "author_id"

        // Columns for Category
        private const val COLUMN_CATEGORY_ID = "categoryId"
        private const val COLUMN_CATEGORY_NAME = "name"

        // Columns for Category_Book
        private const val COLUMN_BC_BOOK_ID = "bookId"
        private const val COLUMN_BC_CATEGORY_ID = "categoryId"

        // Columns for Order
        private const val COLUMN_ORDER_ID = "orderId"
        private const val COLUMN_ORDER_DATE = "date"
        private const val COLUMN_ORDER_TOTAL_PRICE = "totalPrice"

        // Columns for Order item
        private const val COLUMN_ORDER_ITEM_ID = "orderItemId"
        private const val COLUMN_OI_ORDER_ID = "orderId"
        private const val COLUMN_OI_BOOK_ID = "bookId"
        private const val COLUMN_OI_QUANTITY = "quantity"
        private const val COLUMN_OI_PRICE_AT_PURCHASE = "priceAtPurchase"

        // Col
        private const val COLUMN_CART_ID = "cartId"
        private const val COLUMN_CART_BOOK_ID = "bookId"
        private const val COLUMN_CART_QUANTITY = "quantity"

        // Col
        private const val COLUMN_FAV_ID = "favoriteId"
        private const val COLUMN_FAV_BOOK_ID = "bookId"

    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createBooksTable = """
            CREATE TABLE $TABLE_BOOKS (
                $COLUMN_BOOK_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_BOOK_TITLE TEXT NOT NULL,
                $COLUMN_BOOK_ISBN TEXT,
                $COLUMN_BOOK_PRICE REAL,
                $COLUMN_BOOK_DESCRIPTION TEXT,
                $COLUMN_BOOK_STOCK_QUANTITY INTEGER NOT NULL,
                $COLUMN_BOOK_COVER_IMAGE_PATH TEXT
            )
        """.trimIndent()

        val createAuthorsTable = """
            CREATE TABLE $TABLE_AUTHORS (
                $COLUMN_AUTHOR_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_AUTHOR_NAME TEXT NOT NULL,
                $COLUMN_AUTHOR_BIO TEXT
            )
        """.trimIndent()

        val createBookAuthorsTable = """
            CREATE TABLE $TABLE_BOOK_AUTHORS (
                $COLUMN_BA_BOOK_ID INTEGER NOT NULL,
                $COLUMN_BA_AUTHOR_ID INTEGER NOT NULL,
                FOREIGN KEY($COLUMN_BA_BOOK_ID) REFERENCES $TABLE_BOOKS($COLUMN_BOOK_ID),
                FOREIGN KEY($COLUMN_BA_AUTHOR_ID) REFERENCES $TABLE_AUTHORS($COLUMN_AUTHOR_ID),
                PRIMARY KEY($COLUMN_BA_BOOK_ID, $COLUMN_BA_AUTHOR_ID)
            )
        """.trimIndent()

        val createCategoryTable = """
            CREATE TABLE $TABLE_CATEGORY (
                $COLUMN_CATEGORY_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_CATEGORY_NAME TEXT NOT NULL
            )
        """.trimIndent()

        val createBookCategoryTable = """
            CREATE TABLE $TABLE_BOOK_CATEGORY (
                $COLUMN_BC_BOOK_ID INTEGER NOT NULL,
                $COLUMN_BC_CATEGORY_ID INTEGER NOT NULL,
                FOREIGN KEY($COLUMN_BC_BOOK_ID) REFERENCES $TABLE_BOOKS($COLUMN_BOOK_ID),
                FOREIGN KEY($COLUMN_BC_CATEGORY_ID) REFERENCES $TABLE_CATEGORY($COLUMN_CATEGORY_ID),
                PRIMARY KEY($COLUMN_BC_BOOK_ID, $COLUMN_BC_CATEGORY_ID)
            )
        """.trimIndent()

        val createOrderTable = """
            CREATE TABLE $TABLE_ORDER (
                $COLUMN_ORDER_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_ORDER_DATE TEXT NOT NULL,
                $COLUMN_ORDER_TOTAL_PRICE REAL NOT NULL
            )
        """.trimIndent()

        val createOrderItemTable = """
            CREATE TABLE $TABLE_ORDER_ITEM (
                $COLUMN_ORDER_ITEM_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_OI_ORDER_ID INTEGER NOT NULL,
                $COLUMN_OI_BOOK_ID INTEGER NOT NULL,
                $COLUMN_OI_QUANTITY INTEGER NOT NULL,
                $COLUMN_OI_PRICE_AT_PURCHASE REAL NOT NULL,
                FOREIGN KEY($COLUMN_OI_ORDER_ID) REFERENCES $TABLE_ORDER($COLUMN_ORDER_ID),
                FOREIGN KEY($COLUMN_OI_BOOK_ID) REFERENCES $TABLE_BOOKS($COLUMN_BOOK_ID)
            )
        """.trimIndent()

        val createCartTable = """
            CREATE TABLE $TABLE_CART (
                $COLUMN_CART_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_CART_BOOK_ID INTEGER NOT NULL,
                $COLUMN_CART_QUANTITY INTEGER NOT NULL,
                FOREIGN KEY($COLUMN_CART_BOOK_ID) REFERENCES $TABLE_BOOKS($COLUMN_BOOK_ID)
            )
        """.trimIndent()

        val createFavoriteTable = """
            CREATE TABLE $TABLE_FAVORITE (
                $COLUMN_FAV_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COLUMN_FAV_BOOK_ID INTEGER NOT NULL,
                FOREIGN KEY($COLUMN_FAV_BOOK_ID) REFERENCES $TABLE_BOOKS($COLUMN_BOOK_ID)
            )
        """.trimIndent()

        db?.execSQL(createBooksTable)
        db?.execSQL(createAuthorsTable)
        db?.execSQL(createBookAuthorsTable)
        db?.execSQL(createCategoryTable)
        db?.execSQL(createBookCategoryTable)
        db?.execSQL(createOrderTable)
        db?.execSQL(createOrderItemTable)
        db?.execSQL(createCartTable)
        db?.execSQL(createFavoriteTable)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_BOOK_AUTHORS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_AUTHORS")
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_BOOKS")
        onCreate(db)
    }

    fun insertBook(
        title: String,
        isbn: String?,
        price: Double,
        description: String?,
        stockQuantity: Int,
        coverImagePath: String?
    ): Long {
        val db = writableDatabase

        val values = ContentValues().apply {
            put(COLUMN_BOOK_TITLE, title)
            put(COLUMN_BOOK_ISBN, isbn)
            put(COLUMN_BOOK_PRICE, price)
            put(COLUMN_BOOK_DESCRIPTION, description)
            put(COLUMN_BOOK_STOCK_QUANTITY, stockQuantity)
            put(COLUMN_BOOK_COVER_IMAGE_PATH, coverImagePath)
        }

        return db.insert(TABLE_BOOKS, null, values)
    }

}


package com.example.minishopmanager

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import java.security.MessageDigest

class DBHelper(context: Context) :
    SQLiteOpenHelper(context, "MiniShop.db", null, 2) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("""
            CREATE TABLE panier (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                product_id INTEGER NOT NULL,
                nom TEXT NOT NULL,
                categorie TEXT NOT NULL,
                prix REAL NOT NULL,
                description TEXT,
                date_ajout TEXT DEFAULT CURRENT_TIMESTAMP
            )
        """.trimIndent())

        db.execSQL("""
            CREATE TABLE users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                nom TEXT NOT NULL,
                email TEXT UNIQUE NOT NULL,
                password TEXT NOT NULL
            )
        """.trimIndent())
    }

    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        if (oldVersion < 2) {
            db.execSQL("""
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nom TEXT NOT NULL,
                    email TEXT UNIQUE NOT NULL,
                    password TEXT NOT NULL
                )
            """.trimIndent())
        }
    }

    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256")
            .digest(password.toByteArray())

        return bytes.joinToString("") { "%02x".format(it) }
    }

    fun registerUser(nom: String, email: String, password: String): Boolean {
        val values = ContentValues().apply {
            put("nom", nom)
            put("email", email.lowercase())
            put("password", hashPassword(password))
        }

        return writableDatabase.insert("users", null, values) != -1L
    }

    fun checkUser(email: String, password: String): Boolean {
        val cursor = readableDatabase.rawQuery(
            "SELECT id FROM users WHERE email = ? AND password = ?",
            arrayOf(email.lowercase(), hashPassword(password))
        )

        val exists = cursor.moveToFirst()
        cursor.close()
        return exists
    }

    fun ajouterAuPanier(product: Product): Long {
        val values = ContentValues().apply {
            put("product_id", product.id)
            put("nom", product.nom)
            put("categorie", product.categorie)
            put("prix", product.prix)
            put("description", product.description)
        }

        return writableDatabase.insert("panier", null, values)
    }

    fun getCartCount(): Int {
        val cursor = readableDatabase.rawQuery(
            "SELECT COUNT(*) FROM panier", null
        )

        var count = 0
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0)
        }
        cursor.close()
        return count
    }
}


package com.example.minishopmanager

import android.os.Bundle
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.json.JSONObject

class CatalogueActivity : AppCompatActivity() {

    private lateinit var dbHelper: DBHelper
    private lateinit var products: ArrayList<Product>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_catalogue)

        dbHelper = DBHelper(this)

        val listView = findViewById<ListView>(R.id.listViewProducts)
        val welcome = findViewById<TextView>(R.id.tvWelcomeUser)

        val username = intent.getStringExtra("USERNAME") ?: "Client"
        welcome.text = "Bienvenue, $username !"

        products = loadProducts()

        val adapter = ProductAdapter(this, products)
        listView.adapter = adapter

        listView.setOnItemClickListener { _, _, position, _ ->
            val product = products[position]
            val result = dbHelper.ajouterAuPanier(product)

            if (result != -1L) {
                Toast.makeText(
                    this,
                    "${product.nom} ajouté au panier ! Total : ${dbHelper.getCartCount()}",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this,
                    "Erreur lors de l'ajout au panier",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }


    private fun loadProducts(): ArrayList<Product> {
        val result = ArrayList<Product>()

        try {
            val jsonText = assets.open("produits.json").bufferedReader().use {
                it.readText()
            }

            val jsonObject = org.json.JSONObject(jsonText)
            val jsonArray = jsonObject.getJSONArray("produits")

            for (i in 0 until jsonArray.length()) {
                val item = jsonArray.getJSONObject(i)

                val product = Product(
                    id = item.getInt("id"),
                    nom = item.getString("nom"),
                    categorie = item.getString("categorie"),
                    prix = item.getDouble("prix"),
                    description = item.getString("description"),
                    image = item.getString("image")
                )

                result.add(product)
            }

            Toast.makeText(
                this,
                "${result.size} products loaded successfully",
                Toast.LENGTH_SHORT
            ).show()

        } catch (e: Exception) {
            android.util.Log.e("MINISHOP_JSON", "JSON loading failed", e)

            Toast.makeText(
                this,
                "JSON error: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
        }

        return result
    }


    override fun onDestroy() {
        super.onDestroy()
        if (::dbHelper.isInitialized) {
            dbHelper.close()
        }
    }
}

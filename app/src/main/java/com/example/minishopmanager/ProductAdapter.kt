
package com.example.minishopmanager

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class ProductAdapter(
    private val context: Context,
    private val products: ArrayList<Product>
) : BaseAdapter() {

    override fun getCount() = products.size

    override fun getItem(position: Int) = products[position]

    override fun getItemId(position: Int) = products[position].id.toLong()

    override fun getView(
        position: Int,
        convertView: View?,
        parent: ViewGroup?
    ): View {

        val view = convertView ?: LayoutInflater.from(context)
            .inflate(R.layout.item_product, parent, false)

        val product = products[position]

        view.findViewById<TextView>(R.id.tvProductIcon).text =
            when (product.image) {
                "headphones" -> "🎧"
                "watch" -> "⌚"
                "backpack" -> "🎒"
                "speaker" -> "🔊"
                "shoes" -> "👟"
                "lamp" -> "💡"
                else -> "🛍️"
            }

        view.findViewById<TextView>(R.id.tvProductName).text = product.nom
        view.findViewById<TextView>(R.id.tvProductCategory).text = product.categorie
        view.findViewById<TextView>(R.id.tvProductPrice).text =
            String.format("%.3f TND", product.prix)

        return view
    }
}

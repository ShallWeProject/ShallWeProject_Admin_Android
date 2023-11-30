package com.shall_we.admin.product

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.shall_we.admin.R

class ProductAdapter(private val productList: List<ProductData>?, private val onItemClicked: (position: Int) -> Unit) :
    RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title = itemView.findViewById<TextView>(R.id.title)
        val description = itemView.findViewById<TextView>(R.id.description)
    }

    override fun onBindViewHolder(holder: ProductAdapter.ProductViewHolder, position: Int) {
        val product = productList?.get(position)
        if (product != null) {
            holder.title.text = product.title
            holder.description.text = product.subtitle

            holder.itemView.setOnClickListener {
                onItemClicked(position)
            }
        }
    }

    override fun getItemCount(): Int = productList?.size ?: 0
}

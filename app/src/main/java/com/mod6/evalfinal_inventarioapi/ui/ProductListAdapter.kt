package com.mod6.evalfinal_inventarioapi.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mod6.evalfinal_inventarioapi.databinding.ItemProductoBinding
import com.mod6.evalfinal_inventarioapi.inventario.data.ProductoEntity

class ProductListAdapter(private val listener: OnItemActionListener) :
    ListAdapter<ProductoEntity, ProductListAdapter.ProductViewHolder>(DiffCallback()) {

    interface OnItemActionListener {
        fun onEdit(producto: ProductoEntity)
        fun onDelete(producto: ProductoEntity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding = ItemProductoBinding.inflate(LayoutInflater.from(parent.context),
            parent, false)
        return ProductViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ProductViewHolder(private val b: ItemProductoBinding)
        : RecyclerView.ViewHolder(b.root) {
        fun bind(item: ProductoEntity) {
            b.tvName.text         = item.nombre
            b.tvDescription.text  = item.descripcion
            b.tvPrice.text        = "Precio: ${item.precio}"
            b.tvQuantity.text     = "Cantidad: ${item.cantidad}"
            b.ivSynced.visibility = if (item.isSynced) View.GONE else View.VISIBLE

            b.btnEdit.setOnClickListener { listener.onEdit(item) }
            b.btnDelete.setOnClickListener { listener.onDelete(item) }
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<ProductoEntity>() {
        override fun areItemsTheSame(oldItem: ProductoEntity, newItem: ProductoEntity): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ProductoEntity, newItem: ProductoEntity): Boolean =
            oldItem == newItem
    }
}
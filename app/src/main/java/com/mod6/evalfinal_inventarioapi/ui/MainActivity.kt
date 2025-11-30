package com.mod6.evalfinal_inventarioapi.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.mod6.evalfinal_inventarioapi.databinding.ActivityMainBinding
import com.mod6.evalfinal_inventarioapi.inventario.data.ProductoEntity
import com.mod6.evalfinal_inventarioapi.viewmodel.InventoryViewModel
import com.mod6.evalfinal_inventarioapi.viewmodel.UiState
import com.mod6.evalfinal_inventarioapi.databinding.DialogAddEditProductBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

// Main: muestra la lista de productos.
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ProductListAdapter.OnItemActionListener {
    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: ProductListAdapter
    private val viewModel: InventoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = ProductListAdapter(this)
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // Observers
        lifecycleScope.launch {
            viewModel.products.collectLatest { list ->
                adapter.submitList(list)
            }
        }

        lifecycleScope.launch {
            viewModel.uiState.collectLatest { state ->
                when (state) {
                    is UiState.Loading -> binding.progressBar.visibility = View.VISIBLE
                    is UiState.Success -> {
                        binding.progressBar.visibility = View.GONE
                    }
                    is UiState.Error -> {
                        binding.progressBar.visibility = View.GONE
                        // Alerta
                        AlertDialog.Builder(this@MainActivity)
                            .setTitle("Error")
                            .setMessage(state.error)
                            .setPositiveButton("OK", null)
                            .show()
                    }
                    else -> binding.progressBar.visibility = View.GONE
                }
            }
        }
        binding.fabAdd.setOnClickListener {
            showAddEditDialog(null)
        }
    }

    private fun showAddEditDialog(existing: ProductoEntity?) {
        val dialogBinding = DialogAddEditProductBinding.inflate(LayoutInflater.from(this))
        val builder = AlertDialog.Builder(this).setView(dialogBinding.root)
        if (existing != null) {
            dialogBinding.etName.setText(existing.nombre)
            dialogBinding.etDescription.setText(existing.descripcion)
            dialogBinding.etPrice.setText(existing.precio.toString())
            dialogBinding.etQuantity.setText(existing.cantidad.toString())
            builder.setTitle("Editar producto")
        } else {
            builder.setTitle("Agregar producto")
        }

        builder.setPositiveButton("Guardar") { dialog, _ ->
            val name  = dialogBinding.etName.text.toString()
            val desc  = dialogBinding.etDescription.text.toString()
            val price = dialogBinding.etPrice.text.toString().toDoubleOrNull() ?: -1.0
            val qty   = dialogBinding.etQuantity.text.toString().toIntOrNull() ?: -1

            // Valida datos para evitar guardar -1
            if (name.isBlank() || price <= 0 || qty < 0) {
                return@setPositiveButton // Sale sin guardar ni cerrar el diálogo
            }

            if (existing != null) {
                val updated = existing.copy(
                    nombre      = name,
                    descripcion = desc,
                    precio      = price,
                    cantidad    = qty,
                    isSynced    = false
                )
                viewModel.updateProduct(updated)
            } else {
                viewModel.addProduct(name,
                    desc,
                    price,
                    qty)
            }
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancelar", null)
        builder.show()
    }

    override fun onEdit(producto: ProductoEntity) {
        showAddEditDialog(producto)
    }

    override fun onDelete(producto: ProductoEntity) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar")
            .setMessage("Eliminar ${producto.nombre}?")
            .setPositiveButton("Sí") { _, _ ->
                viewModel.deleteProduct(producto)
            }
            .setNegativeButton("No", null)
            .show()
    }
}
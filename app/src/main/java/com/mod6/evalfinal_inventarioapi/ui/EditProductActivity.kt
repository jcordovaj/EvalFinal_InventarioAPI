package com.mod6.evalfinal_inventarioapi.ui

import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.mod6.evalfinal_inventarioapi.databinding.ActivityEditProductBinding
import com.mod6.evalfinal_inventarioapi.inventario.data.ProductoEntity
import com.mod6.evalfinal_inventarioapi.viewmodel.InventoryViewModel
import dagger.hilt.android.AndroidEntryPoint

// Actividad que permite editar o ver los detalles de un producto existente.
@AndroidEntryPoint
class EditProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProductBinding

    // Inyección del ViewModel
    private val viewModel: InventoryViewModel by viewModels()

    private var producto: ProductoEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        producto = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("producto", ProductoEntity::class.java)
        } else {
            @Suppress("DEPRECATION", "UNCHECKED_CAST")
            (intent.getParcelableExtra("producto") as Parcelable?) as ProductoEntity?
        }

        // Carga los datos del producto en el form. de edición
        producto?.let { p ->
            binding.inputNombre.setText(p.nombre)
            binding.inputDescripcion.setText(p.descripcion)
            binding.inputPrecio.setText(p.precio.toString())
            binding.inputCantidad.setText(p.cantidad.toString())
        }

        // Botón Guardar
        binding.btnSave.setOnClickListener {
            producto?.let { p ->
                val newName  = binding.inputNombre.text.toString()
                val newDesc  = binding.inputDescripcion.text.toString()
                val newPrice = binding.inputPrecio.text.toString().toDoubleOrNull()
                val newQty   = binding.inputCantidad.text.toString().toIntOrNull()

                // Valida campos
                if (newName.isNotBlank() && newPrice != null && newQty != null &&
                    newPrice > 0 && newQty >= 0) {
                    val updatedProduct = p.copy(
                        nombre      = newName,
                        descripcion = newDesc,
                        precio      = newPrice,
                        cantidad    = newQty,
                        isSynced    = false
                    )
                    viewModel.updateProduct(updatedProduct)
                    finish()
                } else {
                    // Falta manejo de error de validación
                }
            }
        }
    }
}
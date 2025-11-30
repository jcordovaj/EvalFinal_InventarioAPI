package com.mod6.evalfinal_inventarioapi.ui

import android.os.Bundle
import androidx.activity.viewModels // Importar el delegado
import androidx.appcompat.app.AppCompatActivity
import com.mod6.evalfinal_inventarioapi.databinding.ActivityCreateProductBinding
import com.mod6.evalfinal_inventarioapi.viewmodel.InventoryViewModel
import dagger.hilt.android.AndroidEntryPoint // Importar AndroidEntryPoint

@AndroidEntryPoint
class CreateProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateProductBinding

    private val viewModel: InventoryViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnSave.setOnClickListener {
            val name  = binding.inputNombre.text.toString()
            val desc  = binding.inputDescripcion.text.toString()
            // Usamos un valor seguro (-1) para la validación interna del ViewModel
            val price = binding.inputPrecio.text.toString().toDoubleOrNull() ?: -1.0
            val qty   = binding.inputCantidad.text.toString().toIntOrNull() ?: -1

            viewModel.addProduct(name,
                                desc,
                                price,
                                qty)

            finish()
        }
    }
}
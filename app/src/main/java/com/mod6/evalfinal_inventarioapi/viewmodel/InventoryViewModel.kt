package com.mod6.evalfinal_inventarioapi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mod6.evalfinal_inventarioapi.inventario.data.ProductoEntity
import com.mod6.evalfinal_inventarioapi.inventario.repository.InventoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class UiState {
    data object Idle : UiState()
    object Loading: UiState()
    data class Success(val message: String): UiState()
    data class Error(val error: String): UiState()
}

@HiltViewModel
class InventoryViewModel@Inject constructor(private val repository: InventoryRepository
) : ViewModel() {

    val products: StateFlow<List<ProductoEntity>> =
        repository.allProductos.stateIn(viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            emptyList())

    private val _uiState = MutableStateFlow<UiState>(UiState.Idle)
    val uiState: StateFlow<UiState> = _uiState

    init {
        //loadAndSyncProducts()
    }

    fun loadAndSyncProducts() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                repository.syncProducts()
                _uiState.value = UiState.Success("Sincronización completada")
            } catch (e: Exception) {
                _uiState.value = UiState.Error("Error sincronizando: ${e.message}")
            }
        }
    }

    fun addProduct(nombre: String, descripcion: String, precio: Double, cantidad: Int) {
        viewModelScope.launch {
            if (nombre.isBlank() || precio <= 0.0 || cantidad <= 0) {
                _uiState.value = UiState.Error("Validación: nombre, precio o cantidad inválidos")
                return@launch
            }
            _uiState.value = UiState.Loading
            repository.createProducto(nombre, descripcion, precio, cantidad)
            _uiState.value = UiState.Success("Producto agregado")
        }
    }

    fun updateProduct(producto: ProductoEntity) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            repository.updateProducto(producto)
            _uiState.value = UiState.Success("Producto actualizado")
        }
    }

    fun deleteProduct(producto: ProductoEntity) {
        viewModelScope.launch { // SÓLO una corrutina
            _uiState.value = UiState.Loading
            repository.deleteProducto(producto)
            _uiState.value = UiState.Success("Producto eliminado")
        }
    }
}
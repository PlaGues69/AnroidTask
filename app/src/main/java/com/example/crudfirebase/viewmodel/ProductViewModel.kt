package com.example.crudfirebase.viewmodel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.crudfirebase.model.Product
import com.example.crudfirebase.repository.ProductRepository

class ProductViewModel : ViewModel() {
    private val repository = ProductRepository()
    val products = MutableLiveData<List<Product>>()

    fun fetchProducts() {
        repository.getProducts { productList ->
            products.value = productList
        }
    }

    fun addProduct(product: Product, imageUri: Uri?) {
        repository.addProduct(product, imageUri) { success ->
            if (success) {
                fetchProducts() // Refresh the list after adding
            }
        }
    }

    fun updateProduct(product: Product, imageUri: Uri?) {
        repository.updateProduct(product, imageUri) { success ->
            if (success) {
                fetchProducts() // Refresh the list after updating
            }
        }
    }

    fun deleteProduct(productId: String) {
        repository.deleteProduct(productId) { success ->
            if (success) {
                fetchProducts() // Refresh the list after deleting
            }
        }
    }
}

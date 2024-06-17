package com.example.crudfirebase.repository

import android.net.Uri
import com.example.crudfirebase.model.Product
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage

class ProductRepository {
    private val database = FirebaseDatabase.getInstance().getReference("products")
    private val storage = FirebaseStorage.getInstance().reference.child("product_images")

    fun addProduct(product: Product, imageUri: Uri?, onResult: (Boolean) -> Unit) {
        product.id = database.push().key ?: ""
        if (imageUri != null) {
            val imageRef = storage.child(product.id)
            imageRef.putFile(imageUri).addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    product.imageUrl = uri.toString()
                    database.child(product.id).setValue(product).addOnCompleteListener {
                        onResult(it.isSuccessful)
                    }
                }
            }.addOnFailureListener {
                onResult(false)
            }
        } else {
            database.child(product.id).setValue(product).addOnCompleteListener {
                onResult(it.isSuccessful)
            }
        }
    }

    fun updateProduct(product: Product, imageUri: Uri?, onResult: (Boolean) -> Unit) {
        if (imageUri != null) {
            val imageRef = storage.child(product.id)
            imageRef.putFile(imageUri).addOnSuccessListener {
                imageRef.downloadUrl.addOnSuccessListener { uri ->
                    product.imageUrl = uri.toString()
                    database.child(product.id).setValue(product).addOnCompleteListener {
                        onResult(it.isSuccessful)
                    }
                }
            }.addOnFailureListener {
                onResult(false)
            }
        } else {
            database.child(product.id).setValue(product).addOnCompleteListener {
                onResult(it.isSuccessful)
            }
        }
    }

    fun deleteProduct(productId: String, onResult: (Boolean) -> Unit) {
        database.child(productId).removeValue().addOnCompleteListener {
            onResult(it.isSuccessful)
        }
    }

    fun getProducts(onResult: (List<Product>) -> Unit) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val products = mutableListOf<Product>()
                snapshot.children.forEach {
                    it.getValue(Product::class.java)?.let { product ->
                        products.add(product)
                    }
                }
                onResult(products)
            }

            override fun onCancelled(error: DatabaseError) {
                onResult(emptyList())
            }
        })
    }
}

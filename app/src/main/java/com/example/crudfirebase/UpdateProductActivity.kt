package com.example.crudfirebase

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.crudfirebase.model.Product
import com.example.crudfirebase.viewmodel.ProductViewModel
import com.squareup.picasso.Picasso

class UpdateProductActivity : AppCompatActivity() {

    private val productViewModel: ProductViewModel by viewModels()
    private lateinit var product: Product
    private lateinit var productImageView: ImageView
    private var imageUri: Uri? = null

    private val selectImageResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            imageUri = result.data?.data
            productImageView.setImageURI(imageUri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_product)

        product = intent.getParcelableExtra("product") ?: Product()

        val productNameInput: EditText = findViewById(R.id.productNameInput)
        val productDescriptionInput: EditText = findViewById(R.id.productDescriptionInput)
        val productPriceInput: EditText = findViewById(R.id.productPriceInput)
        productImageView = findViewById(R.id.productImageView)
        val selectImageButton: Button = findViewById(R.id.selectImageButton)
        val saveProductButton: Button = findViewById(R.id.saveProductButton)

        productNameInput.setText(product.name)
        productDescriptionInput.setText(product.description)
        productPriceInput.setText(product.price.toString())
        Picasso.get().load(product.imageUrl).into(productImageView)

        selectImageButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK).apply {
                type = "image/*"
            }
            selectImageResult.launch(intent)
        }

        saveProductButton.setOnClickListener {
            product.name = productNameInput.text.toString()
            product.description = productDescriptionInput.text.toString()
            product.price = productPriceInput.text.toString().toDoubleOrNull() ?: 0.0

            productViewModel.updateProduct(product, imageUri)

            finish()
        }
    }
}

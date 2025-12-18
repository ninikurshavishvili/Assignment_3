package com.example.assignment_3.model


data class Product(
    var id: String? = "",
    var name: String? = "",
    var price: String? = "",
    var description: String? = "",
    var category: String? = "",
    var inStock: Boolean = true,
    var rating: Double = 0.0,
    var imageUrl: String? = ""
)
package com.cesar.catalogviewer.domain.model

data class CatalogItem(
    val id: String,
    val title: String,
    val category: String,
    val price: Double,
    val rating: Double,
    val isFavorite: Boolean = false
)
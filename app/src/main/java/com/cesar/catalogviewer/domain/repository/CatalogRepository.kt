package com.cesar.catalogviewer.domain.repository

import com.cesar.catalogviewer.domain.model.CatalogItem

interface CatalogRepository {
    suspend fun getItems(): List<CatalogItem>
    suspend fun getItemById(id: String): CatalogItem?
}
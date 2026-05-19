package com.cesar.catalogviewer.data.repository

import com.cesar.catalogviewer.data.datasource.CatalogLocalDataSource
import com.cesar.catalogviewer.data.mapper.toDomain
import com.cesar.catalogviewer.domain.model.CatalogItem
import com.cesar.catalogviewer.domain.repository.CatalogRepository
import javax.inject.Inject

class CatalogRepositoryImpl @Inject constructor(
    private val catalogLocalDataSource: CatalogLocalDataSource
) : CatalogRepository {

    override suspend fun getItems(): List<CatalogItem> =
        catalogLocalDataSource.getCatalogItems().map { itemDto ->
            itemDto.toDomain()
        }

    override suspend fun getItemById(id: String): CatalogItem? =
        getItems().firstOrNull { item ->
            item.id == id
        }
}

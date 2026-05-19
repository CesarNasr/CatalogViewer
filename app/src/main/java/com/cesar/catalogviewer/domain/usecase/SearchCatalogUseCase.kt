package com.cesar.catalogviewer.domain.usecase

import com.cesar.catalogviewer.domain.model.CatalogItem
import javax.inject.Inject

class SearchCatalogUseCase @Inject constructor() {
    operator fun invoke(query: String, items: List<CatalogItem>): List<CatalogItem> =
        if (query.isBlank()) items
        else items.filter { it.title.contains(query.trim(), ignoreCase = true) }
}
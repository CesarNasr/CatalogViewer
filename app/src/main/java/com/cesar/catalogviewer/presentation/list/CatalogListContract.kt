package com.cesar.catalogviewer.presentation.list

import com.cesar.catalogviewer.domain.model.CatalogItem

data class CatalogListState(
    val isLoading: Boolean = false,
    val items: List<CatalogItem> = emptyList(),
    val visibleItems: List<CatalogItem> = emptyList(),
    val searchQuery: String = "",
    val errorMessage: String? = null
)

sealed interface CatalogListIntent {
    data class SearchQueryChanged(val query: String) : CatalogListIntent
    data class ItemClicked(val itemId: String) : CatalogListIntent
    data class FavoriteClicked(val itemId: String) : CatalogListIntent
    data object RetryClicked : CatalogListIntent
}

sealed interface CatalogListEffect {
    data class NavigateToDetail(val itemId: String) : CatalogListEffect
}

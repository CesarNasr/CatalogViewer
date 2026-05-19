package com.cesar.catalogviewer.presentation.detail

import com.cesar.catalogviewer.domain.model.CatalogItem

data class CatalogDetailState(
    val isLoading: Boolean = false,
    val item: CatalogItem? = null,
    val errorMessage: String? = null
)

sealed interface CatalogDetailIntent {
    data class FavoriteClicked(val itemId: String) : CatalogDetailIntent
    data object BackClicked : CatalogDetailIntent
    data object RetryClicked : CatalogDetailIntent
}

sealed interface CatalogDetailEffect {
    data object NavigateBack : CatalogDetailEffect
}

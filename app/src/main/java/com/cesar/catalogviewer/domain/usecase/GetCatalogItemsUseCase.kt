package com.cesar.catalogviewer.domain.usecase

import com.cesar.catalogviewer.domain.model.CatalogItem
import com.cesar.catalogviewer.domain.model.Resource
import com.cesar.catalogviewer.domain.repository.CatalogRepository
import com.cesar.catalogviewer.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetCatalogItemsUseCase @Inject constructor(
    private val catalogRepository: CatalogRepository,
    private val favoritesRepository: FavoritesRepository
) {
    operator fun invoke(): Flow<Resource<List<CatalogItem>>> = flow {
        emit(Resource.Loading)

        try {
            val items = catalogRepository.getItems()

            favoritesRepository.observeFavorites().collect { favoriteIds ->
                val itemsWithFavorites = items.map { item ->
                    item.copy(isFavorite = item.id in favoriteIds)
                }

                emit(Resource.Success(itemsWithFavorites))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unable to load catalog", e))
        }
    }
}

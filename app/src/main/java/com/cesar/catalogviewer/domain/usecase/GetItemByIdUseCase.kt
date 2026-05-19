package com.cesar.catalogviewer.domain.usecase

import com.cesar.catalogviewer.domain.model.CatalogItem
import com.cesar.catalogviewer.domain.model.Resource
import com.cesar.catalogviewer.domain.repository.CatalogRepository
import com.cesar.catalogviewer.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetItemByIdUseCase @Inject constructor(
    private val catalogRepository: CatalogRepository,
    private val favoritesRepository: FavoritesRepository
) {
    operator fun invoke(id: String): Flow<Resource<CatalogItem?>> = flow {
        emit(Resource.Loading)

        try {
            val item = catalogRepository.getItemById(id)

            favoritesRepository.observeFavorites().collect { favoriteIds ->
                emit(Resource.Success(item?.copy(isFavorite = id in favoriteIds)))
            }
        } catch (e: Exception) {
            emit(Resource.Error(e.message ?: "Unable to load item", e))
        }
    }
}

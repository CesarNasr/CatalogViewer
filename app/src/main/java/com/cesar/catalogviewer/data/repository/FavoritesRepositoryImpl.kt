package com.cesar.catalogviewer.data.repository

import com.cesar.catalogviewer.data.datasource.FavoritesLocalDataSource
import com.cesar.catalogviewer.domain.repository.FavoritesRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class FavoritesRepositoryImpl @Inject constructor(
    private val favoritesLocalDataSource: FavoritesLocalDataSource
) : FavoritesRepository {

    override fun observeFavorites(): Flow<Set<String>> =
        favoritesLocalDataSource.observeFavoriteIds()

    override suspend fun toggleFavorite(id: String) {
        favoritesLocalDataSource.toggleFavorite(id)
    }

    override suspend fun isFavorite(id: String): Boolean =
        favoritesLocalDataSource.isFavorite(id)
}

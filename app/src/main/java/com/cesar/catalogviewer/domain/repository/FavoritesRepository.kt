package com.cesar.catalogviewer.domain.repository

import kotlinx.coroutines.flow.Flow

interface FavoritesRepository {
    fun observeFavorites(): Flow<Set<String>>
    suspend fun toggleFavorite(id: String)
    suspend fun isFavorite(id: String): Boolean
}

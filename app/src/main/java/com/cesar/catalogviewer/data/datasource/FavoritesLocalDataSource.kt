package com.cesar.catalogviewer.data.datasource

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

private val Context.favoritesDataStore by preferencesDataStore(
    name = "favorites_preferences"
)

class FavoritesLocalDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun observeFavoriteIds(): Flow<Set<String>> =
        context.favoritesDataStore.data
            .catch { exception ->
                if (exception is IOException) {
                    emit(emptyPreferences())
                } else {
                    throw exception
                }
            }.map { preferences ->
                preferences[FAVORITE_IDS_KEY].orEmpty()
            }


    suspend fun toggleFavorite(id: String) {
        context.favoritesDataStore.edit { preferences ->
            val currentFavorites = preferences[FAVORITE_IDS_KEY].orEmpty()

            preferences[FAVORITE_IDS_KEY] =
                if (id in currentFavorites) {
                    currentFavorites - id
                } else {
                    currentFavorites + id
                }
        }
    }

    suspend fun isFavorite(id: String): Boolean =
        id in observeFavoriteIds().first()

    private companion object {
        val FAVORITE_IDS_KEY = stringSetPreferencesKey("favorite_ids")
    }
}

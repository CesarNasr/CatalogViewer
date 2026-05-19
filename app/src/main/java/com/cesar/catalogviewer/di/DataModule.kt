package com.cesar.catalogviewer.di

import com.cesar.catalogviewer.data.repository.CatalogRepositoryImpl
import com.cesar.catalogviewer.data.repository.FavoritesRepositoryImpl
import com.cesar.catalogviewer.domain.repository.CatalogRepository
import com.cesar.catalogviewer.domain.repository.FavoritesRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {

    @Binds
    @Singleton
    abstract fun bindCatalogRepository(
        impl: CatalogRepositoryImpl
    ): CatalogRepository

    @Binds
    @Singleton
    abstract fun bindFavoritesRepository(
        impl: FavoritesRepositoryImpl
    ): FavoritesRepository

    companion object {
        @Provides
        @Singleton
        fun provideJson(): Json =
            Json {
                ignoreUnknownKeys = true
                isLenient = true
            }
    }
}

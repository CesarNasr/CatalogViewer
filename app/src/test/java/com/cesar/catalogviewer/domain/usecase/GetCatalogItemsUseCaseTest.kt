package com.cesar.catalogviewer.domain.usecase

import app.cash.turbine.test
import com.cesar.catalogviewer.domain.model.CatalogItem
import com.cesar.catalogviewer.domain.model.Resource
import com.cesar.catalogviewer.domain.repository.CatalogRepository
import com.cesar.catalogviewer.domain.repository.FavoritesRepository
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GetCatalogItemsUseCaseTest {

    private val catalogRepository = mockk<CatalogRepository>()
    private val favoritesRepository = mockk<FavoritesRepository>()

    private val useCase = GetCatalogItemsUseCase(
        catalogRepository = catalogRepository,
        favoritesRepository = favoritesRepository
    )

    @Test
    fun `invoke emits loading then success with favorite state`() = runTest {
        val favoriteIds = MutableStateFlow(setOf("bk_001"))

        coEvery { catalogRepository.getItems() } returns catalogItems
        every { favoritesRepository.observeFavorites() } returns favoriteIds

        useCase().test {
            assertEquals(Resource.Loading, awaitItem())

            val success = awaitItem() as Resource.Success
            assertEquals(2, success.data.size)
            assertTrue(success.data.first { it.id == "bk_001" }.isFavorite)
            assertFalse(success.data.first { it.id == "bk_002" }.isFavorite)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `invoke emits updated success when favorites change`() = runTest {
        val favoriteIds = MutableStateFlow<Set<String>>(emptySet())

        coEvery { catalogRepository.getItems() } returns catalogItems
        every { favoritesRepository.observeFavorites() } returns favoriteIds

        useCase().test {
            assertEquals(Resource.Loading, awaitItem())

            val initialSuccess = awaitItem() as Resource.Success
            assertFalse(initialSuccess.data.first { it.id == "bk_001" }.isFavorite)

            favoriteIds.value = setOf("bk_001")

            val updatedSuccess = awaitItem() as Resource.Success
            assertTrue(updatedSuccess.data.first { it.id == "bk_001" }.isFavorite)

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `invoke emits error when catalog loading fails`() = runTest {
        val exception = IllegalStateException("Catalog failed")

        coEvery { catalogRepository.getItems() } throws exception

        useCase().test {
            assertEquals(Resource.Loading, awaitItem())

            val error = awaitItem() as Resource.Error
            assertEquals("Catalog failed", error.message)
            assertEquals(exception, error.throwable)

            awaitComplete()
        }
    }

    private companion object {
        val catalogItems = listOf(
            CatalogItem(
                id = "bk_001",
                title = "The Blue Fox",
                category = "Fiction",
                price = 12.99,
                rating = 4.4
            ),
            CatalogItem(
                id = "bk_002",
                title = "Data Sketches",
                category = "Non-Fiction",
                price = 32.00,
                rating = 4.8
            )
        )
    }
}

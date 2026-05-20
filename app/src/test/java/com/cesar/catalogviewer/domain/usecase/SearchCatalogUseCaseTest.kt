package com.cesar.catalogviewer.domain.usecase

import com.cesar.catalogviewer.domain.model.CatalogItem
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SearchCatalogUseCaseTest {

    private val useCase = SearchCatalogUseCase()

    @Test
    fun `blank query returns all items`() {
        val result = useCase(
            query = "",
            items = catalogItems
        )

        assertEquals(catalogItems, result)
    }

    @Test
    fun `query filters items by title ignoring case`() {
        val result = useCase(
            query = "kotlin",
            items = catalogItems
        )

        assertEquals(
            listOf(catalogItems[3]),
            result
        )
    }

    @Test
    fun `query trims whitespace before filtering`() {
        val result = useCase(
            query = "  blue  ",
            items = catalogItems
        )

        assertEquals(
            listOf(catalogItems[0]),
            result
        )
    }

    @Test
    fun `unknown query returns empty list`() {
        val result = useCase(
            query = "missing",
            items = catalogItems
        )

        assertTrue(result.isEmpty())
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
            ),
            CatalogItem(
                id = "bk_003",
                title = "Swift Patterns",
                category = "Tech",
                price = 24.50,
                rating = 4.1
            ),
            CatalogItem(
                id = "bk_004",
                title = "Kotlin by Example",
                category = "Tech",
                price = 21.00,
                rating = 4.3
            ),
            CatalogItem(
                id = "bk_005",
                title = "Windswept",
                category = "Fiction",
                price = 14.25,
                rating = 3.9
            )
        )
    }
}

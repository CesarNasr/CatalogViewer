package com.cesar.catalogviewer.data.mapper

import com.cesar.catalogviewer.data.dto.CatalogItemDto
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class CatalogMapperTest {

    @Test
    fun `toDomain maps catalog item dto to domain model`() {
        val dto = CatalogItemDto(
            id = "bk_001",
            title = "The Blue Fox",
            category = "Fiction",
            price = 12.99,
            rating = 4.4
        )

        val result = dto.toDomain()

        assertEquals("bk_001", result.id)
        assertEquals("The Blue Fox", result.title)
        assertEquals("Fiction", result.category)
        assertEquals(12.99, result.price, DOUBLE_DELTA)
        assertEquals(4.4, result.rating, DOUBLE_DELTA)
        assertFalse(result.isFavorite)
    }

    private companion object {
        const val DOUBLE_DELTA = 0.0
    }
}

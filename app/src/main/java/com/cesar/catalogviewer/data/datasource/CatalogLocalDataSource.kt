package com.cesar.catalogviewer.data.datasource

import android.content.Context
import com.cesar.catalogviewer.data.dto.CatalogItemDto
import com.cesar.catalogviewer.data.dto.DataEnvelope
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.serialization.json.Json
import javax.inject.Inject

class CatalogLocalDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
    private val json: Json
) {
    suspend fun getCatalogItems(): List<CatalogItemDto> {
        val catalogJson = context.assets
            .open(CATALOG_FILE_NAME)
            .bufferedReader()
            .use { reader -> reader.readText() }

        return json.decodeFromString<DataEnvelope<List<CatalogItemDto>>>(catalogJson).items
    }

    private companion object {
        const val CATALOG_FILE_NAME = "catalog.json"
    }
}

package com.cesar.catalogviewer.data.datasource

import android.content.Context
import com.cesar.catalogviewer.data.dto.CatalogItemDto
import com.cesar.catalogviewer.data.dto.DataEnvelope
import com.cesar.catalogviewer.di.IoDispatcher
import com.cesar.catalogviewer.domain.error.CatalogException
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import java.io.IOException
import javax.inject.Inject

class CatalogLocalDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
    private val json: Json,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
) {
    suspend fun getCatalogItems(): List<CatalogItemDto> =
        withContext(ioDispatcher) {
            val catalogJson = try {
                context.assets
                    .open(CATALOG_FILE_NAME)
                    .bufferedReader()
                    .use { reader -> reader.readText() }
            } catch (e: IOException) {
                throw CatalogException.CatalogFileNotFound(e)
            }

            try {
                json.decodeFromString<DataEnvelope<List<CatalogItemDto>>>(catalogJson).items
            } catch (e: SerializationException) {
                throw CatalogException.CatalogParsingFailed(e)
            }
        }

    private companion object {
        const val CATALOG_FILE_NAME = "catalog.json"
    }
}


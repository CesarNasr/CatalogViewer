package com.cesar.catalogviewer.data.util

import com.cesar.catalogviewer.domain.model.Resource

suspend fun <T> safeCall(call: suspend () -> T): Resource<T> = try {
    Resource.Success(call())
} catch (e: Exception) {
    Resource.Error(e.message ?: "Unexpected error", e)
}
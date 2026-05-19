package com.cesar.catalogviewer.data.dto


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DataEnvelope<T>(
    @SerialName("updatedAt") val updatedAt: String,
    @SerialName("items") val items: T
)
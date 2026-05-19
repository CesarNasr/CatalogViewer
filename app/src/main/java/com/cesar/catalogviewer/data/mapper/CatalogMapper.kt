package com.cesar.catalogviewer.data.mapper

import com.cesar.catalogviewer.data.dto.CatalogItemDto
import com.cesar.catalogviewer.domain.model.CatalogItem

fun CatalogItemDto.toDomain(): CatalogItem =
    CatalogItem(
        id = id,
        title = title,
        category = category,
        price = price,
        rating = rating
    )

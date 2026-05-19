package com.cesar.catalogviewer.domain.error

sealed class CatalogException(
    message: String,
    cause: Throwable? = null
) : Exception(message, cause) {

    class CatalogFileNotFound(
        cause: Throwable
    ) : CatalogException("Catalog file could not be found.", cause)

    class CatalogParsingFailed(
        cause: Throwable
    ) : CatalogException("Catalog data could not be read.", cause)
}

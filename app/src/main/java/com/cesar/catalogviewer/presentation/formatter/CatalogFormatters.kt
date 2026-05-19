package com.cesar.catalogviewer.presentation.formatter

import java.text.NumberFormat
import java.util.Locale

fun formatPrice(price: Double): String =
    NumberFormat.getCurrencyInstance(Locale.US).format(price)

fun formatRating(rating: Double): String =
    "Rating $rating"

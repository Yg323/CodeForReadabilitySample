package com.pluu.sample.codeforreadability.model

data class SampleItem(
    val text: String,
    val bgColor: ColorValue,
    val isFavorite: Boolean = false,
    val onFavorite: (String) -> Unit
)
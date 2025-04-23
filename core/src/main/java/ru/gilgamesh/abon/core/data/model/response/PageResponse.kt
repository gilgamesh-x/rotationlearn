package ru.gilgamesh.abon.core.data.model.response

data class PageResponse<T>(
    val content: List<T>,
    val last: Boolean,
    val totalPages: Int,
    val totalElements: Int,
    val size: Int,
    val number: Int,
    val first: Boolean,
    val numberOfElements: Int,
    val empty: Boolean
)
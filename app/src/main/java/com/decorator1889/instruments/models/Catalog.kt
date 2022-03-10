package com.decorator1889.instruments.models

import com.decorator1889.instruments.util.*

data class Catalog(
    val id: Long,
    val name: String,
    val type: String,
    val count: String,
    val lock: Boolean
)

fun getCatalogList() : List<Catalog> {
    return listOf(
        Catalog(1, surgery, "surgery", "40", true),
        Catalog(2, dentistry, "dentistry", "24", true),
        Catalog(3, obstetrics_gynecology, "obstetrics_gynecology", "37", true),
        Catalog(4, neurosurgery, "neurosurgery", "18", false),
        Catalog(5, ophthalmology, "ophthalmology", "12", false),
        Catalog(6, otorhinolaryngology, "otorhinolaryngology", "24", false),
    )
}

fun getCatalogCategoryList() : List<Catalog> {
    return listOf(
        Catalog(1, "Разъединяющие", "surgery", "40", true),
        Catalog(2, "Соединяющие", "surgery", "24", true),
        Catalog(3, "Оттесняющие, раздвигающие", "surgery", "37", true),
        Catalog(4, "Удерживающие", "surgery", "18", false),
        Catalog(5, "Колющие", "surgery", "12", false),
    )
}
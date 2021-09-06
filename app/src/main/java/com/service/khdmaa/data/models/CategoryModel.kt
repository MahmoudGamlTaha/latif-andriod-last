package com.service.khdmaa.data.models

data class CategoryModel(
    val category: CategoryItemsModel
)

data class CategoryItemsModel(
    val id: Int?,
    val name: String?,
    val nameAr: String?,
    val icon: String?,
    val iconSelect: String?,
    val isExternalLink: Boolean
)

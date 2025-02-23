package com.andredigital.catancounter

import kotlinx.serialization.Serializable

data class CatanResourceEntity(
    val rollNumber: Int = 2,
    val items: List<CatanResourceItemEntity> = emptyList(),
)

@Serializable
data class CatanResourceItemEntity(
    val resourceName: CatanResourceTypeEntity = CatanResourceTypeEntity.Wheat,
    val rollNumber: Int = 2,
    val count: Int
)

@Serializable
sealed class CatanResourceTypeEntity {
    @Serializable
    data object Wheat : CatanResourceTypeEntity()
    @Serializable
    data object Brick : CatanResourceTypeEntity()
    @Serializable
    data object Wood : CatanResourceTypeEntity()
    @Serializable
    data object Ore : CatanResourceTypeEntity()
    @Serializable
    data object Sheep : CatanResourceTypeEntity()
}
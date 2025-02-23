package com.andredigital.catancounter

import catancounter.composeapp.generated.resources.Res
import catancounter.composeapp.generated.resources.dice_six_faces_five
import catancounter.composeapp.generated.resources.dice_six_faces_four
import catancounter.composeapp.generated.resources.dice_six_faces_one
import catancounter.composeapp.generated.resources.dice_six_faces_six
import catancounter.composeapp.generated.resources.dice_six_faces_three
import catancounter.composeapp.generated.resources.dice_six_faces_two
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.DrawableResource

data class CatanCardState(
    val rollNumber: Int = 2,
    val items: List<CatanResourceItem> = emptyList(),
    val rollNumberIcons: Pair<RollNumberIcon, RollNumberIcon>? = Roll().icons[rollNumber]
) {
    fun toEntity(): CatanResourceEntity {
        return CatanResourceEntity(
            rollNumber = rollNumber,
            items = items.map { it.toEntity() }
        )
    }

    fun fromEntity(entity: CatanResourceEntity): CatanCardState {
        return CatanCardState(
            rollNumber = entity.rollNumber,
            items = entity.items.map { CatanResourceItem.fromEntity(it) }
        )
    }
}

data class CatanResourceItem(
    val resourceName: CatanResource = CatanResource.Wheat,
    val rollNumber: Int = 2
) {
    private val _count: MutableStateFlow<Int> = MutableStateFlow(1)
    val count: StateFlow<Int> = _count

    fun toEntity(): CatanResourceItemEntity {
        return CatanResourceItemEntity(
            resourceName = resourceName.toEntity(),
            rollNumber = rollNumber,
            count = count.value
        )
    }

    companion object {

        fun fromEntity(entity: CatanResourceItemEntity): CatanResourceItem {
            val item = CatanResourceItem(
                resourceName = CatanResource.fromEntity(entity.resourceName),
                rollNumber = entity.rollNumber,
            )
            item._count.value = entity.count
            return item
        }
    }

    fun decrementCount() {
        _count.update { count -> (count - 1).coerceAtLeast(1) }
    }

    fun incremenintCount() {
        _count.update { count -> count + 1 }
    }

    fun getKey() = "$rollNumber-$resourceName"
}

data class Roll(
    val icons: Map<Int, Pair<RollNumberIcon, RollNumberIcon>> =
        mapOf(
            2 to (RollNumberIcon.One to RollNumberIcon.One),
            3 to (RollNumberIcon.Two to RollNumberIcon.One),
            4 to (RollNumberIcon.Two to RollNumberIcon.Two),
            5 to (RollNumberIcon.Three to RollNumberIcon.Two),
            6 to (RollNumberIcon.Three to RollNumberIcon.Three),
            7 to (RollNumberIcon.Four to RollNumberIcon.Three),
            8 to (RollNumberIcon.Four to RollNumberIcon.Four),
            9 to (RollNumberIcon.Five to RollNumberIcon.Four),
            10 to (RollNumberIcon.Five to RollNumberIcon.Five),
            11 to (RollNumberIcon.Six to RollNumberIcon.Five),
            12 to (RollNumberIcon.Six to RollNumberIcon.Six)
        )
)

sealed class RollNumberIcon(val iconId: DrawableResource) {
    data object One : RollNumberIcon(iconId = Res.drawable.dice_six_faces_one)
    data object Two : RollNumberIcon(iconId = Res.drawable.dice_six_faces_two)
    data object Three : RollNumberIcon(iconId = Res.drawable.dice_six_faces_three)
    data object Four : RollNumberIcon(iconId = Res.drawable.dice_six_faces_four)
    data object Five : RollNumberIcon(iconId = Res.drawable.dice_six_faces_five)
    data object Six : RollNumberIcon(iconId = Res.drawable.dice_six_faces_six)
}

sealed class CatanResource {
    data object Wheat : CatanResource()
    data object Brick : CatanResource()
    data object Wood : CatanResource()
    data object Ore : CatanResource()
    data object Sheep : CatanResource()

    fun toEntity(): CatanResourceTypeEntity {
        return when (this) {
            Wheat -> CatanResourceTypeEntity.Wheat
            Brick -> CatanResourceTypeEntity.Brick
            Wood -> CatanResourceTypeEntity.Wood
            Ore -> CatanResourceTypeEntity.Ore
            Sheep -> CatanResourceTypeEntity.Sheep
        }
    }

    companion object {
        fun toList() = listOf(Wheat, Brick, Wood, Ore, Sheep)

        fun fromEntity(entity: CatanResourceTypeEntity): CatanResource {
            return when (entity) {
                CatanResourceTypeEntity.Wheat -> Wheat
                CatanResourceTypeEntity.Brick -> Brick
                CatanResourceTypeEntity.Wood -> Wood
                CatanResourceTypeEntity.Ore -> Ore
                CatanResourceTypeEntity.Sheep -> Sheep
            }
        }


    }


}

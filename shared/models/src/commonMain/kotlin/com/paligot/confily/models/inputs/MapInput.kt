package com.paligot.confily.models.inputs

import com.paligot.confily.models.MapPictogram
import com.paligot.confily.models.MapShape
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MapInput(
    val name: String,
    val order: Int,
    val color: String,
    @SerialName("color_selected")
    val colorSelected: String,
    @SerialName("picto_size")
    val pictoSize: Int = 0,
    val shapes: List<MapShape> = emptyList(),
    val pictograms: List<MapPictogram> = emptyList()
) : Validator {
    override fun validate(): List<String> = emptyList()
}

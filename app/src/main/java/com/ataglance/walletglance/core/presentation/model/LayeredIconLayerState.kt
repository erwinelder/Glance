package com.ataglance.walletglance.core.presentation.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Immutable
data class LayeredIconLayerState(
    val tintColor: Color,
    val offset: Dp
) {

    companion object {

        fun asLayers(
            baseColor: Color,
            layerCount: Int,
            layerOffsetRatio: Double,
            colorStep: Float
        ): List<LayeredIconLayerState> {
            return IntRange(0, layerCount)
                .map { int ->
                    val colorDelta = colorStep * int
                    LayeredIconLayerState(
                        tintColor = Color(
                            red = (baseColor.red + colorDelta).coerceIn(0f, 1f),
                            green = (baseColor.green + colorDelta).coerceIn(0f, 1f),
                            blue = (baseColor.blue + colorDelta).coerceIn(0f, 1f)
                        ),
                        offset = (int * layerOffsetRatio).dp
                    )
                }
                .reversed()
        }

    }

}
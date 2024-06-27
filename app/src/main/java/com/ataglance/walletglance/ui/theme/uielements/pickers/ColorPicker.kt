package com.ataglance.walletglance.ui.theme.uielements.pickers

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.data.color.ColorWithName
import com.ataglance.walletglance.ui.theme.GlanceTheme
import com.ataglance.walletglance.ui.theme.animation.bounceClickEffect

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun ColorPicker(
    visible: Boolean,
    colorList: List<ColorWithName>,
    onColorClick: (String) -> Unit,
    onPickerClose: () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(300)),
        exit = fadeOut(tween(300))
    ) {
        Box(
            modifier = Modifier
                .clickable { onPickerClose() }
                .fillMaxSize()
                .background(Color.Black.copy(.2f))
        )
    }
    AnimatedVisibility(
        visible = visible,
        enter = scaleIn(
            initialScale = .5f
        ) + slideInHorizontally { (it * 1.25).toInt() },
        exit = scaleOut(
            targetScale = .5f
        ) + slideOutHorizontally { (it * 1.25).toInt() },
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .clip(RoundedCornerShape(dimensionResource(R.dimen.record_corner_size)))
                .background(GlanceTheme.background)
                .fillMaxWidth(.8f)
                .padding(12.dp)
        ) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                colorList.forEach { colorWithName ->
                    Spacer(
                        modifier = Modifier
                            .bounceClickEffect(.97f) {
                                onColorClick(colorWithName.name)
                                onPickerClose()
                            }
                            .clip(RoundedCornerShape(dimensionResource(R.dimen.field_corners)))
                            .fillMaxWidth(.22f)
                            .aspectRatio(1.4f, false)
                            .background(colorWithName.color)
                    )
                }
            }
        }
    }
}
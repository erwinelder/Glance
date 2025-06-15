package com.ataglance.walletglance.core.presentation.component.icon

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices.PIXEL_7_PRO
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.component.button.SmallPrimaryButton
import com.ataglance.walletglance.core.presentation.component.screenContainer.PreviewContainer
import com.ataglance.walletglance.core.presentation.model.TransformedLargeIconState
import com.ataglance.walletglance.core.presentation.theme.CurrAppTheme
import com.ataglance.walletglance.core.presentation.theme.GlanciColors

@Composable
fun LayeredIcon(
    @DrawableRes iconRes: Int,
    visible: Boolean = true,
    offset: Dp = 0.dp
) {
    val appTheme = CurrAppTheme
    val baseColor = GlanciColors.layeredIconBaseColor
    val statesWithLastOffset by remember {
        derivedStateOf {
            val colorStep = when (appTheme) {
                AppTheme.LightDefault -> 0.0313f
                AppTheme.DarkDefault -> -0.0313f
            }
            val states = TransformedLargeIconState.asLayers(
                baseColor = baseColor,
                layerCount = 30,
                layerOffsetRatio = 1.3,
                colorStep = colorStep
            )
            states to states.first().offset * 1.55f
        }
    }

    Box(
        contentAlignment = Alignment.TopCenter,
        modifier = Modifier
            .offset(y = offset)
            .padding(start = 22.dp, end = 22.dp, top = 2.dp)
    ) {
        LayeredIconPlatform(lastLayerOffset = statesWithLastOffset.second, visible = visible)
        Box {
            statesWithLastOffset.first.forEach { state ->
                LayeredIconLayer(iconRes = iconRes, state = state, visible = visible)
            }
        }
    }
}

@Composable
private fun LayeredIconLayer(
    @DrawableRes iconRes: Int,
    state: TransformedLargeIconState,
    visible: Boolean = true
) {
    val offset by animateDpAsState(
        targetValue = if (visible) state.offset else 0.dp
    )
    val color by animateColorAsState(
        targetValue = state.tintColor.copy(alpha = if (visible) 1f else 0f)
    )

    Icon(
        painter = painterResource(iconRes),
        contentDescription = "layered icon",
        tint = color,
        modifier = Modifier
            .offset(y = offset)
            .width(130.dp)
    )
}

@Composable
private fun LayeredIconPlatform(
    lastLayerOffset: Dp = 0.dp,
    visible: Boolean = true
) {
    val color by animateColorAsState(
        targetValue = GlanciColors.layeredIconPlatformColor.copy(alpha = if (visible) 1f else 0f)
    )
    val offset by animateDpAsState(
        targetValue = if (visible) 0.dp else -lastLayerOffset
    )

    Box(
        modifier = Modifier
            .scale(scaleX = 1f, scaleY = .65f)
            .offset(y = 3.dp)
            .offset(y = offset)
            .rotate(45f)
            .clip(RoundedCornerShape(24))
            .size(170.dp)
            .background(color)
    )
}



@Preview(device = PIXEL_7_PRO)
@Composable
private fun TransformedLargeIconPreviewLightDefault(
    appTheme: AppTheme = AppTheme.LightDefault
) {
    var applyOffset by remember {
        mutableStateOf(true)
    }

    PreviewContainer(appTheme = appTheme) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LayeredIcon(
                iconRes = R.drawable.language_layer_icon,
                visible = applyOffset
            )
            SmallPrimaryButton(text = "Animate") { applyOffset = !applyOffset }
        }
    }
}

@Preview(device = PIXEL_7_PRO)
@Composable
private fun TransformedLargeIconPreviewDarkDefault(
    appTheme: AppTheme = AppTheme.DarkDefault
) {
    var applyOffset by remember {
        mutableStateOf(true)
    }

    PreviewContainer(appTheme = appTheme) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LayeredIcon(
                iconRes = R.drawable.language_layer_icon,
                visible = applyOffset
            )
            SmallPrimaryButton(text = "Animate") { applyOffset = !applyOffset }
        }
    }
}
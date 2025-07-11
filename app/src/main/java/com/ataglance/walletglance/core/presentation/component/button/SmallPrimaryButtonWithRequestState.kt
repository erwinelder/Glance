package com.ataglance.walletglance.core.presentation.component.button

import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.modifier.bounceClickEffect
import com.ataglance.walletglance.core.presentation.preview.PreviewContainer
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.core.presentation.theme.Manrope
import com.ataglance.walletglance.request.presentation.model.RequestErrorState
import com.ataglance.walletglance.request.presentation.model.ResultState.MessageState

@Composable
fun SmallPrimaryButtonWithRequestState(
    text: String,
    requestErrorState: RequestErrorState<MessageState>?,
    @DrawableRes iconRes: Int? = null,
    enabled: Boolean = true,
    enabledGradient: Pair<Color, Color> = GlanciColors.primaryGlassGradientPair,
    fontSize: TextUnit = 17.sp,
    onClick: () -> Unit
) {
    val lighterGradientColor by animateColorAsState(
        targetValue = if (enabled && requestErrorState == null) enabledGradient.first else
            GlanciColors.disabledGlassGradientPair.first
    )
    val darkerGradientColor by animateColorAsState(
        targetValue = if (enabled && requestErrorState == null) enabledGradient.second else
            GlanciColors.disabledGlassGradientPair.second
    )

    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = GlanciColors.onPrimary,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = GlanciColors.onPrimary
        ),
        contentPadding = PaddingValues(horizontal = 24.dp, vertical = 12.dp),
        modifier = Modifier
            .bounceClickEffect(.98f, enabled = enabled)
            .clip(RoundedCornerShape(20.dp))
            .background(
                brush = Brush.linearGradient(
                    colors = listOf(darkerGradientColor, lighterGradientColor),
                    start = Offset(75f, 200f),
                    end = Offset(100f, 0f)
                )
            )
    ) {
        AnimatedContent(
            targetState = requestErrorState
        ) { state ->
            when (state) {
                null -> {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        iconRes?.let {
                            Icon(
                                painter = painterResource(it),
                                contentDescription = "$text button icon",
                                tint = GlanciColors.onPrimary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                        }
                        Text(
                            text = text,
                            fontSize = fontSize,
                            fontFamily = Manrope
                        )
                    }
                }
                is RequestErrorState.Loading -> {
                    Text(
                        text = stringResource(state.messageRes),
                        fontSize = fontSize,
                        fontFamily = Manrope
                    )
                }
                is RequestErrorState.Error -> {
                    Text(
                        text = stringResource(state.state.messageRes),
                        fontSize = fontSize,
                        fontFamily = Manrope
                    )
                }
            }
        }
    }
}

@Preview(device = Devices.PIXEL_7_PRO)
@Composable
private fun PreviewPrimarySmallButton() {
    val requestState = null

    PreviewContainer(appTheme = AppTheme.LightDefault) {
        SmallPrimaryButtonWithRequestState(
            text = "ApplyApplyApply",
            requestErrorState = requestState,
            iconRes = R.drawable.close_icon,
            onClick = {}
        )
    }
}
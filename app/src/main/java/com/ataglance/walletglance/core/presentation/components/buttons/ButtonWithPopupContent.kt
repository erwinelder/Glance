package com.ataglance.walletglance.core.presentation.components.buttons

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.GlanceTheme
import com.ataglance.walletglance.core.presentation.animation.scaleFadeInAnimation
import com.ataglance.walletglance.core.presentation.animation.scaleFadeOutAnimation
import com.ataglance.walletglance.core.presentation.components.containers.PreviewContainer
import com.ataglance.walletglance.core.presentation.modifiers.bounceClickEffect
import com.ataglance.walletglance.core.presentation.modifiers.innerVolumeShadow

@Composable
fun ButtonWithPopupContent(
    buttonText: String,
    animationTransformOrigin: TransformOrigin = TransformOrigin(0.5f, 0.5f),
    contentPadding: PaddingValues = PaddingValues(horizontal = 24.dp, vertical = 16.dp),
    popupContent: @Composable (() -> Unit) -> Unit
) {
    val isExpandedState = remember { MutableTransitionState(false) }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {

        Button(
            modifier = Modifier
                .bounceClickEffect()
                .clip(RoundedCornerShape(36))
                .background(
                    brush = Brush.linearGradient(
                        colors = GlanceTheme.glassGradientLightToDark.toList().reversed(),
                        start = Offset(75f, 210f),
                        end = Offset(95f, -10f)
                    )
                )
                .border(
                    width = 1.dp,
                    color = GlanceTheme.onGlassSurfaceBorder,
                    shape = RoundedCornerShape(36)
                ),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = GlanceTheme.onSurface,
            ),
            onClick = {
                isExpandedState.targetState = true
            }
        ) {
            Text(
                text = buttonText,
                color = GlanceTheme.onSurface,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
            Spacer(modifier = Modifier.width(8.dp))
            Icon(
                painter = painterResource(R.drawable.see_other_icon),
                contentDescription = "see other icon",
                tint = GlanceTheme.onSurface,
                modifier = Modifier.size(24.dp)
            )
        }

        Column {
            if (isExpandedState.targetState || isExpandedState.currentState) {
                Popup(
                    alignment = Alignment.TopCenter,
                    onDismissRequest = {
                        isExpandedState.targetState = false
                    },
                    properties = PopupProperties(
                        focusable = true
                    )
                ) {
                    AnimatedVisibility(
                        visibleState = isExpandedState,
                        enter = scaleFadeInAnimation(animationTransformOrigin),
                        exit = scaleFadeOutAnimation(animationTransformOrigin)
                    ) {
                        Box(
                            modifier = Modifier
                                .padding(horizontal = 8.dp, vertical = 8.dp)
                                .innerVolumeShadow(shape = RoundedCornerShape(26.dp))
                                .clip(RoundedCornerShape(26.dp))
                                .background(GlanceTheme.surface)
                                .padding(contentPadding)
                        ) {
                            popupContent {
                                isExpandedState.targetState = false
                            }
                        }
                    }
                }
            }
        }

    }
}



@Preview
@Composable
private fun Preview() {
    PreviewContainer(appTheme = AppTheme.LightDefault) {
        ButtonWithPopupContent(
            buttonText = stringResource(R.string.preferences),
            animationTransformOrigin = TransformOrigin(.7f, 0f)
        ) {

        }
    }
}

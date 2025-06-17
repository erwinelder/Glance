package com.ataglance.walletglance.core.presentation.component.button

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType

@Composable
fun GlassSurfaceTopNavButton(
    text: String,
    @DrawableRes imageRes: Int? = null,
    filledWidths: FilledWidthByScreenType = FilledWidthByScreenType(1f, .75f, .75f),
    onClick: () -> Unit
) {
    GlassSurfaceNavigationButton(
        text = text,
        imageRes = imageRes,
        showRightIconInsteadOfLeft = false,
        filledWidths = filledWidths,
        onClick = onClick
    )
}
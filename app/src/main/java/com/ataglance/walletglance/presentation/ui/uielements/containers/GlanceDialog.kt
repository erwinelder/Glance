package com.ataglance.walletglance.presentation.ui.uielements.containers

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.presentation.ui.GlanceTheme
import com.ataglance.walletglance.presentation.ui.animation.dialogSlideFromBottomTransition
import com.ataglance.walletglance.presentation.ui.animation.dialogSlideToBottomTransition

@Composable
fun GlanceDialog(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(400)),
        exit = fadeOut(tween(400))
    ) {
        Box(
            modifier = Modifier
                .clickable { onDismissRequest() }
                .fillMaxSize()
                .background(Color.Black.copy(.3f))
        )
    }
    AnimatedVisibility(
        visible = visible,
        enter = dialogSlideFromBottomTransition,
        exit = dialogSlideToBottomTransition
    ) {
        Box(
            modifier = Modifier
                .padding(top = 84.dp, bottom = dimensionResource(R.dimen.screen_vertical_padding))
                .fillMaxWidth(.9f)
                .clip(RoundedCornerShape(dimensionResource(R.dimen.dialog_corner_size)))
                .background(GlanceTheme.surfaceVariant.copy(1f))
        ) {
            content()
        }
    }
}
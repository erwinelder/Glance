package com.ataglance.walletglance.ui.theme.uielements.containers

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.ataglance.walletglance.R

@Composable
fun DialogWindow(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    content: @Composable BoxScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(
            tween(400)
        ) + scaleIn(
            tween(400), .5f
        ),
        exit = fadeOut(
            tween(400)
        ) + scaleOut(
            tween(400), 0f
        )
    ) {
        Dialog(
            onDismissRequest = onDismissRequest,
            properties = DialogProperties(
                dismissOnClickOutside = true,
                usePlatformDefaultWidth = false
            )
        ) {
            Box(
                contentAlignment = Alignment.BottomCenter,
                modifier = Modifier
                    .fillMaxWidth(.88f)
                    .fillMaxHeight()
                    .padding(
                        top = 70.dp,
                        bottom = dimensionResource(R.dimen.screen_vertical_padding)
                    )
            ) {
                BackButtonArea(onDismissRequest)
                content()
            }
        }
    }
}

@Composable
private fun BackButtonArea(onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .offset(y = -(70).dp)
    ) {
        Spacer(
            modifier = Modifier
                .clickable { onClick() }
                .fillMaxWidth()
                .height(70.dp)
                .weight(1f)
        )
    }
}
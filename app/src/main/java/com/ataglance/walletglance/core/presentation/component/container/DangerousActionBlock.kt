package com.ataglance.walletglance.core.presentation.component.container

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.presentation.theme.GlanceColors
import com.ataglance.walletglance.core.presentation.component.button.SmallPrimaryDangerousButton
import com.ataglance.walletglance.core.presentation.component.screenContainers.ScreenContainerWithBackButton
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun DangerousActionBlock(
    actionText: String,
    actionConfirmationText: String,
    actionButtonText: String,
    onAction: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var showActionConfirmation by remember { mutableStateOf(false) }
    val job = remember { mutableStateOf<Job?>(null) }

    ScreenContainerWithBackButton {
        GlassSurface {
            DangerousActionBlockContent(
                showConfirmation = showActionConfirmation,
                actionText = actionText,
                actionConfirmationText = actionConfirmationText,
                actionButtonText = actionButtonText,
                onActionButton = {
                    job.value?.cancel()

                    showActionConfirmation = true

                    job.value = coroutineScope.launch {
                        delay(5000)
                        showActionConfirmation = false
                    }
                },
                onActionConfirm = onAction,
                onActionCancel = {
                    job.value?.cancel()
                    showActionConfirmation = false
                }
            )
        }
    }
}

@Composable
private fun DangerousActionBlockContent(
    showConfirmation: Boolean,
    actionText: String,
    actionConfirmationText: String,
    actionButtonText: String,
    onActionButton: () -> Unit,
    onActionConfirm: () -> Unit,
    onActionCancel: () -> Unit
) {
    GlassSurfaceContentColumnWrapper {
        AnimatedContent(
            targetState = if (showConfirmation) actionConfirmationText else actionText,
            label = "action text"
        ) { targetMessage ->
            Text(
                text = targetMessage,
                color = GlanceColors.onSurface,
                fontSize = 18.sp,
                textAlign = TextAlign.Center
            )
        }
        AnimatedContent(
            targetState = showConfirmation,
            label = "action buttons"
        ) { targetShowConfirmation ->
            if (targetShowConfirmation) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    SmallPrimaryDangerousButton(
                        text = stringResource(R.string.yes),
                        onClick = onActionConfirm
                    )
                    SmallPrimaryDangerousButton(
                        text = stringResource(R.string.no),
                        onClick = onActionCancel
                    )
                }
            } else {
                SmallPrimaryDangerousButton(
                    text = actionButtonText,
                    onClick = onActionButton
                )
            }
        }
    }
}
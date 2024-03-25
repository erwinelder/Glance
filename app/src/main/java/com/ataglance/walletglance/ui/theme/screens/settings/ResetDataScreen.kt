package com.ataglance.walletglance.ui.theme.screens.settings

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.ui.theme.GlanceTheme
import com.ataglance.walletglance.ui.theme.uielements.buttons.SmallPrimaryButton
import com.ataglance.walletglance.ui.theme.uielements.containers.GlassSurface
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SettingsDataScreen(
    onResetData: () -> Unit,
    onExportData: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    val backgroundRedColorStep = remember { mutableIntStateOf(0) }
    val fillPercent by animateFloatAsState(
        targetValue = when (backgroundRedColorStep.intValue) {
            0 -> 0f
                1 -> .25f
                2 -> .5f
                3 -> .75f
                else -> 1f
        },
        animationSpec = spring(dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessMediumLow),
        label = "resetting progress bar"
    )
    val job: MutableState<Job?> = remember { mutableStateOf(null) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        ResetDataBlock(fillPercent) {
            job.value?.cancel()

            backgroundRedColorStep.intValue ++
            if (backgroundRedColorStep.intValue > 3) {
                onResetData()
            }

            job.value = coroutineScope.launch {
                delay(1200)
                backgroundRedColorStep.intValue = 0
            }
        }
//        Spacer(modifier = Modifier.weight(1f))
    }
}

@Composable
private fun ResetDataBlock(fillPercent: Float, onResetData: () -> Unit) {
    GlassSurface {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp, vertical = 18.dp)
        ) {
            AnimatedVisibility(visible = fillPercent > 0) {
                ProgressBar(progress = fillPercent)
            }
            Text(
                text = stringResource(R.string.reset_data_description),
                fontSize = 17.sp,
                color = GlanceTheme.onSurface,
                textAlign = TextAlign.Center
            )
            SmallPrimaryButton(
                text = stringResource(R.string.reset_data),
                enabledGradientColor = GlanceTheme.errorGradientLightToDark,
                onClick = onResetData,
            )
        }
    }
}

@Composable
private fun ProgressBar(progress: Float) {
    val firstGradientColor by animateColorAsState(
        targetValue = GlanceTheme.error,
        label = "resetting progress bar color"
    )
    val secondGradientColor = Color(
        red = firstGradientColor.red.minus(.2f),
        green = firstGradientColor.green.minus(.2f),
        blue = firstGradientColor.blue.minus(.2f)
    )

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(50))
            .fillMaxWidth()
            .background(GlanceTheme.onSurfaceVariant)
    ) {
        Box(
            modifier = Modifier
                .clip(RoundedCornerShape(50))
                .background(
                    brush = Brush.horizontalGradient(
                        listOf(secondGradientColor, firstGradientColor)
                    )
                )
                .fillMaxWidth(progress)
                .height(16.dp)
        )
    }
}

@Composable
private fun ExportDataBlock(onExportData: () -> Unit) {
    GlassSurface(filledWidth = 1f) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp, vertical = 18.dp)
        ) {
            Text(
                text = stringResource(R.string.export_data_description),
                fontSize = 17.sp,
                textAlign = TextAlign.Center
            )
            SmallPrimaryButton(
                text = stringResource(R.string.export_data),
                onClick = onExportData,
            )
        }
    }
}

package com.ataglance.walletglance.ui.theme.screens.settings

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    var resetStep by remember { mutableIntStateOf(0) }
    val job: MutableState<Job?> = remember { mutableStateOf(null) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 24.dp)
    ) {
        ResetDataBlock(
            showPrompt = resetStep == 1,
            onResetDataConfirm = onResetData,
            onResetDataCancel = {
                job.value?.cancel()
                resetStep = 0
            }
        ) {
            job.value?.cancel()

            resetStep ++

            job.value = coroutineScope.launch {
                delay(5000)
                resetStep = 0
            }
        }
    }
}

@Composable
private fun ResetDataBlock(
    showPrompt: Boolean,
    onResetDataConfirm: () -> Unit,
    onResetDataCancel: () -> Unit,
    onResetDataButton: () -> Unit,
) {
    GlassSurface(filledWidth = 1f) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 22.dp, vertical = 18.dp)
        ) {
            AnimatedContent(
                targetState = showPrompt,
                label = "reset data description"
            ) { targetShowPrompt ->
                if (targetShowPrompt) {
                    Text(
                        text = stringResource(R.string.reset_data_warning),
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                } else {
                    Text(
                        text = stringResource(R.string.reset_data_description),
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
            AnimatedContent(
                targetState = showPrompt,
                label = "reset data buttons"
            ) { targetShowPrompt ->
                if (targetShowPrompt) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        SmallPrimaryButton(
                            text = stringResource(R.string.yes),
                            enabledGradientColor = GlanceTheme.errorGradientLightToDark,
                            onClick = onResetDataConfirm,
                        )
                        SmallPrimaryButton(
                            text = stringResource(R.string.no),
                            enabledGradientColor = GlanceTheme.errorGradientLightToDark,
                            onClick = onResetDataCancel,
                        )
                    }
                } else {
                    SmallPrimaryButton(
                        text = stringResource(R.string.reset_data),
                        enabledGradientColor = GlanceTheme.errorGradientLightToDark,
                        onClick = onResetDataButton,
                    )
                }
            }
        }
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

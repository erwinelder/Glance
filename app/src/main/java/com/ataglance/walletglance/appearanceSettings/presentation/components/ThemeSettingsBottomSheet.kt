package com.ataglance.walletglance.appearanceSettings.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.ataglance.walletglance.core.presentation.GlanceTheme
import com.ataglance.walletglance.core.presentation.components.containers.GlanceBottomSheet
import com.ataglance.walletglance.settings.domain.ThemeUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeSettingsBottomSheet(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    themeUiState: ThemeUiState,
    onSetUseDeviceTheme: (Boolean) -> Unit,
    onChooseLightTheme: (String) -> Unit,
    onChooseDarkTheme: (String) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val backgroundColor by animateColorAsState(
        targetValue = GlanceTheme.background,
        label = "background color"
    )

    GlanceBottomSheet(
        visible = visible,
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        backgroundColor = backgroundColor
    ) {
        ThemePickerContent(
            onChooseLightTheme = onChooseLightTheme,
            onChooseDarkTheme = onChooseDarkTheme,
            onSetUseDeviceTheme = onSetUseDeviceTheme,
            themeUiState = themeUiState
        )
    }
}
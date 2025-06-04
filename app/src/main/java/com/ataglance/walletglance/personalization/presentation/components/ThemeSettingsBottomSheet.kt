package com.ataglance.walletglance.personalization.presentation.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.component.bottomSheet.BottomSheetComponent
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.settings.domain.model.AppThemeConfiguration

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeSettingsBottomSheet(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    appThemeConfiguration: AppThemeConfiguration,
    onChooseLightTheme: (AppTheme) -> Unit,
    onChooseDarkTheme: (AppTheme) -> Unit,
    onSetUseDeviceTheme: (Boolean) -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val backgroundColor by animateColorAsState(
        targetValue = GlanciColors.background
    )

    BottomSheetComponent(
        visible = visible,
        sheetState = sheetState,
        onDismissRequest = onDismissRequest,
        backgroundColor = backgroundColor
    ) {
        ThemePickerContent(
            onChooseLightTheme = onChooseLightTheme,
            onChooseDarkTheme = onChooseDarkTheme,
            onSetUseDeviceTheme = onSetUseDeviceTheme,
            appThemeConfiguration = appThemeConfiguration
        )
    }
}
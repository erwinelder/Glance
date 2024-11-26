package com.ataglance.walletglance.settings.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppLanguage
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.presentation.CurrAppTheme
import com.ataglance.walletglance.core.presentation.WindowTypeIsCompact
import com.ataglance.walletglance.core.presentation.components.buttons.GlassSurfaceNavigationButton
import com.ataglance.walletglance.core.presentation.components.buttons.PrimaryButton
import com.ataglance.walletglance.core.presentation.components.buttons.SmallPrimaryButton
import com.ataglance.walletglance.core.presentation.components.screenContainers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.settings.domain.SettingsCategories
import com.ataglance.walletglance.settings.presentation.components.LanguagePicker

@Composable
fun LanguageScreen(
    isAppSetUp: Boolean,
    onNavigateBack: () -> Unit,
    appLanguage: String,
    chosenLanguage: String?,
    onSelectNewLanguage: (String) -> Unit,
    onApplyLanguageButton: (String) -> Unit,
    onContinueButton: () -> Unit
) {
    val settingsCategory = SettingsCategories(CurrAppTheme).language

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = 8.dp,
                bottom = dimensionResource(R.dimen.screen_vertical_padding)
            )
    ) {
        if (isAppSetUp && WindowTypeIsCompact) {
            GlassSurfaceNavigationButton(
                text = stringResource(settingsCategory.stringRes),
                imageRes = settingsCategory.iconRes,
                showRightIconInsteadOfLeft = false,
                filledWidths = FilledWidthByScreenType(.96f),
                onClick = onNavigateBack
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        LanguagePicker(
            currentLangCode = chosenLanguage,
            onRadioButton = { langCode ->
                onSelectNewLanguage(langCode)
            }
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.buttons_gap)))
        SmallPrimaryButton(
            text = stringResource(R.string.apply),
            enabled = appLanguage != chosenLanguage
        ) {
            chosenLanguage?.let { onApplyLanguageButton(it) }
        }
        Spacer(modifier = Modifier.weight(1f))
        if (!isAppSetUp) {
            PrimaryButton(
                onClick = onContinueButton,
                text = stringResource(R.string._continue)
            )
        }
    }
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun LanguageScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault,
    isAppSetUp: Boolean = true,
    isSetupProgressTopBarVisible: Boolean = false,
    isBottomBarVisible: Boolean = false,
    appLanguage: String = AppLanguage.English.languageCode,
    selectedLanguage: String? = AppLanguage.German.languageCode,
) {
    PreviewWithMainScaffoldContainer(
        appTheme = appTheme,
        isSetupProgressTopBarVisible = isSetupProgressTopBarVisible,
        isBottomBarVisible = isBottomBarVisible
    ) {
        LanguageScreen(
            isAppSetUp = isAppSetUp,
            onNavigateBack = {},
            appLanguage = appLanguage,
            chosenLanguage = selectedLanguage,
            onSelectNewLanguage = {},
            onApplyLanguageButton = {},
            onContinueButton = {}
        )
    }
}

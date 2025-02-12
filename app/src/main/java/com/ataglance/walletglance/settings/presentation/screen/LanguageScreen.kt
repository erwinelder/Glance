package com.ataglance.walletglance.settings.presentation.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppLanguage
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.theme.CurrAppTheme
import com.ataglance.walletglance.core.presentation.components.buttons.PrimaryButton
import com.ataglance.walletglance.core.presentation.components.buttons.SmallPrimaryButton
import com.ataglance.walletglance.core.presentation.components.screenContainers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.settings.domain.SettingsCategories
import com.ataglance.walletglance.settings.presentation.components.LanguagePicker
import com.ataglance.walletglance.settings.presentation.screenContainers.SettingsCategoryScreenContainer

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
    val appTheme = CurrAppTheme
    val thisCategory = remember {
        SettingsCategories(appTheme).language
    }

    SettingsCategoryScreenContainer(
        thisCategory = thisCategory,
        onNavigateBack = onNavigateBack.takeIf { isAppSetUp },
        title = stringResource(R.string.choose_app_language),
        mainScreenContentBlock = {
            LanguagePicker(
                currentLangCode = chosenLanguage,
                onRadioButton = { langCode ->
                    onSelectNewLanguage(langCode)
                }
            )
            SmallPrimaryButton(
                text = stringResource(R.string.apply),
                enabled = appLanguage != chosenLanguage
            ) {
                chosenLanguage?.let { onApplyLanguageButton(it) }
            }
        },
        allowScroll = false,
        bottomBlock = if (!isAppSetUp) {{
            PrimaryButton(
                onClick = onContinueButton,
                text = stringResource(R.string._continue)
            )
        }} else null
    )
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun LanguageScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault,
    isAppSetUp: Boolean = false,
    isBottomBarVisible: Boolean = false,
    appLanguage: String = AppLanguage.English.languageCode,
    selectedLanguage: String? = AppLanguage.German.languageCode,
) {
    PreviewWithMainScaffoldContainer(
        appTheme = appTheme,
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

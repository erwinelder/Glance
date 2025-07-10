package com.ataglance.walletglance.settings.presentation.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppConfiguration
import com.ataglance.walletglance.core.domain.app.AppLanguage
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.component.button.PrimaryButton
import com.ataglance.walletglance.core.presentation.component.button.SmallPrimaryButtonWithRequestState
import com.ataglance.walletglance.core.presentation.preview.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.theme.CurrAppTheme
import com.ataglance.walletglance.core.utils.takeComposableIf
import com.ataglance.walletglance.request.presentation.model.RequestState
import com.ataglance.walletglance.request.presentation.model.ResultState.MessageState
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.settings.domain.navigation.SettingsScreens
import com.ataglance.walletglance.settings.presentation.component.LanguagePicker
import com.ataglance.walletglance.settings.presentation.model.SettingsCategory
import com.ataglance.walletglance.settings.presentation.screenContainer.SettingsCategoryScreenContainer
import com.ataglance.walletglance.settings.presentation.viewmodel.LanguageViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun LanguageScreenWrapper(
    screenPadding: PaddingValues = PaddingValues(),
    navController: NavController,
    navViewModel: NavigationViewModel,
    appConfiguration: AppConfiguration
) {
    val viewModel = koinViewModel<LanguageViewModel> {
        parametersOf(appConfiguration.langCode)
    }

    val chosenLanguage by viewModel.langCode.collectAsStateWithLifecycle()
    val requestState by viewModel.requestState.collectAsStateWithLifecycle()

    LanguageScreen(
        screenPadding = screenPadding,
        isAppSetUp = appConfiguration.isSetUp,
        onNavigateBack = navController::popBackStack,
        appLanguage = appConfiguration.langCode,
        chosenLanguage = chosenLanguage,
        onSelectLangCode = viewModel::selectLanguage,
        onApplyLanguage = viewModel::applyLanguage,
        onContinueButton = {
            navViewModel.navigateToScreen(navController, SettingsScreens.Personalisation)
        },
        requestState = requestState
    )
}

@Composable
fun LanguageScreen(
    screenPadding: PaddingValues = PaddingValues(),
    isAppSetUp: Boolean,
    onNavigateBack: () -> Unit,
    appLanguage: String,
    chosenLanguage: String?,
    onSelectLangCode: (String) -> Unit,
    onApplyLanguage: () -> Unit,
    onContinueButton: () -> Unit,
    requestState: RequestState<MessageState>?
) {
    val appTheme = CurrAppTheme
    val thisCategory = remember {
        SettingsCategory.Language(appTheme)
    }

    SettingsCategoryScreenContainer(
        screenPadding = screenPadding,
        thisCategory = thisCategory,
        onNavigateBack = onNavigateBack.takeIf { isAppSetUp },
        title = stringResource(R.string.choose_app_language),
        mainScreenContent = {
            LanguagePicker(
                currentLangCode = chosenLanguage,
                onLangCodeSelect = onSelectLangCode
            )
            SmallPrimaryButtonWithRequestState(
                text = stringResource(R.string.apply),
                requestState = requestState,
                enabled = appLanguage != chosenLanguage,
                onClick = onApplyLanguage
            )
        },
        allowScroll = false,
        bottomBlock = takeComposableIf(!isAppSetUp) {
            PrimaryButton(
                onClick = onContinueButton,
                text = stringResource(R.string._continue)
            )
        }
    )
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun LanguageScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault,
    isAppSetUp: Boolean = true,
    appLanguage: String = AppLanguage.English.languageCode,
    selectedLanguage: String? = AppLanguage.German.languageCode,
) {
    PreviewWithMainScaffoldContainer(appTheme = appTheme) {
        LanguageScreen(
            isAppSetUp = isAppSetUp,
            onNavigateBack = {},
            appLanguage = appLanguage,
            chosenLanguage = selectedLanguage,
            onSelectLangCode = {},
            onApplyLanguage = {},
            onContinueButton = {},
            requestState = null
        )
    }
}

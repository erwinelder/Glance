package com.ataglance.walletglance.settings.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppConfiguration
import com.ataglance.walletglance.core.domain.app.AppLanguage
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.component.button.PrimaryButton
import com.ataglance.walletglance.core.presentation.component.button.SmallPrimaryButton
import com.ataglance.walletglance.core.presentation.model.IconPathsRes
import com.ataglance.walletglance.core.presentation.preview.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.utils.takeComposableIf
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.request.presentation.component.screenContainer.AnimatedRequestScreenContainerWithTopNavBackButton
import com.ataglance.walletglance.request.presentation.model.RequestErrorState
import com.ataglance.walletglance.request.presentation.model.ResultState.ButtonState
import com.ataglance.walletglance.settings.error.SettingsError
import com.ataglance.walletglance.settings.domain.navigation.SettingsScreens
import com.ataglance.walletglance.settings.mapper.toResultStateButton
import com.ataglance.walletglance.settings.presentation.component.LanguagePicker
import com.ataglance.walletglance.settings.presentation.viewmodel.LanguageViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
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
        onNavigateBack = navController::popBackStack,
        isAppSetUp = appConfiguration.isSetUp,
        appLanguage = appConfiguration.langCode,
        chosenLanguage = chosenLanguage,
        onSelectLangCode = viewModel::selectLanguage,
        onApplyLanguage = viewModel::applyLanguage,
        onContinueButton = {
            navViewModel.navigateToScreen(navController, SettingsScreens.Personalization)
        },
        requestState = requestState,
        onErrorButton = viewModel::resetRequestState
    )
}

@Composable
fun LanguageScreen(
    screenPadding: PaddingValues = PaddingValues(),
    onNavigateBack: () -> Unit,
    isAppSetUp: Boolean,
    appLanguage: String,
    chosenLanguage: String?,
    onSelectLangCode: (String) -> Unit,
    onApplyLanguage: () -> Unit,
    onContinueButton: () -> Unit,
    requestState: RequestErrorState<ButtonState>?,
    onErrorButton: () -> Unit
) {
    AnimatedRequestScreenContainerWithTopNavBackButton(
        screenPadding = screenPadding,
        iconPathsRes = IconPathsRes.Language,
        title = stringResource(R.string.choose_app_language),
        requestErrorStateButton = requestState,
        onErrorButton = onErrorButton,
        backButtonText = stringResource(R.string.language),
        onBackButtonClick = onNavigateBack,
        screenCenterContent = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                LanguagePicker(
                    currentLangCode = chosenLanguage,
                    onLangCodeSelect = onSelectLangCode
                )
                SmallPrimaryButton(
                    text = stringResource(R.string.apply),
                    enabled = appLanguage != chosenLanguage,
                    onClick = onApplyLanguage
                )
            }
        },
        screenBottomContent = takeComposableIf(!isAppSetUp) {
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
    val coroutineScope = rememberCoroutineScope()
    val initialRequestState = null
    var requestState by remember {
        mutableStateOf<RequestErrorState<ButtonState>?>(initialRequestState)
    }

    PreviewWithMainScaffoldContainer(appTheme = appTheme) {
        LanguageScreen(
            onNavigateBack = {},
            isAppSetUp = isAppSetUp,
            appLanguage = appLanguage,
            chosenLanguage = selectedLanguage,
            onSelectLangCode = {},
            onApplyLanguage = {
                coroutineScope.launch {
                    requestState = RequestErrorState.Loading(
                        messageRes = R.string.saving_language_loader
                    )
                    delay(2000)
                    requestState = RequestErrorState.Error(
                        state = SettingsError.LanguageNotSavedRemotely.toResultStateButton()
                    )
                }
            },
            onContinueButton = {},
            requestState = requestState,
            onErrorButton = { requestState = initialRequestState }
        )
    }
}

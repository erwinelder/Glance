package com.ataglance.walletglance.presentation.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.presentation.ui.uielements.buttons.PrimaryButton
import com.ataglance.walletglance.presentation.ui.uielements.buttons.SmallPrimaryButton
import com.ataglance.walletglance.presentation.ui.uielements.pickers.LanguagePicker

@Composable
fun SetupLanguageScreen(
    scaffoldPadding: PaddingValues,
    isAppSetUp: Boolean,
    appLanguage: String,
    chosenLanguage: String?,
    chooseNewLanguage: (String) -> Unit,
    onApplyLanguageButton: (String) -> Unit,
    onContinueButton: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(
                bottom = (if (isAppSetUp) scaffoldPadding.calculateBottomPadding() else 0.dp) +
                        dimensionResource(R.dimen.screen_vertical_padding)
            )
    ) {
        Spacer(modifier = Modifier.weight(1f))
        LanguagePicker(
            currentLangCode = chosenLanguage,
            onRadioButton = { langCode ->
                chooseNewLanguage(langCode)
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
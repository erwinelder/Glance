package com.ataglance.walletglance.ui.theme.screens.settings

import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.ui.theme.uielements.buttons.PrimaryButton
import com.ataglance.walletglance.ui.theme.uielements.buttons.SmallPrimaryButton
import com.ataglance.walletglance.ui.theme.uielements.pickers.LanguagePicker

@Composable
fun SetupLanguageScreen(
    scaffoldPadding: PaddingValues,
    isAppSetUp: Boolean,
    appLanguage: String,
    chosenLanguage: String?,
    chooseNewLanguage: (String) -> Unit,
    onApplyButton: (String, Context) -> Unit,
    onContextChange: (Context) -> Unit,
    onNextNavigationButton: () -> Unit
) {
    val context = LocalContext.current
    LaunchedEffect(context) {
        if (isAppSetUp && chosenLanguage != null) {
            onContextChange(context)
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(
                bottom = if (isAppSetUp) scaffoldPadding.calculateBottomPadding() else 0.dp +
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
            chosenLanguage?.let { onApplyButton(it, context) }
        }
        Spacer(modifier = Modifier.weight(1f))
        if (!isAppSetUp) {
            PrimaryButton(
                onClick = onNextNavigationButton,
                text = stringResource(R.string._continue)
            )
        }
    }
}
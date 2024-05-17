package com.ataglance.walletglance.ui.theme.screens.settings.accounts

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.data.Account
import com.ataglance.walletglance.ui.theme.WindowTypeIsExpanded
import com.ataglance.walletglance.ui.theme.theme.AppTheme
import com.ataglance.walletglance.ui.theme.uielements.buttons.PrimaryButton
import com.ataglance.walletglance.ui.theme.uielements.buttons.SecondaryButton
import com.ataglance.walletglance.ui.theme.uielements.containers.AccountsSetupContainer

@Composable
fun SetupAccountsScreen(
    scaffoldPadding: PaddingValues,
    isAppSetUp: Boolean,
    accountsList: List<Account>,
    appTheme: AppTheme?,
    navigateToEditAccountScreen: (Int) -> Unit,
    swapAccounts: (Int, Int) -> Unit,
    onAddNewAccount: () -> Unit,
    onResetButton: () -> Unit,
    onSaveButton: (List<Account>) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = if (isAppSetUp) {
                    0.dp
                } else {
                    scaffoldPadding.calculateTopPadding()
                } +
                        dimensionResource(R.dimen.screen_vertical_padding),
                bottom = dimensionResource(R.dimen.screen_vertical_padding)
            )
    ) {
        AccountsSetupContainer(
            accountsList = accountsList,
            appTheme = appTheme,
            onAddNewAccount = onAddNewAccount,
            navigateToEditAccountScreen = navigateToEditAccountScreen,
            swapAccounts = swapAccounts
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.widgets_gap)))
        if (!WindowTypeIsExpanded) {
            SecondaryButton(
                onClick = onResetButton,
                text = stringResource(R.string.reset)
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.buttons_gap)))
            PrimaryButton(
                onClick = { onSaveButton(accountsList) },
                text = stringResource(
                    if (isAppSetUp) R.string.save else R.string.save_and_continue
                )
            )
        } else {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                SecondaryButton(
                    onClick = onResetButton,
                    text = stringResource(R.string.reset)
                )
                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.buttons_gap)))
                PrimaryButton(
                    onClick = { onSaveButton(accountsList) },
                    text = stringResource(
                        if (isAppSetUp) R.string.save else R.string.save_and_continue
                    )
                )
            }
        }
    }
}
package com.ataglance.walletglance.account.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.toRoute
import com.ataglance.walletglance.R
import com.ataglance.walletglance.account.domain.navigation.AccountsSettingsScreens
import com.ataglance.walletglance.account.domain.utils.toSortedCurrencyItemList
import com.ataglance.walletglance.account.presentation.model.CurrencyItem
import com.ataglance.walletglance.account.presentation.model.CurrencyPickerUiState
import com.ataglance.walletglance.account.presentation.viewmodel.CurrencyPickerViewModel
import com.ataglance.walletglance.account.presentation.viewmodel.EditAccountViewModel
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.component.container.glassSurface.GlassSurface
import com.ataglance.walletglance.core.presentation.component.field.LargeTextField
import com.ataglance.walletglance.core.presentation.preview.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.component.screenContainer.ScreenContainerWithTopBackNavButtonAndPrimaryButton
import com.ataglance.walletglance.core.presentation.modifier.bounceClickEffect
import com.ataglance.walletglance.core.presentation.theme.CurrAppTheme
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.core.presentation.viewmodel.sharedViewModel
import com.ataglance.walletglance.settings.presentation.model.SettingsCategory
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf
import java.util.Currency

@Composable
fun CurrencyPickerScreenWrapper(
    screenPadding: PaddingValues = PaddingValues(),
    backStack: NavBackStackEntry,
    navController: NavHostController
) {
    val currency = backStack.toRoute<AccountsSettingsScreens.EditAccountCurrency>().currency

    val editAccountViewModel = backStack.sharedViewModel<EditAccountViewModel>(navController)
    val currencyPickerViewModel = koinViewModel<CurrencyPickerViewModel> {
        parametersOf(currency)
    }

    val accountDraft by editAccountViewModel.accountDraft.collectAsStateWithLifecycle()
    val uiState by currencyPickerViewModel.uiState.collectAsStateWithLifecycle()
    val currencyList by currencyPickerViewModel.currencyList.collectAsStateWithLifecycle()
    val searchedPrompt by currencyPickerViewModel.searchedPrompt.collectAsStateWithLifecycle()

    CurrencyPickerScreen(
        screenPadding = screenPadding,
        onNavigateBack = navController::popBackStack,
        accountName = accountDraft.name,
        currencyPickerUiState = uiState,
        currencyList = currencyList,
        searchedPrompt = searchedPrompt,
        onSearchPromptChange = currencyPickerViewModel::changeSearchPrompt,
        onSelectCurrency = currencyPickerViewModel::selectCurrency,
        onSaveButtonClick = { selectedCurrency ->
            editAccountViewModel.changeCurrency(selectedCurrency)
            navController.popBackStack()
        }
    )
}

@Composable
fun CurrencyPickerScreen(
    screenPadding: PaddingValues = PaddingValues(),
    onNavigateBack: () -> Unit,
    accountName: String,
    currencyPickerUiState: CurrencyPickerUiState,
    currencyList: List<CurrencyItem>,
    searchedPrompt: String,
    onSearchPromptChange: (String) -> Unit,
    onSelectCurrency: (CurrencyItem) -> Unit,
    onSaveButtonClick: (String) -> Unit,
) {
    val settingsCategory = SettingsCategory.Accounts(appTheme = CurrAppTheme)
    val backNavButtonText = accountName.takeIf { it.isNotBlank() }
        ?: stringResource(R.string.currency)

    ScreenContainerWithTopBackNavButtonAndPrimaryButton(
        screenPadding = screenPadding,
        backNavButtonText = backNavButtonText,
        backNavButtonImageRes = settingsCategory.iconRes,
        onBackNavButtonClick = onNavigateBack,
        primaryButtonText = stringResource(R.string.save),
        primaryButtonEnabled = currencyPickerUiState.selectedCurrency != null,
        onPrimaryButtonClick = {
            currencyPickerUiState.selectedCurrency?.currencyCode?.let(onSaveButtonClick)
        }
    ) {

        LargeTextField(
            text = searchedPrompt,
            onValueChange = onSearchPromptChange,
            placeholderText = "\"${currencyPickerUiState.selectedCurrency?.currencyCode ?: ""}\"",
            fontSize = 20.sp,
            cornerSize = 17.dp
        )

        GlassSurface(
            modifier = Modifier.weight(1f)
        ) {
            GlassSurfaceContent(
                currencyList = currencyList,
                uiState = currencyPickerUiState,
                onSelectCurrency = onSelectCurrency
            )
        }

    }
}

@Composable
private fun GlassSurfaceContent(
    currencyList: List<CurrencyItem>,
    uiState: CurrencyPickerUiState,
    onSelectCurrency: (CurrencyItem) -> Unit
) {
    val lazyColumnState = rememberLazyListState()

    LazyColumn(
        state = lazyColumnState,
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp)
    ) {
        items(items = currencyList, key = { it.currencyCode }) { currency ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .bounceClickEffect(.99f) {
                        onSelectCurrency(currency)
                    }
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 8.dp)
            ) {
                RadioButton(
                    selected = currency == uiState.selectedCurrency,
                    onClick = null,
                    colors = RadioButtonDefaults.colors(
                        selectedColor = GlanciColors.primary,
                        unselectedColor = GlanciColors.outline
                    )
                )
                Text(
                    text = "${currency.currencyCode} - ${currency.displayName}",
                    color = GlanciColors.onSurface,
                    fontSize = 17.sp
                )
            }
        }
    }
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun CurrencyPickerScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault
) {
    PreviewWithMainScaffoldContainer(appTheme = appTheme) { scaffoldPadding ->
        CurrencyPickerScreen(
            screenPadding = scaffoldPadding,
            onNavigateBack = {},
            accountName = "Account Name",
            currencyPickerUiState = CurrencyPickerUiState(
                isSearching = false,
                selectedCurrency = CurrencyItem("USD", "US Dollar")
            ),
            currencyList = Currency.getAvailableCurrencies().toSortedCurrencyItemList(),
            searchedPrompt = "",
            onSearchPromptChange = {},
            onSelectCurrency = {},
            onSaveButtonClick = {}
        )
    }
}

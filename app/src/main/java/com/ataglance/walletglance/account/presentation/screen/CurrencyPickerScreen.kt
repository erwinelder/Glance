package com.ataglance.walletglance.account.presentation.screen

import androidx.compose.animation.AnimatedContent
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.account.domain.utils.toSortedCurrencyItemList
import com.ataglance.walletglance.account.presentation.model.CurrencyItem
import com.ataglance.walletglance.account.presentation.model.CurrencyPickerUiState
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.presentation.component.button.PrimaryButton
import com.ataglance.walletglance.core.presentation.component.field.TextFieldComponent
import com.ataglance.walletglance.core.presentation.component.screenContainer.GlassSurfaceScreenContainer
import com.ataglance.walletglance.core.presentation.component.screenContainer.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.modifier.bounceClickEffect
import com.ataglance.walletglance.core.presentation.theme.CurrWindowType
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import java.util.Currency

@Composable
fun CurrencyPickerScreen(
    scaffoldPadding: PaddingValues,
    currencyPickerUiState: CurrencyPickerUiState,
    currencyList: List<CurrencyItem>,
    searchedPrompt: String,
    onSearchPromptChange: (String) -> Unit,
    onSelectCurrency: (CurrencyItem) -> Unit,
    onSaveButtonClick: (String) -> Unit,
) {

    GlassSurfaceScreenContainer(
        topPadding = scaffoldPadding.calculateTopPadding(),
        topButton = {
            AnimatedContent(
                targetState = currencyPickerUiState.selectedCurrency?.currencyCode,
                label = "selected currency code"
            ) { targetCurrencyCode ->
                TextFieldComponent(
                    text = searchedPrompt,
                    onValueChange = onSearchPromptChange,
                    placeholderText = "\"${targetCurrencyCode}\"",
                    modifier = Modifier.fillMaxWidth(
                        FilledWidthByScreenType(compact = .86f).getByType(CurrWindowType)
                    ),
                    fontSize = 20.sp,
                    cornerSize = 17.dp
                )
            }
        },
        glassSurfaceContent = {
            GlassSurfaceContent(
                currencyList = currencyList,
                uiState = currencyPickerUiState,
                onSelectCurrency = onSelectCurrency
            )
        },
        primaryBottomButton = {
            PrimaryButton(
                text = stringResource(R.string.save),
                enabled = currencyPickerUiState.selectedCurrency != null
            ) {
                currencyPickerUiState.selectedCurrency?.currencyCode?.let {
                    onSaveButtonClick(it)
                }
            }
        }
    )
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
        contentPadding = PaddingValues(12.dp, 12.dp)
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
            scaffoldPadding = scaffoldPadding,
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

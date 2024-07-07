package com.ataglance.walletglance.ui.theme.screens.settings.accounts

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.ui.theme.GlanceTheme
import com.ataglance.walletglance.ui.theme.WindowTypeIsCompact
import com.ataglance.walletglance.ui.theme.WindowTypeIsMedium
import com.ataglance.walletglance.ui.theme.animation.bounceClickEffect
import com.ataglance.walletglance.ui.theme.screencontainers.SetupDataScreenContainer
import com.ataglance.walletglance.ui.theme.uielements.buttons.PrimaryButton
import com.ataglance.walletglance.ui.theme.uielements.fields.CustomTextField
import com.ataglance.walletglance.ui.viewmodels.accounts.CurrencyItem
import com.ataglance.walletglance.ui.viewmodels.accounts.CurrencyPickerUiState

@Composable
fun CurrencyPickerScreen(
    scaffoldPadding: PaddingValues,
    uiState: CurrencyPickerUiState,
    currencyList: List<CurrencyItem>,
    searchedPrompt: String,
    onSearchPromptChange: (String) -> Unit,
    onSelectCurrency: (CurrencyItem) -> Unit,
    onSaveButtonClick: (String) -> Unit,
) {

    SetupDataScreenContainer(
        topPadding = scaffoldPadding.calculateTopPadding(),
        topButton = {
            AnimatedContent(
                targetState = uiState.selectedCurrency?.currencyCode,
                label = "selected currency code"
            ) { targetCurrencyCode ->
                CustomTextField(
                    text = searchedPrompt,
                    onValueChange = onSearchPromptChange,
                    placeholderText = "\"${targetCurrencyCode}\"",
                    modifier = Modifier
                        .fillMaxWidth(
                            when {
                                WindowTypeIsCompact -> .88f
                                WindowTypeIsMedium -> .66f
                                else -> .44f
                            }
                        ),
                    fontSize = 20.sp,
                    cornerSize = 17.dp
                )
            }
        },
        glassSurfaceContent = {
            GlassSurfaceContent(
                currencyList = currencyList,
                uiState = uiState,
                onSelectCurrency = onSelectCurrency
            )
        },
        primaryBottomButton = {
            PrimaryButton(
                text = stringResource(R.string.save),
                enabled = uiState.selectedCurrency != null
            ) {
                uiState.selectedCurrency?.currencyCode?.let {
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
                        selectedColor = GlanceTheme.primary,
                        unselectedColor = GlanceTheme.onSurface.copy(.5f)
                    )
                )
                Text(
                    text = "${currency.currencyCode} - ${currency.displayName}",
                    color = GlanceTheme.onSurface,
                    fontSize = 17.sp
                )
            }
        }
    }
}

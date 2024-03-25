package com.ataglance.walletglance.ui.theme.screens.settings

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ataglance.walletglance.R
import com.ataglance.walletglance.model.CurrencyPickerViewModel
import com.ataglance.walletglance.ui.theme.GlanceTheme
import com.ataglance.walletglance.ui.theme.WindowTypeIsCompact
import com.ataglance.walletglance.ui.theme.WindowTypeIsExpanded
import com.ataglance.walletglance.ui.theme.WindowTypeIsMedium
import com.ataglance.walletglance.ui.theme.animation.bounceClickEffect
import com.ataglance.walletglance.ui.theme.uielements.buttons.PrimaryButton
import com.ataglance.walletglance.ui.theme.uielements.containers.GlassSurface
import com.ataglance.walletglance.ui.theme.uielements.fields.CustomTextField

@Composable
fun CurrencyPickerScreen(
    scaffoldPadding: PaddingValues,
    viewModel: CurrencyPickerViewModel,
    onSaveButtonClick: (String) -> Unit,
) {
    val lazyColumnState = rememberLazyListState()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val currencyList by viewModel.currencyList.collectAsStateWithLifecycle()
    val searchedPrompt by viewModel.searchedPrompt.collectAsStateWithLifecycle()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.buttons_gap)),
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = scaffoldPadding.calculateTopPadding() + dimensionResource(R.dimen.screen_vertical_padding),
                bottom = dimensionResource(R.dimen.screen_vertical_padding)
            )
    ) {
        AnimatedContent(
            targetState = uiState.selectedCurrency?.currencyCode,
            label = "selected currency code"
        ) { targetCurrencyCode ->
            CustomTextField(
                text = searchedPrompt,
                onValueChange = viewModel::onSearchPromptChange,
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
        GlassSurface(
            modifier = Modifier.weight(1f),
            filledWidth = if (!WindowTypeIsExpanded) null else .64f
        ) {
            LazyColumn(
                state = lazyColumnState,
                contentPadding = PaddingValues(12.dp, 12.dp)
            ) {
                items(items = currencyList, key = { it.currencyCode }) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .bounceClickEffect(.99f) { viewModel.selectCurrency(it) }
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 8.dp)
                    ) {
                        RadioButton(
                            selected = it == uiState.selectedCurrency,
                            onClick = null,
                            colors = RadioButtonDefaults.colors(
                                selectedColor = GlanceTheme.primary,
                                unselectedColor = GlanceTheme.onSurface.copy(.5f)
                            )
                        )
                        Text(
                            text = "${it.currencyCode} - ${it.displayName}",
                            color = GlanceTheme.onSurface,
                            fontSize = 17.sp
                        )
                    }
                }
            }
        }
        PrimaryButton(
            text = stringResource(R.string.save),
            enabled = uiState.selectedCurrency != null
        ) {
            uiState.selectedCurrency?.currencyCode?.let {
                onSaveButtonClick(it)
            }
        }
    }
}
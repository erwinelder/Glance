package com.ataglance.walletglance.recordCreation.presentation.components

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.category.domain.CategoryType
import com.ataglance.walletglance.core.presentation.components.buttons.BarButton

@Composable
fun RecordCreationTypeBar(
    isTransferButtonVisible: Boolean,
    onTransferButtonClick: () -> Unit,
    currentCategoryType: CategoryType,
    onButtonClick: (CategoryType) -> Unit
) {
    val scrollState = rememberScrollState()

    Row(modifier = Modifier.fillMaxWidth()) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.screen_horizontal_padding)))
            if (isTransferButtonVisible) {
                BarButton(text = stringResource(R.string.transfer), onClick = onTransferButtonClick)
                Spacer(modifier = Modifier.width(8.dp))
            }
            BarButton(
                text = stringResource(R.string.expense),
                active = currentCategoryType == CategoryType.Expense
            ) {
                onButtonClick(CategoryType.Expense)
            }
            Spacer(modifier = Modifier.width(8.dp))
            BarButton(
                text = stringResource(R.string.income_singular),
                active = currentCategoryType == CategoryType.Income
            ) {
                onButtonClick(CategoryType.Income)
            }
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.screen_horizontal_padding)))
        }
    }
}
package com.ataglance.walletglance.ui.theme.uielements.categories

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
import com.ataglance.walletglance.ui.viewmodels.CategoryType
import com.ataglance.walletglance.ui.theme.uielements.buttons.BarButton

@Composable
fun CategoryTypeFilterBar(
    currentCategoryType: CategoryType,
    onClick: (CategoryType) -> Unit
) {
    val scrollState = rememberScrollState()

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
        ) {
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.screen_horizontal_padding)))
            BarButton(
                onClick = { onClick(CategoryType.Income) },
                active = currentCategoryType == CategoryType.Income,
                text = stringResource(R.string.income_plural)
            )
            Spacer(modifier = Modifier.width(8.dp))
            BarButton(
                onClick = { onClick(CategoryType.Expense) },
                active = currentCategoryType == CategoryType.Expense,
                text = stringResource(R.string.expenses)
            )
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.screen_horizontal_padding)))
        }
    }
}
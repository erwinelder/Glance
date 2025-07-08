package com.ataglance.walletglance.category.presentation.component

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
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.core.presentation.component.button.BarButton

@Composable
fun CategoryTypeBar(
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
            BarButton(
                text = stringResource(R.string.expenses),
                active = currentCategoryType == CategoryType.Expense,
                onClick = { onButtonClick(CategoryType.Expense) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            BarButton(
                text = stringResource(R.string.income_plural),
                active = currentCategoryType == CategoryType.Income,
                onClick = { onButtonClick(CategoryType.Income) }
            )
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.screen_horizontal_padding)))
        }
    }
}
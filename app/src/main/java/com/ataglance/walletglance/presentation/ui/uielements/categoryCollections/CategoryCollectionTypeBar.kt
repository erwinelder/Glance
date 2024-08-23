package com.ataglance.walletglance.presentation.ui.uielements.categoryCollections

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
import com.ataglance.walletglance.domain.categoryCollections.CategoryCollectionType
import com.ataglance.walletglance.presentation.ui.uielements.buttons.BarButton

@Composable
fun CategoryCollectionTypeBar(
    currentType: CategoryCollectionType,
    onClick: (CategoryCollectionType) -> Unit
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
                onClick = { onClick(CategoryCollectionType.Expense) },
                active = currentType == CategoryCollectionType.Expense,
                text = stringResource(R.string.expenses)
            )
            Spacer(modifier = Modifier.width(8.dp))
            BarButton(
                onClick = { onClick(CategoryCollectionType.Income) },
                active = currentType == CategoryCollectionType.Income,
                text = stringResource(R.string.income_plural)
            )
            Spacer(modifier = Modifier.width(8.dp))
            BarButton(
                onClick = { onClick(CategoryCollectionType.Mixed) },
                active = currentType == CategoryCollectionType.Mixed,
                text = stringResource(R.string.mixed)
            )
            Spacer(modifier = Modifier.width(dimensionResource(R.dimen.screen_horizontal_padding)))
        }
    }
}
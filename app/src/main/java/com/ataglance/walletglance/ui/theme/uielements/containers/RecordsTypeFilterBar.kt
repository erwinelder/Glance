package com.ataglance.walletglance.ui.theme.uielements.containers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.ataglance.walletglance.R
import com.ataglance.walletglance.model.RecordsTypeFilter
import com.ataglance.walletglance.ui.theme.uielements.buttons.BarButton

@Composable
fun RecordsTypeFilterBar(
    currentTypeFiler: RecordsTypeFilter,
    onClick: (RecordsTypeFilter) -> Unit,
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxWidth()
    ) {
        BarButton(
            onClick = { onClick(RecordsTypeFilter.Expenses) },
            active = currentTypeFiler == RecordsTypeFilter.Expenses,
            text = stringResource(R.string.expenses)
        )
        BarButton(
            onClick = { onClick(RecordsTypeFilter.Income) },
            active = currentTypeFiler == RecordsTypeFilter.Income,
            text = stringResource(R.string.income_plural)
        )
        BarButton(
            onClick = { onClick(RecordsTypeFilter.All) },
            active = currentTypeFiler == RecordsTypeFilter.All,
            text = stringResource(R.string.view_all)
        )
    }
}
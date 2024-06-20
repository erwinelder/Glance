package com.ataglance.walletglance.ui.theme.uielements.records

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import com.ataglance.walletglance.R
import com.ataglance.walletglance.data.records.RecordsTypeFilter
import com.ataglance.walletglance.ui.theme.GlanceTheme

@Composable
fun EmptyRecordsHistoryMessageContainer(
    recordsTypeFilter: RecordsTypeFilter = RecordsTypeFilter.All
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(
                when(recordsTypeFilter) {
                    RecordsTypeFilter.All -> R.string.you_have_no_records_in_date_range
                    RecordsTypeFilter.Expenses -> R.string.you_have_no_expenses_in_date_range
                    RecordsTypeFilter.Income -> R.string.you_have_no_income_in_date_range
                }
            ),
            color = GlanceTheme.onSurface.copy(.6f),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Light
        )
    }
}
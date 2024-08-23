package com.ataglance.walletglance.presentation.ui.uielements.categories

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
import com.ataglance.walletglance.presentation.ui.GlanceTheme

@Composable
fun EmptyCategoriesStatisticsMessageContainer() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = stringResource(R.string.no_data_for_the_selected_filter),
            color = GlanceTheme.onSurface.copy(.6f),
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Light
        )
    }
}
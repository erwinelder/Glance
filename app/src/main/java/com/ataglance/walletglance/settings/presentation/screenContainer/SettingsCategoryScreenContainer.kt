package com.ataglance.walletglance.settings.presentation.screenContainer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.presentation.theme.CurrWindowType
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.core.presentation.theme.Typography
import com.ataglance.walletglance.core.presentation.theme.WindowTypeIsCompact
import com.ataglance.walletglance.core.presentation.utils.plus
import com.ataglance.walletglance.settings.presentation.component.SettingsCategoryTopNavBackButton
import com.ataglance.walletglance.settings.presentation.model.SettingsCategory

@Composable
fun SettingsCategoryScreenContainer(
    screenPadding: PaddingValues = PaddingValues(),
    thisCategory: SettingsCategory,
    onNavigateBack: (() -> Unit)? = null,
    topBottomSpacingProportion: Pair<Float, Float> = Pair(2f, 1f),
    title: String? = null,
    mainScreenContent: @Composable ColumnScope.() -> Unit,
    allowScroll: Boolean = true,
    bottomBlock: @Composable (() -> Unit)? = null
) {
    val scrollState = rememberScrollState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(
                screenPadding + PaddingValues(
                    top = 8.dp,
                    bottom = if (bottomBlock != null) 24.dp else 8.dp
                )
            )
    ) {
        if (onNavigateBack != null && WindowTypeIsCompact) {
            SettingsCategoryTopNavBackButton(thisCategory, onNavigateBack)
        }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxWidth(FilledWidthByScreenType().get(CurrWindowType))
                .weight(topBottomSpacingProportion.first)
        ) {
            title?.let {
                Text(
                    text = title,
                    style = Typography.titleLarge,
                    color = GlanciColors.onSurface
                )
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp),
            modifier = Modifier
                .run {
                    if (allowScroll) verticalScroll(scrollState) else this
                }
                .fillMaxWidth(FilledWidthByScreenType().get(CurrWindowType))
        ) {
            mainScreenContent()
        }

        Spacer(modifier = Modifier.weight(topBottomSpacingProportion.second))
        bottomBlock?.invoke()
    }
}
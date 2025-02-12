package com.ataglance.walletglance.billing.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.billing.presentation.model.SubscriptionUiState
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.theme.CurrAppTheme
import com.ataglance.walletglance.core.presentation.theme.GlanceColors
import com.ataglance.walletglance.core.presentation.theme.Manrope
import com.ataglance.walletglance.core.presentation.components.buttons.TertiaryButton
import com.ataglance.walletglance.core.presentation.components.containers.GlassSurface
import com.ataglance.walletglance.core.presentation.components.containers.GlassSurfaceContentColumnWrapper
import com.ataglance.walletglance.core.presentation.components.dividers.BigDivider
import com.ataglance.walletglance.core.presentation.components.screenContainers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.errorHandling.presentation.components.containers.ResultBottomSheet
import com.ataglance.walletglance.errorHandling.presentation.model.ResultUiState
import com.ataglance.walletglance.settings.domain.SettingsCategories
import com.ataglance.walletglance.settings.presentation.screenContainers.SettingsCategoryScreenContainer

@Composable
fun SubscriptionsScreen(
    onNavigateBack: () -> Unit,
    activeSubscriptions: List<SubscriptionUiState>,
    availableSubscriptions: List<SubscriptionUiState>,
    onStartPurchase: (SubscriptionUiState) -> Unit,
    purchaseResultUiState: ResultUiState?,
    onResultReset: () -> Unit
) {
    val thisCategory = SettingsCategories(appTheme = CurrAppTheme).manageSubscriptions

    Box {
        SettingsCategoryScreenContainer(
            thisCategory = thisCategory,
            onNavigateBack = onNavigateBack,
            topBottomSpacingProportion = Pair(1f, 1f),
            mainScreenContentBlock = {
                SubscriptionsList(
                    subscriptions = activeSubscriptions,
                    onStartPurchase = onStartPurchase
                )
                BigDivider(
                    modifier = Modifier.padding(vertical = 16.dp)
                )
                SubscriptionsList(
                    subscriptions = availableSubscriptions,
                    onStartPurchase = onStartPurchase
                )
            }
        )
        ResultBottomSheet(resultState = purchaseResultUiState, onDismissRequest = onResultReset)
    }
}

@Composable
fun SubscriptionsList(
    subscriptions: List<SubscriptionUiState>,
    onStartPurchase: (SubscriptionUiState) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        subscriptions.forEach { subscription ->
            SubscriptionComponent(
                uiState = subscription,
                onStartPurchase = onStartPurchase
            )
        }
    }
}

@Composable
fun SubscriptionComponent(
    uiState: SubscriptionUiState,
    onStartPurchase: (SubscriptionUiState) -> Unit
) {
    GlassSurface {
        GlassSurfaceContentColumnWrapper {
            Text(
                text = uiState.name,
                color = GlanceColors.onSurface,
                fontFamily = Manrope,
                fontSize = 20.sp,
                fontWeight = FontWeight.W600
            )
            Text(
                text = uiState.price,
                color = GlanceColors.onSurface,
                fontFamily = Manrope,
                fontSize = 20.sp,
                fontWeight = FontWeight.W600
            )
            TertiaryButton(text = "Subscribe") {
                onStartPurchase(uiState)
            }
        }
    }
}



@Preview
@Composable
fun PreviewSubscriptionScreen(
    appTheme: AppTheme = AppTheme.LightDefault
) {
    PreviewWithMainScaffoldContainer(appTheme = appTheme) {
        SubscriptionsScreen(
            onNavigateBack = {},
            activeSubscriptions = listOf(
                SubscriptionUiState(
                    id = "free",
                    name = "Base plan",
                    benefits = listOf(
                        "Benefit 1",
                        "Benefit 2"
                    ),
                    price = "0.00 USD"
                )
            ),
            availableSubscriptions = listOf(
                SubscriptionUiState(
                    id = "free",
                    name = "Premium plan",
                    benefits = listOf(
                        "Benefit 1",
                        "Benefit 2"
                    ),
                    price = "0.00 USD"
                )
            ),
            onStartPurchase = {},
            purchaseResultUiState = null,
            onResultReset = {}
        )
    }
}

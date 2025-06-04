package com.ataglance.walletglance.billing.presentation.screen

import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.ataglance.walletglance.billing.presentation.model.SubscriptionUiState
import com.ataglance.walletglance.billing.presentation.viewmodel.SubscriptionViewModel
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.component.button.TertiaryButton
import com.ataglance.walletglance.core.presentation.component.container.GlassSurface
import com.ataglance.walletglance.core.presentation.component.container.GlassSurfaceContentColumnWrapper
import com.ataglance.walletglance.core.presentation.component.divider.BigDivider
import com.ataglance.walletglance.core.presentation.component.screenContainer.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.theme.CurrAppTheme
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.core.presentation.theme.Manrope
import com.ataglance.walletglance.core.presentation.viewmodel.sharedKoinNavViewModel
import com.ataglance.walletglance.settings.presentation.model.SettingsCategory
import com.ataglance.walletglance.settings.presentation.screenContainer.SettingsCategoryScreenContainer

@Composable
fun SubscriptionsScreenWrapper(
    navController: NavHostController,
    backStack: NavBackStackEntry
) {
    val viewModel = backStack.sharedKoinNavViewModel<SubscriptionViewModel>(navController)

    val activeSubscriptions by viewModel.activeSubscriptions.collectAsStateWithLifecycle()
    val availableSubscriptions by viewModel.availableSubscriptions.collectAsStateWithLifecycle()
    val purchaseResult by viewModel.purchaseResult.collectAsStateWithLifecycle()

    val activity = LocalActivity.current

    SubscriptionsScreen(
        onNavigateBack = navController::popBackStack,
        activeSubscriptions = activeSubscriptions,
        availableSubscriptions = availableSubscriptions,
        onStartPurchase = { subscription ->
            activity?.let {
                viewModel.startPurchase(activity = it, subscription = subscription)
            }
        },
    )
}

@Composable
fun SubscriptionsScreen(
    onNavigateBack: () -> Unit,
    activeSubscriptions: List<SubscriptionUiState>,
    availableSubscriptions: List<SubscriptionUiState>,
    onStartPurchase: (SubscriptionUiState) -> Unit
) {
    Box {
        SettingsCategoryScreenContainer(
            thisCategory = SettingsCategory.ManageSubscriptions(CurrAppTheme),
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
                color = GlanciColors.onSurface,
                fontFamily = Manrope,
                fontSize = 20.sp,
                fontWeight = FontWeight.W600
            )
            Text(
                text = uiState.price,
                color = GlanciColors.onSurface,
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
            onStartPurchase = {}
        )
    }
}

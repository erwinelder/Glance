package com.ataglance.walletglance.core.presentation.component.screenContainer

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.presentation.component.AccountsFilterBar
import com.ataglance.walletglance.categoryCollection.presentation.component.CategoryCollectionPickerContainer
import com.ataglance.walletglance.categoryCollection.presentation.model.CategoryCollectionsUiState
import com.ataglance.walletglance.core.domain.date.DateRangeEnum
import com.ataglance.walletglance.core.presentation.component.container.DateFilterBar
import com.ataglance.walletglance.core.presentation.component.container.MessageContainer
import com.ataglance.walletglance.core.presentation.utils.bottom
import com.ataglance.walletglance.core.presentation.utils.top

@Composable
fun <S> GlassSurfaceScreenContainerWithFilters(
    screenPadding: PaddingValues = PaddingValues(),

    accounts: List<Account>,
    onAccountClick: (Int) -> Unit,

    currentDateRangeEnum: DateRangeEnum,
    isCustomDateRangeWindowOpened: Boolean,
    onDateRangeChange: (DateRangeEnum) -> Unit,
    onCustomDateRangeButtonClick: () -> Unit,

    collectionsUiState: CategoryCollectionsUiState,
    onCollectionSelect: (Int) -> Unit,
    onToggleCollectionType: () -> Unit,

    animatedContentTargetState: S,
    visibleNoDataMessage: Boolean,
    noDataMessageRes: Int,
    onNavigateToEditCollectionsScreen: () -> Unit,
    onDimBackgroundChange: (Boolean) -> Unit = {},
    animatedContent: @Composable (S) -> Unit
) {
    val visibleAccounts by remember(accounts) {
        derivedStateOf { accounts.filterNot { it.hide } }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxSize()
            .padding(top = screenPadding.top + 12.dp)
    ) {
        if (visibleAccounts.size > 1) {
            AccountsFilterBar(
                visibleAccounts = visibleAccounts,
                onAccountClick = onAccountClick
            )
        }
        DateFilterBar(
            currentDateRangeEnum = currentDateRangeEnum,
            isCustomDateRangeWindowOpened = isCustomDateRangeWindowOpened,
            onDateRangeChange = onDateRangeChange,
            onCustomDateRangeButtonClick = onCustomDateRangeButtonClick
        )
        CategoryCollectionPickerContainer(
            collectionList = collectionsUiState.collections,
            activeCollection = collectionsUiState.activeCollection,
            onCollectionSelect = onCollectionSelect,
            activeType = collectionsUiState.activeType,
            onTypeToggle = onToggleCollectionType,
            onNavigateToEditCollectionsScreen = onNavigateToEditCollectionsScreen,
            onDimBackgroundChange = onDimBackgroundChange
        )
        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier.fillMaxSize()
        ) {
            AnimatedContent(targetState = animatedContentTargetState) {
                animatedContent(it)
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = screenPadding.bottom)
            ) {
                AnimatedVisibility(
                    visible = visibleNoDataMessage,
                    enter = fadeIn(tween(durationMillis = 220, delayMillis = 90)) +
                            scaleIn(
                                animationSpec = tween(durationMillis = 220, delayMillis = 90),
                                initialScale = .92f
                            ),
                    exit = fadeOut(animationSpec = tween(durationMillis = 90)),
                    modifier = Modifier.fillMaxSize()
                ) {
                    MessageContainer(message = stringResource(noDataMessageRes))
                }
            }
        }
    }
}
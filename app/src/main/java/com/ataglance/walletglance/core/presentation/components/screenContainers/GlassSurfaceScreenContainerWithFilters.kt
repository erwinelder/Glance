package com.ataglance.walletglance.core.presentation.components.screenContainers

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
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.ataglance.walletglance.R
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.presentation.components.AccountsFilterBar
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionWithIds
import com.ataglance.walletglance.categoryCollection.presentation.components.CategoryCollectionPickerContainer
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.domain.date.DateRangeEnum
import com.ataglance.walletglance.core.presentation.components.containers.DateFilterBar
import com.ataglance.walletglance.core.presentation.components.containers.GlassSurface
import com.ataglance.walletglance.core.presentation.components.containers.MessageContainer

@Composable
fun <S> GlassSurfaceScreenContainerWithFilters(
    screenPadding: PaddingValues,
    accountList: List<Account>,
    onAccountClick: (Int) -> Unit,
    currentDateRangeEnum: DateRangeEnum,
    isCustomDateRangeWindowOpened: Boolean,
    onDateRangeChange: (DateRangeEnum) -> Unit,
    onCustomDateRangeButtonClick: () -> Unit,
    collectionList: List<CategoryCollectionWithIds>,
    selectedCollection: CategoryCollectionWithIds,
    onCollectionSelect: (CategoryCollectionWithIds) -> Unit,
    typeToggleButton: @Composable () -> Unit,
    animatedContentLabel: String,
    animatedContentTargetState: S,
    visibleNoDataMessage: Boolean,
    noDataMessageRes: Int,
    onNavigateToEditCollectionsScreen: () -> Unit,
    onDimBackgroundChange: (Boolean) -> Unit = {},
    animatedContent: @Composable (S) -> Unit
) {
    val visibleAccounts by remember(accountList) {
        derivedStateOf { accountList.filterNot { it.hide } }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.button_bar_to_widget_gap)),
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = screenPadding.calculateTopPadding() +
                        dimensionResource(R.dimen.button_bar_to_widget_gap),
                bottom = screenPadding.calculateBottomPadding() +
                        dimensionResource(R.dimen.screen_vertical_padding)
            )
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
            collectionList = collectionList,
            selectedCollection = selectedCollection,
            onCollectionSelect = onCollectionSelect,
            typeToggleButton = typeToggleButton,
            onNavigateToEditCollectionsScreen = onNavigateToEditCollectionsScreen,
            onDimBackgroundChange = onDimBackgroundChange
        )
        Spacer(modifier = Modifier)
        GlassSurface(
            modifier = Modifier
                .fillMaxHeight()
                .padding(horizontal = dimensionResource(R.dimen.screen_horizontal_padding)),
            filledWidths = FilledWidthByScreenType(compact = 1f)
        ) {
            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = dimensionResource(R.dimen.screen_horizontal_padding))
            ) {
                AnimatedContent(
                    targetState = animatedContentTargetState,
                    label = animatedContentLabel
                ) {
                    animatedContent(it)
                }
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxSize()
                ) {
                    AnimatedVisibility(
                        visible = visibleNoDataMessage,
                        enter = fadeIn(tween(220, delayMillis = 90)) +
                                scaleIn(tween(220, delayMillis = 90), .92f),
                        exit = fadeOut(animationSpec = tween(90)),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        MessageContainer(message = stringResource(noDataMessageRes))
                    }
                }
            }
        }
    }
}
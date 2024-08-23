package com.ataglance.walletglance.presentation.ui.screencontainers

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.ataglance.walletglance.R
import com.ataglance.walletglance.domain.accounts.Account
import com.ataglance.walletglance.domain.app.AppTheme
import com.ataglance.walletglance.domain.categoryCollections.CategoryCollectionWithIds
import com.ataglance.walletglance.domain.date.DateRangeEnum
import com.ataglance.walletglance.presentation.ui.WindowTypeIsCompact
import com.ataglance.walletglance.presentation.ui.uielements.accounts.AccountsFilterBar
import com.ataglance.walletglance.presentation.ui.uielements.categoryCollections.CategoryCollectionPickerContainer
import com.ataglance.walletglance.presentation.ui.uielements.containers.DateFilterBar
import com.ataglance.walletglance.presentation.ui.uielements.containers.GlassSurface
import com.ataglance.walletglance.presentation.ui.uielements.containers.MessageContainer

@Composable
fun <S> DataPresentationScreenContainer(
    scaffoldAppScreenPadding: PaddingValues,
    appTheme: AppTheme?,
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
    animationContentLabel: String,
    animatedContentTargetState: S,
    visibleNoDataMessage: Boolean,
    noDataMessageRes: Int,
    onNavigateToEditCollectionsScreen: () -> Unit,
    onDimBackgroundChange: (Boolean) -> Unit = {},
    animatedContent: @Composable (S) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.button_bar_to_widget_gap)),
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = scaffoldAppScreenPadding.calculateTopPadding() +
                        dimensionResource(R.dimen.button_bar_to_widget_gap),
                bottom = scaffoldAppScreenPadding.calculateBottomPadding() +
                        dimensionResource(R.dimen.screen_vertical_padding)
            )
    ) {
        if (accountList.size > 1) {
            AccountsFilterBar(
                accountList = accountList,
                appTheme = appTheme,
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
            filledWidth = 1f.takeIf { WindowTypeIsCompact }
        ) {
            Box(
                contentAlignment = Alignment.TopCenter,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = dimensionResource(R.dimen.screen_horizontal_padding))
            ) {
                AnimatedContent(
                    targetState = animatedContentTargetState,
                    label = animationContentLabel
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
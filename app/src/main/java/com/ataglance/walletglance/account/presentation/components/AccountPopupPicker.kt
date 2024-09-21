package com.ataglance.walletglance.account.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.ataglance.walletglance.R
import com.ataglance.walletglance.account.domain.Account

@Composable
fun AccountPopupPicker(
    accountList: List<Account>,
    selectedAccount: Account?,
    onToggleAccounts: (List<Account>) -> Unit,
    onSelectAccount: (Account) -> Unit,
    onDimBackgroundChange: (Boolean) -> Unit
) {
    val expandedState = remember { MutableTransitionState(false) }

    Column(
        modifier = Modifier.padding(horizontal = 16.dp)
    ) {
        PickerButton(selectedAccount = selectedAccount) {
            if (accountList.size == 2) {
                onToggleAccounts(accountList)
            } else if (accountList.size > 1) {
                onDimBackgroundChange(it)
                expandedState.targetState = it
            }
        }
        Box {
            if (expandedState.targetState || expandedState.currentState || !expandedState.isIdle) {
                Popup(
                    properties = PopupProperties(
                        focusable = true,
                        excludeFromSystemGesture = true
                    ),
                    onDismissRequest = {
                        onDimBackgroundChange(false)
                        expandedState.targetState = false
                    }
                ) {
                    PopupContent(
                        accountList = accountList,
                        onAccountSelect = onSelectAccount,
                        expandedState = expandedState,
                        onExpandedChange = {
                            onDimBackgroundChange(it)
                            expandedState.targetState = it
                        }
                    )
                }
            }
        }
    }

}

@Composable
private fun PickerButton(
    selectedAccount: Account?,
    onExpandedChange: (Boolean) -> Unit
) {
    AnimatedContent(
        targetState = selectedAccount,
        label = "selected account"
    ) { account ->
        SmallAccount(
            account = account,
            onClick = {
                onExpandedChange(true)
            }
        )
    }
}

@Composable
private fun PopupContent(
    accountList: List<Account>,
    onAccountSelect: (Account) -> Unit,
    expandedState: MutableTransitionState<Boolean>,
    onExpandedChange: (Boolean) -> Unit
) {
    val lazyListState = rememberLazyListState()
    val itemAppearanceAnimSpeed = 300 / (accountList.size.takeIf { it != 0 } ?: 1)
    val itemAppearanceAnimationFloat: (Int) -> FiniteAnimationSpec<Float> = { orderNum ->
        tween(
            durationMillis = 300,
            delayMillis = itemAppearanceAnimSpeed * (orderNum - 1)
        )
    }
    val itemAppearanceAnimationOffset: (Int) -> FiniteAnimationSpec<IntOffset> = { orderNum ->
        tween(
            durationMillis = 300,
            delayMillis = itemAppearanceAnimSpeed * (orderNum - 1)
        )
    }

    LazyColumn(
        state = lazyListState,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 12.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(
            items = accountList,
            key = { it.id }
        ) { account ->
            AnimatedVisibility(
                visibleState = expandedState,
                enter = fadeIn(itemAppearanceAnimationFloat(account.orderNum)) +
                        scaleIn(itemAppearanceAnimationFloat(account.orderNum)) +
                        slideInVertically(
                            animationSpec = itemAppearanceAnimationOffset(account.orderNum),
                            initialOffsetY = { -it }
                        ),
                exit = fadeOut(tween(300)) +
                        scaleOut(tween(300)) +
                        slideOutVertically(
                            animationSpec = tween(300),
                            targetOffsetY = { -it }
                        )
            ) {
                SmallAccount(
                    account = account,
                    outerPadding = PaddingValues(
                        horizontal = dimensionResource(R.dimen.screen_horizontal_padding)
                    )
                ) {
                    onExpandedChange(false)
                    onAccountSelect(account)
                }
            }
        }
    }
}
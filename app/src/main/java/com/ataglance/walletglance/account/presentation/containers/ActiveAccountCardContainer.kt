package com.ataglance.walletglance.account.presentation.containers

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.presentation.components.ActiveAccountCard
import com.ataglance.walletglance.account.presentation.viewmodel.ActiveAccountCardViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ActiveAccountCardContainer(
    activeAccount: Account?,
    onChangeHideActiveAccountBalance: () -> Unit,
) {
    val viewModel = koinViewModel<ActiveAccountCardViewModel>()

    LaunchedEffect(activeAccount) {
        activeAccount?.id?.let(viewModel::applyActiveAccount)
    }

    val todayExpenses by viewModel.todayTotalExpenses.collectAsStateWithLifecycle()

    if (activeAccount != null) {
        ActiveAccountCard(
            account = activeAccount,
            todayExpenses = todayExpenses,
            onHideBalanceButton = onChangeHideActiveAccountBalance
        )
    }
}
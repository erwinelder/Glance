package com.ataglance.walletglance.core.domain.app

import com.ataglance.walletglance.account.domain.model.AccountsAndActiveOne
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionsWithIdsByType
import com.ataglance.walletglance.core.domain.date.DateRangeMenuUiState
import com.ataglance.walletglance.navigation.domain.model.BottomBarNavigationButton
import com.ataglance.walletglance.record.domain.model.RecordStack

data class AppUiState(
    val navigationButtonList: List<BottomBarNavigationButton>,
    val dateRangeMenuUiState: DateRangeMenuUiState,
    val categoryCollectionsUiState: CategoryCollectionsWithIdsByType,
    val accountsAndActiveOne: AccountsAndActiveOne,
    val recordStackListByDate: List<RecordStack>
)

package com.ataglance.walletglance.core.domain.app

import com.ataglance.walletglance.account.domain.AccountsAndActiveOne
import com.ataglance.walletglance.budget.domain.model.BudgetsByType
import com.ataglance.walletglance.category.domain.CategoriesWithSubcategories
import com.ataglance.walletglance.categoryCollection.domain.CategoryCollectionsWithIdsByType
import com.ataglance.walletglance.core.domain.date.DateRangeMenuUiState
import com.ataglance.walletglance.navigation.domain.model.BottomBarNavigationButton
import com.ataglance.walletglance.record.domain.RecordStack

data class AppUiState(
    val navigationButtonList: List<BottomBarNavigationButton>,
    val dateRangeMenuUiState: DateRangeMenuUiState,
    val categoriesWithSubcategories: CategoriesWithSubcategories,
    val categoryCollectionsUiState: CategoryCollectionsWithIdsByType,
    val accountsAndActiveOne: AccountsAndActiveOne,
    val recordStackListByDate: List<RecordStack>,
    val budgetsByType: BudgetsByType,
)

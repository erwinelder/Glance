package com.ataglance.walletglance.record.data.utils

import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.record.data.local.model.RecordEntity


fun List<RecordEntity>.getTotalAmountByType(type: CategoryType): Double {
    return this
        .filter {
            type == CategoryType.Expense && it.isExpenseOrOutTransfer() ||
                    type == CategoryType.Income && it.isIncomeOrInTransfer()
        }
        .fold(0.0) { total, record ->
            total + record.amount
        }
}

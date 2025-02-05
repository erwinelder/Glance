package com.ataglance.walletglance.record.domain.model

data class Record(
    val id: Int,
    val recordNum: Int,
    val date: Long,
    val type: RecordType,
    val accountId: Int,
    val amount: Double,
    val quantity: Int?,
    val categoryId: Int,
    val subcategoryId: Int?,
    val note: String?,
    val includeInBudgets: Boolean
) {

    fun isExpense() = type == RecordType.Expense
    fun isIncome() = type == RecordType.Income
    fun isOutTransfer() = type == RecordType.OutTransfer
    fun isInTransfer() = type == RecordType.InTransfer
    fun isExpenseOrOutTransfer() = isExpense() || isOutTransfer()
    fun isIncomeOrInTransfer() = isIncome() || isInTransfer()

    fun containsParentOrSubcategoryId(id: Int?): Boolean {
        return categoryId == id || subcategoryId == id
    }

}

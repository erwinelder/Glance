package com.ataglance.walletglance.category.domain.model

enum class CategoryType {
    Expense, Income;

    fun asChar(): Char {
        return when (this) {
            Expense -> '-'
            Income -> '+'
        }
    }

    fun toggle(): CategoryType {
        return when (this) {
            Expense -> Income
            Income -> Expense
        }
    }

    companion object {

        fun fromChar(char: Char): CategoryType? {
            return when (char) {
                '-' -> Expense
                '+' -> Income
                else -> null
            }
        }

    }

}
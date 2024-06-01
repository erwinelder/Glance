package com.ataglance.walletglance.model

import com.ataglance.walletglance.domain.entities.Account
import com.ataglance.walletglance.domain.repositories.AccountRepository
import com.ataglance.walletglance.domain.entities.Category
import com.ataglance.walletglance.domain.repositories.CategoryRepository
import com.ataglance.walletglance.domain.repositories.GeneralRepository
import com.ataglance.walletglance.domain.repositories.RecordAndAccountRepository
import com.ataglance.walletglance.domain.repositories.RecordRepository
import com.ataglance.walletglance.data.SettingsRepository
import org.junit.Assert
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.mockito.Mockito

class AppViewModelTest {

    private lateinit var appViewModel: AppViewModel

    @BeforeEach
    fun setUp() {
        appViewModel = AppViewModel(
            settingsRepository = Mockito.mock(SettingsRepository::class.java),
            accountRepository = Mockito.mock(AccountRepository::class.java),
            categoryRepository = Mockito.mock(CategoryRepository::class.java),
            recordRepository = Mockito.mock(RecordRepository::class.java),
            recordAndAccountRepository = Mockito.mock(RecordAndAccountRepository::class.java),
            generalRepository = Mockito.mock(GeneralRepository::class.java)
        )
    }



    private fun getAccountListByBalances(balanceList: List<Double>): List<Account> {
        return balanceList.mapIndexed { index, balance ->
            Account(
                id = index + 1,
                name = "${index + 1}",
                balance = balance
            )
        }
    }

    private fun getTestMadeTransferState(
        fromAccount: Account,
        toAccount: Account,
        startAmount: Double,
        finalAmount: Double,
    ): MadeTransferState {
        return MadeTransferState(
            idFrom = fromAccount.id,
            idTo = toAccount.id,
            recordStatus = MakeRecordStatus.Edit,
            fromAccount = fromAccount,
            toAccount = toAccount,
            startAmount = startAmount,
            finalAmount = finalAmount,
            recordNum = 1
        )
    }

    private fun getTestRecordStackList(
        fromAccount: Account,
        toAccount: Account,
        startAmount: Double,
        finalAmount: Double
    ): Pair<RecordStack, RecordStack> {
        return Pair(
            RecordStack(
                recordNum = 1,
                date = 202403110324,
                type = '-',
                accountId = fromAccount.id,
                totalAmount = startAmount,
                stack = listOf(
                    RecordStackUnit(
                        id = 1,
                        amount = startAmount,
                        quantity = null,
                        categoryId = 0,
                        subcategoryId = null,
                        note = null
                    )
                )
            ),
            RecordStack(
                recordNum = 2,
                date = 202403110324,
                type = '+',
                accountId = toAccount.id,
                totalAmount = finalAmount,
                stack = listOf(
                    RecordStackUnit(
                        id = 2,
                        amount = finalAmount,
                        quantity = null,
                        categoryId = 0,
                        subcategoryId = null,
                        note = null
                    )
                )
            )
        )
    }

    companion object {
        @JvmStatic
        fun testDataProviderGetUpdatedAccountsAfterEditedTransfer(): Collection<Arguments> {
            return listOf(
                Arguments.of(
                    listOf(200.0, 300.0),
                    listOf(225.0, 350.0),
                    50.0, 50.0,
                    25.0, 100.0,
                    0, 1,
                    0, 1
                ),
                Arguments.of(
                    listOf(200.0, 300.0),
                    listOf(350.0, 225.0),
                    50.0, 50.0,
                    25.0, 100.0,
                    0, 1,
                    1, 0
                ),
                Arguments.of(
                    listOf(200.0, 300.0, 100.0),
                    listOf(250.0, 225.0, 200.0),
                    50.0, 50.0,
                    25.0, 100.0,
                    0, 1,
                    1, 2
                ),
                Arguments.of(
                    listOf(200.0, 300.0, 400.0),
                    listOf(225.0, 250.0, 500.0),
                    50.0, 50.0,
                    25.0, 100.0,
                    0, 1,
                    0, 2
                ),
                Arguments.of(
                    listOf(200.0, 300.0, 400.0, 500.0),
                    listOf(250.0, 250.0, 375.0, 600.0),
                    50.0, 50.0,
                    25.0, 100.0,
                    0, 1,
                    2, 3
                ),
                Arguments.of(
                    listOf(200.0, 300.0, 400.0),
                    listOf(350.0, 250.0, 375.0),
                    50.0, 50.0,
                    25.0, 100.0,
                    0, 1,
                    2, 0
                )
            )
        }
    }

    @ParameterizedTest
    @MethodSource("testDataProviderGetUpdatedAccountsAfterEditedTransfer")
    fun testGetUpdatedAccountsAfterEditedTransfer(
        currentBalances: List<Double>,
        expectedBalances: List<Double>,
        prevStartAmount: Double,
        prevFinalAmount: Double,
        newStartAmount: Double,
        newFinalAmount: Double,
        prevFromAccountIndex: Int,
        prevToAccountIndex: Int,
        newFromAccountIndex: Int,
        newToAccountIndex: Int,
    ) {
        val accounts = getAccountListByBalances(currentBalances)
        val recordStacks = getTestRecordStackList(
            accounts[prevFromAccountIndex], accounts[prevToAccountIndex],
            prevStartAmount, prevFinalAmount
        )
        val uiState = getTestMadeTransferState(
            accounts[newFromAccountIndex],
            accounts[newToAccountIndex],
            newStartAmount, newFinalAmount
        )

        appViewModel.applyAccountListToUiState(accounts)

        val result = appViewModel.getUpdatedAccountsAfterEditedTransfer(
            uiState, recordStacks.first, recordStacks.second
        )

        Assertions.assertEquals(
            getAccountListByBalances(expectedBalances),
            result?.sortedBy { it.id })
    }



    @Test
    fun testBreakCategoriesOnDifferentLists() {
        val categoryList = listOf(
            Category(
                id = 1, type = '-', rank = 'c', orderNum = 1, parentCategoryId = 1,
                name = "category 1", iconName = "",
                colorName = CategoryColors.Olive(null).color.name
            ),
            Category(
                id = 2, type = '-', rank = 'c', orderNum = 2, parentCategoryId = 2,
                name = "category 2", iconName = "",
                colorName = CategoryColors.Camel(null).color.name
            ),

            Category(
                id = 13, type = '-', rank = 's', orderNum = 1, parentCategoryId = 1,
                name = "subcategory 11", iconName = "",
                colorName = CategoryColors.Olive(null).color.name
            ),
            Category(
                id = 14, type = '-', rank = 's', orderNum = 2, parentCategoryId = 1,
                name = "subcategory 12", iconName = "",
                colorName = CategoryColors.Olive(null).color.name
            ),

            Category(
                id = 15, type = '-', rank = 's', orderNum = 1, parentCategoryId = 2,
                name = "subcategory 21", iconName = "",
                colorName = CategoryColors.Camel(null).color.name
            ),
            Category(
                id = 16, type = '-', rank = 's', orderNum = 1, parentCategoryId = 2,
                name = "subcategory 22", iconName = "",
                colorName = CategoryColors.Camel(null).color.name
            ),
            Category(
                id = 17, type = '-', rank = 's', orderNum = 3, parentCategoryId = 2,
                name = "subcategory 23", iconName = "",
                colorName = CategoryColors.Camel(null).color.name
            ),
            Category(
                id = 18, type = '-', rank = 's', orderNum = 4, parentCategoryId = 2,
                name = "subcategory 24", iconName = "",
                colorName = CategoryColors.Camel(null).color.name
            ),
            Category(
                id = 19, type = '-', rank = 's', orderNum = 5, parentCategoryId = 2,
                name = "subcategory 25", iconName = "",
                colorName = CategoryColors.Camel(null).color.name
            ),
            Category(
                id = 20, type = '-', rank = 's', orderNum = 6, parentCategoryId = 2,
                name = "subcategory 26", iconName = "",
                colorName = CategoryColors.Camel(null).color.name
            )
        )

        Assert.assertThrows(IllegalAccessException::class.java) {
            CategoryController().breakCategoriesOnDifferentLists(categoryList)
        }
    }

    @Test
    fun testFixCategoriesOrderNumbers() {
        val currentCategoryList = listOf(
            Category(
                id = 1, type = '-', rank = 'c', orderNum = 1, parentCategoryId = 1,
                name = "category 1", iconName = "",
                colorName = CategoryColors.Olive(null).color.name
            ),
            Category(
                id = 2, type = '-', rank = 'c', orderNum = 1, parentCategoryId = 2,
                name = "category 2", iconName = "",
                colorName = CategoryColors.Camel(null).color.name
            ),

            Category(
                id = 13, type = '-', rank = 's', orderNum = 1, parentCategoryId = 1,
                name = "subcategory 11", iconName = "",
                colorName = CategoryColors.Olive(null).color.name
            ),
            Category(
                id = 14, type = '-', rank = 's', orderNum = 2, parentCategoryId = 1,
                name = "subcategory 12", iconName = "",
                colorName = CategoryColors.Olive(null).color.name
            ),

            Category(
                id = 15, type = '-', rank = 's', orderNum = 1, parentCategoryId = 2,
                name = "subcategory 21", iconName = "",
                colorName = CategoryColors.Camel(null).color.name
            ),
            Category(
                id = 16, type = '-', rank = 's', orderNum = 1, parentCategoryId = 2,
                name = "subcategory 22", iconName = "",
                colorName = CategoryColors.Camel(null).color.name
            ),
            Category(
                id = 17, type = '-', rank = 's', orderNum = 3, parentCategoryId = 2,
                name = "subcategory 23", iconName = "",
                colorName = CategoryColors.Camel(null).color.name
            ),
            Category(
                id = 18, type = '-', rank = 's', orderNum = 4, parentCategoryId = 2,
                name = "subcategory 24", iconName = "",
                colorName = CategoryColors.Camel(null).color.name
            ),
            Category(
                id = 19, type = '-', rank = 's', orderNum = 5, parentCategoryId = 2,
                name = "subcategory 25", iconName = "",
                colorName = CategoryColors.Camel(null).color.name
            ),
            Category(
                id = 20, type = '-', rank = 's', orderNum = 6, parentCategoryId = 2,
                name = "subcategory 26", iconName = "",
                colorName = CategoryColors.Camel(null).color.name
            )
        )
        val expectedCategoryList = listOf(
            Category(
                id = 1, type = '-', rank = 'c', orderNum = 1, parentCategoryId = 1,
                name = "category 1", iconName = "",
                colorName = CategoryColors.Olive(null).color.name
            ),
            Category(
                id = 2, type = '-', rank = 'c', orderNum = 2, parentCategoryId = 2,
                name = "category 2", iconName = "",
                colorName = CategoryColors.Camel(null).color.name
            ),

            Category(
                id = 13, type = '-', rank = 's', orderNum = 1, parentCategoryId = 1,
                name = "subcategory 11", iconName = "",
                colorName = CategoryColors.Olive(null).color.name
            ),
            Category(
                id = 14, type = '-', rank = 's', orderNum = 2, parentCategoryId = 1,
                name = "subcategory 12", iconName = "",
                colorName = CategoryColors.Olive(null).color.name
            ),

            Category(
                id = 15, type = '-', rank = 's', orderNum = 1, parentCategoryId = 2,
                name = "subcategory 21", iconName = "",
                colorName = CategoryColors.Camel(null).color.name
            ),
            Category(
                id = 16, type = '-', rank = 's', orderNum = 2, parentCategoryId = 2,
                name = "subcategory 22", iconName = "",
                colorName = CategoryColors.Camel(null).color.name
            ),
            Category(
                id = 17, type = '-', rank = 's', orderNum = 3, parentCategoryId = 2,
                name = "subcategory 23", iconName = "",
                colorName = CategoryColors.Camel(null).color.name
            ),
            Category(
                id = 18, type = '-', rank = 's', orderNum = 4, parentCategoryId = 2,
                name = "subcategory 24", iconName = "",
                colorName = CategoryColors.Camel(null).color.name
            ),
            Category(
                id = 19, type = '-', rank = 's', orderNum = 5, parentCategoryId = 2,
                name = "subcategory 25", iconName = "",
                colorName = CategoryColors.Camel(null).color.name
            ),
            Category(
                id = 20, type = '-', rank = 's', orderNum = 6, parentCategoryId = 2,
                name = "subcategory 26", iconName = "",
                colorName = CategoryColors.Camel(null).color.name
            )
        )
        Assertions.assertArrayEquals(
            expectedCategoryList.toTypedArray(),
            CategoryController().fixCategoriesOrderNums(currentCategoryList).toTypedArray()
        )
    }

}
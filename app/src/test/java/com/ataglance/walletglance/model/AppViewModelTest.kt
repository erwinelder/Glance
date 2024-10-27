package com.ataglance.walletglance.model

import com.ataglance.walletglance.account.data.repository.AccountRepository
import com.ataglance.walletglance.account.domain.Account
import com.ataglance.walletglance.budget.data.repository.BudgetAndBudgetAccountAssociationRepository
import com.ataglance.walletglance.category.data.model.CategoryEntity
import com.ataglance.walletglance.category.data.repository.CategoryRepository
import com.ataglance.walletglance.category.domain.color.CategoryColors
import com.ataglance.walletglance.category.data.utils.fixOrderNumbers
import com.ataglance.walletglance.categoryCollection.data.repository.CategoryCollectionAndCollectionCategoryAssociationRepository
import com.ataglance.walletglance.core.data.preferences.SettingsRepository
import com.ataglance.walletglance.core.data.repository.GeneralRepository
import com.ataglance.walletglance.core.presentation.viewmodel.AppViewModel
import com.ataglance.walletglance.record.data.repository.RecordRepository
import com.ataglance.walletglance.record.domain.RecordStack
import com.ataglance.walletglance.record.domain.RecordStackItem
import com.ataglance.walletglance.record.domain.RecordType
import com.ataglance.walletglance.recordAndAccount.data.repository.RecordAndAccountRepository
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
            categoryCollectionAndCollectionCategoryAssociationRepository = Mockito.mock(
                CategoryCollectionAndCollectionCategoryAssociationRepository::class.java
            ),
            recordRepository = Mockito.mock(RecordRepository::class.java),
            recordAndAccountRepository = Mockito.mock(RecordAndAccountRepository::class.java),
            budgetAndBudgetAccountAssociationRepository = Mockito.mock(
                BudgetAndBudgetAccountAssociationRepository::class.java
            ),
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
            recordIdFrom = fromAccount.id,
            recordIdTo = toAccount.id,
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
                type = RecordType.OutTransfer,
                account = fromAccount.toRecordAccount(),
                totalAmount = startAmount,
                stack = listOf(
                    RecordStackItem(
                        id = 1,
                        amount = startAmount,
                        quantity = null,
                        categoryWithSubcategory = null,
                        note = null,
                        includeInBudgets = true
                    )
                )
            ),
            RecordStack(
                recordNum = 2,
                date = 202403110324,
                type = RecordType.InTransfer,
                account = toAccount.toRecordAccount(),
                totalAmount = finalAmount,
                stack = listOf(
                    RecordStackItem(
                        id = 2,
                        amount = finalAmount,
                        quantity = null,
                        categoryWithSubcategory = null,
                        note = null,
                        includeInBudgets = true
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
            result?.sortedBy { it.id }
        )
    }



    /*@Test
    fun testBreakCategoriesOnDifferentLists() {
        val categoryList = listOf(
            CategoryEntity(
                id = 1, type = '-', rank = 'c', orderNum = 1, parentCategoryId = 1,
                name = "category 1", iconName = "",
                colorName = CategoryColors.Olive.name.name
            ),
            CategoryEntity(
                id = 2, type = '-', rank = 'c', orderNum = 2, parentCategoryId = 2,
                name = "category 2", iconName = "",
                colorName = CategoryColors.Camel.name.name
            ),

            CategoryEntity(
                id = 13, type = '-', rank = 's', orderNum = 1, parentCategoryId = 1,
                name = "subcategory 11", iconName = "",
                colorName = CategoryColors.Olive.name.name
            ),
            CategoryEntity(
                id = 14, type = '-', rank = 's', orderNum = 2, parentCategoryId = 1,
                name = "subcategory 12", iconName = "",
                colorName = CategoryColors.Olive.name.name
            ),

            CategoryEntity(
                id = 15, type = '-', rank = 's', orderNum = 1, parentCategoryId = 2,
                name = "subcategory 21", iconName = "",
                colorName = CategoryColors.Camel.name.name
            ),
            CategoryEntity(
                id = 16, type = '-', rank = 's', orderNum = 1, parentCategoryId = 2,
                name = "subcategory 22", iconName = "",
                colorName = CategoryColors.Camel.name.name
            ),
            CategoryEntity(
                id = 17, type = '-', rank = 's', orderNum = 3, parentCategoryId = 2,
                name = "subcategory 23", iconName = "",
                colorName = CategoryColors.Camel.name.name
            ),
            CategoryEntity(
                id = 18, type = '-', rank = 's', orderNum = 4, parentCategoryId = 2,
                name = "subcategory 24", iconName = "",
                colorName = CategoryColors.Camel.name.name
            ),
            CategoryEntity(
                id = 19, type = '-', rank = 's', orderNum = 5, parentCategoryId = 2,
                name = "subcategory 25", iconName = "",
                colorName = CategoryColors.Camel.name.name
            ),
            CategoryEntity(
                id = 20, type = '-', rank = 's', orderNum = 6, parentCategoryId = 2,
                name = "subcategory 26", iconName = "",
                colorName = CategoryColors.Camel.name.name
            )
        )

        Assert.assertThrows(IllegalAccessException::class.java) {
            categoryList.breakOnDifferentLists()
        }
    }*/

    @Test
    fun testFixCategoriesOrderNumbers() {
        val currentCategoryList = listOf(
            CategoryEntity(
                id = 1, type = '-', orderNum = 1, parentCategoryId = null,
                name = "category 1", iconName = "",
                colorName = CategoryColors.Olive.name.name
            ),
            CategoryEntity(
                id = 2, type = '-', orderNum = 1, parentCategoryId = null,
                name = "category 2", iconName = "",
                colorName = CategoryColors.Camel.name.name
            ),

            CategoryEntity(
                id = 13, type = '-', orderNum = 3, parentCategoryId = 1,
                name = "subcategory 11", iconName = "",
                colorName = CategoryColors.Olive.name.name
            ),
            CategoryEntity(
                id = 14, type = '-', orderNum = 2, parentCategoryId = 1,
                name = "subcategory 12", iconName = "",
                colorName = CategoryColors.Olive.name.name
            ),

            CategoryEntity(
                id = 15, type = '-', orderNum = 1, parentCategoryId = 2,
                name = "subcategory 21", iconName = "",
                colorName = CategoryColors.Camel.name.name
            ),
            CategoryEntity(
                id = 16, type = '-', orderNum = 1, parentCategoryId = 2,
                name = "subcategory 22", iconName = "",
                colorName = CategoryColors.Camel.name.name
            ),
            CategoryEntity(
                id = 17, type = '-', orderNum = 3, parentCategoryId = 2,
                name = "subcategory 23", iconName = "",
                colorName = CategoryColors.Camel.name.name
            ),

            CategoryEntity(
                id = 3, type = '+', orderNum = 1, parentCategoryId = null,
                name = "category 3", iconName = "",
                colorName = CategoryColors.Camel.name.name
            ),

            CategoryEntity(
                id = 31, type = '+', orderNum = 4, parentCategoryId = 3,
                name = "category 31", iconName = "",
                colorName = CategoryColors.Camel.name.name
            ),
            CategoryEntity(
                id = 32, type = '+', orderNum = 4, parentCategoryId = 3,
                name = "category 32", iconName = "",
                colorName = CategoryColors.Camel.name.name
            ),
        )
        val expectedCategoryList = listOf(
            CategoryEntity(
                id = 1, type = '-', orderNum = 1, parentCategoryId = null,
                name = "category 1", iconName = "",
                colorName = CategoryColors.Olive.name.name
            ),
            CategoryEntity(
                id = 2, type = '-', orderNum = 2, parentCategoryId = null,
                name = "category 2", iconName = "",
                colorName = CategoryColors.Camel.name.name
            ),

            CategoryEntity(
                id = 13, type = '-', orderNum = 1, parentCategoryId = 1,
                name = "subcategory 11", iconName = "",
                colorName = CategoryColors.Olive.name.name
            ),
            CategoryEntity(
                id = 14, type = '-', orderNum = 2, parentCategoryId = 1,
                name = "subcategory 12", iconName = "",
                colorName = CategoryColors.Olive.name.name
            ),

            CategoryEntity(
                id = 15, type = '-', orderNum = 1, parentCategoryId = 2,
                name = "subcategory 21", iconName = "",
                colorName = CategoryColors.Camel.name.name
            ),
            CategoryEntity(
                id = 16, type = '-', orderNum = 2, parentCategoryId = 2,
                name = "subcategory 22", iconName = "",
                colorName = CategoryColors.Camel.name.name
            ),
            CategoryEntity(
                id = 17, type = '-', orderNum = 3, parentCategoryId = 2,
                name = "subcategory 23", iconName = "",
                colorName = CategoryColors.Camel.name.name
            ),

            CategoryEntity(
                id = 3, type = '+', orderNum = 1, parentCategoryId = null,
                name = "category 3", iconName = "",
                colorName = CategoryColors.Camel.name.name
            ),

            CategoryEntity(
                id = 31, type = '+', orderNum = 1, parentCategoryId = 3,
                name = "category 31", iconName = "",
                colorName = CategoryColors.Camel.name.name
            ),
            CategoryEntity(
                id = 32, type = '+', orderNum = 2, parentCategoryId = 3,
                name = "category 32", iconName = "",
                colorName = CategoryColors.Camel.name.name
            ),
        )

        val fixedCategoryList = currentCategoryList.fixOrderNumbers()
        Assertions.assertArrayEquals(
            expectedCategoryList.sortedBy { it.id }.toTypedArray(),
            fixedCategoryList.sortedBy { it.id }.toTypedArray()
        )
    }

}
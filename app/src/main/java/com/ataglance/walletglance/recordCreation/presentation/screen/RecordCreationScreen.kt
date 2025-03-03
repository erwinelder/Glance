package com.ataglance.walletglance.recordCreation.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.model.AccountsAndActiveOne
import com.ataglance.walletglance.account.domain.model.color.AccountColors
import com.ataglance.walletglance.account.presentation.components.AccountPopupPicker
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.category.domain.model.CategoryWithSub
import com.ataglance.walletglance.category.domain.model.DefaultCategoriesPackage
import com.ataglance.walletglance.category.domain.model.GroupedCategoriesByType
import com.ataglance.walletglance.category.presentation.components.CategoryPicker
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.domain.date.DateTimeState
import com.ataglance.walletglance.core.presentation.components.buttons.AddNewItemButton
import com.ataglance.walletglance.core.presentation.components.fields.DateField
import com.ataglance.walletglance.core.presentation.components.fields.FieldWithLabel
import com.ataglance.walletglance.core.presentation.components.pickers.CustomDatePicker
import com.ataglance.walletglance.core.presentation.components.pickers.CustomTimePicker
import com.ataglance.walletglance.core.presentation.components.screenContainers.GlassSurfaceScreenContainer
import com.ataglance.walletglance.core.presentation.components.screenContainers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.recordCreation.presentation.components.RecordCreationBottomButtonsBlock
import com.ataglance.walletglance.recordCreation.presentation.components.RecordCreationTopBar
import com.ataglance.walletglance.recordCreation.presentation.components.RecordItemCreationComponent
import com.ataglance.walletglance.recordCreation.presentation.model.record.RecordDraft
import com.ataglance.walletglance.recordCreation.presentation.model.record.RecordDraftGeneral
import com.ataglance.walletglance.recordCreation.presentation.model.record.RecordDraftItem

@Composable
fun RecordCreationScreen(
    recordDraftGeneral: RecordDraftGeneral,
    recordDraftItems: List<RecordDraftItem>,
    savingIsAllowed: Boolean,
    accountList: List<Account>,
    groupedCategoriesByType: GroupedCategoriesByType,

    onSelectCategoryType: (CategoryType) -> Unit,
    onNavigateToTransferCreationScreen: () -> Unit,
    onIncludeInBudgetsChange: (Boolean) -> Unit,
    onSelectDate: (Long) -> Unit,
    onSelectTime: (Int, Int) -> Unit,

    onToggleAccounts: (List<Account>) -> Unit,
    onSelectAccount: (Account) -> Unit,
    onDimBackgroundChange: (Boolean) -> Unit,
    onAmountChange: (Int, String) -> Unit,
    onSelectCategory: (Int, CategoryWithSub) -> Unit,
    onNoteChange: (Int, String) -> Unit,
    onQuantityChange: (Int, String) -> Unit,
    onSwapItems: (Int, Int) -> Unit,
    onDeleteItem: (Int) -> Unit,
    onCollapsedChange: (Int, Boolean) -> Unit,
    onAddDraftItemButton: () -> Unit,

    onSaveButton: () -> Unit,
    onRepeatButton: () -> Unit,
    onDeleteButton: () -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var showCategoryPicker by remember { mutableStateOf(false) }

    var selectedItemIndex by remember { mutableStateOf<Int?>(null) }

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier.fillMaxSize()
    ) {
        GlassSurfaceScreenContainer(
            topBar = {
                RecordCreationTopBar(
                    showCategoryTypeButton = recordDraftGeneral.isNew,
                    currentCategoryType = recordDraftGeneral.type,
                    onSelectCategoryType = onSelectCategoryType,
                    showTransferButton = recordDraftGeneral.isNew && accountList.size > 1,
                    onNavigateToTransferCreationScreen = onNavigateToTransferCreationScreen,
                    preferences = recordDraftGeneral.preferences,
                    onIncludeInBudgetsChange = onIncludeInBudgetsChange
                )
            },
            glassSurfaceContent = {
                GlassSurfaceContent(
                    recordDraftGeneral = recordDraftGeneral,
                    recordDraftItems = recordDraftItems,
                    accountList = accountList,
                    onDateFieldClick = {
                        showDatePicker = true
                    },
                    onToggleAccounts = onToggleAccounts,
                    onSelectAccount = onSelectAccount,
                    onAmountChange = onAmountChange,
                    onCategoryFieldClick = { index ->
                        selectedItemIndex = index
                        showCategoryPicker = true
                    },
                    onNoteChange = onNoteChange,
                    onQuantityChange = onQuantityChange,
                    onDimBackgroundChange = onDimBackgroundChange,
                    onSwapItems = onSwapItems,
                    onDeleteItem = onDeleteItem,
                    onCollapsedChange = onCollapsedChange,
                    onAddDraftItemButton = onAddDraftItemButton
                )
            },
            glassSurfaceFilledWidths = FilledWidthByScreenType(compact = .96f),
            primaryBottomButton = {
                RecordCreationBottomButtonsBlock(
                    showOnlySaveButton = recordDraftGeneral.isNew,
                    singlePrimaryButtonStringRes = R.string.save_record,
                    onSaveButton = onSaveButton,
                    onRepeatButton = onRepeatButton,
                    onDeleteButton = onDeleteButton,
                    savingAndRepeatingAreAllowed = savingIsAllowed
                )
            }
        )
        CustomDatePicker(
            openDialog = showDatePicker,
            initialTimeInMillis = recordDraftGeneral.dateTimeState.getTimeInMillis(),
            onOpenDateDialogChange = { showDatePicker = it },
            onConfirmButton = { timeInMillis ->
                onSelectDate(timeInMillis)
                showDatePicker = false
                showTimePicker = true
            }
        )
        CustomTimePicker(
            openDialog = showTimePicker,
            initialHourAndMinute = recordDraftGeneral.dateTimeState.getHourAndMinute(),
            onOpenDialogChange = { showTimePicker = it },
            onConfirmButton = { hour, minute ->
                onSelectTime(hour, minute)
                showTimePicker = false
            }
        )
        CategoryPicker(
            visible = showCategoryPicker,
            groupedCategoriesByType = groupedCategoriesByType,
            type = recordDraftGeneral.type,
            onDismissRequest = { showCategoryPicker = false },
            onCategoryChoose = {
                selectedItemIndex?.let { index ->
                    onSelectCategory(index, it)
                }
            }
        )
    }
}

@Composable
private fun GlassSurfaceContent(
    recordDraftGeneral: RecordDraftGeneral,
    recordDraftItems: List<RecordDraftItem>,
    accountList: List<Account>,
    onDateFieldClick: () -> Unit,
    onToggleAccounts: (List<Account>) -> Unit,
    onSelectAccount: (Account) -> Unit,
    onAmountChange: (Int, String) -> Unit,
    onCategoryFieldClick: (Int) -> Unit,
    onNoteChange: (Int, String) -> Unit,
    onQuantityChange: (Int, String) -> Unit,
    onDimBackgroundChange: (Boolean) -> Unit,
    onSwapItems: (Int, Int) -> Unit,
    onDeleteItem: (Int) -> Unit,
    onCollapsedChange: (Int, Boolean) -> Unit,
    onAddDraftItemButton: () -> Unit
) {
    val lazyListState: LazyListState = rememberLazyListState()

    LazyColumn(
        state = lazyListState,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 18.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        item {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                DateField(
                    formattedDate = recordDraftGeneral.dateTimeState.dateFormatted,
                    onClick = onDateFieldClick
                )
                FieldWithLabel(labelText = stringResource(R.string.account)) {
                    AccountPopupPicker(
                        accountList = accountList,
                        selectedAccount = recordDraftGeneral.account,
                        onToggleAccounts = onToggleAccounts,
                        onSelectAccount = onSelectAccount,
                        onDimBackgroundChange = onDimBackgroundChange
                    )
                }
            }
        }
        items(items = recordDraftItems, key = { it.lazyListKey }) { item ->
            RecordItemCreationComponent(
                recordDraftItem = item,
                accountCurrency = recordDraftGeneral.account?.currency,
                onAmountChange = {
                    onAmountChange(item.index, it)
                },
                onCategoryFieldClick = {
                    onCategoryFieldClick(item.index)
                },
                onNoteChange = {
                    onNoteChange(item.index, it)
                },
                onQuantityChange = {
                    onQuantityChange(item.index, it)
                },
                draftLastItemIndex = recordDraftItems.lastIndex,
                onSwapItems = onSwapItems,
                onDeleteItem = onDeleteItem,
                onCollapsedChange = {
                    onCollapsedChange(item.index, it)
                }
            )
        }
        item {
            AddNewItemButton(
                modifier = Modifier.animateItem(),
                onClick = onAddDraftItemButton
            )
        }
    }
}



@Preview(
    device = Devices.PIXEL_7_PRO,
    locale = "en"
)
@Composable
fun RecordCreationScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault,
    accountsAndActiveOne: AccountsAndActiveOne = AccountsAndActiveOne(
        accounts = listOf(
            Account(
                id = 1, color = AccountColors.Pink,
                name = "Main account",
                isActive = true
            ),
            Account(
                id = 2, color = AccountColors.Blue,
                name = "Czech Local Card", currency = "CZK",
                isActive = false
            )
        ),
        activeAccount = Account(
            id = 1, color = AccountColors.Pink,
            name = "Main account",
            isActive = true
        )
    ),
    groupedCategoriesByType: GroupedCategoriesByType = DefaultCategoriesPackage(
        LocalContext.current
    ).getDefaultCategories(),
    recordDraft: RecordDraft = RecordDraft(
        general = RecordDraftGeneral(
            isNew = true,
            recordNum = 1,
            account = accountsAndActiveOne.activeAccount,
            type = CategoryType.Expense,
            dateTimeState = DateTimeState.fromCurrentTime()
        ),
        items = listOf(
            RecordDraftItem(
                lazyListKey = 0,
                index = 0,
                categoryWithSub = groupedCategoriesByType.expense[0]
                    .getWithFirstSubcategory(),
                note = "",
                amount = "42.43",
                quantity = "2",
                collapsed = false
            )
        )
    )
) {

    PreviewWithMainScaffoldContainer(appTheme = appTheme) {
        RecordCreationScreen(
            recordDraftGeneral = recordDraft.general,
            recordDraftItems = recordDraft.items,
            savingIsAllowed = true,
            accountList = accountsAndActiveOne.accounts,
            groupedCategoriesByType = groupedCategoriesByType,
            onSelectCategoryType = {},
            onNavigateToTransferCreationScreen = {},
            onIncludeInBudgetsChange = {},
            onSelectDate = {},
            onSelectTime = { _, _ -> },
            onToggleAccounts = {},
            onSelectAccount = {},
            onDimBackgroundChange = {},
            onAmountChange = { _, _ -> },
            onSelectCategory = { _, _ -> },
            onNoteChange = { _, _ -> },
            onQuantityChange = { _, _ -> },
            onSwapItems = { _, _ -> },
            onDeleteItem = {},
            onCollapsedChange = { _, _ -> },
            onAddDraftItemButton = {},
            onSaveButton = {},
            onRepeatButton = {},
            onDeleteButton = {}
        )
    }
}

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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import androidx.navigation.toRoute
import com.ataglance.walletglance.R
import com.ataglance.walletglance.account.domain.model.Account
import com.ataglance.walletglance.account.domain.model.AccountsAndActiveOne
import com.ataglance.walletglance.account.domain.model.color.AccountColors
import com.ataglance.walletglance.category.domain.model.CategoryType
import com.ataglance.walletglance.category.domain.model.CategoryWithSub
import com.ataglance.walletglance.category.domain.model.DefaultCategoriesPackage
import com.ataglance.walletglance.category.domain.model.GroupedCategoriesByType
import com.ataglance.walletglance.category.presentation.component.CategoryPicker
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.app.DrawableResByTheme
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.domain.date.DateTimeState
import com.ataglance.walletglance.core.domain.navigation.MainScreens
import com.ataglance.walletglance.core.presentation.component.button.AddNewItemButton
import com.ataglance.walletglance.core.presentation.component.container.KeyboardTypingAnimatedVisibilityContainer
import com.ataglance.walletglance.core.presentation.component.container.KeyboardTypingAnimatedVisibilitySpacer
import com.ataglance.walletglance.core.presentation.component.picker.CustomDatePicker
import com.ataglance.walletglance.core.presentation.component.picker.CustomTimePicker
import com.ataglance.walletglance.core.presentation.component.screenContainer.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.component.screenContainer.ScreenContainerWithTopBackNavButton
import com.ataglance.walletglance.core.presentation.theme.CurrAppTheme
import com.ataglance.walletglance.core.presentation.theme.CurrWindowType
import com.ataglance.walletglance.recordCreation.presentation.component.RecordCreationBottomButtonsBlock
import com.ataglance.walletglance.recordCreation.presentation.component.RecordCreationTopBar
import com.ataglance.walletglance.recordCreation.presentation.component.RecordItemCreationComponent
import com.ataglance.walletglance.recordCreation.presentation.model.record.RecordDraft
import com.ataglance.walletglance.recordCreation.presentation.model.record.RecordDraftGeneral
import com.ataglance.walletglance.recordCreation.presentation.model.record.RecordDraftItem
import com.ataglance.walletglance.recordCreation.presentation.viewmodel.RecordCreationViewModel
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun RecordCreationScreenWrapper(
    screenPadding: PaddingValues,
    backStack: NavBackStackEntry,
    navController: NavHostController,
    onDimBackgroundChange: (Boolean) -> Unit
) {
    val recordNum = backStack.toRoute<MainScreens.RecordCreation>().recordNum

    val viewModel = koinViewModel<RecordCreationViewModel> {
        parametersOf(recordNum)
    }

    val recordDraftGeneral by viewModel.recordDraftGeneral.collectAsStateWithLifecycle()
    val recordDraftItems by viewModel.recordDraftItems.collectAsStateWithLifecycle()
    val savingIsAllowed by viewModel.savingIsAllowed.collectAsStateWithLifecycle()
    val accounts by viewModel.accounts.collectAsStateWithLifecycle()
    val groupedCategories by viewModel.groupedCategoriesByType.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    RecordCreationScreen(
        screenPadding = screenPadding,
        onNavigateBack = navController::popBackStack,
        recordDraftGeneral = recordDraftGeneral,
        recordDraftItems = recordDraftItems,
        savingIsAllowed = savingIsAllowed,
        accounts = accounts,
        groupedCategoriesByType = groupedCategories,

        onSelectCategoryType = viewModel::selectCategoryType,
        onIncludeInBudgetsChange = viewModel::changeIncludeInBudgets,
        onSelectDate = viewModel::selectDate,
        onSelectTime = viewModel::selectTime,

        onToggleAccounts = viewModel::toggleSelectedAccount,
        onSelectAccount = viewModel::selectAccount,
        onDimBackgroundChange = onDimBackgroundChange,
        onAmountChange = viewModel::changeAmount,
        onSelectCategory = viewModel::selectCategory,
        onNoteChange = viewModel::changeNote,
        onQuantityChange = viewModel::changeQuantity,
        onSwapItems = viewModel::swapDraftItems,
        onDeleteItem = viewModel::deleteDraftItem,
        onCollapsedChange = viewModel::changeCollapsed,
        onAddDraftItemButton = viewModel::addNewDraftItem,

        onSaveButton = {
            coroutineScope.launch {
                viewModel.saveRecord()
                navController.popBackStack()
            }
        },
        onRepeatButton = {
            coroutineScope.launch {
                viewModel.repeatRecord()
                navController.popBackStack()
            }
        },
        onDeleteButton = {
            coroutineScope.launch {
                viewModel.deleteRecord()
                navController.popBackStack()
            }
        }
    )
}

@Composable
fun RecordCreationScreen(
    screenPadding: PaddingValues = PaddingValues(0.dp),
    onNavigateBack: () -> Unit,
    recordDraftGeneral: RecordDraftGeneral,
    recordDraftItems: List<RecordDraftItem>,
    savingIsAllowed: Boolean,
    accounts: List<Account>,
    groupedCategoriesByType: GroupedCategoriesByType,

    onSelectCategoryType: (CategoryType) -> Unit,
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
    val backNavButtonImageRes = DrawableResByTheme(
        lightDefault = R.drawable.create_record_light_default,
        darkDefault = R.drawable.create_record_dark_default
    ).getByTheme(CurrAppTheme)

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var showCategoryPicker by remember { mutableStateOf(false) }

    var selectedItemIndex by remember { mutableStateOf<Int?>(null) }

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier.fillMaxSize()
    ) {
        ScreenContainerWithTopBackNavButton(
            screenPadding = screenPadding,
            backNavButtonText = stringResource(R.string.create_record),
            backNavButtonImageRes = backNavButtonImageRes,
            onBackNavButtonClick = onNavigateBack
        ) { keyboardInFocus ->

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
            ) {
                KeyboardTypingAnimatedVisibilityContainer(isVisible = !keyboardInFocus) {
                    RecordCreationTopBar(
                        draftGeneral = recordDraftGeneral,
                        accounts = accounts,
                        onSelectCategoryType = onSelectCategoryType,
                        onIncludeInBudgetsChange = onIncludeInBudgetsChange,
                        onDateFieldClick = { showDatePicker = true },
                        onToggleAccounts = onToggleAccounts,
                        onSelectAccount = onSelectAccount,
                        onDimBackgroundChange = onDimBackgroundChange,
                    )
                }
                KeyboardTypingAnimatedVisibilitySpacer(isVisible = !keyboardInFocus, height = 16.dp)
                RecordCreationScreenContent(
                    recordDraftItems = recordDraftItems,
                    selectedAccount = recordDraftGeneral.account,
                    onAmountChange = onAmountChange,
                    onCategoryFieldClick = { index ->
                        selectedItemIndex = index
                        showCategoryPicker = true
                    },
                    onNoteChange = onNoteChange,
                    onQuantityChange = onQuantityChange,
                    onSwapItems = onSwapItems,
                    onDeleteItem = onDeleteItem,
                    onCollapsedChange = onCollapsedChange,
                    onAddDraftItemButton = onAddDraftItemButton
                )
            }

            KeyboardTypingAnimatedVisibilitySpacer(isVisible = !keyboardInFocus, height = 24.dp)

            KeyboardTypingAnimatedVisibilityContainer(isVisible = !keyboardInFocus) {
                RecordCreationBottomButtonsBlock(
                    showOnlySaveButton = recordDraftGeneral.isNew,
                    singlePrimaryButtonStringRes = R.string.save_record,
                    onSaveButton = onSaveButton,
                    onRepeatButton = onRepeatButton,
                    onDeleteButton = onDeleteButton,
                    savingAndRepeatingAreAllowed = savingIsAllowed
                )
            }

        }
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
private fun RecordCreationScreenContent(
    recordDraftItems: List<RecordDraftItem>,
    selectedAccount: Account?,
    onAmountChange: (Int, String) -> Unit,
    onCategoryFieldClick: (Int) -> Unit,
    onNoteChange: (Int, String) -> Unit,
    onQuantityChange: (Int, String) -> Unit,
    onSwapItems: (Int, Int) -> Unit,
    onDeleteItem: (Int) -> Unit,
    onCollapsedChange: (Int, Boolean) -> Unit,
    onAddDraftItemButton: () -> Unit
) {
    val lazyListState: LazyListState = rememberLazyListState()

    LazyColumn(
        state = lazyListState,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp),
        modifier = Modifier
            .fillMaxWidth(FilledWidthByScreenType().getByType(CurrWindowType))
    ) {
        items(items = recordDraftItems, key = { it.lazyListKey }) { item ->
            RecordItemCreationComponent(
                recordDraftItem = item,
                accountCurrency = selectedAccount?.currency,
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



@Preview(device = Devices.PIXEL_7_PRO, locale = "en")
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
            recordDraftGeneral = RecordDraftGeneral(
                isNew = true,
                type = recordDraft.general.type,
                dateTimeState = recordDraft.general.dateTimeState,
                account = recordDraft.general.account
            ),
            onNavigateBack = {},
            recordDraftItems = recordDraft.items,
            savingIsAllowed = true,
            accounts = accountsAndActiveOne.accounts,
            groupedCategoriesByType = groupedCategoriesByType,

            onSelectCategoryType = {},
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

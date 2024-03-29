package com.ataglance.walletglance.ui.theme.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ataglance.walletglance.R
import com.ataglance.walletglance.data.Account
import com.ataglance.walletglance.data.Category
import com.ataglance.walletglance.model.CategoriesUiState
import com.ataglance.walletglance.model.CategoryType
import com.ataglance.walletglance.model.MakeRecordStatus
import com.ataglance.walletglance.model.MakeRecordUiState
import com.ataglance.walletglance.model.MakeRecordUnitUiState
import com.ataglance.walletglance.model.MakeRecordViewModel
import com.ataglance.walletglance.model.RecordType
import com.ataglance.walletglance.ui.theme.GlanceTheme
import com.ataglance.walletglance.ui.theme.animation.bounceClickEffect
import com.ataglance.walletglance.ui.theme.theme.AppTheme
import com.ataglance.walletglance.ui.theme.uielements.accounts.SmallAccount
import com.ataglance.walletglance.ui.theme.uielements.buttons.BarButton
import com.ataglance.walletglance.ui.theme.uielements.buttons.MakeRecordBottomButtonBlock
import com.ataglance.walletglance.ui.theme.uielements.containers.GlassSurface
import com.ataglance.walletglance.ui.theme.uielements.dividers.BigDivider
import com.ataglance.walletglance.ui.theme.uielements.dividers.SmallDivider
import com.ataglance.walletglance.ui.theme.uielements.fields.CategoryField
import com.ataglance.walletglance.ui.theme.uielements.fields.CustomTextField
import com.ataglance.walletglance.ui.theme.uielements.fields.DateField
import com.ataglance.walletglance.ui.theme.uielements.fields.MakeRecordFieldContainer
import com.ataglance.walletglance.ui.theme.uielements.pickers.AccountPicker
import com.ataglance.walletglance.ui.theme.uielements.pickers.CategoryPicker
import com.ataglance.walletglance.ui.theme.uielements.pickers.CustomDatePicker
import com.ataglance.walletglance.ui.theme.uielements.pickers.CustomTimePicker
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MakeRecordScreen(
    appTheme: AppTheme?,
    viewModel: MakeRecordViewModel,
    makeRecordStatus: String,
    accountList: List<Account>,
    categoriesUiState: CategoriesUiState,
    categoryNameAndIconMap: Map<String, Int>,
    onMakeTransferButtonClick: () -> Unit,
    onSaveButton: (MakeRecordUiState, List<MakeRecordUnitUiState>) -> Unit,
    onRepeatButton: (MakeRecordUiState, List<MakeRecordUnitUiState>) -> Unit,
    onDeleteButton: (Int) -> Unit
) {
    val fieldsCornerSize = 15.dp
    val lazyListState: LazyListState = rememberLazyListState()

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val recordUnitList by viewModel.recordUnitList.collectAsStateWithLifecycle()
    val savingIsAllowed = recordUnitList.none { recordUnit ->
        recordUnit.amount.isBlank() ||
                recordUnit.amount.last() == '.' ||
                recordUnit.amount.toDouble() == 0.0 ||
                recordUnit.category == null
    } && uiState.account != null

    val openDateDialog = remember { mutableStateOf(false) }
    val openTimeDialog = remember { mutableStateOf(false) }
    val openAccountDialog = remember { mutableStateOf(false) }
    val openCategoryDialog = remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = uiState.dateTimeState.calendar.timeInMillis
    )
    val timePickerState = rememberTimePickerState(
        initialHour = uiState.dateTimeState.calendar.get(Calendar.HOUR_OF_DAY),
        initialMinute = uiState.dateTimeState.calendar.get(Calendar.MINUTE),
        is24Hour = true
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.buttons_gap)),
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = dimensionResource(R.dimen.screen_vertical_padding))
        ) {

            if (makeRecordStatus == MakeRecordStatus.Create.name) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    BarButton(
                        onClick = onMakeTransferButtonClick,
                        active = false,
                        text = stringResource(R.string.transfer)
                    )
                    BarButton(
                        onClick = {
                            viewModel.changeRecordType(RecordType.Expense, categoriesUiState)
                        },
                        active = uiState.type == RecordType.Expense,
                        text = stringResource(R.string.expense)
                    )
                    BarButton(
                        onClick = {
                            viewModel.changeRecordType(RecordType.Income, categoriesUiState)
                        },
                        active = uiState.type == RecordType.Income,
                        text = stringResource(R.string.income_singular)
                    )
                }
            }

            GlassSurface(modifier = Modifier.weight(1f)) {
                LazyColumn(
                    state = lazyListState,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(12.dp, 18.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        DateField(
                            dateFormatted = uiState.dateTimeState.dateFormatted,
                            cornerSize = fieldsCornerSize,
                            onClick = { openDateDialog.value = true }
                        )
                    }
                    item {
                        MakeRecordFieldContainer(R.string.account) {
                            AnimatedContent(
                                targetState = uiState.account,
                                label = "account field at the make record screen"
                            ) { targetAccount ->
                                SmallAccount(targetAccount, appTheme) {
                                    openAccountDialog.value = true
                                }
                            }
                        }
                    }
                    item {
                        BigDivider(Modifier.padding(top = 4.dp))
                    }
                    items(items = recordUnitList, key = { it.index }) { recordUnit ->
                        RecordUnitBlock(
                            noteText = recordUnit.note,
                            onNoteValueChange = { value ->
                                viewModel.changeNoteValue(recordUnit.index, value)
                            },
                            categoryToShow = recordUnit.subcategory ?: recordUnit.category,
                            categoryIconRes = categoryNameAndIconMap[
                                recordUnit.subcategory?.iconName ?: recordUnit.category?.iconName
                            ],
                            onCategoryClick = {
                                viewModel.changeClickedUnitIndex(recordUnit.index)
                                openCategoryDialog.value = true
                            },
                            amount = recordUnit.amount,
                            onAmountValueChange = { value ->
                                viewModel.changeAmountValue(recordUnit.index, value)
                            },
                            quantity = recordUnit.quantity,
                            onQuantityChange = { value ->
                                viewModel.changeQuantityValue(recordUnit.index, value)
                            },
                            index = recordUnit.index,
                            lastIndex = recordUnitList.last().index,
                            onSwapUnits = viewModel::swapRecordUnits,
                            onDeleteButton = viewModel::deleteRecordUnit,
                            fieldsCornerSize = fieldsCornerSize,
                        )
                        SmallDivider(Modifier.padding(top = 24.dp, bottom = 8.dp))
                    }
                    item {
                        AddNewRecordUnit(viewModel::addNewRecordUnit)
                    }
                }
            }

            MakeRecordBottomButtonBlock(
                showSingleButton = makeRecordStatus == MakeRecordStatus.Create.name,
                singlePrimaryButtonStringRes = R.string.save_record,
                onSaveButton = { onSaveButton(uiState, recordUnitList) },
                onRepeatButton = { onRepeatButton(uiState, recordUnitList) },
                onDeleteButton = {
                    uiState.recordNum?.let { onDeleteButton(it) }
                },
                buttonsAreEnabled = savingIsAllowed
            )

        }
        CustomDatePicker(
            openDialog = openDateDialog.value,
            onOpenDateDialogChange = { openDateDialog.value = it },
            onConfirmButton = {
                datePickerState.selectedDateMillis?.let { viewModel.selectNewDate(it) }
                openDateDialog.value = false
                openTimeDialog.value = true
            },
            state = datePickerState
        )
        CustomTimePicker(
            openDialog = openTimeDialog.value,
            onOpenTimeDialogChange = { openTimeDialog.value = it },
            onConfirmButton = {
                viewModel.selectNewTime(timePickerState.hour, timePickerState.minute)
                openTimeDialog.value = false
            },
            state = timePickerState
        )
        AccountPicker(
            visible = openAccountDialog.value,
            accountList = accountList,
            appTheme = appTheme,
            onDismissRequest = { openAccountDialog.value = false },
            onAccountChoose = viewModel::chooseAccount
        )
        CategoryPicker(
            visible = openCategoryDialog.value,
            categoriesUiState = categoriesUiState,
            categoryNameAndIconMap = categoryNameAndIconMap,
            type = if (uiState.type == RecordType.Expense) CategoryType.Expense
                else CategoryType.Income,
            onDismissRequest = { openCategoryDialog.value = false },
            onCategoryChoose = viewModel::chooseCategory
        )
    }
}

@Composable
private fun RecordUnitBlock(
    noteText: String,
    onNoteValueChange: (String) -> Unit,
    categoryToShow: Category?,
    categoryIconRes: Int?,
    onCategoryClick: () -> Unit,
    amount: String,
    onAmountValueChange: (String) -> Unit,
    quantity: String,
    onQuantityChange: (String) -> Unit,
    index: Int,
    lastIndex: Int,
    onSwapUnits: (Int, Int) -> Unit,
    onDeleteButton: (Int) -> Unit,
    fieldsCornerSize: Dp,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        MakeRecordFieldContainer(R.string.note) {
            CustomTextField(
                text = noteText,
                placeholderText = stringResource(R.string.note_placeholder),
                fontSize = 18.sp,
                cornerSize = fieldsCornerSize,
                onValueChange = onNoteValueChange,
                modifier = Modifier.bounceClickEffect(.97f)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        MakeRecordFieldContainer(R.string.category) {
            AnimatedContent(
                targetState = categoryToShow to categoryIconRes,
                label = "category field at the make record screen"
            ) { targetCategoryAndIconRes ->
                CategoryField(
                    category = targetCategoryAndIconRes.first,
                    categoryIconRes = targetCategoryAndIconRes.second,
                    fontSize = 20.sp,
                    cornerSize = fieldsCornerSize,
                    onClick = onCategoryClick
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        MakeRecordFieldContainer(R.string.amount) {
            CustomTextField(
                text = amount,
                placeholderText = "0.00",
                fontSize = 22.sp,
                cornerSize = fieldsCornerSize,
                onValueChange = onAmountValueChange,
                keyboardType = KeyboardType.Number,
                modifier = Modifier.bounceClickEffect(.97f)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        MakeRecordFieldContainer(R.string.quantity) {
            CustomTextField(
                text = quantity,
                placeholderText = stringResource(R.string.quantity_placeholder),
                fontSize = 18.sp,
                cornerSize = fieldsCornerSize,
                onValueChange = onQuantityChange,
                keyboardType = KeyboardType.Number,
                modifier = Modifier.bounceClickEffect(.97f)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        ControlPanel(
            index = index,
            lastIndex = lastIndex,
            onSwapUnits = onSwapUnits,
            onDeleteButton = onDeleteButton
        )
    }
}

@Composable
private fun ControlPanelButton(
    onClick: () -> Unit,
    iconRes: Int,
    iconContentDescription: String,
    enabled: Boolean = true
) {
    val color by animateColorAsState(
        targetValue = if (enabled) {
            GlanceTheme.onSurface
        } else {
            GlanceTheme.outline
        },
        label = "make record unit control panel button color"
    )

    IconButton(
        onClick = onClick,
        enabled = enabled,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = Color.Transparent
        ),
        modifier = Modifier.bounceClickEffect(.98f)
    ) {
        Icon(
            painter = painterResource(iconRes),
            contentDescription = iconContentDescription,
            tint = color,
            modifier = Modifier
                .size(36.dp)
                .padding(horizontal = 4.dp)
        )
    }
}

@Composable
private fun ControlPanel(
    index: Int,
    lastIndex: Int,
    onSwapUnits: (Int, Int) -> Unit,
    onDeleteButton: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .bounceClickEffect(.98f)
            .clip(RoundedCornerShape(17.dp))
            .background(GlanceTheme.surface)
            .padding(horizontal = 4.dp)
    ) {
        ControlPanelButton(
            onClick = { onDeleteButton(index) },
            enabled = lastIndex != 0,
            iconRes = R.drawable.trash_icon,
            iconContentDescription = "delete"
        )
        ControlPanelButton(
            onClick = { onSwapUnits(index, index - 1) },
            enabled = index > 0,
            iconRes = R.drawable.short_arrow_up_icon,
            iconContentDescription = "move up"
        )
        ControlPanelButton(
            onClick = { onSwapUnits(index, index + 1) },
            enabled = index < lastIndex,
            iconRes = R.drawable.short_arrow_down_icon,
            iconContentDescription = "move down"
        )
    }
}

@Composable
private fun AddNewRecordUnit(onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = Color.Transparent
        ),
        modifier = Modifier
            .bounceClickEffect(.97f)
            .clip(RoundedCornerShape(22.dp))
            .border(2.dp, GlanceTheme.outline, RoundedCornerShape(22.dp))
            .padding(18.dp, 8.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.add_icon),
            contentDescription = "add",
            tint = GlanceTheme.outline,
            modifier = Modifier
                .size(26.dp)
        )
    }
}
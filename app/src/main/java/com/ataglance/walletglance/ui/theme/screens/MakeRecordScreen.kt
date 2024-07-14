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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ataglance.walletglance.R
import com.ataglance.walletglance.data.accounts.Account
import com.ataglance.walletglance.data.app.AppTheme
import com.ataglance.walletglance.data.categories.CategoriesWithSubcategories
import com.ataglance.walletglance.data.categories.Category
import com.ataglance.walletglance.data.categories.CategoryType
import com.ataglance.walletglance.data.records.MakeRecordStatus
import com.ataglance.walletglance.data.records.RecordType
import com.ataglance.walletglance.ui.theme.GlanceTheme
import com.ataglance.walletglance.ui.theme.animation.bounceClickEffect
import com.ataglance.walletglance.ui.theme.uielements.accounts.SmallAccount
import com.ataglance.walletglance.ui.theme.uielements.buttons.MakeRecordBottomButtonBlock
import com.ataglance.walletglance.ui.theme.uielements.buttons.SmallFilledIconButton
import com.ataglance.walletglance.ui.theme.uielements.categories.RecordCategory
import com.ataglance.walletglance.ui.theme.uielements.containers.GlassSurface
import com.ataglance.walletglance.ui.theme.uielements.containers.GlassSurfaceOnGlassSurface
import com.ataglance.walletglance.ui.theme.uielements.categories.CategoryField
import com.ataglance.walletglance.ui.theme.uielements.fields.CustomTextField
import com.ataglance.walletglance.ui.theme.uielements.fields.DateField
import com.ataglance.walletglance.ui.theme.uielements.fields.MakeRecordFieldContainer
import com.ataglance.walletglance.ui.theme.uielements.accounts.AccountPicker
import com.ataglance.walletglance.ui.theme.uielements.categories.CategoryPicker
import com.ataglance.walletglance.ui.theme.uielements.pickers.CustomDatePicker
import com.ataglance.walletglance.ui.theme.uielements.pickers.CustomTimePicker
import com.ataglance.walletglance.ui.theme.uielements.records.MakeRecordTypeBar
import com.ataglance.walletglance.ui.viewmodels.records.MakeRecordUiState
import com.ataglance.walletglance.ui.viewmodels.records.MakeRecordUnitUiState
import com.ataglance.walletglance.ui.viewmodels.records.MakeRecordViewModel
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MakeRecordScreen(
    appTheme: AppTheme?,
    viewModel: MakeRecordViewModel,
    makeRecordStatus: MakeRecordStatus,
    accountList: List<Account>,
    categoriesWithSubcategories: CategoriesWithSubcategories,
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
                recordUnit.categoryWithSubcategory == null
    } && uiState.account != null

    val openDateDialog = remember { mutableStateOf(false) }
    val openTimeDialog = remember { mutableStateOf(false) }
    val openAccountDialog = remember { mutableStateOf(false) }
    var openCategoryDialog by remember { mutableStateOf(false) }

    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = uiState.dateTimeState.calendar.timeInMillis
    )
    val timePickerState = rememberTimePickerState(
        initialHour = uiState.dateTimeState.calendar.get(Calendar.HOUR_OF_DAY),
        initialMinute = uiState.dateTimeState.calendar.get(Calendar.MINUTE),
        is24Hour = true
    )

    Box(
        contentAlignment = Alignment.BottomCenter,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.buttons_gap)),
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = dimensionResource(R.dimen.screen_vertical_padding))
        ) {

            if (makeRecordStatus == MakeRecordStatus.Create) {
                MakeRecordTypeBar(
                    isTransferButtonVisible = accountList.size > 1,
                    onMakeTransferButtonClick = onMakeTransferButtonClick,
                    currentRecordType = uiState.type,
                    onRecordTypeChange = {
                        viewModel.changeRecordType(it, categoriesWithSubcategories)
                    }
                )
            }

            GlassSurface(modifier = Modifier.weight(1f), filledWidth = .96f) {
                LazyColumn(
                    state = lazyListState,
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp),
                    contentPadding = PaddingValues(12.dp, 18.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    item {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                        ) {
                            DateField(
                                dateFormatted = uiState.dateTimeState.dateFormatted,
                                cornerSize = fieldsCornerSize,
                                onClick = { openDateDialog.value = true }
                            )
                            MakeRecordFieldContainer(R.string.account) {
                                AnimatedContent(
                                    targetState = uiState.account,
                                    label = "account field at the make record screen"
                                ) { targetAccount ->
                                    SmallAccount(targetAccount, appTheme) {
                                        if (accountList.size == 2) {
                                            uiState.account?.let { account ->
                                                viewModel.changeAccount(account, accountList)
                                            }
                                        } else if (accountList.size > 1) {
                                            openAccountDialog.value = true
                                        }
                                    }
                                }
                            }
                        }
                    }
                    items(items = recordUnitList, key = { it.lazyListKey }) { recordUnit ->
                        RecordUnitBlock(
                            recordUnitUiState = recordUnit,
                            onNoteValueChange = { value ->
                                viewModel.changeNoteValue(recordUnit.index, value)
                            },
                            categoryIconRes = recordUnit.getSubcategoryOrCategory()?.icon?.res,
                            onCategoryClick = {
                                viewModel.changeClickedUnitIndex(recordUnit.index)
                                openCategoryDialog = true
                            },
                            onAmountValueChange = { value ->
                                viewModel.changeAmountValue(recordUnit.index, value)
                            },
                            onQuantityChange = { value ->
                                viewModel.changeQuantityValue(recordUnit.index, value)
                            },
                            lastIndex = recordUnitList.last().index,
                            onSwapUnits = viewModel::swapRecordUnits,
                            onDeleteButton = viewModel::deleteRecordUnit,
                            fieldsCornerSize = fieldsCornerSize,
                            collapsed = recordUnit.collapsed,
                            onChangeCollapsedValue = { value ->
                                viewModel.changeCollapsedValue(recordUnit.index, value)
                            },
                            accountCurrency = uiState.account?.currency
                        )
                    }
                    item {
                        AddNewRecordUnitButton(viewModel::addNewRecordUnit)
                    }
                }
            }

            MakeRecordBottomButtonBlock(
                showSingleButton = makeRecordStatus == MakeRecordStatus.Create,
                singlePrimaryButtonStringRes = R.string.save_record,
                onSaveButton = { onSaveButton(uiState, recordUnitList) },
                onRepeatButton = { onRepeatButton(uiState, recordUnitList) },
                onDeleteButton = { onDeleteButton(uiState.recordNum) },
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
            visible = openCategoryDialog,
            categoriesWithSubcategories = categoriesWithSubcategories,
            type = if (uiState.type == RecordType.Expense) CategoryType.Expense
                else CategoryType.Income,
            onDismissRequest = { openCategoryDialog = false },
            onCategoryChoose = viewModel::chooseCategory
        )
    }
}

@Composable
private fun LazyItemScope.RecordUnitBlock(
    recordUnitUiState: MakeRecordUnitUiState,
    onNoteValueChange: (String) -> Unit,
    categoryIconRes: Int?,
    onCategoryClick: () -> Unit,
    onAmountValueChange: (String) -> Unit,
    onQuantityChange: (String) -> Unit,
    lastIndex: Int,
    onSwapUnits: (Int, Int) -> Unit,
    onDeleteButton: (Int) -> Unit,
    fieldsCornerSize: Dp,
    collapsed: Boolean,
    onChangeCollapsedValue: (Boolean) -> Unit,
    accountCurrency: String?
) {
    GlassSurfaceOnGlassSurface(
        modifier = Modifier.animateItem(placementSpec = null),
        paddingValues = PaddingValues(0.dp),
        enableClick = collapsed,
        onClick = { onChangeCollapsedValue(false) }
    ) {
        AnimatedContent(
            targetState = recordUnitUiState.collapsed,
            label = "record unit block"
        ) { targetCollapsed ->
            if (targetCollapsed) {
                RecordUnitBlockCollapsed(
                    noteText = recordUnitUiState.note,
                    category = recordUnitUiState.categoryWithSubcategory
                        ?.getSubcategoryOrCategory(),
                    amount = recordUnitUiState.getFormattedAmountWithSpaces(),
                    quantity = recordUnitUiState.quantity,
                    accountCurrency = accountCurrency,
                    index = recordUnitUiState.index,
                    lastIndex = lastIndex,
                    onSwapUnits = onSwapUnits,
                    onDeleteButton = onDeleteButton,
                    onExpandButton = { onChangeCollapsedValue(false) }
                )
            } else {
                RecordUnitBlockExpanded(
                    noteText = recordUnitUiState.note,
                    onNoteValueChange = onNoteValueChange,
                    category = recordUnitUiState.categoryWithSubcategory
                        ?.getSubcategoryOrCategory(),
                    categoryIconRes = categoryIconRes,
                    onCategoryClick = onCategoryClick,
                    amount = recordUnitUiState.amount,
                    onAmountValueChange = onAmountValueChange,
                    quantity = recordUnitUiState.quantity,
                    onQuantityChange = onQuantityChange,
                    index = recordUnitUiState.index,
                    lastIndex = lastIndex,
                    onSwapUnits = onSwapUnits,
                    onDeleteButton = onDeleteButton,
                    fieldsCornerSize = fieldsCornerSize,
                    onCollapseButton = { onChangeCollapsedValue(true) }
                )
            }
        }
    }
}

@Composable
private fun RecordUnitBlockCollapsed(
    noteText: String,
    category: Category?,
    amount: String,
    quantity: String,
    accountCurrency: String?,
    index: Int,
    lastIndex: Int,
    onSwapUnits: (Int, Int) -> Unit,
    onDeleteButton: (Int) -> Unit,
    onExpandButton: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        if (noteText.isNotBlank()) {
            Text(
                text = noteText,
                color = GlanceTheme.onSurface,
                fontSize = 18.sp,
                fontWeight = FontWeight.Light,
                fontStyle = FontStyle.Italic,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            if (quantity.isNotBlank()) {
                Text(
                    text = "$quantity x",
                    fontSize = 18.sp,
                    color = GlanceTheme.onSurface.copy(.6f),
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }
            Text(
                text = amount,
                fontSize = 22.sp,
                color = GlanceTheme.onSurface,
            )
            accountCurrency?.let {
                Text(
                    text = it,
                    fontSize = 18.sp,
                    color = GlanceTheme.onSurface.copy(.6f),
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        AnimatedContent(
            targetState = category,
            label = "record unit category"
        ) { targetCategory ->
            RecordCategory(
                category = targetCategory,
                iconSize = 24.dp,
                fontSize = 20.sp
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        ControlPanel(
            index = index,
            lastIndex = lastIndex,
            spaceSize = 12.dp,
            onSwapUnits = onSwapUnits,
            onDeleteButton = onDeleteButton
        ) {
            SmallFilledIconButton(
                iconRes = R.drawable.expand_icon,
                iconContendDescription = "expand record unit",
                onClick = onExpandButton
            )
        }
    }
}

@Composable
private fun RecordUnitBlockExpanded(
    noteText: String,
    onNoteValueChange: (String) -> Unit,
    category: Category?,
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
    onCollapseButton: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 22.dp)
    ) {
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
        MakeRecordFieldContainer(R.string.category) {
            AnimatedContent(
                targetState = category to categoryIconRes,
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
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            ControlPanel(
                index = index,
                lastIndex = lastIndex,
                spaceSize = 16.dp,
                onSwapUnits = onSwapUnits,
                onDeleteButton = onDeleteButton
            ) {
                SmallFilledIconButton(
                    iconRes = R.drawable.collapse_icon,
                    iconContendDescription = "collapse record unit",
                    onClick = onCollapseButton
                )
            }
        }
    }
}

@Composable
private fun ControlPanel(
    index: Int,
    lastIndex: Int,
    spaceSize: Dp,
    onSwapUnits: (Int, Int) -> Unit,
    onDeleteButton: (Int) -> Unit,
    collapseExpandButton: @Composable () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(spaceSize),
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(start = 3.dp)
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
        collapseExpandButton()
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
            modifier = Modifier.size(28.dp)
        )
    }
}

@Composable
private fun LazyItemScope.AddNewRecordUnitButton(onClick: () -> Unit) {
    IconButton(
        onClick = onClick,
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = Color.Transparent
        ),
        modifier = Modifier
            .animateItem(placementSpec = null)
            .width(250.dp)
            .bounceClickEffect(.97f)
            .clip(RoundedCornerShape(22.dp))
            .border(2.dp, GlanceTheme.outline, RoundedCornerShape(22.dp))
            .padding(18.dp, 8.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.add_icon),
            contentDescription = "add",
            tint = GlanceTheme.outline,
            modifier = Modifier.size(26.dp)
        )
    }
}
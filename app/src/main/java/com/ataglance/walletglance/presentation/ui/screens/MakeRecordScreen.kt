package com.ataglance.walletglance.presentation.ui.screens

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
import com.ataglance.walletglance.domain.accounts.Account
import com.ataglance.walletglance.domain.app.AppTheme
import com.ataglance.walletglance.domain.categories.CategoriesWithSubcategories
import com.ataglance.walletglance.domain.categories.CategoryType
import com.ataglance.walletglance.domain.makingRecord.MakeRecordStatus
import com.ataglance.walletglance.domain.makingRecord.MakeRecordUiState
import com.ataglance.walletglance.domain.makingRecord.MakeRecordUnitUiState
import com.ataglance.walletglance.domain.records.RecordType
import com.ataglance.walletglance.presentation.ui.GlanceTheme
import com.ataglance.walletglance.presentation.ui.WindowTypeIsCompact
import com.ataglance.walletglance.presentation.ui.animation.bounceClickEffect
import com.ataglance.walletglance.presentation.ui.uielements.accounts.AccountPopupPicker
import com.ataglance.walletglance.presentation.ui.uielements.buttons.MakeRecordBottomButtonBlock
import com.ataglance.walletglance.presentation.ui.uielements.buttons.SmallFilledIconButton
import com.ataglance.walletglance.presentation.ui.uielements.categories.CategoryField
import com.ataglance.walletglance.presentation.ui.uielements.categories.CategoryPicker
import com.ataglance.walletglance.presentation.ui.uielements.categories.RecordCategory
import com.ataglance.walletglance.presentation.ui.uielements.containers.GlassSurface
import com.ataglance.walletglance.presentation.ui.uielements.containers.GlassSurfaceOnGlassSurface
import com.ataglance.walletglance.presentation.ui.uielements.fields.DateField
import com.ataglance.walletglance.presentation.ui.uielements.fields.GlanceTextField
import com.ataglance.walletglance.presentation.ui.uielements.fields.MakeRecordFieldContainer
import com.ataglance.walletglance.presentation.ui.uielements.pickers.CustomDatePicker
import com.ataglance.walletglance.presentation.ui.uielements.pickers.CustomTimePicker
import com.ataglance.walletglance.presentation.ui.uielements.records.MakeRecordTypeBar
import com.ataglance.walletglance.presentation.viewmodels.records.MakeRecordViewModel
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
    onDeleteButton: (Int) -> Unit,
    onDimBackgroundChange: (Boolean) -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val recordUnitList by viewModel.recordUnitList.collectAsStateWithLifecycle()
    val savingIsAllowed by viewModel.allowSaving.collectAsStateWithLifecycle()

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var showCategoryPicker by remember { mutableStateOf(false) }

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

            GlassSurface(
                modifier = Modifier.weight(1f),
                filledWidth = .96f.takeIf { WindowTypeIsCompact }
            ) {
                GlassSurfaceContent(
                    appTheme = appTheme,
                    viewModel = viewModel,
                    uiState = uiState,
                    recordUnitList = recordUnitList,
                    accountList = accountList,
                    onDimBackgroundChange = onDimBackgroundChange,
                    onShowDateDialogChange = {
                        showDatePicker = it
                    },
                    onShowCategoryPickerChange = {
                        showCategoryPicker = it
                    }

                )
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
            openDialog = showDatePicker,
            onOpenDateDialogChange = { showDatePicker = it },
            onConfirmButton = {
                datePickerState.selectedDateMillis?.let { viewModel.selectNewDate(it) }
                showDatePicker = false
                showTimePicker = true
            },
            state = datePickerState
        )
        CustomTimePicker(
            openDialog = showTimePicker,
            onOpenTimeDialogChange = { showTimePicker = it },
            onConfirmButton = {
                viewModel.selectNewTime(timePickerState.hour, timePickerState.minute)
                showTimePicker = false
            },
            state = timePickerState
        )
        CategoryPicker(
            visible = showCategoryPicker,
            categoriesWithSubcategories = categoriesWithSubcategories,
            type = if (uiState.type == RecordType.Expense) CategoryType.Expense
                else CategoryType.Income,
            appTheme = appTheme,
            onDismissRequest = { showCategoryPicker = false },
            onCategoryChoose = viewModel::chooseCategory
        )
    }
}

@Composable
private fun GlassSurfaceContent(
    appTheme: AppTheme?,
    viewModel: MakeRecordViewModel,
    uiState: MakeRecordUiState,
    recordUnitList: List<MakeRecordUnitUiState>,
    accountList: List<Account>,
    onDimBackgroundChange: (Boolean) -> Unit,
    onShowDateDialogChange: (Boolean) -> Unit,
    onShowCategoryPickerChange: (Boolean) -> Unit
) {
    val fieldsCornerSize = 15.dp
    val lazyListState: LazyListState = rememberLazyListState()

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
                    onClick = {
                        onShowDateDialogChange(true)
                    }
                )
                MakeRecordFieldContainer(R.string.account) {
                    AccountPopupPicker(
                        appTheme = appTheme,
                        accountList = accountList,
                        selectedAccount = uiState.account,
                        onToggleAccounts = {
                            uiState.account?.let { currentAccount ->
                                viewModel.toggleSelectedAccount(currentAccount, accountList)
                            }
                        },
                        onAccountSelect = { account ->
                            viewModel.selectAccount(account)
                        },
                        onDimBackgroundChange = onDimBackgroundChange
                    )
                }
            }
        }
        items(items = recordUnitList, key = { it.lazyListKey }) { recordUnit ->
            RecordUnitBlock(
                appTheme = appTheme,
                recordUnitUiState = recordUnit,
                onNoteValueChange = { value ->
                    viewModel.changeNoteValue(recordUnit.index, value)
                },
                onCategoryClick = {
                    viewModel.changeClickedUnitIndex(recordUnit.index)
                    onShowCategoryPickerChange(true)
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

@Composable
private fun LazyItemScope.RecordUnitBlock(
    appTheme: AppTheme?,
    recordUnitUiState: MakeRecordUnitUiState,
    onNoteValueChange: (String) -> Unit,
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
                    recordUnitUiState = recordUnitUiState,
                    accountCurrency = accountCurrency,
                    lastIndex = lastIndex,
                    appTheme = appTheme,
                    onSwapUnits = onSwapUnits,
                    onDeleteButtonClick = onDeleteButton,
                    onExpandButtonClick = { onChangeCollapsedValue(false) }
                )
            } else {
                RecordUnitBlockExpanded(
                    recordUnitUiState = recordUnitUiState,
                    onNoteValueChange = onNoteValueChange,
                    onCategoryClick = onCategoryClick,
                    onAmountValueChange = onAmountValueChange,
                    onQuantityChange = onQuantityChange,
                    lastIndex = lastIndex,
                    appTheme = appTheme,
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
    recordUnitUiState: MakeRecordUnitUiState,
    accountCurrency: String?,
    lastIndex: Int,
    appTheme: AppTheme?,
    onSwapUnits: (Int, Int) -> Unit,
    onDeleteButtonClick: (Int) -> Unit,
    onExpandButtonClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        recordUnitUiState.note.takeIf { it.isNotBlank() }?.let { note ->
            Text(
                text = note,
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
            recordUnitUiState.quantity.takeIf { it.isNotBlank() }?.let { quantity ->
                Text(
                    text = "$quantity x",
                    fontSize = 18.sp,
                    color = GlanceTheme.onSurface.copy(.6f),
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }
            Text(
                text = recordUnitUiState.getFormattedAmount(),
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
            targetState = recordUnitUiState.categoryWithSubcategory?.getSubcategoryOrCategory(),
            label = "record unit category"
        ) { targetCategory ->
            RecordCategory(
                category = targetCategory,
                appTheme = appTheme,
                iconSize = 32.dp,
                fontSize = 20.sp
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        ControlPanel(
            index = recordUnitUiState.index,
            lastIndex = lastIndex,
            spaceSize = 12.dp,
            onSwapUnits = onSwapUnits,
            onDeleteButton = onDeleteButtonClick
        ) {
            SmallFilledIconButton(
                iconRes = R.drawable.expand_icon,
                iconContendDescription = "expand record unit",
                onClick = onExpandButtonClick
            )
        }
    }
}

@Composable
private fun RecordUnitBlockExpanded(
    recordUnitUiState: MakeRecordUnitUiState,
    onNoteValueChange: (String) -> Unit,
    onCategoryClick: () -> Unit,
    onAmountValueChange: (String) -> Unit,
    onQuantityChange: (String) -> Unit,
    lastIndex: Int,
    appTheme: AppTheme?,
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
            GlanceTextField(
                text = recordUnitUiState.amount,
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
                targetState = recordUnitUiState.categoryWithSubcategory?.getSubcategoryOrCategory(),
                label = "category field at the make record screen"
            ) { targetCategory ->
                CategoryField(
                    category = targetCategory,
                    fontSize = 20.sp,
                    cornerSize = fieldsCornerSize,
                    appTheme = appTheme,
                    onClick = onCategoryClick
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        MakeRecordFieldContainer(R.string.note) {
            GlanceTextField(
                text = recordUnitUiState.note,
                placeholderText = stringResource(R.string.note_placeholder),
                fontSize = 18.sp,
                cornerSize = fieldsCornerSize,
                onValueChange = onNoteValueChange,
                modifier = Modifier.bounceClickEffect(.97f)
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        MakeRecordFieldContainer(R.string.quantity) {
            GlanceTextField(
                text = recordUnitUiState.quantity,
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
                index = recordUnitUiState.index,
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
        targetValue = if (enabled) GlanceTheme.onSurface else GlanceTheme.outline,
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
package com.ataglance.walletglance.recordCreation.presentation.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.category.presentation.component.CategoryField
import com.ataglance.walletglance.category.presentation.component.RecordCategory
import com.ataglance.walletglance.core.presentation.component.button.SmallFilledIconButton
import com.ataglance.walletglance.core.presentation.component.container.GlassSurfaceOnGlassSurface
import com.ataglance.walletglance.core.presentation.component.field.FieldWithLabel
import com.ataglance.walletglance.core.presentation.component.field.GlanceTextField
import com.ataglance.walletglance.core.presentation.theme.GlanceColors
import com.ataglance.walletglance.core.presentation.theme.Manrope
import com.ataglance.walletglance.recordCreation.presentation.model.record.RecordDraftItem

@Composable
fun LazyItemScope.RecordItemCreationComponent(
    recordDraftItem: RecordDraftItem,
    accountCurrency: String?,
    onAmountChange: (String) -> Unit,
    onCategoryFieldClick: () -> Unit,
    onNoteChange: (String) -> Unit,
    onQuantityChange: (String) -> Unit,
    draftLastItemIndex: Int,
    onSwapItems: (Int, Int) -> Unit,
    onDeleteItem: (Int) -> Unit,
    onCollapsedChange: (Boolean) -> Unit
) {
    GlassSurfaceOnGlassSurface(
        modifier = Modifier.animateItem(),
        paddingValues = PaddingValues(0.dp),
        clickEnabled = recordDraftItem.collapsed,
        onClick = { onCollapsedChange(false) }
    ) {
        AnimatedContent(
            targetState = recordDraftItem.collapsed,
            label = "record unit block"
        ) { targetCollapsed ->
            if (targetCollapsed) {
                RecordItemCreationComponentCollapsed(
                    recordDraftItem = recordDraftItem,
                    accountCurrency = accountCurrency,
                    draftLastItemIndex = draftLastItemIndex,
                    onSwapItems = onSwapItems,
                    onDeleteItem = onDeleteItem,
                    onExpandButtonClick = { onCollapsedChange(false) }
                )
            } else {
                RecordItemCreationComponentExpanded(
                    recordDraftItem = recordDraftItem,
                    onAmountChange = onAmountChange,
                    onCategoryFieldClick = onCategoryFieldClick,
                    onNoteChange = onNoteChange,
                    onQuantityChange = onQuantityChange,
                    draftLastItemIndex = draftLastItemIndex,
                    onSwapItems = onSwapItems,
                    onDeleteItem = onDeleteItem,
                    onCollapseButtonClick = { onCollapsedChange(true) }
                )
            }
        }
    }
}

@Composable
private fun RecordItemCreationComponentCollapsed(
    recordDraftItem: RecordDraftItem,
    accountCurrency: String?,
    draftLastItemIndex: Int,
    onSwapItems: (Int, Int) -> Unit,
    onDeleteItem: (Int) -> Unit,
    onExpandButtonClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        if (recordDraftItem.note.isNotBlank()) {
            Text(
                text = recordDraftItem.note,
                color = GlanceColors.onSurface,
                fontSize = 18.sp,
                fontFamily = Manrope,
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
            if (recordDraftItem.quantity.isNotBlank()) {
                Text(
                    text = "${recordDraftItem.quantity} x",
                    color = GlanceColors.onSurface.copy(.6f),
                    fontSize = 18.sp,
                    fontFamily = Manrope,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }
            Text(
                text = recordDraftItem.getFormattedAmountOrPlaceholder(),
                color = GlanceColors.onSurface,
                fontSize = 22.sp,
                fontFamily = Manrope
            )
            accountCurrency?.let {
                Text(
                    text = it,
                    color = GlanceColors.onSurface.copy(.6f),
                    fontSize = 18.sp,
                    fontFamily = Manrope,
                    modifier = Modifier.padding(bottom = 2.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(4.dp))

        AnimatedContent(
            targetState = recordDraftItem.categoryWithSub?.getSubcategoryOrCategory(),
            label = "record draft item category"
        ) { targetCategory ->
            RecordCategory(
                category = targetCategory,
                iconSize = 32.dp,
                fontSize = 20.sp
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        RecordItemCreationControlPanel(
            thisIndex = recordDraftItem.index,
            lastIndex = draftLastItemIndex,
            spaceSize = 12.dp,
            onSwapButtonsClick = onSwapItems,
            onDeleteButtonClick = onDeleteItem
        ) {
            SmallFilledIconButton(
                iconRes = R.drawable.expand_icon,
                iconContendDescription = "expand record draft item",
                onClick = onExpandButtonClick
            )
        }
    }
}

@Composable
private fun RecordItemCreationComponentExpanded(
    recordDraftItem: RecordDraftItem,
    onAmountChange: (String) -> Unit,
    onCategoryFieldClick: () -> Unit,
    onNoteChange: (String) -> Unit,
    onQuantityChange: (String) -> Unit,
    draftLastItemIndex: Int,
    onSwapItems: (Int, Int) -> Unit,
    onDeleteItem: (Int) -> Unit,
    onCollapseButtonClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 22.dp)
    ) {
        FieldWithLabel(labelText = stringResource(R.string.amount)) {
            GlanceTextField(
                text = recordDraftItem.amount,
                placeholderText = "0.00",
                fontSize = 22.sp,
                onValueChange = onAmountChange,
                keyboardType = KeyboardType.Number
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        FieldWithLabel(labelText = stringResource(R.string.category)) {
            AnimatedContent(
                targetState = recordDraftItem.categoryWithSub?.getSubcategoryOrCategory(),
                label = "category field at the make record screen"
            ) { targetCategory ->
                CategoryField(
                    category = targetCategory,
                    onClick = onCategoryFieldClick
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        FieldWithLabel(labelText = stringResource(R.string.note)) {
            GlanceTextField(
                text = recordDraftItem.note,
                placeholderText = stringResource(R.string.note_placeholder),
                fontSize = 18.sp,
                onValueChange = onNoteChange
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        FieldWithLabel(labelText = stringResource(R.string.quantity)) {
            GlanceTextField(
                text = recordDraftItem.quantity,
                placeholderText = stringResource(R.string.quantity_placeholder),
                fontSize = 18.sp,
                onValueChange = onQuantityChange,
                keyboardType = KeyboardType.Number
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            RecordItemCreationControlPanel(
                thisIndex = recordDraftItem.index,
                lastIndex = draftLastItemIndex,
                spaceSize = 16.dp,
                onSwapButtonsClick = onSwapItems,
                onDeleteButtonClick = onDeleteItem
            ) {
                SmallFilledIconButton(
                    iconRes = R.drawable.collapse_icon,
                    iconContendDescription = "collapse record draft item",
                    onClick = onCollapseButtonClick
                )
            }
        }
    }
}

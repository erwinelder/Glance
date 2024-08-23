package com.ataglance.walletglance.category.presentation.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.category.domain.Category
import com.ataglance.walletglance.category.domain.color.CategoryPossibleColors
import com.ataglance.walletglance.category.domain.icons.CategoryIcon
import com.ataglance.walletglance.core.presentation.GlanceTheme
import com.ataglance.walletglance.core.presentation.modifiers.bounceClickEffect
import com.ataglance.walletglance.core.presentation.components.screenContainers.SetupDataScreenContainer
import com.ataglance.walletglance.core.presentation.components.buttons.ColorButton
import com.ataglance.walletglance.core.presentation.components.buttons.PrimaryButton
import com.ataglance.walletglance.core.presentation.components.buttons.SecondaryButton
import com.ataglance.walletglance.core.presentation.components.fields.TextFieldWithLabel
import com.ataglance.walletglance.core.presentation.components.pickers.ColorPicker

@Composable
fun EditCategoryScreen(
    scaffoldPadding: PaddingValues,
    appTheme: AppTheme?,
    category: Category,
    allowDeleting: Boolean,
    allowSaving: Boolean,
    onNameChange: (String) -> Unit,
    onCategoryColorChange: (String) -> Unit,
    onIconChange: (CategoryIcon) -> Unit,
    onSaveButton: () -> Unit,
    onDeleteButton: () -> Unit,
    categoryIconList: List<CategoryIcon>
) {
    var showColorPicker by remember { mutableStateOf(false) }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        SetupDataScreenContainer(
            topPadding = scaffoldPadding.calculateTopPadding(),
            topButton = if (allowDeleting) {
                {
                    SecondaryButton(
                        onClick = onDeleteButton,
                        text = stringResource(R.string.delete)
                    )
                }
            } else null,
            glassSurfaceContent = {
                GlassSurfaceContent(
                    appTheme = appTheme,
                    category = category,
                    onNameChange = onNameChange,
                    onIconChange = onIconChange,
                    categoryIconList = categoryIconList,
                    onColorButtonClick = { showColorPicker = true }
                )
            },
            primaryBottomButton = {
                PrimaryButton(
                    onClick = onSaveButton,
                    text = stringResource(R.string.save),
                    enabled = allowSaving
                )
            }
        )
        ColorPicker(
            visible = showColorPicker,
            colorList = CategoryPossibleColors().asColorWithNameList(appTheme),
            onColorClick = onCategoryColorChange,
            onPickerClose = {
                showColorPicker = false
            }
        )
    }
}

@Composable
private fun GlassSurfaceContent(
    appTheme: AppTheme?,
    category: Category,
    onNameChange: (String) -> Unit,
    onIconChange: (CategoryIcon) -> Unit,
    categoryIconList: List<CategoryIcon>,
    onColorButtonClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.field_gap)),
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
            .padding(start = 12.dp, end = 12.dp, top = 24.dp)
    ) {
        TextFieldWithLabel(
            text = category.name,
            placeholderText = stringResource(R.string.category_name),
            labelText = stringResource(R.string.name),
            onValueChange = onNameChange
        )
        if (category.isParentCategory()) {
            ColorButton(
                color = category.getColorByTheme(appTheme).darker,
                onClick = onColorButtonClick
            )
        }
        CategoryIconsGrid(
            categoryIconList = categoryIconList,
            currentCategoryIcon = category.icon,
            onCategoryIconChange = onIconChange
        )
    }
}

@Composable
private fun CategoryIconsGrid(
    categoryIconList: List<CategoryIcon>,
    currentCategoryIcon: CategoryIcon,
    onCategoryIconChange: (CategoryIcon) -> Unit
) {
    val lazyGridState = rememberLazyGridState()

    LazyVerticalGrid(
        columns = GridCells.Adaptive(40.dp),
        state = lazyGridState,
        contentPadding = PaddingValues(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .size(300.dp, 500.dp)
    ) {
        items(items = categoryIconList, key = { it.res }) { categoryIcon ->
            val color by animateColorAsState(
                targetValue = if (currentCategoryIcon.name == categoryIcon.name) {
                    GlanceTheme.primary
                } else {
                    GlanceTheme.onSurface
                },
                label = "icon color"
            )
            Icon(
                painter = painterResource(categoryIcon.res),
                contentDescription = categoryIcon.name + " icon",
                tint = color,
                modifier = Modifier
                    .bounceClickEffect(.97f) {
                        onCategoryIconChange(categoryIcon)
                    }
            )
        }
    }
}

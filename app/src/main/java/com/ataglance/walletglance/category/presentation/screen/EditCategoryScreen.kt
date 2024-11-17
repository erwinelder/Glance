package com.ataglance.walletglance.category.presentation.screen

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.category.domain.color.CategoryPossibleColors
import com.ataglance.walletglance.category.domain.icons.CategoryIcon
import com.ataglance.walletglance.category.domain.icons.CategoryPossibleIcons
import com.ataglance.walletglance.category.domain.model.CategoriesWithSubcategories
import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.category.domain.model.DefaultCategoriesPackage
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.CurrAppTheme
import com.ataglance.walletglance.core.presentation.GlanceTheme
import com.ataglance.walletglance.core.presentation.components.buttons.ColorButton
import com.ataglance.walletglance.core.presentation.components.buttons.PrimaryButton
import com.ataglance.walletglance.core.presentation.components.buttons.SecondaryButton
import com.ataglance.walletglance.core.presentation.components.containers.GlassSurfaceContentColumnWrapper
import com.ataglance.walletglance.core.presentation.components.containers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.components.fields.TextFieldWithLabel
import com.ataglance.walletglance.core.presentation.components.pickers.ColorPicker
import com.ataglance.walletglance.core.presentation.components.screenContainers.GlassSurfaceScreenContainer
import com.ataglance.walletglance.core.presentation.modifiers.bounceClickEffect

@Composable
fun EditCategoryScreen(
    scaffoldPadding: PaddingValues,
    category: Category,
    allowDeleting: Boolean,
    allowSaving: Boolean,
    onNameChange: (String) -> Unit,
    onCategoryColorChange: (String) -> Unit,
    onIconChange: (CategoryIcon) -> Unit,
    onSaveButton: () -> Unit,
    onDeleteButton: () -> Unit,
) {
    var showColorPicker by remember { mutableStateOf(false) }
    val categoryIconList by remember {
        derivedStateOf { CategoryPossibleIcons().asList() }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        GlassSurfaceScreenContainer(
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
            colorList = CategoryPossibleColors().asColorWithNameList(CurrAppTheme),
            onColorClick = onCategoryColorChange,
            onPickerClose = {
                showColorPicker = false
            }
        )
    }
}

@Composable
private fun GlassSurfaceContent(
    category: Category,
    onNameChange: (String) -> Unit,
    onIconChange: (CategoryIcon) -> Unit,
    categoryIconList: List<CategoryIcon>,
    onColorButtonClick: () -> Unit
) {
    GlassSurfaceContentColumnWrapper {
        TextFieldWithLabel(
            text = category.name,
            placeholderText = stringResource(R.string.category_name),
            labelText = stringResource(R.string.name),
            onValueChange = onNameChange
        )
        if (category.isParentCategory()) {
            ColorButton(
                color = category.getColorByTheme(CurrAppTheme).darker,
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



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun EditCategoryScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault,
    isAppSetUp: Boolean = true,
    isSetupProgressTopBarVisible: Boolean = false,
    categoriesWithSubcategories: CategoriesWithSubcategories = DefaultCategoriesPackage(
        LocalContext.current
    ).getDefaultCategories(),
) {
    val category = categoriesWithSubcategories.expense[0].category

    PreviewWithMainScaffoldContainer(
        appTheme = appTheme,
        isSetupProgressTopBarVisible = isSetupProgressTopBarVisible,
    ) { scaffoldPadding ->
        EditCategoryScreen(
            scaffoldPadding = scaffoldPadding,
            category = category,
            allowDeleting = false,
            allowSaving = category.allowSaving(),
            onNameChange = {},
            onCategoryColorChange = {},
            onIconChange = {},
            onSaveButton = {},
            onDeleteButton = {}
        )
    }
}

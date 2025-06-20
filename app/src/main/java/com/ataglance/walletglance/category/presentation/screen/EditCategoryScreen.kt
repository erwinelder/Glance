package com.ataglance.walletglance.category.presentation.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.ataglance.walletglance.R
import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.category.domain.model.CategoryColor
import com.ataglance.walletglance.category.domain.model.CategoryIcon
import com.ataglance.walletglance.category.domain.model.DefaultCategoriesPackage
import com.ataglance.walletglance.category.domain.model.GroupedCategoriesByType
import com.ataglance.walletglance.category.presentation.component.CategoryIconComponent
import com.ataglance.walletglance.category.presentation.viewmodel.EditCategoriesViewModel
import com.ataglance.walletglance.category.presentation.viewmodel.EditCategoryViewModel
import com.ataglance.walletglance.core.domain.app.AppConfiguration
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.component.button.ColorButton
import com.ataglance.walletglance.core.presentation.component.button.TertiaryButton
import com.ataglance.walletglance.core.presentation.component.container.GlassSurface
import com.ataglance.walletglance.core.presentation.component.container.GlassSurfaceContentColumnWrapper
import com.ataglance.walletglance.core.presentation.component.field.TextFieldWithLabel
import com.ataglance.walletglance.core.presentation.component.picker.ColorPicker
import com.ataglance.walletglance.core.presentation.component.screenContainer.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.component.screenContainer.ScreenContainerWithTopBackNavButtonAndPrimaryButton
import com.ataglance.walletglance.core.presentation.modifier.bounceClickEffect
import com.ataglance.walletglance.core.presentation.theme.CurrAppTheme
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.core.presentation.viewmodel.sharedKoinNavViewModel
import com.ataglance.walletglance.core.presentation.viewmodel.sharedViewModel
import com.ataglance.walletglance.core.utils.takeRowComposableIf
import org.koin.core.parameter.parametersOf

@Composable
fun EditCategoryScreenWrapper(
    screenPadding: PaddingValues = PaddingValues(),
    backStack: NavBackStackEntry,
    navController: NavHostController,
    appConfiguration: AppConfiguration
) {
    val categoriesViewModel = backStack.sharedKoinNavViewModel<EditCategoriesViewModel>(
        navController = navController,
        parameters = { parametersOf(appConfiguration.langCode) }
    )
    val categoryViewModel = backStack.sharedViewModel<EditCategoryViewModel>(navController)

    val category by categoryViewModel.category.collectAsStateWithLifecycle()
    val allowDeleting by categoryViewModel.allowDeleting.collectAsStateWithLifecycle()
    val allowSaving by categoryViewModel.allowSaving.collectAsStateWithLifecycle()

    EditCategoryScreen(
        screenPadding = screenPadding,
        onNavigateBack = navController::popBackStack,
        category = category,
        allowDeleting = allowDeleting,
        allowSaving = allowSaving,
        onNameChange = categoryViewModel::changeName,
        onCategoryColorChange = categoryViewModel::changeColor,
        onIconChange = categoryViewModel::changeIcon,
        onDeleteButton = {
            categoriesViewModel.deleteCategory(categoryViewModel.getCategory())
            navController.popBackStack()
        },
        onSaveButton = {
            categoriesViewModel.saveEditedCategory(categoryViewModel.getCategory())
            navController.popBackStack()
        }
    )
}

@Composable
fun EditCategoryScreen(
    screenPadding: PaddingValues = PaddingValues(),
    onNavigateBack: () -> Unit,
    category: Category,
    allowDeleting: Boolean,
    allowSaving: Boolean,
    onNameChange: (String) -> Unit,
    onCategoryColorChange: (String) -> Unit,
    onIconChange: (CategoryIcon) -> Unit,
    onSaveButton: () -> Unit,
    onDeleteButton: () -> Unit,
) {
    val categoryIconWithColor by remember(category.icon, category.color) {
        derivedStateOf { category.icon to category.color }
    }
    val backNavButtonText = category.name.takeIf { it.isNotBlank() }
        ?: stringResource(R.string.category)

    var showColorPicker by remember { mutableStateOf(false) }
    val categoryIconList by remember {
        derivedStateOf { CategoryIcon.getAll() }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {

        ScreenContainerWithTopBackNavButtonAndPrimaryButton(
            screenPadding = screenPadding,
            backNavButtonText = backNavButtonText,
            backNavButtonIconComponent = {
                AnimatedContent(
                    targetState = categoryIconWithColor
                ) { (icon, color) ->
                    CategoryIconComponent(
                        categoryIcon = icon,
                        categoryColor = color,
                        modifier = Modifier.padding(vertical = 2.dp)
                    )
                }
            },
            onBackNavButtonClick = onNavigateBack,
            backNavButtonCompanionComponent = takeRowComposableIf(allowDeleting) {
                TertiaryButton(
                    text = stringResource(R.string.delete),
                    iconRes = R.drawable.trash_icon,
                    onClick = onDeleteButton
                )
            },
            primaryButtonText = stringResource(R.string.save),
            primaryButtonEnabled = allowSaving,
            onPrimaryButtonClick = onSaveButton
        ) {
            GlassSurface(
                modifier = Modifier.weight(1f, fill = false)
            ) {
                GlassSurfaceContent(
                    category = category,
                    onNameChange = onNameChange,
                    onIconChange = onIconChange,
                    categoryIconList = categoryIconList,
                    onColorButtonClick = { showColorPicker = true }
                )
            }
        }

        ColorPicker(
            visible = showColorPicker,
            colorList = CategoryColor.asColorWithNameList(CurrAppTheme),
            onColorClick = onCategoryColorChange,
            onPickerClose = { showColorPicker = false }
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
        columns = GridCells.Adaptive(32.dp),
        state = lazyGridState,
        contentPadding = PaddingValues(vertical = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        items(items = categoryIconList, key = { it.res }) { categoryIcon ->
            val color by animateColorAsState(
                targetValue = if (currentCategoryIcon.name == categoryIcon.name) {
                    GlanciColors.primary
                } else {
                    GlanciColors.onSurface
                }
            )
            Icon(
                painter = painterResource(categoryIcon.res),
                contentDescription = categoryIcon.name + " icon",
                tint = color,
                modifier = Modifier.bounceClickEffect {
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
    groupedCategoriesByType: GroupedCategoriesByType = DefaultCategoriesPackage(
        LocalContext.current
    ).getDefaultCategories()
) {
    val category = groupedCategoriesByType.expense[0].category

    PreviewWithMainScaffoldContainer(appTheme = appTheme) { scaffoldPadding ->
        EditCategoryScreen(
            screenPadding = scaffoldPadding,
            onNavigateBack = {},
            category = category,
            allowDeleting = false,
            allowSaving = category.savingIsAllowed(),
            onNameChange = {},
            onCategoryColorChange = {},
            onIconChange = {},
            onSaveButton = {},
            onDeleteButton = {}
        )
    }
}

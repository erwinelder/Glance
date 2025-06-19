package com.ataglance.walletglance.category.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavHostController
import com.ataglance.walletglance.R
import com.ataglance.walletglance.category.domain.model.Category
import com.ataglance.walletglance.category.domain.model.DefaultCategoriesPackage
import com.ataglance.walletglance.category.domain.model.GroupedCategories
import com.ataglance.walletglance.category.domain.model.GroupedCategoriesByType
import com.ataglance.walletglance.category.domain.navigation.CategoriesSettingsScreens
import com.ataglance.walletglance.category.presentation.component.CategoryIconComponent
import com.ataglance.walletglance.category.presentation.component.EditingSubcategoryComponent
import com.ataglance.walletglance.category.presentation.viewmodel.EditCategoriesViewModel
import com.ataglance.walletglance.category.presentation.viewmodel.EditCategoryViewModel
import com.ataglance.walletglance.core.domain.app.AppConfiguration
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.component.button.SmallSecondaryButton
import com.ataglance.walletglance.core.presentation.component.container.GlassSurface
import com.ataglance.walletglance.core.presentation.component.screenContainer.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.component.screenContainer.ScreenContainerWithTopBackNavButtonAndPrimaryButton
import com.ataglance.walletglance.core.presentation.theme.CurrAppTheme
import com.ataglance.walletglance.core.presentation.theme.WindowTypeIsExpanded
import com.ataglance.walletglance.core.presentation.viewmodel.sharedKoinNavViewModel
import com.ataglance.walletglance.core.presentation.viewmodel.sharedViewModel
import com.ataglance.walletglance.core.utils.takeComposableIfNotNull
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.settings.presentation.model.SettingsCategory
import org.koin.core.parameter.parametersOf

@Composable
fun EditSubcategoriesScreenWrapper(
    screenPadding: PaddingValues = PaddingValues(),
    backStack: NavBackStackEntry,
    navController: NavHostController,
    navViewModel: NavigationViewModel,
    appConfiguration: AppConfiguration
) {
    val categoriesViewModel = backStack.sharedKoinNavViewModel<EditCategoriesViewModel>(
        navController = navController,
        parameters = { parametersOf(appConfiguration.langCode) }
    )
    val categoryViewModel = backStack.sharedViewModel<EditCategoryViewModel>(navController)

    val categoriesUiState by categoriesViewModel.uiState.collectAsStateWithLifecycle()

    EditSubcategoriesScreen(
        screenPadding = screenPadding,
        onNavigateBack = navController::popBackStack,
        groupedCategories = categoriesUiState.groupedCategories,
        onSaveButton = {
            categoriesViewModel.saveSubcategoryList()
            navController.popBackStack()
        },
        onNavigateToEditCategoryScreen = { categoryOrNull ->
            categoryViewModel.applyCategory(
                category = categoryOrNull ?: categoriesViewModel.getNewSubcategory()
            )
            navViewModel.navigateToScreen(
                navController, CategoriesSettingsScreens.EditCategory
            )
        },
        onSwapCategories = categoriesViewModel::moveSubcategories
    )
}

@Composable
fun EditSubcategoriesScreen(
    screenPadding: PaddingValues = PaddingValues(),
    onNavigateBack: () -> Unit,
    groupedCategories: GroupedCategories?,
    onSaveButton: () -> Unit,
    onNavigateToEditCategoryScreen: (Category?) -> Unit,
    onSwapCategories: (Int, Int) -> Unit
) {
    val settingsCategory = SettingsCategory.Categories(appTheme = CurrAppTheme)
    val topBackNavButtonText = groupedCategories?.category?.name
        ?: stringResource(settingsCategory.stringRes)

    ScreenContainerWithTopBackNavButtonAndPrimaryButton(
        screenPadding = screenPadding,
        topBackNavButtonText = topBackNavButtonText,
        topBackNavButtonIconComponent = takeComposableIfNotNull(groupedCategories?.category) {
            CategoryIconComponent(
                category = it,
                modifier = Modifier.padding(vertical = 2.dp)
            )
        },
        onTopBackNavButtonClick = onNavigateBack,
        primaryButtonText = stringResource(R.string.save),
        onPrimaryButtonClick = onSaveButton
    ) {

        GlassSurface(
            modifier = Modifier.weight(1f, fill = false)
        ) {
            GlassSurfaceContent(
                subcategoryList = groupedCategories?.subcategoryList.orEmpty(),
                onNavigateToEditCategoryScreen = onNavigateToEditCategoryScreen,
                onSwapCategories = onSwapCategories
            )
        }

        SmallSecondaryButton(
            text = stringResource(R.string.add_subcategory),
            iconRes = R.drawable.add_icon
        ) {
            onNavigateToEditCategoryScreen(null)
        }

    }
}

@Composable
private fun GlassSurfaceContent(
    subcategoryList: List<Category>,
    onNavigateToEditCategoryScreen: (Category) -> Unit,
    onSwapCategories: (Int, Int) -> Unit
) {
    if (!WindowTypeIsExpanded) {
        CompactLayout(
            subcategoryList = subcategoryList,
            onNavigateToEditCategoryScreen = onNavigateToEditCategoryScreen,
            onSwapCategories = onSwapCategories
        )
    } else {
        ExpandedLayout(
            subcategoryList = subcategoryList,
            onNavigateToEditCategoryScreen = onNavigateToEditCategoryScreen,
            onSwapCategories = onSwapCategories
        )
    }
}

@Composable
private fun CompactLayout(
    subcategoryList: List<Category>,
    onNavigateToEditCategoryScreen: (Category) -> Unit,
    onSwapCategories: (Int, Int) -> Unit
) {
    val lazyListState = rememberLazyListState()

    LazyColumn(
        state = lazyListState,
        contentPadding = PaddingValues(dimensionResource(R.dimen.widget_content_padding)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        itemsIndexed(
            items = subcategoryList,
            key = { _, item -> item.id }
        ) { index, category ->
            EditingSubcategoryComponent(
                category = category,
                onEditButton = {
                    onNavigateToEditCategoryScreen(category)
                },
                onUpButtonClick = {
                    onSwapCategories(index, index - 1)
                },
                upButtonEnabled = index > 0,
                onDownButtonClick = {
                    onSwapCategories(index, index + 1)
                },
                downButtonEnabled = index < subcategoryList.lastIndex,
                modifier = Modifier.animateItem()
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ExpandedLayout(
    subcategoryList: List<Category>,
    onNavigateToEditCategoryScreen: (Category) -> Unit,
    onSwapCategories: (Int, Int) -> Unit
) {
    val scrollState = rememberScrollState()

    FlowRow(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        subcategoryList.forEachIndexed { index, category ->
            Box(modifier = Modifier.padding(8.dp)) {
                EditingSubcategoryComponent(
                    category = category,
                    onEditButton = {
                        onNavigateToEditCategoryScreen(category)
                    },
                    onUpButtonClick = {
                        onSwapCategories(index, index - 1)
                    },
                    upButtonEnabled = index > 0,
                    onDownButtonClick = {
                        onSwapCategories(index, index + 1)
                    },
                    downButtonEnabled = index < subcategoryList.lastIndex,
                )
            }
        }
    }
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun EditSubcategoriesScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault,
    groupedCategoriesByType: GroupedCategoriesByType = DefaultCategoriesPackage(
        LocalContext.current
    ).getDefaultCategories(),
) {
    PreviewWithMainScaffoldContainer(appTheme = appTheme) { scaffoldPadding ->
        EditSubcategoriesScreen(
            screenPadding = scaffoldPadding,
            onNavigateBack = {},
            groupedCategories = groupedCategoriesByType.expense[2],
            onSaveButton = {},
            onNavigateToEditCategoryScreen = {},
            onSwapCategories = { _, _ -> }
        )
    }
}

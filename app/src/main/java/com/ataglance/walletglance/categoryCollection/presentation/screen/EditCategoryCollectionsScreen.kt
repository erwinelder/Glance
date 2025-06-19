package com.ataglance.walletglance.categoryCollection.presentation.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
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
import com.ataglance.walletglance.category.domain.model.DefaultCategoriesPackage
import com.ataglance.walletglance.category.domain.model.GroupedCategoriesByType
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionType
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionWithCategories
import com.ataglance.walletglance.categoryCollection.domain.model.CategoryCollectionsWithIdsByType
import com.ataglance.walletglance.categoryCollection.domain.navigation.CategoryCollectionsSettingsScreens
import com.ataglance.walletglance.categoryCollection.presentation.component.CategoryCollectionTypeBar
import com.ataglance.walletglance.categoryCollection.presentation.component.EditingCategoryCollectionComponent
import com.ataglance.walletglance.categoryCollection.presentation.viewmodel.EditCategoryCollectionViewModel
import com.ataglance.walletglance.categoryCollection.presentation.viewmodel.EditCategoryCollectionsViewModel
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.component.button.SmallSecondaryButton
import com.ataglance.walletglance.core.presentation.component.container.GlassSurface
import com.ataglance.walletglance.core.presentation.component.container.MessageContainer
import com.ataglance.walletglance.core.presentation.component.screenContainer.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.component.screenContainer.ScreenContainerWithTopBackNavButtonAndPrimaryButton
import com.ataglance.walletglance.core.presentation.theme.CurrAppTheme
import com.ataglance.walletglance.core.presentation.theme.WindowTypeIsExpanded
import com.ataglance.walletglance.core.presentation.viewmodel.sharedKoinNavViewModel
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.settings.presentation.model.SettingsCategory
import kotlinx.coroutines.launch

@Composable
fun EditCategoryCollectionsScreenWrapper(
    screenPadding: PaddingValues = PaddingValues(),
    backStack: NavBackStackEntry,
    navController: NavHostController,
    navViewModel: NavigationViewModel
) {
    val collectionsViewModel = backStack.sharedKoinNavViewModel<EditCategoryCollectionsViewModel>(navController)
    val collectionViewModel = backStack.sharedKoinNavViewModel<EditCategoryCollectionViewModel>(navController)

    val collectionsByType by collectionsViewModel.collectionsByType.collectAsStateWithLifecycle()
    val collectionType by collectionsViewModel.collectionType.collectAsStateWithLifecycle()
    val coroutineScope = rememberCoroutineScope()

    EditCategoryCollectionsScreen(
        screenPadding = screenPadding,
        onNavigateBack = navController::popBackStack,
        collectionsWithCategories = collectionsByType,
        collectionType = collectionType,
        onCategoryTypeChange = collectionsViewModel::changeCategoryType,
        onNavigateToEditCollectionScreen = { collectionOrNull ->
            collectionViewModel.applyCollection(
                collection = collectionOrNull ?: collectionsViewModel.getNewCollection()
            )
            navViewModel.navigateToScreen(
                navController = navController,
                screen = CategoryCollectionsSettingsScreens.EditCategoryCollection
            )
        },
        onSaveCollectionsButton = {
            coroutineScope.launch {
                collectionsViewModel.saveCategoryCollections()
                navController.popBackStack()
            }
        }
    )
}

@Composable
fun EditCategoryCollectionsScreen(
    screenPadding: PaddingValues = PaddingValues(),
    onNavigateBack: () -> Unit,
    collectionsWithCategories: List<CategoryCollectionWithCategories>,
    collectionType: CategoryCollectionType,
    onCategoryTypeChange: (CategoryCollectionType) -> Unit,
    onNavigateToEditCollectionScreen: (CategoryCollectionWithCategories?) -> Unit,
    onSaveCollectionsButton: () -> Unit,
) {
    val settingsCategory = SettingsCategory.CategoryCollections(appTheme = CurrAppTheme)

    ScreenContainerWithTopBackNavButtonAndPrimaryButton(
        screenPadding = screenPadding,
        backNavButtonText = stringResource(settingsCategory.stringRes),
        backNavButtonImageRes = settingsCategory.iconRes,
        onBackNavButtonClick = onNavigateBack,
        primaryButtonText = stringResource(R.string.save),
        onPrimaryButtonClick = onSaveCollectionsButton
    ) {

        CategoryCollectionTypeBar(
            currentType = collectionType,
            onClick = onCategoryTypeChange
        )

        GlassSurface(
            modifier = Modifier.weight(1f)
        ) {
            GlassSurfaceContent(
                collectionWithCategoriesList = collectionsWithCategories,
                onNavigateToEditCollectionScreen = onNavigateToEditCollectionScreen
            )
        }

        SmallSecondaryButton(
            text = stringResource(R.string.add_collection),
            iconRes = R.drawable.add_icon
        ) {
            onNavigateToEditCollectionScreen(null)
        }

    }

    /*GlassSurfaceScreenContainer(
        topPadding = screenPadding.calculateTopPadding(),
        bottomPadding = screenPadding.calculateBottomPadding(),
        topBar = {
            CategoryCollectionTypeBar(
                currentType = collectionType,
                onClick = onCategoryTypeChange
            )
        },
        glassSurfaceContent = {
            GlassSurfaceContent(
                collectionWithCategoriesList = collectionsWithCategories,
                onNavigateToEditCollectionScreen = onNavigateToEditCollectionScreen
            )
        },
        smallPrimaryButton = {
            SmallPrimaryButton(
                text = stringResource(R.string.add_collection),
                iconRes = R.drawable.add_icon
            ) {
                onNavigateToEditCollectionScreen(null)
            }
        },
        primaryBottomButton = {
            PrimaryButton(
                text = stringResource(R.string.save),
                onClick = onSaveCollectionsButton
            )
        }
    )*/
}

@Composable
private fun GlassSurfaceContent(
    collectionWithCategoriesList: List<CategoryCollectionWithCategories>,
    onNavigateToEditCollectionScreen: (CategoryCollectionWithCategories) -> Unit,
) {
    AnimatedContent(
        targetState = collectionWithCategoriesList
    ) { targetCollectionWithCategoriesList ->
        if (targetCollectionWithCategoriesList.isNotEmpty()) {
            if (!WindowTypeIsExpanded) {
                CompactLayoutContent(
                    collectionWithCategoriesList = targetCollectionWithCategoriesList,
                    onNavigateToEditCollectionScreen = onNavigateToEditCollectionScreen
                )
            } else {
                ExpandedLayoutContent(
                    collectionWithCategoriesList = targetCollectionWithCategoriesList,
                    onNavigateToEditCollectionScreen = onNavigateToEditCollectionScreen
                )
            }
        } else {
            MessageContainer(message = stringResource(R.string.no_collections_of_this_type))
        }
    }
}

@Composable
private fun CompactLayoutContent(
    collectionWithCategoriesList: List<CategoryCollectionWithCategories>,
    onNavigateToEditCollectionScreen: (CategoryCollectionWithCategories) -> Unit
) {
    val lazyListState = rememberLazyListState()

    LazyColumn(
        state = lazyListState,
        contentPadding = PaddingValues(
            dimensionResource(R.dimen.widget_content_padding)
        ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(
            items = collectionWithCategoriesList,
            key = { it.id }
        ) { collection ->
            EditingCategoryCollectionComponent(collection = collection) {
                onNavigateToEditCollectionScreen(collection)
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ExpandedLayoutContent(
    collectionWithCategoriesList: List<CategoryCollectionWithCategories>,
    onNavigateToEditCollectionScreen: (CategoryCollectionWithCategories) -> Unit
) {
    val scrollState = rememberScrollState()

    FlowRow(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxWidth()
            .padding(9.dp)
    ) {
        collectionWithCategoriesList.forEach { collection ->
            Box(modifier = Modifier.padding(9.dp)) {
                EditingCategoryCollectionComponent(collection) {
                    onNavigateToEditCollectionScreen(collection)
                }
            }
        }
    }
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun EditCategoryCollectionsScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault,
    groupedCategoriesByType: GroupedCategoriesByType = DefaultCategoriesPackage(
        LocalContext.current
    ).getDefaultCategories(),
    collectionType: CategoryCollectionType = CategoryCollectionType.Mixed,
    categoryCollectionsWithIdsByType: CategoryCollectionsWithIdsByType? = null,
    collectionWithCategoriesList: List<CategoryCollectionWithCategories> =
        categoryCollectionsWithIdsByType?.toCollectionsWithCategories(
            allCategories = groupedCategoriesByType.asList()
        )?.getByType(collectionType) ?: listOf(
            CategoryCollectionWithCategories(
                id = 1,
                orderNum = 1,
                type = CategoryCollectionType.Mixed,
                name = "Category collection collection 1",
                categoryList = groupedCategoriesByType.expense.let { list ->
                    list.subList(0, 3).map { it.category }
                }
            ),
            CategoryCollectionWithCategories(
                id = 2,
                orderNum = 2,
                type = CategoryCollectionType.Mixed,
                name = "Collection 2",
                categoryList = groupedCategoriesByType.expense.let { list ->
                    list.subList(3, 6).map { it.category }
                }
            ),
            CategoryCollectionWithCategories(
                id = 3,
                orderNum = 3,
                type = CategoryCollectionType.Mixed,
                name = "Category collection 3",
                categoryList = groupedCategoriesByType.expense.let { list ->
                    list.subList(0, list.size).map { it.category }
                }
            ),
        )
) {
    PreviewWithMainScaffoldContainer(appTheme = appTheme) { scaffoldPadding ->
        EditCategoryCollectionsScreen(
            screenPadding = scaffoldPadding,
            onNavigateBack = {},
            collectionsWithCategories = collectionWithCategoriesList,
            collectionType = CategoryCollectionType.Mixed,
            onCategoryTypeChange = {},
            onNavigateToEditCollectionScreen = {},
            onSaveCollectionsButton = {},
        )
    }
}
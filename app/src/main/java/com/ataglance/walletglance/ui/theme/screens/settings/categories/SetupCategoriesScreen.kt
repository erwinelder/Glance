package com.ataglance.walletglance.ui.theme.screens.settings.categories

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.model.CategoryType
import com.ataglance.walletglance.model.Colors
import com.ataglance.walletglance.model.SetupCategoriesUiState
import com.ataglance.walletglance.ui.theme.WindowTypeIsExpanded
import com.ataglance.walletglance.ui.theme.uielements.buttons.PrimaryButton
import com.ataglance.walletglance.ui.theme.uielements.buttons.SecondaryButton
import com.ataglance.walletglance.ui.theme.uielements.containers.CategoriesSetupContainer

@Composable
fun SetupCategoriesScreen(
    scaffoldPadding: PaddingValues,
    isAppSetUp: Boolean,
    uiState: SetupCategoriesUiState,
    categoryNameAndIconMap: Map<String, Int>,
    categoryColorNameToColorMap: Map<String, Colors>,
    onResetButton: () -> Unit,
    onSaveAndFinishSetupButton: () -> Unit,
    onShowCategoriesByType: (CategoryType) -> Unit,
    onNavigateToEditSubcategoryListScreen: (Int) -> Unit,
    onNavigateToEditCategoryScreen: (Int) -> Unit,
    onSwapCategories: (Int, Int) -> Unit,
    onAddNewCategory: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(
                top = if (isAppSetUp) { 0.dp } else { scaffoldPadding.calculateTopPadding() } +
                    dimensionResource(R.dimen.buttons_gap),
                bottom = dimensionResource(R.dimen.screen_vertical_padding)
            )
    ) {
        CategoriesSetupContainer(
            uiState = uiState,
            categoryNameAndIconMap = categoryNameAndIconMap,
            categoryColorNameToColorMap = categoryColorNameToColorMap,
            onShowCategoriesByType = onShowCategoriesByType,
            onNavigateToEditSubcategoryListScreen = onNavigateToEditSubcategoryListScreen,
            onNavigateToEditCategoryScreen = onNavigateToEditCategoryScreen,
            onSwapCategories = onSwapCategories,
            onAddNewCategory = onAddNewCategory
        )
        Spacer(modifier = Modifier.height(dimensionResource(R.dimen.widgets_gap)))
        if (!WindowTypeIsExpanded) {
            SecondaryButton(
                onClick = onResetButton,
                text = stringResource(R.string.reset)
            )
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.buttons_gap)))
            PrimaryButton(
                onClick = onSaveAndFinishSetupButton,
                text = stringResource(if (isAppSetUp) R.string.save else R.string.save_and_finish)
            )
        } else {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                SecondaryButton(
                    onClick = onResetButton,
                    text = stringResource(R.string.reset)
                )
                Spacer(modifier = Modifier.width(dimensionResource(R.dimen.buttons_gap)))
                PrimaryButton(
                    onClick = onSaveAndFinishSetupButton,
                    text = stringResource(if (isAppSetUp) R.string.save else R.string.save_and_finish)
                )
            }
        }
    }
}
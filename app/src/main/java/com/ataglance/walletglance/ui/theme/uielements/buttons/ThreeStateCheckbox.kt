package com.ataglance.walletglance.ui.theme.uielements.buttons

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.data.app.AppTheme
import com.ataglance.walletglance.data.categories.Category
import com.ataglance.walletglance.data.categories.CategoryType
import com.ataglance.walletglance.data.categories.DefaultCategoriesPackage
import com.ataglance.walletglance.data.categories.color.CategoryColors
import com.ataglance.walletglance.data.categories.icons.CategoryIcon
import com.ataglance.walletglance.data.categoryCollections.CategoryCollectionType
import com.ataglance.walletglance.data.categoryCollections.CategoryCollectionWithCategories
import com.ataglance.walletglance.ui.theme.GlanceTheme
import com.ataglance.walletglance.ui.theme.WalletGlanceTheme
import com.ataglance.walletglance.ui.theme.screens.settings.categoryCollections.EditCategoryCollectionScreen
import com.ataglance.walletglance.ui.utils.toCategoryColorWithName

@Composable
fun ThreeStateCheckbox(
    state: Boolean?,
    onClick: (Boolean?) -> Unit
) {
    val iconRes = when (state) {
        true -> R.drawable.checked_icon
        false -> null
        else -> R.drawable.partly_checked_icon
    }
    val checkboxBackground by animateColorAsState(
        targetValue = GlanceTheme.primary.copy(if (state == false) 0f else 1f),
        label = "three state checkbox background color",
        animationSpec = tween(150, 0)
    )
    val checkboxBorderColor by animateColorAsState(
        targetValue = GlanceTheme.outline.copy(if (state == false) 1f else 0f),
        label = "three state checkbox border color",
        animationSpec = tween(150, 0)
    )

    FilledIconButton(
        onClick = {
            onClick(state?.let { !state } ?: true)
        },
        shape = RoundedCornerShape(15),
        colors = IconButtonDefaults.iconButtonColors(
            containerColor = checkboxBackground
        ),
        modifier = Modifier
            .border(2.dp, checkboxBorderColor, RoundedCornerShape(15))
            .size(30.dp)
    ) {
        AnimatedContent(
            targetState = iconRes,
            label = "three state checkbox icon",
            transitionSpec = {
                (fadeIn(tween(150, 70)) +
                        scaleIn(tween(150, 70), .92f))
                    .togetherWith(fadeOut(tween(70)))
            },
            contentAlignment = Alignment.Center
        ) { targetIconRes ->
            targetIconRes?.let {
                Icon(
                    painter = painterResource(targetIconRes),
                    contentDescription = "",
                    tint = GlanceTheme.background,
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    }
}



@Preview
@Composable
private fun ThreeStateCheckboxPreview() {
    BoxWithConstraints {
        WalletGlanceTheme(
            useDeviceTheme = false,
            lastChosenTheme = AppTheme.LightDefault.name,
            boxWithConstraintsScope = this
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(R.drawable.main_background_light),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize()
                )
                ThreeStateCheckbox(
                    state = true,
                    onClick = {}
                )
            }
        }
    }
}

@Preview
@Composable
private fun EditCategoryCollectionScreenPreview() {
    val categoryList = listOf(
        Category(
            id = 13,
            type = CategoryType.Expense,
            orderNum = 1,
            parentCategoryId = 1,
            name = "Groceries",
            icon = CategoryIcon.Other,
            colorWithName = CategoryColors.Olive.toCategoryColorWithName()
        ),
        Category(
            id = 14,
            type = CategoryType.Expense,
            orderNum = 2,
            parentCategoryId = null,
            name = "Category 2",
            icon = CategoryIcon.Other,
            colorWithName = CategoryColors.Olive.toCategoryColorWithName()
        ),
        Category(
            id = 25,
            type = CategoryType.Expense,
            orderNum = 3,
            parentCategoryId = null,
            name = "Category 3",
            icon = CategoryIcon.Other,
            colorWithName = CategoryColors.Olive.toCategoryColorWithName()
        ),
        Category(
            id = 30,
            type = CategoryType.Expense,
            orderNum = 3,
            parentCategoryId = null,
            name = "Category 3",
            icon = CategoryIcon.Other,
            colorWithName = CategoryColors.Olive.toCategoryColorWithName()
        )
    )
    val collection = CategoryCollectionWithCategories(
        id = 1,
        orderNum = 1,
        type = CategoryCollectionType.Expense,
        name = "Collection",
        categoryList = categoryList
    )

    BoxWithConstraints {
        WalletGlanceTheme(
            useDeviceTheme = false,
            lastChosenTheme = AppTheme.LightDefault.name,
            boxWithConstraintsScope = this
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxSize()
            ) {
                Image(
                    painter = painterResource(R.drawable.main_background_light),
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize()
                )
                EditCategoryCollectionScreen(
                    collection = collection,
                    editingCategoriesWithSubcategories =
                    DefaultCategoriesPackage(LocalContext.current).getDefaultCategories()
                        .toEditingCategoriesWithSubcategories(collection),
                    expandedCategory = null,
                    allowDeleting = true,
                    allowSaving = true,
                    onNameChange = {},
                    onCheckedChange = {},
                    onExpandedChange = {},
                    onDeleteButton = {},
                    onSaveButton = {}
                )
            }
        }
    }
}
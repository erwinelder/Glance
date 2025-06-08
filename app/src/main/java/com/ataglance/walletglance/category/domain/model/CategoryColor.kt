package com.ataglance.walletglance.category.domain.model

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.ataglance.walletglance.category.domain.mapper.toColorWithName
import com.ataglance.walletglance.category.presentation.screen.EditCategoriesScreen
import com.ataglance.walletglance.category.presentation.model.SetupCategoriesUiState
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.color.ColorWithName
import com.ataglance.walletglance.core.domain.color.LighterDarkerColors
import com.ataglance.walletglance.core.domain.color.LighterDarkerColorsByTheme
import com.ataglance.walletglance.core.presentation.component.screenContainer.PreviewContainer

sealed class CategoryColor(val name: CategoryColorName, val color: LighterDarkerColorsByTheme) {

    data object Olive : CategoryColor(
        CategoryColorName.Olive,
        LighterDarkerColorsByTheme(
            lightDefault = LighterDarkerColors(
                Color(185, 207, 147), Color(163, 182, 129)
            ),
            darkDefault = LighterDarkerColors(
                Color(132, 145, 111), Color(109, 120, 92)
            )
        )
    )

    data object Camel : CategoryColor(
        CategoryColorName.Camel,
        LighterDarkerColorsByTheme(
            lightDefault = LighterDarkerColors(
                Color(232, 195, 144), Color(207, 175, 129)
            ),
            darkDefault = LighterDarkerColors(
                Color(175, 144, 96), Color(150, 123, 83)
            )
        )
    )

    data object Pink : CategoryColor(
        CategoryColorName.Pink,
        LighterDarkerColorsByTheme(
            lightDefault = LighterDarkerColors(
                Color(250, 167, 211), Color(224, 150, 190)
            ),
            darkDefault = LighterDarkerColors(
                Color(185, 114, 153), Color(161, 100, 133)
            )
        )
    )

    data object Green : CategoryColor(
        CategoryColorName.Green,
        LighterDarkerColorsByTheme(
            lightDefault = LighterDarkerColors(
                Color(157, 227, 164), Color(138, 201, 144)
            ),
            darkDefault = LighterDarkerColors(
                Color(118, 156, 121), Color(99, 130, 101)
            )
        )
    )

    data object Red : CategoryColor(
        CategoryColorName.Red,
        LighterDarkerColorsByTheme(
            lightDefault = LighterDarkerColors(
                Color(255, 148, 148), Color(230, 133, 133)
            ),
            darkDefault = LighterDarkerColors(
                Color(194, 100, 100), Color(168, 88, 88)
            )
        )
    )

    data object LightBlue : CategoryColor(
        CategoryColorName.LightBlue,
        LighterDarkerColorsByTheme(
            lightDefault = LighterDarkerColors(
                Color(150, 197, 250), Color(134, 176, 224)
            ),
            darkDefault = LighterDarkerColors(
                Color(109, 144, 185), Color(95, 126, 161)
            )
        )
    )

    data object Lavender : CategoryColor(
        CategoryColorName.Lavender,
        LighterDarkerColorsByTheme(
            lightDefault = LighterDarkerColors(
                Color(218, 162, 250), Color(195, 146, 224)
            ),
            darkDefault = LighterDarkerColors(
                Color(162, 121, 187), Color(139, 104, 161)
            )
        )
    )

    data object Blue : CategoryColor(
        CategoryColorName.Blue,
        LighterDarkerColorsByTheme(
            lightDefault = LighterDarkerColors(
                Color(154, 157, 245), Color(138, 141, 219)
            ),
            darkDefault = LighterDarkerColors(
                Color(119, 122, 190), Color(104, 107, 166)
            )
        )
    )

    data object Aquamarine : CategoryColor(
        CategoryColorName.Aquamarine,
        LighterDarkerColorsByTheme(
            lightDefault = LighterDarkerColors(
                Color(135, 232, 196), Color(120, 206, 174)
            ),
            darkDefault = LighterDarkerColors(
                Color(110, 173, 150), Color(95, 148, 128)
            )
        )
    )

    data object Orange : CategoryColor(
        CategoryColorName.Orange,
        LighterDarkerColorsByTheme(
            lightDefault = LighterDarkerColors(
                Color(255, 186, 145), Color(230, 167, 131)
            ),
            darkDefault = LighterDarkerColors(
                Color(194, 132, 96), Color(168, 114, 82)
            )
        )
    )

    data object Yellow : CategoryColor(
        CategoryColorName.Yellow,
        LighterDarkerColorsByTheme(
            lightDefault = LighterDarkerColors(
                Color(247, 226, 116), Color(223, 203, 104)
            ),
            darkDefault = LighterDarkerColors(
                Color(184, 156, 76), Color(158, 133, 65)
            )
        )
    )

    data object GrayDefault : CategoryColor(
        CategoryColorName.GrayDefault,
        LighterDarkerColorsByTheme(
            lightDefault = LighterDarkerColors(
                Color(186, 186, 186), Color(160, 160, 160)
            ),
            darkDefault = LighterDarkerColors(
                Color(130, 130, 130), Color(105, 105, 105)
            )
        )
    )


    fun getNameValue(): String {
        return name.name
    }

    fun getColorByTheme(theme: AppTheme): LighterDarkerColors {
        return color.getByTheme(theme)
    }

    companion object {

        private fun getAll(): List<CategoryColor> {
            return listOf(
                Olive,
                Camel,
                Pink,
                Green,
                Red,
                LightBlue,
                Lavender,
                Blue,
                Aquamarine,
                Orange,
                Yellow,
                GrayDefault
            )
        }

        fun getByName(name: String): CategoryColor {
            return when (name) {
                CategoryColorName.Olive.name -> Olive
                CategoryColorName.Camel.name -> Camel
                CategoryColorName.Pink.name -> Pink
                CategoryColorName.Green.name -> Green
                CategoryColorName.Red.name -> Red
                CategoryColorName.LightBlue.name -> LightBlue
                CategoryColorName.Lavender.name -> Lavender
                CategoryColorName.Blue.name -> Blue
                CategoryColorName.Aquamarine.name -> Aquamarine
                CategoryColorName.Orange.name -> Orange
                CategoryColorName.Yellow.name -> Yellow
                CategoryColorName.GrayDefault.name -> GrayDefault
                else -> GrayDefault
            }
        }

        fun asColorWithNameList(theme: AppTheme): List<ColorWithName> {
            return getAll().map { it.toColorWithName(theme) }
        }

    }

}


@Preview(heightDp = 1520)
@Composable
private fun Preview() {
    val categoriesWithSubcategories = DefaultCategoriesPackage(LocalContext.current)
        .getDefaultCategories()

    PreviewContainer(appTheme = AppTheme.DarkDefault) {
        EditCategoriesScreen(
            screenPadding = PaddingValues(),
            isAppSetUp = true,
            uiState = SetupCategoriesUiState(
                groupedCategoriesByType = categoriesWithSubcategories
            ),
            onResetButton = {},
            onSaveAndFinishSetupButton = {},
            onShowCategoriesByType = {},
            onNavigateToEditSubcategoriesScreen = {},
            onNavigateToEditCategoryScreen = {},
            onSwapCategories = { _, _ -> }
        )
    }
}
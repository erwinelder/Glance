package com.ataglance.walletglance.data.categories.color

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import com.ataglance.walletglance.data.app.AppTheme
import com.ataglance.walletglance.data.categories.DefaultCategoriesPackage
import com.ataglance.walletglance.data.color.LighterDarkerColors
import com.ataglance.walletglance.data.color.LighterDarkerColorsByTheme
import com.ataglance.walletglance.ui.theme.screens.settings.categories.SetupCategoriesScreen
import com.ataglance.walletglance.ui.theme.uielements.containers.PreviewContainer
import com.ataglance.walletglance.ui.viewmodels.categories.SetupCategoriesUiState

sealed class CategoryColors(val name: CategoryColorName, val color: LighterDarkerColorsByTheme) {

    data object Olive : CategoryColors(
        CategoryColorName.Olive,
        LighterDarkerColorsByTheme(
            lightDefault = LighterDarkerColors(
                Color(185, 207, 147), Color(163, 182, 129)
            ),
            darkDefault = LighterDarkerColors(
                Color(109, 120, 92), Color(132, 145, 111)
            )
        )
    )

    data object Camel : CategoryColors(
        CategoryColorName.Camel,
        LighterDarkerColorsByTheme(
            lightDefault = LighterDarkerColors(
                Color(232, 195, 144), Color(207, 175, 129)
            ),
            darkDefault = LighterDarkerColors(
                Color(150, 123, 83), Color(175, 144, 96)
            )
        )
    )

    data object Pink : CategoryColors(
        CategoryColorName.Pink,
        LighterDarkerColorsByTheme(
            lightDefault = LighterDarkerColors(
                Color(250, 167, 211), Color(224, 150, 190)
            ),
            darkDefault = LighterDarkerColors(
                Color(161, 100, 133), Color(185, 114, 153)
            )
        )
    )

    data object Green : CategoryColors(
        CategoryColorName.Green,
        LighterDarkerColorsByTheme(
            lightDefault = LighterDarkerColors(
                Color(157, 227, 164), Color(138, 201, 144)
            ),
            darkDefault = LighterDarkerColors(
                Color(99, 130, 101), Color(118, 156, 121)
            )
        )
    )

    data object Red : CategoryColors(
        CategoryColorName.Red,
        LighterDarkerColorsByTheme(
            lightDefault = LighterDarkerColors(
                Color(255, 148, 148), Color(230, 133, 133)
            ),
            darkDefault = LighterDarkerColors(
                Color(168, 88, 88), Color(194, 100, 100)
            )
        )
    )

    data object LightBlue : CategoryColors(
        CategoryColorName.LightBlue,
        LighterDarkerColorsByTheme(
            lightDefault = LighterDarkerColors(
                Color(150, 197, 250), Color(134, 176, 224)
            ),
            darkDefault = LighterDarkerColors(
                Color(95, 126, 161), Color(109, 144, 185)
            )
        )
    )

    data object Lavender : CategoryColors(
        CategoryColorName.Lavender,
        LighterDarkerColorsByTheme(
            lightDefault = LighterDarkerColors(
                Color(218, 162, 250), Color(195, 146, 224)
            ),
            darkDefault = LighterDarkerColors(
                Color(139, 104, 161), Color(162, 121, 187)
            )
        )
    )

    data object Blue : CategoryColors(
        CategoryColorName.Blue,
        LighterDarkerColorsByTheme(
            lightDefault = LighterDarkerColors(
                Color(154, 157, 245), Color(138, 141, 219)
            ),
            darkDefault = LighterDarkerColors(
                Color(104, 107, 166), Color(119, 122, 190)
            )
        )
    )

    data object Aquamarine : CategoryColors(
        CategoryColorName.Aquamarine,
        LighterDarkerColorsByTheme(
            lightDefault = LighterDarkerColors(
                Color(135, 232, 196), Color(120, 206, 174)
            ),
            darkDefault = LighterDarkerColors(
                Color(95, 148, 128), Color(110, 173, 150)
            )
        )
    )

    data object Orange : CategoryColors(
        CategoryColorName.Orange,
        LighterDarkerColorsByTheme(
            lightDefault = LighterDarkerColors(
                Color(255, 186, 145), Color(230, 167, 131)
            ),
            darkDefault = LighterDarkerColors(
                Color(168, 114, 82), Color(194, 132, 96)
            )
        )
    )

    data object Yellow : CategoryColors(
        CategoryColorName.Yellow,
        LighterDarkerColorsByTheme(
            lightDefault = LighterDarkerColors(
                Color(247, 226, 116), Color(223, 203, 104)
            ),
            darkDefault = LighterDarkerColors(
                Color(158, 133, 65), Color(184, 156, 76)
            )
        )
    )

    data object GrayDefault : CategoryColors(
        CategoryColorName.GrayDefault,
        LighterDarkerColorsByTheme(
            lightDefault = LighterDarkerColors(
                Color(186, 186, 186), Color(160, 160, 160)
            ),
            darkDefault = LighterDarkerColors(
                Color(105, 105, 105), Color(130, 130, 130)
            )
        )
    )

}



@Preview(heightDp = 1520)
@Composable
private fun Preview() {
    PreviewContainer(AppTheme.DarkDefault) {
        val context = LocalContext.current
        SetupCategoriesScreen(
            scaffoldPadding = PaddingValues(),
            appTheme = AppTheme.DarkDefault,
            isAppSetUp = true,
            uiState = SetupCategoriesUiState(
                categoriesWithSubcategories = DefaultCategoriesPackage(context)
                    .getDefaultCategories()
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
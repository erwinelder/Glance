package com.ataglance.walletglance.core.presentation.components.containers

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.navigation.domain.model.BottomBarNavigationButtons
import com.ataglance.walletglance.navigation.domain.model.MainScreens

@Composable
fun BottomNavBar(
    appTheme: AppTheme?,
    isVisible: Boolean,
    anyScreenInHierarchyIsScreenProvider: (Any) -> Boolean,
    currentScreenIsScreenProvider: (Any) -> Boolean,
    onNavigateToScreen: (MainScreens) -> Unit,
    onMakeRecordButtonClick: () -> Unit,
    barButtons: List<BottomBarNavigationButtons>
) {
    val bottomBarButtons by remember {
        derivedStateOf { barButtons.take(3) }
    }
    val popupListBarButtons by remember {
        derivedStateOf {
            barButtons.subList(3, barButtons.size).reversed()
        }
    }

    GlanceBottomNavBar(
        appTheme = appTheme,
        isVisible = isVisible,
        anyScreenInHierarchyIsScreenProvider = anyScreenInHierarchyIsScreenProvider,
        currentScreenIsScreenProvider = currentScreenIsScreenProvider,
        onNavigateToScreen = onNavigateToScreen,
        onFloatingButtonClick = onMakeRecordButtonClick,
        bottomBarButtons = bottomBarButtons,
        popupListBarButtons = popupListBarButtons
    )
}



@Preview
@Composable
private fun BottomNavBarPreview() {
    val appTheme = AppTheme.LightDefault
    val barButtons = listOf(
        BottomBarNavigationButtons.Home,
        BottomBarNavigationButtons.Records,
        BottomBarNavigationButtons.CategoryStatistics,
        BottomBarNavigationButtons.Budgets,
        BottomBarNavigationButtons.Settings
    )

    PreviewContainer(appTheme = appTheme) {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier.fillMaxSize()
        ) {
            BottomNavBar(
                appTheme = appTheme,
                isVisible = true,
                anyScreenInHierarchyIsScreenProvider = { false },
                currentScreenIsScreenProvider = { false },
                onNavigateToScreen = {},
                onMakeRecordButtonClick = {},
                barButtons = barButtons
            )
        }
    }
}

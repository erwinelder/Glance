package com.ataglance.walletglance.core.presentation.component.container

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
import com.ataglance.walletglance.core.presentation.component.screenContainer.PreviewContainer
import com.ataglance.walletglance.core.domain.navigation.MainScreens
import com.ataglance.walletglance.navigation.domain.model.BottomBarNavigationButton

@Composable
fun BottomNavBar(
    isVisible: Boolean,
    anyScreenInHierarchyIsScreenProvider: (Any) -> Boolean,
    currentScreenIsScreenProvider: (Any) -> Boolean,
    onNavigateToScreen: (MainScreens) -> Unit,
    onMakeRecordButtonClick: () -> Unit,
    barButtons: List<BottomBarNavigationButton>
) {
    val bottomBarButtons by remember(barButtons) {
        derivedStateOf { barButtons.take(3) }
    }
    val popupListBarButtons by remember(barButtons) {
        derivedStateOf {
            barButtons.subList(3, barButtons.size).reversed()
        }
    }

    GlanceBottomNavBar(
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
    val barButtons = listOf(
        BottomBarNavigationButton.Home,
        BottomBarNavigationButton.Records,
        BottomBarNavigationButton.CategoryStatistics,
        BottomBarNavigationButton.Budgets,
        BottomBarNavigationButton.Settings
    )

    PreviewContainer(appTheme = AppTheme.LightDefault) {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier.fillMaxSize()
        ) {
            BottomNavBar(
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

package com.ataglance.walletglance.core.presentation.components.screenContainers

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.ataglance.walletglance.core.presentation.components.containers.BottomNavBar
import com.ataglance.walletglance.core.domain.navigation.MainScreens
import com.ataglance.walletglance.navigation.domain.model.BottomBarNavigationButton

@Composable
fun MainScaffold(
    isBottomBarVisible: Boolean,
    onNavigateToScreenAndPopUp: (MainScreens) -> Unit,
    onMakeRecordButtonClick: () -> Unit,
    anyScreenInHierarchyIsScreenProvider: (Any) -> Boolean,
    currentScreenIsScreenProvider: (Any) -> Boolean,
    bottomBarButtons: List<BottomBarNavigationButton>,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomNavBar(
                isVisible = isBottomBarVisible,
                anyScreenInHierarchyIsScreenProvider = anyScreenInHierarchyIsScreenProvider,
                currentScreenIsScreenProvider = currentScreenIsScreenProvider,
                onNavigateToScreen = onNavigateToScreenAndPopUp,
                onMakeRecordButtonClick = onMakeRecordButtonClick,
                barButtons = bottomBarButtons
            )
        },
        containerColor = Color.Transparent
    ) { scaffoldPadding ->
        content(scaffoldPadding)
    }
}
package com.ataglance.walletglance.core.presentation.components.containers

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.ataglance.walletglance.core.domain.componentState.SetupProgressTopBarUiState
import com.ataglance.walletglance.core.navigation.MainScreens
import com.ataglance.walletglance.navigation.domain.model.BottomBarNavigationButtons
import com.ataglance.walletglance.settings.presentation.components.SetupProgressTopBar

@Composable
fun MainScaffold(
    setupProgressTopBarUiState: SetupProgressTopBarUiState,
    isBottomBarVisible: Boolean,
    onNavigateBack: () -> Unit,
    onNavigateToScreenAndPopUp: (MainScreens) -> Unit,
    onMakeRecordButtonClick: () -> Unit,
    anyScreenInHierarchyIsScreenProvider: (Any) -> Boolean,
    currentScreenIsScreenProvider: (Any) -> Boolean,
    bottomBarButtons: List<BottomBarNavigationButtons>,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        topBar = {
            SetupProgressTopBar(
                uiState = setupProgressTopBarUiState,
                onBackNavigationButton = onNavigateBack
            )
        },
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
package com.ataglance.walletglance.core.presentation.components.containers

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.componentState.SetupProgressTopBarUiState
import com.ataglance.walletglance.core.navigation.BottomBarNavigationButtons
import com.ataglance.walletglance.core.navigation.MainScreens
import com.ataglance.walletglance.settings.presentation.components.SetupProgressTopBar

@Composable
fun MainScaffold(
    appTheme: AppTheme?,
    setupProgressTopBarUiState: SetupProgressTopBarUiState,
    isBottomBarVisible: Boolean,
    onNavigateBack: () -> Unit,
    onNavigateToScreen: (MainScreens) -> Unit,
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
                appTheme = appTheme,
                isVisible = isBottomBarVisible,
                anyScreenInHierarchyIsScreenProvider = anyScreenInHierarchyIsScreenProvider,
                currentScreenIsScreenProvider = currentScreenIsScreenProvider,
                onNavigateToScreen = onNavigateToScreen,
                onMakeRecordButtonClick = onMakeRecordButtonClick,
                barButtons = bottomBarButtons
            )
        },
        containerColor = Color.Transparent
    ) { scaffoldPadding ->
        content(scaffoldPadding)
    }
}
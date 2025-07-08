package com.ataglance.walletglance.core.presentation.component.screenContainer

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import com.ataglance.walletglance.core.presentation.component.container.BottomNavBarWrapper
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel

@Composable
fun MainScaffold(
    navViewModel: NavigationViewModel,
    navController: NavController,
    navBackStackEntry: NavBackStackEntry?,
    content: @Composable (PaddingValues) -> Unit
) {
    Scaffold(
        bottomBar = {
            BottomNavBarWrapper(
                navViewModel = navViewModel,
                navController = navController,
                navBackStackEntry = navBackStackEntry
            )
        },
        containerColor = Color.Transparent
    ) { scaffoldPadding ->
        content(scaffoldPadding)
    }
}
package com.ataglance.walletglance.core.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.ataglance.walletglance.core.domain.app.AppConfiguration
import com.ataglance.walletglance.core.domain.app.AppUiState
import com.ataglance.walletglance.core.presentation.component.picker.DateRangeAssetsPickerContainer
import com.ataglance.walletglance.core.presentation.component.screenContainer.DimmedBackgroundOverlay
import com.ataglance.walletglance.core.presentation.component.screenContainer.MainScaffold
import com.ataglance.walletglance.core.presentation.viewmodel.AppViewModel
import com.ataglance.walletglance.navigation.presentation.AppNavHost
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainAppContent(
    appConfiguration: AppConfiguration,
    navController: NavHostController,
    appViewModel: AppViewModel
) {
    val navViewModel = koinViewModel<NavigationViewModel>()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val moveScreenTowardsLeft by navViewModel.moveScreensTowardsLeft.collectAsStateWithLifecycle()
    LaunchedEffect(appConfiguration.isSetUp, navBackStackEntry) {
        navViewModel.updateBottomBarVisibility(
            isSetUp = appConfiguration.isSetUp, navBackStackEntry = navBackStackEntry
        )
    }
    LaunchedEffect(navBackStackEntry) {
        navViewModel.updateBottomBarActiveButton(navBackStackEntry = navBackStackEntry)
    }

    val accountsUiState by appViewModel.accountsAndActiveOne.collectAsState()
    val dateRangeMenuUiState by appViewModel.dateRangeWithEnum.collectAsStateWithLifecycle()
    val widgetNames by appViewModel.widgetNames.collectAsStateWithLifecycle()

    var dimBackground by remember { mutableStateOf(false) }
    var openCustomDateRangeWindow by remember { mutableStateOf(false) }

    val appUiState by remember(accountsUiState, dateRangeMenuUiState) {
        derivedStateOf {
            AppUiState(
                accountsAndActiveOne = accountsUiState,
                dateRangeWithEnum = dateRangeMenuUiState
            )
        }
    }

    Box {
        MainScaffold(
            navViewModel = navViewModel,
            navController = navController,
            navBackStackEntry = navBackStackEntry
        ) { scaffoldPadding ->
            AppNavHost(
                screenPadding = scaffoldPadding,
                navController = navController,
                navViewModel = navViewModel,
                appViewModel = appViewModel,
                moveScreenTowardsLeft = moveScreenTowardsLeft,
                appConfiguration = appConfiguration,
                appUiState = appUiState,
                widgetNames = widgetNames,
                openCustomDateRangeWindow = openCustomDateRangeWindow,
                onCustomDateRangeButtonClick = {
                    openCustomDateRangeWindow = !openCustomDateRangeWindow
                },
                onDimBackgroundChange = { dimBackground = it }
            )
            DateRangeAssetsPickerContainer(
                scaffoldPadding = scaffoldPadding,
                dateRangeWithEnum = dateRangeMenuUiState,
                openCustomDateRangeWindow = openCustomDateRangeWindow,
                onCloseCustomDateRangeWindow = { openCustomDateRangeWindow = false },
                onDateRangeSelect = appViewModel::selectDateRange,
                onCustomDateRangeSelect = appViewModel::selectCustomDateRange
            )
        }
        DimmedBackgroundOverlay(visible = dimBackground)
    }
}

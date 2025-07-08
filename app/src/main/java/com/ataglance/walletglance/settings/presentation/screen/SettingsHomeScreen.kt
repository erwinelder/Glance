package com.ataglance.walletglance.settings.presentation.screen

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.ataglance.walletglance.R
import com.ataglance.walletglance.auth.domain.model.user.UserContext
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.preview.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.theme.CurrAppTheme
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.core.presentation.theme.GlanciTypography
import com.ataglance.walletglance.core.presentation.theme.Manrope
import com.ataglance.walletglance.core.presentation.theme.NotoSans
import com.ataglance.walletglance.core.presentation.theme.WindowTypeIsExpanded
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import com.ataglance.walletglance.settings.presentation.component.NavigateToSettingsCategoryButton
import com.ataglance.walletglance.settings.presentation.model.SettingsCategory
import org.koin.compose.koinInject

@Composable
fun SettingsHomeScreenWrapper(
    screenPadding: PaddingValues,
    navController: NavHostController,
    navViewModel: NavigationViewModel
) {
    val isSignedIn = koinInject<UserContext>().isSignedIn()

    SettingsHomeScreen(
        screenPadding = screenPadding,
        isSignedIn = isSignedIn,
        onNavigateToScreen = { screen ->
            navViewModel.navigateToScreen(navController = navController, screen = screen)
        }
    )
}

@Composable
fun SettingsHomeScreen(
    screenPadding: PaddingValues = PaddingValues(),
    isSignedIn: Boolean,
    onNavigateToScreen: (Any) -> Unit
) {
    val appTheme = CurrAppTheme
    val settingsCategories = remember(appTheme, isSignedIn) {
        SettingsCategory.asList(appTheme = appTheme, isSignedIn = isSignedIn)
    }

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 16.dp + screenPadding.calculateTopPadding())
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.settings),
                color = GlanciColors.onSurface,
                style = GlanciTypography.titleMedium,
                fontFamily = NotoSans
            )
            Text(
                text = stringResource(R.string.version) + " 5.0 alpha 2",
                color = GlanciColors.onSurface,
                fontSize = 16.sp,
                letterSpacing = 0.sp,
                fontFamily = Manrope
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        if (!WindowTypeIsExpanded) {
            CompactLayout(
                screenPadding = screenPadding,
                settingsCategories = settingsCategories,
                onNavigateToScreen = onNavigateToScreen
            )
        } else {
            ExpandedLayout(
                screenPadding = screenPadding,
                settingsCategories = settingsCategories,
                onNavigateToScreen = onNavigateToScreen
            )
        }
    }
}

@Composable
private fun CompactLayout(
    screenPadding: PaddingValues,
    settingsCategories: List<SettingsCategory>,
    onNavigateToScreen: (Any) -> Unit
) {
    val lazyListState = rememberLazyListState(initialFirstVisibleItemScrollOffset = 1800)

    val categories by remember(settingsCategories) {
        derivedStateOf { settingsCategories.reversed() }
    }

    LazyColumn(
        state = lazyListState,
        contentPadding = PaddingValues(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(items = categories) { category ->
            NavigateToSettingsCategoryButton(
                category = category,
                onNavigateToScreen = onNavigateToScreen
            )
        }
        item {
            BottomSpacer(padding = screenPadding.calculateBottomPadding())
        }
    }
}

@Composable
private fun ExpandedLayout(
    screenPadding: PaddingValues,
    settingsCategories: List<SettingsCategory>,
    onNavigateToScreen: (Any) -> Unit
) {
    val lazyGridState = rememberLazyGridState()

    LazyVerticalGrid(
        columns = GridCells.Adaptive(300.dp),
        state = lazyGridState,
        contentPadding = PaddingValues(horizontal = 20.dp),
        horizontalArrangement = Arrangement.spacedBy(24.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        reverseLayout = true,
        modifier = Modifier.fillMaxWidth()
    ) {
        repeat(3) {
            item {
                BottomSpacer(padding = screenPadding.calculateBottomPadding())
            }
        }
        items(items = settingsCategories) { category ->
            NavigateToSettingsCategoryButton(
                category = category,
                onNavigateToScreen = onNavigateToScreen
            )
        }
    }
}

@Composable
fun BottomSpacer(padding: Dp) {
    val padding by animateDpAsState(targetValue = padding)

    Spacer(
        modifier = Modifier.height(padding + 8.dp)
    )
}


@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun SettingsHomeScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault,
    isSignedIn: Boolean = true
) {
    PreviewWithMainScaffoldContainer(appTheme = appTheme) { scaffoldPadding ->
        SettingsHomeScreen(
            screenPadding = scaffoldPadding,
            isSignedIn = isSignedIn,
            onNavigateToScreen = {}
        )
    }
}
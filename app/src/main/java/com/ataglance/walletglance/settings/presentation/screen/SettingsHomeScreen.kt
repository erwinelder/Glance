package com.ataglance.walletglance.settings.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.navigation.MainScreens
import com.ataglance.walletglance.core.presentation.components.screenContainers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.theme.CurrAppTheme
import com.ataglance.walletglance.core.presentation.theme.GlanceColors
import com.ataglance.walletglance.core.presentation.theme.Manrope
import com.ataglance.walletglance.core.presentation.theme.Typography
import com.ataglance.walletglance.core.presentation.theme.WindowTypeIsExpanded
import com.ataglance.walletglance.navigation.domain.utils.isScreen
import com.ataglance.walletglance.settings.presentation.components.NavigateToSettingsCategoryButton
import com.ataglance.walletglance.settings.presentation.model.SettingsCategory

@Composable
fun SettingsHomeScreen(
    scaffoldPadding: PaddingValues,
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
            .padding(top = 16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.settings),
                color = GlanceColors.onSurface,
                style = Typography.titleMedium
            )
            Text(
                text = stringResource(R.string.version) + " 4.1",
                color = GlanceColors.onSurface,
                fontSize = 16.sp,
                letterSpacing = 0.sp,
                fontFamily = Manrope
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        if (!WindowTypeIsExpanded) {
            CompactLayout(
                scaffoldPadding = scaffoldPadding,
                settingsCategories = settingsCategories,
                onNavigateToScreen = onNavigateToScreen
            )
        } else {
            ExpandedLayout(
                scaffoldPadding = scaffoldPadding,
                settingsCategories = settingsCategories,
                onNavigateToScreen = onNavigateToScreen
            )
        }
    }
}

@Composable
private fun CompactLayout(
    scaffoldPadding: PaddingValues,
    settingsCategories: List<SettingsCategory>,
    onNavigateToScreen: (Any) -> Unit
) {
    val gap = 16.dp
    val scrollState = rememberScrollState(initial = 1800)
    
    val categories by remember(settingsCategories) {
        derivedStateOf { settingsCategories.reversed() }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(gap),
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
    ) {
        categories.forEach { category ->
            NavigateToSettingsCategoryButton(
                category = category,
                onNavigateToScreen = onNavigateToScreen
            )
        }
        BottomSpacer(scaffoldPadding.calculateBottomPadding())
    }
}

@Composable
private fun ExpandedLayout(
    scaffoldPadding: PaddingValues,
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
            item { BottomSpacer(scaffoldPadding.calculateBottomPadding()) }
        }
        items(items = settingsCategories) { category ->
            NavigateToSettingsCategoryButton(category = category, onNavigateToScreen = onNavigateToScreen)
        }
    }
}

@Composable
fun BottomSpacer(scaffoldBottomPadding: Dp) {
    val bottomPadding by remember {
        derivedStateOf { scaffoldBottomPadding }
    }
    Spacer(
        modifier = Modifier.height(bottomPadding + 8.dp)
    )
}


@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun SettingsHomeScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault,
    isAppSetUp: Boolean = true,
    isBottomBarVisible: Boolean = true,
) {
    PreviewWithMainScaffoldContainer(
        appTheme = appTheme,
        isBottomBarVisible = isBottomBarVisible,
        anyScreenInHierarchyIsScreenProvider = { it.isScreen(MainScreens.Settings) },
        currentScreenIsScreenProvider = { false }
    ) { scaffoldPadding ->
        SettingsHomeScreen(
            scaffoldPadding = scaffoldPadding,
            isSignedIn = false,
            onNavigateToScreen = {}
        )
    }
}
package com.ataglance.walletglance.settings.presentation.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.presentation.GlanceTheme
import com.ataglance.walletglance.core.presentation.Manrope
import com.ataglance.walletglance.core.presentation.WindowTypeIsExpanded
import com.ataglance.walletglance.core.presentation.components.containers.GlassSurface
import com.ataglance.walletglance.core.presentation.components.containers.PreviewWithMainScaffoldContainer
import com.ataglance.walletglance.core.presentation.modifiers.bounceClickEffect
import com.ataglance.walletglance.navigation.utils.isScreen
import com.ataglance.walletglance.navigation.domain.model.MainScreens
import com.ataglance.walletglance.settings.domain.SettingsCategories
import com.ataglance.walletglance.settings.domain.SettingsCategory
import com.ataglance.walletglance.settings.navigation.SettingsScreens

@Composable
fun SettingsHomeScreen(
    scaffoldPadding: PaddingValues,
    appTheme: AppTheme?,
    onNavigateToScreen: (SettingsScreens) -> Unit
) {
    val settingsCategories = remember {
        SettingsCategories(appTheme).let {
            listOf(
                it.accounts,
                it.budgets,
                it.categories,
                it.categoryCollections,
                it.appearance,
                it.language,
                it.resetData
            )
        }
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
                color = GlanceTheme.onSurface,
                fontSize = 30.sp,
                fontFamily = Manrope
            )
            Text(
                text = stringResource(R.string.version) + " 3.0",
                color = GlanceTheme.onSurface,
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
    onNavigateToScreen: (SettingsScreens) -> Unit
) {
    val gap = 20.dp
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
            SettingPlate(category = category) {
                onNavigateToScreen(category.screen)
            }
        }
        BottomSpacer(scaffoldPadding.calculateBottomPadding())
    }
}

@Composable
private fun ExpandedLayout(
    scaffoldPadding: PaddingValues,
    settingsCategories: List<SettingsCategory>,
    onNavigateToScreen: (SettingsScreens) -> Unit
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
            SettingPlate(category = category) {
                onNavigateToScreen(category.screen)
            }
        }
    }
}

@Composable
fun SettingPlate(
    category: SettingsCategory,
    onClick: () -> Unit
) {
    GlassSurface(
        filledWidths = FilledWidthByScreenType(1f, .75f, .75f),
        cornerSize = dimensionResource(R.dimen.settings_category_plate_corner_size),
        modifier = Modifier.bounceClickEffect(.98f, onClick = onClick)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 2.dp,
                    color = GlanceTheme.outlineVariant,
                    shape = RoundedCornerShape(dimensionResource(R.dimen.settings_category_plate_corner_size))
                )
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Image(
                painter = painterResource(category.iconRes),
                contentDescription = stringResource(category.stringRes),
                modifier = Modifier.size(46.dp)
            )
            Text(
                text = stringResource(category.stringRes),
                color = GlanceTheme.onSurface,
                fontSize = 21.sp,
                textAlign = TextAlign.Center,
                fontFamily = Manrope
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(R.drawable.short_arrow_right_icon),
                contentDescription = stringResource(category.stringRes),
                tint = GlanceTheme.onSurface,
                modifier = Modifier.size(26.dp)
            )
        }
    }
}

@Composable
fun BottomSpacer(scaffoldBottomPadding: Dp) {
    Spacer(
        modifier = Modifier.height(scaffoldBottomPadding + 8.dp)
    )
}


@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun SettingsHomeScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault,
    isAppSetUp: Boolean = true,
    isSetupProgressTopBarVisible: Boolean = false,
    isBottomBarVisible: Boolean = true,
) {
    PreviewWithMainScaffoldContainer(
        appTheme = appTheme,
        isSetupProgressTopBarVisible = isSetupProgressTopBarVisible,
        isBottomBarVisible = isBottomBarVisible,
        anyScreenInHierarchyIsScreenProvider = { it.isScreen(MainScreens.Settings) },
        currentScreenIsScreenProvider = { false }
    ) { scaffoldPadding ->
        SettingsHomeScreen(
            scaffoldPadding = scaffoldPadding,
            appTheme = appTheme,
            onNavigateToScreen = {}
        )
    }
}
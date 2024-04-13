package com.ataglance.walletglance.ui.theme.screens.settings

import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.model.SettingsCategories
import com.ataglance.walletglance.model.SettingsCategory
import com.ataglance.walletglance.model.SettingsScreen
import com.ataglance.walletglance.ui.theme.GlanceTheme
import com.ataglance.walletglance.ui.theme.Manrope
import com.ataglance.walletglance.ui.theme.WindowTypeIsCompact
import com.ataglance.walletglance.ui.theme.WindowTypeIsExpanded
import com.ataglance.walletglance.ui.theme.animation.bounceClickEffect
import com.ataglance.walletglance.ui.theme.theme.AppTheme
import com.ataglance.walletglance.ui.theme.uielements.containers.GlassSurface

@Composable
fun SettingsHomeScreen(
    scaffoldPadding: PaddingValues,
    appTheme: AppTheme?,
    onNavigateToScreen: (SettingsScreen) -> Unit
) {
    val gap = 20.dp
    val scrollState = rememberScrollState()
    val settingsCategories = SettingsCategories(appTheme)

    Column(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                top = 16.dp,
                bottom = scaffoldPadding.calculateBottomPadding() +
                        dimensionResource(R.dimen.screen_vertical_padding)
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.settings),
                color = GlanceTheme.onSurface,
                fontSize = 30.sp,
                fontFamily = Manrope
            )
            Text(
                text = stringResource(R.string.version) + " 1.0.5",
                color = GlanceTheme.onSurface,
                fontSize = 16.sp,
                letterSpacing = 0.sp,
                fontFamily = Manrope
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        if (!WindowTypeIsExpanded) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(gap),
                modifier = Modifier
                    .verticalScroll(scrollState)
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                SettingPlate(category = settingsCategories.resetData) {
                    onNavigateToScreen(SettingsScreen.Data)
                }
                SettingPlate(category = settingsCategories.language) {
                    onNavigateToScreen(SettingsScreen.Language)
                }
                SettingPlate(category = settingsCategories.appearance) {
                    onNavigateToScreen(SettingsScreen.Appearance)
                }
                SettingPlate(category = settingsCategories.categories) {
                    onNavigateToScreen(SettingsScreen.Categories)
                }
                SettingPlate(category = settingsCategories.accounts) {
                    onNavigateToScreen(SettingsScreen.Accounts)
                }
            }
        } else {
            val lazyGridState = rememberLazyGridState()
            LazyVerticalGrid(
                columns = GridCells.Adaptive(300.dp),
                state = lazyGridState,
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp),
                reverseLayout = true,
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                item {
                    SettingPlate(category = settingsCategories.accounts) {
                        onNavigateToScreen(SettingsScreen.Data)
                    }
                }
                item {
                    SettingPlate(category = settingsCategories.categories) {
                        onNavigateToScreen(SettingsScreen.Language)
                    }
                }
                item {
                    SettingPlate(category = settingsCategories.appearance) {
                        onNavigateToScreen(SettingsScreen.Appearance)
                    }
                }
                item {
                    SettingPlate(category = settingsCategories.language) {
                        onNavigateToScreen(SettingsScreen.Categories)
                    }
                }
                item {
                    SettingPlate(category = settingsCategories.resetData) {
                        onNavigateToScreen(SettingsScreen.Accounts)
                    }
                }
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
        filledWidth = if (WindowTypeIsCompact) 1f else .75f,
        cornerSize = dimensionResource(R.dimen.settings_category_plate_corner_size),
        modifier = Modifier.bounceClickEffect(.98f, onClick = onClick)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    2.dp,
                    GlanceTheme.outlineVariant,
                    RoundedCornerShape(dimensionResource(R.dimen.settings_category_plate_corner_size))
                )
                .padding(horizontal = 16.dp, vertical = 16.dp)
        ) {
            Image(
                painter = painterResource(category.iconRes),
                contentDescription = stringResource(category.stringRes),
                modifier = Modifier
                    .size(50.dp)
            )
            Text(
                text = stringResource(category.stringRes),
                color = GlanceTheme.onSurface,
                fontSize = 22.sp,
                textAlign = TextAlign.Center,
                fontFamily = Manrope
            )
            Spacer(modifier = Modifier.weight(1f))
            Icon(
                painter = painterResource(R.drawable.short_arrow_right_icon),
                contentDescription = stringResource(category.stringRes),
                tint = GlanceTheme.onSurface,
                modifier = Modifier
                    .size(30.dp)
            )
        }
    }
}

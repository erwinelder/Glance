package com.ataglance.walletglance.ui.theme.uielements

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hierarchy
import com.ataglance.walletglance.R
import com.ataglance.walletglance.model.AppScreen
import com.ataglance.walletglance.model.SettingsScreen
import com.ataglance.walletglance.ui.theme.GlanceTheme
import com.ataglance.walletglance.ui.theme.animation.bounceClickEffect

@Composable
fun BottomNavBar(
    isAppSetUp: Boolean,
    navBackStackEntry: NavBackStackEntry?,
    navigateBack: () -> Unit,
    onNavigationButton: (String) -> Unit,
    onMakeRecordButtonClick: () -> Unit,
) {
    val appScreensList = listOf(
        AppScreen.Home,
        null,
        AppScreen.Settings
    )
    val currentScreen = navBackStackEntry?.destination?.route

    AnimatedVisibility(
        visible = isAppSetUp &&
                (currentScreen == AppScreen.Home.route ||
                        currentScreen == SettingsScreen.SettingsHome.route ||
                        currentScreen == SettingsScreen.Language.route ||
                        currentScreen == SettingsScreen.Appearance.route),
        enter = slideInVertically { (it * 1.5).toInt() },
        exit = slideOutVertically { (it * 1.5).toInt() }
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .bounceClickEffect(.985f)
                .fillMaxWidth()
                .padding(bottom = 12.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(28.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .shadow(
                        elevation = 15.dp,
                        shape = RoundedCornerShape(26.dp),
                        ambientColor = GlanceTheme.onSurface
                    )
                    .clip(RoundedCornerShape(26.dp))
                    .background(GlanceTheme.surface)
                    .border(
                        1.dp,
                        GlanceTheme.onSurface.copy(alpha = .05f),
                        RoundedCornerShape(26.dp)
                    )
                    .padding(vertical = 16.dp, horizontal = 28.dp)
            ) {
                appScreensList.forEach { item ->
                    if (item != null) {
                        val iconColor by animateColorAsState(
                            targetValue = if (navBackStackEntry?.destination?.hierarchy?.any { it.route == item.route } == true) {
                                GlanceTheme.primary
                            } else {
                                GlanceTheme.onSurface
                            },
                            label = "bottom bar icon color"
                        )
                        Icon(
                            painter = painterResource(item.iconRes),
                            contentDescription = item.route,
                            tint = iconColor,
                            modifier = Modifier
                                .bounceClickEffect(.97f) {
                                    if (
                                        item == AppScreen.Settings &&
                                        (currentScreen == SettingsScreen.Language.route ||
                                                currentScreen == SettingsScreen.Appearance.route)
                                    ) {
                                        navigateBack()
                                    } else {
                                        onNavigationButton(item.route)
                                    }
                                }
                                .size(36.dp)
                        )
                    } else {
                        Spacer(modifier = Modifier.width(50.dp))
                    }
                }
            }
            Box(
                modifier = Modifier.absoluteOffset(y = -(24).dp)
            ) {
                IconButton(
                    onClick = onMakeRecordButtonClick,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.Transparent,
                        contentColor = GlanceTheme.onSurface,
                        disabledContentColor = Color.Transparent,
                        disabledContainerColor = GlanceTheme.onSurface
                    ),
                    modifier = Modifier
                        .bounceClickEffect()
                        .shadow(
                            elevation = 15.dp,
                            shape = RoundedCornerShape(38),
                            spotColor = GlanceTheme.primaryGradientLightToDark.first
                        )
                        .clip(RoundedCornerShape(38))
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(
                                    GlanceTheme.primaryGradientLightToDark.second,
                                    GlanceTheme.primaryGradientLightToDark.first
                                ),
                                start = Offset(75f, 210f),
                                end = Offset(95f, -10f)
                            )
                        )
                        .padding(8.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.make_record_icon),
                        contentDescription = "make record",
                        tint = GlanceTheme.onPrimary,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }
        }
    }
}
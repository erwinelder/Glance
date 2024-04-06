package com.ataglance.walletglance.ui.theme.uielements

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
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
import com.ataglance.walletglance.model.BottomBarButtons
import com.ataglance.walletglance.model.SettingsScreen
import com.ataglance.walletglance.ui.theme.GlanceTheme
import com.ataglance.walletglance.ui.theme.animation.bounceClickEffect
import com.ataglance.walletglance.ui.theme.theme.AppTheme

@Composable
fun BottomNavBar(
    appTheme: AppTheme?,
    isAppSetUp: Boolean,
    navBackStackEntry: NavBackStackEntry?,
    navigateBack: () -> Unit,
    onNavigationButton: (String) -> Unit,
    onMakeRecordButtonClick: () -> Unit,
) {
    val bottomBarButtonList = listOf(
        Pair(
            BottomBarButtons.HomeInactive(appTheme),
            BottomBarButtons.HomeActive(appTheme)
        ),
        Pair(
            BottomBarButtons.RecordsInactive(appTheme),
            BottomBarButtons.RecordsActive(appTheme)
        ),
        null,
        Pair(
            BottomBarButtons.CategoriesStatisticsInactive(appTheme),
            BottomBarButtons.CategoriesStatisticsActive(appTheme)
        ),
        Pair(
            BottomBarButtons.SettingsInactive(appTheme),
            BottomBarButtons.SettingsActive(appTheme)
        )
    )
    val currentScreenRoute = navBackStackEntry?.destination?.route

    AnimatedVisibility(
        visible = isAppSetUp &&
                (currentScreenRoute == AppScreen.Home.route ||
                currentScreenRoute == AppScreen.Records.route ||
                currentScreenRoute?.startsWith(AppScreen.CategoriesStatistics.route) == true ||
                currentScreenRoute == SettingsScreen.SettingsHome.route ||
                currentScreenRoute == SettingsScreen.Language.route),
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
                horizontalArrangement = Arrangement.spacedBy(24.dp),
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
                bottomBarButtonList.forEach { item ->
                    if (item != null) {
                        AnimatedContent(
                            targetState = if (
                                navBackStackEntry?.destination?.hierarchy?.any {
                                    it.route?.startsWith(item.first.relatedScreen.route) == true
                                } == true
                            ) item.second else item.first,
                            transitionSpec = { fadeIn() togetherWith fadeOut() },
                            label = "bottom bar icon + ${item.first.route}"
                        ) { bottomBarButton ->
                            Image(
                                painter = painterResource(bottomBarButton.iconRes),
                                contentDescription = bottomBarButton.route,
                                modifier = Modifier
                                    .bounceClickEffect(.97f) {
                                        if (
                                            bottomBarButton.route == AppScreen.Settings.route &&
                                            currentScreenRoute == SettingsScreen.Language.route
                                        ) {
                                            navigateBack()
                                        } else {
                                            onNavigationButton(bottomBarButton.route)
                                        }
                                    }
                                    .size(36.dp)
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.width(60.dp))
                    }
                }
            }
            Box(
                modifier = Modifier.absoluteOffset(y = -(20).dp)
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
package com.ataglance.walletglance.presentation.ui.uielements

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import com.ataglance.walletglance.data.app.AppTheme
import com.ataglance.walletglance.presentation.ui.GlanceTheme
import com.ataglance.walletglance.presentation.ui.animation.bounceClickEffect
import com.ataglance.walletglance.presentation.ui.navigation.BottomBarButtons
import com.ataglance.walletglance.presentation.ui.navigation.screens.MainScreens
import com.ataglance.walletglance.presentation.ui.navigation.screens.SettingsScreens
import com.ataglance.walletglance.domain.utils.currentScreenIs
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun BottomNavBar(
    appTheme: AppTheme?,
    isAppSetUp: Boolean,
    navBackStackEntry: NavBackStackEntry?,
    onNavigateBack: () -> Unit,
    onNavigateToScreen: (MainScreens) -> Unit,
    onMakeRecordButtonClick: () -> Unit
) {
    val bottomBarButtonList = listOf(
        BottomBarButtons.Home,
        BottomBarButtons.Records,
        null,
        BottomBarButtons.CategoryStatistics,
        BottomBarButtons.Settings,
    )
    var timerIsUp by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

    AnimatedVisibility(
        visible = isAppSetUp &&
                (navBackStackEntry.currentScreenIs(MainScreens.Home) ||
                        navBackStackEntry.currentScreenIs(MainScreens.Records) ||
                        navBackStackEntry.currentScreenIs(MainScreens.CategoryStatistics(0)) ||
                        navBackStackEntry.currentScreenIs(SettingsScreens.SettingsHome) ||
                        navBackStackEntry.currentScreenIs(SettingsScreens.Language)),
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
                bottomBarButtonList.forEach { button ->
                    if (button != null) {
                        BottomBarButton(
                            button = button,
                            navBackStackEntry = navBackStackEntry,
                            onNavigateToScreen = onNavigateToScreen,
                            onNavigateBack = onNavigateBack,
                            appTheme = appTheme,
                            coroutineScope = coroutineScope,
                            timerIsUp = timerIsUp,
                            setupTimerIsUp = { timerIsUp = it }
                        )
                    } else {
                        Spacer(modifier = Modifier.width(60.dp))
                    }
                }
            }
            Box(
                modifier = Modifier.absoluteOffset(y = -(20).dp)
            ) {
                MakeRecordButton(onMakeRecordButtonClick)
            }
        }
    }
}

@Composable
private fun BottomBarButton(
    button: BottomBarButtons,
    navBackStackEntry: NavBackStackEntry?,
    onNavigateToScreen: (MainScreens) -> Unit,
    onNavigateBack: () -> Unit,
    appTheme: AppTheme?,
    coroutineScope: CoroutineScope,
    timerIsUp: Boolean,
    setupTimerIsUp: (Boolean) -> Unit,
) {

    AnimatedContent(
        targetState = if (
            navBackStackEntry?.destination?.hierarchy?.any {
                it.currentScreenIs(button.screen)
            } == true
        ) button.activeIconRes else button.inactiveIconRes,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "bottom bar ${button.screen} icon"
    ) { buttonIconRes ->
        Image(
            painter = painterResource(buttonIconRes.getByTheme(appTheme)),
            contentDescription = "bottom bar ${button.screen} icon",
            modifier = Modifier
                .bounceClickEffect(.97f) {
                    if (navBackStackEntry.currentScreenIs(button.screen)) return@bounceClickEffect
                    if (!timerIsUp) return@bounceClickEffect

                    coroutineScope.launch {
                        setupTimerIsUp(false)
                        delay(500)
                        setupTimerIsUp(true)
                    }

                    if (
                        button.screen == MainScreens.Settings &&
                        navBackStackEntry.currentScreenIs(SettingsScreens.Language)
                    ) {
                        onNavigateBack()
                    } else {
                        onNavigateToScreen(button.screen)
                    }
                }
                .size(36.dp)
        )
    }
}

@Composable
private fun MakeRecordButton(onMakeRecordButtonClick: () -> Unit) {
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
                elevation = 12.dp,
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
            .padding(6.dp)
    ) {
        Icon(
            painter = painterResource(R.drawable.make_record_icon),
            contentDescription = "make record",
            tint = GlanceTheme.onPrimary,
            modifier = Modifier.size(24.dp)
        )
    }
}
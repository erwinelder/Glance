package com.ataglance.walletglance.core.presentation.components.containers

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
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.navigation.BottomBarNavigationButtons
import com.ataglance.walletglance.core.navigation.MainScreens
import com.ataglance.walletglance.core.presentation.GlanceTheme
import com.ataglance.walletglance.core.presentation.modifiers.bounceClickEffect
import com.ataglance.walletglance.settings.navigation.SettingsScreens
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun BottomNavBar(
    appTheme: AppTheme?,
    isVisible: Boolean,
    anyScreenInHierarchyIsScreenProvider: (Any) -> Boolean,
    currentScreenIsScreenProvider: (Any) -> Boolean,
    onNavigateBack: () -> Unit,
    onNavigateToScreen: (MainScreens) -> Unit,
    onMakeRecordButtonClick: () -> Unit
) {
    BottomBarContainer(isVisible = isVisible) {
        BottomBarMainButtonsRow(
            appTheme = appTheme,
            anyScreenInHierarchyIsScreenProvider = anyScreenInHierarchyIsScreenProvider,
            currentScreenIsScreenProvider = currentScreenIsScreenProvider,
            onNavigateBack = onNavigateBack,
            onNavigateToScreen = onNavigateToScreen
        )
        Box(modifier = Modifier.absoluteOffset(y = -(20).dp)) {
            MakeRecordButton(onMakeRecordButtonClick)
        }
    }
}

@Composable
private fun BottomBarContainer(
    isVisible: Boolean,
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
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
            content()
        }
    }
}

@Composable
private fun BottomBarMainButtonsRow(
    appTheme: AppTheme?,
    anyScreenInHierarchyIsScreenProvider: (Any) -> Boolean,
    currentScreenIsScreenProvider: (Any) -> Boolean,
    onNavigateBack: () -> Unit,
    onNavigateToScreen: (MainScreens) -> Unit,
) {
    val bottomBarButtonList = listOf(
        BottomBarNavigationButtons.Home,
        BottomBarNavigationButtons.Records,
        null,
        BottomBarNavigationButtons.CategoryStatistics,
        BottomBarNavigationButtons.Settings,
    )
    var timerIsUp by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

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
                width = 1.dp,
                color = GlanceTheme.onSurface.copy(alpha = .05f),
                shape = RoundedCornerShape(26.dp)
            )
            .padding(vertical = 16.dp, horizontal = 28.dp)
    ) {
        bottomBarButtonList.forEach { button ->
            if (button != null) {
                BottomBarButton(
                    appTheme = appTheme,
                    button = button,
                    anyScreenInHierarchyIsScreenProvider = anyScreenInHierarchyIsScreenProvider,
                    currentScreenIsScreenProvider = currentScreenIsScreenProvider,
                    onNavigateToScreen = onNavigateToScreen,
                    onNavigateBack = onNavigateBack,
                    coroutineScope = coroutineScope,
                    timerIsUp = timerIsUp,
                    setupTimerIsUp = { timerIsUp = it }
                )
            } else {
                Spacer(modifier = Modifier.width(60.dp))
            }
        }
    }
}

@Composable
private fun BottomBarButton(
    appTheme: AppTheme?,
    button: BottomBarNavigationButtons,
    anyScreenInHierarchyIsScreenProvider: (Any) -> Boolean,
    currentScreenIsScreenProvider: (Any) -> Boolean,
    onNavigateToScreen: (MainScreens) -> Unit,
    onNavigateBack: () -> Unit,
    coroutineScope: CoroutineScope,
    timerIsUp: Boolean,
    setupTimerIsUp: (Boolean) -> Unit,
) {
    AnimatedContent(
        targetState = if (anyScreenInHierarchyIsScreenProvider(button.screen))
            button.activeIconRes else button.inactiveIconRes,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "bottom bar ${button.screen} icon"
    ) { buttonIconRes ->
        Image(
            painter = painterResource(buttonIconRes.getByTheme(appTheme)),
            contentDescription = "bottom bar ${button.screen} icon",
            modifier = Modifier
                .bounceClickEffect(.97f) {
                    if (currentScreenIsScreenProvider(button.screen)) {
                        return@bounceClickEffect
                    }
                    if (!timerIsUp) {
                        return@bounceClickEffect
                    }

                    coroutineScope.launch {
                        setupTimerIsUp(false)
                        delay(500)
                        setupTimerIsUp(true)
                    }

                    if (
                        button.screen == MainScreens.Settings &&
                        currentScreenIsScreenProvider(SettingsScreens.Language)
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
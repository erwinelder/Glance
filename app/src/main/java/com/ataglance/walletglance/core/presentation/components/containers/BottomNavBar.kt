package com.ataglance.walletglance.core.presentation.components.containers

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.navigation.BottomBarNavigationButtons
import com.ataglance.walletglance.core.navigation.MainScreens
import com.ataglance.walletglance.core.presentation.GlanceTheme
import com.ataglance.walletglance.core.presentation.components.dividers.BigDivider
import com.ataglance.walletglance.core.presentation.modifiers.bounceClickEffect
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun BottomNavBar(
    appTheme: AppTheme?,
    isVisible: Boolean,
    anyScreenInHierarchyIsScreenProvider: (Any) -> Boolean,
    currentScreenIsScreenProvider: (Any) -> Boolean,
    onNavigateToScreen: (MainScreens) -> Unit,
    onMakeRecordButtonClick: () -> Unit
) {
    var timerIsUp by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()
    var isExpanded by remember { mutableStateOf(true) }

    val floatingButtonOffset by animateDpAsState(
        targetValue = if (isExpanded) (-4).dp else (-25).dp,
        label = "button bar floating button offset"
    )
    val onButtonClick = { screen: MainScreens ->
        if (!currentScreenIsScreenProvider(screen) && timerIsUp) {
            coroutineScope.launch {
                timerIsUp = false
                delay(500)
                timerIsUp = true
            }

            onNavigateToScreen(screen)
            if (isExpanded) {
                isExpanded = false
            }
        }
    }

    BottomBarContainer(isVisible = isVisible, isExpanded = isExpanded) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .shadow(
                    elevation = 16.dp,
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
                .padding(vertical = 16.dp, horizontal = 4.dp)
        ) {
            BottomBarOtherButtonsGrid(
                appTheme = appTheme,
                isExpanded = isExpanded,
                anyScreenInHierarchyIsScreenProvider = anyScreenInHierarchyIsScreenProvider,
                onButtonClick = onButtonClick
            )
            BottomBarMainButtonsRow(
                appTheme = appTheme,
                onIsExpandedToggle = { isExpanded = !isExpanded },
                anyScreenInHierarchyIsScreenProvider = anyScreenInHierarchyIsScreenProvider,
                onButtonClick = onButtonClick,
                mainButtons = listOf(
                    BottomBarNavigationButtons.Home,
                    BottomBarNavigationButtons.Records,
                    null,
                    BottomBarNavigationButtons.CategoryStatistics
                )
            )
        }
        Box(
            modifier = Modifier
                .absoluteOffset {
                    IntOffset(x = 0, y = floatingButtonOffset.roundToPx())
                }
        ) {
            MakeRecordButton(onMakeRecordButtonClick)
        }
    }
}

@Composable
private fun BottomBarContainer(
    isVisible: Boolean,
    isExpanded: Boolean,
    content: @Composable () -> Unit
) {
    val bottomPadding by animateDpAsState(
        targetValue = if (isExpanded) 0.dp else 12.dp,
        label = "bottom nav bar bottom padding"
    )

    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically { (it * 1.5).toInt() },
        exit = slideOutVertically { (it * 1.5).toInt() }
    ) {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier
                .bounceClickEffect(.985f)
                .fillMaxWidth()
                .padding(bottom = bottomPadding)
        ) {
            content()
        }
    }
}

@Composable
private fun BottomBarMainButtonsRow(
    appTheme: AppTheme?,
    onIsExpandedToggle: () -> Unit,
    anyScreenInHierarchyIsScreenProvider: (Any) -> Boolean,
    onButtonClick: (MainScreens) -> Unit,
    mainButtons: List<BottomBarNavigationButtons?>
) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .shadow(
                elevation = 16.dp,
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
            .padding(vertical = 16.dp, horizontal = 4.dp)
    ) {
        mainButtons.forEachIndexed { index, button ->
            ButtonsSpacerGap()
            if (button != null) {
                BottomBarButton(
                    appTheme = appTheme,
                    button = button,
                    anyScreenInHierarchyIsScreenProvider = anyScreenInHierarchyIsScreenProvider,
                    onClick = onButtonClick
                )
            } else {
                Spacer(modifier = Modifier.width(60.dp))
            }
            if (index == mainButtons.lastIndex) {
                ButtonsSpacerGap()
            }
        }
        Icon(
            painter = painterResource(R.drawable.expand_icon),
            contentDescription = "expand top bar icon",
            tint = GlanceTheme.onSurface,
            modifier = Modifier
                .bounceClickEffect(.97f, onClick = onIsExpandedToggle)
                .size(36.dp)
        )
        ButtonsSpacerGap()
    }
}

@Composable
private fun BottomBarOtherButtonsGrid(
    appTheme: AppTheme?,
    isExpanded: Boolean,
    anyScreenInHierarchyIsScreenProvider: (Any) -> Boolean,
    onButtonClick: (MainScreens) -> Unit,
) {
    val otherButtons = listOf(
        BottomBarNavigationButtons.Budgets,
        BottomBarNavigationButtons.Settings,
        BottomBarNavigationButtons.CategoryStatistics,
        BottomBarNavigationButtons.CategoryStatistics,
        BottomBarNavigationButtons.CategoryStatistics,
        BottomBarNavigationButtons.CategoryStatistics,
        BottomBarNavigationButtons.CategoryStatistics,
    )

    AnimatedVisibility(visible = isExpanded) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(5),
                contentPadding = PaddingValues(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(items = otherButtons) { button ->
                    BottomBarButton(
                        appTheme = appTheme,
                        button = button,
                        anyScreenInHierarchyIsScreenProvider = anyScreenInHierarchyIsScreenProvider,
                        onClick = onButtonClick
                    )
                }
            }
            BigDivider(
                modifier = Modifier.padding(vertical = 16.dp),
                color = GlanceTheme.outline.copy(.5f)
            )
        }
    }
}

@Composable
private fun RowScope.ButtonsSpacerGap() {
    Spacer(
        modifier = Modifier
            .width(24.dp)
            .weight(1f, fill = false)
    )
}

@Composable
private fun BottomBarButton(
    appTheme: AppTheme?,
    button: BottomBarNavigationButtons,
    anyScreenInHierarchyIsScreenProvider: (Any) -> Boolean,
    onClick: (MainScreens) -> Unit
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
                    onClick(button.screen)
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



@Preview
@Composable
private fun BottomNavBarPreview() {
    val appTheme = AppTheme.LightDefault

    PreviewContainer(appTheme = appTheme) {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier.fillMaxSize()
        ) {
            BottomNavBar(
                appTheme = appTheme,
                isVisible = true,
                anyScreenInHierarchyIsScreenProvider = { false },
                currentScreenIsScreenProvider = { false },
                onNavigateToScreen = {},
                onMakeRecordButtonClick = {}
            )
        }
    }
}

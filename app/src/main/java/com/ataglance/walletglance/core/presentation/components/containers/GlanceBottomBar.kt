package com.ataglance.walletglance.core.presentation.components.containers

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.absoluteOffset
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.app.DrawableResByTheme
import com.ataglance.walletglance.core.presentation.GlanceTheme
import com.ataglance.walletglance.core.presentation.animation.scaleSlideVerFadeInAnimation
import com.ataglance.walletglance.core.presentation.animation.scaleSlideVerFadeOutAnimation
import com.ataglance.walletglance.core.presentation.modifiers.bounceClickEffect
import com.ataglance.walletglance.core.presentation.modifiers.innerVolumeShadow
import com.ataglance.walletglance.navigation.domain.model.BottomBarNavigationButtons
import com.ataglance.walletglance.navigation.domain.model.MainScreens
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun GlanceBottomNavBar(
    appTheme: AppTheme?,
    isVisible: Boolean,
    anyScreenInHierarchyIsScreenProvider: (Any) -> Boolean,
    currentScreenIsScreenProvider: (Any) -> Boolean,
    onNavigateToScreen: (MainScreens) -> Unit,
    onFloatingButtonClick: () -> Unit,
    bottomBarButtons: List<BottomBarNavigationButtons>,
    popupListBarButtons: List<BottomBarNavigationButtons>
) {
    var isExpanded by remember { mutableStateOf(false) }
    var timerIsUp by remember { mutableStateOf(true) }
    val coroutineScope = rememberCoroutineScope()

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

    BottomBarContainer(
        isVisible = isVisible,
        isExpanded = isExpanded,
        floatingButton = {
            GlanceFloatingButton(
                iconRes = R.drawable.make_record_icon, onClick = onFloatingButtonClick
            )
        }
    ) {
        Column(horizontalAlignment = Alignment.End) {
            PopupButtonsList(
                appTheme = appTheme,
                isExpanded = isExpanded,
                onIsExpandedToggle = { isExpanded = !isExpanded },
                anyScreenInHierarchyIsScreenProvider = anyScreenInHierarchyIsScreenProvider,
                onButtonClick = onButtonClick,
                barButtons = popupListBarButtons
            )
            BottomBarButtonsRow(
                appTheme = appTheme,
                onIsExpandedToggle = { isExpanded = !isExpanded },
                anyScreenInHierarchyIsScreenProvider = anyScreenInHierarchyIsScreenProvider,
                onButtonClick = onButtonClick,
                barButtons = bottomBarButtons
            )
        }
    }
}

@Composable
private fun BottomBarContainer(
    isVisible: Boolean,
    isExpanded: Boolean,
    floatingButton: @Composable (() -> Unit)?,
    content: @Composable () -> Unit
) {
    val floatingButtonOffset by animateDpAsState(
        targetValue = if (isExpanded) (-4).dp else (-25).dp,
        animationSpec = spring(stiffness = 700f),
        label = "button bar floating button offset"
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
                .padding(bottom = 12.dp)
        ) {
            content()
            floatingButton?.let {
                Box(
                    modifier = Modifier.absoluteOffset {
                        IntOffset(x = 0, y = floatingButtonOffset.roundToPx())
                    }
                ) { it () }
            }
        }
    }
}

@Composable
private fun ExpandBottomBarButton(appTheme: AppTheme?, onClick: () -> Unit) {
    val iconRes = DrawableResByTheme(
        lightDefault = R.drawable.show_other_light,
        darkDefault = R.drawable.show_other_dark
    ).getByTheme(appTheme)

    Image(
        painter = painterResource(iconRes),
        contentDescription = "expand top bar icon",
        modifier = Modifier
            .bounceClickEffect(onClick = onClick)
            .size(36.dp)
    )
}

@Composable
private fun BottomBarButtonsRow(
    appTheme: AppTheme?,
    onIsExpandedToggle: () -> Unit,
    anyScreenInHierarchyIsScreenProvider: (Any) -> Boolean,
    onButtonClick: (MainScreens) -> Unit,
    barButtons: List<BottomBarNavigationButtons>
) {
    val buttonList by remember(barButtons) {
        derivedStateOf {
            val list: MutableList<BottomBarNavigationButtons?> = barButtons.toMutableList()
            list.add(2, null)
            list
        }
    }
    val buttonListSize by remember(barButtons) {
        derivedStateOf { buttonList.size }
    }

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
        buttonList.forEachIndexed { index, button ->
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
            if (index == buttonList.lastIndex) {
                ButtonsSpacerGap()
            }
        }
        if (buttonListSize == 4) {
            ExpandBottomBarButton(appTheme = appTheme, onIsExpandedToggle)
            ButtonsSpacerGap()
        }
    }
}

@Composable
private fun PopupButtonsList(
    appTheme: AppTheme?,
    isExpanded: Boolean,
    onIsExpandedToggle: () -> Unit,
    anyScreenInHierarchyIsScreenProvider: (Any) -> Boolean,
    onButtonClick: (MainScreens) -> Unit,
    barButtons: List<BottomBarNavigationButtons>
) {
    val expandedState = remember { MutableTransitionState(isExpanded) }
    LaunchedEffect(isExpanded) {
        expandedState.targetState = isExpanded
    }

    AnimatedVisibility(visible = expandedState.targetState) {
        Popup(
            alignment = Alignment.BottomEnd,
            onDismissRequest = onIsExpandedToggle,
            properties = PopupProperties(
                focusable = true
            )
        ) {
            AnimatedVisibility(
                visibleState = expandedState,
                enter = scaleSlideVerFadeInAnimation(
                    scaleTransformOrigin = TransformOrigin(1.0f, 0.8f)
                ),
                exit = scaleSlideVerFadeOutAnimation(
                    scaleTransformOrigin = TransformOrigin(1.0f, 0.8f)
                )
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                        .innerVolumeShadow(shape = RoundedCornerShape(26.dp))
                        .clip(RoundedCornerShape(26.dp))
                        .background(GlanceTheme.surface)
                        .padding(horizontal = 24.dp, vertical = 16.dp)
                ) {
                    barButtons.forEach { button ->
                        ListBarButton(
                            appTheme = appTheme,
                            button = button,
                            anyScreenInHierarchyIsScreenProvider =
                            anyScreenInHierarchyIsScreenProvider,
                            onClick = onButtonClick
                        )
                    }
                }
            }
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
                .bounceClickEffect {
                    onClick(button.screen)
                }
                .size(36.dp)
        )
    }
}

@Composable
private fun ListBarButton(
    appTheme: AppTheme?,
    button: BottomBarNavigationButtons,
    anyScreenInHierarchyIsScreenProvider: (Any) -> Boolean,
    onClick: (MainScreens) -> Unit
) {
    val isActive by remember {
        derivedStateOf { anyScreenInHierarchyIsScreenProvider(button.screen) }
    }
    val textColor by animateColorAsState(
        targetValue = if (isActive) GlanceTheme.primary else GlanceTheme.onSurface,
        label = "list bar button text color"
    )

    AnimatedContent(
        targetState = if (isActive) button.activeIconRes else button.inactiveIconRes,
        transitionSpec = { fadeIn() togetherWith fadeOut() },
        label = "bottom bar ${button.screen} icon"
    ) { buttonIconRes ->
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .bounceClickEffect {
                    onClick(button.screen)
                }
        ) {
            Image(
                painter = painterResource(buttonIconRes.getByTheme(appTheme)),
                contentDescription = "bottom bar ${button.screen} icon",
                modifier = Modifier.size(36.dp)
            )
            Text(
                text = stringResource(button.screenNameRes),
                fontSize = 18.sp,
                color = textColor
            )
        }
    }
}
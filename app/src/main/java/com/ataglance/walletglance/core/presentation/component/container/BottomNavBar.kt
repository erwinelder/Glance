package com.ataglance.walletglance.core.presentation.component.container

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.MutableTransitionState
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
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices.PIXEL_7_PRO
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.animation.scaleSlideVerFadeInAnimation
import com.ataglance.walletglance.core.presentation.animation.scaleSlideVerFadeOutAnimation
import com.ataglance.walletglance.core.presentation.preview.PreviewContainer
import com.ataglance.walletglance.core.presentation.modifier.bounceClickEffect
import com.ataglance.walletglance.core.presentation.modifier.innerVolumeShadow
import com.ataglance.walletglance.core.presentation.theme.CurrAppTheme
import com.ataglance.walletglance.core.presentation.theme.GlanciColors
import com.ataglance.walletglance.core.presentation.theme.Manrope
import com.ataglance.walletglance.di.initializeKoinMockedModule
import com.ataglance.walletglance.navigation.domain.usecase.GetNavigationButtonScreensUseCase
import com.ataglance.walletglance.navigation.domain.usecase.GetNavigationButtonScreensUseCaseMock
import com.ataglance.walletglance.navigation.presentation.model.BottomNavBarButtonState
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.module.dsl.viewModel

@Composable
fun BottomNavBarWrapper(
    navViewModel: NavigationViewModel,
    navController: NavController,
    navBackStackEntry: NavBackStackEntry?
) {
    val isBottomBarVisible by navViewModel.isBottomBarVisible.collectAsStateWithLifecycle()
    val isBottomBarExpanded by navViewModel.isBottomBarExpanded.collectAsStateWithLifecycle()
    val primaryNavButtons by navViewModel.primaryNavigationButtons.collectAsStateWithLifecycle()
    val secondaryNavButtons by navViewModel.secondaryNavigationButtons.collectAsStateWithLifecycle()

    val bottomSystemBarPadding = WindowInsets.systemBars.asPaddingValues().calculateBottomPadding()

    BottomNavBar(
        isBottomBarVisible = isBottomBarVisible,
        isBottomBarExpanded = isBottomBarExpanded,
        onIsExpandedToggle = navViewModel::toggleBottomBarExpanded,
        primaryNavButtons = primaryNavButtons,
        secondaryNavButtons = secondaryNavButtons,
        onButtonClick = { button ->
            navViewModel.onNavButtonClick(
                button = button,
                navController = navController,
                navBackStackEntry = navBackStackEntry
            )
        },
        bottomPadding = bottomSystemBarPadding
    )
}

@Composable
fun BottomNavBar(
    isBottomBarVisible: Boolean,
    isBottomBarExpanded: Boolean,
    onIsExpandedToggle: () -> Unit = {},
    primaryNavButtons: List<BottomNavBarButtonState>,
    secondaryNavButtons: List<BottomNavBarButtonState>,
    onButtonClick: (BottomNavBarButtonState) -> Unit,
    bottomPadding: Dp = 0.dp
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, end = 16.dp, bottom = 4.dp + bottomPadding),
        contentAlignment = Alignment.BottomEnd
    ) {
        AnimatedVisibility(
            visible = isBottomBarVisible,
            enter = slideInVertically { (it * 1.5).toInt() } + fadeIn(),
            exit = slideOutVertically { (it * 1.5).toInt() } + fadeOut()
        ) {
            Column(
                horizontalAlignment = Alignment.End
            ) {
                PopupButtonsList(
                    isExpanded = isBottomBarExpanded,
                    onIsExpandedToggle = onIsExpandedToggle,
                    buttons = secondaryNavButtons,
                    onButtonClick = onButtonClick
                )
                BottomBarButtonsRow(
                    buttons = primaryNavButtons,
                    onButtonClick = onButtonClick
                )
            }
        }
    }
}


@Composable
private fun BottomBarButtonsRow(
    buttons: List<BottomNavBarButtonState>,
    onButtonClick: (BottomNavBarButtonState) -> Unit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(22.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .bounceClickEffect(.985f)
                .shadow(
                    elevation = 16.dp,
                    shape = RoundedCornerShape(26.dp),
                    ambientColor = GlanciColors.onSurface
                )
                .clip(RoundedCornerShape(26.dp))
                .background(GlanciColors.surface)
                .border(
                    width = 1.dp,
                    color = GlanciColors.onSurface.copy(.05f),
                    shape = RoundedCornerShape(26.dp)
                )
                .padding(horizontal = 28.dp, vertical = 16.dp)
        ) {
            buttons.forEach { button ->
                BottomBarButton(button = button, onClick = onButtonClick)
            }
        }
    }
}

/*
@Composable
private fun ExpandBottomBarButton(isActive: Boolean, onClick: () -> Unit) {
    val iconRes = when (CurrAppTheme) {
        AppTheme.LightDefault -> if (isActive) R.drawable.show_other_light_active else
            R.drawable.show_other_light_inactive
        AppTheme.DarkDefault -> if (isActive) R.drawable.show_other_dark_active else
            R.drawable.show_other_dark_inactive
    }

    AnimatedContent(
        targetState = iconRes,
        transitionSpec = { fadeIn() togetherWith fadeOut() }
    ) { targetIconRes ->
        Image(
            painter = painterResource(targetIconRes),
            contentDescription = "expand top bar icon",
            modifier = Modifier
                .bounceClickEffect(onClick = onClick)
                .size(36.dp)
        )
    }
}
*/

@Composable
private fun BottomBarButton(
    button: BottomNavBarButtonState,
    onClick: (BottomNavBarButtonState) -> Unit
) {
    AnimatedContent(
        targetState = button.iconRes,
        transitionSpec = { fadeIn() togetherWith fadeOut() }
    ) { buttonIconRes ->
        Image(
            painter = painterResource(buttonIconRes.getByTheme(CurrAppTheme)),
            contentDescription = "bottom bar ${button.screen} icon",
            modifier = Modifier
                .bounceClickEffect {
                    onClick(button)
                }
                .size(34.dp)
        )
    }
}


@Composable
private fun PopupButtonsList(
    isExpanded: Boolean,
    onIsExpandedToggle: () -> Unit,
    buttons: List<BottomNavBarButtonState>,
    onButtonClick: (BottomNavBarButtonState) -> Unit,
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
                        .innerVolumeShadow(shape = RoundedCornerShape(24.dp))
                        .clip(RoundedCornerShape(24.dp))
                        .background(GlanciColors.surface)
                        .padding(horizontal = 20.dp, vertical = 16.dp)
                ) {
                    buttons.forEach { button ->
                        ListBarButton(
                            button = button,
                            onClick = onButtonClick
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ListBarButton(
    button: BottomNavBarButtonState,
    onClick: (BottomNavBarButtonState) -> Unit
) {
    val textColor by animateColorAsState(
        targetValue = if (button.isActive) GlanciColors.primary else GlanciColors.onSurface
    )

    AnimatedContent(
        targetState = button.iconRes,
        transitionSpec = { fadeIn() togetherWith fadeOut() }
    ) { buttonIconRes ->
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .bounceClickEffect {
                    onClick(button)
                }
        ) {
            Image(
                painter = painterResource(buttonIconRes.getByTheme(CurrAppTheme)),
                contentDescription = "bottom bar ${button.screen} icon",
                modifier = Modifier.size(36.dp)
            )
            Text(
                text = stringResource(button.screenNameRes),
                fontSize = 18.sp,
                color = textColor,
                fontFamily = Manrope
            )
        }
    }
}


@Preview(device = PIXEL_7_PRO)
@Composable
private fun BottomNavBarPreview() {
    initializeKoinMockedModule {
        single<GetNavigationButtonScreensUseCase> {
            GetNavigationButtonScreensUseCaseMock()
        }
        viewModel {
            NavigationViewModel(getNavigationButtonScreensUseCase = get())
        }
    }

    val navViewModel = koinViewModel<NavigationViewModel>()
    navViewModel.setBottomBarVisibility(isVisible = true)
    val navController = rememberNavController()

    PreviewContainer(appTheme = AppTheme.LightDefault) {
        Box(
            contentAlignment = Alignment.BottomCenter,
            modifier = Modifier.fillMaxSize()
        ) {
            BottomNavBarWrapper(
                navViewModel = navViewModel,
                navController = navController,
                navBackStackEntry = null
            )
        }
    }
}
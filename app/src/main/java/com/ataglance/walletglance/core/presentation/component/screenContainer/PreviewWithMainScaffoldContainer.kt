package com.ataglance.walletglance.core.presentation.component.screenContainer

import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.rememberNavController
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.theme.GlanciTheme
import com.ataglance.walletglance.di.initializeKoinMockedModule
import com.ataglance.walletglance.navigation.domain.usecase.GetNavigationButtonScreensUseCase
import com.ataglance.walletglance.navigation.domain.usecase.GetNavigationButtonScreensUseCaseMock
import com.ataglance.walletglance.navigation.presentation.viewmodel.NavigationViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.KoinAppDeclaration

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun PreviewWithMainScaffoldContainer(
    appTheme: AppTheme = AppTheme.LightDefault,
    isBottomBarVisible: Boolean = false,
    koinConfig: KoinAppDeclaration? = null,
    koinModuleDeclaration: Module.() -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    runCatching {
        initializeKoinMockedModule(config = koinConfig) {
            single<GetNavigationButtonScreensUseCase> {
                GetNavigationButtonScreensUseCaseMock()
            }
            viewModel {
                NavigationViewModel(getNavigationButtonScreensUseCase = get())
            }
            koinModuleDeclaration()
        }
    }

    val navViewModel = koinViewModel<NavigationViewModel>()
    navViewModel.setBottomBarVisibility(isVisible = isBottomBarVisible)
    val navController = rememberNavController()

    BoxWithConstraints {
        SharedTransitionLayout {
            GlanciTheme(
                useDeviceTheme = false,
                lastChosenTheme = appTheme,
                boxWithConstraintsScope = this@BoxWithConstraints,
                sharedTransitionScope = this@SharedTransitionLayout
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Image(
                        painter = painterResource(
                            when (appTheme) {
                                AppTheme.LightDefault -> R.drawable.main_background_light
                                AppTheme.DarkDefault -> R.drawable.main_background_dark
                            }
                        ),
                        contentDescription = null,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier.fillMaxSize()
                    )
                    MainScaffold(
                        navViewModel = navViewModel,
                        navController = navController,
                        navBackStackEntry = null,
                        content = content
                    )
                }
            }
        }
    }
}
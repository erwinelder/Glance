package com.ataglance.walletglance.ui.theme.animation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.navigation.NavBackStackEntry

class CustomAnimation {

    fun screenEnterTransition(scope: AnimatedContentTransitionScope<NavBackStackEntry>): EnterTransition {
        return scope.slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Left,
            animationSpec = tween(400)
        )
    }

    fun screenPopEnterTransition(scope: AnimatedContentTransitionScope<NavBackStackEntry>): EnterTransition {
        return scope.slideIntoContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
            animationSpec = tween(400)
        )
    }

    fun screenExitTransition(scope: AnimatedContentTransitionScope<NavBackStackEntry>): ExitTransition {
        return scope.slideOutOfContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Companion.Left,
            animationSpec = tween(400)
        )
    }

    fun screenPopExitTransition(scope: AnimatedContentTransitionScope<NavBackStackEntry>): ExitTransition {
        return scope.slideOutOfContainer(
            towards = AnimatedContentTransitionScope.SlideDirection.Companion.Right,
            animationSpec = tween(400)
        )
    }
}

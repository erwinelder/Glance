package com.ataglance.walletglance.ui.theme.animation

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInVertically
import androidx.navigation.NavBackStackEntry

class CustomAnimation {

    fun screenEnterTransition(
        scope: AnimatedContentTransitionScope<NavBackStackEntry>,
        moveScreenTowardsLeft: Boolean = true
    ): EnterTransition {
        return scope.slideIntoContainer(
            towards = if (moveScreenTowardsLeft) {
                AnimatedContentTransitionScope.SlideDirection.Left
            } else {
                AnimatedContentTransitionScope.SlideDirection.Right
            },
            animationSpec = tween(400)
        )
    }

    fun screenPopEnterTransition(
        scope: AnimatedContentTransitionScope<NavBackStackEntry>,
        moveScreenTowardsLeft: Boolean = false
    ): EnterTransition {
        return scope.slideIntoContainer(
            towards = if (moveScreenTowardsLeft) {
                AnimatedContentTransitionScope.SlideDirection.Left
            } else {
                AnimatedContentTransitionScope.SlideDirection.Right
            },
            animationSpec = tween(400)
        )
    }

    fun screenExitTransition(
        scope: AnimatedContentTransitionScope<NavBackStackEntry>,
        moveScreenTowardsLeft: Boolean = true
    ): ExitTransition {
        return scope.slideOutOfContainer(
            towards = if (moveScreenTowardsLeft) {
                AnimatedContentTransitionScope.SlideDirection.Left
            } else {
                AnimatedContentTransitionScope.SlideDirection.Right
            },
            animationSpec = tween(400)
        )
    }

    fun screenPopExitTransition(
        scope: AnimatedContentTransitionScope<NavBackStackEntry>,
        moveScreenTowardsLeft: Boolean = false
    ): ExitTransition {
        return scope.slideOutOfContainer(
            towards = if (moveScreenTowardsLeft) {
                AnimatedContentTransitionScope.SlideDirection.Left
            } else {
                AnimatedContentTransitionScope.SlideDirection.Right
            },
            animationSpec = tween(400)
        )
    }



    fun widgetEnterTransition(delayMillis: Int = 0): EnterTransition {
        return fadeIn(tween(300, delayMillis)) +
                slideInVertically(tween(300, delayMillis)) { -(it*0.2).toInt() } +
                scaleIn(animationSpec = tween(300, delayMillis), initialScale = .6f)
    }

}

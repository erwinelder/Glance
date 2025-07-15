package com.ataglance.walletglance.core.presentation.utils

import androidx.annotation.StringRes
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.ime
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import com.ataglance.walletglance.auth.domain.model.user.UserContext
import com.ataglance.walletglance.core.utils.getCurrentLocalDateTime
import org.koin.compose.koinInject


@Composable
fun isKeyboardVisible(): State<Boolean> {
    val isKeyboardVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0
    return rememberUpdatedState(isKeyboardVisible)
}

@Composable
fun getImeBottomInset(): State<Dp> {
    val imeBottomInset = WindowInsets.ime.getBottom(LocalDensity.current)
    val imeBottomInsetDp = with(LocalDensity.current) { imeBottomInset.toDp() }
    return rememberUpdatedState(imeBottomInsetDp)
}

@Composable
fun getKeyboardBottomPaddingAnimated(minPadding: Dp): State<Dp> {
    val imeBottomInset = WindowInsets.ime.getBottom(LocalDensity.current)
    val imeBottomInsetDp = with(LocalDensity.current) { imeBottomInset.toDp() }
    return animateDpAsState(imeBottomInsetDp.coerceAtLeast(minPadding))
}


operator fun PaddingValues.plus(paddingValues: PaddingValues): PaddingValues {
    return PaddingValues(
        start = this.start + paddingValues.start,
        top = this.top + paddingValues.top,
        end = this.end + paddingValues.end,
        bottom = this.bottom + paddingValues.bottom
    )
}

fun PaddingValues.plusBottomPadding(padding: Dp): PaddingValues {
    return PaddingValues(
        start = this.start,
        top = this.top,
        end = this.end,
        bottom = this.bottom + padding
    )
}

fun PaddingValues.copy(
    start: Dp = this.start,
    top: Dp = this.top,
    end: Dp = this.end,
    bottom: Dp = this.bottom
): PaddingValues {
    return PaddingValues(
        start = start,
        top = top,
        end = end,
        bottom = bottom
    )
}

val PaddingValues.start: Dp
    get() = calculateStartPadding(LayoutDirection.Ltr)

val PaddingValues.end: Dp
    get() = calculateEndPadding(LayoutDirection.Ltr)

val PaddingValues.top: Dp
    get() = calculateTopPadding()

val PaddingValues.bottom: Dp
    get() = calculateBottomPadding()


@Composable
@StringRes fun getGreetingsWidgetTitleRes(): State<Int> {
    val userContext = koinInject<UserContext>()
    val username by remember(userContext.name) {
        mutableStateOf(userContext.name)
    }
    val currentLocalDateTime = getCurrentLocalDateTime()

    return remember(currentLocalDateTime.hour, username) {
        derivedStateOf {
            if (username?.isNotBlank() == true) {
                currentLocalDateTime.hour.getGreetingsWithUsernameWidgetTitleRes()
            } else {
                currentLocalDateTime.hour.getGreetingsWidgetTitleRes()
            }
        }
    }
}

@Composable
fun getGreetingsWidgetTitleResWithUsername(): State<Pair<Int, String?>> {
    val userContext = koinInject<UserContext>()
    val username by remember(userContext.name) {
        mutableStateOf(userContext.name)
    }
    val currentLocalDateTime = getCurrentLocalDateTime()

    return remember(currentLocalDateTime.hour, username) {
        derivedStateOf {
            val stringRes = if (username?.isNotBlank() == true) {
                currentLocalDateTime.hour.getGreetingsWithUsernameWidgetTitleRes()
            } else {
                currentLocalDateTime.hour.getGreetingsWidgetTitleRes()
            }
            stringRes to username
        }
    }
}

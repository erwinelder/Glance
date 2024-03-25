package com.ataglance.walletglance.ui.theme.uielements

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavBackStackEntry
import com.ataglance.walletglance.ui.theme.GlanceTheme
import com.ataglance.walletglance.ui.theme.uielements.buttons.BackButton

@Composable
fun SetupProgressTopBar(
    visible: Boolean,
    navBackStackEntry: NavBackStackEntry?,
    onBackNavigationButton: () -> Unit
) {
    AnimatedVisibility(
        visible = visible && navBackStackEntry != null,
        enter = slideInVertically { -it },
        exit = slideOutVertically { -it }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(GlanceTheme.surfaceVariant)
                .padding(2.dp)
        ) {
            BackButton(onClick = onBackNavigationButton)
        }
    }
}
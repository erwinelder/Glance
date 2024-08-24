package com.ataglance.walletglance.settings.presentation.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import com.ataglance.walletglance.core.presentation.GlanceTheme
import com.ataglance.walletglance.core.presentation.components.buttons.BackButton
import com.ataglance.walletglance.core.utils.getSetupProgressTopBarTitleRes

@Composable
fun SetupProgressTopBar(
    visible: Boolean,
    navBackStackEntry: NavBackStackEntry?,
    onBackNavigationButton: () -> Unit
) {
    val titleRes by remember {
        derivedStateOf { navBackStackEntry.getSetupProgressTopBarTitleRes() }
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically { -it },
        exit = slideOutVertically { -it }
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = androidx.compose.ui.Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(GlanceTheme.surfaceVariant)
                .padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            BackButton(onClick = onBackNavigationButton)
            AnimatedContent(
                targetState = titleRes,
                label = "setup progress bar title"
            ) { targetTitleRes ->
                Text(
                    text = stringResource(targetTitleRes),
                    color = GlanceTheme.onSurface,
                    fontSize = 22.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }
    }
}

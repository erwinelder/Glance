package com.ataglance.walletglance.presentation.theme.uielements

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
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavBackStackEntry
import com.ataglance.walletglance.R
import com.ataglance.walletglance.data.utils.currentScreenIs
import com.ataglance.walletglance.presentation.theme.GlanceTheme
import com.ataglance.walletglance.presentation.theme.navigation.screens.AccountsSettingsScreens
import com.ataglance.walletglance.presentation.theme.navigation.screens.CategoriesSettingsScreens
import com.ataglance.walletglance.presentation.theme.navigation.screens.MainScreens
import com.ataglance.walletglance.presentation.theme.navigation.screens.SettingsScreens
import com.ataglance.walletglance.presentation.theme.uielements.buttons.BackButton

@Composable
fun SetupProgressTopBar(
    visible: Boolean,
    navBackStackEntry: NavBackStackEntry?,
    onBackNavigationButton: () -> Unit
) {
    val titleRes = when {
        navBackStackEntry.currentScreenIs(SettingsScreens.Language) -> R.string.language
        navBackStackEntry.currentScreenIs(SettingsScreens.Appearance) -> R.string.appearance
        navBackStackEntry.currentScreenIs(AccountsSettingsScreens.EditAccounts) ||
                navBackStackEntry.currentScreenIs(AccountsSettingsScreens.EditAccount) ||
                navBackStackEntry.currentScreenIs(AccountsSettingsScreens.EditAccountCurrency) -> R.string.accounts
        navBackStackEntry.currentScreenIs(CategoriesSettingsScreens.EditCategories) ||
                navBackStackEntry.currentScreenIs(CategoriesSettingsScreens.EditSubcategories) ||
                navBackStackEntry.currentScreenIs(CategoriesSettingsScreens.EditCategory) -> R.string.categories
        else -> R.string.settings
    }

    AnimatedVisibility(
        visible = visible && navBackStackEntry != null,
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

fun shouldDisplaySetupProgressTopBar(
    mainStartDestination: MainScreens,
    navBackStackEntry: NavBackStackEntry?
): Boolean {
    return mainStartDestination != MainScreens.Home &&
            !navBackStackEntry.currentScreenIs(SettingsScreens.Start) &&
            !navBackStackEntry.currentScreenIs(MainScreens.FinishSetup)
}
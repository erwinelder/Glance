package com.ataglance.walletglance.core.presentation.components.other

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ataglance.walletglance.core.presentation.CurrAppTheme
import com.ataglance.walletglance.core.presentation.GlanceTheme
import com.ataglance.walletglance.navigation.domain.model.BottomBarNavigationButton

@Composable
fun ScreenNameWithIconComponent(
    navigationButton: BottomBarNavigationButton,
    modifier: Modifier = Modifier,
    showAsActive: Boolean = false
) {
    val screenName = stringResource(navigationButton.screenNameRes)

    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
    ) {
        Image(
            painter = painterResource(
                if (showAsActive) navigationButton.activeIconRes.getByTheme(CurrAppTheme)
                else navigationButton.inactiveIconRes.getByTheme(CurrAppTheme)
            ),
            contentDescription = "$screenName screen icon",
            modifier = Modifier.size(36.dp)
        )
        Text(
            text = screenName,
            fontSize = 18.sp,
            color = if (showAsActive) GlanceTheme.primary else GlanceTheme.onSurface
        )
    }
}
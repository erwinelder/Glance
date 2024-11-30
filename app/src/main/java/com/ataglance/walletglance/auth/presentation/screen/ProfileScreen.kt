package com.ataglance.walletglance.auth.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.domain.app.FilledWidthByScreenType
import com.ataglance.walletglance.core.presentation.LocalWindowType
import com.ataglance.walletglance.core.presentation.Typography
import com.ataglance.walletglance.core.presentation.components.buttons.GlassSurfaceNavigationButton
import com.ataglance.walletglance.core.presentation.components.buttons.PrimaryButton
import com.ataglance.walletglance.core.presentation.components.containers.GlanceBottomSheetDialog
import com.ataglance.walletglance.core.presentation.components.screenContainers.PreviewContainer
import com.ataglance.walletglance.core.presentation.components.screenContainers.ScreenContainer
import com.ataglance.walletglance.core.utils.getGreetingsWidgetTitleRes
import java.time.LocalDateTime

@Composable
fun ProfileScreen(
    onNavigateBack: () -> Unit,
    onSignOut: () -> Unit,
    onNavigateToUpdateEmailScreen: () -> Unit,
    onNavigateToUpdatePasswordScreen: () -> Unit
) {
    var showSignOutSheet by remember { mutableStateOf(false) }

    val currentLocalDateTime = LocalDateTime.now()
    val greetingsTitleRes by remember(currentLocalDateTime.hour) {
        derivedStateOf {
            currentLocalDateTime.hour.getGreetingsWidgetTitleRes()
        }
    }

    Box {
        ScreenContainer(
            onBackButtonClick = onNavigateBack
        ) {
            Text(
                text = stringResource(greetingsTitleRes),
                style = Typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth(FilledWidthByScreenType().getByType(LocalWindowType.current))
            )
            Spacer(modifier = Modifier.height(32.dp))
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth(
                    FilledWidthByScreenType().getByType(LocalWindowType.current)
                )
            ) {
                GlassSurfaceNavigationButton(
                    text = stringResource(R.string.sign_out),
                    imageRes = R.drawable.accounts_light_default_icon,
                    rightIconRes = R.drawable.short_arrow_up_icon
                ) {
                    showSignOutSheet = true
                }
                GlassSurfaceNavigationButton(
                    text = stringResource(R.string.update_email),
                    imageRes = R.drawable.accounts_light_default_icon,
                    onClick = onNavigateToUpdateEmailScreen
                )
                GlassSurfaceNavigationButton(
                    text = stringResource(R.string.update_password),
                    imageRes = R.drawable.accounts_light_default_icon,
                    onClick = onNavigateToUpdatePasswordScreen
                )
            }
        }
        GlanceBottomSheetDialog(
            visible = showSignOutSheet,
            iconRes = R.drawable.success_icon,
            iconDescription = "Sing out",
            title = stringResource(R.string.sign_out),
            message = stringResource(R.string.sign_out_of_your_account),
            onDismissRequest = { showSignOutSheet = false }
        ) { onSheetHide ->
            PrimaryButton(
                text = stringResource(R.string.sign_out),
                onClick = {
                    onSheetHide()
                    showSignOutSheet = false
                    onSignOut()
                }
            )
        }
    }
}



@Preview(device = Devices.PIXEL_7_PRO)
@Composable
fun ProfileScreenPreview() {
    PreviewContainer(appTheme = AppTheme.LightDefault) {
        ProfileScreen(
            onNavigateBack = {},
            onSignOut = {},
            onNavigateToUpdateEmailScreen = {},
            onNavigateToUpdatePasswordScreen = {}
        )
    }
}
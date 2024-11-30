package com.ataglance.walletglance.core.presentation.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ataglance.walletglance.R
import com.ataglance.walletglance.core.domain.app.AppTheme
import com.ataglance.walletglance.core.presentation.GlanceTheme
import com.ataglance.walletglance.core.presentation.components.buttons.PrimaryButton
import com.ataglance.walletglance.core.presentation.components.screenContainers.PreviewWithMainScaffoldContainer

@Composable
fun SetupFinishScreen(
    onFinishSetupButton: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = dimensionResource(R.dimen.screen_vertical_padding))
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Box {
            Icon(
                painter = painterResource(R.drawable.setup_finished_icon),
                tint = GlanceTheme.primary,
                contentDescription = "setup finished icon",
                modifier = Modifier
                    .size(200.dp)
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        PrimaryButton(text = stringResource(R.string._continue), onClick = onFinishSetupButton)
    }
}



@Preview(
    device = Devices.PIXEL_7_PRO
)
@Composable
fun SetupFinishScreenPreview(
    appTheme: AppTheme = AppTheme.LightDefault,
    isAppSetUp: Boolean = true
) {
    PreviewWithMainScaffoldContainer(
        appTheme = appTheme,
        anyScreenInHierarchyIsScreenProvider = { false },
        currentScreenIsScreenProvider = { false }
    ) {
        SetupFinishScreen(
            onFinishSetupButton = {}
        )
    }
}
